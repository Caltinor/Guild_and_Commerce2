package dicemc.gnc.land.client;

import java.text.DecimalFormat;

import com.mojang.blaze3d.matrix.MatrixStack;

import dicemc.gnc.GnC;
import dicemc.gnc.common.client.SlideToggle;
import dicemc.gnclib.guilds.entries.Guild;
import dicemc.gnclib.realestate.ChunkData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.gui.ScrollPanel;

public class GuiLandDetail extends Screen{
	public static final ResourceLocation MAP_BORDER = new ResourceLocation(GnC.MOD_ID+":guis/chunkgui.png");
	private DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
	//Global Objects
	private GuiLandWidget widget;
	private ITextComponent response = new StringTextComponent("");
	private int respX, respY;
	//Info Objects
	private String labelPrice, labelRenter, labelWLmode;
	private ImageButton buttonTempClaim, buttonGuildClaim;
	//Access Objects
	private String labelWLsettings, labelMinRankSettings;
	private WhitelistPanel panelWL;
	private MinRankPanel panelMR;
	private SlideToggle toggleExplosion, togglePublic;
	private Button buttonClearWL;
	//Rent Objects
	private String labelUntil, labelPermitted;
	private ImageButton buttonRent, buttonExtend, buttonSet;
	private Button buttonDisable, buttonPlayerAdd, buttonPlayerSub;
	private TextFieldWidget textboxPrice, textboxDuration, textboxAddPlayer;
	private PermittedPlayersPanel permittedPanel;
	//Given Variables
	private Screen parent;
	private double balP, balG;
	private ChunkData ckData;
	private Guild myGuild = Guild.getDefault();
	
	protected GuiLandDetail(GuiLandWidget widget) {
		super(new StringTextComponent("LandDetail"));
	}
	
	@Override
	public void render(MatrixStack mStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(mStack);
		Minecraft.getInstance().getTextureManager().bind(MAP_BORDER);
		super.render(mStack, mouseX, mouseY, partialTicks);
	}
	
//=====BUTTON ACTIONS==========================================================
	private void actionTempClaim() {}//Networking.sendToServer(new PacketChunkDataToServer(PacketChunkDataToServer.PkType.TEMPCLAIM, selectedCK, Minecraft.getInstance().player.getUniqueID()));}
	private void actionGuildClaim() {}//Networking.sendToServer(new PacketChunkDataToServer(PacketChunkDataToServer.PkType.GUILDCLAIM, selectedCK, Minecraft.getInstance().player.getUniqueID()));}
	private void actionAbandon() {}
	private void actionExtend() {}//Networking.sendToServer(new PacketChunkDataToServer(PacketChunkDataToServer.PkType.EXTEND, selectedCK, Minecraft.getInstance().player.getUniqueID()));}
	private void actionSell() {}
	private void actionUpdateSublet() {}
	private void actionPublicToggle() {}//Networking.sendToServer(new PacketChunkDataToServer(PacketChunkDataToServer.PkType.PUBLIC, selectedCK, !ckData.isPublic));} 
	private void actionMinRankIncrease() {}
	private void actionMinRankDecrease() {}
	private void actionDisableSublet() {} 
	private void actionBreakToggle() {} // UNTESTED
		/*WhitelistItem wlItem = ckData.whitelist.get(whiteList.selectedItem);
		wlItem.setCanBreak(!wlItem.getCanBreak());
		Networking.sendToServer(new PacketChunkDataToServer(PacketChunkDataToServer.PkType.INTERACT, selectedCK, wlItem.toNBT().toString()));*/
	private void actionInteractToggle() {} // UNTESTED
		/*WhitelistItem wlItem = ckData.whitelist.get(whiteList.selectedItem);
		wlItem.setCanBreak(!wlItem.getCanBreak());
		Networking.sendToServer(new PacketChunkDataToServer(PacketChunkDataToServer.PkType.INTERACT, selectedCK, wlItem.toNBT().toString()));*/
	private void actionClearWhitelist() {}//Networking.sendToServer(new PacketChunkDataToServer(PacketChunkDataToServer.PkType.CLEARWL, selectedCK));}
	private void actionMemberAdd() {}
		/*Networking.sendToServer(new PacketChunkDataToServer(PacketChunkDataToServer.PkType.MEMBER, selectedCK, playerAddField.getText()));
		playerAddField.setText("");*/
	private void actionMemberRemove() {}
		/*Networking.sendToServer(new PacketChunkDataToServer(PacketChunkDataToServer.PkType.MEMBER, selectedCK, playerList.getSelected()));
		playerList.selectedItem = 0;*/
	
//=====HELPER METHODS==========================================================
	private String whitelistType() {
		if (ckData.isPublic) return TextFormatting.GREEN+"PUBLIC";
		if (ckData.whitelist.size() == 0) {
			return ckData.permittedPlayers.size() == 0 ? TextFormatting.BLACK+"MEMBERS ONLY" : TextFormatting.RED+"RENTED (U)";
		}
		else { return ckData.permittedPlayers.size() == 0 ? TextFormatting.YELLOW+"SPECIAL ACCESS" : TextFormatting.RED+"RENTED (R)";}
	}
	
//=====CUSTOM OBJECTS==========================================================
	class WhitelistPanel extends ScrollPanel {

		public WhitelistPanel(Minecraft client, int width, int height, int top, int left) {
			super(client, width, height, top, left);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected int getContentHeight() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		protected void drawPanel(MatrixStack mStack, int entryRight, int relativeY, Tessellator tess, int mouseX,
				int mouseY) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class PermittedPlayersPanel extends ScrollPanel {

		public PermittedPlayersPanel(Minecraft client, int width, int height, int top, int left) {
			super(client, width, height, top, left);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected int getContentHeight() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		protected void drawPanel(MatrixStack mStack, int entryRight, int relativeY, Tessellator tess, int mouseX,
				int mouseY) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class MinRankPanel extends ScrollPanel {

		public MinRankPanel(Minecraft client, int width, int height, int top, int left) {
			super(client, width, height, top, left);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected int getContentHeight() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		protected void drawPanel(MatrixStack mStack, int entryRight, int relativeY, Tessellator tess, int mouseX,
				int mouseY) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
