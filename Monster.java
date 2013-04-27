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
    private int health;
    private int mana;
    private int strength;
    private int speed;
    private int damage;
    private int defense;
    private int id;
    private String names[] = {"Creaboxireis","Ant","Wolf","Snake","Slime","Dragon"};
    
    
    private boolean l = true;
    private boolean r = true;
    private boolean u = true;
    private boolean d = true;
	Random rand = new Random(); 
	
	TileData td = new TileData();
	int[] restricted = td.getRestricted();
    
    public Monster(int h, int m, int str, int spd,int def,int identity) {
    	
        health = h;
        mana = m;
        strength = str;
        speed=spd;
        defense = def;
        id = identity;
    }
    
    public void reset() {
    	health = 0;
        mana = 0;
        strength = 0;
        speed=0;
        defense = 0;
    }
    public void resetDamage() {
    	damage=0;
    }

    
    public int getHealth() { return health; }
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
    public void defend() { defense+=3; }
   // public void run() { if(canRun()) run=true; }
    
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

