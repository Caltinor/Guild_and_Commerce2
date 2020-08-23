package dicemc.gnc.guild.network;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import dicemc.gnc.GnC;
import dicemc.gnc.account.AccountManager;
import dicemc.gnc.setup.Networking;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketNoGuildDataToServer {
	private final PkType action;
	private final String str;
	
	public enum PkType {
		JOIN((packet, ctx) -> GnC.gMgr.joinGuild(GnC.gMgr.getGuildByName(packet.str).guildID, ctx.get().getSender().getUniqueID())),
		REJECT((packet, ctx) -> {
			String resp = GnC.gMgr.rejectInvite(GnC.gMgr.getGuildByName(packet.str).guildID, ctx.get().getSender().getUniqueID());
			double balP = GnC.aMgr.getBalance(ctx.get().getSender().getUniqueID());
			List<String> invites = GnC.gMgr.getInvitedGuilds(ctx.get().getSender().getUniqueID());
			List<String> openGuilds = GnC.gMgr.getOpenGuilds(ctx.get().getSender().getUniqueID());
			Networking.sendToClient(new PacketUpdateGuiNoGuild(balP, invites, openGuilds), ctx.get().getSender());
			return resp;			
		}),
		CREATE((packet, ctx) -> GnC.gMgr.createNewGuild(packet.str, ctx.get().getSender().getUniqueID(), false));
	
		public final BiFunction<PacketNoGuildDataToServer, Supplier<NetworkEvent.Context>, String> packetHandler;
		
		PkType(BiFunction<PacketNoGuildDataToServer, Supplier<NetworkEvent.Context>, String> packetHandler) {this.packetHandler = packetHandler;}
	}
	
	public PacketNoGuildDataToServer(PacketBuffer buf) {
		action = PkType.values()[buf.readVarInt()];
		str = buf.readString();
		
	}
	
	public PacketNoGuildDataToServer(PkType action, String str) {
		this.action = action;
		this.str = str;
	}
	
	public void toBytes(PacketBuffer buf) {
		buf.writeVarInt(action.ordinal());
		buf.writeString(str);
	}
 	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			StringTextComponent text = new StringTextComponent(this.action.packetHandler.apply(this, ctx));
			ctx.get().getSender().getServer().sendMessage(text, ctx.get().getSender().getUniqueID());
		});
		return true;
	}
}
