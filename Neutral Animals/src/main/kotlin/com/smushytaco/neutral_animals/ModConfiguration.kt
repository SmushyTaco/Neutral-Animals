package com.smushytaco.neutral_animals
import io.wispforest.owo.config.annotation.Config
import io.wispforest.owo.config.annotation.Modmenu
@Modmenu(modId = NeutralAnimals.MOD_ID)
@Config(name = NeutralAnimals.MOD_ID, wrapperName = "ModConfig")
@Suppress("UNUSED")
class ModConfiguration {
    @JvmField
    var chickensAreNeutral = true
    @JvmField
    var cowsAreNeutral = true
    @JvmField
    var pigsAreNeutral = true
    @JvmField
    var rabbitsAreNeutral = true
    @JvmField
    var sheepAreNeutral = true
    @JvmField
    var villagersAreNeutral = false
}