package dicemc.gnc.land.events;

import dicemc.gnc.GnC;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityEvent.EnteringChunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ChunkEventHandler {

	@SubscribeEvent
	public static void onChunkLoad(ChunkEvent.Load event) {
		if (!event.getWorld().isRemote()) GnC.ckMgr.loadChunkData(event.getChunk().getPos(), (ServerWorld) event.getWorld());
	}
	
	@SubscribeEvent
	public static void onChunkUnload(ChunkEvent.Unload event) {
		/*TODO change to world.save event.  
		 * WSD interactions should interact directly with the WSD itself 
		 * whereas DB interactions should use the chunk manager to buffer
		 * land loading and saving. 
		 */
		if (!event.getWorld().isRemote()) GnC.ckMgr.saveChunkData(event.getChunk().getPos(), (ServerWorld) event.getWorld());
	}
	
	@SubscribeEvent
	public static void onChunkEnter(EnteringChunk event) {}
}
