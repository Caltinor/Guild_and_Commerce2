package dicemc.gnc.common.client;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.gui.ScrollPanel;

public class DropDownBox extends TextFieldWidget{
	private ComboOptions comboList;
	public String selection = "";

	public DropDownBox(Minecraft mc, int x, int y, int width, int height, List<String> options) {
		super(mc.font, x, y, width, height, new StringTextComponent("dropdown"));
		comboList = new ComboOptions(mc, width, height, y+11, x, options);
	}
	
	@Override
	public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		//TODO draw the dropdown button
		//TODO render the options and scroll.  scroll panel as sub object?
	}
	
	public void setOptions(List<String> newOptions) {comboList.setOptions(newOptions);}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		//if mouse is over dropdown, adjust
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	class ComboOptions extends ScrollPanel {
		private Minecraft client;
		private List<String> options = new ArrayList<String>();
		public String selectedOption = "";

		public ComboOptions(Minecraft client, int width, int height, int top, int left, List<String> options) {
			super(client, width, height, top, left);
			this.client = client;
			this.options = options;
		}

		@Override
		protected int getContentHeight() {return 10;}
		
		public void setOptions(List<String> newOptions) {options = newOptions;}

		@Override
		protected void drawPanel(MatrixStack mStack, int entryRight, int relativeY, Tessellator tess, int mouseX,
				int mouseY) {
			for (int i = 0; i < options.size(); i++) {
				ComboOptions.drawString(mStack, client.font, options.get(i), 0, 10*i, 1677215);
			}
			// TODO Auto-generated method stub
			
		}
		
		@Override
        public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
			//TODO set selectedOption based on item clicked;
			return super.mouseClicked(mouseX, mouseY, button);
		}
		
	}
}
