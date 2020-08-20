package dicemc.gnc.land.events;

import dicemc.gnc.GnC;
import dicemc.gnc.land.ChunkData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.StringTextComponent;
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
		if (!event.getWorld().isRemote()) {
			ServerWorld world = (ServerWorld) event.getWorld();
			GnC.ckMgr.loadChunkData(event.getChunk().getPos(), world, world.getWorld().func_234923_W_());
		}
	}
	
	@SubscribeEvent
	public static void onChunkUnload(WorldEvent.Save event) {
		if (!event.getWorld().isRemote()) {
			ServerWorld world = (ServerWorld) event.getWorld();
			GnC.ckMgr.saveChunkData(world, world.getWorld().func_234923_W_());
		}
	}
	
	@SubscribeEvent
	public static void onChunkEnter(EnteringChunk event) {
		if (event.getEntity() instanceof PlayerEntity && !event.getEntity().world.isRemote()) {
			int oldX = event.getOldChunkX();
			int oldZ = event.getOldChunkZ();
			int newX = event.getNewChunkX();
			int newZ = event.getNewChunkZ();
			if (oldX != newX || oldZ != newZ) {
				ChunkData oref = GnC.ckMgr.getChunk(new ChunkPos(oldX, oldZ), event.getEntity().getEntityWorld().func_234923_W_());
				ChunkData nref = GnC.ckMgr.getChunk(new ChunkPos(newX, newZ), event.getEntity().getEntityWorld().func_234923_W_());
				if (!oref.owner.equals(nref.owner) || (nref.owner.equals(GnC.NIL) && !oref.renter.equals(nref.renter))) {
					String msg = "Now Entering: " ;
					msg += nref.owner.equals(GnC.NIL) ? 
							"Temporary Claim of: "+ event.getEntity().getServer().getPlayerProfileCache().getProfileByUUID(nref.renter).getName() : 
							"Territory of Guild: "+ GnC.gMgr.getGuildByID(GnC.ckMgr.getChunk(new ChunkPos(newX, newZ), event.getEntity().getEntityWorld().func_234923_W_()).owner).name;
					event.getEntity().sendMessage(new StringTextComponent(msg), event.getEntity().getUniqueID());
				}
			}
		}
	}
}
