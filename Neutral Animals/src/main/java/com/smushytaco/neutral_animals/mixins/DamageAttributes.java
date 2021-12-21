package com.smushytaco.neutral_animals.mixins;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(LivingEntity.class)
public abstract class DamageAttributes {
    @Shadow
    @Final
    private AttributeContainer attributes;
    @Inject(method = "getAttributes", at = @At("HEAD"))
    private void hookGetAttributes(CallbackInfoReturnable<AttributeContainer> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (attributes.hasAttribute(EntityAttributes.GENERIC_ATTACK_DAMAGE) || !(livingEntity instanceof ChickenEntity) && !(livingEntity instanceof CowEntity) && !(livingEntity instanceof PigEntity) && !(livingEntity instanceof RabbitEntity) && !(livingEntity instanceof SheepEntity)) return;
        ((AttributeContainerAccess) attributes).getCustom().putIfAbsent(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE, (it) -> {}));
    }
}