package dicemc.gnc.account.events;

import dicemc.gnc.GnC;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class AccountEvents {

	@SubscribeEvent
	public static void onPlayerLogin(PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof PlayerEntity) {
			System.out.println(GnC.aMgr.accountExists(event.getEntity().getUniqueID()));
		}
	}
}
