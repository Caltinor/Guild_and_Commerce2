package dicemc.gnc.land.network;

import java.util.function.Supplier;

import dicemc.gnc.land.client.GuiLandManager;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketOpenGui_Land {
	private final double balP;
	
	public PacketOpenGui_Land(PacketBuffer buf) {balP = buf.readDouble();}
	
	public PacketOpenGui_Land(double balP) {this.balP = balP;}
	
	public void toBytes(PacketBuffer buf) {buf.writeDouble(balP);}

	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			GuiLandManager.open(balP);
		});
		return true;
	}
}
