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
    private int onetimeuse;
    private int id;
    private int itemid;
    private int price;
    private int NUMITEMS;
	private int type;
    private String names[] = {"Bamboo","Sword","Shield","Heart Medallion","Helmet","Cursed Seal","Broad Sword", "Azure's Terror", "Spiked Sheild", "Plate of Magi","Health Capsule"};
    private String itemlist;
	private BufferedReader br;
	private int icon;
    
    private boolean l = true;
    private boolean r = true;
    private boolean u = true;
    private boolean d = true;
	Random rand = new Random(); 
    
    public Item(int itemid,String fileName) {
    	this.itemid=itemid;
		
		
		Load l = new Load();
		String[][] data = l.readFileToArray(fileName);
		
		//copyOfRange just takes a range from an array
		//data[itemid] looks like [i00, 3, 0, 0, 2, 5, 0]
		//but you can't parseint on i00, so I'm skipping that one
		//don't use it for anything anyways (same thing as itemid, right?)
		setStats(Arrays.copyOfRange(data[itemid],1,data[itemid].length));
		System.out.println("MADE items!");
	}
	
	public int getItemID() {
		return itemid;
	}
	
	public int getType() {
		return type;
	}
	public int getPrice() {
		return price;
	}
	public int getUseage() {
		return onetimeuse;
	}
    public void setStats(String[] s) {
    	
    	strength = Integer.parseInt(s[0]);
    	defense = Integer.parseInt(s[1]);
    	health = Integer.parseInt(s[2]);
    	icon = Integer.parseInt(s[3]);
    	type = Integer.parseInt(s[3]);
    	price = Integer.parseInt(s[4]);
    	onetimeuse = Integer.parseInt(s[5]);
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

    
    public int getHealth() { return health; }
    public int getHealthMax() { return healthmax; }
    public int getMana() { return mana; }
    public int getStrength() { return strength; }
    public int getSpeed() { return speed; }
    public int getDefense() { return defense; }
    public int getDamage() { return damage; }
    public int getIcon() { return icon; }
    public String getName() { return names[itemid]; }
    
    public void setDamage(int d) { health-=d; }
    public void setDefense(int d) { defense-=d; }
    public void attack() { damage= rand.nextInt(speed) + strength; }
    public void spell() { damage= rand.nextInt(speed) + mana; }
    public void defend() { defense*=2; }
   // public void run() { if(canRun()) run=true; }
    

    

}
