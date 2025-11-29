package com.smushytaco.neutral_animals
import com.smushytaco.neutral_animals.angerable_defaults.DefaultAngerable
import com.smushytaco.neutral_animals.mixins.PlayerHitTimerAccessor
import net.fabricmc.api.ModInitializer
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.TimeUtil
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.entity.EntitySelector
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.attributes.AttributeInstance
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.GoalSelector
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.AABB
object NeutralAnimals : ModInitializer {
    const val MOD_ID = "neutral_animals"
    val config = ModConfig.createAndLoad()
    private val ATTACKING_SPEED_BOOST_IDENTIFIER = ResourceLocation.fromNamespaceAndPath(MOD_ID, "attacking_speed_boost")
    private val ATTACKING_SPEED_BOOST = AttributeModifier(ATTACKING_SPEED_BOOST_IDENTIFIER, 0.05, AttributeModifier.Operation.ADD_VALUE)
    val ANGER_TIME_RANGE: UniformInt = TimeUtil.rangeOfSeconds(20, 39)
    val ANGER_PASSING_COOLDOWN_RANGE: UniformInt = TimeUtil.rangeOfSeconds(4, 6)
    private fun <T: PathfinderMob> angerNearbyPathAwareEntities(pathAwareEntity: T) {
        val followRange = pathAwareEntity.getAttributeValue(Attributes.FOLLOW_RANGE)
        val box = AABB.unitCubeFromLowerCorner(pathAwareEntity.position()).inflate(followRange, 10.0, followRange)
        pathAwareEntity.level().getEntitiesOfClass(pathAwareEntity.javaClass, box, EntitySelector.NO_SPECTATORS).stream().filter { it !== pathAwareEntity }.filter { it.target == null }.filter { it.isAlliedTo(pathAwareEntity.target) }.forEach { it.target = pathAwareEntity.target }
    }
    private fun <T> tickAngerPassing(pathAwareEntity: T) where T : PathfinderMob, T : DefaultAngerable {
        if (pathAwareEntity.defaultAngerableValues.angerPassingCooldown > 0) {
            --pathAwareEntity.defaultAngerableValues.angerPassingCooldown
        } else {
            (pathAwareEntity as PathfinderMob).target?.let {
                if (pathAwareEntity.sensing.hasLineOfSight(it)) angerNearbyPathAwareEntities(pathAwareEntity)
            }
            pathAwareEntity.defaultAngerableValues.angerPassingCooldown =
                ANGER_PASSING_COOLDOWN_RANGE.sample(pathAwareEntity.random)
        }
    }
    fun <T> mobTickLogic(pathAwareEntity: T) where T : PathfinderMob, T : DefaultAngerable {
        val entityAttributeInstance: AttributeInstance = pathAwareEntity.getAttribute(Attributes.MOVEMENT_SPEED) ?: return
        if (pathAwareEntity.isAngry) {
            if (!entityAttributeInstance.hasModifier(ATTACKING_SPEED_BOOST_IDENTIFIER)) entityAttributeInstance.addTransientModifier(ATTACKING_SPEED_BOOST)
        } else if (entityAttributeInstance.hasModifier(ATTACKING_SPEED_BOOST_IDENTIFIER)) {
            entityAttributeInstance.removeModifier(ATTACKING_SPEED_BOOST)
        }
        pathAwareEntity.updatePersistentAnger(pathAwareEntity.level() as ServerLevel, true)
        if (pathAwareEntity.target != null) tickAngerPassing(pathAwareEntity)
        if (pathAwareEntity.isAngry) (pathAwareEntity as PlayerHitTimerAccessor).setLastHurtByPlayerMemoryTime(pathAwareEntity.tickCount)
    }
    fun <T> neutralAnimalGoalAndTargets(goalSelector: GoalSelector, targetSelector: GoalSelector, pathAwareEntity: T) where T : PathfinderMob, T: DefaultAngerable {
        goalSelector.addGoal(0, MeleeAttackGoal(pathAwareEntity, 1.0, false))
        targetSelector.addGoal(0, HurtByTargetGoal(pathAwareEntity).setAlertOthers())
        targetSelector.addGoal(0, NearestAttackableTargetGoal(pathAwareEntity, Player::class.java, 10, true, false, pathAwareEntity::isAngryAt))
        targetSelector.addGoal(0, ResetUniversalAngerTargetGoal(pathAwareEntity, true))
    }
    override fun onInitialize() {}
}