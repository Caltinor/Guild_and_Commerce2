package dicemc.gnc.land.events;

import dicemc.gnc.GnC;
import dicemc.gnc.land.ChunkData;
import dicemc.gnc.land.ChunkManager;
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
			GnC.ckMgr.get(world.func_234923_W_()).loadChunkData(event.getChunk().getPos(), world);
		}
	}
	
	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load event) {
		if (!event.getWorld().isRemote()) {
			ServerWorld world = (ServerWorld) event.getWorld();
			if (GnC.ckMgr.get(world.func_234923_W_()) == null) {
				GnC.ckMgr.put(world.func_234923_W_(), new ChunkManager());
				GnC.ckMgr.get(world.func_234923_W_()).setServer(world.getServer());
			}
		}
	}
	
	@SubscribeEvent
	public static void onChunkUnload(WorldEvent.Save event) {
		if (!event.getWorld().isRemote()) {
			ServerWorld world = (ServerWorld) event.getWorld();
			GnC.ckMgr.get(world.func_234923_W_()).saveChunkData(world);
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
				ChunkData oref = GnC.ckMgr.get(event.getEntity().getEntityWorld().func_234923_W_()).getChunk(new ChunkPos(oldX, oldZ));
				ChunkData nref = GnC.ckMgr.get(event.getEntity().getEntityWorld().func_234923_W_()).getChunk(new ChunkPos(newX, newZ));
				if (oref == null) return;
				if (!oref.owner.equals(nref.owner) || (nref.owner.equals(GnC.NIL) && !oref.renter.equals(nref.renter))) {
					String msg = "Now Entering: " ;
					if (nref.owner.equals(GnC.NIL)) {
						if (nref.renter.equals(GnC.NIL)) msg += "Unowned Territory";
						else msg += "Temporary Claim of: "+ event.getEntity().getServer().getPlayerProfileCache().getProfileByUUID(nref.renter).getName();
					}
					else msg += "Territory of Guild: "+ GnC.gMgr.getGuildByID(GnC.ckMgr.get(event.getEntity().getEntityWorld().func_234923_W_()).getChunk(new ChunkPos(newX, newZ)).owner).name;	
					event.getEntity().sendMessage(new StringTextComponent(msg), event.getEntity().getUniqueID());
				}
			}
		}
	}
}
