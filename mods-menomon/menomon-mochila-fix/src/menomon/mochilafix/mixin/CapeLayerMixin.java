package menomon.mochilafix.mixin;

import menomon.mochilafix.EstampaCapa;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_742;
import net.minecraft.class_972;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Capa vanilla (CapeFeatureRenderer): some com mochila do Inmis, como acontece com o elitro. */
@Mixin(value = class_972.class, remap = false)
public abstract class CapeLayerMixin {
    @Inject(method = "method_4177(Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;ILnet/minecraft/class_742;FFFFFF)V",
            at = @At("HEAD"), cancellable = true)
    private void menomon$semCapaComMochila(class_4587 m, class_4597 b, int luz, class_742 jogador,
                                          float a, float c, float d, float e, float f, float g, CallbackInfo ci) {
        if (EstampaCapa.usaMochilaInmis(jogador)) ci.cancel();
    }
}
