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

	private long previousTime = 0;
	
	private int fadeDuration = 1000; //milliseconds
	private int msPerFrame = 100; //milliseconds
	
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
		
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		//g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLACK);
		
		g.fillRect(padding,height-thickness-padding,width-2*padding,thickness);
		
		
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9F));
		Font labelFont = new Font("Monospaced",Font.BOLD,15);	
		g.setFont(labelFont);
		g.setColor(Color.WHITE);
  		g.drawString("Oops, try again",padding*2,height-thickness+15/2-3);
		
		g.setColor(tempCol);
		g.setFont(tempFont);
		
		//alpha += 0.01;
	
	}

    public void update(Graphics g, long time) {
	
		
		//draw(g);
		//so first 4 images must be cardinal directions (U R D L) (N E S W)
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

}
