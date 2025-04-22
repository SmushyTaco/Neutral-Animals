package com.smushytaco.neutral_animals.angerable_defaults
import com.smushytaco.neutral_animals.NeutralAnimals.ANGER_TIME_RANGE
import com.smushytaco.neutral_animals.mixins.EntityRandomAccessor
import net.minecraft.entity.Entity
import net.minecraft.entity.mob.Angerable
import java.util.*
interface DefaultAngerable: Angerable {
    val defaultAngerableValues: DefaultAngerableValues
    override fun getAngerTime() = defaultAngerableValues.angerTime
    override fun setAngerTime(ticks: Int) { defaultAngerableValues.angerTime = ticks }
    override fun getAngryAt(): UUID? = defaultAngerableValues.targetUuid
    override fun setAngryAt(uuid: UUID?) { defaultAngerableValues.targetUuid = uuid }
    override fun chooseRandomAngerTime() {
        if (this !is Entity) return
        angerTime = ANGER_TIME_RANGE[(this as EntityRandomAccessor).random]
    }
}