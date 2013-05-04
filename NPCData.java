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
		
		Load l = new Load();
		String[][] data = l.readFileToArray(NPCDataFile);
		
		for(int i = 0; i < data.length; i++) {
			NPCNameMap.put(Integer.parseInt(data[i][0]), data[i][1]); //ID, name
			NPCDescMap.put(Integer.parseInt(data[i][0]), data[i][2]); //ID, desc
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