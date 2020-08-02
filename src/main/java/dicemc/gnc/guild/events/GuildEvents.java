package dicemc.gnc.guild.events;

import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class GuildEvents {

	@SubscribeEvent
	public static void onServerTick(ServerTickEvent event) {
		//TODO construct tax logic
	}
}
