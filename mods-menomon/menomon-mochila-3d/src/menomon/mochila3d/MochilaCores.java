package menomon.mochila3d;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_2960;
import net.minecraft.class_7923;
import net.minecraft.class_9282;
import net.minecraft.class_9334;

/**
 * Pano e borda do item 3D da mochila do Inmis recebem a cor como o Sophisticated Backpacks faz nas costas:
 * pano = cor do tingimento (ou a padrao do SB), borda = acento padrao do SB.
 */
public class MochilaCores implements ClientModInitializer {
    static final int PANO_PADRAO = -3382982;   // BackpackWrapper.DEFAULT_MAIN_COLOR
    static final int BORDA_PADRAO = -10342886; // BackpackWrapper.DEFAULT_ACCENT_COLOR
    static final String[] NOMES = {"baby", "frayed", "blazing", "plated", "gilded", "bejeweled", "withered", "endless"};

    @Override
    public void onInitializeClient() {
        for (String n : NOMES) {
            class_1792 item = class_7923.field_41178.method_10223(class_2960.method_60655("inmis", n + "_backpack"));
            if (item == null || item == class_7923.field_41178.method_10223(class_2960.method_60654("minecraft:air"))) continue;
            ColorProviderRegistry.ITEM.register((class_1799 stack, int tint) -> {
                if (tint == 0) {
                    class_9282 tinta = stack.method_57824(class_9334.field_49644);
                    return tinta == null ? PANO_PADRAO : (0xFF000000 | tinta.comp_2384());
                }
                return tint == 1 ? BORDA_PADRAO : -1;
            }, item);
        }
    }
}
