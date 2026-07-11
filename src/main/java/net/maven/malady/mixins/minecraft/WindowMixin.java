package net.maven.malady.mixins.minecraft;


import net.maven.malady.core.gui.WindowIconHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(Window.class)
public class WindowMixin {

    @Shadow
    private long window;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onWindowCreated(CallbackInfo ci) {
        WindowIconHelper.setWindowIcon(window, "/assets/malady/textures/gui/startup/favicon.png");
    }
}