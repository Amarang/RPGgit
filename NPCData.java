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

class NPCData
{
	// map integer (sprite ID) to string (NPC name)
	private Map<Integer, String> NPCDescMap = new HashMap<Integer, String>();
	// map integer (sprite ID) to string (NPC description (caption), or whatever)
	private Map<Integer, String> NPCNameMap = new HashMap<Integer, String>();
	private String NPCDataFile = "data/npc.txt";
	private BufferedReader br;
	private String dataStr;
	
    public NPCData() {
	
		System.out.println("making NPC data");
		
		try {
			System.out.println("inside NPCData try");
			br = new BufferedReader(new FileReader(NPCDataFile));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			
			System.out.println("end of NPCData try");
			String data = sb.toString();
			br.close();
			
			dataStr = data;
    	} catch (Exception e) { System.out.println("couldn't get " + NPCDataFile); }
		
		String temp = "";
		System.out.println(dataStr.length());
		for (int i=0; i < dataStr.length(); i++) {
			char c = dataStr.charAt(i);
			if(c == '\n') {
				String[] lineArray;
				Pattern pattern = Pattern.compile(Pattern.quote("\t"));
				temp = temp.replace("\n", "");
				temp = temp.replace("\r", "");
				lineArray = pattern.split(temp);
				System.out.println("DFDF" + lineArray[0]);
				NPCNameMap.put(Integer.parseInt(lineArray[0]), lineArray[1]); //ID, name
				NPCDescMap.put(Integer.parseInt(lineArray[0]), lineArray[2]); //ID, desc
				temp = "";
			}
			temp += c;
		}
	}
	
	public String getName(Sprite sp) {
		return NPCNameMap.get(sp.getID());
	}
	public String getName(int sID) {
		return NPCNameMap.get(sID);
	}
	public String getDesc(Sprite sp) {
		return NPCDescMap.get(sp.getID());
	}
	public String getDesc(int sID) {
		return NPCDescMap.get(sID);
	}
}