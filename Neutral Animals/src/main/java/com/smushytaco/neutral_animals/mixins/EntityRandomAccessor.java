package com.smushytaco.neutral_animals.mixins;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
@Mixin(Entity.class)
public interface EntityRandomAccessor {
    @Accessor
    Random getRandom();
}