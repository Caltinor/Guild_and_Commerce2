package dicemc.gnc.account.events;

import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class AccountEvents {

	@SubscribeEvent
	public static void onPlayerLogin(PlayerLoggedInEvent event) {
		//TODO check for account and create or load anew.
	}
}
