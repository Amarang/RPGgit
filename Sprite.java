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
	private int originx, originy;
	private Image image;
	private Image[] frames;
	private int numFrames = 5;
	private int TILESIZE = 20;
	private int currentFrame = 0;
	Random rand = new Random();
	private long previousTime = 0;
	private int msPerFrame = 100;
	private static int FRAMESPERDIRECTION;
	
	private boolean l = true;
    private boolean r = true;
    private boolean u = true;
    private boolean d = true;
	
	int xmove;
	int ymove;
	public Sprite() {
		isReady = false;
	}
	TileData td = new TileData();
	public Sprite(Image image, int x, int y, int TS) {
		System.out.println("instantiated single image sprite");
		isReady = true;
		multiframe = false;
		this.x = x;
		this.y = y;
		this.image = image;
		originx=x;
		originy=y;
		TILESIZE = TS;
	}
	
	public Sprite(Image[] images, int x, int y, int TS) {
		System.out.println("instantiated multi-image sprite");
		isReady = true;
		multiframe = true;
		this.x = x;
		this.y = y;
		originx=x;
		originy=y;
		this.frames = images;
		numFrames = images.length;
		TILESIZE = TS;
	}
	public void addX (int d0) {x+=d0;}
	public void addY (int d0) {y+=d0;}
	public void setPos(int x, int y) { this.x = x; this.y = y; }
	public int getX() { return x; }
	public int getY() { return y; }
	public void resetOrigin() { originx=x; originy=y; }
	public void reset() { x=originx; y=originy;}
	
	public boolean isReady() { return isReady; }
	
	public boolean isEmpty() {
		 if(multiframe) return (frames.length == 0);
		 else return (image == null);
	}
	
	public void drawSprite(Graphics g) {
		//System.out.println("S " + x + ", " + y);
		g.drawImage(image,x*(TILESIZE)/2,y*(TILESIZE)/2, this);
	}
	
	public void drawFrame(Graphics g, int frame) {
		g.drawImage(frames[frame],x*(TILESIZE)/2,y*(TILESIZE)/2, this);
		try {
		g.drawImage(frames[frame],x*(TILESIZE),y*(TILESIZE), this);
		} catch (Exception e) { System.out.println("tried to get frame " + frame); }
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
			drawFrame(g, currentFrame);
			if(previousTime == 0 || time - previousTime >= msPerFrame) {
				
				currentFrame++;
				previousTime = time;
			}
		}
		if(currentFrame == numFrames) {
			currentFrame = 0;
		}
	}
	
	public void updateAnimationP(Graphics g, long time, int facing, int framesPerDirection) {
		//update animation for player
		//if(!loop) {
			//drawFrame(g, currentFrame);
			drawFrame(g, facing*framesPerDirection);
			//so first 4 images must be cardinal directions (U R D L) (N E S W)
		//}
		
		if(running) {

			//System.out.println("drawing frame " + currentFrame);
			drawFrame(g, facing*framesPerDirection+currentFrame%framesPerDirection+1);
			System.out.println(facing*framesPerDirection+currentFrame%framesPerDirection+1);
			if(previousTime == 0 || time - previousTime >= msPerFrame) {
				
				currentFrame++;
				previousTime = time;
			}
		}
		if(currentFrame%framesPerDirection == framesPerDirection-1) {
			currentFrame = facing*framesPerDirection;
			//if(!loop)
			stop();
		}
		
	}
	
	public void updateAnimationRand(Graphics g, long time) {
		if(running) {
		
			drawFrame(g, currentFrame);
			
			
			if(previousTime == 0 || time - previousTime >= msPerFrame) {
			
				currentFrame=rand.nextInt(5);
				xmove=rand.nextInt(3);
				ymove=rand.nextInt(3);
				int XorY = rand.nextInt(3);
				if(XorY == 0) {
					if (xmove==0){moveLeft();}
					if (xmove==2){moveRight();}
				}
				if(XorY == 2) {
					if (ymove==0){moveDown();}
					if (ymove==2){moveUp();}
				}
				previousTime = time;
			}
		}
		if(currentFrame == numFrames) currentFrame = 0;
	}
	public void moveLeft() { if(canMoveLeft()) x--; }
    public void moveRight() { if(canMoveRight()) x++; }
    public void moveUp() { if(canMoveUp()) y--; }
    public void moveDown() { if(canMoveDown()) y++; }
	private boolean canMoveLeft() { return l; }
    private boolean canMoveRight() { return r; }
    private boolean canMoveUp() { return u; }
    private boolean canMoveDown() { return d; }    
	public void allowMove(int[] directions) {
		l = td.isWalkRestricted(directions[0]) ? false : true;
		r = td.isWalkRestricted(directions[1]) ? false : true;
		u = td.isWalkRestricted(directions[2]) ? false : true;
		d = td.isWalkRestricted(directions[3]) ? false : true;
	}
}