package dicemc.gnc.testmaterial;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketRequestData {
	
	private final boolean order;
	
	public PacketRequestData(PacketBuffer buf) {order = buf.readBoolean();}
	
	public PacketRequestData(boolean sortOrder) {this.order = sortOrder;}
	
	public void toBytes(PacketBuffer buf) {buf.writeBoolean(order);}
 	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			TestEventHandler.dbConn.callSortedPriceList(order);
			List<String> list = TestEventHandler.dbConn.getResults();
			for (String str : list) {
				ctx.get().getSender().sendMessage(new StringTextComponent(str), ctx.get().getSender().getUniqueID());
			}
		});
		return true;
	}
}
