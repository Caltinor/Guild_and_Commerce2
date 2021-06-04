package dicemc.gnc.common;

import java.util.function.Consumer;
import java.util.function.Supplier;

import dicemc.gnc.land.WorldWSD;
import dicemc.gnc.land.network.PacketOpenGui_Land;
import dicemc.gnc.setup.Networking;
import dicemc.gnclib.guilds.LogicGuilds;
import dicemc.gnclib.guilds.entries.Guild;
import dicemc.gnclib.money.LogicMoney;
import dicemc.gnclib.money.LogicMoney.AccountType;
import dicemc.gnclib.realestate.ChunkData;
import dicemc.gnclib.util.ChunkPos3D;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketGuiRequest {
	private final gui action;
	
	public enum gui {
		CHUNK(ctx -> {
			Guild guild = LogicGuilds.getGuildByMember(ctx.get().getSender().getUUID());
			ServerWorld server = ctx.get().getSender().getLevel();
			ChunkPos3D center = new ChunkPos3D(ctx.get().getSender().xChunk, ctx.get().getSender().yChunk, ctx.get().getSender().zChunk);
			ChunkData ckData = WorldWSD.get(server).getOrCreate(center);
			double balG = guild.equals(Guild.getDefault()) ? 0.0 : LogicMoney.getBalance(guild.guildID, AccountType.GUILD.rl);
			double balP = LogicMoney.getBalance(ctx.get().getSender().getUUID(), AccountType.PLAYER.rl);
			//send to client to open gui
			Networking.sendToClient(new PacketOpenGui_Land(balG, balP, guild, ckData), ctx.get().getSender());
		}),
		MARKET(ctx -> {}),
		GUILD(ctx -> {}),
		ACCOUNT(ctx -> {}),
		PERMS(ctx -> {}),
		MEMBERS(ctx -> {}),
		REAL_ESTATE(ctx -> {});
		
		public final Consumer<Supplier<NetworkEvent.Context>> packetHandler;
		
		gui(Consumer<Supplier<NetworkEvent.Context>> packetHandler) {this.packetHandler = packetHandler;}
	}
	
	public PacketGuiRequest(PacketBuffer buf) {action = gui.values()[buf.readInt()];}
	
	public PacketGuiRequest(gui guiType) {this.action = guiType;}
	
	public void toBytes(PacketBuffer buf) {buf.writeInt(action.ordinal());}
 	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {this.action.packetHandler.accept(ctx);});
		return true;
	}
	
	
}
