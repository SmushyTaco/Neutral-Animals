package com.smushytaco.neutral_animals.mixins;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class DamageAttributes {
	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(EntityType<? extends LivingEntity> entityType, World world, CallbackInfo info) {
		LivingEntity livingEntity = (LivingEntity)(Object)this;
		AttributeContainer container = livingEntity.getAttributes();
		
		if(!container.hasAttribute(EntityAttributes.GENERIC_ATTACK_DAMAGE) && (livingEntity instanceof ChickenEntity || livingEntity instanceof CowEntity || livingEntity instanceof PigEntity || livingEntity instanceof RabbitEntity || livingEntity instanceof SheepEntity)) {
			((AttributeContainerAccess)container).getCustom().putIfAbsent(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE, (it) -> {}));
		}
	}
}