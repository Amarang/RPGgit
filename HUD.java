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




class HUD extends Applet
{
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
	
	private Player p;
	private Monster m;
	
	private Color HPColor = new Color(230, 0, 0);
	private Color ManaColor = new Color(0, 0, 170);
	private Color ExperienceColor = new Color(255, 255, 50);
	
    
    public HUD(Player p) {
        this.p = p;
		/////
		health = p.getHealth();
		healthmax = p.getHealthMax();
		level = p.getLevel();
		gold = p.getGold();
		experience = p.getExperience();
		/////
		mana = p.getMana();
		/////
		damage = p.getDamage();
		defense = p.getDefense();
		speed = p.getSpeed();
		strength = p.getStrength();
		levelExperience = level*20;
    }
	
	public HUD(Monster m) {
		this.m = m;
	}
	
	public HUD(Player p, Monster m) {
        this.p = p;
		/////
		health = p.getHealth();
		healthmax = p.getHealthMax();
		level = p.getLevel();
		gold = p.getGold();
		experience = p.getExperience();
		/////
		mana = p.getMana();
		/////
		damage = p.getDamage();
		defense = p.getDefense();
		speed = p.getSpeed();
		strength = p.getStrength();
		levelExperience = level*20;
		
		this.m = m;
    }
	
	public void draw(Graphics g) {
		int thickness = 20;
		drawBar(g, 0, 0*thickness, 250, thickness, HPColor, health, healthmax);
		drawBar(g, 0, 1*thickness, 250, thickness, ManaColor, mana, mana);
		drawBar(g, 0, 2*thickness, 250, thickness, ExperienceColor, experience, levelExperience);
		
		drawLabel(g, Integer.toString(health)+" / "+Integer.toString(healthmax),
				  15, (int)(0.8*thickness), Color.WHITE);
		drawLabel(g, Integer.toString(mana)+" / "+Integer.toString(mana),
				  15, (int)(1.8*thickness), Color.WHITE);
		drawLabel(g, Integer.toString(experience)+" / "+Integer.toString(levelExperience),
				  15, (int)(2.8*thickness), Color.BLACK);
	}
	
    public void drawBar(Graphics g, int xStart, int yStart, int length, int thickness, Color color, int currentVal, int maxVal) {
		//System.out.println("drawing bar");
		
		float percentage = (float)currentVal / (float)maxVal;
		
		g.setColor(color.darker().darker());
		g.drawRect(xStart, yStart, length, thickness);
		
		g.setColor(Color.WHITE);
		g.fillRect(xStart, yStart, length, thickness);
		
		
		g.setColor(color);
		g.fillRect(xStart, yStart, (int)(percentage*length), thickness);
	}
	
	public void drawLabel(Graphics g, String label, int xStart, int yStart, Color color) {
		
 	Font labelFont = new Font("DialogInput",Font.BOLD,15);	
	g.setFont(labelFont);
		g.setColor(color);
  		g.drawString(label,xStart,yStart);
	}
	
	public void battleDraw(Graphics g) {
		draw(g);
	
		int thickness = 20;
		int mhealth = m.getHealth();
		int mhealthmax = m.getHealthMax();
		int mmana = m.getMana();
		int mOffset = 550;
		
		drawBar(g, mOffset, 0*thickness, 250, thickness, HPColor, mhealth, mhealthmax);
		drawBar(g, mOffset, 1*thickness, 250, thickness, ManaColor, mmana, mmana);
		//drawBar(g, 0, 2*thickness, 250, thickness, new Color(255, 255, 50), level, levelExperience);
		
		drawLabel(g, Integer.toString(mhealth)+" / "+Integer.toString(mhealthmax),
				  mOffset+15, (int)(0.8*thickness), Color.WHITE);
		drawLabel(g, Integer.toString(mmana)+" / "+Integer.toString(mmana),
				  mOffset+15, (int)(1.8*thickness), Color.WHITE);
		//drawLabel(g, Integer.toString(experience)+" / "+Integer.toString(levelExperience),
		//		  15, (int)(2.8*thickness), new Color(0, 0, 0));
	}

}