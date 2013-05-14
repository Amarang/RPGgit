import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.awt.*;


class Effects {
	
	private long tBegin = 0;
	private long tCurrent;
	private long tEnd = 1000;
	public boolean effect = false;
	Random rand = new Random();
	
	public Effects() {
		
	}
	
	/*public void applyEffect(Graphics g, BufferedImage bf, String effectName) {
		if(effectName == "earthquake") {
			earthquake(g, bf);
		}
		switch(effectName) {
			case "none":
				draw(g, bf);
				break;
			case "earthquake":
				earthquake(g, bf);
				break;
			default:
				draw(g, bf);
				break;
		}
	}*/
	public void initEffectParams(int strength, int duration) {
		tBegin = System.currentTimeMillis();
		tEnd = tBegin + duration;//duration in ms
		effect = true;
		
	}
	
	
	public void draw(Graphics g, BufferedImage bf,int effectChoice) {

		tCurrent = System.currentTimeMillis();
		double completion = (double)(tCurrent - tBegin)/(tEnd - tBegin);
		
		if(tCurrent > tEnd) effect = false;
		
		if(effect) {
			switch(effectChoice)
			{
				case 0:
					earthquake(g, bf, tCurrent, completion);
					break;
				case 1:
					attacked(g, tCurrent, completion); 
					break;
			}
		} else {
			g.drawImage(bf, 0, 0, null);
		}
	}
	public void draw(Graphics g,int effectChoice) {

		tCurrent = System.currentTimeMillis();
		double completion = (double)(tCurrent - tBegin)/(tEnd - tBegin);
		
		if(tCurrent > tEnd) effect = false;
		
		if(effect) {
			switch(effectChoice)
			{
				case 0:
					earthquake(g, tCurrent, completion);
					break;
				case 1:
					attacked(g, tCurrent, completion); 
					break;
			}
		} else {
			//g.drawImage(bf, 0, 0, null);
		}
	}
	public void earthquake(Graphics g, BufferedImage bf, long tcurr, double completion) {
		
		if(tCurrent > tEnd) return;
		System.out.println(completion);
		int amplitude = (int)(5*Math.pow((1-completion),2));
		double t = tCurrent%10000; //milliseconds
		double period = 0.4*Math.pow((1-completion),2); //seconds
		int x = (int)(amplitude * Math.sin(2.4*2*3.142/period*(t/1000)));
		int y = (int)(amplitude * Math.sin(3.1*2*3.142/period*(t/1000)));
		//int y = x;
		
		if(tCurrent < tEnd) {
			g.drawImage(bf, x, y, null);
		} else {
			g.drawImage(bf,0,0, null);
		}
	}
	public void earthquake(Graphics g, long tcurr, double completion) {
		
		if(tCurrent > tEnd) return;
		System.out.println(completion);
		int amplitude = (int)(5*Math.pow((1-completion),2));
		double t = tCurrent%10000; //milliseconds
		double period = 0.4*Math.pow((1-completion),2); //seconds
		int x = (int)(amplitude * Math.sin(2.4*2*3.142/period*(t/1000)));
		int y = (int)(amplitude * Math.sin(3.1*2*3.142/period*(t/1000)));
		//int y = x;
		
		if(tCurrent < tEnd) {
			//g.drawImage(bf, x, y, null);
		} else {
			//g.drawImage(bf,0,0, null);
		}
	}
	public void attacked(Graphics g, long tcurr, double completion) {
		
		if(tCurrent > tEnd) return;
		if(tCurrent < tEnd) {
			g.setColor(Color.RED);
			for (int i = 0; i<2000;i++)
			{	
			g.fillOval((int)(rand.nextGaussian()*50)+400,(int)(rand.nextGaussian()*50)+300,rand.nextInt(5),rand.nextInt(5));	
			}
			
		} else {
			//g.drawImage(bf,0,0, null);
		}	
	}
	
}