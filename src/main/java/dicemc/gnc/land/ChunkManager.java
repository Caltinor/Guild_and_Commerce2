package dicemc.gnc.land;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dicemc.gnc.GnC;
import dicemc.gnc.datastorage.database.SQLBuilder;
import dicemc.gnc.datastorage.database.TableDefinitions;
import dicemc.gnc.datastorage.wsd.WorldWSD;
import dicemc.gnc.setup.Config;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ServerWorld;

public class ChunkManager {
	private Map<ChunkPos, ChunkData> cap = new HashMap<ChunkPos, ChunkData>();

	public ChunkManager() {}
	
	public String addChunk(ChunkPos pos) {
		//dummy code for compiler
		cap.put(pos, new ChunkData(pos));
		return "";}
	
	public String updateChunk(ChunkPos ck, Map<String, String> values) {
		//TODO populate switch statement
		ChunkData cd = cap.getOrDefault(ck, new ChunkData(ck));
		for (Map.Entry<String, String> vals : values.entrySet()) {
			switch(vals.getKey()) {
			case "price": {
				cd.price = Double.valueOf(vals.getValue());
				System.out.println("price updated" +String.valueOf(cap.get(ck).price));
				break;
			}
			default: return "Key Unrecognized" + vals.getKey();
			}
		}
		cap.put(ck, cd);
		return "Success";
	}
	
	public ChunkData getChunk(ChunkPos pos) {return cap.get(pos);}
	
	public String setWhitelist(ChunkPos ck, List<WhitelistItem> whitelist) {return "";}
	
	public String updateWhitelistItem(ChunkPos ck, WhitelistItem wlItem) {return "";}
	
	public String removeWhitelistItem(ChunkPos ck, String item) {return "";}
	
	public List<WhitelistItem> getWhitelist(ChunkPos ck) {return new ArrayList<WhitelistItem>();}
	
	public String addPlayer(ChunkPos ck, UUID player, boolean principal) {return "";}
	
	public String updatePlayer(ChunkPos ck, UUID player, boolean principal) {return "";}
	
	public String removePlayer(ChunkPos ck, UUID player) {return "";}
	
	public Map<UUID, String> getPlayers(ChunkPos ck) {return new HashMap<UUID, String>();}
	
	public void saveChunkData(ServerWorld world) {
		//TODO save to DB/WSD
		if (Config.WORLD_USE_DB.get()) {
			String table = TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.TABLE_NAME) + Config.DB_SUFFIX.get();
			String conditions = "";
			List<String> fields = new ArrayList<String>();
			//TODO declare all the fields
			Map<String, String> fav = new HashMap<String, String>();
			/*TODO find out the best way to iterate through the variables and string them to the list.
			 * for (int i = 0; i < fields.size(); i++) {	
				for (Map.Entry<ChunkPos, ChunkData> map : cap.entrySet()) {
					List<String> rawVal = new ArrayList<String>();
					rawVal.add(String.valueOf(map.getValue().pos.x));
					values.put(fields.get(i), rawVal);
				}
			}*/
			GnC.DBM_MAIN.executeUpdate(SQLBuilder.buildUPDATE(table, fields, fav, conditions));
		}
		else {
			for (Map.Entry<ChunkPos, ChunkData> map : cap.entrySet()) {
				WorldWSD.get(world).getChunks().put(map.getKey(), map.getValue());
			}
			WorldWSD.get(world).markDirty();
		}
	}
	
	public void loadChunkData(ChunkPos ck, ServerWorld world) {
		ChunkData cnk = new ChunkData(ck);
		if (Config.WORLD_USE_DB.get()) {
			String conditions = TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.CHUNK_X)+" = "+String.valueOf(ck.x)+
						" AND "+TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.CHUNK_Z)+" = "+String.valueOf(ck.z);
			String table = TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.TABLE_NAME) + Config.DB_SUFFIX.get();
			List<String> fields = new ArrayList<String>(); fields.add("*");
			ResultSet rs = GnC.DBM_MAIN.executeQuery(SQLBuilder.buildSELECT(table, fields, conditions));
			try { if (!rs.isBeforeFirst()) { 
					rs.next();
					cnk = new ChunkData(rs, ck);
					List<List<String>> list = new ArrayList<List<String>>();
					list.add(cnk.toDBList());
					GnC.DBM_MAIN.executeUpdate(SQLBuilder.buildINSERT(table, cnk.fieldList(), list));
				} 
			} catch (SQLException e) {e.printStackTrace();}
		}
		else {
			cnk = WorldWSD.get(world).getChunks().getOrDefault(ck, cnk);			
		}
		cap.put(ck, cnk);
	}
}
