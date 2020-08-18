package dicemc.gnc.testmaterial;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketRequestData {
	private final int igr;
	
	public PacketRequestData(PacketBuffer buf) {
		igr = buf.readInt();
	}
	
	public PacketRequestData(int igr) {
		this.igr = igr;
	}
	
	public void toBytes(PacketBuffer buf) {
		buf.writeInt(igr);
	}
 	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			System.out.println("Packet Worked");
		});
		return true;
	}
}
