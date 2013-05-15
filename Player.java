import java.util.Random;

class Player
{
	private static int NUMITEMS = 10;
	
    private int x;
    private int y;
    private int health;
    private int healthmax;
    private int mana;
    private int manamax;
    private int strength;
    private int speed;
    private int damage;
    private int defense;
    private int gold;
    private int experience;
	private int levelExperience;
	private int level;
    private int statpoints;
    private int lasttownx;
    private int lasttowny;
    private int townentrancex=3;
    private int townentrancey=36;
	private int facing = 0; //0-3 north east south west
	private int maptracker;
	private int mapspawner=0;
    private String name;
    private String loadFile = "http://people.tamu.edu/~amin.nj/rpgsave.txt";
    private String saveFile = "http://people.tamu.edu/~amin.nj/rpgsave.php?stats=";
    Random rand = new Random();
    private int battlefrequency=3;
    private int treasurespots[][][]= new int [400][300][40];
    
    
    private boolean battling=false;
    
    private boolean l = true;
    private boolean r = true;
    private boolean u = true;
    private boolean d = true;
	
	TileData td = new TileData();
	
    public Player(int xPos, int yPos,int h, int m, int str, int spd,int def,int g,int xp,int lvl,String playerName, int mt) {
		System.out.println("made Player");
        x = xPos;
        y = yPos;
        lasttownx = xPos;
        lasttowny = yPos;
        health = h;
        healthmax=h;
        mana = m;
        manamax = m;
        strength = str;
        speed= spd;
        defense = def;
        gold = g;
        experience = xp;
        level = lvl;
		name = playerName;
		levelExperience = lvl * 10;
		statpoints = 5;
		maptracker = mt;
    }
    
    public void reset() {
    	x = 0;
    	y = 0;
    }
    public void resetDamage() {
    	damage=0;
    }
    
    Item[] inventory= new Item[NUMITEMS];
	Item[] equipped= new Item[NUMITEMS];
    
    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int h) { x=h; }
    public void setY(int h) { y=h; }
    public void setBattleCondition(boolean n) { battling = n; }
    public void moveLeft() { if(canMoveLeft()&&!battling) x--; facing = 3; } // W
    public void moveRight() { if(canMoveRight()&&!battling) x++; facing = 1; } // E
    public void moveUp() { if(canMoveUp()&&!battling) y--; facing = 0; } // N
    public void moveDown() { if(canMoveDown()&&!battling) y++; facing = 2; } // S
	
	//boolean so you can, say, play a sound (goat yeaahhhh) if it's false
	//in addition to moving, so you save some calculation time
    public boolean moveLeftB() { if(canMoveLeft()&&!battling) x--; facing = 3; return canMoveLeft(); } // W
    public boolean moveRightB() { if(canMoveRight()&&!battling) x++; facing = 1; return canMoveRight(); } // E
    public boolean moveUpB() { if(canMoveUp()&&!battling) y--; facing = 0; return canMoveUp(); } // N
    public boolean moveDownB() { if(canMoveDown()&&!battling) y++; facing = 2; return canMoveDown(); } // S
	
    public int getTownX() { return lasttownx; }
    public int getTownY() { return lasttowny; }
    public void setTownX(int h) { lasttownx=h; }
    public void setTownY(int h) { lasttowny=h; }
    public int getTownEntranceX() { return townentrancex; }
    public int getTownEntranceY() { return townentrancey; }
	
    public int getFacing() { return facing; }
    public void setFacing(int f) { facing = f; }
    
    public int getHealth() { return health; }
    public int getMana() { return mana; }
    public int getStrength() { return strength; }
    public int getSpeed() { return speed; }
    public int getDefense() { return defense; }
    public int getDamage() { return damage; }
    public int getLevel() { return level; }
    public int getHealthMax() { return healthmax; }
    public int getManaMax() { return manamax; }
    public int getGold() { return gold; }
    public int getStatPoints() { return statpoints; }
    public int getExperience() { return experience; }
    public int getLevelExperience() { return levelExperience; }
    public String getName() { return name; }
    
    public void setDamage(int d) { if (health-d >healthmax) health = healthmax; else health-=d; }
    public void setLevel(int d) { level=d; }
    public void setDefense(int d) { defense=d; }
    public void setGold(int d) { gold=d; }
    public void addGold(int d) { gold+=d; }
    public void setStatPoints(int d) { statpoints=d; }
    public void addStatPoints(int d) { statpoints+=d; }
    public void pay(int d) {gold-=d;}
    
    public void payMana(int d) {mana-=d;}
    public void setHealth(int d) { health=Math.max(0,d); }
    public void setMana(int d) { mana=d; }
    public void setHealthMax(int d) { healthmax=d; }
    public void setStrength(int d) { strength=d; }
    public void setSpeed(int d) { speed=d; }
    public void setManaMax(int d) { manamax=d; }

	public int getMapTracker() { return maptracker; }
	public void setMapTracker(int mt) {	this.maptracker = mt; }
	public int getMapSpawner() { return mapspawner; }
	public void setMapSpawner(int ms) {	this.mapspawner = ms; }
	public int getBattleFrequency() { return battlefrequency; }
	public void setBattleFrequency(int bf) {	this.battlefrequency = bf; }
	
	public boolean foundTreasure(int x, int y, int m) 
	{
		if (treasurespots[x][y][m]==1)
		return true;
		else
		return false;
	}
	public void addTreasure(int x, int y, int m) {
	treasurespots[x][y][m]=1;
	}
    
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
		healthmax+=rand.nextInt(3)+2;
		manamax+=rand.nextInt(3)+2;
		strength+=rand.nextInt(3)+2;
		speed+=rand.nextInt(3)+2;
		defense+=rand.nextInt(3)+2;
		statpoints+=2;
		
		levelExperience = 10*level*level;
	}
    public void addItem(Item item) 
	{ 
		for (int i=0;i<inventory.length;i++)
			if (inventory[i]==null)
			{
				inventory[i]=item;	
				System.out.println("inventory slot filled");
				break;
			}
			
	}
	public void removeItem(Item item) 
	{ 
		for (int i=0;i<inventory.length;i++)
			if (inventory[i]==item)
			{
				inventory[i]=null;	
				System.out.println("inventory slot removed");
				break;
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
	
	// returns true if already have item equipped of same type
	public boolean alreadySameType(Item item)
	{
		//System.out.println("checking for same type");
		for (int i=0;i<equipped.length;i++) {
			if(equipped[i] != null)
				if (equipped[i].getType()==item.getType()) return true;	
		}
		return false;	
		
	}
	
	// returns item of same type as parameter item
	public Item equippedOfType(Item item)
	{
		//System.out.println("checking for same type");
		for (int i=0;i<equipped.length;i++) {
			if(equipped[i] != null)
				if (equipped[i].getType()==item.getType()) return equipped[i];	
		}
		return null;	
		//return null if we didn't find item of same type equipped
		
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
	public void use(Item i) 
	{ 
		if (isInInventory(i))
		{
			if (healthmax-health>i.getHealth())
			health+=i.getHealth();
			else
			health=healthmax;
				
			strength+=i.getStrength(); 	
			defense+=i.getDefense();
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
			}
	}
	public void save() 
	{ 	
		
      Load l = new Load();
      System.out.println("saving, please don\'t turn off the power.");

      //l.loadSite("http://people.tamu.edu/~amin.nj/rpgsave.php?stats="
    	//	  	+level+"|"+health+"|"+healthmax+"|"+gold+"|"+x+"|"+y);
      
      l.loadSite(saveFile      
    		  +x+"|"+y+"|"+health+"|"+healthmax+"|"+mana+"|"+manamax+"|"+strength
    		  +"|"+speed+"|"+damage+"|"+defense+"|"+gold+"|"+experience+"|"
    		  +levelExperience+"|"+level+"|"+statpoints+"|"+lasttownx+"|"
    		  +lasttowny+"|"+townentrancex+"|"+townentrancey+"|"+facing+"|"+maptracker);
      
      
      System.out.println(name + " has saved the game.");
        
	}
	
	
	public void load() 
	{ 	
		Load l = new Load();
		String[][] stats = l.readSiteToArray(loadFile, "\t");
		//String[][] stats = l.readFileToArray(SaveFile);
		
		this.x = Integer.parseInt(stats[0][0].trim());
		this.y = Integer.parseInt(stats[0][1].trim());
		this.health = Integer.parseInt(stats[0][2].trim());
		this.healthmax = Integer.parseInt(stats[0][3].trim());
		this.mana = Integer.parseInt(stats[0][4].trim());
		this.manamax = Integer.parseInt(stats[0][5].trim());
		this.strength = Integer.parseInt(stats[0][6].trim());
		this.speed = Integer.parseInt(stats[0][7].trim());
		this.damage = Integer.parseInt(stats[0][8].trim());
		this.defense = Integer.parseInt(stats[0][9].trim());
		this.gold = Integer.parseInt(stats[0][10].trim());
		this.experience = Integer.parseInt(stats[0][11].trim());
		this.levelExperience = Integer.parseInt(stats[0][12].trim());
		this.level = Integer.parseInt(stats[0][13].trim());
		this.statpoints = Integer.parseInt(stats[0][14].trim());
		this.lasttownx = Integer.parseInt(stats[0][15].trim());
		this.lasttowny = Integer.parseInt(stats[0][16].trim());
		this.townentrancex = Integer.parseInt(stats[0][17].trim());
		this.townentrancey = Integer.parseInt(stats[0][18].trim());
		this.facing = Integer.parseInt(stats[0][19].trim());
		this.maptracker = Integer.parseInt(stats[0][20].trim());
        
	}
	
    private boolean canMoveLeft() { return l; }
    private boolean canMoveRight() { return r; }
    private boolean canMoveUp() { return u; }
    private boolean canMoveDown() { return d; }

    public boolean isBlocked() {
    	return ( (!l && facing==3) ||
    			 (!r && facing==1) ||
    			 (!u && facing==0) ||
    			 (!d && facing==2) );    	
    }
    
	public void allowMove(int[] directions) {
		l = td.isWalkRestricted(directions[0]) ? false : true;
		r = td.isWalkRestricted(directions[1]) ? false : true;
		u = td.isWalkRestricted(directions[2]) ? false : true;
		d = td.isWalkRestricted(directions[3]) ? false : true;
	}

}