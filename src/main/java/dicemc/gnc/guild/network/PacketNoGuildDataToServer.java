package dicemc.gnc.guild.network;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketNoGuildDataToServer {
	private final PkType action;
	private final String str;
	
	public enum PkType {
		JOIN((packet, ctx) -> ""),
		REJECT((packet, ctx) -> ""),
		CREATE((packet, ctx) -> "");
	
		public final BiFunction<PacketNoGuildDataToServer, Supplier<NetworkEvent.Context>, String> packetHandler;
		
		PkType(BiFunction<PacketNoGuildDataToServer, Supplier<NetworkEvent.Context>, String> packetHandler) {this.packetHandler = packetHandler;}
	}
	
	public PacketNoGuildDataToServer(PacketBuffer buf) {
		action = PkType.values()[buf.readVarInt()];
		str = buf.readUtf(32767);
		
	}
	
	public PacketNoGuildDataToServer(PkType action, String str) {
		this.action = action;
		this.str = str;
	}
	
	public void toBytes(PacketBuffer buf) {
		buf.writeVarInt(action.ordinal());
		buf.writeUtf(str);
	}
 	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			StringTextComponent text = new StringTextComponent(this.action.packetHandler.apply(this, ctx));
			ctx.get().getSender().getServer().sendMessage(text, ctx.get().getSender().getUUID());
		});
		return true;
	}
}
