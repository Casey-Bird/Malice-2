package net.maven.malady.mixins.minecraft;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Minecraft.class)
public class TitleMixin {


    @Inject(method = "createTitle", at = @At("HEAD"), cancellable = true)
    private void overrideTitle(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue("Malice 2");
    }

}
