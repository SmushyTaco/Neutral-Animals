package com.smushytaco.neutral_animals.mixins;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.smushytaco.neutral_animals.NeutralAnimals;
import com.smushytaco.neutral_animals.angerable_defaults.DefaultAngerable;
import com.smushytaco.neutral_animals.angerable_defaults.DefaultAngerableValues;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(Rabbit.class)
public abstract class RabbitEntityToNeutral extends Animal implements DefaultAngerable {
    protected RabbitEntityToNeutral(EntityType<? extends Animal> entityType, Level world) { super(entityType, world); }
    @Unique
    DefaultAngerableValues defaultAngerableValues = new DefaultAngerableValues();
    @NotNull
    @Override
    @SuppressWarnings("AddedMixinMembersNamePattern")
    public DefaultAngerableValues getDefaultAngerableValues() { return defaultAngerableValues; }
    @Inject(method = "registerGoals", at = @At("RETURN"))
    private void hookInitGoals(CallbackInfo ci) { if (NeutralAnimals.INSTANCE.getConfig().getRabbitsAreNeutral()) NeutralAnimals.INSTANCE.neutralAnimalGoalAndTargets(goalSelector, targetSelector, (Rabbit & DefaultAngerable) (Object) this); }
    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void hookWriteCustomData(ValueOutput view, CallbackInfo ci) { if (NeutralAnimals.INSTANCE.getConfig().getRabbitsAreNeutral()) addPersistentAngerSaveData(view); }
    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void hookReadCustomData(ValueInput view, CallbackInfo ci) { if (NeutralAnimals.INSTANCE.getConfig().getRabbitsAreNeutral()) readPersistentAngerSaveData(level(), view); }
    @Inject(method = "customServerAiStep", at = @At("HEAD"))
    protected void hookMobTick(CallbackInfo ci) { if (NeutralAnimals.INSTANCE.getConfig().getRabbitsAreNeutral()) NeutralAnimals.INSTANCE.mobTickLogic((Rabbit & DefaultAngerable) (Object) this); }
    @ModifyReturnValue(method = "createAttributes", at = @At("RETURN"))
    private static AttributeSupplier.Builder hookCreateRabbitAttributes(AttributeSupplier.Builder original) { return original.add(Attributes.ATTACK_DAMAGE); }
}