package com.smushytaco.neutral_animals.mixins;
import com.smushytaco.neutral_animals.NeutralAnimals;
import com.smushytaco.neutral_animals.angerable_defaults.DefaultAngerable;
import com.smushytaco.neutral_animals.angerable_defaults.DefaultAngerableValues;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(PigEntity.class)
public abstract class PigEntityToNeutral extends AnimalEntity implements DefaultAngerable {
    protected PigEntityToNeutral(EntityType<? extends AnimalEntity> entityType, World world) { super(entityType, world); }
    @Unique
    DefaultAngerableValues defaultAngerableValues = new DefaultAngerableValues();
    @NotNull
    @Override
    public DefaultAngerableValues getDefaultAngerableValues() { return defaultAngerableValues; }
    @Inject(method = "initGoals", at = @At("RETURN"))
    @SuppressWarnings("all")
    private void hookInitGoals(CallbackInfo ci) { if (NeutralAnimals.INSTANCE.getConfig().getPigsAreNeutral()) NeutralAnimals.INSTANCE.neutralAnimalGoalAndTargets(goalSelector, targetSelector, (PigEntity & DefaultAngerable) (Object) this); }
    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void hookWriteCustomDataToTag(NbtCompound nbt, CallbackInfo ci) { if (NeutralAnimals.INSTANCE.getConfig().getPigsAreNeutral()) writeAngerToNbt(nbt); }
    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void hookReadCustomDataFromTag(NbtCompound nbt, CallbackInfo ci) { if (NeutralAnimals.INSTANCE.getConfig().getPigsAreNeutral()) readAngerFromNbt(getWorld(), nbt); }
}