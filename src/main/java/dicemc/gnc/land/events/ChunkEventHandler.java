package dicemc.gnc.land.events;

import dicemc.gnc.GnC;
import dicemc.gnc.land.ChunkData;
import dicemc.gnc.land.ChunkManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityEvent.EnteringChunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ChunkEventHandler {

	@SubscribeEvent
	public static void onChunkLoad(ChunkEvent.Load event) {		
		if (!event.getWorld().isRemote()) {
			ServerWorld world = (ServerWorld) event.getWorld();
			if (GnC.wldMgr.get(world.getDimensionKey()) == null) return;
			GnC.wldMgr.get(world.getDimensionKey()).loadChunkData(event.getChunk().getPos(), world);
		}
	}
	
	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load event) {
		if (!event.getWorld().isRemote()) {
			ServerWorld world = (ServerWorld) event.getWorld();
			if (GnC.wldMgr.get(world.getDimensionKey()) == null) {
				GnC.wldMgr.put(world.getDimensionKey(), new ChunkManager());
				GnC.wldMgr.get(world.getDimensionKey()).setServer(world.getServer());
				System.out.println("Added Dimension: "+world.getDimensionKey().toString());
			}
		}
	}
	
	@SubscribeEvent
	public static void onChunkUnload(WorldEvent.Save event) {
		if (!event.getWorld().isRemote()) {
			ServerWorld world = (ServerWorld) event.getWorld();
			GnC.wldMgr.get(world.getDimensionKey()).saveChunkData(world);
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
				ChunkManager cMgr = GnC.wldMgr.get(event.getEntity().getEntityWorld().getDimensionKey());
				if (!cMgr.getChunk(new ChunkPos(newX, newZ)).owner.equals(GnC.NIL)) {
					cMgr.updateOutpostToCore(new ChunkPos(newX, newZ), cMgr.getChunk(new ChunkPos(newX, newZ)).owner);
				}			
				ChunkData oref = cMgr.getChunk(new ChunkPos(oldX, oldZ));
				ChunkData nref = cMgr.getChunk(new ChunkPos(newX, newZ));
				if (oref == null) return;
				if (!oref.owner.equals(nref.owner) || (nref.owner.equals(GnC.NIL) && !oref.renter.equals(nref.renter))) {
					String msg = "Now Entering: " ;
					if (nref.owner.equals(GnC.NIL)) {
						if (nref.renter.equals(GnC.NIL)) msg += "Unowned Territory";
						else msg += "Temporary Claim of: "+ event.getEntity().getServer().getPlayerProfileCache().getProfileByUUID(nref.renter).getName();
					}
					else msg += "Territory of Guild: "+ GnC.gMgr.getGuildByID(cMgr.getChunk(new ChunkPos(newX, newZ)).owner).name;	
					event.getEntity().sendMessage(new StringTextComponent(msg), event.getEntity().getUniqueID());
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onExplosion (ExplosionEvent.Detonate event) {
		for (int i = event.getAffectedBlocks().size()-1; i > 0; i--) {
			ChunkPos pos = new ChunkPos(new BlockPos(event.getAffectedBlocks().get(i).getX(), event.getAffectedBlocks().get(i).getY(), event.getAffectedBlocks().get(i).getZ()));
			ChunkData cap = GnC.wldMgr.get(event.getWorld().getDimensionKey()).getChunk(pos);
			if (!cap.owner.equals(GnC.NIL) && !cap.canExplode) event.getAffectedBlocks().remove(i);
		}
		for (int i = event.getAffectedEntities().size()-1; i > 0; i--) {
			ChunkPos pos = new ChunkPos(event.getAffectedEntities().get(i).chunkCoordX, event.getAffectedEntities().get(i).chunkCoordZ);
			ChunkData cap = GnC.wldMgr.get(event.getWorld().getDimensionKey()).getChunk(pos);
			if (!cap.owner.equals(GnC.NIL) && !cap.canExplode) event.getAffectedEntities().remove(i);
		}		
	}
}
