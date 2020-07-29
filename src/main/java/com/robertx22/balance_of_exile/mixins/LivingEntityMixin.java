package com.robertx22.balance_of_exile.mixins;

import com.robertx22.balance_of_exile.mixin_methods.OnDropLoot;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "dropLoot", at = @At(value = "HEAD"), cancellable = true)
    private void onKeyPriority(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        OnDropLoot.tryCancel(entity, ci);
    }

}
