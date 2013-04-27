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
		 
		//System.out.println("making map");
		
		for(int w=0; w<MAPWIDTH;w++)
			for(int h=0; h<MAPHEIGHT;h++) matrix[h][w]=-1;
			
		try{
			br = new BufferedReader(new FileReader(fileName));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			map = sb.toString();
			br.close();
    	} catch (Exception e) {}
		
		String mapStr = map;
		
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
		System.out.println("MADE MAP!");
		//printMap();
	}
	
	public int getVal(int x, int y) { return matrix[y][x]; }
	
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
		
		//System.out.println("boolean: l: " + l + "; r: " + r + "; u: " + u + "; d: " + d);
		//System.out.println("tileID:  l: " + matrix[y][x-1] + "; r: " + matrix[y][x+1] + 
		//				 "; u: " + matrix[y-1][x] + "; d: " + matrix[y+1][x]);
		int[] directions = {l, r, u, d};
		return directions;
	}
}