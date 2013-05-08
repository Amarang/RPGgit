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

public class RPG extends Applet implements KeyListener
{
	static int TILESIZE = 20;
	static int NUMMAPS = 4;
	static int MAPWIDTH[] = {400,90,43,80};
	static int MAPHEIGHT[] = {300,70,50,60};
	static int TILETYPES = 50;
	static int NUMSPRITES = 2;
	static int NUMITEMS = 11;
	static int NUMICONS = 6;
	static int NUMMONSTERS = 10;
	static int NUMSPELLS = 4;
	static int NUMBOSSES = 1;
	static int NUMSOUNDCLIPS = 2;
	static int WALKINGDELAY = 10; //default 125
	static int BATTLEFREQUENCY = 0; //percentage of encounter per step (default 3)
	static int FPS = 60;
	int boss=0;
	
	/////DEBUG/////
	//also can hit f key to toggle
	boolean debug = true;
	/////DEBUG/////
	
	boolean running;// = true;
	
	TileData td = new TileData();
	int appSizeX = 800;
	int appSizeY = 600;
	private TileMap[] theMap = new TileMap[NUMMAPS];
	int startx = appSizeX/2/TILESIZE;
	int starty = appSizeY/2/TILESIZE;
	int maptracker = 0;
	
	long prevPaint = 0;
	long currPaint = 0;
	
	int nearSprite = -1;
	int shopkeeperID = 1;
	
	static boolean music = true;
	boolean sound = true;	
	SoundClip[] soundClips = new SoundClip[NUMSOUNDCLIPS];
	SoundClip hit;
	SoundClip death;
	SoundClip battlemusic;
	SoundClip bossmusic;
	SoundClip outofbounds;
	
	Image[] tileImages = new Image[TILETYPES];
	Image[] monsterImages = new Image[NUMMONSTERS];
	Image[] icons = new Image[NUMICONS];

	Image player;
	Image player1;
	Image player2;
	Image player1a;
	Image player2a;
	Image u,u1,u2,u3,u4, l,l1,l2,l3,l4, d,d1,d2,d3,d4, r,r1,r2,r3,r4;
	
	
	Player p = new Player(startx, starty,20,5,6,3,2,20,0,1, "Batman");
	Monster m;
	Spell[] spell = new Spell[NUMSPELLS];
	Battle b;
	HUD hud;
	Sprite[] sp = new Sprite[NUMSPRITES];
	Sprite pSp;
	Item[] item = new Item[NUMITEMS];
	Item[] shop = new Item[NUMITEMS];
	Pointer c = new Pointer(0);
	Minimap mm;
	
	Random rand = new Random(); 
 	Font title = new Font("DialogInput",Font.BOLD,20);	
	
	BufferedReader br;
	AudioClip intro;
    URL base;
	WaitThread wait;
    MediaTracker mt;
	
	boolean released = false;
	boolean key_space = false;
	boolean key_enter = false;
	boolean run = false;
	boolean battle = false;
	boolean firsttimebattle = true;
	boolean firststep = false;
	boolean oktomove = true;
	boolean withinrangesprite = false;
	
	boolean showinventory = false;
	boolean showstats = false;
	boolean showminimap = false;
	boolean showinteraction = false;

	
	Message msg;
	 
	public static void main(String[] args) { } 
	
	public void init()
	{
		
		running = true;
		
		msg = new Message(appSizeX, appSizeY);
		msg.setTextAndStart("Hi BAMasdfAN HW QR YOU D)TAY!(", 2000);
		
		
		System.out.println("anear beginning of init in RPG.java");
		
		for (int i=0;i<NUMMAPS;i++)
			theMap[i] = new TileMap(MAPWIDTH[i], MAPHEIGHT[i], "maps/file"+i+".txt");
		start();
		wait = new WaitThread();
		wait.start();
		try 
		{
			URL soundURL = getClass().getResource("music/glory.wav");
			intro = Applet.newAudioClip(soundURL);
		} catch (Exception e) {System.out.println("failed to load clip");}
		intro.play();
		
		for(int i = 0; i < soundClips.length; i++) {
			soundClips[i] = new SoundClip("clip" + Integer.toString(i));
		}
		for(int i = 0; i < item.length; i++) {
			item[i] = new Item(i,"items/items.txt");
			shop[i] = item[i];	
		}
		for(int i = 0; i < spell.length; i++) {
			spell[i] = new Spell(i,"data/spells.txt");	
		}
		p.addItem(item[0]);
		p.addItem(item[5]);
		
		
		hit = new SoundClip("hit");
		death = new SoundClip("death");
		battlemusic = new SoundClip("battlemusic");
		bossmusic = new SoundClip("bossmusic");
		outofbounds = new SoundClip("goat");
		
		try { 
			base = getDocumentBase();    
		} catch (Exception e) {} 	

        mt = new MediaTracker(this);
		for(int i = 0; i < tileImages.length; i++) {
			Image img = getImage(base, "images/tiles/" + Integer.toString(i) + ".png");
			mt.addImage(img, i+1);
			tileImages[i] = img;
		}
		for(int i = 0; i < monsterImages.length; i++) {
			Image img = getImage(base, "images/entities/monsters/id"+Integer.toString(i) + ".png");
			mt.addImage(img, i+1);
			monsterImages[i] = img;
		}
		 for(int i = 0; i < icons.length; i++) {
			Image img = getImage(base, "images/icons/"+Integer.toString(i) + ".png");
			mt.addImage(img, i+1);
			icons[i] = img;
		}
		 
		player = getImage(base,"images/entities/player/player.png");
		player1 = getImage(base,"images/entities/player/player1.png");
		player2 = getImage(base,"images/entities/player/player2.png");
		player1a = getImage(base,"images/entities/player/player1a.png");
		player2a = getImage(base,"images/entities/player/player2a.png");
        Image[] playerImgs = new Image[] {player, player1, player2, player1a, player2a};
				
		u = getImage(base,"images/entities/playertestb/u.png");
		l = getImage(base,"images/entities/playertestb/l.png");
		d = getImage(base,"images/entities/playertestb/d.png");
		r = getImage(base,"images/entities/playertestb/r.png");
		u1 = getImage(base,"images/entities/playertestb/u1.png");
		l1 = getImage(base,"images/entities/playertestb/l1.png");
		d1 = getImage(base,"images/entities/playertestb/d1.png");
		r1 = getImage(base,"images/entities/playertestb/r1.png");
		u2 = getImage(base,"images/entities/playertestb/u2.png");
		l2 = getImage(base,"images/entities/playertestb/l2.png");
		d2 = getImage(base,"images/entities/playertestb/d2.png");
		r2 = getImage(base,"images/entities/playertestb/r2.png");
		u3 = getImage(base,"images/entities/playertestb/u3.png");
		l3 = getImage(base,"images/entities/playertestb/l3.png");
		d3 = getImage(base,"images/entities/playertestb/d3.png");
		r3 = getImage(base,"images/entities/playertestb/r3.png");
		u4 = getImage(base,"images/entities/playertestb/u4.png");
		l4 = getImage(base,"images/entities/playertestb/l4.png");
		d4 = getImage(base,"images/entities/playertestb/d4.png");
		r4 = getImage(base,"images/entities/playertestb/r4.png");
        Image[] playerDirs = new Image[] {u,u1,u2,u3,u4, r,r1,r2,r3,r4, d,d1,d2,d3,d4, l,l1,l2,l3,l4};
				
		
		pSp = new Sprite(playerDirs, p, TILESIZE);
		
        for (int i = 0; i < NUMSPRITES; i++)
        {
			sp[i] = new Sprite(playerImgs, rand.nextInt(10)+20, rand.nextInt(10)+20, TILESIZE, i);
			
			sp[i].setSpeed(900+rand.nextInt(700));
			sp[i].start();
        }
		 
        try { 
              mt.waitForAll(); 
         } catch (InterruptedException  e) {}
		  
		addKeyListener(this);
		
		hud = new HUD(p, icons, shop);
		//hud.initMinimap(MAPWIDTH, MAPHEIGHT, TILESIZE, theMap, tileImages, this.getSize());
		mm = new Minimap(MAPWIDTH, MAPHEIGHT, TILESIZE, theMap, tileImages, p, this.getSize());
			
	}
	
	public boolean isFree() {
		return !battle&&!showinventory&&!showinteraction&&!showstats&&!showminimap;
	}
	
  	public void keyPressed(KeyEvent evt) 
	{
		int key=0; key = evt.getKeyCode(); 
		if (oktomove)
		{
			if(key==KeyEvent.VK_A)
			{
				released = false;
				c.setPointer(1);
				if (isFree()) step('l');
				oktomove=false;
			}
			if(key==KeyEvent.VK_S)
			{
				released = false;
				c.setPointer(2);
				if (isFree()) step('d');
				oktomove=false;
			}
			if(key==KeyEvent.VK_D)
			{
				released = false;
				c.setPointer(3);
				if (isFree()) step('r');
				oktomove=false;
			}
			if(key==KeyEvent.VK_W)
			{
				released = false;
				c.setPointer(0);
				if (isFree()) step('u');
				oktomove=false;
			}
			/*if(key==KeyEvent.VK_SPACE)
			{
				c.setPointer(10);
			}*/
			if(key==KeyEvent.VK_ENTER)
			{
				c.setPointer(7);
				key_enter=true;
			}
		}
	}
	
	public class WaitThread extends Thread { //create thread class
		WaitThread() {//bring in arguments for the thread to use	
		}
		public void run() { //executes when thread is called
			while(true)
			{
				delay(WALKINGDELAY);
				oktomove=true;
				repaint();	
			}	
		}
	}
	
	public void DrawMap(Graphics g)
	{	
		int xDraw = 0;
		int yDraw = 0;
		
		Dimension appletSize = this.getSize();
		int appSizeY = appletSize.height;
		int appSizeX = appletSize.width;
		
		for(int y=Math.max(0,p.getY()-starty); y<MAPHEIGHT[maptracker] && yDraw <= appSizeY; y++) {
			for(int x=Math.max(0,p.getX()-startx); x<MAPWIDTH[maptracker]; x++) {
				xDraw = (startx-p.getX())*TILESIZE + x*TILESIZE;
				yDraw = (starty-p.getY())*TILESIZE + y*TILESIZE;
				if(xDraw >= appSizeX) break;
				
				g.drawImage(tileImages[theMap[maptracker].getVal(x,y)],xDraw,yDraw, TILESIZE, TILESIZE,this);
				
			}
		}
		
		
		repaint();			
	}
	
	public void HandlePointer() {
	
		
		if (c.getPointer()==7)
		{
			if(showinventory) showinventory=false;
			else showinventory=true;	
			c.setPointer(5);
		}
		
		if (c.getPointer()==11&&nearSprite>=0)
		{ // c 11 = E
			if(showinteraction) {
				showinteraction = false;
				nearSprite = -1;
			}
			else showinteraction=true;
			if(nearSprite >= 0) showinteraction=true;
			
			System.out.println(c.getPointer());	
				
			c.setPointer(5);
			
		}
		
		if (c.getPointer()==12)
		{ // c 12 = R
			if(showstats) {
				showstats = false;
			}
			else showstats=true;
				
			c.setPointer(5);
			
		}
		
		if (c.getPointer()==13)
		{ // c 13 = M
			if(showminimap) {
				showminimap = false;
			}
			else showminimap=true;
				
			c.setPointer(5);
			
		}
		
	}
	
	public void PlayerMenu(Graphics g)
	{
		hud.draw(g);
		if (showinventory&&!showinteraction)
		{
			hud.drawInventory(g,c);
		}
		if (showinteraction&&nearSprite==shopkeeperID)
		{
			hud.drawShop(g,c);
		}	
		if (showstats)
		{
			hud.drawStats(g,p);
		}
		if (showminimap)
		{
			//hud.drawMinimap(g,maptracker);
			mm.draw(g,maptracker);
		}	
	}
	public void update(Graphics g) {
	
		if(!running) return;
		
		Graphics offgc;
		Image offscreen = null;
		Dimension d = size();
		offscreen = createImage(d.width, d.height);
		offgc = offscreen.getGraphics();
		offgc.setColor(getBackground());
		offgc.fillRect(0, 0, d.width, d.height);
		offgc.setColor(getForeground());
		paint(offgc);
		g.drawImage(offscreen, 0, 0, this);
		
	}
	
	 
	public void destroy() {
		intro.stop();
		running = false;
	}
	
	public void stop() {
		intro.stop();
		running = false;
	}
	
	public void start() {
		if(running == false)
			intro.play();
		running = true;
	}
	
	public void paint(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	
		try { Thread.sleep((int)(1000.0/FPS)); }
		catch(InterruptedException e) {}

		prevPaint = System.currentTimeMillis();
		int dt = (int)(prevPaint - currPaint);
		float fps = 1000.0F/(float)dt;
		currPaint = prevPaint;
		
		
		
	
		if(!battle)
		{
			p.setBattleCondition(false);
			DrawMap(g);	
			HandlePointer();
			
			//if(!withinrangesprite) showinteraction = false;
			
			//if(showinteraction && withinrangesprite && nearSprite >= 0) {
			if(showinteraction && nearSprite >= 0) {
				hud.drawInteractionPane(g,nearSprite);
			}
			
			pSp.setSpeed(40);
			pSp.updateAnimationP(g, System.currentTimeMillis());
			
			
			msg.update(g, System.currentTimeMillis());
			
			for (int i = 0; i< NUMSPRITES; i++)
         	{
				if(sp[i].isReady()&&maptracker==1) {
					sp[i].updateAnimationRand(g, System.currentTimeMillis(), p);
					sp[i].allowMove(theMap[maptracker].getNeighbors(sp[i].getX(), sp[i].getY()));
				}
         	}
			PlayerMenu(g);
		}
		
		else if (battle)
		{
			
			msg.stop();
			p.setBattleCondition(true);
			if(firsttimebattle) {
				wait.suspend();
				intro.stop();
				b = new Battle(p, monsterImages,c,icons,hit,death,battlemusic,bossmusic,boss,spell);
				firsttimebattle = false;
			} else {
				
				if(!b.BattleSequence(g, key_space, key_enter)) {
					battle = false;
					firsttimebattle = true;
					battlemusic.stop();
					bossmusic.stop();
					death.stop();
					intro.play();
					wait.resume();
					boss=0;
				}
				key_space=false;
			}	
		}
		
		int endPaint = (int)(System.currentTimeMillis() - currPaint);
		
		
		////////////////////////
		/////DEBUG DISPLAY//////
		////////////////////////
		
		if(debug) {
			Color tempc = g.getColor();
			Composite original = g2d.getComposite();
			
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75F));
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			
			g.setColor(Color.BLACK);
			
			g.fillRoundRect(610,480, 175, 110, 20, 20);
			g.setColor(Color.WHITE);
			
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85F));
			g.drawString("curr tile:  " + theMap[maptracker].getVal(p.getX(), p.getY()), 620,500);
			g.drawString("face tile:  " + theMap[maptracker].getFacing(p.getX(), p.getY(), p.getFacing()), 620,520);
			g.drawString("(x,y):     (" + p.getX() + "," + p.getY() + ")", 620,540);
			g.drawString("fps:        " + Math.round(fps), 620,560);
			g.drawString("ms to paint:" + endPaint, 620,580);
			
			g.setColor(tempc);
			g2d.setComposite(original);
		}
		
		////////////////////////
		/////DEBUG DISPLAY//////
		////////////////////////
		
		
	}

	public void step(char direction) {
		
		outofbounds.stop();
		switch(direction) {
			case 'l':
				if(!p.moveLeftB()) {
					outofbounds.play();
					msg.setTextAndStart("You can\'t move there!",600);
				}
				break;
			case 'r':
				if(!p.moveRightB()) {
					outofbounds.play();
					msg.setTextAndStart("You can\'t move there!",600);
				}
				break;
			case 'u':
				if(!p.moveUpB()) {
					outofbounds.play();
					msg.setTextAndStart("You can\'t move there!",600);
				}
				break;
			case 'd':
				if(!p.moveDownB()) {
					outofbounds.play();
					msg.setTextAndStart("You can\'t move there!",600);
				}
				break;
		}
		int currTile = theMap[maptracker].getVal(p.getX(), p.getY());
		int facingTile = theMap[maptracker].getFacing(p.getX(), p.getY(), p.getFacing());
		//{x, y}
		int[] facingCoords = theMap[maptracker].getFacingCoords(p.getX(), p.getY(), p.getFacing());
		
		//keep for future tile debugging
		
		//System.out.println("P " + p.getX() + ", " + p.getY());
		//System.out.println("currtile " + currTile);
		//System.out.println("facing " + p.getFacing());
		//System.out.println("facetile " + facingTile);
		
		pSp.start();
		
		//withinrangesprite = false;
		nearSprite = -1;
		for (int i=0;i< sp.length;i++)
		{
			
			//if(theMap[maptracker].within(p, sp[i], 3)) {
				//withinrangesprite = true;
			//}
				
			if( (sp[i].getX() == facingCoords[0]
			 &&  sp[i].getY() == facingCoords[1])
			 || (sp[i].getX() == p.getX())
			 &&  sp[i].getY() == p.getY() )
			{
				//System.out.println("NEAR SPRITE id = " + sp[i].getID());
				nearSprite = sp[i].getID();
				break;
			}
		}	
		
		if ((rand.nextInt(1000) < BATTLEFREQUENCY * 10||(p.getX()==85&&p.getY()==57))
			&& !td.isBattleRestricted(currTile) && maptracker == 0)
		{
			if(p.getX()==85&&p.getY()==57)
				boss=1; 
			battle=true;
		}
		
		if (td.isTown(currTile))
		{
			msg.setTextAndStart("Changing maps", 1500);
			if (maptracker==1)
			{
				maptracker=0;
				p.setX(p.getTownX());
				p.setY(p.getTownY());
			}
			else if (maptracker==0)
			{
				maptracker=1;
				hud.updateNPCInfo();
				
				p.setTownX(p.getX());
				p.setTownY(p.getY());
				p.setX(p.getTownEntranceX());
				p.setY(p.getTownEntranceY());	
			}
			//System.out.println(maptracker);
			
			
			for (int i=0;i< NUMSPRITES;i++)
			{
				sp[i].resetOrigin();
			}	
		}
		if (td.isBed(currTile))
		{
			if( ( p.getHealth()!=p.getHealthMax()
			   || p.getMana()!=p.getManaMax() ) && p.getGold()>=7 )
			{
				p.setHealth(p.getHealthMax());
				p.setMana(p.getManaMax());
				p.pay(7);
			}
		}
		if(sound) {	
			int sID = td.getSoundID(currTile);
			if(sID != -1) {
				soundClips[sID].stop();
				soundClips[sID].play();	
			}
		}
		p.allowMove(theMap[maptracker].getNeighbors(p.getX(), p.getY()));
	}

	public void keyReleased(KeyEvent evt)	
	{
		int key=0; key = evt.getKeyCode(); 
		if (battle)
		oktomove=true;
		repaint();
		if(key==KeyEvent.VK_SPACE)
		{
			key_space=true;
			if (!battle)
			c.setPointer(10);
		}
		if(key==KeyEvent.VK_E)
		{
			if (!battle)
				c.setPointer(11);
		}
		if(key==KeyEvent.VK_R)
		{
			if (!battle)
				c.setPointer(12);
		}
		if(key==KeyEvent.VK_M)
		{
			if (!battle)
				c.setPointer(13);
		}
		
		if(key==KeyEvent.VK_F)
		{
			if(debug) debug = false;
			else debug = true;
		}
	} 	
	
    public void keyTyped(KeyEvent evt)		{
			int key=0; key = evt.getKeyCode(); 
			if(key==KeyEvent.VK_ENTER)
			{
				key_enter=true;
			}
			
    	} 
    public void delay(double n)
	{
		long startDelay = System.currentTimeMillis();
		long endDelay = 0;
		while (endDelay - startDelay < n)
			endDelay = System.currentTimeMillis();	
	}
}