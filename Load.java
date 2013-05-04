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
	private String LoadDataFile;
	private BufferedReader br;
	private String dataStr;
	private Player p;
	String[] lineArray;
	
    public Load() {
		/*LoadDataFile = "data/save.txt";
		
		
		System.out.println("getting load data");
		
		String temp = "";
		String dataStr = readFile(LoadDataFile);
		readFileToArray("items/items.txt");
		System.out.println(dataStr.length());
		for (int i=0; i < dataStr.length(); i++) {
			char c = dataStr.charAt(i);
			if(c == '\n') {
				Pattern pattern = Pattern.compile(Pattern.quote("\t"));
				temp = temp.replace("\n", "");
				lineArray = pattern.split(temp);
				System.out.println("Load" + lineArray[0]);
				temp = "";
			}
			temp += c;
		}*/
	}
	

	
	private String readFileToString(String fileName) {
		String dataStr = "";
		int numLines = 0;
		try {
			File file = new File(fileName);
			StringBuilder fileContents = new StringBuilder((int)file.length());
			Scanner scanner = new Scanner(file);
			try {
				while(scanner.hasNextLine()) {      
					numLines++;
					fileContents.append(scanner.nextLine() + "\n");
				}
			} finally {
				scanner.close();
			}
			dataStr = fileContents.toString();
		} catch (Exception e) { 
			System.out.println("couldn't get " + fileName); 
			e.printStackTrace();
		}
		return dataStr;
	}
	
	public String[][] readFileToArray(String fileName) {
		String dataStr = "";
		
		int numLines = 0;
		int numCols = 0;
		
		try {
			File file = new File(fileName);
			StringBuilder fileContents = new StringBuilder((int)file.length());
			Scanner scanner = new Scanner(file);
			String nextLine = "";
			try {
				System.out.println(fileName);
				while(scanner.hasNextLine()) {  
					nextLine = scanner.nextLine();
					int cols = nextLine.split("\t").length;
					if(cols > numCols) numCols = cols;
					System.out.println("cols" + cols);
					
					numLines++;
					fileContents.append(nextLine + "\n");
				}
			} finally {
				scanner.close();
			}
			dataStr = fileContents.toString();
		} catch (Exception e) { 
			System.out.println("couldn't get " + fileName); 
			e.printStackTrace();
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
				if(!(temp.length() < 1 || temp.charAt(0) == '/')) {
					lines[nline] = lineArray;
					nline++;
				}
				temp = "";
			}
			temp += c;
		}
		System.out.println("nline " + nline + " numCols " + numCols);
		System.out.println(Arrays.deepToString(lines));
		String[][] linesClean = new String[nline][numCols];
		
		int y = 0;
		for(int j = 0; j < lines.length; j++) {
			System.out.println("lines[j][0] " + lines[j][0]);
				if(lines[j][0] != null) {
					linesClean[y] = lines[j];
					y++;
				}
		}	
		System.out.println("y " + y);
		System.out.println(Arrays.deepToString(linesClean));
		return linesClean;
	}
	
	/*public void LoadData(Player p) {
		p.setLevel(Integer.parseInt(lineArray[0]));
		p.setHealth(Integer.parseInt(lineArray[1]));
		p.setHealthMax(Integer.parseInt(lineArray[2]));
		p.setGold(Integer.parseInt(lineArray[3]));
	}*/

}