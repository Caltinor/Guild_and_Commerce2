package dicemc.gnc.guild.events;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dicemc.gnc.land.WorldWSD;
import dicemc.gnc.setup.Config;
import dicemc.gnclib.guilds.LogicGuilds;
import dicemc.gnclib.util.Duo;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class GuildEvents {
	private static int timer = 0;
	public static Iterable<ServerWorld> worlds;

	@SubscribeEvent
	public static void onServerTick(ServerTickEvent event) {
		if (event.phase == Phase.END) {
			timer++;
			if (timer >= Config.GLOBAL_TAX_INTERVAL.get()) {
				Map<UUID, Duo<Integer, Double>> taxData = new HashMap<UUID, Duo<Integer, Double>>();
				for (ServerWorld world : worlds) {
					taxData.putAll(WorldWSD.get(world).getTaxData());
				}
				LogicGuilds.applyTaxes(taxData);
			}
		}
	}
}
