package menomon.mochilafix.mixin;

import menomon.mochilafix.Alcas;
import menomon.mochilafix.EstampaCapa;
import menomon.mochilafix.SbPonte;
import net.minecraft.class_1304;
import net.minecraft.class_1799;
import net.minecraft.class_7923;
import net.minecraft.class_3887;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_572;
import net.minecraft.class_583;
import net.minecraft.class_742;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Inmis 2.8.2 (draylar.inmis.client.BackpackFeature) desenha a mochila a partir da pose
 * base do jogador e imita o agachar com um giro fixo de 25 graus. Ele nunca aplica a
 * transformacao do torso (ModelPart body), entao nao acompanha nenhuma animacao que
 * mexa no torso: Punchy Moves, Emotecraft/playerAnimator ou o agachar real do vanilla.
 *
 * Conserto:
 *  1. logo depois do pushPose() do inmis, aplica body.rotate(matrices) (pivo + rotacao +
 *     escala animados do torso), igual o vanilla faz com armadura e o Punchy Moves faz com
 *     capa/elitro (LegacyTorsoAttachments.body);
 *  2. desenha as alcas (Alcas) nesse mesmo espaco do torso, entao elas tambem acompanham;
 *  3. desliga o giro fixo de agachar do inmis (a inclinacao real ja vem do torso);
 *  4. aproxima a mochila das costas (o inmis deixava ~1,25 px de vao).
 *
 * Nomes em intermediary: class_4587=MatrixStack, class_3887=FeatureRenderer,
 * method_17165=getContextModel, class_572=BipedEntityModel, field_3391=body,
 * class_630.method_22703=ModelPart.rotate, class_742.method_5715=isSneaking,
 * class_918.method_23178=ItemRenderer.renderItem, method_22904=MatrixStack.translate.
 */
@Mixin(targets = "draylar.inmis.client.BackpackFeature", remap = false)
public abstract class BackpackFeatureMixin {

    /** Quanto aproximar a mochila das costas, em blocos (0,9 px). Positivo = em direcao ao corpo. */
    @Unique
    private static final double MENOMON$APROXIMAR = 0.9 / 16.0;

    @Unique
    private boolean menomon$presoAoTorso;

    /**
     * Caminho principal (Sophisticated Backpacks instalado): desenha com o modelo de costas do
     * SB, igual ao Cobbleverse, mais as alcas, e cancela o desenho original do Inmis.
     */
    @Inject(
        method = "render(Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;ILnet/minecraft/class_742;FFFFFF)V",
        at = @At("HEAD"), cancellable = true
    )
    private void menomon$modeloSb(class_4587 matrices, class_4597 consumers, int light, class_742 player,
                                  float limbAngle, float limbDistance, float tickDelta, float animationProgress,
                                  float headYaw, float headPitch, CallbackInfo ci) {
        class_1799 mochila = player.method_6118(class_1304.field_6174);
        if (mochila.method_7960()) return;
        String ns = class_7923.field_41178.method_10221(mochila.method_7909()).method_12836();
        if (!"inmis".equals(ns)) return;
        class_583<?> modelo = ((class_3887<?, ?>) (Object) this).method_17165();
        if (!(modelo instanceof class_572<?> bipede)) return;
        if (!SbPonte.desenhar(modelo, player, matrices, consumers, light, mochila)) return;
        matrices.method_22903();
        bipede.field_3391.method_22703(matrices);
        Alcas.desenhar(matrices, consumers, light);
        EstampaCapa.desenhar(matrices, consumers, light, player);
        matrices.method_22909();
        ci.cancel();
    }

    @Inject(
        method = "render(Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;ILnet/minecraft/class_742;FFFFFF)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/class_4587;method_22903()V", shift = At.Shift.AFTER, ordinal = 0)
    )
    private void menomon$aplicarTorso(class_4587 matrices, class_4597 consumers, int light, class_742 player,
                                      float limbAngle, float limbDistance, float tickDelta, float animationProgress,
                                      float headYaw, float headPitch, CallbackInfo ci) {
        menomon$presoAoTorso = false;
        class_583<?> modelo = ((class_3887<?, ?>) (Object) this).method_17165();
        if (modelo instanceof class_572<?> bipede) {
            bipede.field_3391.method_22703(matrices);
            menomon$presoAoTorso = true;
            Alcas.desenhar(matrices, consumers, light);
        }
    }

    @Redirect(
        method = "render(Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;ILnet/minecraft/class_742;FFFFFF)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/class_742;method_5715()Z")
    )
    private boolean menomon$semGiroFixo(class_742 player) {
        // Preso ao torso: a inclinacao de agachar ja veio do body. Sem modelo bipede, comportamento original.
        return !menomon$presoAoTorso && player.method_5715();
    }

    @Inject(
        method = "render(Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;ILnet/minecraft/class_742;FFFFFF)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/class_918;method_23178(Lnet/minecraft/class_1799;Lnet/minecraft/class_811;IILnet/minecraft/class_4587;Lnet/minecraft/class_4597;Lnet/minecraft/class_1937;I)V")
    )
    private void menomon$aproximar(class_4587 matrices, class_4597 consumers, int light, class_742 player,
                                   float limbAngle, float limbDistance, float tickDelta, float animationProgress,
                                   float headYaw, float headPitch, CallbackInfo ci) {
        // Aqui o espaco ja esta girado 180 graus pelo inmis: a mochila fica em Z negativo e +Z aponta para o corpo.
        if (menomon$presoAoTorso) matrices.method_22904(0.0, 0.0, MENOMON$APROXIMAR);
    }
}
