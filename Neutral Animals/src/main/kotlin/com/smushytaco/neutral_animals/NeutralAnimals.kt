package com.smushytaco.neutral_animals
import com.smushytaco.neutral_animals.angerable_defaults.DefaultAngerable
import com.smushytaco.neutral_animals.configuration_support.ModConfiguration
import com.smushytaco.neutral_animals.mixins.PlayerHitTimerAccessor
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer
import net.fabricmc.api.ModInitializer
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.EntityAttributeInstance
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.predicate.entity.EntityPredicates
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.TimeHelper
import net.minecraft.util.math.Box
import net.minecraft.util.math.intprovider.UniformIntProvider
import java.util.*
object NeutralAnimals : ModInitializer {
    const val MOD_ID = "neutral_animals"
    lateinit var config: ModConfiguration
        private set
    private val ATTACKING_SPEED_BOOST_ID = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718")
    private val ATTACKING_SPEED_BOOST = EntityAttributeModifier(ATTACKING_SPEED_BOOST_ID, "Attacking speed boost", 0.05, EntityAttributeModifier.Operation.ADDITION)
    val RANDOM = Random()
    val ANGER_TIME_RANGE: UniformIntProvider = TimeHelper.betweenSeconds(20, 39)
    val ANGER_PASSING_COOLDOWN_RANGE: UniformIntProvider = TimeHelper.betweenSeconds(4, 6)
    private fun <T: PathAwareEntity> angerNearbyPathAwareEntities(pathAwareEntity: T) {
        val followRange = pathAwareEntity.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE)
        val box = Box.from(pathAwareEntity.pos).expand(followRange, 10.0, followRange)
        pathAwareEntity.world.getEntitiesByClass(pathAwareEntity.javaClass, box, EntityPredicates.EXCEPT_SPECTATOR).stream()
            .filter { it !== pathAwareEntity }
            .filter { it.target == null }
            .filter { it.isTeammate(pathAwareEntity.target) }
            .forEach { it.target = pathAwareEntity.target }
    }
    private fun <T> tickAngerPassing(pathAwareEntity: T) where T : PathAwareEntity, T : DefaultAngerable {
        if (pathAwareEntity.defaultAngerableValues.angerPassingCooldown > 0) {
            --pathAwareEntity.defaultAngerableValues.angerPassingCooldown
        } else {
            if (pathAwareEntity.visibilityCache.canSee((pathAwareEntity as PathAwareEntity).target)) angerNearbyPathAwareEntities(
                pathAwareEntity
            )
            pathAwareEntity.defaultAngerableValues.angerPassingCooldown = ANGER_PASSING_COOLDOWN_RANGE.get(pathAwareEntity.random)
        }
    }
    fun <T> mobTickLogic(pathAwareEntity: T) where T : PathAwareEntity, T : DefaultAngerable {
        val entityAttributeInstance: EntityAttributeInstance = pathAwareEntity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED) ?: return
        if (pathAwareEntity.hasAngerTime()) {
            if (!entityAttributeInstance.hasModifier(ATTACKING_SPEED_BOOST)) {
                entityAttributeInstance.addTemporaryModifier(ATTACKING_SPEED_BOOST)
            }
        } else if (entityAttributeInstance.hasModifier(ATTACKING_SPEED_BOOST)) {
            entityAttributeInstance.removeModifier(ATTACKING_SPEED_BOOST)
        }
        pathAwareEntity.tickAngerLogic(pathAwareEntity.world as ServerWorld, true)
        if (pathAwareEntity.getTarget() != null) tickAngerPassing(pathAwareEntity)
        if (pathAwareEntity.hasAngerTime()) {
            (pathAwareEntity as PlayerHitTimerAccessor).setPlayerHitTimer(pathAwareEntity.age)
        }
    }
    fun <T> neutralAnimalGoalAndTargets(goalSelector: GoalSelector, targetSelector: GoalSelector, pathAwareEntity: T) where T : PathAwareEntity, T: DefaultAngerable {
        goalSelector.add(0, MeleeAttackGoal(pathAwareEntity, 1.0, false))
        targetSelector.add(0, RevengeGoal(pathAwareEntity).setGroupRevenge())
        targetSelector.add(0, ActiveTargetGoal(pathAwareEntity, PlayerEntity::class.java, 10, true, false, pathAwareEntity::shouldAngerAt))
        targetSelector.add(0, UniversalAngerGoal(pathAwareEntity, true))
    }
    override fun onInitialize() {
        AutoConfig.register(ModConfiguration::class.java) { definition: Config, configClass: Class<ModConfiguration> ->
            GsonConfigSerializer(definition, configClass)
        }
        config = AutoConfig.getConfigHolder(ModConfiguration::class.java).config
    }
}