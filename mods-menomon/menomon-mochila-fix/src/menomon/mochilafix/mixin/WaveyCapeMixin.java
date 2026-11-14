package menomon.mochilafix.mixin;

import menomon.mochilafix.EstampaCapa;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_742;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Capa do WaveyCapes (substitui a camada vanilla): some com mochila do Inmis. Ignorado sem o WaveyCapes. */
@Pseudo
@Mixin(targets = "dev.tr7zw.waveycapes.renderlayers.CustomCapeRenderLayer", remap = false)
public abstract class WaveyCapeMixin {
    @Inject(method = "render(Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;ILnet/minecraft/class_742;FFFFFF)V",
            at = @At("HEAD"), cancellable = true, require = 0)
    private void menomon$semCapaComMochila(class_4587 m, class_4597 b, int luz, class_742 jogador,
                                          float a, float c, float d, float e, float f, float g, CallbackInfo ci) {
        if (EstampaCapa.usaMochilaInmis(jogador)) ci.cancel();
    }
}
