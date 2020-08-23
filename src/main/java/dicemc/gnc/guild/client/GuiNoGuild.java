package dicemc.gnc.guild.client;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import dicemc.gnc.guild.network.PacketNoGuildDataToServer;
import dicemc.gnc.setup.Config;
import dicemc.gnc.setup.Networking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraftforge.client.gui.ScrollPanel;
import net.minecraftforge.common.ForgeHooks;

public class GuiNoGuild extends Screen {
	private Screen parentScreen;
	private boolean inviteView = true;
	private DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
	//Objects
	private Button backButton, inviteToggle, openToggle, joinButton, rejectButton, createButton;
	private TextFieldWidget nameField;
	private InviteListPanel inviteList;
	//Given Variables
	private double balP;
	private List<String> invites = new ArrayList<String>();
	private List<String> openGuilds = new ArrayList<String>();
	
	public static void open(double balP, List<String> invites, List<String> openGuilds) {
		Screen parent = Minecraft.getInstance().currentScreen;
		Minecraft.getInstance().displayGuiScreen(new GuiNoGuild(parent, balP, invites, openGuilds));
	}
	
	public static void sync(double balP, List<String> invites, List<String> openGuilds) {
		GuiNoGuild screen = (GuiNoGuild) Minecraft.getInstance().currentScreen;
		screen.updateGui(balP, invites, openGuilds);
	}
	
	protected void updateGui(double balP, List<String> invites, List<String> openGuilds) {
		this.balP = balP;
		this.invites = invites;
		this.openGuilds = openGuilds;
		updateVisibility();
	}
	
	protected GuiNoGuild(Screen parentScreen, double balP, List<String> invites, List<String> openGuilds) {
		super(new StringTextComponent("No Guild Screen"));
		this.parentScreen = parentScreen;
		this.balP = balP;
		this.invites = invites;
		this.openGuilds = openGuilds;
	}
	
	@Override
	protected void init() {
		nameField = new TextFieldWidget(font, this.width/8, this.height/2, this.width/4, 20, new StringTextComponent(""));
		createButton = new Button(nameField.x, nameField.y+nameField.getHeight()+5, nameField.getWidth(), 20, new StringTextComponent("Create Guild"), button -> actionCreate());
		backButton = new Button(this.width/2-28, this.height-30, 75, 20, new StringTextComponent("Back"), button -> actionBack());
		inviteList = new InviteListPanel(minecraft, this.width/2-3, this.height/2,  this.height/4, this.width/2);
		inviteToggle = new Button(inviteList.x, inviteList.y-23, inviteList.width/2, 20, new StringTextComponent("Invites"), button -> actionInviteToggle());
		openToggle = new Button(inviteToggle.x+inviteToggle.getWidth(), inviteToggle.y, inviteToggle.getWidth(), 20, new StringTextComponent("Open Guilds"), button -> actionOpenToggle());
		joinButton = new Button(inviteList.x, inviteList.y+inviteList.height+3, inviteToggle.getWidth(), 20, new StringTextComponent("Join Guild"), button -> actionJoin());
		rejectButton = new Button(openToggle.x, joinButton.y, joinButton.getWidth(), 20, new StringTextComponent("Reject Invite"), button -> actionReject());
		addButton(createButton);
		addButton(backButton);
		addButton(inviteToggle);
		addButton(openToggle);
		addButton(joinButton);
		addButton(rejectButton);
		updateVisibility();
	}
	
	private void updateVisibility() {
		nameField.active = balP >= Config.GUILD_CREATE_COST.get();
		createButton.active = balP >= Config.GUILD_CREATE_COST.get();
		rejectButton.active = inviteView;
		inviteToggle.active = !inviteView;
		openToggle.active = inviteView;
		inviteList.clearInfo();
		inviteList.setInfo(inviteView ? invites : openGuilds);
	}

	@Override
	public boolean isPauseScreen() {return false;}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	public boolean charTyped(char ch, int a) {
		super.charTyped(ch, a);
		if (nameField.isFocused()) nameField.charTyped(ch, a);
		return true;
	}
	
	public boolean mouseScrolled(double mouseX, double mouseY, double amountScrolled) {
		super.mouseScrolled(mouseX, mouseY, amountScrolled);
		if (mouseX > inviteList.x && mouseX < inviteList.x+inviteList.width && mouseY > inviteList.y && mouseY < inviteList.y+inviteList.height)
			inviteList.mouseScrolled(mouseX, mouseY, amountScrolled);
		return true;
	}
	
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		nameField.mouseClicked(mouseX, mouseY, mouseButton);
		inviteList.mouseClicked(mouseX, mouseY, mouseButton);
		return false;
	}
	
	@Override
	public void render(MatrixStack mStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(mStack);
		this.drawString(mStack, font, "Create Guild Cost = $"+df.format(Config.GUILD_CREATE_COST.get()), nameField.x, nameField.y-30, 16777215);
		this.drawString(mStack, font, "Account $"+df.format(balP), nameField.x, nameField.y-15, 16777215);
		nameField.render(mStack, mouseX, mouseY, partialTicks);
		inviteList.render(mStack, mouseX, mouseY, partialTicks);
		super.render(mStack, mouseX, mouseY, partialTicks);
	}
	
	private void actionBack() {minecraft.displayGuiScreen(parentScreen);}
	private void actionInviteToggle() {inviteView = true; updateVisibility();}
	private void actionOpenToggle() {inviteView = false; updateVisibility();}
	private void actionJoin() {
		if (inviteList.selectedItem >= 0) {
			Networking.sendToServer(new PacketNoGuildDataToServer(PacketNoGuildDataToServer.PkType.JOIN, inviteList.getSelected()));
			this.closeScreen();
		}		
	}
	private void actionReject() {if (inviteList.selectedItem >= 0) Networking.sendToServer(new PacketNoGuildDataToServer(PacketNoGuildDataToServer.PkType.REJECT, inviteList.getSelected()));}
	private void actionCreate() {
		Networking.sendToServer(new PacketNoGuildDataToServer(PacketNoGuildDataToServer.PkType.CREATE, nameField.getText()));
		this.closeScreen();
	}
	
	class InviteListPanel extends ScrollPanel {
        private List<ITextProperties> lines = Collections.emptyList();
        public int x, y, width, height, selectedItem;

        public InviteListPanel(Minecraft mcIn, int width, int height, int y, int x)
        {
            super(mcIn, width, height, y, x);
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            selectedItem = -1;
        }

        public String getSelected() {return lines.get(selectedItem).getString();}
        
        void setInfo(List<String> lines) { this.lines = resizeContent(lines); }

        void clearInfo() { this.lines = Collections.emptyList(); }

        private List<ITextProperties> resizeContent(List<String> lines) {
            List<ITextProperties> ret = new ArrayList<>();
            if (lines.size() == 0) {
            	ITextComponent chat = ForgeHooks.newChatWithLinks("", false);
                int maxTextLength = this.width - 12;
            	ret.addAll(font.func_238420_b_().func_238362_b_(chat, maxTextLength, Style.EMPTY)); 
            	return ret;
            }
            for (String line : lines) {
                if (line == null) {
                    ret.add(null);
                    continue;
                }
                ITextComponent chat = ForgeHooks.newChatWithLinks(line, false);
                int maxTextLength = this.width - 12;
                if (maxTextLength >= 0) {
                    ret.addAll(font.func_238420_b_().func_238362_b_(chat, maxTextLength, Style.EMPTY));
                }
            }
            return ret;
        }

        @Override
        public int getContentHeight() {
            int height = 50;
            height += (lines.size() * font.FONT_HEIGHT);
            if (height < this.bottom - this.top - 8)
                height = this.bottom - this.top - 8;
            return height;
        }

        @Override
        protected int getScrollAmount() { return font.FONT_HEIGHT * 3; }

        @Override
        protected void drawPanel(MatrixStack mStack, int entryRight, int relativeY, Tessellator tess, int mouseX, int mouseY)
        {
            for (int i = 0; i < lines.size(); i++){
                if (lines.get(i) != null)
                {
                	if (i == selectedItem) {
                    	hLine(mStack, left, left+width, relativeY-1, Color.YELLOW.getRGB());
                    	hLine(mStack, left, left+width, relativeY-1+font.FONT_HEIGHT, Color.YELLOW.getRGB());
                    	vLine(mStack, left, relativeY-1, relativeY-1+font.FONT_HEIGHT, Color.YELLOW.getRGB());
                    	vLine(mStack, left+width-1, relativeY-1, relativeY-1+font.FONT_HEIGHT, Color.YELLOW.getRGB());
                    }
                    RenderSystem.enableBlend();
                    GuiNoGuild.this.font.func_238407_a_(mStack, lines.get(i), left+1, relativeY, 0xFFFFFF);
                    RenderSystem.disableAlphaTest();
                    RenderSystem.disableBlend();
                }
                relativeY += font.FONT_HEIGHT;
            }
        }

        private Style findTextLine(final int mouseX, final int mouseY) {
            double offset = (mouseY - top) + border + scrollDistance + 1;

            int lineIdx = (int) (offset / font.FONT_HEIGHT);
            if (lineIdx > lines.size() || lineIdx < 1)
                return null;

            ITextProperties line = lines.get(lineIdx-1);
            selectedItem = lineIdx-1;
            if (line != null)
            {
                return font.func_238420_b_().func_238357_a_(line, mouseX);
            }
            return null;
        }

        @Override
        public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
            final Style component = findTextLine((int) mouseX, (int) mouseY);
            if (component != null) {
                GuiNoGuild.this.handleComponentClicked(component);
                return true;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        protected void drawBackground() {}
	}	
}
