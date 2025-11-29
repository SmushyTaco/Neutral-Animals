package com.smushytaco.neutral_animals.mixins;
import com.smushytaco.neutral_animals.NeutralAnimals;
import com.smushytaco.neutral_animals.angerable_defaults.DefaultAngerable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(Mob.class)
public abstract class MobEntityMixin extends LivingEntity {
    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) { super(entityType, world); }
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
        Mob mobEntity = (Mob) (Object) this;
        if (!(mobEntity instanceof DefaultAngerable defaultAngerable)) return;
        if (mobEntity instanceof Chicken && NeutralAnimals.INSTANCE.getConfig().getChickensAreNeutral() || mobEntity instanceof Cow && NeutralAnimals.INSTANCE.getConfig().getCowsAreNeutral() || mobEntity instanceof Pig && NeutralAnimals.INSTANCE.getConfig().getPigsAreNeutral() || mobEntity instanceof Rabbit && NeutralAnimals.INSTANCE.getConfig().getRabbitsAreNeutral() || mobEntity instanceof Sheep && NeutralAnimals.INSTANCE.getConfig().getSheepAreNeutral() || mobEntity instanceof Villager && NeutralAnimals.INSTANCE.getConfig().getVillagersAreNeutral()) {
            if (getTarget() == null && target != null) defaultAngerable.getDefaultAngerableValues().setAngerPassingCooldown(NeutralAnimals.INSTANCE.getANGER_PASSING_COOLDOWN_RANGE().sample(random));
            if (target instanceof Player playerEntity) setLastHurtByPlayer(playerEntity, 100);
        }
    }
    @Inject(method = "registerGoals", at = @At("RETURN"))
    private void hookInitGoals(CallbackInfo ci) {
        if ((Mob) (Object) this instanceof Villager && NeutralAnimals.INSTANCE.getConfig().getVillagersAreNeutral()) NeutralAnimals.INSTANCE.neutralAnimalGoalAndTargets(goalSelector, targetSelector, (Villager & DefaultAngerable) (Object) this);
    }
}