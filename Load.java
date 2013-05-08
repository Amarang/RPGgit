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
import java.util.Arrays;
import java.util.regex.Pattern;

class Load
{	
    public Load() {
	}
	
	public String readFileToString(String fileName) {
		String dataStr = "";
		
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
		
			Scanner s = new Scanner(is).useDelimiter("\\A");
			dataStr = s.hasNext() ? s.next() : "";
			
			is.close();
			
		} catch (Exception e) {
			System.out.println("couldn't get " + fileName); 
			e.printStackTrace();
		}
		return dataStr;
	}
	
	public String[][] readFileToArray(String fileName) {
		String dataStr = "";
		
		String[] lineArray;
		
		int numLines = 0;
		int numCols = 0;
		
		
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
		
			Scanner s = new Scanner(is).useDelimiter("\\A");
			dataStr = s.hasNext() ? s.next() : "";
		} catch (Exception e) {
			System.out.println("couldn't get " + fileName); 
			e.printStackTrace();
		}
		
		
		
		dataStr += "\n";
		
		dataStr.replace("\r","");
			//String nextLine = "";
			
				//System.out.println(fileName);
				
			numLines = dataStr.split("\n").length;
			String[] colCountArray = dataStr.split("\n");
			
			for(int i = 0; i < colCountArray.length; i++) {
				int cols = colCountArray[i].split("\t").length;
				
				if(cols > numCols) numCols = cols;
				
			}


		String[][] lines = new String[numLines][numCols];
		String temp = "";
		int nline = 0;
		for (int i=0; i < dataStr.length(); i++) {
			char c = dataStr.charAt(i);
			if(c == '\n') {
				Pattern pattern = Pattern.compile(Pattern.quote("\t"));
				temp = temp.replace("\n", "");
				lineArray = pattern.split(temp);
				
				if(!(temp.length() < 1 || lineArray.length == 1 || temp.length() == 0 || temp.charAt(0) == '/')) {
					lines[nline] = lineArray;
					nline++;
				}
				temp = "";
			}
			temp += c;
		}
		//System.out.println("nline " + nline + " numCols " + numCols);
		//System.out.println(Arrays.deepToString(lines));
		String[][] linesClean = new String[nline][numCols];
		
		int y = 0;
		for(int j = 0; j < lines.length; j++) {
			//System.out.println("lines[j][0] " + lines[j][0]);
				if(lines[j][0] != null) {
					linesClean[y] = lines[j];
					y++;
				}
		}	
		//System.out.println("y " + y);
		//System.out.println(Arrays.deepToString(linesClean));
		return linesClean;
	}
}