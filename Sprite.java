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

class Sprite extends Applet
{
	private boolean multiframe = false;
	private boolean isReady = false;
	private boolean running = false;
	private int x, y;
	private Image image;
	private Image[] frames;
	private int numFrames = 5;
	private int TILESIZE = 20;
	private int currentFrame = 0;
	private long previousTime = 0;
	private int msPerFrame = 100;
	
	public Sprite() {
		isReady = false;
	}
	
	public Sprite(Image image, int x, int y, int TS) {
		System.out.println("instantiated single image sprite");
		isReady = true;
		multiframe = false;
		this.x = x;
		this.y = y;
		this.image = image;
		TILESIZE = TS;
	}
	
	public Sprite(Image[] images, int x, int y, int TS) {
		System.out.println("instantiated multi-image sprite");
		isReady = true;
		multiframe = true;
		this.x = x;
		this.y = y;
		this.frames = images;
		numFrames = images.length;
		TILESIZE = TS;
	}
	public void addX (int d) {x+=d;}
	public void addY (int d) {y+=d;}
	public void setPos(int x, int y) { this.x = x; this.y = y; }
	public int getX() { return x; }
	public int getY() { return y; }
	
	
	public boolean isReady() { return isReady; }
	
	public boolean isEmpty() {
		 if(multiframe) return (frames.length == 0);
		 else return (image == null);
	}
	
	public void drawSprite(Graphics g) {
		g.drawImage(image,x*(TILESIZE/2),y*(TILESIZE/2), this);
	}
	
	public void drawFrame(Graphics g, int frame) {
		g.drawImage(frames[frame],x*(TILESIZE/2),y*(TILESIZE/2), this);
	}
	
	public void start() { running = true; }
	public void stop() { running = false; }
	
	public void setSpeed(int msPerFrame) {
		this.msPerFrame = msPerFrame;
	}
	
	public void updateAnimation(Graphics g, long time) {
		//time in milliseconds (System.currentTimeMillis() ?)
		/*if(currentFrame == 0) {
			previousTime = time;
			currentFrame++;
		}*/
		
		
		if(running) {
		
				//System.out.println("drawing frame " + currentFrame);
				drawFrame(g, currentFrame);
			if(previousTime == 0 || time - previousTime >= msPerFrame) {
			
				currentFrame++;
				previousTime = time;
			}
		}
		if(currentFrame == numFrames) currentFrame = 0;
	}
}