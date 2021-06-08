package com.smushytaco.neutral_animals.mixins;
import com.smushytaco.neutral_animals.NeutralAnimals;
import com.smushytaco.neutral_animals.angerable_defaults.DefaultAngerable;
import com.smushytaco.neutral_animals.angerable_defaults.DefaultAngerableValues;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(RabbitEntity.class)
public abstract class RabbitEntityToNeutral extends AnimalEntity implements DefaultAngerable {
    DefaultAngerableValues defaultAngerableValues = new DefaultAngerableValues();
    protected RabbitEntityToNeutral(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }
    @Inject(method = "initGoals", at = @At("RETURN"))
    private void hookInitGoals(CallbackInfo ci) {
        NeutralAnimals.INSTANCE.neutralAnimalGoalAndTargets(goalSelector, targetSelector, this);
    }
    @Inject(method = "createRabbitAttributes", at = @At("HEAD"), cancellable = true)
    private static void hookCreateRabbitAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.setReturnValue(MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 3.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.30000001192092896D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0D));
    }
    @NotNull
    @Override
    public DefaultAngerableValues getDefaultAngerableValues() { return defaultAngerableValues; }
    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void hookWriteCustomDataToTag(NbtCompound nbt, CallbackInfo ci) {
        writeAngerToNbt(nbt);
    }
    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void hookReadCustomDataFromTag(NbtCompound nbt, CallbackInfo ci) {
        readAngerFromNbt(world, nbt);
    }
    @Inject(method = "mobTick", at = @At("HEAD"))
    protected void hookMobTick(CallbackInfo ci) {
        NeutralAnimals.INSTANCE.mobTickLogic(this);
    }
    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (getTarget() == null && target != null) {
            defaultAngerableValues.setAngerPassingCooldown(NeutralAnimals.INSTANCE.getANGER_PASSING_COOLDOWN_RANGE().get(random));
        }
        if (target instanceof PlayerEntity) {
            setAttacking((PlayerEntity) target);
        }
        super.setTarget(target);
    }
}