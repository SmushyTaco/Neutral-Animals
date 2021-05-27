package com.smushytaco.neutral_animals
import com.smushytaco.neutral_animals.angerable_defaults.DefaultAngerable
import net.fabricmc.api.ModInitializer
import net.minecraft.entity.ai.Durations
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.IntRange
import java.util.*
object NeutralAnimals : ModInitializer {
    val RANDOM = Random()
    val ANGER_TIME_RANGE: IntRange = Durations.betweenSeconds(20, 39)
    fun neutralAnimalGoalAndTargets(goalSelector: GoalSelector, targetSelector: GoalSelector, angerable: DefaultAngerable) {
        if (angerable !is PathAwareEntity) return
        goalSelector.add(0, MeleeAttackGoal(angerable, 1.0, false))
        targetSelector.add(0, RevengeGoal(angerable).setGroupRevenge())
        targetSelector.add(0, FollowTargetGoal(angerable, PlayerEntity::class.java, 10, true, false, angerable::shouldAngerAt))
        targetSelector.add(0, UniversalAngerGoal(angerable, true))
    }
    override fun onInitialize() {}
}