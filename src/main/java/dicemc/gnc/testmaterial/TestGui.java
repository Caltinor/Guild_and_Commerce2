package dicemc.gnc.testmaterial;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

public class TestGui extends Screen{

	protected TestGui() { super(new StringTextComponent("Test")); }
	
	@Override
	protected void init() {		
		addButton(new Button(5, 5, 75, 20, new StringTextComponent("Exit"), button -> action()));
		addButton(new Button(80, 5, 75, 20, new StringTextComponent("Ascending"), button -> callAsc()));
		addButton(new Button(155, 5, 75, 20, new StringTextComponent("Descending"), button -> callDes()));
	}
	
	@Override
	public boolean isPauseScreen() {return false;}
	
	@Override
	public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
		//input if statement for the keycode being used.  guess the mappings
		return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
	}
	
	private void action() {minecraft.displayGuiScreen(null);}
	
	private void callAsc() {
		Networking.sendToServer(new PacketRequestData(0));
		action();
	}
	
	private void callDes() {
		Networking.sendToServer(new PacketRequestData(0));
		action();
	}

	@Override
	public void render(MatrixStack mStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(mStack);
		super.render(mStack, mouseX, mouseY, partialTicks);
	}
	
	public static void open() {Minecraft.getInstance().displayGuiScreen(new TestGui());}
}
