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
	private int fadeDuration = 1000; //milliseconds
	private int msPerFrame = 50; //milliseconds
	
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
		
		int numLines = 3;
		
		Composite original = g2d.getComposite();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLACK);
		
		g.fillRect(padding,height-thickness*numLines-padding,width-2*padding,thickness*numLines);
		
		
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		Font labelFont = new Font("Monospaced",Font.BOLD,15);	
		g.setFont(labelFont);
		g.setColor(Color.WHITE);
		
		String lines[] = {"dfdf", "dfdlfkjdf", "dfdklfjdfk"};
		
		for(int i = 0; i < lines.length; i++) {
			g.drawString(text,padding*2,height-thickness*i+15/2-3);
		}
		
		g.setColor(tempCol);
		g.setFont(tempFont);
		
		g2d.setComposite(original);
	
	}

    public void update(Graphics g, long time) {
		if(running) {
			draw(g, alpha);
			if(previousTime == 0 || time - previousTime >= msPerFrame) {
				alpha -= (float)(1.0 / fadeDuration * msPerFrame);
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
	
	public void setTextAndStart(String txt) {
		this.text = txt;
		start();
	}
	
	
	public String getText() {
		return text;
	}

}
