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
		
		try {
		
			File file = new File(fileName);
			StringBuilder fileContents = new StringBuilder((int)file.length());
			Scanner scanner = new Scanner(file);
			try {
				while(scanner.hasNextLine()) {        
					fileContents.append(scanner.nextLine() + "\n");
				}
				return fileContents.toString();
			} finally {
				scanner.close();
			}
		} catch (Exception e) { 
			System.out.println("couldn't get " + fileName); 
			e.printStackTrace();
		}
		return dataStr;
	}
	
	public void LoadData(Player p) {
		p.setLevel(Integer.parseInt(lineArray[0]));
		p.setHealth(Integer.parseInt(lineArray[1]));
		p.setHealthMax(Integer.parseInt(lineArray[2]));
		p.setGold(Integer.parseInt(lineArray[3]));
	}

}