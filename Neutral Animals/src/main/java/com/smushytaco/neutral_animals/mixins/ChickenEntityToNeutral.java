package com.smushytaco.neutral_animals.mixins;
import com.smushytaco.neutral_animals.NeutralAnimals;
import com.smushytaco.neutral_animals.angerable_defaults.DefaultAngerable;
import com.smushytaco.neutral_animals.angerable_defaults.DefaultAngerableValues;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(ChickenEntity.class)
public abstract class ChickenEntityToNeutral extends AnimalEntity implements DefaultAngerable {
    DefaultAngerableValues defaultAngerableValues = new DefaultAngerableValues();
    protected ChickenEntityToNeutral(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }
    @Inject(method = "initGoals", at = @At("RETURN"))
    private void hookInitGoals(CallbackInfo ci) {
        NeutralAnimals.INSTANCE.neutralAnimalGoalAndTargets(goalSelector, targetSelector, (ChickenEntity & DefaultAngerable) (Object) this);
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
    @Override
    protected void mobTick() {
        NeutralAnimals.INSTANCE.mobTickLogic((ChickenEntity & DefaultAngerable) (Object) this);
        super.mobTick();
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