package dicemc.gnc.account.events;

import java.util.logging.Logger;

import dicemc.gnclib.money.LogicMoney;
import dicemc.gnclib.money.LogicMoney.AccountType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class AccountEvents {
	private static Logger LOG = Logger.getLogger("dicemc.gnc.account.events");

	@SubscribeEvent
	public static void onPlayerLogin(PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof PlayerEntity) {
			Double bal = LogicMoney.getBalance(event.getEntity().getUUID(), AccountType.PLAYER.rl);
			LOG.info("Player: "+event.getPlayer().getDisplayName().getString() + " joined with $"+bal);
			event.getPlayer().sendMessage(new TranslationTextComponent("gnc.money.events.login", bal), event.getPlayer().getUUID());
		}
	}
}
