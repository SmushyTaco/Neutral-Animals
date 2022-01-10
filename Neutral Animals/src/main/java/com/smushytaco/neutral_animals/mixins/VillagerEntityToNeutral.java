package com.smushytaco.neutral_animals.mixins;
import com.smushytaco.neutral_animals.NeutralAnimals;
import com.smushytaco.neutral_animals.angerable_defaults.DefaultAngerable;
import com.smushytaco.neutral_animals.angerable_defaults.DefaultAngerableValues;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(VillagerEntity.class)
public abstract class VillagerEntityToNeutral extends MerchantEntity implements DefaultAngerable {
    DefaultAngerableValues defaultAngerableValues = new DefaultAngerableValues();
    public VillagerEntityToNeutral(EntityType<? extends MerchantEntity> entityType, World world) {
        super(entityType, world);
    }
    @Override
    @SuppressWarnings("all")
    protected void initGoals() {
        if (!NeutralAnimals.INSTANCE.getConfig().getVillagersAreNeutral()) return;
        NeutralAnimals.INSTANCE.neutralAnimalGoalAndTargets(goalSelector, targetSelector, (VillagerEntity & DefaultAngerable) (Object) this);
    }
    @NotNull
    @Override
    public DefaultAngerableValues getDefaultAngerableValues() { return defaultAngerableValues; }
    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void hookWriteCustomDataToTag(NbtCompound nbt, CallbackInfo ci) {
        if (!NeutralAnimals.INSTANCE.getConfig().getVillagersAreNeutral()) return;
        writeAngerToNbt(nbt);
    }
    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void hookReadCustomDataFromTag(NbtCompound nbt, CallbackInfo ci) {
        if (!NeutralAnimals.INSTANCE.getConfig().getVillagersAreNeutral()) return;
        readAngerFromNbt(world, nbt);
    }
    @Inject(method = "mobTick", at = @At("HEAD"))
    @SuppressWarnings("all")
    private void mobTick(CallbackInfo ci) {
        if (NeutralAnimals.INSTANCE.getConfig().getVillagersAreNeutral()) {
            NeutralAnimals.INSTANCE.mobTickLogic((VillagerEntity & DefaultAngerable) (Object) this);
        }
        super.mobTick();
    }
    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (NeutralAnimals.INSTANCE.getConfig().getVillagersAreNeutral()) {
            if (getTarget() == null && target != null) {
                defaultAngerableValues.setAngerPassingCooldown(NeutralAnimals.INSTANCE.getANGER_PASSING_COOLDOWN_RANGE().get(random));
            }
            if (target instanceof PlayerEntity) {
                setAttacking((PlayerEntity) target);
            }
        }
        super.setTarget(target);
    }
}