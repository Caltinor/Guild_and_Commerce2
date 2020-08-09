package dicemc.gnc.land.client;

import java.text.DecimalFormat;

import com.mojang.blaze3d.matrix.MatrixStack;

import dicemc.gnc.GnC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.gui.ScrollPanel;

public class GuiLandManager extends Screen{
	public static final ResourceLocation MAP_BORDER = new ResourceLocation(GnC.MOD_ID+":guis/chunkgui.png");
	private Screen parentScreen;
	private DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
	private String header;
	private boolean chunkView = true;
	private boolean overlayView = false;
	//Objects
	private Button backButton, chunkButton, subletButton, overlayButton;
	private Button tempClaimButton, guildClaimButton, abandonButton, extendButton, sellButton;
	private Button updateSubletButton, publicToggleButton, minRankIncreaseButton, minRankDecreaseButton, disableSubletButton, 
		breakToggleButton, interactToggleButton, clearWhitelistButton, memberAddButton, memberRemoveButton; 
	private TextFieldWidget sellField, subletCostField, subletDurationField, playerAddField;
	private PlayerListPanel playerList, whiteList;
	//Given Variables
	private double balP;
	
	public static void open(double balP) {
		Screen parent = Minecraft.getInstance().currentScreen;
		Minecraft.getInstance().displayGuiScreen(new GuiLandManager(parent, balP));
	}
	
	protected GuiLandManager(Screen parentScreen, double balP) {
		super(new StringTextComponent("Land Manager"));
		this.parentScreen = parentScreen;		
		this.balP = balP;
	}
	
	@Override
	protected void init() {
		header = "Account: $"+df.format(balP);
		int xq1 = this.width/4;
		int xq2 = this.width/2;
		int xq3 = (this.width/4)*3;
		int yq1 = this.height/4;
		//int yq2 = this.height/2;
		int yq3 = (this.height/4)*3;
		backButton = new Button(xq2-75, this.height-30, 75, 20, new StringTextComponent("Back"), button -> actionBack());
		chunkButton = new Button(xq2, 3, xq1, 20, new StringTextComponent("Chunk View"), button -> actionChunkToggle());
		subletButton = new Button(xq3, 3, xq1, 20, new StringTextComponent("Sublet View"), button -> actionSubletToggle());
		overlayButton = new Button(5, this.height-30, xq1, 20, new StringTextComponent("Overlay: Off"), button -> actionOverlayToggle());
		playerList = new PlayerListPanel(minecraft, xq1, yq1-9, yq3, xq2+3);
		//Claim Toggle Objects
		tempClaimButton = new Button(xq2+3, 28, xq1-6, 20, new StringTextComponent("Temp Claim"), button -> actionTempClaim());
		guildClaimButton = new Button(xq2+3, tempClaimButton.y + tempClaimButton.getHeight()+ 10, xq1-6, 20, new StringTextComponent("Guild Claim"), button -> actionGuildClaim());
		abandonButton = new Button(xq2+3, guildClaimButton.y + guildClaimButton.getHeight() + 3, xq1-6, 20, new StringTextComponent("Abandon Claim"), button -> actionAbandon());
		extendButton = new Button(xq3, tempClaimButton.y, xq1-6, 20, new StringTextComponent("Extend Time"), button -> actionExtend());
		sellButton = new Button(xq3, guildClaimButton.y, xq1-6, 20, new StringTextComponent("Sell Claim"), button -> actionSell());
		sellField = new TextFieldWidget(font, xq3, abandonButton.y, xq1-6, 20, new StringTextComponent("")); //TODO replace text with land current value
		//Sublet Toggle Objects
		subletCostField = new TextFieldWidget(font, xq2+3, 41, ((xq2-6)/3), 20, new StringTextComponent(""));
		subletDurationField = new TextFieldWidget(font, subletCostField.x + subletCostField.getWidth(), subletCostField.y, subletCostField.getWidth(), 20, new StringTextComponent(""));
		updateSubletButton = new Button(subletDurationField.x + subletDurationField.getWidth()+ 3, subletCostField.y, subletCostField.getWidth()-5, 20, new StringTextComponent("Update Rent"), button -> actionUpdateSublet());
		publicToggleButton = new Button(xq2+3, subletCostField.y + subletCostField.getHeight() + 3, 75, 20, new StringTextComponent("Public: No"), button -> actionPublicToggle());
		whiteList = new PlayerListPanel(minecraft, updateSubletButton.x-(xq2+3), (playerList.y-13)-(publicToggleButton.y+publicToggleButton.getHeight()+13), (publicToggleButton.y+publicToggleButton.getHeight()+13), playerList.x);
		disableSubletButton = new Button(updateSubletButton.x, publicToggleButton.y, updateSubletButton.getWidth(), 20, new StringTextComponent("Disable Rent"), button -> actionDisableSublet());
		minRankIncreaseButton = new Button(disableSubletButton.x - 18, disableSubletButton.y, 15, 20, new StringTextComponent("+"), button -> actionMinRankIncrease());
		minRankDecreaseButton = new Button(minRankIncreaseButton.x-18, minRankIncreaseButton.y, 15, 20, new StringTextComponent("-"), button -> actionMinRankDecrease());
		breakToggleButton = new Button(disableSubletButton.x, whiteList.y, updateSubletButton.getWidth(), 20, new StringTextComponent("Break"), button -> actionBreakToggle());
		interactToggleButton = new Button(breakToggleButton.x, breakToggleButton.y + breakToggleButton.getHeight() + 3, updateSubletButton.getWidth(), 20, new StringTextComponent("Interact"), button -> actionInteractToggle());
		clearWhitelistButton = new Button(interactToggleButton.x, whiteList.bottom-20, updateSubletButton.getWidth(), 20, new StringTextComponent("Clear List"), button -> actionClearWhitelist());
		playerAddField = new TextFieldWidget(font, playerList.right+3, playerList.y, this.width-playerList.right-12, 20, new StringTextComponent(""));
		memberRemoveButton = new Button(playerAddField.x, playerAddField.y+playerAddField.getHeight()+3, playerAddField.getWidth()/2, 20, new StringTextComponent("Remove"), button -> actionMemberRemove());
		memberAddButton = new Button(memberRemoveButton.x+memberRemoveButton.getWidth(), memberRemoveButton.y, memberRemoveButton.getWidth(), 20, new StringTextComponent("Add"), button -> actionMemberAdd());
		//final touches
		addButton(backButton);
		addButton(chunkButton);
		addButton(subletButton);
		addButton(overlayButton);
		addButton(tempClaimButton);
		addButton(guildClaimButton);
		addButton(abandonButton);
		addButton(extendButton);
		addButton(sellButton);
		addButton(updateSubletButton);
		addButton(publicToggleButton);
		addButton(disableSubletButton);
		addButton(minRankIncreaseButton);
		addButton(minRankDecreaseButton);
		addButton(breakToggleButton);
		addButton(interactToggleButton);
		addButton(clearWhitelistButton);
		addButton(memberRemoveButton);
		addButton(memberAddButton);
		updateVisibility();
	}
	
	private void updateVisibility() {
		chunkButton.active = !chunkView;
		subletButton.active = chunkView;
		overlayButton.setMessage(overlayView ? new StringTextComponent("Overlay: On") : new StringTextComponent("Overlay: Off"));
		
		//chunk toggle items
		tempClaimButton.visible = chunkView;
		guildClaimButton.visible = chunkView;
		abandonButton.visible = chunkView;
		extendButton.visible = chunkView;
		sellButton.visible = chunkView;
		sellField.visible = chunkView;
		//sublet toggle items
		updateSubletButton.visible = !chunkView;
		publicToggleButton.visible = !chunkView;
		disableSubletButton.visible = !chunkView;
		minRankIncreaseButton.visible = !chunkView;
		minRankDecreaseButton.visible = !chunkView;
		breakToggleButton.visible = !chunkView;
		interactToggleButton.visible = !chunkView;
		clearWhitelistButton.visible = !chunkView;
		memberRemoveButton.visible = !chunkView;
		memberAddButton.visible = !chunkView;
		subletCostField.visible = !chunkView;
		subletDurationField.visible = !chunkView;
		playerAddField.visible = !chunkView;
	}
	
	@Override
	public boolean isPauseScreen() {return false;}
	
	@Override
	public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
		//input if statement for the keycode being used.  guess the mappings
		return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
	}
	
	@Override
	public void render(MatrixStack mStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(mStack);
		minecraft.getTextureManager().bindTexture(MAP_BORDER);
		int d = (this.width/2) > (this.height-55) ? this.height-55 : this.width/2;
		blit(mStack, ((this.width/2)-d)/2, 25, d, d, 0, 0, 256, 256, 256, 256);
		blit(mStack, this.width/2, 25, (this.width/2)-3, this.height-28, 0, 0, 256, 256, 256, 256);
		this.drawString(mStack, this.font, header, 5, 5, 16777215);		
		this.drawString(mStack, font, TextFormatting.BOLD+"Permitted Players:", playerList.x, playerList.y-10, 1677215);
		sellField.render(mStack, mouseX, mouseY, partialTicks);
		playerList.render(mStack, mouseX, mouseY, partialTicks);
		subletCostField.render(mStack, mouseX, mouseY, partialTicks);
		subletDurationField.render(mStack, mouseX, mouseY, partialTicks);
		playerAddField.render(mStack, mouseX, mouseY, partialTicks);
		super.render(mStack, mouseX, mouseY, partialTicks);
		if (chunkView) {
			this.drawString(mStack, font, TextFormatting.BOLD+""+TextFormatting.DARK_RED+"Outpost = No", abandonButton.x, abandonButton.y + abandonButton.getHeight() + 10, 16777215);
			this.drawString(mStack, font, TextFormatting.BOLD+""+TextFormatting.DARK_PURPLE+"Whitelist Type: Default", abandonButton.x, abandonButton.y + abandonButton.getHeight() + 25, 16777215);
		}
		if (!chunkView) {
			this.drawString(mStack, font, "Min Rank:", publicToggleButton.x + publicToggleButton.getWidth() + 3, publicToggleButton.y, 16777215);
			this.drawString(mStack, font, "Leader", publicToggleButton.x + publicToggleButton.getWidth() + 3, publicToggleButton.y+11, 16777215); //TODO replace text with rank variable	
			this.drawString(mStack, font, TextFormatting.BOLD+"Whitelist: "+TextFormatting.RED+"Break"+TextFormatting.BLUE+" Interact", whiteList.x, whiteList.y-10, 16777215);
			this.drawString(mStack, font, TextFormatting.DARK_GRAY+"Sublet Rate:", subletCostField.x, subletCostField.y-10, 16777215);
			this.drawString(mStack, font, TextFormatting.BLACK+"Rent Duration (in Hours)", subletDurationField.x, subletDurationField.y-10, 16777215);
			whiteList.render(mStack, mouseX, mouseY, partialTicks);
		}
	}
	
	private void actionBack() {minecraft.displayGuiScreen(parentScreen);}
	private void actionChunkToggle() {chunkView = true; updateVisibility();}
	private void actionSubletToggle() {chunkView = false; updateVisibility();}
	private void actionOverlayToggle() {overlayView = overlayView ? false : true; updateVisibility();}
	private void actionTempClaim() {}
	private void actionGuildClaim() {}
	private void actionAbandon() {}
	private void actionExtend() {}
	private void actionSell() {}
	private void actionUpdateSublet() {}
	private void actionPublicToggle() {} 
	private void actionMinRankIncrease() {}
	private void actionMinRankDecrease() {}
	private void actionDisableSublet() {} 
	private void actionBreakToggle() {}
	private void actionInteractToggle() {}
	private void actionClearWhitelist() {}
	private void actionMemberAdd() {}
	private void actionMemberRemove() {} 
	
	class PlayerListPanel extends ScrollPanel {
		public int x, y, right, bottom;

		public PlayerListPanel(Minecraft client, int width, int height, int top, int left) {
			super(client, width, height, top, left);
			x = left;
			y = top;
			right = x + width;
			bottom = y + height;
			// TODO Add to params populating data
		}
		
		public void refresh() {
			//TODO add parameter to update new data
		}

		@Override
		protected int getContentHeight() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		protected void drawPanel(MatrixStack mStack, int entryRight, int relativeY, Tessellator tess, int mouseX, int mouseY) {
			// TODO Auto-generated method stub			
		}
		
	}
}
