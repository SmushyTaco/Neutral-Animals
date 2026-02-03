package com.smushytaco.neutral_animals.angerable_defaults
import com.smushytaco.neutral_animals.NeutralAnimals.ANGER_TIME_RANGE
import com.smushytaco.neutral_animals.mixins.EntityRandomAccessor
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityReference
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.NeutralMob
interface DefaultAngerable: NeutralMob {
    val defaultAngerableValues: DefaultAngerableValues
    override fun getPersistentAngerEndTime() = defaultAngerableValues.angerTime
    override fun setPersistentAngerEndTime(ticks: Long) { defaultAngerableValues.angerTime = ticks }
    override fun getPersistentAngerTarget(): EntityReference<LivingEntity>? = defaultAngerableValues.entityReference
    override fun setPersistentAngerTarget(uuid: EntityReference<LivingEntity>?) { defaultAngerableValues.entityReference = uuid }
    override fun startPersistentAngerTimer() {
        if (this !is Entity) return
        setTimeToRemainAngry(ANGER_TIME_RANGE.sample((this as EntityRandomAccessor).random).toLong())
    }
}