package dicemc.gnc.common;

import org.lwjgl.glfw.GLFW;

import dicemc.gnc.GnC;
import dicemc.gnc.land.client.GuiLandDetail;
import dicemc.gnc.land.client.GuiLandWidget;
import dicemc.gnc.setup.Networking;
import dicemc.gnclib.guilds.entries.Guild;
import dicemc.gnclib.realestate.ChunkData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class GuiEventHandler {
	static ImageButton guildButton, marketsButton;
	static GuiLandWidget chunkWidget;
	public static final ResourceLocation INVENTORY_ADDITIONS = new ResourceLocation(GnC.MOD_ID+":guis/inventoryadditions.png");

	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onInventoryLoad(GuiScreenEvent.InitGuiEvent event) {
		if (event.getGui() instanceof InventoryScreen || event.getGui() instanceof GuiLandDetail) {
			int guiX = (event.getGui().width - 176)/2;
			int xOffset = 80 + 33;
			int guiY = (event.getGui().height - 166)/2;
			//chunkButton = 	new ImageButton(guiX +xOffset + 3, guiY - 32, 32, 32, 157, 0, 32, INVENTORY_ADDITIONS, button -> chunkGuiLoad());
			chunkWidget = new GuiLandWidget(event.getGui().getMinecraft().font, guiX, guiY-32, xOffset, 32
					, new StringTextComponent("chunkWidget"), 0, 0, Guild.getDefault(), ChunkData.getPlaceholder(), true, guiX, guiY-43);
			marketsButton = new ImageButton(guiX +xOffset, guiY - 32, 32, 32, 190, 0, 32, INVENTORY_ADDITIONS, button -> marketGuiLoad());
			guildButton = 	new ImageButton(guiX +xOffset +30, guiY - 32, 32, 32, 124, 0, 32, INVENTORY_ADDITIONS, button -> guildGuiLoad());
			event.addWidget(marketsButton);
			event.addWidget(guildButton);
			event.addWidget(chunkWidget);
			Networking.sendToServer(new PacketGuiRequest(PacketGuiRequest.gui.CHUNK));
		}
	}
	
	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onKeyPress(KeyInputEvent event) {
		if (Minecraft.getInstance().screen instanceof InventoryScreen && event.getAction() == GLFW.GLFW_PRESS) {
			if (event.getKey() == 49) {chunkGuiLoad();}
			if (event.getKey() == 50) {marketGuiLoad();}
			if (event.getKey() == 51) {guildGuiLoad();}
		}
	}
	
	private static void chunkGuiLoad() {}
	private static void marketGuiLoad() {Networking.sendToServer(new PacketGuiRequest(PacketGuiRequest.gui.MARKET));}
	private static void guildGuiLoad() {Networking.sendToServer(new PacketGuiRequest(PacketGuiRequest.gui.GUILD));}
}
