package dicemc.gnc.testmaterial;

import java.util.HashMap;
import java.util.Map;

import dicemc.gnc.GnC;
import dicemc.gnc.land.ChunkData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.EntityEvent.EnteringChunk;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class TestEventHandler {
	/*public static MySQLCon dbConn;

	@SubscribeEvent
	public static void onServerStart(FMLServerStartingEvent event) {
		DBCommands.register(event.getCommandDispatcher());
		dbConn = new MySQLCon();
	}*/
	
	@SubscribeEvent
	public static void onChunkEnterEvent(EnteringChunk event) {
		if (event.getEntity() instanceof PlayerEntity && !event.getEntity().world.isRemote()) {
			int oldX = event.getOldChunkX();
			int oldZ = event.getOldChunkZ();
			int newX = event.getNewChunkX();
			int newZ = event.getNewChunkZ();
			if (oldX != newX || oldZ != newZ) {
				ChunkData ref = GnC.ckMgr.getChunk(new ChunkPos(newX, newZ));
				event.getEntity().sendMessage(new StringTextComponent("Entering: " +ref.pos.toString() + " $"+String.valueOf(ref.price)), event.getEntity().getUniqueID());
				//		" $"+String.valueOf(GnC.ckMgr.getChunk(new ChunkPos(newX, newZ)).price)), event.getEntity().getUniqueID());
			}
		}
	}
	
	@SubscribeEvent
	public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
		if (!event.getWorld().isRemote()) {
			ChunkPos ck = event.getWorld().getChunk(event.getPos()).getPos();
			Map<String, String> vals = new HashMap<String, String>();
			vals.put("price", String.valueOf(GnC.ckMgr.getChunk(ck).price += 10));
			System.out.println(GnC.ckMgr.updateChunk(ck, vals));
		}
	}
}

