package dicemc.gnc.market.events;

import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MarketEvents {

	@SubscribeEvent
	public static void onServerTick(ServerTickEvent event) {
	}
}
