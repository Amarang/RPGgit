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




class Player
{
    private int x;
    private int y;
    private int health;
    private int healthmax;
    private int mana;
    private int strength;
    private int speed;
    private int damage;
    private int defense;
    private int gold;
    private int experience;
	private int levelExperience;
    private int level;
    private int lasttownx;
    private int lasttowny;
    private String name;
    Random rand = new Random();
    
    private boolean battling=false;
    
    private boolean l = true;
    private boolean r = true;
    private boolean u = true;
    private boolean d = true;
	
	TileData td = new TileData();
	//int[] restricted = td.getRestricted();
	
    
    public Player(int xPos, int yPos,int h, int m, int str, int spd,int def,int g,int xp,int lvl,String playerName) {
        x = xPos;
        y = yPos;
        lasttownx = xPos;
        lasttowny = yPos;
        health = h;
        healthmax=h;
        mana = m;
        strength = str;
        speed= spd;
        defense = def;
        gold = g;
        experience = xp;
        level = lvl;
		name = playerName;
		levelExperience = lvl * 10;
    }
    
    public void reset() {
    	x = 0;
    	y = 0;
    }
    public void resetDamage() {
    	damage=0;
    }
    
    Item[] inventory= new Item[10];
	Item[] equipped= new Item[10];
    
    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int h) { x=h; }
    public void setY(int h) { y=h; }
    public void setBattleCondition(boolean n) { battling = n; }
    public void moveLeft() { if(canMoveLeft()&&!battling) x--; }
    public void moveRight() { if(canMoveRight()&&!battling) x++; }
    public void moveUp() { if(canMoveUp()&&!battling) y--; }
    public void moveDown() { if(canMoveDown()&&!battling) y++; }
    public int getTownX() { return lasttownx; }
    public int getTownY() { return lasttowny; }
    public void setTownX(int h) { lasttownx=h; }
    public void setTownY(int h) { lasttowny=h; }
    
    public int getHealth() { return health; }
    public int getMana() { return mana; }
    public int getStrength() { return strength; }
    public int getSpeed() { return speed; }
    public int getDefense() { return defense; }
    public int getDamage() { return damage; }
    public int getLevel() { return level; }
    public int getHealthMax() { return healthmax; }
    public int getGold() { return gold; }
    public int getExperience() { return experience; }
    public int getLevelExperience() { return levelExperience; }
    public String getName() { return name; }
    
    public void setDamage(int d) { health-=d; }
    public void setDefense(int d) { defense-=d; }
    public void setGold(int d) { gold+=d; }
    public void pay(int d) {gold-=d;}
    public void setHealth(int d) { health=d; }
    public boolean setExperience(int d) 
    	{ 
    		experience+=d; 
    		if(experience >= levelExperience) 
    			{
    				levelUp();
    				return true;
    			}
    		else 
    			return false;
    	}
    
    public void attack() { damage= rand.nextInt(speed) + strength; }
    public void spell() { damage= rand.nextInt(speed) + mana; }
    public void defend() { defense*=2; }
    public void levelUp()
    	{ 
    			experience = 0;
    			level++; 
    			healthmax+=rand.nextInt(4)+1;
    			mana+=rand.nextInt(4)+1;
    			strength+=rand.nextInt(4)+1;
    			speed+=rand.nextInt(4)+1;
    			defense+=rand.nextInt(4)+1;
    			
				levelExperience = 10*level;
    	}
     public void addItem(Item item) 
    	{ 
    		for (int i=0;i<inventory.length;i++)
    			if (inventory[i]==null)
    			{
    			inventory[i]=item;	
    			System.out.println("inventory slot filled");
				break;
    			//i=inventory.length;
    			}
    			
    	}
    public Item[] getInventory() 
    { 
    		return inventory;
    }
	
    public boolean isInInventory(Item item)
    {
		for (int i=0;i<inventory.length;i++)
			if (inventory[i]==item)	return true;
		return false;	
    } 
	
    public boolean isEquipped(Item item)
    {
		for (int i=0;i<equipped.length;i++)
			if (equipped[i]==item) return true;	
		return false;	
    }
	
	public boolean alreadySameType(Item item)
	{
		System.out.println("checking for same type");
		for (int i=0;i<equipped.length;i++)
			if (equipped[i].getType()==item.getType()) return true;	
		return false;	
		
	}
	
    public void equip(Item i) 
    	{ 
    		
    		if (isInInventory(i))
    		{
				strength+=i.getStrength(); 
				health+=i.getHealth();
				defense+=i.getDefense();
				healthmax+=i.getHealth();
				
				for (int j=0;j<equipped.length;j++) 
				{
					if (equipped[j]==null)
					{
						equipped[j]=i;
						System.out.println("equip slot filled "+j);
						break;
						//j=equipped.length;
					}
				}
    		}
    	}
    public void unequip(Item i) 
    	{ 	
    		strength-=i.getStrength(); 
    		health-=i.getHealth();
    		defense-=i.getDefense();
    		healthmax-=i.getHealth();
    		for (int j=0;j<equipped.length;j++)
    			if (equipped[j]==i)
    			{
    			equipped[j]=null;
    			System.out.println("equip slot removed "+j);
				break;
    			//j=equipped.length;
    			}
    	}
    private boolean canMoveLeft() { return l; }
    private boolean canMoveRight() { return r; }
    private boolean canMoveUp() { return u; }
    private boolean canMoveDown() { return d; }
	
	// private boolean isRestricted(int t)
	// {
		// for(int i = 0; i < restricted.length; i++)
			// if(t == restricted[i])
				// return true;
		// return false;
	// }
    
	public void allowMove(int[] directions) {
		l = td.isWalkRestricted(directions[0]) ? false : true;
		r = td.isWalkRestricted(directions[1]) ? false : true;
		u = td.isWalkRestricted(directions[2]) ? false : true;
		d = td.isWalkRestricted(directions[3]) ? false : true;
	}

}