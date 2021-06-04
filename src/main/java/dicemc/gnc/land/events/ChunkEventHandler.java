package dicemc.gnc.land.events;

import dicemc.gnc.land.WorldWSD;
import dicemc.gnclib.guilds.LogicGuilds;
import dicemc.gnclib.protection.LogicProtection;
import dicemc.gnclib.realestate.ChunkData;
import dicemc.gnclib.util.ChunkPos3D;
import dicemc.gnclib.util.ComVars;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityEvent.EnteringChunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ChunkEventHandler extends LogicProtection{

	@SubscribeEvent
	public static void onChunkLoad(ChunkEvent.Load event) {		
		if (!event.getWorld().isClientSide()) {
			ServerWorld world = (ServerWorld) event.getWorld();
			ChunkPos cp = event.getChunk().getPos();			
			int bottom = 0;  //TODO 1.17 update floor to new dimension bottom
			for (int i = bottom; i < event.getWorld().getMaxBuildHeight(); i+=16) {
				WorldWSD.get(world).loadChunkData(new ChunkPos3D(cp.x, ChunkPos3D.chunkYFromAltitude(i, bottom), cp.z));
			}
		}
	}
	
	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load event) {
		/*if (!event.getWorld().isRemote()) {
			ServerWorld world = (ServerWorld) event.getWorld();
			if (GnC.wldMgr.get(world.getDimensionKey()) == null) {
				GnC.wldMgr.put(world.getDimensionKey(), new ChunkManager());
				GnC.wldMgr.get(world.getDimensionKey()).setServer(world.getServer());
				System.out.println("Added Dimension: "+world.getDimensionKey().toString());
			}
		}*/
	}
	
	@SubscribeEvent
	public static void onChunkUnload(WorldEvent.Save event) {
		if (!event.getWorld().isClientSide()) {
			ServerWorld world = (ServerWorld) event.getWorld();
			WorldWSD.get(world).saveChunkData();
		}
	}
	
	@SubscribeEvent
	public static void onChunkEnter(EnteringChunk event) {
		/* TODO consider how processing could be handled by 
		 * the library and push those actions to that method
		 */
		if (event.getEntity() instanceof PlayerEntity && !event.getEntity().getCommandSenderWorld().isClientSide()) {
			int bottom = 0; //TODO 1.17 update dimension bottom;
			int oldX = event.getOldChunkX();
			int oldY = event.getEntity().yChunk;
			int oldZ = event.getOldChunkZ();
			int newX = event.getNewChunkX();
			int newY = event.getEntity().yChunk;
			int newZ = event.getNewChunkZ();			
			if (oldX != newX || oldZ != newZ || oldY != newY) {
				WorldWSD wsd = WorldWSD.get((ServerWorld) event.getEntity().level);			
				ChunkData oref = wsd.getCap().get(new ChunkPos3D(oldX, oldY, oldZ));
				ChunkData nref = wsd.getCap().get(new ChunkPos3D(newX, ChunkPos3D.chunkYFromAltitude(newY, bottom), newZ));
				if (oref == null) return;
				if (!oref.owner.equals(nref.owner) || ((nref.owner.refID.equals(ComVars.NIL) && !oref.renter.equals(nref.renter)))) {
					TextComponent context = new StringTextComponent("");
					if (nref.owner.refID.equals(ComVars.NIL)) {
						if (nref.renter.refID.equals(ComVars.NIL)) context = new TranslationTextComponent("gnc.land.events.chunkevent.enter.unowned");
						else context = new TranslationTextComponent("gnc.land.events.chunkevent.enter.tempclaim", event.getEntity().getServer().getProfileCache().get(nref.renter.refID).getName());
					}
					else context = new TranslationTextComponent("gnc.land.events.chunkevent.enter.guildclaim", LogicGuilds.getGuildByID(nref.owner.refID));	
					event.getEntity().sendMessage(new TranslationTextComponent("gnc.land.events.chunkevent.enter", context), event.getEntity().getUUID());
				}
			}
		}
	}
}
