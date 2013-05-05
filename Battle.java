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

class Battle extends Applet
{
	private static int NUMMONSTERS = 10;
	private static int NUMBOSSES = 1;
	private SoundClip hit;
	private SoundClip death;
	private SoundClip battlemusic;
	private SoundClip bossmusic;
	private Monster m;
	private Pointer c;
	private Image[] monsterImages;
	private Image[] icons;
	private Player p;
	private Random rand = new Random(); 
 	private Font title = new Font("DialogInput",Font.PLAIN,20);	
	
	private boolean run=false;
	private boolean battle=true;
	private boolean initialize = false;
	private boolean mdefended = false;
	private boolean pdefended = false;
	private int xp;
	private int gold;
	private int boss;
	
	
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
	private boolean buffer=false;
	private int pdamagedealt;
  	private int mdamagedealt;
  	private boolean levelup = false;
  	private boolean playernotgone = true;
	
    public Battle(Player pl, Image[] mi, Pointer cl,Image[] icons,SoundClip hit,SoundClip death,SoundClip battlemusic,SoundClip bossmusic,int boss) {
		System.out.println("made Battle");
		this.p = pl;
		this.c = cl;
		this.monsterImages = mi;
		this.hit = hit;
		this.death = death;
		this.battlemusic = battlemusic;
		this.icons = icons;
		this.bossmusic=bossmusic;
		this.boss=boss;
	}
	public boolean getBattle() { return battle; }
	public boolean getSpace() { return key_space; }
	public boolean BattleSequence(Graphics g, boolean key_space,boolean key_enter)
	{
		this.key_space = key_space;
		this.key_enter = key_enter;
		if (battle)
		{
			if (!initialize)
			{
				Initialize(g);
			}
			if (m.getHealth()>=0&&p.getHealth()>=0)
				ShowPane(g);
			if (m.getHealth()<=0)
			{
				if (buffer==false)
				{
					key_space=false;
					buffer=true;
					xp=((m.getHealthMax() + m.getDamage() + m.getDefense() + m.getSpeed() + m.getMana())*3/2 - 
						(p.getHealthMax() + p.getDamage() + p.getDefense() + p.getSpeed() + p.getMana()))*(p.getLevel()/3+1);	
					if (xp<2*p.getLevel())
						xp=2*p.getLevel();
					gold=m.getId()+(rand.nextInt(4)+1)*(p.getLevel()*m.getStrength())/(p.getLevel());
					p.addGold(gold);
					if (p.setExperience(xp))
						levelup=true;	
				}
				Victory(g);
				if (key_space)
				{
					battle=false;
					initialize = false;
				}
					
			}
			if (p.getHealth()<=0)
			{
				if (buffer==false)
				{
					key_space=false;
					buffer=true;
				
					xp=-(p.getExperience()/2);
					//gold=-(p.getGold()/2);
					//p.setGold(gold);
					p.addGold(-(int)(p.getGold()/2));
					if (p.setExperience(xp))
						g.drawString("congrats on leveling up to level : "+p.getLevel(),50,540);	
				}
				battlemusic.stop();
				death.play();
				Defeat(g);
				if (key_space)
				{
					battle=false;
					initialize = false;	
					p.setHealth(p.getHealthMax());
					/*for (int i=0; i<NUMSPRITES; i++)
					{
						sp[i].reset();
					}	*/
					
					p.setX(p.getTownX());
					p.setY(p.getTownY());
				}
					
			}
		}	
			
		return battle;
			
	}
	public void Display(Graphics g)
	{
		g.setFont(title);
		g.setColor(Color.white);
		drawMonster(g);
		g.drawString("Enemy: "+ m.getName(),300,30+100);
  		g.drawString("monster health = "+ m.getHealth(),300,50+100);
  		g.drawString("strength = "+ m.getStrength(),300,71+100);
  		g.drawString("defense = "+ m.getDefense(),300,91+100);
		
  		g.drawString("your health = "+ p.getHealth(),30,50);
  		g.drawString("strength = "+ p.getStrength(),30,71+100+100);
  		g.drawString("defense = "+ p.getDefense(),30,91+100);
		
		HUD hud = new HUD(p, m, icons);
		hud.battleDraw(g);
	}
	public void Defeat(Graphics g)
	{
		m.resetDamage();
		p.resetDamage();
		g.setColor(Color.black);
		g.fillRect(0,0,800,600);
		Display(g);
		g.setFont(title);
		g.setColor(Color.white);
  		g.drawString("You were killed by the "+m.getName()+"! Exp lost : "+xp,50,500);
  		g.drawString("Gold lost : "+gold,50,520);
  		g.drawString("You take some time to tend to your wounds.",50,540);
  		
	}
	public void Victory(Graphics g)
	{
		m.resetDamage();
		p.resetDamage();
		g.setColor(Color.black);
		g.fillRect(0,0,800,600);
		Display(g);
		g.setFont(title);
		g.setColor(Color.white);
  		g.drawString("Congratulations on defeating the "+m.getName()+"! Exp earned: "+xp,50,500);
		g.drawString("Gold earned: "+gold,50,520);
  		if (levelup)
			g.drawString("Congratulations! You\'re now level "+p.getLevel(),50,540);
		
	}
	public void drawMonster(Graphics g)
	{	
		g.drawImage(monsterImages[m.getId()],350,250, this);
	}
	public void Initialize(Graphics g)
	{
		death.stop();
		if (boss==0)
		{
			battlemusic.play();
			g.setFont(title);
			key_space=false;
			int monster_id = rand.nextInt(NUMMONSTERS-NUMBOSSES);
			double stat_scale = (p.getLevel() / 4.0 + 1+(p.getX()/80)+(p.getY()/80));
			
			m = new Monster(monster_id, stat_scale);	
		}
		else if (boss==1)
		{
			bossmusic.play();
			g.setFont(title);
			key_space=false;
			int monster_id = 9;
			double stat_scale = 1D;
			
			m = new Monster(monster_id, stat_scale);	
		}
		
		
		 c.reset();
		 initialize = true;
		mdefended=false;
		 
	}
	public void ShowPane(Graphics g)
	{
		m.resetDamage();
		p.resetDamage();
		g.setFont(title);
		g.setColor(Color.black);
		g.fillRect(0,0,800,600);
		g.setColor(Color.white);
		drawMonster(g);
		Actions(g);
		Display(g);
  	
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
	}
	
	
	public void Actions(Graphics g)
	{
		g.drawString("Attack",380,400);
  		g.drawString("Spell",310,420);
  		g.drawString("Defend",380,420);
  		g.drawString("Run",460,420);
  		hit.stop();
		if (key_space&&c.getPointer()<4&&playernotgone)
		{
			
			switch(c.getPointer()) {
			case 0: 
				hit.play();
				p.attack(); 
				g.drawString("You dealt: "+ (p.getDamage()-m.getDefense())+" damage!",50,500);
				mdamagedealt=(p.getDamage()-m.getDefense());
  				if (mdamagedealt>0)
  				m.setDamage(mdamagedealt);	 
					break;
			case 1: 
				hit.play();
				p.spell();
				g.drawString("You dealt: "+ (p.getDamage()-m.getDefense())+" damage!",50,500);	
				mdamagedealt=(p.getDamage()-m.getDefense());
  				if (mdamagedealt>0)
  				m.setDamage(mdamagedealt); 
					break;
			case 2: 
				p.defend(); 
				g.drawString("Defending! You braced yourself!",50,500); 
					pdefended=true;
					break;
			case 3: 
				if (rand.nextInt(2)==0)
				battle=false;
				else
				g.drawString("NO ESCAPE",50,500);
					break;
			}
			key_space=false;
			playernotgone=false;
			if (m.getHealth()>0)
			g.drawString("The "+m.getName()+" is ready!",370,540); 
		}
		
		if (key_space&&c.getPointer()<4&&!playernotgone&&m.getHealth()>0)
		{
			playernotgone=true;
			key_space=false;
			c.setPointer(5);
			if (mdefended)
			{
			m.setDefense(m.getDefense()/2);
			mdefended=false;	
			}
			key_w=false;key_a=false;key_s=false;key_d=false;
			int monstaction=rand.nextInt(4);
			if (monstaction==0)
			{
				hit.play();
				m.attack();
				g.drawString("Attack! enemy "+m.getName()+" dealt: "+ m.getDamage()+" damage!",370,500);
				pdamagedealt=(m.getDamage()-p.getDefense());
  				if (pdamagedealt>0)
  					p.setDamage(pdamagedealt);	
				}
				
			if (monstaction==1)
			{
				hit.play();
				m.spell();
				g.drawString("Spell! enemy "+m.getName()+" dealt: "+ m.getDamage()+" damage!",370,500);	
				pdamagedealt=(m.getDamage()-p.getDefense());
  				if (pdamagedealt>0)
  					p.setDamage(pdamagedealt);	
				
			}
			if (monstaction==2)
			{
				m.defend();
				g.drawString("Defending! Enemy has braced itself!",370,500);	
					mdefended=true;
			}
			if (monstaction==3)
			{
				g.drawString("Tried to run!",370,500);
				if(m.canRun())
					battle=false;
			}
			if (pdefended)
			{
			p.setDefense(p.getDefense()/2);
			pdefended=false;	
			}
		}
		key_space=false;	 
	}
	public void delay(double n)
	{
		long startDelay = System.currentTimeMillis();
		long endDelay = 0;
		while (endDelay - startDelay < n)
			endDelay = System.currentTimeMillis();	
	}
		
}