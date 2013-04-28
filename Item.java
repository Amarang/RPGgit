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



class Item
{
    private int health;
    private int healthmax;
    private int mana;
    private int strength;
    private int speed;
    private int damage;
    private int defense;
    private int id;
    private int itemid;
    private int numitems;
    private String names[] = {"pole","sword","sheild","heart medalion","helmet","cursed seal"};
    //String[] s;
    private String itemlist;
	private BufferedReader br;
    
    private boolean l = true;
    private boolean r = true;
    private boolean u = true;
    private boolean d = true;
	Random rand = new Random(); 
    
    public Item(int itemid,String fileName) {
    	
    	this.itemid=itemid;

		try{
			br = new BufferedReader(new FileReader(fileName));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			itemlist = sb.toString();
			br.close();
    	} catch (Exception e) {}
		
		char c;
		char c1;
		String[] s= {"","","","","","","",""};
		int counter=-1;
		int ii=2;
		for (int i=0; i < itemlist.length()-5; i++) {
			c = itemlist.charAt(i);
			c1= itemlist.charAt(i+1);
			if(c=='i' && c1==itemid+48 /*+Integer.toString(itemid)*/) 
				{
				c = itemlist.charAt(i+ii);
				while (c!='\n')
				{
					
					if (c==' ')
						counter++;
					else 
					{
					s[counter]+=c;
					//System.out.println("s["+counter+"]= "+s[counter]);		
					} 
						
				ii++;
				c = itemlist.charAt(i+ii);
				}
				
				
				}
		}
		//System.out.println("MADE items!");
		setStats(s);
    }
    public void setStats(String[] s) {
    	strength = Integer.parseInt(s[0]);
    	defense = Integer.parseInt(s[1]);
    	health = Integer.parseInt(s[2]);
    }
    public void reset() {
    	health = 0;
        mana = 0;
        strength = 0;
        speed = 0;
        defense = 0;
    }
    public void resetDamage() {
    	damage = 0;
    }

    
    public int getHealth() {
    	 return health;
	}
    public int getHealthMax() { return healthmax; }
    public int getMana() { return mana; }
    public int getStrength() { return strength; }
    public int getSpeed() { return speed; }
    public int getDefense() { return defense; }
    public int getDamage() { return damage; }
    public int getId() { return id; }
    public String getName() { return names[id]; }
    
    public void setDamage(int d) { health-=d; }
    public void setDefense(int d) { defense-=d; }
    public void attack() { damage= rand.nextInt(speed) + strength; }
    public void spell() { damage= rand.nextInt(speed) + mana; }
    public void defend() { defense*=2; }
   // public void run() { if(canRun()) run=true; }
    

    

}
