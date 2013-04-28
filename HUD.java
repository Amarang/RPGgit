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
	private Pointer c;
	private int INVENTORYSIZE;
	Item[] inventory= new Item[INVENTORYSIZE];
	Item[] equipped= new Item[INVENTORYSIZE];
	private Image[] icons;
	private int selectedItem=0;
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
	
	public void drawInventory(Graphics g, Pointer c) {
		this.c=c;
		if (c.getPointer()==2)
		{
			if (selectedItem<10)
			selectedItem++;
			c.setPointer(6);	
		}
		if (c.getPointer()==0)
		{
			if (selectedItem>0)
			selectedItem--;	
			c.setPointer(6);
		}
		if (c.getPointer()==10)
		{
			if (selectedItem<10)
			{
				if(!(inventory[selectedItem] == null)) {
				
				
					if(!p.alreadySameType(inventory[selectedItem]) && !p.isEquipped(inventory[selectedItem]))
					{
						p.equip(inventory[selectedItem]);
					} else {
						p.unequip(inventory[selectedItem]);
					}
					
					
				} else {
					System.out.println("tried to fetch non-existent item");
				}
				c.setPointer(6);	
			}
			else
			c.setPointer(7);//back out of inventory
		}
		g.setColor(Color.white);
		g.fillRect(600,0,200,400);
		g.setColor(Color.black);
		g.drawRect(600,0,200,400);
		g.setColor(Color.red);
		g.drawRect(601,24+selectedItem*20,199,20);
		g.setColor(Color.black);
		g.drawString("Gold: "+p.getGold(),640,20);
		inventory =p.getInventory();
		for (int i=0;i<inventory.length;i++)
		{
			if(inventory[i]!=null)
			{
				g.drawString(inventory[i].getName(),650,40+20*i);
				g.drawImage(icons[inventory[i].getIcon()], 620, 25+20*i, null); //icon	
			}
			
			if (p.isEquipped(inventory[i])&&inventory[i]!=null) {
				//g.drawString("E",605,40+20*i);	
				g.drawImage(icons[3], 601,24+20*i, null);
			}
		}
		g.drawString("Exit",640,40+20*10);
	}
	
	public void draw(Graphics g) {
		int health = p.getHealth();
		int healthmax = p.getHealthMax();
		int level = p.getLevel();
		int gold = p.getGold();
		int experience = p.getExperience();
		int mana = p.getMana();
		int levelExperience = p.getLevelExperience();
		
		
		int offsetx = 5;
		int offsety = 5;		
		
		//hp, mana, exp bars
		drawBar(g, offsetx, 0*thickness + offsety, 250, thickness, HPColor, health, healthmax);
		drawBar(g, offsetx, 1*thickness + offsety, 250, thickness, ManaColor, mana, mana);
		drawBar(g, offsetx, 2*thickness + offsety, 250, thickness, ExperienceColor, experience, levelExperience);
		
		//icons
		g.drawImage(icons[0], 0+offsetx,3*thickness+offsety+3, null); //gold
		g.drawImage(icons[1], 80+offsetx,3*thickness+offsety+3, null); //shield
		g.drawImage(icons[2], 160+offsetx,3*thickness+offsety+3, null); //sword
		
		Color textColor;
		if(battleHUD) textColor = Color.WHITE;
		else textColor = Color.BLACK;
		
		drawLabel(g, Integer.toString(p.getGold()), 30+offsetx, (int)(thickness*3.7)+offsety+3, textColor);
		drawLabel(g, Integer.toString(p.getDefense()), 110+offsetx, (int)(thickness*3.7)+offsety+3, textColor);
		drawLabel(g, Integer.toString(p.getStrength()), 190+offsetx, (int)(thickness*3.7)+offsety+3, textColor);
		
		drawLabel(g, p.getName() + " (level " + Integer.toString(p.getLevel()) + ")", 10+offsetx, (int)(thickness*4.7)+offsety+3, textColor);
		
		drawLabel(g, Integer.toString(health)+" / "+Integer.toString(healthmax),
				  15+offsetx, (int)(0.7*thickness)+offsety, Color.WHITE);
		drawLabel(g, Integer.toString(mana)+" / "+Integer.toString(mana),
				  15+offsetx, (int)(1.7*thickness)+offsety, Color.WHITE);
		drawLabel(g, Integer.toString(experience)+" / "+Integer.toString(levelExperience),
				  15+offsetx, (int)(2.7*thickness)+offsety, Color.BLACK);
	}
	
	
	
    public void drawBar(Graphics g, int xStart, int yStart, int length, int thickness, Color color, int currentVal, int maxVal) {
		//System.out.println("drawing bar");
		
		float percentage = (float)currentVal / (float)maxVal;
		
		if(battleHUD) g.setColor(color.brighter().brighter());
		else g.setColor(color.darker().darker());
		
		g.drawRect(xStart-1, yStart-1, length+1, thickness+1);
		
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
		int offsetx = 550-5;
		int offsety = 5;
		
		drawBar(g, offsetx, 0*thickness+offsety, 250, thickness, HPColor, health, healthmax);
		drawBar(g, offsetx, 1*thickness+offsety, 250, thickness, ManaColor, mana, mana);
		
		drawLabel(g, Integer.toString(health)+" / "+Integer.toString(healthmax),
				  offsetx+15, (int)(0.7*thickness)+offsety, Color.WHITE);
		drawLabel(g, Integer.toString(mana)+" / "+Integer.toString(mana),
				  offsetx+15, (int)(1.7*thickness)+offsety, Color.WHITE);
				  
		//icons
		g.drawImage(icons[1], 0+offsetx,2*thickness+offsety+3, null); //shield
		g.drawImage(icons[2], 80+offsetx,2*thickness+offsety+3, null); //sword
		
		Color textColor = Color.WHITE;
		
		drawLabel(g, Integer.toString(m.getDefense()), 30+offsetx, (int)(thickness*2.7)+offsety+3, textColor);
		drawLabel(g, Integer.toString(m.getStrength()), 110+offsetx, (int)(thickness*2.7)+offsety+3, textColor);
		
				  
	}

}