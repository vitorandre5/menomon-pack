package menomon.mochilafix;

import net.minecraft.class_1309;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_2960;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_583;
import net.minecraft.class_7923;
import net.minecraft.class_9282;
import net.minecraft.class_9334;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Desenha a mochila do Inmis com o modelo de costas do Sophisticated Backpacks (o mesmo do
 * Cobbleverse), usando a rotina publica do proprio SB:
 *   BackpackLayerRenderer.renderBackpack(modeloPai, entidade, matrizes, buffers, luz, stack, usaArmadura, modelo)
 * que ja prende o modelo ao torso animado (BackpackModel.translateRotateAndScale chama body.rotate).
 *
 * Tudo por reflexao: se o SB nao estiver instalado ou mudar a API, desliga sozinho e o
 * mixin volta a desenhar o item 3D do Inmis (tambem preso ao torso).
 */
public final class SbPonte {
    private static boolean iniciado, disponivel;
    private static Method renderBackpack, getBackpackModel, setColors;
    private static final Map<String, Supplier<?>> NIVEIS = new HashMap<>();
    private static final Map<String, class_1799> STACKS = new HashMap<>();

    private SbPonte() {}

    private static void iniciar() {
        iniciado = true;
        try {
            Class<?> camada = Class.forName("net.p3pp3rf1y.sophisticatedbackpacks.client.render.BackpackLayerRenderer");
            Class<?> imodelo = Class.forName("net.p3pp3rf1y.sophisticatedbackpacks.client.render.IBackpackModel");
            Class<?> gerente = Class.forName("net.p3pp3rf1y.sophisticatedbackpacks.client.render.BackpackModelManager");
            Class<?> itens = Class.forName("net.p3pp3rf1y.sophisticatedbackpacks.init.ModItems");
            Class<?> item = Class.forName("net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem");
            renderBackpack = camada.getMethod("renderBackpack", class_583.class, class_1309.class, class_4587.class,
                class_4597.class, int.class, class_1799.class, boolean.class, imodelo);
            getBackpackModel = gerente.getMethod("getBackpackModel", class_1792.class);
            setColors = item.getMethod("setColors", class_1799.class, int.class, int.class);
            for (String n : new String[]{"BACKPACK", "COPPER_BACKPACK", "IRON_BACKPACK", "GOLD_BACKPACK", "DIAMOND_BACKPACK", "NETHERITE_BACKPACK"}) {
                NIVEIS.put(n, (Supplier<?>) itens.getField(n).get(null));
            }
            disponivel = true;
            MochilaFix.LOG.info("[menomon-mochila-fix] Sophisticated Backpacks encontrado: mochilas do Inmis usam o modelo 3D de costas do SB");
        } catch (Throwable t) {
            disponivel = false;
            MochilaFix.LOG.info("[menomon-mochila-fix] Sophisticated Backpacks indisponivel ({}): usando o item 3D do Inmis", t.toString());
        }
    }

    /** Nivel do SB equivalente a cada mochila do Inmis (mesmos fechos da menomon-mochila-3d). */
    private static String nivel(String caminho) {
        return switch (caminho) {
            case "blazing_backpack" -> "COPPER_BACKPACK";
            case "plated_backpack" -> "IRON_BACKPACK";
            case "gilded_backpack" -> "GOLD_BACKPACK";
            case "bejeweled_backpack" -> "DIAMOND_BACKPACK";
            case "withered_backpack", "endless_backpack" -> "NETHERITE_BACKPACK";
            default -> "BACKPACK"; // baby, frayed e qualquer outra
        };
    }

    /** @return true se desenhou com o SB (o chamador cancela o desenho original do Inmis). */
    public static boolean desenhar(class_583<?> modeloPai, class_1309 entidade, class_4587 matrizes,
                                   class_4597 buffers, int luz, class_1799 mochilaInmis) {
        if (!iniciado) iniciar();
        if (!disponivel) return false;
        try {
            class_2960 id = class_7923.field_41178.method_10221(mochilaInmis.method_7909());
            String nivel = nivel(id.method_12832());
            class_9282 tinta = mochilaInmis.method_57824(class_9334.field_49644);
            int cor = tinta == null ? Integer.MIN_VALUE : (0xFF000000 | tinta.comp_2384());
            String chave = nivel + "|" + cor;
            class_1799 falsa = STACKS.get(chave);
            if (falsa == null) {
                class_1792 itemSb = (class_1792) NIVEIS.get(nivel).get();
                falsa = new class_1799(itemSb);
                if (cor != Integer.MIN_VALUE) setColors.invoke(null, falsa, cor, -10342886); // borda padrao do SB
                STACKS.put(chave, falsa);
            }
            Object modelo = getBackpackModel.invoke(null, falsa.method_7909());
            matrizes.method_22903();
            try {
                renderBackpack.invoke(null, modeloPai, entidade, matrizes, buffers, luz, falsa, false, modelo);
            } finally {
                matrizes.method_22909();
            }
            return true;
        } catch (Throwable t) {
            disponivel = false;
            MochilaFix.LOG.error("[menomon-mochila-fix] Falha ao desenhar com o SB, voltando ao item 3D do Inmis", t);
            return false;
        }
    }
}
