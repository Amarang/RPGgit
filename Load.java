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
		
		try {
			System.out.println("inside Load try");
			br = new BufferedReader(new FileReader(LoadDataFile));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			
			System.out.println("end of Load try");
			String data = sb.toString();
			br.close();
			
			dataStr = data;
    	} catch (Exception e) { System.out.println("couldn't get " + LoadDataFile); }
		
		String temp = "";
		System.out.println(dataStr.length());
		for (int i=0; i < dataStr.length(); i++) {
			char c = dataStr.charAt(i);
			if(c == '\n') {
				
				Pattern pattern = Pattern.compile(Pattern.quote("\t"));
				temp = temp.replace("\n", "");
				temp = temp.replace("\r", "");
				lineArray = pattern.split(temp);
				System.out.println("Load" + lineArray[0]);
				temp = "";
			}
			temp += c;
		}

	}
	
	public void LoadData(Player p) {
		p.setLevel(Integer.parseInt(lineArray[0]));
		p.setHealth(Integer.parseInt(lineArray[1]));
		p.setHealthMax(Integer.parseInt(lineArray[2]));
		p.setGold(Integer.parseInt(lineArray[3]));
	}

}