package dicemc.gnc.market.client;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import dicemc.gnc.common.client.DropDownBox;
import dicemc.gnc.common.client.SlideToggle;
import dicemc.gnclib.trade.entries.IMarketEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.gui.ScrollPanel;

public class GuiMarketManager extends Screen{
	//Control Variables
	private boolean isLoading = true;
	//UI Objects
	private ToggleWidget toggleFilter;
	private ImageButton buttonSort, buttonStorage, buttonHistory;
	private TextFieldWidget textboxName, textboxPriceFrom, textboxPriceTo;
	private DropDownBox marketSelector, filterVendorSelector;
	private SlideToggle slideOffer;
	private MarketPanel marketPanel;
	private Button applyFilter;
	//Data Objects
	Screen parentScreen;
	double balP;
	
	@SuppressWarnings("resource")
	public static void open(double balP) {
		Screen parent = Minecraft.getInstance().screen;
		Minecraft.getInstance().setScreen(new GuiMarketManager(parent, balP));
	}
	
	@SuppressWarnings("resource")
	public static void sync(List<IMarketEntry> newData) {
		GuiMarketManager screen = (GuiMarketManager) Minecraft.getInstance().screen;
		screen.updateGui(newData);
	}
	
	protected void updateGui(List<IMarketEntry> newData) {
		marketPanel.setData(newData);
		isLoading = false;
	}
	
	protected GuiMarketManager(Screen parent, double balP) {
		super(new StringTextComponent("MarketGui"));
		parentScreen = parent;
		this.balP = balP;
	}
	
	@Override
	protected void init() {
		toggleFilter 		= null;
		buttonSort 			= null;
		buttonStorage 		= null;
		buttonHistory 		= null;
		textboxName 		= null;
		textboxPriceFrom 	= null;
		textboxPriceTo 		= null;
		marketSelector 		= null;
		filterVendorSelector= null;
		slideOffer 			= null;
		marketPanel 		= null;
		applyFilter 		= null;
	}
	
	private void updateVisibility() {
		//Filter panel visibility
	}
	
	@Override
	public void render(MatrixStack mStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(mStack);
		//bind textures
		//draw gui title
		//print account balance
		/*
		 * draw objects
		 */
		
		if (toggleFilter.isStateTriggered()) {
			//subsection for filter UI descriptions
		}
		super.render(mStack, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean isPauseScreen() {return false;}
	
	private class MarketPanel extends ScrollPanel {
		private List<IMarketEntry> entries = new ArrayList<IMarketEntry>();

		public MarketPanel(Minecraft client, int width, int height, int top, int left, IMarketEntry entry) {
			super(client, width, height, top, left);
		}

		@Override
		protected int getContentHeight() {return 20;}
		
		public void setData(List<IMarketEntry> newData) {entries = newData;}

		@Override
		protected void drawPanel(MatrixStack mStack, int entryRight, int relativeY, Tessellator tess, int mouseX,
				int mouseY) {
			for (int i = 0; i < entries.size(); i++) {
				//TODO draw panel data
			}
			
		}
		
	}
}
