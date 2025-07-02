package com.smushytaco.neutral_animals.mixins;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.smushytaco.neutral_animals.NeutralAnimals;
import com.smushytaco.neutral_animals.angerable_defaults.DefaultAngerable;
import com.smushytaco.neutral_animals.angerable_defaults.DefaultAngerableValues;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(ChickenEntity.class)
public abstract class ChickenEntityToNeutral extends AnimalEntity implements DefaultAngerable {
    protected ChickenEntityToNeutral(EntityType<? extends AnimalEntity> entityType, World world) { super(entityType, world); }
    @Unique
    DefaultAngerableValues defaultAngerableValues = new DefaultAngerableValues();
    @NotNull
    @Override
    @SuppressWarnings("AddedMixinMembersNamePattern")
    public DefaultAngerableValues getDefaultAngerableValues() { return defaultAngerableValues; }
    @Inject(method = "initGoals", at = @At("RETURN"))
    private void hookInitGoals(CallbackInfo ci) { if (NeutralAnimals.INSTANCE.getConfig().getChickensAreNeutral()) NeutralAnimals.INSTANCE.neutralAnimalGoalAndTargets(goalSelector, targetSelector, (ChickenEntity & DefaultAngerable) (Object) this); }
    @Inject(method = "writeCustomData", at = @At("RETURN"))
    private void hookWriteCustomData(WriteView view, CallbackInfo ci) { if (NeutralAnimals.INSTANCE.getConfig().getChickensAreNeutral()) writeAngerToData(view); }
    @Inject(method = "readCustomData", at = @At("RETURN"))
    private void hookReadCustomData(ReadView view, CallbackInfo ci) { if (NeutralAnimals.INSTANCE.getConfig().getChickensAreNeutral()) readAngerFromData(getWorld(), view); }
    @ModifyReturnValue(method = "createChickenAttributes", at = @At("RETURN"))
    private static DefaultAttributeContainer.Builder hookCreateChickenAttributes(DefaultAttributeContainer.Builder original) { return original.add(EntityAttributes.ATTACK_DAMAGE); }
}