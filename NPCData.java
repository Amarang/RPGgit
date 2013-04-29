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

class NPCData
{

	
	private Map<Integer, String> NPCMap = new HashMap<Integer, String>();
	// map integer (sprite ID) to string (NPC name)
	
   
	
    public NPCData() {
		
		// map integer (sprite ID) to string (NPC name)
		//also note that this spells USA! USA! USA!
		//ironic, considering that none of these represent the US
		NPCMap.put(0, "Usain Bolt");
		NPCMap.put(1, "Saddam Hussain");
		NPCMap.put(2, "Adolf Hitler");
		
	}
	
	public String getName(Sprite sp) {
		//soundMap.get(tileID) != null
		//System.out.println(sp.getID());
		return NPCMap.get(sp.getID());
	}

}