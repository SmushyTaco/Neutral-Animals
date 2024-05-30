package com.smushytaco.neutral_animals.mixins;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.smushytaco.neutral_animals.NeutralAnimals;
import com.smushytaco.neutral_animals.angerable_defaults.DefaultAngerable;
import com.smushytaco.neutral_animals.angerable_defaults.DefaultAngerableValues;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(RabbitEntity.class)
public abstract class RabbitEntityToNeutral extends AnimalEntity implements DefaultAngerable {
    protected RabbitEntityToNeutral(EntityType<? extends AnimalEntity> entityType, World world) { super(entityType, world); }
    @Unique
    DefaultAngerableValues defaultAngerableValues = new DefaultAngerableValues();
    @NotNull
    @Override
    public DefaultAngerableValues getDefaultAngerableValues() { return defaultAngerableValues; }
    @Inject(method = "initGoals", at = @At("RETURN"))
    private void hookInitGoals(CallbackInfo ci) { if (NeutralAnimals.INSTANCE.getConfig().getRabbitsAreNeutral()) NeutralAnimals.INSTANCE.neutralAnimalGoalAndTargets(goalSelector, targetSelector, (RabbitEntity & DefaultAngerable) (Object) this); }
    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void hookWriteCustomDataToTag(NbtCompound nbt, CallbackInfo ci) { if (NeutralAnimals.INSTANCE.getConfig().getRabbitsAreNeutral()) writeAngerToNbt(nbt); }
    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void hookReadCustomDataFromTag(NbtCompound nbt, CallbackInfo ci) { if (NeutralAnimals.INSTANCE.getConfig().getRabbitsAreNeutral()) readAngerFromNbt(getWorld(), nbt); }
    @Inject(method = "mobTick", at = @At("HEAD"))
    protected void hookMobTick(CallbackInfo ci) { if (NeutralAnimals.INSTANCE.getConfig().getRabbitsAreNeutral()) NeutralAnimals.INSTANCE.mobTickLogic((RabbitEntity & DefaultAngerable) (Object) this); }
    @ModifyReturnValue(method = "createRabbitAttributes", at = @At("RETURN"))
    private static DefaultAttributeContainer.Builder hookCreateRabbitAttributes(DefaultAttributeContainer.Builder original) { return original.add(EntityAttributes.GENERIC_ATTACK_DAMAGE); }
}