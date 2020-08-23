package dicemc.gnc.guild.events;

import dicemc.gnc.GnC;
import dicemc.gnc.setup.Config;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class GuildEvents {
	private static int timer = 0;

	@SubscribeEvent
	public static void onServerTick(ServerTickEvent event) {
		if (event.phase == Phase.END) {
			timer++;
			if (timer >= Config.GLOBAL_TAX_INTERVAL.get()) {
				GnC.gMgr.applyTaxes();
				GnC.gMgr.printTaxInfo();
			}
		}
	}
}
