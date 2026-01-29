package com.radimous.vaultcuriosenhancements;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.SlotTypeMessage;

@Mod(VaultCuriosEnhancements.MODID)
public class VaultCuriosEnhancements {
    public static final String MODID = "vault_curios_enhancements";
    public static final Logger LOGGER = LogUtils.getLogger();

    public VaultCuriosEnhancements() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        MinecraftForge.EVENT_BUS.register(new CommonEvent());
//        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE,
            () -> new SlotTypeMessage.Builder("shard_pouch")
                .icon(ResourceLocation.parse(CuriosApi.MODID + ":slot/shard_pouch"))
                .priority(800).build());
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE,
            () -> new SlotTypeMessage.Builder("coin_pouch")
                .icon(ResourceLocation.parse(CuriosApi.MODID + ":slot/coin_pouch"))
                .priority(800).build());
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE,
            () -> new SlotTypeMessage.Builder("antique_collector_book")
                .icon(ResourceLocation.parse(CuriosApi.MODID + ":slot/antique_collector_book"))
                .priority(800).build());
	    InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE,
             () -> new SlotTypeMessage.Builder("vault_map")
                     .icon(ResourceLocation.parse(CuriosApi.MODID + ":slot/vault_map"))
                     .priority(800).build());
    }

    public static String fmtNum(int num) {
        if (num >= 1_000_000_000) {      // 1.0B
            return String.format("%.1fB", num / 1_000_000_000.0);
        } else if (num >= 100_000_000) { // 100M
            return String.format("%.0fM", num / 1_000_000.0);
        } else if (num >= 1_000_000) {   // 1.0M 10.0M
            return String.format("%.1fM", num / 1_000_000.0);
        } else if (num >= 100_000) {     // 100K
            return String.format("%.0fK", num / 1_000.0);
        } else if (num >= 10_000) {      // 10.0K
            return String.format("%.1fK", num / 1_000.0);
        } else if (num >= 1_000) {       // 1.00K
            return String.format("%.2fK", num / 1_000.0);
        } else {
            return String.valueOf(num);
        }
    }

    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MODID, name);
    }

}
