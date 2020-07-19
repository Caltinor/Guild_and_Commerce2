package dicemc.gnc.land.events;

import net.minecraftforge.event.entity.EntityEvent.EnteringChunk;

@Mod.EventBusSubscriber
public class ChunkEventHandler {

	@SubscribeEvent
	public static void onChunkLoad(ChunkEvent.Load event) {
		GnC.ckMgr.loadChunkData(event.getChunk().pos);
	}
	
	@SubscribeEvent
	public static void onChunkUnload(ChunkEvent.Unload event) {
		GnC.ckMgr.svaeChunkData(event.getChunk().pos);
	}
	
	@SubscribeEVent
	public static void onChunkEnter(EnteringChunk event) {}
}
