import java.awt.*;
import javax.swing.*;
import java.lang.Math;
import java.awt.image.BufferedImage;
import java.util.List;

class HUD
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
	private int statSelectedY=0;
	private Color HPColor = new Color(230, 0, 0);
	private Color ManaColor = new Color(0, 0, 170);
	private Color ExperienceColor = new Color(255, 255, 50);
	
	private Color HPColor2 = new Color(108, 0, 0);
	private Color ManaColor2 = new Color(0, 0, 80);
	private Color ExperienceColor2 = new Color(155, 155, 0);
	
	private int thickness = 20;
	private boolean battleHUD = false;
	private boolean loadedRecently = false;
	private boolean savedRecently = false;
	private NPCData nd;// = new NPCData();
	Spell[] spell;
	
	private Utils u = new Utils();
	

    Message msg = new Message(800,600);
	
	private float HUDalpha = 0.85F;
	private int HUDround = 10;
    
    public HUD(Player p, Image[] icons,Item[] shop) {
		System.out.println("made HUD");
        this.p = p;
		this.icons = icons;
		this.shop = shop;
		//this.load = load;
    }
		
	public HUD(Player p, Monster m, Image[] icons, Spell[] spell) {
		System.out.println("made HUD");
        this.p = p;
		this.m = m;
		this.icons = icons;
		this.spell = spell;
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
				savedRecently = false;
			}
			else if(selectedItem==11)
			{
				if(!savedRecently) {
					p.save();
				}
				savedRecently = true;
			}
			else if(selectedItem==12)
			{
				if(!loadedRecently) {
					p.load();
					updateNPCInfo()
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
		Color tempCol = g.getColor();
	
		int health = p.getHealth();
		int healthmax = p.getHealthMax();
		int level = p.getLevel();
		int gold = p.getGold();
		int experience = p.getExperience();
		int mana = p.getMana();
		int manamax = p.getManaMax();
		int levelExperience = p.getLevelExperience();
		
		int offsetx = 5;
		int offsety = 5;		
		
		//hp, mana, exp bars
		drawBar(g, offsetx, 0*thickness + offsety, 250, thickness-2, HPColor, health, healthmax);
		drawBar(g, offsetx, 1*thickness + offsety, 250, thickness-2, ManaColor, mana, manamax);
		drawBar(g, offsetx, 2*thickness + offsety, 250, thickness-2, ExperienceColor, experience, levelExperience);
		
		//icons
		drawIcon(g, 0, 0+offsetx,3*thickness+offsety+3); //gold
		drawIcon(g, 1, 80+offsetx,3*thickness+offsety+3); //shield
		drawIcon(g, 2, 160+offsetx,3*thickness+offsety+3); //sword
		
		Color textColor;
		if(battleHUD) textColor = Color.WHITE;
		else textColor = Color.BLACK;
		
		u.drawLabel(g, Integer.toString(p.getGold()), 30+offsetx, (int)(thickness*3.7)+offsety+3, textColor);
		u.drawLabel(g, Integer.toString(p.getDefense()), 110+offsetx, (int)(thickness*3.7)+offsety+3, textColor);
		u.drawLabel(g, Integer.toString(p.getStrength()), 190+offsetx, (int)(thickness*3.7)+offsety+3, textColor);
		
		u.drawLabel(g, p.getName() + " (level " + Integer.toString(p.getLevel()) + ")", 10+offsetx, (int)(thickness*4.7)+offsety+3, textColor);
		
		if(health >= healthmax/3)
			u.drawLabel(g, Integer.toString(health)+" / "+Integer.toString(healthmax),
				  15+offsetx, (int)(0.7*thickness)+offsety, Color.WHITE);
		else
			u.drawLabel(g, Integer.toString(health)+" / "+Integer.toString(healthmax),
				  15+offsetx, (int)(0.7*thickness)+offsety, HPColor2);
		
		if(mana >= manamax/3)
			u.drawLabel(g, Integer.toString(mana)+" / "+Integer.toString(manamax),
				  15+offsetx, (int)(1.7*thickness)+offsety, Color.WHITE);
		else
			u.drawLabel(g, Integer.toString(mana)+" / "+Integer.toString(manamax),
				  15+offsetx, (int)(1.7*thickness)+offsety, ManaColor2);
				  
		u.drawLabel(g, Integer.toString(experience)+" / "+Integer.toString(levelExperience),
			  15+offsetx, (int)(2.7*thickness)+offsety, Color.BLACK);
				  
		g.setColor(tempCol);
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
	}
	
	public void drawIcon(Graphics g, int iconID, int xStart, int yStart) {
		g.drawImage(icons[iconID], xStart,yStart, null);
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
		
		drawBar(g, offsetx, 0*thickness+offsety, 250, thickness-2, HPColor, health, healthmax);
		drawBar(g, offsetx, 1*thickness+offsety, 250, thickness-2, ManaColor, mana, mana);
		
		u.drawLabel(g, Integer.toString(health)+" / "+Integer.toString(healthmax),
				  offsetx+15, (int)(0.7*thickness)+offsety, Color.WHITE);
		u.drawLabel(g, Integer.toString(mana)+" / "+Integer.toString(mana),
				  offsetx+15, (int)(1.7*thickness)+offsety, Color.WHITE);
				  
		//icons
		drawIcon(g, 1, 0+offsetx,2*thickness+offsety+3); //shield
		drawIcon(g, 2, 80+offsetx,2*thickness+offsety+3); //sword
		
		Color textColor = Color.WHITE;
		
		u.drawLabel(g, Integer.toString(m.getDefense()), 30+offsetx, (int)(thickness*2.7)+offsety+3, textColor);
		u.drawLabel(g, Integer.toString(m.getStrength()), 110+offsetx, (int)(thickness*2.7)+offsety+3, textColor);
		
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
		
		//todo
		//for some reason, alpha needs to be set lower here for it to look the same as other things
		//I think issue is that itempane gets called twice (or more), so it gets drawn over itself
		//so that makes it less transparent if you overlap it twice
		//theory is that it gets overlapped twice, so I made alpha HUDalpha^2
		
		
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, HUDalpha*HUDalpha));
		g.setColor(Color.WHITE);
		g.fillRoundRect(800-length-paddingx, 600-height-paddingy, length, height, HUDround,HUDround);
		
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0F));
		
		g.setColor(Color.BLACK);
		g.drawRoundRect(800-length-paddingx, 600-height-paddingy, length, height, HUDround,HUDround);
		
		g.drawLine(length/2, 600-height-paddingy, length/2, 600-paddingy);
		
		Item e = p.equippedOfType(i);
		
		if(e != null) {
		
			//if(e.getName() == i.getName())
			
			healthComp = u.comparison(i.getHealth(), e.getHealth(), better, worse, same);
			strComp = u.comparison(i.getStrength(), e.getStrength(), better, worse, same);
			defComp = u.comparison(i.getDefense(), e.getDefense(), better, worse, same);
			spdComp = u.comparison(i.getSpeed(), e.getSpeed(), better, worse, same);
			prcComp = u.comparison(i.getPrice(), e.getPrice(), better, worse, same);
		
		
			drawIcon(g, e.getIcon(), offsetx+col2Offset, offsety+0*thickness-3);
			u.drawLabel(g, e.getName(), offsetx+col2Offset+26, offsety+ 0*thickness + 10, Color.BLACK);
			u.drawLabelInt(g, "Health: ", e.getHealth(), offsetx+col2Offset, offsety+ 1*thickness + 10, Color.BLACK);
			u.drawLabelInt(g, "Strength: ", e.getStrength(), offsetx+col2Offset, offsety+ 2*thickness + 10, Color.BLACK);
			u.drawLabelInt(g, "Defense: ", e.getDefense(), offsetx+col2Offset, offsety+ 3*thickness + 10, Color.BLACK);
			u.drawLabelInt(g, "Speed: ", e.getSpeed(), offsetx+col2Offset, offsety+ 4*thickness + 10, Color.BLACK);
			u.drawLabelInt(g, "Price: ", e.getPrice(), offsetx+col2Offset, offsety+ 5*thickness + 10, Color.BLACK);
		}
		
		//getIcon returns int IconID
		drawIcon(g, i.getIcon(), offsetx, offsety+0*thickness-3);
		u.drawLabel(g, i.getName(), offsetx+26, offsety+ 0*thickness + 10, Color.BLACK);
		//drawLabelInt(g, "Health: ", i.getHealth(), offsetx, offsety+ 1*thickness + 10, Color.BLACK);
		u.drawLabelIntComp(g, "Health: ", i.getHealth(), healthComp, compOffset, offsetx, offsety+ 1*thickness + 10, Color.BLACK);
		u.drawLabelIntComp(g, "Strength: ", i.getStrength(), strComp, compOffset, offsetx, offsety+ 2*thickness + 10, Color.BLACK);
		u.drawLabelIntComp(g, "Defense: ", i.getDefense(), defComp, compOffset, offsetx, offsety+ 3*thickness + 10, Color.BLACK);
		u.drawLabelIntComp(g, "Speed: ", i.getSpeed(), spdComp, compOffset, offsetx, offsety+ 4*thickness + 10, Color.BLACK);
		u.drawLabelIntComp(g, "Price: ", i.getPrice(), prcComp, compOffset, offsetx, offsety+ 5*thickness + 10, Color.BLACK);
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
		u.drawTextInBox(g, str, offsetx, offsety, (int)(0.8*length), height); 
		
		
		g.setColor(tempCol);
		g.setFont(tempFont);
		g2d.setComposite(original);
	}
	
	public void drawStats(Graphics g, Player p, Pointer c) {
		
		Graphics2D g2d = (Graphics2D) g;
		Color tempCol = g.getColor();
		Font tempFont = g.getFont();
		Composite original = g2d.getComposite();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, HUDalpha));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		int numStats = 4;
		
		int width = 200;
		int height = 300;
		
		int paddingx = 10;
		int paddingy = 5;
		int offsetx = (800-width)/2; // dist from left of screen
		int offsety = (600-height)/2; // dist from top of screen
		int thickness = 20;
		

		if(c.getPointer() == 0) {
			statSelectedY--;
			c.setPointer(6);	
		}
		else if(c.getPointer() == 2) {
			statSelectedY++;
			c.setPointer(6);	
		}
		if(statSelectedY > numStats-1) statSelectedY = numStats-1;
		if(statSelectedY < 0) statSelectedY = 0;
		
		if(c.getPointer() == 10) {
			c.setPointer(6);	
			if(p.getStatPoints() > 0) {
			
				p.addStatPoints(-1);
				
				switch(statSelectedY) {
				
				case 0:	//str
					p.setStrength(p.getStrength()+1);
					break;
				case 1: //spd
					p.setSpeed(p.getSpeed()+1);
					break;
				case 2: //def
					p.setDefense(p.getDefense()+1);
					break;
				case 3: //mana
					p.setManaMax(p.getManaMax()+1);
					break;
				}
			
			} else {
				msg.setTextAndStart("No more stat points!", 250);
			}
			
		}
		msg.update(g, System.currentTimeMillis());

		g.setColor(Color.WHITE);
		g.fillRoundRect(offsetx, offsety, width, height, HUDround, HUDround);
		
		g.setColor(Color.BLACK);
		g.drawRoundRect(offsetx, offsety, width, height, HUDround, HUDround);
		

		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0F));
		
		g.setColor(Color.RED);
		u.drawCenteredRoundRect(g, offsetx + 166, offsety + thickness*(statSelectedY+3)-4, 25, thickness, HUDround);

		g.setColor(Color.BLACK);
		g.drawString(String.format("%-11s%3d", "Stat points", p.getStatPoints()), offsetx+paddingx*2, offsety+thickness);
		
		//str 0, spd 1, def 2, mana 3
		g.drawString(String.format("  %-9s%3d", "Strength", p.getStrength()), offsetx+paddingx*2, offsety+thickness*3);
		g.drawString(String.format("  %-9s%3d", "Speed", p.getSpeed()), offsetx+paddingx*2, offsety+thickness*4);
		g.drawString(String.format("  %-9s%3d", "Defense", p.getDefense()), offsetx+paddingx*2, offsety+thickness*5);
		g.drawString(String.format("  %-9s%3d", "Mana", p.getManaMax()), offsetx+paddingx*2, offsety+thickness*6);
		
		g.drawString(String.format("%-14s  (+)", " "), offsetx+paddingx, offsety+thickness*3);
		g.drawString(String.format("%-14s  (+)", " "), offsetx+paddingx, offsety+thickness*4);
		g.drawString(String.format("%-14s  (+)", " "), offsetx+paddingx, offsety+thickness*5);
		g.drawString(String.format("%-14s  (+)", " "), offsetx+paddingx, offsety+thickness*6);
		
		drawIcon(g, 2, offsetx+paddingx, offsety+thickness*3-13);
		drawIcon(g, 6, offsetx+paddingx, offsety+thickness*4-13);
		drawIcon(g, 1, offsetx+paddingx, offsety+thickness*5-13);
		drawIcon(g, 4, offsetx+paddingx, offsety+thickness*6-13);
		
		for(int i = 0; i < 8; i++) {
			g.drawString("BANANA BANANA BANANA", offsetx+paddingx, offsety+thickness*(7+i));
		
		}
		
		g.setColor(tempCol);
		g.setFont(tempFont);
		g2d.setComposite(original);
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

	
	public void drawTicker(Graphics g, List<String> inputStrings) {
		Graphics2D g2d = (Graphics2D) g;
		Color tempCol = g.getColor();
		Font tempFont = g.getFont();
		
		int numLines = inputStrings.size();
		if(numLines < 1) return;
		
		int padding = 10;
		int thickness = 20;
		int width = 800;//applet
		double widthPercentage = 0.75; //%age of width to take up
		int height = 600;//applet
		
		String[] lines = inputStrings.subList(Math.max(0,inputStrings.size()-numLines),inputStrings.size()).toArray(new String[0]);
		
		Composite original = g2d.getComposite();
		//g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLACK);
		
		g.fillRoundRect(padding,(height-thickness*numLines-padding*2),(int)(widthPercentage*(width-2*padding)),thickness*numLines+padding, padding, padding);
		
		g.setColor(Color.WHITE);
		g.drawRoundRect(padding,(height-thickness*numLines-padding*2),(int)(widthPercentage*(width-2*padding)),thickness*numLines+padding, padding, padding);
		
		//g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		Font labelFont = new Font("Monospaced",Font.BOLD,15);	
		g.setFont(labelFont);
		
		for(int i = 0; i < lines.length; i++) {
			if(i==lines.length-1) g.setColor(new Color(215, 75, 75));
			g.drawString(lines[i],padding*2,height-thickness*(lines.length+1-(i+1))-padding+15/2+2);
		}
		
		g.setColor(tempCol);
		g.setFont(tempFont);
		
		g2d.setComposite(original);
	}
	public boolean drawItems(Graphics g, Pointer c, boolean key_space) {
		System.out.println(c.getPointer());
		System.out.println("hmm");
		
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
		if (key_space)
		{
			if (selectedItem<10)
			{
				if(!(inventory[selectedItem] == null)) {
					System.out.println(inventory[selectedItem].getUseage());
					if(!p.alreadySameType(inventory[selectedItem]) && !p.isEquipped(inventory[selectedItem])&&inventory[selectedItem].getUseage()==0)
					{
						p.equip(inventory[selectedItem]);
						return true;
					} else if (p.isEquipped(inventory[selectedItem])){
						p.unequip(inventory[selectedItem]);
						return true;
					}
					else if(!p.alreadySameType(inventory[selectedItem]) && !p.isEquipped(inventory[selectedItem])&&inventory[selectedItem].getUseage()==1)
					{
						p.use(inventory[selectedItem]);
						p.removeItem(inventory[selectedItem]);
						return true;
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
				savedRecently = false;
				return true;
			}
			/*else if(selectedItem==11)
			{
				if(!savedRecently)
				{
					p.save();
				}
				savedRecently = true;
			}
			else if(selectedItem==12)
			{
				if(!loadedRecently) {
					p.load();
					updateNPCInfo()
				}
				loadedRecently = true;
			}
			//load.LoadData(p);*/
		}
		
		int paddingx = 7;
		int paddingy = 150;
		
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
				drawIcon(g,3, 601-paddingx,24+20*i+paddingy);
			}
		}
		g.drawString("Exit",640-paddingx,40+20*10+paddingy);
		//g.drawString("Save",640-paddingx,40+20*11+paddingy);
		//g.drawString("Load",640-paddingx,40+20*12+paddingy);
		
		g.setColor(tempCol);
		g.setFont(tempFont);
		g2d.setComposite(original);
		return false;
	}
	public int drawSpells(Graphics g, Pointer c, boolean key_space) {
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
		if (key_space)
		{
			if (selectedItem<10)
			{
				if(!(spell[selectedItem] == null)) {
					return spell[selectedItem].getSpellID();
					
				} else {
					System.out.println("tried to fetch non-existent item");
				}
				c.setPointer(6);	
			}
			else if(selectedItem==10)
			{
				c.setPointer(7);//back out of spell
				loadedRecently = false;
				return 0;
			}
			/*else if(selectedItem==11)
			{
				if(!savedRecently)
				{
					p.save();
				}
				savedRecently = true;
			}
			else if(selectedItem==12)
			{
				if(!loadedRecently) {
					p.load();
					updateNPCInfo()
				}
				loadedRecently = true;
			}
			//load.LoadData(p);*/
		}
		
		int paddingx = 7;
		int paddingy = 150;
		
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
		for (int i=0;i<spell.length;i++)
		{
			if(spell[i]!=null)
			{
				g.drawString(spell[i].getName(),650-paddingx,40+20*i+paddingy);
			}
		}
		g.drawString("Exit",640-paddingx,40+20*10+paddingy);
		//g.drawString("Save",640-paddingx,40+20*11+paddingy);
		//g.drawString("Load",640-paddingx,40+20*12+paddingy);
		
		
		
		g.setColor(tempCol);
		g.setFont(tempFont);
		g2d.setComposite(original);
		return -1;
	}
}
