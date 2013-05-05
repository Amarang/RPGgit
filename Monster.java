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

class Monster
{
	private static int NUMMONSTERS = 9;
	private static String monsterDataFile = "data/monsters.txt";
    private int health;
    private int healthmax;
    private int mana;
    private int strength;
    private int speed;
    private int damage;
    private int defense;
    private int id;
	private String name;
	
	Random rand = new Random(); 
	
	TileData td = new TileData();
	int[] restricted = td.getRestricted();
    
    public Monster(int h, int m, int str, int spd,int def,int identity) {
		System.out.println("made Monstermanual");
        health = h;
        healthmax = h;
        mana = m;
        strength = str;
        speed=spd;
        defense = def;
        id = identity;
    }
	
	public Monster(int id, double statScale) {
		System.out.println("made Monsterdynamic");
		Load l = new Load();
		String[][] data = l.readFileToArray(monsterDataFile);
		//NAME ID HP MANA STR SPD DEF
		name = data[id][0];
		this.id = id;
		health = (int)(statScale*Integer.parseInt(data[id][2].trim()));
		healthmax = (int)(statScale*Integer.parseInt(data[id][2].trim()));
		mana = (int)(statScale*Integer.parseInt(data[id][3].trim()));
		strength = (int)(statScale*Integer.parseInt(data[id][4].trim()));
		speed = (int)(statScale*Integer.parseInt(data[id][5].trim()));
		defense = (int)(statScale*Integer.parseInt(data[id][6].trim()));
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
		if(health < 0) return 0;
		else return health;
	}
    public int getHealthMax() { return healthmax; }
    public int getMana() { return mana; }
    public int getStrength() { return strength; }
    public int getSpeed() { return speed; }
    public int getDefense() { return defense; }
    public int getDamage() { return damage; }
    public int getId() { return id; }
    //public String getName() { return names[id]; }
    public String getName() { return name; }
    
    public void setDamage(int d) { health-=d; }
    public void setDefense(int d) { defense-=d; }
    public void attack() { damage= rand.nextInt(speed) + strength; }
    public void spell() { damage= rand.nextInt(speed) + mana; }
    public void defend() { defense*=2; }
    
    public boolean canRun() 
	{
		if (health==1)	
		return true; 
		else return false;
	}	
	private boolean isRestricted(int t)
	{
		for(int i = 0; i < restricted.length; i++)
			if(t == restricted[i]) return true;
		return false;
	}
}