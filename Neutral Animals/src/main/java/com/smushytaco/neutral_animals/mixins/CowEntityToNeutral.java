package com.smushytaco.neutral_animals.mixins;
import com.smushytaco.neutral_animals.NeutralAnimals;
import com.smushytaco.neutral_animals.angerable_defaults.DefaultAngerable;
import com.smushytaco.neutral_animals.angerable_defaults.DefaultAngerableValues;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(CowEntity.class)
public abstract class CowEntityToNeutral extends AnimalEntity implements DefaultAngerable {
    DefaultAngerableValues defaultAngerableValues = new DefaultAngerableValues();
    protected CowEntityToNeutral(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }
    @Inject(method = "initGoals", at = @At("RETURN"))
    private void hookInitGoals(CallbackInfo ci) {
        if (!NeutralAnimals.INSTANCE.getConfig().getCowsAreNeutral()) return;
        NeutralAnimals.INSTANCE.neutralAnimalGoalAndTargets(goalSelector, targetSelector, (CowEntity & DefaultAngerable) (Object) this);
    }
    @NotNull
    @Override
    public DefaultAngerableValues getDefaultAngerableValues() { return defaultAngerableValues; }
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (!NeutralAnimals.INSTANCE.getConfig().getCowsAreNeutral()) return;
        writeAngerToNbt(nbt);
    }
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (!NeutralAnimals.INSTANCE.getConfig().getCowsAreNeutral()) return;
        readAngerFromNbt(world, nbt);
    }
    @Override
    protected void mobTick() {
        if (NeutralAnimals.INSTANCE.getConfig().getCowsAreNeutral()) {
            NeutralAnimals.INSTANCE.mobTickLogic((CowEntity & DefaultAngerable) (Object) this);
        }
        super.mobTick();
    }
    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (NeutralAnimals.INSTANCE.getConfig().getCowsAreNeutral()) {
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