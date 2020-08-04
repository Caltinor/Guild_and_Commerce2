package dicemc.gnc.testmaterial;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketRequestData {
	
	private final int gui;
	
	public PacketRequestData(PacketBuffer buf) {gui = buf.readInt();}
	
	public PacketRequestData(int guiType) {this.gui = guiType;}
	
	public void toBytes(PacketBuffer buf) {buf.writeInt(gui);}
 	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {System.out.println("Packet Worked");});
		return true;
	}
}
