package com.smushytaco.neutral_animals.mixins;
import com.smushytaco.neutral_animals.NeutralAnimals;
import com.smushytaco.neutral_animals.angerable_defaults.DefaultAngerable;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(Animal.class)
public abstract class AnimalEntityMixin extends AgeableMob {
    protected AnimalEntityMixin(EntityType<? extends AgeableMob> entityType, Level world) { super(entityType, world); }
    @Inject(method = "customServerAiStep", at = @At("HEAD"))
    @SuppressWarnings("DataFlowIssue")
    private void hookMobTick(CallbackInfo ci) {
        Animal animalEntity = (Animal) (Object) this;
        if (animalEntity instanceof Chicken && NeutralAnimals.INSTANCE.getConfig().getChickensAreNeutral()) NeutralAnimals.INSTANCE.mobTickLogic((Chicken & DefaultAngerable) (Object) this);
        if (animalEntity instanceof Cow && NeutralAnimals.INSTANCE.getConfig().getCowsAreNeutral()) NeutralAnimals.INSTANCE.mobTickLogic((Cow & DefaultAngerable) (Object) this);
        if (animalEntity instanceof Pig && NeutralAnimals.INSTANCE.getConfig().getPigsAreNeutral()) NeutralAnimals.INSTANCE.mobTickLogic((Pig & DefaultAngerable) (Object) this);
    }
    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void hookWriteCustomData(ValueOutput view, CallbackInfo ci) {
        Animal animalEntity = (Animal) (Object) this;
        if (!(animalEntity instanceof DefaultAngerable defaultAngerable)) return;
        if (animalEntity instanceof Cow && NeutralAnimals.INSTANCE.getConfig().getCowsAreNeutral()) defaultAngerable.addPersistentAngerSaveData(view);
    }
    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void hookReadCustomData(ValueInput view, CallbackInfo ci) {
        Animal animalEntity = (Animal) (Object) this;
        if (!(animalEntity instanceof DefaultAngerable defaultAngerable)) return;
        if (animalEntity instanceof Cow && NeutralAnimals.INSTANCE.getConfig().getCowsAreNeutral()) defaultAngerable.readPersistentAngerSaveData(level(), view);
    }
}