package com.smushytaco.neutral_animals.mixins;
import com.smushytaco.neutral_animals.NeutralAnimals;
import com.smushytaco.neutral_animals.angerable_defaults.DefaultAngerable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin extends PassiveEntity {
    protected AnimalEntityMixin(EntityType<? extends PassiveEntity> entityType, World world) { super(entityType, world); }
    @Inject(method = "mobTick", at = @At("HEAD"))
    @SuppressWarnings("all")
    private void hookMobTick(CallbackInfo ci) {
        AnimalEntity animalEntity = (AnimalEntity) (Object) this;
        if (animalEntity instanceof ChickenEntity && NeutralAnimals.INSTANCE.getConfig().getChickensAreNeutral()) NeutralAnimals.INSTANCE.mobTickLogic((ChickenEntity & DefaultAngerable) (Object) this);
        if (animalEntity instanceof CowEntity && NeutralAnimals.INSTANCE.getConfig().getCowsAreNeutral()) NeutralAnimals.INSTANCE.mobTickLogic((CowEntity & DefaultAngerable) (Object) this);
        if (animalEntity instanceof PigEntity && NeutralAnimals.INSTANCE.getConfig().getPigsAreNeutral()) NeutralAnimals.INSTANCE.mobTickLogic((PigEntity & DefaultAngerable) (Object) this);
    }
    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void hookWriteCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        AnimalEntity animalEntity = (AnimalEntity) (Object) this;
        if (!(animalEntity instanceof DefaultAngerable defaultAngerable)) return;
        if (animalEntity instanceof CowEntity && NeutralAnimals.INSTANCE.getConfig().getCowsAreNeutral()) defaultAngerable.writeAngerToNbt(nbt);
    }
    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void hookReadCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        AnimalEntity animalEntity = (AnimalEntity) (Object) this;
        if (!(animalEntity instanceof DefaultAngerable defaultAngerable)) return;
        if (animalEntity instanceof CowEntity && NeutralAnimals.INSTANCE.getConfig().getCowsAreNeutral()) defaultAngerable.readAngerFromNbt(getWorld(), nbt);
    }
}