package dicemc.gnc.testmaterial;

import java.util.List;
import java.util.Map;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;

public class TestClass {
	public enum ClansEnum {UCHIHA,SENJU, HYUGA,NARA,UZUMAKI,ABURAME,AKIMICHI,NONE;}

	public ListNBT binaMapToNBT(Map<ClansEnum, List<String>> map) {
		ListNBT lnbt = new ListNBT();
		for (Map.Entry<ClansEnum, List<String>> entry : map.entrySet()) {
			CompoundNBT snbt = new CompoundNBT();
			ListNBT list = new ListNBT();
			for (int i = 0; i < entry.getValue().size(); i++) {
				list.add(StringNBT.valueOf(entry.getValue().get(i)));
			}
			snbt.putInt("clan", entry.getKey().ordinal());
			snbt.put("members", list);
			lnbt.add(snbt);
		}
		return lnbt;
	}
}


