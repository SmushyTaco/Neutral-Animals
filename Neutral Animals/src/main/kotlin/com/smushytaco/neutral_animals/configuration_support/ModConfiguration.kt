package com.smushytaco.neutral_animals.configuration_support
import com.smushytaco.neutral_animals.NeutralAnimals
import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment
@Config(name = NeutralAnimals.MOD_ID)
class ModConfiguration: ConfigData {
    @Comment("Default value is yes. If set to yes chickens will be neutral. If set to no they won't. After modifying this value if you're currently in a world you'll need to relaunch the world for things to take effect.")
    val chickensAreNeutral = true
    @Comment("Default value is yes. If set to yes cows will be neutral. If set to no they won't. After modifying this value if you're currently in a world you'll need to relaunch the world for things to take effect.")
    val cowsAreNeutral = true
    @Comment("Default value is yes. If set to yes pigs will be neutral. If set to no they won't. After modifying this value if you're currently in a world you'll need to relaunch the world for things to take effect.")
    val pigsAreNeutral = true
    @Comment("Default value is yes. If set to yes rabbits will be neutral. If set to no they won't. After modifying this value if you're currently in a world you'll need to relaunch the world for things to take effect.")
    val rabbitsAreNeutral = true
    @Comment("Default value is yes. If set to yes sheep will be neutral. If set to no they won't. After modifying this value if you're currently in a world you'll need to relaunch the world for things to take effect.")
    val sheepAreNeutral = true
    @Comment("Default value is no. If set to yes villagers will be neutral. If set to no they won't. After modifying this value if you're currently in a world you'll need to relaunch the world for things to take effect.")
    val villagersAreNeutral = false
}