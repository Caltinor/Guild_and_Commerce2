package dicemc.gnc.testmaterial;

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
				ChunkData ref = GnC.ckMgr.getChunk(new ChunkPos(newX, newZ), event.getEntity().getEntityWorld().getDimensionKey());
				event.getEntity().sendMessage(new StringTextComponent("Entering: " +ref.pos.toString() + " $"+String.valueOf(ref.price)), event.getEntity().getUniqueID());
			}
		}
	}
	
	@SubscribeEvent
	public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
		if (!event.getWorld().isRemote()) {
			ChunkPos ck = event.getWorld().getChunk(event.getPos()).getPos();
			WhitelistItem wlItem = new WhitelistItem(event.getWorld().getBlockState(event.getPos()).getBlock().getRegistryName().getPath());
			Map<String, String> vals = new HashMap<String, String>();
			vals.put("price", String.valueOf(GnC.wldMgr.get(event.getWorld().getDimensionKey()).getChunk(ck).price += 10));
			//vals.put("player", UUID.randomUUID().toString()+"Test Player");
			//vals.put("whitelist", wlItem.toNBT().toString());
			System.out.println(GnC.wldMgr.get(event.getWorld().getDimensionKey()).updateChunk(ck, vals));
			if (event.getEntity().isCrouching() && event.getEntity() instanceof PlayerEntity) {
				GnC.aMgr.changeBalance(event.getEntity().getUniqueID(), 1000);
			}
		}
	}
	
	@SubscribeEvent
	public static void onLeftClick(PlayerInteractEvent.LeftClickBlock event) {
		if (!event.getWorld().isRemote()) {
			if (event.getEntity().isCrouching() && event.getEntity() instanceof PlayerEntity) {
				Guild guild = GnC.gMgr.getGuildByMember(event.getEntity().getUniqueID());
				if (guild != null && GnC.aMgr.getBalance(event.getEntity().getUniqueID()) >= 100) {
					GnC.aMgr.changeBalance(event.getEntity().getUniqueID(), -100);
					System.out.println(GnC.aMgr.changeBalance(guild.guildID, 100));
				}
			}
			else {
				Guild guild = GnC.gMgr.getGuildByMember(event.getEntity().getUniqueID());
				if (guild != null) {
					System.out.println(guild.members.getOrDefault(event.getEntity().getUniqueID(), 1337));
					for (Map.Entry<permKey, Integer> perms : guild.permissions.entrySet()) {
						System.out.println(perms.getKey().toString() + ": "+ String.valueOf(perms.getValue()));
					}
				}
			}
		}
	}*/
}

