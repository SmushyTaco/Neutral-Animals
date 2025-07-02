package com.smushytaco.neutral_animals.mixins;
import com.smushytaco.neutral_animals.NeutralAnimals;
import com.smushytaco.neutral_animals.angerable_defaults.DefaultAngerable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.*;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin extends PassiveEntity {
    protected AnimalEntityMixin(EntityType<? extends PassiveEntity> entityType, World world) { super(entityType, world); }
    @Inject(method = "mobTick", at = @At("HEAD"))
    @SuppressWarnings("DataFlowIssue")
    private void hookMobTick(CallbackInfo ci) {
        AnimalEntity animalEntity = (AnimalEntity) (Object) this;
        if (animalEntity instanceof ChickenEntity && NeutralAnimals.INSTANCE.getConfig().getChickensAreNeutral()) NeutralAnimals.INSTANCE.mobTickLogic((ChickenEntity & DefaultAngerable) (Object) this);
        if (animalEntity instanceof CowEntity && NeutralAnimals.INSTANCE.getConfig().getCowsAreNeutral()) NeutralAnimals.INSTANCE.mobTickLogic((CowEntity & DefaultAngerable) (Object) this);
        if (animalEntity instanceof PigEntity && NeutralAnimals.INSTANCE.getConfig().getPigsAreNeutral()) NeutralAnimals.INSTANCE.mobTickLogic((PigEntity & DefaultAngerable) (Object) this);
    }
    @Inject(method = "writeCustomData", at = @At("RETURN"))
    private void hookWriteCustomData(WriteView view, CallbackInfo ci) {
        AnimalEntity animalEntity = (AnimalEntity) (Object) this;
        if (!(animalEntity instanceof DefaultAngerable defaultAngerable)) return;
        if (animalEntity instanceof CowEntity && NeutralAnimals.INSTANCE.getConfig().getCowsAreNeutral()) defaultAngerable.writeAngerToData(view);
    }
    @Inject(method = "readCustomData", at = @At("RETURN"))
    private void hookReadCustomData(ReadView view, CallbackInfo ci) {
        AnimalEntity animalEntity = (AnimalEntity) (Object) this;
        if (!(animalEntity instanceof DefaultAngerable defaultAngerable)) return;
        if (animalEntity instanceof CowEntity && NeutralAnimals.INSTANCE.getConfig().getCowsAreNeutral()) defaultAngerable.readAngerFromData(getWorld(), view);
    }
}