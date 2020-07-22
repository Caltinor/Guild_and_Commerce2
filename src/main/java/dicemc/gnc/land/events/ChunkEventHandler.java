package dicemc.gnc.land.events;

import dicemc.gnc.GnC;
import net.minecraftforge.event.entity.EntityEvent.EnteringChunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ChunkEventHandler {

	@SubscribeEvent
	public static void onChunkLoad(ChunkEvent.Load event) {
		GnC.ckMgr.loadChunkData(event.getChunk().getPos());
	}
	
	@SubscribeEvent
	public static void onChunkUnload(ChunkEvent.Unload event) {
		GnC.ckMgr.saveChunkData(event.getChunk().getPos());
	}
	
	@SubscribeEvent
	public static void onChunkEnter(EnteringChunk event) {}
}
