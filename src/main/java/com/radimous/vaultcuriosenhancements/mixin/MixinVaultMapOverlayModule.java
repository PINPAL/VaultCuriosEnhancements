package com.radimous.vaultcuriosenhancements.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import iskallia.vault.client.render.hud.module.vault.VaultMapOverlayModule;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import top.theillusivec4.curios.api.CuriosApi;

@Mixin(value = VaultMapOverlayModule.class, remap = false)
public abstract class MixinVaultMapOverlayModule {
	@WrapOperation(
	  method = "renderVault",
	  at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/world/entity/player/Inventory;hasAnyOf(Ljava/util/Set;)Z",
		ordinal = 0
	  )
	)
	private static boolean inventoryChecksCuriosToo(
	  Inventory instance,
	  java.util.Set<net.minecraft.world.item.Item> itemsSet,
	  Operation<Boolean> original
	) {
		// Original vanilla inventory check
		if (original.call(instance, itemsSet)) {
			return true;
		}

		// Curios check
		var player = Minecraft.getInstance().player;
		if (player == null) return false;
		return CuriosApi.getCuriosHelper()
		                .findFirstCurio(player, stack -> itemsSet.contains(stack.getItem()))
		                .isPresent();
	}
}
