package com.smushytaco.neutral_animals
import com.smushytaco.neutral_animals.angerable_defaults.DefaultAngerable
import com.smushytaco.neutral_animals.mixin.PlayerHitTimerAccessor
import net.fabricmc.api.ModInitializer
import net.minecraft.entity.ai.Durations
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.EntityAttributeInstance
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Box
import net.minecraft.util.math.IntRange
import java.util.*
object NeutralAnimals : ModInitializer {
    private val ATTACKING_SPEED_BOOST_ID = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718")
    val ATTACKING_SPEED_BOOST = EntityAttributeModifier(ATTACKING_SPEED_BOOST_ID, "Attacking speed boost", 0.05, EntityAttributeModifier.Operation.ADDITION)
    val RANDOM = Random()
    val ANGER_TIME_RANGE: IntRange = Durations.betweenSeconds(20, 39)
    val FIELD_25609: IntRange = Durations.betweenSeconds(4, 6)
    fun method_29942(pathAwareEntity: PathAwareEntity) {
        val d: Double = pathAwareEntity.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE)
        val box = Box.method_29968(pathAwareEntity.pos).expand(d, 10.0, d)
        pathAwareEntity.world.getEntitiesIncludingUngeneratedChunks(PathAwareEntity::class.java, box).stream()
            .filter { pathAwareEntityTwo -> pathAwareEntityTwo !== pathAwareEntity }
            .filter { pathAwareEntityTwo -> pathAwareEntityTwo.target == null }
            .filter { pathAwareEntityTwo -> !pathAwareEntityTwo.isTeammate(pathAwareEntity.target) }
            .forEach { pathAwareEntityTwo -> pathAwareEntityTwo.target = pathAwareEntity.target }
    }
    fun method_29941(defaultAngerable: DefaultAngerable) {
        if (defaultAngerable !is PathAwareEntity) return
        if (defaultAngerable.defaultAngerableValues.field_25608 > 0) {
            --defaultAngerable.defaultAngerableValues.field_25608
        } else {
            if (defaultAngerable.visibilityCache.canSee((defaultAngerable as PathAwareEntity).target)) method_29942(defaultAngerable)
            defaultAngerable.defaultAngerableValues.field_25608 = FIELD_25609.choose(defaultAngerable.random)
        }
    }
    fun mobTickLogic(defaultAngerable: DefaultAngerable) {
        if (defaultAngerable !is PathAwareEntity) return
        val entityAttributeInstance: EntityAttributeInstance = defaultAngerable.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED) ?: return
        if (defaultAngerable.hasAngerTime()) {
            if (!entityAttributeInstance.hasModifier(ATTACKING_SPEED_BOOST)) {
                entityAttributeInstance.addTemporaryModifier(ATTACKING_SPEED_BOOST)
            }
        } else if (entityAttributeInstance.hasModifier(ATTACKING_SPEED_BOOST)) {
            entityAttributeInstance.removeModifier(ATTACKING_SPEED_BOOST)
        }
        defaultAngerable.tickAngerLogic(defaultAngerable.world as ServerWorld?, true)
        if (defaultAngerable.getTarget() != null) method_29941(defaultAngerable)
        if (defaultAngerable.hasAngerTime()) {
            (defaultAngerable as PlayerHitTimerAccessor).setPlayerHitTimer(defaultAngerable.age)
        }
    }
    fun neutralAnimalGoalAndTargets(goalSelector: GoalSelector, targetSelector: GoalSelector, defaultAngerable: DefaultAngerable) {
        if (defaultAngerable !is PathAwareEntity) return
        goalSelector.add(0, MeleeAttackGoal(defaultAngerable, 1.0, false))
        targetSelector.add(0, RevengeGoal(defaultAngerable).setGroupRevenge())
        targetSelector.add(0, FollowTargetGoal(defaultAngerable, PlayerEntity::class.java, 10, true, false, defaultAngerable::shouldAngerAt))
        targetSelector.add(0, UniversalAngerGoal(defaultAngerable, true))
    }
    override fun onInitialize() {}
}