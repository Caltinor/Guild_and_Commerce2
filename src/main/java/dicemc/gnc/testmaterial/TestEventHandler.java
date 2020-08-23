package dicemc.gnc.testmaterial;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dicemc.gnc.GnC;
import dicemc.gnc.land.WhitelistItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.ChunkPos;
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
	
	/*@SubscribeEvent
	public static void onChunkEnterEvent(EnteringChunk event) {
		if (event.getEntity() instanceof PlayerEntity && !event.getEntity().world.isRemote()) {
			int oldX = event.getOldChunkX();
			int oldZ = event.getOldChunkZ();
			int newX = event.getNewChunkX();
			int newZ = event.getNewChunkZ();
			if (oldX != newX || oldZ != newZ) {
				ChunkData ref = GnC.ckMgr.getChunk(new ChunkPos(newX, newZ), event.getEntity().getEntityWorld().func_234923_W_());
				event.getEntity().sendMessage(new StringTextComponent("Entering: " +ref.pos.toString() + " $"+String.valueOf(ref.price)), event.getEntity().getUniqueID());
			}
		}
	}*/
	
	@SubscribeEvent
	public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
		if (!event.getWorld().isRemote()) {
			ChunkPos ck = event.getWorld().getChunk(event.getPos()).getPos();
			WhitelistItem wlItem = new WhitelistItem(event.getWorld().getBlockState(event.getPos()).getBlock().getRegistryName().getPath());
			Map<String, String> vals = new HashMap<String, String>();
			vals.put("price", String.valueOf(GnC.ckMgr.get(event.getWorld().func_234923_W_()).getChunk(ck).price += 10));
			vals.put("player", UUID.randomUUID().toString()+"Test Player");
			vals.put("whitelist", wlItem.toNBT().toString());
			System.out.println(GnC.ckMgr.get(event.getWorld().func_234923_W_()).updateChunk(ck, vals));
			if (event.getEntity().isCrouching() && event.getEntity() instanceof PlayerEntity) {
				GnC.aMgr.changeBalance(event.getEntity().getUniqueID(), 1000);
			}
		}
	}
	
	@SubscribeEvent
	public static void onLeftClick(PlayerInteractEvent.LeftClickBlock event) {
		if (!event.getWorld().isRemote()) {
			ChunkPos ck = event.getWorld().getChunk(event.getPos()).getPos();
			WhitelistItem wlItem = new WhitelistItem(event.getWorld().getBlockState(event.getPos()).getBlock().getRegistryName().getPath());
			wlItem.setCanBreak(true);
			Map<String, String> vals = new HashMap<String, String>();
			vals.put("whitelist", wlItem.toNBT().toString());
			System.out.println(GnC.ckMgr.get(event.getWorld().func_234923_W_()).updateChunk(ck, vals));
		}
	}
}

