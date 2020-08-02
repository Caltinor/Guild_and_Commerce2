package dicemc.gnc.land.events;

import dicemc.gnc.GnC;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityEvent.EnteringChunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ChunkEventHandler {

	@SubscribeEvent
	public static void onChunkLoad(ChunkEvent.Load event) {
		if (!event.getWorld().isRemote()) GnC.ckMgr.loadChunkData(event.getChunk().getPos(), (ServerWorld) event.getWorld());
	}
	
	@SubscribeEvent
	public static void onChunkUnload(WorldEvent.Save event) {
		if (!event.getWorld().isRemote()) GnC.ckMgr.saveChunkData((ServerWorld) event.getWorld());
	}
	
	@SubscribeEvent
	public static void onChunkEnter(EnteringChunk event) {}
}
