package dicemc.gnc.land.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import dicemc.gnc.GnC;
import dicemc.gnc.account.AccountUtils;
import dicemc.gnc.guild.Guild;
import dicemc.gnc.land.client.GuiLandManager.ChunkSummary;
import dicemc.gnc.setup.Networking;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketChunkDataToServer {
	private final PkType action;
	private final ChunkPos pos;
	private UUID id = GnC.NIL;
	private double dbl = 0d;
	private int igr = 0;
	private String str = "";
		
	public enum PkType {TEMPCLAIM, GUILDCLAIM, ABANDON, EXTEND, SELL, UPDATESUB, PUBLIC, MINRANK, DISABLESUB, BREAK, INTERACT, CLEARWL, MEMBER}
	
	public PacketChunkDataToServer(PacketBuffer buf) {
		action = PkType.values()[buf.readVarInt()];
		pos = new ChunkPos(buf.readLong());
		id = buf.readUniqueId();
		dbl = buf.readDouble();
		igr = buf.readInt();
		str = buf.readString();
	}
	
	/**
	 * Use with actions ABANDON, DISABLESUB, and CLEARWL only.
	 * @param action the PkType enum indiating what action to take with the provided data
	 * @param pos the chunk position being changed
	 */
	public PacketChunkDataToServer(PkType action, ChunkPos pos) {
		this.action = action;
		this.pos = pos;
	}
	
	/**
	 * Use with actions TEMPCLAIM, EXTEND, and GUILDCLAIM only
	 * @param action the PkType enum indiating what action to take with the provided data
	 * @param pos the chunk position being changed
	 * @param id the player or guild UUID executing the claim
	 */
	public PacketChunkDataToServer(PkType action, ChunkPos pos, UUID id) {
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
	public PacketChunkDataToServer(PkType action, ChunkPos pos, double value) {
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
	public PacketChunkDataToServer(PkType action, ChunkPos pos, double rate, int hours) {
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
	public PacketChunkDataToServer(PkType action, ChunkPos pos, boolean change) {
		this.action = action;
		this.pos = pos;
		this.igr = (change ? 1 : -1);
	}
	
	/**
	 * Use with actions INTERACT and BREAK only
	 * @param action the PkType enum indiating what action to take with the provided data
	 * @param pos the chunk position being changed
	 * @param setting the desired setting for the whitelist item
	 * @param item the string of the block or entity being changed
	 */
	public PacketChunkDataToServer(PkType action, ChunkPos pos, boolean setting, String item) {
		this.action = action;
		this.pos = pos;
		this.igr = (setting ? 1 : 0);
		this.str = item;
	}
	
	/**
	 * Use with action MEMBER only
	 * @param action the PkType enum indiating what action to take with the provided data
	 * @param pos the chunk position being changed
	 * @param name of member being added/removed from the list
	 */
	public PacketChunkDataToServer(PkType action, ChunkPos pos, String name) {
		this.action = PkType.MEMBER;
		this.pos = pos;
		this.str = name;
	}
	
	public void toBytes(PacketBuffer buf) {
		buf.writeVarInt(action.ordinal());
		buf.writeLong(pos.asLong());
		buf.writeUniqueId(id);
		buf.writeDouble(dbl);
		buf.writeInt(igr);
		buf.writeString(str);
		}
 	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			String resp = "";
			outer: switch (action) {
			case TEMPCLAIM: {
				resp = GnC.ckMgr.tempClaim(pos, id);
				break;
			}
			case GUILDCLAIM: {
				resp = GnC.ckMgr.guildClaim(pos, id);
				break;
			}
			case ABANDON: {
				break;
			}
			case EXTEND: {
				resp = GnC.ckMgr.extendClaim(pos, id);
				break;
			}
			case SELL: {
				break;
			}
			case UPDATESUB: {
				break;
			}
			case PUBLIC: {
				resp = GnC.ckMgr.publicToggle(pos, igr == 1);
				break;
			}
			case MINRANK: {
				break;
			}
			case DISABLESUB: {
				break;
			}
			case BREAK: {
				break;
			}
			case INTERACT: {
				break;
			}
			case CLEARWL: {
				break;
			}
			case MEMBER: {
				Map<UUID, String> map = GnC.ckMgr.getPlayers(pos);
				for (Map.Entry<UUID, String> entry : map.entrySet()) {if (entry.getValue().equalsIgnoreCase(str)) {resp = GnC.ckMgr.removePlayer(pos, entry.getKey()); break outer;}}
				UUID player = ctx.get().getSender().getServer().getPlayerProfileCache().getGameProfileForUsername(str).getId();
				resp = GnC.ckMgr.addPlayer(pos, player);
				break;
			}
			default:
			}
			//Gather updated information from current server status
			UUID gid = GnC.NIL;
			Guild guild = new Guild("N/A", GnC.NIL);
			if (GnC.gMgr.getGuildByMember(ctx.get().getSender().getUniqueID()) != null) {
				guild = GnC.gMgr.getGuildByMember(ctx.get().getSender().getUniqueID());
			}
			ServerWorld server = ctx.get().getSender().getServerWorld();
			ChunkPos center = new ChunkPos(ctx.get().getSender().chunkCoordX, ctx.get().getSender().chunkCoordZ);
			List<ChunkSummary> chunkData = new ArrayList<ChunkSummary>();
			for (int x = center.x-6; x <= center.x+6; x++) {
				for (int z = center.z-6; z <= center.z+6; z++) {
					ChunkPos pos = new ChunkPos(x, z);
					chunkData.add(new ChunkSummary(GnC.ckMgr.getChunk(pos), ownerName(pos, server)));
				}
			}
			double balG = gid.equals(GnC.NIL) ? 0.0 : AccountUtils.getBalance(gid);
			double balP = AccountUtils.getBalance(ctx.get().getSender().getUniqueID());
			Networking.sendToClient(new PacketUpdateChunkManagerGui(balG, balP, guild, chunkData, resp), ctx.get().getSender()); 
		});
		return true;
	}
	
	private String ownerName(ChunkPos pos, ServerWorld server) {
		if (GnC.ckMgr.getChunk(pos).owner.equals(GnC.NIL)) {return "Unowned";}
		if (GnC.gMgr.getGuildByID(GnC.ckMgr.getChunk(pos).owner) != null) {return GnC.gMgr.getGuildByID(GnC.ckMgr.getChunk(pos).owner).name;}
		if (server.getPlayerByUuid(GnC.ckMgr.getChunk(pos).owner) != null) {return server.getPlayerByUuid(GnC.ckMgr.getChunk(pos).owner).getDisplayName().toString();}
		return "Logical Error";
	}
}
