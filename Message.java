import java.io.*;
import java.awt.*;
import java.applet.*;
import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import java.net.*;
import javax.imageio.*;
import java.util.Random;
import java.awt.event.*;
import java.lang.Math;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

class Message
{

    private int width;
    private int height;
	private float alpha;
	private int padding = 10;
	private int thickness = 20;
	private String text;
	
	private long previousTime = 0;
	private int defaultFadeDuration = 1000; //milliseconds
	
	private int fadeDuration = 1000; //milliseconds
	private int msPerFrame = 15; //milliseconds
	
	private boolean running;
		
	public Message(int width, int height) {
		this.width = width;
		this.height = height;
    }
	
	public void start() { this.running = true; this.alpha = 1F; }
	public void stop() { this.running = false; this.alpha = 0F; }
	
	public void setSpeed(int msPerFrame) {
		this.msPerFrame = msPerFrame;
	}
	
	public void draw(Graphics g, Float alpha) {
		Graphics2D g2d = (Graphics2D) g;
		Color tempCol = g.getColor();
		Font tempFont = g.getFont();
		
		//int numLines = 3;
		
		int lettersPerLine = 70;
		int numLines = (int)(text.length() / lettersPerLine + 1);
		
		
		Composite original = g2d.getComposite();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLACK);
		
		g.fillRoundRect(padding,height-thickness*numLines-padding*2,width-2*padding,thickness*numLines+padding, padding, padding);
		
		
		
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		Font labelFont = new Font("Monospaced",Font.BOLD,15);	
		g.setFont(labelFont);
		g.setColor(Color.WHITE);
		
		//System.out.println(text);
		
		
		//System.out.println("numlines should be " + numLines);
		//System.out.println("alpha " + alpha);
		//String lines[] = {"dfdf", "dfdlfkjdf", "dfdklfjdfk"};
		String[] lines = new String[numLines];
		
		
		for(int i = 0; i < numLines; i++) {
			try {
				lines[i] = text.substring(i,i+lettersPerLine);
			} catch (Exception e) {
				try {
					lines[i] = text.substring(i,text.length());
				} catch (Exception e2) {
					lines[i] = "";
				}
			}
			
		}
		
		for(int i = 0; i < lines.length; i++) {
			g.drawString(lines[i],padding*2,height-thickness*(i+1)-padding+15/2+2);
		}
		
		g.setColor(tempCol);
		g.setFont(tempFont);
		
		g2d.setComposite(original);
	
	}

    public void update(Graphics g, long time) {
		if(running) {
			draw(g, alpha);
			if(previousTime == 0 || time - previousTime >= msPerFrame) {
				alpha -= (float)(1.0 * (1.05-alpha)*3 / fadeDuration * msPerFrame);
				previousTime = time;
			}
			if(alpha <= 0) {
				stop();
			}
		}
	}
	
	public void setText(String txt) {
		this.text = txt;
	}
	
	public void setTextAndStart(String txt, int duration) {
		this.text = txt;
		this.fadeDuration = duration;
		start();
	}
	
	public void setTextAndStart(String txt) {
		this.text = txt;
		this.fadeDuration = defaultFadeDuration;
		start();
	}
	
	
	public String getText() {
		return text;
	}

}
