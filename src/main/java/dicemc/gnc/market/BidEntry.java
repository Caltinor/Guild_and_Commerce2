package dicemc.gnc.market;

import java.sql.ResultSet;

import net.minecraft.nbt.CompoundNBT;

public class BidEntry {
	public int transactionID;
	public String vendor;
	public long dtgPlaced;
	public double value;
	
	public BidEntry(int transactionID, String vendor, long dtgPlaced, double value) {
		this.transactionID = transactionID;
		this.vendor = vendor;
		this.dtgPlaced = dtgPlaced;
		this.value = value;
	}
	
	public BidEntry(ResultSet rs) {}
	
	public BidEntry(CompoundNBT nbt) {}
	
	public CompoundNBT toNBT() {return new CompoundNBT();}
}
