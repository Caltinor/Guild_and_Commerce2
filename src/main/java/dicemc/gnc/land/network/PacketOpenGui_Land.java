package dicemc.gnc.land.network;

import java.util.function.Supplier;

import dicemc.gnc.testmaterial.TestGui;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketOpenGui_Land {

	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(TestGui::open);
		return true;
	}
}
