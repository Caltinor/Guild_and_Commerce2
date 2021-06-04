package dicemc.gnc.common.client;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;

public class SlideToggle extends Widget{
	private boolean state;

	public SlideToggle(int x, int y, int width, int height, ITextComponent title, boolean startingMode) {
		super(x, y, width, height, title);
		state = startingMode;
	}
	
	public boolean getState() {return state;}

}
