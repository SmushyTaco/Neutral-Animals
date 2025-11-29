package com.smushytaco.neutral_animals.angerable_defaults
import com.smushytaco.neutral_animals.NeutralAnimals.ANGER_TIME_RANGE
import com.smushytaco.neutral_animals.mixins.EntityRandomAccessor
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.NeutralMob
import java.util.*
interface DefaultAngerable: NeutralMob {
    val defaultAngerableValues: DefaultAngerableValues
    override fun getRemainingPersistentAngerTime() = defaultAngerableValues.angerTime
    override fun setRemainingPersistentAngerTime(ticks: Int) { defaultAngerableValues.angerTime = ticks }
    override fun getPersistentAngerTarget(): UUID? = defaultAngerableValues.targetUuid
    override fun setPersistentAngerTarget(uuid: UUID?) { defaultAngerableValues.targetUuid = uuid }
    override fun startPersistentAngerTimer() {
        if (this !is Entity) return
        remainingPersistentAngerTime = ANGER_TIME_RANGE.sample((this as EntityRandomAccessor).random)
    }
}