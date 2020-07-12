package dicemc.gnc.testmaterial;

import java.util.function.Supplier;

import dicemc.gnc.GnC;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

public class MyWSD extends WorldSavedData implements Supplier<MyWSD>{
	private static final String DATA_NAME = GnC.MOD_ID + "somefilename";

	public MyWSD(String name) {super(name);}
	
	public MyWSD() {super(DATA_NAME);}

	@Override
	public void read(CompoundNBT nbt) {}

	@Override
	public CompoundNBT write(CompoundNBT compound) {return null;}
	
	public static MyWSD forWorld(ServerWorld world) {
		DimensionSavedDataManager storage = world.getSavedData();
		Supplier<MyWSD> sup = new MyWSD();
		MyWSD instance = (MyWSD) storage.getOrCreate(sup, GnC.MOD_ID);
		
		if (instance == null) {
			instance = new MyWSD();
			storage.set(instance);
		}
		return instance;
	}

	@Override
	public MyWSD get() {
		return this;
	}
}
