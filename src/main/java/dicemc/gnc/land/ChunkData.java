package dicemc.gnc.land;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dicemc.gnc.GnC;
import dicemc.gnc.setup.Config;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.ChunkPos;

public class ChunkData {
	public ChunkPos pos;
	public UUID owner = GnC.NIL;
	public UUID renter = GnC.NIL;
	public double price = Config.DEFAULT_LAND_PRICE.get();
	public double leasePrice = -1;
	public long leaseDuration = 0;
	public int permMin = 1;
	public long rendEnd = System.currentTimeMillis() + Config.TEMPCLAIM_DURATION.get();
	public boolean isPublic = false;
	public boolean isForSale = false;
	public boolean isOutpost = false;
	public boolean canExplode = true;
	public List<WhitelistItem> whitelist = new ArrayList<WhitelistItem>();
	public Map<UUID, String> permittedPlayers = new HashMap<UUID, String>();
	
	public ChunkData(ChunkPos pos) {this.pos = pos;}
	
	public ChunkData(CompoundNBT nbt) {}
	
	public CompoundNBT toNBT() {return new CompoundNBT();}
}
