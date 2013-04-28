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
	
	private Player p;
	private Monster m;
	Item[] inventory= new Item[10];
	Item[] equipped= new Item[10];
	private Image[] icons;
	
	private Color HPColor = new Color(230, 0, 0);
	private Color ManaColor = new Color(0, 0, 170);
	private Color ExperienceColor = new Color(255, 255, 50);
	
	private int thickness = 20;
	
	private boolean battleHUD = false;
	
    
    public HUD(Player p, Image[] icons) {
        this.p = p;
		this.icons = icons;
    }
	
		
	public HUD(Player p, Monster m, Image[] icons) {
        this.p = p;
		this.m = m;
		this.icons = icons;
    }
	public void drawInventory(Graphics g) {
	g.setColor(Color.white);
	g.fillRect(600,0,200,400);
	g.setColor(Color.black);
	g.drawRect(600,0,200,400);
	g.drawString("Gold: "+p.getGold(),640,20);
	inventory =p.getInventory();
	for (int i=0;i<inventory.length;i++)
	    {
	    if(inventory[i]!=null)
	    {
	    g.drawString(inventory[i].getName(),640,40+20*i);
	    g.drawImage(icons[inventory[i].getIcon()], 620, 25+20*i, null); //icon	
	    }
	    
	    if (p.isEquipped(inventory[i])&&inventory[i]!=null)
	    	g.drawString("E",605,40+20*i);	
	    }
	}
	
	public void draw(Graphics g) {
		int health = p.getHealth();
		int healthmax = p.getHealthMax();
		int level = p.getLevel();
		int gold = p.getGold();
		int experience = p.getExperience();
		int mana = p.getMana();
		int levelExperience = p.getLevelExperience();
		
		//g.drawImage(icons[1], 20,thickness*2, null);
		//g.drawImage(icons[2], 20,thickness*3, null);
		
		
		drawBar(g, 0, 0*thickness, 250, thickness, HPColor, health, healthmax);
		drawBar(g, 0, 1*thickness, 250, thickness, ManaColor, mana, mana);
		drawBar(g, 0, 2*thickness, 250, thickness, ExperienceColor, experience, levelExperience);
		g.drawImage(icons[0], 0,3*thickness, null); //gold
		g.drawImage(icons[1], 80,3*thickness, null); //shield
		g.drawImage(icons[2], 160,3*thickness, null); //sword
		
		Color textColor;
		if(battleHUD) textColor = Color.WHITE;
		else textColor = Color.BLACK;
		
		drawLabel(g, Integer.toString(p.getGold()), 30, (int)(thickness*3.7), textColor);
		drawLabel(g, Integer.toString(p.getDefense()), 110, (int)(thickness*3.7), textColor);
		drawLabel(g, Integer.toString(p.getStrength()), 190, (int)(thickness*3.7), textColor);
		
		drawLabel(g, p.getName() + " (level " + Integer.toString(p.getLevel()) + ")", 10, (int)(thickness*4.7), textColor);
		
		drawLabel(g, Integer.toString(health)+" / "+Integer.toString(healthmax),
				  15, (int)(0.7*thickness), Color.WHITE);
		drawLabel(g, Integer.toString(mana)+" / "+Integer.toString(mana),
				  15, (int)(1.7*thickness), Color.WHITE);
		drawLabel(g, Integer.toString(experience)+" / "+Integer.toString(levelExperience),
				  15, (int)(2.7*thickness), Color.BLACK);
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
	
		battleHUD = true;
		draw(g);
	
		
		
		int health = m.getHealth();
		int healthmax = m.getHealthMax();
		int mana = m.getMana();
		int offset = 550;
		
		drawBar(g, offset, 0*thickness, 250, thickness, HPColor, health, healthmax);
		drawBar(g, offset, 1*thickness, 250, thickness, ManaColor, mana, mana);
		//drawBar(g, 0, 2*thickness, 250, thickness, new Color(255, 255, 50), level, levelExperience);
		
		drawLabel(g, Integer.toString(health)+" / "+Integer.toString(healthmax),
				  offset+15, (int)(0.7*thickness), Color.WHITE);
		drawLabel(g, Integer.toString(mana)+" / "+Integer.toString(mana),
				  offset+15, (int)(1.7*thickness), Color.WHITE);
		//drawLabel(g, Integer.toString(experience)+" / "+Integer.toString(levelExperience),
		//		  15, (int)(2.8*thickness), new Color(0, 0, 0));
	}

}