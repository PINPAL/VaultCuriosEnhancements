package com.radimous.vaultcuriosenhancements.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.platform.InputConstants;
import com.radimous.vaultcuriosenhancements.network.C2SOpenAntiqueBookPacket;
import com.radimous.vaultcuriosenhancements.network.C2SOpenCoinPouchPacket;
import com.radimous.vaultcuriosenhancements.network.C2SOpenShardPouchPacket;
import com.radimous.vaultcuriosenhancements.network.PacketHandler;
import iskallia.vault.event.InputEvents;
import iskallia.vault.init.ModItems;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Set;

@Mixin(value = InputEvents.class, remap = false)
public class MixinInputEvents {
    @WrapOperation(method = "onInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isActiveAndMatches(Lcom/mojang/blaze3d/platform/InputConstants$Key;)Z", ordinal = 0),
        slice = @Slice(
            from = @At(value = "FIELD", target = "Liskallia/vault/init/ModKeybinds;openShardPouch:Lnet/minecraft/client/KeyMapping;", opcode = Opcodes.GETSTATIC)
        ))
    private static boolean handleShardPouchInCurio(KeyMapping instance, InputConstants.Key key, Operation<Boolean> original) {
        if (original.call(instance, key)) {
            var player = Minecraft.getInstance().player;
            if (player != null && CuriosApi.getCuriosHelper().findFirstCurio(player, ModItems.SHARD_POUCH).isPresent()) {
                PacketHandler.sendToServer(new C2SOpenShardPouchPacket());
            }
            return true;
        }

        return false;
    }

    @WrapOperation(method = "onInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isActiveAndMatches(Lcom/mojang/blaze3d/platform/InputConstants$Key;)Z", ordinal = 0),
        slice = @Slice(
            from = @At(value = "FIELD", target = "Liskallia/vault/init/ModKeybinds;openCoinPouch:Lnet/minecraft/client/KeyMapping;", opcode = Opcodes.GETSTATIC)
        ))
    private static boolean handleCoinPouchInCurio(KeyMapping instance, InputConstants.Key key, Operation<Boolean> original) {
        if (original.call(instance, key)) {
            var player = Minecraft.getInstance().player;
            if (player != null && CuriosApi.getCuriosHelper().findFirstCurio(player, ModItems.SHARD_POUCH).isPresent()) {
                PacketHandler.sendToServer(new C2SOpenCoinPouchPacket());
            }
            return true;
        }

        return false;
    }

    @WrapOperation(method = "onInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isActiveAndMatches(Lcom/mojang/blaze3d/platform/InputConstants$Key;)Z", ordinal = 0),
        slice = @Slice(
            from = @At(value = "FIELD", target = "Liskallia/vault/init/ModKeybinds;openAntiqueBook:Lnet/minecraft/client/KeyMapping;", opcode = Opcodes.GETSTATIC)
        ))
    private static boolean handleAntiqueBookInCurio(KeyMapping instance, InputConstants.Key key, Operation<Boolean> original) {
        if (original.call(instance, key)) {
            var player = Minecraft.getInstance().player;
            if (player != null && CuriosApi.getCuriosHelper().findFirstCurio(player, ModItems.SHARD_POUCH).isPresent()) {
                PacketHandler.sendToServer(new C2SOpenAntiqueBookPacket());
            }
            return true;
        }

        return false;
    }

	@WrapOperation(
	  method = "onInput", at = @At(
	  value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;hasAnyOf(Ljava/util/Set;)Z", remap = true, ordinal = 0
	), slice = @Slice(
	  from = @At(
		value = "FIELD", target = "Liskallia/vault/init/ModKeybinds;openVaultMap:Lnet/minecraft/client/KeyMapping;", opcode = Opcodes.GETSTATIC
	  )
	)
	) private static boolean vaultMapChecksCuriosToo(Inventory instance, Set<Item> itemsSet, Operation<Boolean> original) {
		// Original vanilla inventory check
		if (original.call(instance, itemsSet)) {
			return true;
		}

		// Curios check
		var player = Minecraft.getInstance().player;
		if (player == null) return false;
		return CuriosApi.getCuriosHelper().findFirstCurio(player, stack -> itemsSet.contains(stack.getItem())).isPresent();
	}

	@WrapOperation(
	  method = "onInput", at = @At(
	  value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;hasAnyOf(Ljava/util/Set;)Z", remap = true, ordinal = 0
	), slice = @Slice(
	  from = @At(
		value = "FIELD", target = "Liskallia/vault/init/ModKeybinds;holdVaultMap:Lnet/minecraft/client/KeyMapping;", opcode = Opcodes.GETSTATIC
	  )
	)
	) private static boolean vaultMapHoldChecksCuriosToo(Inventory instance, Set<Item> itemsSet, Operation<Boolean> original) {
		// Original vanilla inventory check
		if (original.call(instance, itemsSet)) {
			return true;
		}

		// Curios check
		var player = Minecraft.getInstance().player;
		if (player == null) return false;
		return CuriosApi.getCuriosHelper().findFirstCurio(player, stack -> itemsSet.contains(stack.getItem())).isPresent();
	}

}

