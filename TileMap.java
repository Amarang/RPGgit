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


class TileMap
{
	private int MAPWIDTH;
	private int MAPHEIGHT;
	private int matrix[][];
	
	private String map;
	private BufferedReader br;
	
	public TileMap(int MAPWIDTH, int MAPHEIGHT, String fileName) {
	
		matrix = new int[MAPHEIGHT][MAPWIDTH];
		
		for(int w=0; w<MAPWIDTH;w++)
			for(int h=0; h<MAPHEIGHT;h++) matrix[h][w]=-1;
			
		
		Load l = new Load();
		String mapStr = l.readFileToString(fileName);
		
		int x=0;
		int y=0;
		for (int i=0; i < mapStr.length(); i++) {
			char c = mapStr.charAt(i);
			if(c == ' ') x++;
			if(c == '\n') {
				y++;
				x = 0;
			}
			
			if(i != mapStr.length()-1 && c != '\n' && c != '\r') 
				if(x < MAPWIDTH && y < MAPHEIGHT)
					if(!(c-98 > 123 || c-47 < 0))
						matrix[y][x] = (int)c - 97;
		}
	}
	
	public int getVal(int x, int y) { return matrix[y][x]; }
	
	public int getFacing(int x, int y, int facing) {
		int f = -1;
		switch(facing) {
			case 0: try { f = (y == 0) ? -1 : matrix[y-1][x]; } catch (Exception e) { f = -1; } break;
			case 1: try { f = (x == MAPWIDTH-1) ? -1 : matrix[y][x+1]; } catch (Exception e) { f = -1; } break;
			case 2: try { f = (y == MAPHEIGHT-1) ? -1 : matrix[y+1][x]; } catch (Exception e) { f = -1; } break;
			case 3: try { f = (x == 0) ? -1 : matrix[y][x-1]; } catch (Exception e) { f = -1; } break;
		}
		return f;
	}
	
	public int[] getFacingCoords(int x, int y, int facing) {
		int[] coords = {x, y};
		switch(facing) {
			case 0: coords[1] = (y == 0) ? -1 : y-1; break;
			case 1: coords[0] = (x == MAPWIDTH-1) ? -1 : x+1; break;
			case 2: coords[1] = (y == MAPHEIGHT-1) ? -1 : y+1; break;
			case 3: coords[0] = (x == 0) ? -1 : x-1; break;
		}
		return coords;
	}
	
	public int getWidth() { return MAPWIDTH; }
	public int getHeight() { return MAPHEIGHT; }
	
	public void printMap() {
		for(int i = 0; i < matrix.length; i++)
        {
            String buff = new String();
            for(int j = 0; j < matrix[0].length; j++)
				buff += Integer.toString(matrix[i][j]) + " ";
            System.out.println(buff);
        }
	}
	
	public int[] getNeighbors(int x, int y) {
		int l, r, u, d;
		try { l = (x == 0) ? -1 : matrix[y][x-1]; } catch (Exception e) { l = -1; }
		try { r = (x == MAPWIDTH-1) ? -1 : matrix[y][x+1]; } catch (Exception e) { r = -1; }
		try { u = (y == 0) ? -1 : matrix[y-1][x]; } catch (Exception e) { u = -1; }
		try { d = (y == MAPHEIGHT-1) ? -1 : matrix[y+1][x]; } catch (Exception e) { d = -1; }
		return new int[] {l, r, u, d};
	}
	
	public boolean within(Player a, Sprite b, int distance) {
		return ( (a.getX()-b.getX())*(a.getX()-b.getX()) +
				(a.getY()-b.getY())*(a.getY()-b.getY()) <= distance*distance);
	}
	public boolean within(Sprite a, Player b, int distance) {
		return ( (a.getX()-b.getX())*(a.getX()-b.getX()) +
				(a.getY()-b.getY())*(a.getY()-b.getY()) <= distance*distance);
	}
	
	public boolean within(Sprite a, Sprite b, int distance) {
		return ( (a.getX()-b.getX())*(a.getX()-b.getX()) +
				(a.getY()-b.getY())*(a.getY()-b.getY()) <= distance*distance);
	}
}