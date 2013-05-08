import java.io.*;
import java.util.Random;

class Spell
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
    private int target;
    private int spellid;
    private int cost;
    private int NUMSPELLS;
	private int type;
    private String names[] = {"Heal","Infliction","Pain","Crucify"};
    private String spelllist;
	private BufferedReader br;
	private int icon;
    private String spellname;
	Random rand = new Random(); 
    
    public Spell(int spellid,String fileName) {
    	this.spellid=spellid;
		
		
		Load l = new Load();
		String[][] data = l.readFileToArray(fileName);
		
		setStats(data[spellid]);
		
		System.out.println("MADE spells!");
	}
	
	public int getSpellID() {
		return spellid;
	}
	
	public int getTarget() {
		return target;
	}
	public int getCost() {
		return cost;
	}
	public int getDamage() {
		return damage;
	}
	public String getName() {
		return spellname;
	}
    public void setStats(String[] s) {
    	
    	spellname = s[0].trim();
    	target = Integer.parseInt(s[2].trim());
    	damage = Integer.parseInt(s[3].trim());
    	cost = Integer.parseInt(s[4].trim());
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

    //public String getName() { return names[itemid]; }
    
    public void setDamage(int d) { health-=d; }
    public void setDefense(int d) { defense-=d; }
    public void attack() { damage= rand.nextInt(speed) + strength; }
    public void spell() { damage= rand.nextInt(speed) + mana; }
    public void defend() { defense*=2; }
   // public void run() { if(canRun()) run=true; }
    

    

}
