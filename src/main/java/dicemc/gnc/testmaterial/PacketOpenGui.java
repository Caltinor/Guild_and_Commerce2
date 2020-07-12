package dicemc.gnc.testmaterial;

import java.util.function.Supplier;

import net.minecraftforge.fml.network.NetworkEvent;

public class PacketOpenGui {
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(TestGui::open);
		return true;
	}
}
