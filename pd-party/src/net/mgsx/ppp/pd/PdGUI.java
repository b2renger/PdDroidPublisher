package net.mgsx.ppp.pd;

/**
 * Helper for Pure Data original GUI configuration. 
 */
public class PdGUI {

	public static int IEM_GUI_MAX_COLOR = 30;
	public static int iemgui_color_hex[] = {
		16579836, 10526880, 4210752, 16572640, 16572608,
		16579784, 14220504, 14220540, 14476540, 16308476,
		14737632, 8158332, 2105376, 16525352, 16559172,
		15263784, 1370132, 2684148, 3952892, 16003312,
		12369084, 6316128, 0, 9177096, 5779456,
		7874580, 2641940, 17488, 5256, 5767248
	};

	public static int getColor(int iemcolor) {
		int color = 0;		
	
		if(iemcolor < 0)
		{
			iemcolor = -1 - iemcolor;
			color = ((iemcolor & 0x3f000) << 6 )
					+ ((iemcolor & 0xfc0) << 4 )
					+ ((iemcolor & 0x3f) << 2 )
					+ 0xFF000000;
					//(iemcolor & 0xffffff) + 0xFF000000;
		}
		else
		{
			color = (iemgui_color_hex[iemcolor%IEM_GUI_MAX_COLOR] & 0xFFFFFF) | 0xFF000000;
		}
		
		return color;
	}

	public static int getColor24(int iemcolor) {
		int color = 0;		
	
		if(iemcolor < 0)
		{
			iemcolor = -1 - iemcolor;
			color = (iemcolor & 0xffffff) + 0xFF000000;
		}
		else
		{
			color = (iemgui_color_hex[iemcolor%IEM_GUI_MAX_COLOR] & 0xFFFFFF) | 0xFF000000;
		}
		
		return color;
	}

}
