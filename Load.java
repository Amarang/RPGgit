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
		LoadDataFile = "data/save.txt";
		
		
		System.out.println("getting load data");
		
		String temp = "";
		String dataStr = readFile(LoadDataFile);
		readFileToArray(LoadDataFile);
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
		}
	}
	

	
	private String readFile(String fileName) {
		String dataStr = "";
		
		int numLines = 0;
		
		try {
			System.out.println(fileName);
			File file = new File(fileName);
			StringBuilder fileContents = new StringBuilder((int)file.length());
			Scanner scanner = new Scanner(file);
			System.out.println(fileName);
			try {
				System.out.println(fileName);
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
		
		System.out.println("------------");
		System.out.println(numLines);
		System.out.println("------------");
		System.out.println(dataStr);
		return dataStr;
	}
	
	private void readFileToArray(String fileName) {
		String dataStr = "";
		
		int numLines = 0;
		int numCols = 0;
		
		try {
			System.out.println(fileName);
			File file = new File(fileName);
			StringBuilder fileContents = new StringBuilder((int)file.length());
			Scanner scanner = new Scanner(file);
			System.out.println(fileName);
			String nextLine = "";
			try {
				System.out.println(fileName);
				while(scanner.hasNextLine()) {  
					nextLine = scanner.nextLine();
					if(numLines == 0) {
						numCols = nextLine.split("\t").length-1;
					}
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
		System.out.println("------------");
		System.out.println(numLines);
		System.out.println(numCols);
		System.out.println("------------");
		
		
		String[][] lines = new String[numLines][numCols];
		
		String temp = "";
		int line = 0;
		int col = 0;
		for (int i=0; i < dataStr.length(); i++) {
			char c = dataStr.charAt(i);
			if(c == '\n') {
				Pattern pattern = Pattern.compile(Pattern.quote("\t"));
				temp = temp.replace("\n", "");
				lineArray = pattern.split(temp);
				//System.out.println("Load" + lineArray[0]);
				lines[line] = lineArray;
				temp = "";
			}
			temp += c;
		}
		
		System.out.println(Arrays.deepToString(lines));
		
	}
	
	public void LoadData(Player p) {
		p.setLevel(Integer.parseInt(lineArray[0]));
		p.setHealth(Integer.parseInt(lineArray[1]));
		p.setHealthMax(Integer.parseInt(lineArray[2]));
		p.setGold(Integer.parseInt(lineArray[3]));
	}

}