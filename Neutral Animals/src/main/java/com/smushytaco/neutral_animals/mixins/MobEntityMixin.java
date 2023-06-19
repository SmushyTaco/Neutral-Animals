package com.smushytaco.neutral_animals.mixins;
import com.smushytaco.neutral_animals.NeutralAnimals;
import com.smushytaco.neutral_animals.angerable_defaults.DefaultAngerable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) { super(entityType, world); }
    @Shadow
    @Nullable
    public abstract LivingEntity getTarget();
    @Shadow
    @Final
    protected GoalSelector goalSelector;
    @Shadow
    @Final
    protected GoalSelector targetSelector;

    @Inject(method = "setTarget", at = @At("HEAD"))
    private void hookSetTarget(@Nullable LivingEntity target, CallbackInfo ci) {
        MobEntity mobEntity = (MobEntity) (Object) this;
        if (!(mobEntity instanceof DefaultAngerable defaultAngerable)) return;
        if (mobEntity instanceof ChickenEntity && NeutralAnimals.INSTANCE.getConfig().getChickensAreNeutral() || mobEntity instanceof CowEntity && NeutralAnimals.INSTANCE.getConfig().getCowsAreNeutral() || mobEntity instanceof PigEntity && NeutralAnimals.INSTANCE.getConfig().getPigsAreNeutral() || mobEntity instanceof RabbitEntity && NeutralAnimals.INSTANCE.getConfig().getRabbitsAreNeutral() || mobEntity instanceof SheepEntity && NeutralAnimals.INSTANCE.getConfig().getSheepAreNeutral() || mobEntity instanceof VillagerEntity && NeutralAnimals.INSTANCE.getConfig().getVillagersAreNeutral()) {
            if (getTarget() == null && target != null) defaultAngerable.getDefaultAngerableValues().setAngerPassingCooldown(NeutralAnimals.INSTANCE.getANGER_PASSING_COOLDOWN_RANGE().get(random));
            if (target instanceof PlayerEntity playerEntity) setAttacking(playerEntity);
        }
    }
    @Inject(method = "initGoals", at = @At("RETURN"))
    @SuppressWarnings("ConstantConditions")
    private void hookInitGoals(CallbackInfo ci) {
        if ((MobEntity) (Object) this instanceof VillagerEntity && NeutralAnimals.INSTANCE.getConfig().getVillagersAreNeutral()) NeutralAnimals.INSTANCE.neutralAnimalGoalAndTargets(goalSelector, targetSelector, (VillagerEntity & DefaultAngerable) (Object) this);
    }
}