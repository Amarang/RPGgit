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
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

class HUD extends Applet
{

	private int appSizeX;
	private int appSizeY;
	private int tilesize;
	private int[] mapwidth;
	private int[] mapheight;
	private TileMap[] theMap;
	private Image[] tileImages;
	
	private Player p;
	private Monster m;
	private Pointer c;
	private int INVENTORYSIZE;
	Item[] inventory= new Item[INVENTORYSIZE];
	Item[] equipped= new Item[INVENTORYSIZE];
	Item[] shop;
	private Image[] icons;
	private int selectedItem=0;
	private int selectedItemx=0;
	private Color HPColor = new Color(230, 0, 0);
	private Color ManaColor = new Color(0, 0, 170);
	private Color ExperienceColor = new Color(255, 255, 50);
	private int thickness = 20;
	private boolean battleHUD = false;
	private boolean loadedRecently = false;
	private NPCData nd;// = new NPCData();
	
	private float HUDalpha = 0.85F;
	private int HUDround = 10;
    
    public HUD(Player p, Image[] icons,Item[] shop) {
		System.out.println("made HUD");
        this.p = p;
		this.icons = icons;
		this.shop = shop;
		//this.load = load;
    }
		
	public HUD(Player p, Monster m, Image[] icons) {
		System.out.println("made HUD");
        this.p = p;
		this.m = m;
		this.icons = icons;
    }
	
	public void updateNPCInfo() {
		nd = new NPCData();
	}
	
	public void drawInventory(Graphics g, Pointer c) {
		this.c=c;
		if (c.getPointer()==2)
		{
			if (selectedItem<12)
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
					System.out.println(inventory[selectedItem].getUseage());
					if(!p.alreadySameType(inventory[selectedItem]) && !p.isEquipped(inventory[selectedItem])&&inventory[selectedItem].getUseage()==0)
					{
						p.equip(inventory[selectedItem]);
					} else if (p.isEquipped(inventory[selectedItem])){
						p.unequip(inventory[selectedItem]);
					}
					else if(!p.alreadySameType(inventory[selectedItem]) && !p.isEquipped(inventory[selectedItem])&&inventory[selectedItem].getUseage()==1)
					{
						p.use(inventory[selectedItem]);
						p.removeItem(inventory[selectedItem]);
					} 
					
				} else {
					System.out.println("tried to fetch non-existent item");
				}
				c.setPointer(6);	
			}
			else if(selectedItem==10)
			{
				c.setPointer(7);//back out of inventory
				loadedRecently = false;
			}
			else if(selectedItem==11)
				p.save();
			else if(selectedItem==12)
			{
				if(!loadedRecently) {
					p.load();
				}
				loadedRecently = true;
			}
			//load.LoadData(p);
		}
		
		int paddingx = 7;
		int paddingy = 7;
		
		Graphics2D g2d = (Graphics2D) g;
		Color tempCol = g.getColor();
		Font tempFont = g.getFont();
		
		Composite original = g2d.getComposite();
		
		
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, HUDalpha));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
				
		g.setColor(Color.white);
		g.fillRoundRect(600-paddingx,0+paddingy,200,400, HUDround,HUDround);
		
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0F));
		
		g.setColor(Color.black);
		g.drawRoundRect(600-paddingx,0+paddingy,200,400, HUDround,HUDround);
		g.setColor(Color.red);
		g.drawRoundRect(601-paddingx,24+selectedItem*20+paddingy,198,20, HUDround,HUDround);
		g.setColor(Color.black);
		g.drawString("Gold: "+p.getGold(),640-paddingx,20+paddingy);
		inventory =p.getInventory();
		for (int i=0;i<inventory.length;i++)
		{
			if(inventory[i]!=null)
			{
				g.drawString(inventory[i].getName(),650-paddingx,40+20*i+paddingy);
				drawIcon(g, inventory[i].getIcon(), 620-paddingx, 25+20*i+paddingy);
				
				if(selectedItem<inventory.length&&!(inventory[selectedItem] == null))
					drawItemPane(g, inventory[selectedItem]);
			}
			
			if (p.isEquipped(inventory[i])&&inventory[i]!=null) {
				//g.drawString("E",605,40+20*i);	
				drawIcon(g,3, 601-paddingx,24+20*i+paddingy);
			}
		}
		g.drawString("Exit",640-paddingx,40+20*10+paddingy);
		g.drawString("Save",640-paddingx,40+20*11+paddingy);
		g.drawString("Load",640-paddingx,40+20*12+paddingy);
		
		
		
		g.setColor(tempCol);
		g.setFont(tempFont);
		g2d.setComposite(original);
	}
	
	public void drawShop(Graphics g, Pointer c) {
		this.c=c;
		if (c.getPointer()==3)
		{
			selectedItemx=1;
			c.setPointer(6);	
		}
		if (c.getPointer()==2)
		{
			if (selectedItem<shop.length)
			selectedItem++;
			c.setPointer(6);	
		}
		if (c.getPointer()==1)
		{
			selectedItemx=0;
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
			if (selectedItem==10 && selectedItemx==1)
				c.setPointer(11);//back out of inventory
			else if (selectedItem<shop.length)
			{
				if(selectedItemx==0&&p.getGold()>=shop[selectedItem].getPrice()) {
					p.addItem(shop[selectedItem]);
					p.pay(shop[selectedItem].getPrice());
				} 
				else if(selectedItemx==1&&selectedItem<10) {
					if(p.isEquipped(inventory[selectedItem]))
					p.unequip(inventory[selectedItem]);
					p.pay(-inventory[selectedItem].getPrice());
					p.removeItem(inventory[selectedItem]);
				}
				else {
					System.out.println("tried to fetch non-existent item");
				}
				c.setPointer(6);	
			}
			
			else
				c.setPointer(11);//back out of inventory
		}
		
		
		int paddingx = 7;
		int paddingy = 7;
		
		
		Graphics2D g2d = (Graphics2D) g;
		Color tempCol = g.getColor();
		Font tempFont = g.getFont();
		Composite original = g2d.getComposite();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, HUDalpha));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		
		//player inventory
		g.setColor(Color.white);
		g.fillRoundRect(600-paddingx,0+paddingy,200,400, HUDround,HUDround); // player
		g.fillRoundRect(400-paddingx*2,0+paddingy,200,400, HUDround,HUDround); // shop
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));
		
		
		g.setColor(Color.black);
		g.drawRoundRect(600-paddingx,0+paddingy,200,400, HUDround,HUDround);
		g.drawString("Gold: "+p.getGold(),640,20);
		inventory =p.getInventory();
		for (int i=0;i<inventory.length;i++)
		{
			if(inventory[i]!=null)
			{
				g.drawString(inventory[i].getName(),650-paddingx,40+20*i+paddingy);
				drawIcon(g, inventory[i].getIcon(), 620-paddingx, 25+20*i+paddingy);
				
				if(selectedItem<inventory.length&&selectedItemx==1&&!(inventory[selectedItem] == null))
					drawItemPane(g, inventory[selectedItem]);
			}
			
			if (p.isEquipped(inventory[i])&&inventory[i]!=null) {
				//g.drawString("E",605,40+20*i);	
				drawIcon(g,3, 601-paddingx,24+20*i+paddingy);
			}
		}
		g.drawString("Exit",640-paddingx,40+20*10+paddingy);
		//end player inventory
		
		//shop
		
		//white rectangle for shop drawn with player above so we don't have to change 
		//anti aliasing back and forth too much
		g.setColor(Color.black);
		g.drawRoundRect(400-paddingx*2,0+paddingy,200,400, HUDround,HUDround);
		g.setColor(Color.red);
		g.drawRoundRect(401+200*selectedItemx-paddingx*(2-selectedItemx),24+selectedItem*20+paddingy,198,20, HUDround,HUDround);
		g.setColor(Color.black);
		g.drawString("Shop: ",440-paddingx*2,20+paddingy);
		
		for (int i=0;i<shop.length;i++)
		{
			if(shop[i]!=null)
			{
				g.drawString(shop[i].getName(),450-paddingx*2,40+20*i+paddingy);
				drawIcon(g, shop[i].getIcon(), 420-paddingx*2, 25+20*i+paddingy);
				
				if(selectedItem<shop.length&&selectedItemx==0&&!(shop[selectedItem] == null))
					drawItemPane(g, shop[selectedItem]);
			}
			
			if (p.isEquipped(shop[i])&&shop[i]!=null) {
				//g.drawString("E",605,40+20*i);	
				drawIcon(g,3, 401-paddingx*2,24+20*i+paddingy);
			}
		}
		g.drawString("Exit",440-paddingx*2,40+20*shop.length+paddingy);
		//end shop
		
		g.setColor(tempCol);
		g.setFont(tempFont);
		g2d.setComposite(original);
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
		drawIcon(g, 0, 0+offsetx,3*thickness+offsety+3); //gold
		drawIcon(g, 1, 80+offsetx,3*thickness+offsety+3); //shield
		drawIcon(g, 2, 160+offsetx,3*thickness+offsety+3); //sword
		
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
		float percentage = (float)currentVal / (float)maxVal;
		
		if(battleHUD) g.setColor(color.brighter().brighter());
		else g.setColor(color.darker().darker());
		
		g.drawRect(xStart-1, yStart-1, length+1, thickness+1);
		
		g.setColor(Color.WHITE);
		g.fillRect(xStart, yStart, length, thickness);
		
		g.setColor(color);
		g.fillRect(xStart, yStart, (int)(percentage*length), thickness);
		
		/* ROUNDED STUFF
		
		
		int barRound = HUDround/2;
		
		g.drawRoundRect(xStart-1, yStart-1, length+1, thickness+1, barRound,barRound);
		
		g.setColor(Color.WHITE);
		g.fillRoundRect(xStart, yStart, length, thickness, barRound,barRound);
		
		g.setColor(color);
		g.fillRoundRect(xStart, yStart, (int)(percentage*length), thickness, barRound,barRound);
		
		ROUNDED STUFF */
	}
	
	public void drawIcon(Graphics g, int iconID, int xStart, int yStart) {
		g.drawImage(icons[iconID], xStart,yStart, null);
	}
	
	public void drawLabel(Graphics g, String label, int xStart, int yStart, Color color) {
		Color temp = g.getColor();
		Font labelFont = new Font("DialogInput",Font.BOLD,15);	
		g.setFont(labelFont);
		g.setColor(color);
  		g.drawString(label,xStart,yStart);
		g.setColor(temp);
	}
	
	public void drawLabelInt(Graphics g, String label, int val, int xStart, int yStart, Color color) {
		Color temp = g.getColor();
		Font labelFont = new Font("DialogInput",Font.BOLD,15);	
		g.setFont(labelFont);
		g.setColor(color);
  		g.drawString(label + Integer.toString(val),xStart,yStart);
		g.setColor(temp);
	}
	
	public void drawLabelIntComp(Graphics g, String label, int val, String label2, int offsetx, int xStart, int yStart, Color color) {
		Color temp = g.getColor();
		Font labelFont = new Font("DialogInput",Font.BOLD,15);	
		g.setFont(labelFont);
		g.setColor(color);
  		g.drawString(label + Integer.toString(val),xStart,yStart);
		
		Color color2;
		if(label2 == "(+)") color2 = Color.GREEN;
		else if(label2 == "(-)") color2 = Color.RED;
		else color2 = Color.BLUE;
		
		g.setColor(color2);
  		g.drawString(label2,xStart+offsetx,yStart);
		
		g.setColor(temp);
	}
	
	public void drawTextInBox(Graphics g, String str, int xOffset, int yOffset, int width, int height) {
		Font f = g.getFont();

		JLabel textLabel = new JLabel(str);
		textLabel.setFont(f);
		textLabel.setSize(textLabel.getPreferredSize());
		BufferedImage bi = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);

		Graphics g0 = bi.createGraphics();
		g0.setColor(Color.BLACK);
		textLabel.paint(g0);
					
		g.drawImage(bi, xOffset, yOffset, this);
	
	}
	
	public void battleDraw(Graphics g) {
		Color temp = g.getColor();
	
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
		drawIcon(g, 1, 0+offsetx,2*thickness+offsety+3); //shield
		drawIcon(g, 2, 80+offsetx,2*thickness+offsety+3); //sword
		
		Color textColor = Color.WHITE;
		
		drawLabel(g, Integer.toString(m.getDefense()), 30+offsetx, (int)(thickness*2.7)+offsety+3, textColor);
		drawLabel(g, Integer.toString(m.getStrength()), 110+offsetx, (int)(thickness*2.7)+offsety+3, textColor);
		
		g.setColor(temp);		  
	}
	
	public void drawItemPane(Graphics g, Item i) {
		
		Graphics2D g2d = (Graphics2D) g;
		Color tempCol = g.getColor();
		Font tempFont = g.getFont();
		Composite original = g2d.getComposite();
		//g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, HUDalpha));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		int paddingx = 5;
		int paddingy = 5;
		int offsetx = 15; // dist from left of screen
		int offsety = 600-160+paddingy*3; // dist from top of screen
		int length = 800-paddingx*2;
		int height = 160-paddingy*2;
		int col2Offset = length/2; // x distance between two main (inventory and equip) columns
		int compOffset = 150; // x distance between column and comparison strings
		//int thickness = 20;
		String better = "(+)";
		String worse  = "(-)";
		String same   = "(=)";
		String healthComp = "";
		String strComp = "";
		String defComp = "";
		String spdComp = "";
		String prcComp = "";
		
		
		//for some reason, alpha needs to be set lower here for it to look the same as other things
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, HUDalpha-0.1F));
		g.setColor(Color.WHITE);
		g.fillRoundRect(800-length-paddingx, 600-height-paddingy, length, height, HUDround,HUDround);
		
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0F));
		
		g.setColor(Color.BLACK);
		g.drawRoundRect(800-length-paddingx, 600-height-paddingy, length, height, HUDround,HUDround);
		
		g.drawLine(length/2, 600-height-paddingy, length/2, 600-paddingy);
		
		Item e = p.equippedOfType(i);
		
		if(e != null) {
		
			//if(e.getName() == i.getName())
			
			healthComp = comparison(i.getHealth(), e.getHealth(), better, worse, same);
			strComp = comparison(i.getStrength(), e.getStrength(), better, worse, same);
			defComp = comparison(i.getDefense(), e.getDefense(), better, worse, same);
			spdComp = comparison(i.getSpeed(), e.getSpeed(), better, worse, same);
			prcComp = comparison(i.getPrice(), e.getPrice(), better, worse, same);
		
		
			drawIcon(g, e.getIcon(), offsetx+col2Offset, offsety+0*thickness-3);
			drawLabel(g, e.getName(), offsetx+col2Offset+26, offsety+ 0*thickness + 10, Color.BLACK);
			drawLabelInt(g, "Health: ", e.getHealth(), offsetx+col2Offset, offsety+ 1*thickness + 10, Color.BLACK);
			drawLabelInt(g, "Strength: ", e.getStrength(), offsetx+col2Offset, offsety+ 2*thickness + 10, Color.BLACK);
			drawLabelInt(g, "Defense: ", e.getDefense(), offsetx+col2Offset, offsety+ 3*thickness + 10, Color.BLACK);
			drawLabelInt(g, "Speed: ", e.getSpeed(), offsetx+col2Offset, offsety+ 4*thickness + 10, Color.BLACK);
			drawLabelInt(g, "Price: ", e.getPrice(), offsetx+col2Offset, offsety+ 5*thickness + 10, Color.BLACK);
		}
		
		//getIcon returns int IconID
		drawIcon(g, i.getIcon(), offsetx, offsety+0*thickness-3);
		drawLabel(g, i.getName(), offsetx+26, offsety+ 0*thickness + 10, Color.BLACK);
		//drawLabelInt(g, "Health: ", i.getHealth(), offsetx, offsety+ 1*thickness + 10, Color.BLACK);
		drawLabelIntComp(g, "Health: ", i.getHealth(), healthComp, compOffset, offsetx, offsety+ 1*thickness + 10, Color.BLACK);
		drawLabelIntComp(g, "Strength: ", i.getStrength(), strComp, compOffset, offsetx, offsety+ 2*thickness + 10, Color.BLACK);
		drawLabelIntComp(g, "Defense: ", i.getDefense(), defComp, compOffset, offsetx, offsety+ 3*thickness + 10, Color.BLACK);
		drawLabelIntComp(g, "Speed: ", i.getSpeed(), spdComp, compOffset, offsetx, offsety+ 4*thickness + 10, Color.BLACK);
		drawLabelIntComp(g, "Price: ", i.getPrice(), prcComp, compOffset, offsetx, offsety+ 5*thickness + 10, Color.BLACK);
		drawIcon(g,3, length-paddingx-20,offsety);
		
		
		g.setColor(tempCol);
		g.setFont(tempFont);
		g2d.setComposite(original);
	}
	
	public void drawInteractionPane(Graphics g, int sID) {
	
		Graphics2D g2d = (Graphics2D) g;
		Color tempCol = g.getColor();
		Font tempFont = g.getFont();
		Composite original = g2d.getComposite();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, HUDalpha));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		
		int paddingx = 5;
		int paddingy = 5;
		int offsetx = 15; // dist from left of screen
		int offsety = 600-160+paddingy*3; // dist from top of screen
		int length = 800-paddingx*2;
		int height = 160-paddingy*2;
		int col2Offset = length/2; // x distance between two main (inventory and equip) columns
		int compOffset = 150; // x distance between column and comparison strings
		//int thickness = 20;
		
		
		g.setColor(Color.WHITE);
		g.fillRoundRect(800-length-paddingx, 600-height-paddingy, length, height, HUDround,HUDround);
		
		
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0F));
		
		g.setColor(Color.BLACK);
		g.drawRoundRect(800-length-paddingx, 600-height-paddingy, length, height, HUDround,HUDround);
		
		String str;
		
		str =  "<html><h1>My name is " + nd.getName(sID) + ".</h1><br>";
		str += nd.getDesc(sID);
		drawTextInBox(g, str, offsetx, offsety, (int)(0.8*length), height); 
		
		
		g.setColor(tempCol);
		g.setFont(tempFont);
		g2d.setComposite(original);
	}
	
	public void drawStats(Graphics g, Player p) {
	
		Graphics2D g2d = (Graphics2D) g;
		Color tempCol = g.getColor();
		Font tempFont = g.getFont();
		Composite original = g2d.getComposite();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, HUDalpha));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		int width = 200;
		int height = 300;
		
		int paddingx = 5;
		int paddingy = 5;
		int offsetx = (800-width)/2; // dist from left of screen
		int offsety = (600-height)/2; // dist from top of screen
		
		g.setColor(Color.BLACK);
		g.drawRect(offsetx, offsety, width, height);
		
		g.setColor(Color.WHITE);
		g.fillRect(offsetx, offsety, width, height);
		
		g.setColor(Color.BLACK);
	
		
		g.setColor(tempCol);
		g.setFont(tempFont);
		g2d.setComposite(original);
	}
	
	public String comparison(int one, int two, String greater, String less, String equal) {
		if(one == two) return equal;
		else if(one > two) return greater;
		else return less;
	}
	
	public void initMinimap(int[] mapwidth, int[] mapheight, int tilesize, TileMap[] theMap, Image[] tileImages, Dimension d) {
		this.mapwidth = mapwidth;
		this.mapheight = mapheight;
		this.tilesize = tilesize;
		this.theMap = theMap;
		this.tileImages = tileImages;
		appSizeX = d.width;
		appSizeY = d.height;
	}
	
	public void drawMinimap(Graphics g, int maptracker) {	
		
		Color tempCol = g.getColor();
	
		g.setColor(Color.RED);
		
		int scale = 4;
		
		int width = appSizeX/2;
		int height = appSizeY/2;
		
		int offsetx = (appSizeX-width)/2;
		int offsety = (appSizeY-height)/2;
		
		int tilesizemini = tilesize/scale;
	
		int xDraw = 0;
		int yDraw = 0;
		
		
		//for(int y=0; y<mapheight[maptracker] && yDraw <= appSizeY; y++) {
		for(int y=0; y<mapheight[maptracker] && yDraw-offsety <= height; y++) {
			for(int x=0; x<mapwidth[maptracker]; x++) {
				//xDraw = (p.getX())*tilesizemini + x*tilesizemini;
				//yDraw = (p.getY())*tilesizemini + y*tilesizemini;
				xDraw = offsetx + x*tilesizemini;
				yDraw = offsety + y*tilesizemini;
				if(xDraw-offsetx >= width) break;
				
				
				g.drawImage(tileImages[theMap[maptracker].getVal(x,y)],xDraw,yDraw, tilesizemini, tilesizemini,this);
				
				if(x == p.getX() && y == p.getY()) {
					g.fillRect(xDraw,yDraw,tilesizemini,tilesizemini);
				}
				
			}
		}
		
		
		
		
		g.setColor(tempCol);		
	}

}
