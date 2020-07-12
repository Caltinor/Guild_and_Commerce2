package dicemc.gnc.common;

import dicemc.gnc.command.impl.DBCommands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.EntityEvent.EnteringChunk;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod.EventBusSubscriber
public class TestEventHandler {
	public static MySQLCon dbConn;

	@SubscribeEvent
	public static void onServerStart(FMLServerStartingEvent event) {
		DBCommands.register(event.getCommandDispatcher());
		dbConn = new MySQLCon();
	}
	
	@SubscribeEvent
	public static void onChunkEnterEvent(EnteringChunk event) {
		if (event.getEntity() instanceof PlayerEntity) {
			int oldX = event.getOldChunkX();
			int oldZ = event.getOldChunkZ();
			int newX = event.getNewChunkX();
			int newZ = event.getNewChunkZ();
			if (oldX != newX || oldZ != newZ) {
				event.getEntity().sendMessage(new StringTextComponent("Entering: ("+String.valueOf(newX)+", "+String.valueOf(newZ)+")."), event.getEntity().getUniqueID());
			}
		}
	}

}

