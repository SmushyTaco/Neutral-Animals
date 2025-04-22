package com.smushytaco.neutral_animals.mixins;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.smushytaco.neutral_animals.NeutralAnimals;
import com.smushytaco.neutral_animals.angerable_defaults.DefaultAngerable;
import com.smushytaco.neutral_animals.angerable_defaults.DefaultAngerableValues;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(VillagerEntity.class)
public abstract class VillagerEntityToNeutral extends MerchantEntity implements DefaultAngerable {
    protected VillagerEntityToNeutral(EntityType<? extends MerchantEntity> entityType, World world) { super(entityType, world); }
    @Unique
    DefaultAngerableValues defaultAngerableValues = new DefaultAngerableValues();
    @NotNull
    @Override
    @SuppressWarnings("AddedMixinMembersNamePattern")
    public DefaultAngerableValues getDefaultAngerableValues() { return defaultAngerableValues; }
    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void hookWriteCustomDataToTag(NbtCompound nbt, CallbackInfo ci) { if (NeutralAnimals.INSTANCE.getConfig().getVillagersAreNeutral()) writeAngerToNbt(nbt); }
    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void hookReadCustomDataFromTag(NbtCompound nbt, CallbackInfo ci) { if (NeutralAnimals.INSTANCE.getConfig().getVillagersAreNeutral()) readAngerFromNbt(getWorld(), nbt); }
    @Inject(method = "mobTick", at = @At("HEAD"))
    private void mobTick(CallbackInfo ci) { if (NeutralAnimals.INSTANCE.getConfig().getVillagersAreNeutral()) NeutralAnimals.INSTANCE.mobTickLogic((VillagerEntity & DefaultAngerable) (Object) this); }
    @ModifyReturnValue(method = "createVillagerAttributes", at = @At("RETURN"))
    private static DefaultAttributeContainer.Builder hookCreateVillagerAttributes(DefaultAttributeContainer.Builder original) { return original.add(EntityAttributes.ATTACK_DAMAGE); }
}