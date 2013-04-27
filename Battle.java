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

class Battle extends Applet//extends RPG
{
	private SoundClip hit;
	private Graphics g;
	private Monster m;
	private Pointer c;
	private Image[] monsterImages;// = new Image[6];
	private Player p;
	private Random rand = new Random(); 
 	private Font title = new Font("DialogInput",Font.PLAIN,20);	
	//private MediaTracker mt = new MediaTracker(this);
	
	private boolean run=false;
	private boolean battle=true;
	private boolean initialize = false;
	private boolean mdefended = false;
	private int xp;
	private int gold;
	
	private boolean released = false;
	private boolean key_a=false;
	private boolean key_s=false;
	private boolean key_d=false;
	private boolean key_w=false;
	private boolean key_a1=false;
	private boolean key_s1=false;
	private boolean key_d1=false;
	private boolean key_w1=false;
	private boolean key_a2=false;
	private boolean key_s2=false;
	private boolean key_d2=false;
	private boolean key_w2=false;
	private boolean key_space=false;
	private boolean key_enter=false;
	
	private int pdamagedealt;
  	private int mdamagedealt;
	
	/*public Battle() {

		  //System.out.println("got stuff");
		  Player p = new Player(2,2,2,2,2,2,2);
		  Pointer c = new Pointer(0);
		  
	}*/
    public Battle(Graphics gr, Player pl, Image[] mi, Pointer cl,SoundClip hit) {
		this.g = gr;
		this.p = pl;
		this.c = cl;
		this.monsterImages = mi;
		this.hit = hit;
		//m = new Monster(20,2,8,3,1,rand.nextInt(6));
		  System.out.println("got stuff");
	}
	public boolean getBattle() { return battle; }
	public boolean getSpace() { return key_space; }
	/*public void test() {
		g.drawString("IN BATTLE CLASS!!",100,100);
	}
	*/
	public boolean BattleSequence(Graphics g, boolean key_space)
	{
	this.key_space = key_space;
	System.out.println("keyspace " + key_space);
	if (!initialize)
	{
	Initialize(g);
	//repaint();
	}
	ShowPane(g);
	if (m.getHealth()<=0)
	{
		Victory(g);
			battle=false;
		initialize = false;
	}	
	//repaint();
		
	return battle;
		
	}
	
	public void Victory(Graphics g)
	{
		m.resetDamage();
		p.resetDamage();
		g.setFont(title);
		g.setColor(Color.black);
		g.fillRect(0,0,800,600);
		g.setColor(Color.white);
		drawMonster(g);
		g.drawString("Enemy: "+ m.getName(),30,30);
  		g.drawString("monster health = "+ m.getHealth(),30,50);
  		g.drawString("strength = "+ m.getStrength(),30,71);
  		g.drawString("defense = "+ m.getDefense(),30,91);
  		g.drawString("your health = "+ p.getHealth(),300,50);
  		g.drawString("strength = "+ p.getStrength(),300,71);
  		g.drawString("defense = "+ p.getDefense(),300,91);
  		xp=m.getId()+1*rand.nextInt(3)+1;
  		g.drawString("Congratulations on defeating the "+m.getName()+"! Exp earned : "+xp,50,500);
  		if (p.setExperience(xp))
  		g.drawString("congrats on leveling up to level : "+p.getLevel(),50,540);
  		gold=m.getId()+1*rand.nextInt(3)*rand.nextInt(4)+1/(rand.nextInt(4)+1);
  		p.setGold(gold);
  		g.drawString("Gold earned : "+gold,50,520);
	}
	public void drawMonster(Graphics g)
	{	
		g.drawImage(monsterImages[m.getId()],350,250, this);
	}
	public void Initialize(Graphics g)
	{
		
		g.setFont(title);
		 m = new Monster(20,2,8,3,1,rand.nextInt(6));
		 c.reset();
		 initialize = true;
		mdefended=false;
		System.out.println("initializing");
		 
	}
	public void ShowPane(Graphics g)
	{
		System.out.println("showing pane");	
		m.resetDamage();
		p.resetDamage();
		g.setFont(title);
		g.setColor(Color.black);
		g.fillRect(0,0,800,600);
		g.setColor(Color.white);
		drawMonster(g);
		Actions(g);
		g.drawString("Enemy: "+ m.getName(),30,30);
  		g.drawString("monster health = "+ m.getHealth(),30,50);
  		g.drawString("strength = "+ m.getStrength(),30,71);
  		g.drawString("defense = "+ m.getDefense(),30,91);
  		g.drawString("your health = "+ p.getHealth(),300,50);
  		g.drawString("strength = "+ p.getStrength(),300,71);
  		g.drawString("defense = "+ p.getDefense(),300,91);
  	
  		g.setColor(Color.red);
  		switch(c.getPointer()) {
			case 0: 
				g.drawRect(379,379,72,22); 	
					break;
			case 1: 
				g.drawRect(309,399,72,22); 
					break;
			case 2: 
				g.drawRect(379,399,72,22);
					break;
			case 3: 
				g.drawRect(459,399,72,22); 
					break;
  		}
  		//System.out.println("enemyhealth="+m.getHealth());

  			//g.drawString("Damage dealt: "+ pdamagedealt,400,500);
  	
  			//g.drawString("Damage dealt: "+ mdamagedealt,50,500);
  			System.out.println("showed pane");	
  		//repaint();
	}
	
	
	public void Actions(Graphics g)
	{
		System.out.println("showing actions");	
		g.drawString("Attack",380,400);
  		g.drawString("Spell",310,420);
  		g.drawString("Defend",380,420);
  		g.drawString("Run",460,420);
  		hit.stop();
		if (key_space)
		{
			switch(c.getPointer()) {
			case 0: 
				hit.play();
				p.attack(); 
				g.drawString("Damage dealt: "+ p.getDamage(),400,500);
				mdamagedealt=(p.getDamage()-m.getDefense());
  				if (mdamagedealt>0)
  				m.setDamage(mdamagedealt);	 
					break;
			case 1: 
				hit.play();
				p.spell();
				g.drawString("Damage dealt: "+ p.getDamage(),400,500);	
				mdamagedealt=(p.getDamage()-m.getDefense());
  				if (mdamagedealt>0)
  				m.setDamage(mdamagedealt); 
					break;
			case 2: 
				p.defend(); 
				g.drawString("Defending! Damage dealt: "+ p.getDamage(),400,500); 
					break;
			case 3: 
				if (rand.nextInt(2)==0)
				battle=false;
				else
				g.drawString("NO ESCAPE",400,500);
					break;
			}
		}
		if (key_space&&c.getPointer()<4)
		{
			key_space=false;
			c.setPointer(5);
			if (mdefended)
			{
			m.setDefense(3);
			mdefended=false;	
			}
			key_w=false;key_a=false;key_s=false;key_d=false;
			int monstaction=rand.nextInt(4);
			if (monstaction==0)
			{
				hit.play();
				m.attack();
				g.drawString("Attack! Damage dealt: "+ m.getDamage(),50,500);
				pdamagedealt=(m.getDamage()-p.getDefense());
  				if (pdamagedealt>0)
  					p.setDamage(pdamagedealt);	
				}
				
			if (monstaction==1)
			{
				hit.play();
				m.spell();
				g.drawString("Spell! Damage dealt: "+ m.getDamage(),50,500);	
				pdamagedealt=(m.getDamage()-p.getDefense());
  				if (pdamagedealt>0)
  					p.setDamage(pdamagedealt);	
				
			}
			if (monstaction==2)
			{
				//System.out.print("def");
				m.defend();
				g.drawString("Defending! Damage dealt: "+ m.getDamage(),50,500);	
					mdefended=true;
			}
			if (monstaction==3&&m.canRun())
			{
				//System.out.print("def");
				g.drawString("Tried to run! Damage dealt: "+ m.getDamage(),50,500);
			}
					
				//battle=false;
				
		}
	System.out.println("showed actions");		 
	}
		
}