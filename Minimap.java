import java.io.*;
import java.awt.*;
import java.applet.*;
import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import java.net.*;
import java.awt.event.*;
import java.lang.Math;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.util.List;

class Minimap
{

	private int appSizeX;
	private int appSizeY;
	private int tilesize;
	private int[] mapwidth;
	private int[] mapheight;
	private TileMap[] theMap;
	private Image[] tileImages;
	
	private Player p;
    
	private static int NUMSTATES = 2;
	private int state = 1;

	public Minimap(int[] mapwidth, int[] mapheight, int tilesize, TileMap[] theMap, Image[] tileImages, Player p, Dimension d) {
		this.mapwidth = mapwidth;
		this.mapheight = mapheight;
		this.tilesize = tilesize;
		this.theMap = theMap;
		this.tileImages = tileImages;
		this.p = p;
		appSizeX = d.width;
		appSizeY = d.height;
	}
	
	public void toggleState() {
		state++;
		if(state > NUMSTATES) {
			state = 1;
		}
	}
	
	public void draw(Graphics g, int maptracker) {	
		Color tempCol = g.getColor();
		
		int scale = 4;
		
		int width = appSizeX/2;
		int height = appSizeY/2;
		
		int offsetx = (appSizeX-width)/2;
		int offsety = (appSizeY-height)/2;
		
		int tilesizemini = tilesize/scale;
		
		int startx = (int)(width/tilesizemini/2);
		int starty = (int)(height/tilesizemini/2);
	
		int xDraw = 0;
		int yDraw = 0;
		
		g.setColor(Color.WHITE);
		g.fillRect(offsetx-1, offsety-1, width+1, height+tilesizemini*2+1);
		
		g.setColor(Color.BLACK);
		g.drawRect(offsetx-1, offsety-1, width+1, height+tilesizemini*2+1);
		
		g.setColor(Color.RED);
		
		for(int y=Math.max(0,p.getY()-starty); y<mapheight[maptracker] && yDraw-offsety <= height; y++) {
			for(int x=Math.max(0,p.getX()-startx); x<mapwidth[maptracker]; x++) {
			
				xDraw = (offsetx + (x-Math.max(0,p.getX()-startx))*tilesizemini);
				yDraw = (offsety + (y-Math.max(0,p.getY()-starty))*tilesizemini);
				
				if(xDraw-offsetx >= width) break;
				
				g.drawImage(tileImages[theMap[maptracker].getVal(x,y)],xDraw,yDraw, tilesizemini, tilesizemini,null);
				
				if(x == p.getX() && y == p.getY()) {
					g.fillRect(xDraw,yDraw,tilesizemini,tilesizemini);
				}
				
			}
		}
		g.setColor(tempCol);		
	}
	
}