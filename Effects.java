import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

class Effects {
	
	private long tBegin = 0;
	private long tCurrent;
	private long tEnd = 1000;
	private boolean effect = false;
	private int appSizeX;
	private int appSizeY;
	
	private Color effCol;
	private String effStr = "";
	private float effAlpha;
	private int effAmp;
	
	public Effects(int aX, int aY) {
		appSizeX = aX;
		appSizeY = aY;
	}
	
	/*public void applyEffect(Graphics g, BufferedImage bf, String effectName) {
		if(effectName == "shake") {
			shake(g, bf);
		}
		switch(effectName) {
			case "none":
				draw(g, bf);
				break;
			case "shake":
				shake(g, bf);
				break;
			default:
				draw(g, bf);
				break;
		}
	}*/
	public void initEffectParams(int amplitude, int duration, Color color, float alpha) {
		tBegin = System.currentTimeMillis();
		tEnd = tBegin + duration;//duration in ms
		effect = true;
		effCol = color;
		effAmp = amplitude;
		effAlpha = alpha;
		
	}

	public void setEffect(String effectString) {
		effStr = effectString;		
	}
	
	public void draw(Graphics g, BufferedImage bf) {

		tCurrent = System.currentTimeMillis();
		double completion = (double)(tCurrent - tBegin)/(tEnd - tBegin);
		
		if(tCurrent > tEnd) effect = false;
		
		if(effect) {
			switch(effStr) {
				case "shake":
					shake(g, bf, tCurrent, completion);
					break;
				case "tint":
					tint(g, bf, tCurrent, completion, effCol, effAlpha);
					break;
				case "fadetint":
					fadeTint(g, bf, tCurrent, completion, effCol, effAlpha);
					break;
				default:
					break;
			}
		} else {
			g.drawImage(bf, 0, 0, null);
		}
	}
	public void shake(Graphics g, BufferedImage bf, long tcurr, double completion) {
		if(tCurrent > tEnd) return;

		int amplitude = (int)(effAmp*Math.pow((1-completion),2));
		double t = tCurrent%10000; //milliseconds
		double period = 0.4*Math.pow((1-completion),2); //seconds
		int x = (int)(amplitude * Math.sin(2.4*2*3.142/period*(t/1000)));
		int y = (int)(amplitude * Math.sin(3.1*2*3.142/period*(t/1000)));
		
		if(tCurrent < tEnd) {
			g.drawImage(bf, x, y, null);
		} else {
			g.drawImage(bf,0,0, null);
		}
	}
	
	public void fadeTint(Graphics g, BufferedImage bf, long tcurr, double completion, Color col, float alpha) {
		if(tCurrent > tEnd) return;
		Color tempCol = g.getColor();
		
		alpha = Math.max(0.01f,alpha*(float)(1-completion));
		
		int icol = (col.getRGB() + ((int)(alpha*256) << 24)); //woo, bit operation
		Color shade = new Color(icol, true);
		
		g.setColor(shade);
		
		if(tCurrent < tEnd) {
			g.drawImage(bf, 0,0, null);
			g.fillRect(0,0,appSizeX,appSizeY);
		} else {
			g.drawImage(bf,0,0, null);
		}
		
		g.setColor(tempCol);
	}
	
	public void tint(Graphics g, BufferedImage bf, long tcurr, double completion, Color col, float alpha) {
		if(tCurrent > tEnd) return;
		Color tempCol = g.getColor();
				
		int icol = (col.getRGB() + ((int)(alpha*256) << 24)); //woo, bit operation
		Color shade = new Color(icol, true);
		
		g.setColor(shade);
		
		if(tCurrent < tEnd) {
			g.drawImage(bf, 0,0, null);
			g.fillRect(0,0,appSizeX,appSizeY);
		} else {
			g.drawImage(bf,0,0, null);
		}
		
		g.setColor(tempCol);
	}
	
}