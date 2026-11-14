package menomon.mochilafix;

import net.minecraft.class_1921;
import net.minecraft.class_2960;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_4608;
import net.minecraft.class_5603;
import net.minecraft.class_5606;
import net.minecraft.class_5607;
import net.minecraft.class_5609;
import net.minecraft.class_5610;
import net.minecraft.class_630;

/**
 * Alcas da mochila, desenhadas no espaco do torso (body.rotate ja aplicado), em pixels:
 * torso vai de x -4..4, y 0..12 (y para baixo), z -2..2 (frente = z negativo);
 * a camada da jaqueta fica em +-2,25. As alcas ficam logo a frente dela.
 *
 * Intermediary: class_5609=ModelData, class_5610=ModelPartData, class_5606=ModelPartBuilder,
 * class_5603=ModelTransform, class_5607=TexturedModelData, class_630=ModelPart,
 * class_1921=RenderLayer, class_4608=OverlayTexture, class_2960=Identifier.
 */
public final class Alcas {
    private static final class_2960 TEXTURA = class_2960.method_60655("menomon_mochila_fix", "textures/entity/alcas.png");
    private static class_630 modelo;

    private Alcas() {}

    private static class_630 modelo() {
        if (modelo != null) return modelo;
        class_5609 dados = new class_5609();
        class_5610 raiz = dados.method_32111();
        float zf = -2.55f, esp = 0.25f;           // frente: z -2,55..-2,30
        for (int lado = -1; lado <= 1; lado += 2) {
            float x0 = lado < 0 ? -3.25f : 1.75f;  // tiras de 1,5 px
            String n = lado < 0 ? "esq" : "dir";
            // tira da frente, do ombro ate a cintura
            raiz.method_32117("frente_" + n, class_5606.method_32108().method_32101(0, 0)
                .method_32097(x0, -0.3f, zf, 1.5f, 9.3f, esp), class_5603.field_27701);
            // por cima do ombro ate a mochila
            raiz.method_32117("ombro_" + n, class_5606.method_32108().method_32101(0, 0)
                .method_32097(x0, -0.3f, zf, 1.5f, 0.3f, 5.9f), class_5603.field_27701);
            // descida curta nas costas (encontra a mochila)
            raiz.method_32117("costas_" + n, class_5606.method_32108().method_32101(0, 0)
                .method_32097(x0, -0.3f, 2.3f, 1.5f, 3.3f, esp), class_5603.field_27701);
            // fivela de metal
            raiz.method_32117("fivela_" + n, class_5606.method_32108().method_32101(16, 0)
                .method_32097(x0 + 0.25f, 5.6f, zf - 0.2f, 1.0f, 1.0f, 0.2f), class_5603.field_27701);
        }
        // tira peitoral ligando as duas alcas
        raiz.method_32117("peito", class_5606.method_32108().method_32101(0, 16)
            .method_32097(-1.75f, 3.4f, zf - 0.1f, 3.5f, 0.8f, esp), class_5603.field_27701);
        modelo = class_5607.method_32110(dados, 32, 32).method_32109();
        return modelo;
    }

    public static void desenhar(class_4587 matrizes, class_4597 buffers, int luz) {
        modelo().method_22698(matrizes, buffers.getBuffer(class_1921.method_23578(TEXTURA)), luz, class_4608.field_21444);
    }
}
