package com.smushytaco.neutral_animals.angerable_defaults
import net.minecraft.world.entity.EntityReference
import net.minecraft.world.entity.LivingEntity
data class DefaultAngerableValues(var entityReference: EntityReference<LivingEntity>? = null, var angerTime: Long = 0L, var angerPassingCooldown: Int = 0)