 package dicemc.gnc.land.network;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import dicemc.gnc.land.WorldWSD;
import dicemc.gnc.setup.Networking;
import dicemc.gnclib.guilds.LogicGuilds;
import dicemc.gnclib.guilds.entries.Guild;
import dicemc.gnclib.money.LogicMoney;
import dicemc.gnclib.money.LogicMoney.AccountType;
import dicemc.gnclib.realestate.ChunkData;
import dicemc.gnclib.trade.LogicTrade;
import dicemc.gnclib.util.ChunkPos3D;
import dicemc.gnclib.util.ComVars;
import dicemc.gnclib.util.Agent;
import dicemc.gnclib.util.Agent.Type;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketChunkDataToServer {
	private final PkType action;
	private final ChunkPos3D pos;
	private UUID id = ComVars.NIL;
	private double dbl = 0d;
	private int igr = 0;
	private String str = "";
		
	public enum PkType {
		@SuppressWarnings("resource")
		TEMPCLAIM((packet, ctx) -> {
			Agent exec = LogicTrade.get().getTransactor(packet.id, Type.PLAYER, "");
			return new TranslationTextComponent(WorldWSD.get((ServerWorld) ctx.get().getSender().level).tempClaim(packet.pos, exec).translationKey).getString();
		}), 
		@SuppressWarnings("resource")
		GUILDCLAIM((packet, ctx) -> {
			Guild guild = LogicGuilds.getGuildByMember(packet.id);
			Agent exec = LogicTrade.get().getTransactor(guild.guildID, Type.GUILD, guild.name);
			return new TranslationTextComponent(
					WorldWSD.get((ServerWorld) ctx.get().getSender().level).guildClaim(packet.pos, exec).translationKey)
					.getString();
		}), 
		ABANDON((packet, ctx) -> ""),// 
		EXTEND((packet, ctx) ->  ""),//GnC.wldMgr.get(ctx.get().getSender().getEntityWorld().getDimensionKey()).extendClaim(packet.pos, packet.id)), 
		SELL((packet, ctx) -> ""), 
		UPDATESUB((packet, ctx) -> ""), 
		PUBLIC((packet, ctx) ->  ""),//GnC.wldMgr.get(ctx.get().getSender().getEntityWorld().getDimensionKey()).publicToggle(packet.pos, packet.igr == 1)), 
		MINRANK((packet, ctx) -> ""), 
		DISABLESUB((packet, ctx) -> ""), 
		BREAK((packet, ctx) ->  ""),/*{
			CompoundNBT nbt = new CompoundNBT();
			try {nbt = JsonToNBT.getTagFromJson(packet.str);} catch (CommandSyntaxException e) {}
			return GnC.wldMgr.get(ctx.get().getSender().getEntityWorld().getDimensionKey()).updateWhitelistItem(packet.pos, new WhitelistItem(nbt)); 
		}),*/
		INTERACT((packet, ctx) ->  ""),/*{
			CompoundNBT nbt = new CompoundNBT();
			try {nbt = JsonToNBT.getTagFromJson(packet.str);} catch (CommandSyntaxException e) {}
			return GnC.wldMgr.get(ctx.get().getSender().getEntityWorld().getDimensionKey()).updateWhitelistItem(packet.pos, new WhitelistItem(nbt)); 
		}),*/
		CLEARWL((packet, ctx) -> ""),//GnC.wldMgr.get(ctx.get().getSender().getEntityWorld().getDimensionKey()).setWhitelist(packet.pos, new ArrayList<WhitelistItem>())), 
		MEMBER((packet, ctx) ->  ""),/*{
			Map<UUID, String> map = GnC.wldMgr.get(ctx.get().getSender().getEntityWorld().getDimensionKey()).getPlayers(packet.pos);
			for (Map.Entry<UUID, String> entry : map.entrySet()) {if (entry.getValue().equalsIgnoreCase(packet.str)) {return GnC.wldMgr.get(ctx.get().getSender().getEntityWorld().getDimensionKey()).removePlayer(packet.pos, entry.getKey());}}
			UUID player = ctx.get().getSender().getServer().getPlayerProfileCache().getGameProfileForUsername(packet.str).getId();
			return GnC.wldMgr.get(ctx.get().getSender().getEntityWorld().getDimensionKey()).addPlayer(packet.pos, player);
		})*/;
	
		public final BiFunction<PacketChunkDataToServer, Supplier<NetworkEvent.Context>, String> packetHandler;
		
		PkType(BiFunction<PacketChunkDataToServer, Supplier<NetworkEvent.Context>, String> packetHandler) { this.packetHandler = packetHandler;}
	}
	
	public PacketChunkDataToServer(PacketBuffer buf) {
		action = PkType.values()[buf.readVarInt()];
		pos = new ChunkPos3D(0,0,0);
		pos.readBytes(buf);
		id = buf.readUUID();
		dbl = buf.readDouble();
		igr = buf.readInt();
		str = buf.readUtf(32767);
	}
	
	/**
	 * Use with actions ABANDON, DISABLESUB, and CLEARWL only.
	 * @param action the PkType enum indiating what action to take with the provided data
	 * @param pos the chunk position being changed
	 */
	public PacketChunkDataToServer(PkType action, ChunkPos3D pos) {
		this.action = action;
		this.pos = pos;
	}
	
	/**
	 * Use with actions TEMPCLAIM, EXTEND, and GUILDCLAIM only
	 * @param action the PkType enum indiating what action to take with the provided data
	 * @param pos the chunk position being changed
	 * @param id the player or guild UUID executing the claim
	 */
	public PacketChunkDataToServer(PkType action, ChunkPos3D pos, UUID id) {
		this.action = action;
		this.pos = pos;
		this.id = id;
	}
	
	/**
	 * Use with action SELL only
	 * @param action the PkType enum indiating what action to take with the provided data
	 * @param pos the chunk position being changed
	 * @param value the price being sold for
	 */
	public PacketChunkDataToServer(PkType action, ChunkPos3D pos, double value) {
		this.action = PkType.SELL;
		this.pos = pos;
		this.dbl = value;
	}
	
	/**
	 * use with action UPDATESUB only
	 * @param action the PkType enum indiating what action to take with the provided data
	 * @param pos the chunk position being changed
	 * @param rate the rental price
	 * @param hours the rental duration in hours
	 */
	public PacketChunkDataToServer(PkType action, ChunkPos3D pos, double rate, int hours) {
		this.action = PkType.UPDATESUB;
		this.pos = pos;
		this.dbl = rate;
		this.igr = hours;
	}
	
	/**
	 * Use with actions PUBLIC and MINRANK only
	 * @param action the PkType enum indiating what action to take with the provided data
	 * @param pos the chunk position being changed
	 * @param change for PUBLIC action enter desired value.
	 * for MINRANK true equals an increase, false equals decrease.
	 */
	public PacketChunkDataToServer(PkType action, ChunkPos3D pos, boolean change) {
		this.action = action;
		this.pos = pos;
		this.igr = (change ? 1 : -1);
	}
	
	/**
	 * Use with action MEMBER, INTERACT, and BREAK only
	 * @param action the PkType enum indiating what action to take with the provided data
	 * @param pos the chunk position being changed
	 * @param name of member being added/removed from the list or raw NBT string of a WhitelistItem
	 */
	public PacketChunkDataToServer(PkType action, ChunkPos3D pos, String name) {
		this.action = action;
		this.pos = pos;
		this.str = name;
	}
	
	public void toBytes(PacketBuffer buf) {
		buf.writeVarInt(action.ordinal());
		buf.writeBytes(pos.writeBytes(buf));
		buf.writeUUID(id);
		buf.writeDouble(dbl);
		buf.writeInt(igr);
		buf.writeUtf(str);
		}
 	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			String resp = this.action.packetHandler.apply(this, ctx);
			//Gather updated information from current server status
			Guild guild = LogicGuilds.getGuildByMember(ctx.get().getSender().getUUID());
			ServerWorld server = (ServerWorld) ctx.get().getSender().level;
			ChunkPos3D center = new ChunkPos3D(ctx.get().getSender().xChunk, ctx.get().getSender().yChunk, ctx.get().getSender().zChunk);
			List<ChunkData> chunkData = new ArrayList<ChunkData>();
			for (int x = center.x-6; x <= center.x+6; x++) {
				for (int y = center.y-6; y < center.y+6; y++) {
					for (int z = center.z-6; z <= center.z+6; z++) {
						ChunkPos3D pos = new ChunkPos3D(x, y, z);
						chunkData.add(WorldWSD.get(server).getCap().get(pos));
					}
				}
			}
			double balG = guild.guildID.equals(ComVars.NIL) ? 0.0 : LogicMoney.getBalance(guild.guildID, AccountType.GUILD.rl);
			double balP = LogicMoney.getBalance(ctx.get().getSender().getUUID(), AccountType.PLAYER.rl);
			Networking.sendToClient(new PacketUpdateChunkManagerGui(balG, balP, guild, chunkData, resp), ctx.get().getSender()); 
		});
		return true;
	}
}
