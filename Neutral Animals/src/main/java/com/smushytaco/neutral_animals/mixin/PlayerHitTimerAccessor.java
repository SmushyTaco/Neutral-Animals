package com.smushytaco.neutral_animals.mixin;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
@Mixin(LivingEntity.class)
public interface PlayerHitTimerAccessor {
    @Accessor
    void setPlayerHitTimer(int value);
}