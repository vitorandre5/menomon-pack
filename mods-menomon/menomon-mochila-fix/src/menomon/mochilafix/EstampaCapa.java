package menomon.mochilafix;

import net.minecraft.class_1304;
import net.minecraft.class_1664;
import net.minecraft.class_1799;
import net.minecraft.class_1921;
import net.minecraft.class_2960;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_4608;
import net.minecraft.class_5603;
import net.minecraft.class_5606;
import net.minecraft.class_5607;
import net.minecraft.class_5609;
import net.minecraft.class_630;
import net.minecraft.class_742;
import net.minecraft.class_7833;
import net.minecraft.class_7923;

/**
 * Capa + mochila: igual ao elitro do vanilla, a capa deixa de ser desenhada e a arte dela
 * e "absorvida" pela mochila, estampada na face externa do corpo do modelo do SB.
 *
 * Espaco usado: torso (body.rotate ja aplicado), em pixels, y para baixo, costas = +z.
 * O SB (BackpackModel.translateRotateAndScale para jogador adulto sem armadura) gira 180 em Y e
 * desloca (0, -0.75, -0.3); com o pivo do modelo em y=24, o corpo principal (10x13x6 px) fica em
 * x -5..5, y -1..12 e z 1,8..7,8. A estampa fica logo atras da face externa (z = 7,8).
 */
public final class EstampaCapa {
    private static class_630 painel;

    private EstampaCapa() {}

    /** true se o jogador esta com uma mochila do Inmis no peito. */
    public static boolean usaMochilaInmis(class_742 jogador) {
        class_1799 peito = jogador.method_6118(class_1304.field_6174);
        return !peito.method_7960() && "inmis".equals(class_7923.field_41178.method_10221(peito.method_7909()).method_12836());
    }

    private static class_630 painel() {
        if (painel != null) return painel;
        // mesmo recorte de UV da capa vanilla (uv 0,0 numa textura 64x32): a face "norte" e a arte de fora
        class_5609 dados = new class_5609();
        dados.method_32111().method_32117("capa", class_5606.method_32108().method_32101(0, 0)
            .method_32097(-5.0f, 0.0f, -0.01f, 10.0f, 16.0f, 0.01f), class_5603.field_27701);
        painel = class_5607.method_32110(dados, 64, 32).method_32109();
        return painel;
    }

    public static void desenhar(class_4587 matrizes, class_4597 buffers, int luz, class_742 jogador) {
        if (!jogador.method_7348(class_1664.field_7559)) return;
        class_2960 textura = jogador.method_52814().comp_1627();
        if (textura == null) return;
        matrizes.method_22903();
        matrizes.method_46416(0.0f, -1.0f / 16.0f, 7.86f / 16.0f);
        matrizes.method_22907(class_7833.field_40716.rotationDegrees(180.0f)); // face com a arte para fora
        matrizes.method_22905(1.0f, 13.0f / 16.0f, 1.0f);                        // 16 px da capa em 13 px de mochila
        painel().method_22698(matrizes, buffers.getBuffer(class_1921.method_23578(textura)), luz, class_4608.field_21444);
        matrizes.method_22909();
    }
}
