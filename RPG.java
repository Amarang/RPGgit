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
	static int NUMMAPS = 2;
	static int MAPWIDTH[] ={400,90};
	static int MAPHEIGHT[] = {300,70};
	static int TILETYPES = 14;
	static int NUMSPRITES = 10;
	static int NUMITEMS = 6;
	static int NUMICONS = 4;
	static int NUMMONSTERS = 6;
	static int NUMSOUNDCLIPS = 2;
	static int WALKINGDELAY = 250;
	static int BATTLEFREQUENCY = 3; //percentage of encounter per step
	static String map;
	
	TileData td = new TileData();
	int appSizeX = 800;
	int appSizeY = 600;
	private TileMap[] theMap= new TileMap[2];
	int startx=800/2/TILESIZE;//MAPWIDTH / 2;
	int starty=600/2/TILESIZE;//MAPHEIGHT / 2;
	int maptracker = 0;
	//WalkingThread walking;
	int offsetX = 0;
	int offsetY = 0;
	int checkx = 0;
	int checky = 0;
	int playerposition=1;
	int mapx=0;
	int mapy=0;
	
	static boolean music = true;
	boolean sound = true;	
	SoundClip[] soundClips = new SoundClip[NUMSOUNDCLIPS];
	SoundClip hit;
	SoundClip battlemusic;
	
	
	
	Image[] tileImages = new Image[TILETYPES];
	Image[] monsterImages = new Image[NUMMONSTERS];
	Image[] icons = new Image[NUMICONS];

	Image player;
	Image player1;
	Image player2;
	Image player1a;
	Image player2a;
	
	
	Player p = new Player(startx, starty,20,5,6,3,2,20,0,1, "Batman");
	Monster m;
	Battle b;
	HUD hud;
	Sprite[] sp= new Sprite[NUMSPRITES];
	Item[] item= new Item[NUMITEMS];
	Pointer c = new Pointer(0);

	Random rand = new Random(); 
 	Font title = new Font("DialogInput",Font.BOLD,20);	
	
	BufferedReader br;
	AudioClip intro;
    URL base;
	WaitThread wait;
    MediaTracker mt;
	
	
	boolean released = false;
	boolean key_space=false;
	boolean key_enter=false;
	boolean run=false;
	boolean battle=false;
	boolean firstTimeBattle = true;
	boolean firststep = false;
	boolean oktomove = true;
	boolean showinventory=false;
	 
	public static void main(String[] args) { } 
	
	public void init()
	{
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
			p.addItem(item[i]);
		}
		
		hit = new SoundClip("hit");
		battlemusic = new SoundClip("battlemusic");
		
		
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
        for (int i = 0; i < NUMSPRITES; i++)
        {
			sp[i] = new Sprite(playerImgs, rand.nextInt(40)*2, rand.nextInt(30)*2,TILESIZE);
        }
		 
		 
        try { 
              mt.waitForAll(); 
         } catch (InterruptedException  e) {}
		  
		addKeyListener(this);
		
		hud = new HUD(p, icons);
		
		p.equip(item[5]);	
			
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
			if (!battle&&!showinventory)
				step('l');
			oktomove=false;
		}
		if(key==KeyEvent.VK_S)
		{
			released = false;
			c.setPointer(2);
			if (!battle&&!showinventory)
				step('d');
			oktomove=false;
		}
		if(key==KeyEvent.VK_D)
		{
			released = false;
			c.setPointer(3);
			if (!battle&&!showinventory)
				step('r');
			oktomove=false;
		}
		if(key==KeyEvent.VK_W)
		{
			released = false;
			c.setPointer(0);
			if (!battle&&!showinventory)
				step('u');
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
		checkx = 0;
		checky = 0;
		if(!showinventory)
		switch(c.getPointer())
		{
			case 0:checky=1;break;
			case 1:checkx=1;break;
			case 2:checky=-1;break;
			case 3:checkx=-1;break;
		}
		for(int x=0; x<MAPWIDTH[maptracker];x++) {
			for(int y=0; y<MAPHEIGHT[maptracker];y++) {
				g.drawImage(tileImages[theMap[maptracker].getVal(x,y)],
						   (startx-p.getX())*TILESIZE - checkx*TILESIZE/2 + x*TILESIZE,
						   (starty-p.getY())*TILESIZE - checky*TILESIZE/2 + y*TILESIZE, this);
			}
		}
		if (c.getPointer()==7)
		{
			if(showinventory) showinventory=false;
			else showinventory=true;	
				
				
			c.setPointer(5);
			
		}
		if (c.getPointer()<3&&!showinventory)
		{
			if(firststep==false)
			{
				g.drawImage(player1a,startx*TILESIZE,starty*TILESIZE, this);
				firststep=true;
				if(c.getPointer()==0) for (int i = 0; i< NUMSPRITES; i++) {sp[i].addY(1);}
				if(c.getPointer()==1) for (int i = 0; i< NUMSPRITES; i++) {sp[i].addX(1);}
				if(c.getPointer()==2) for (int i = 0; i< NUMSPRITES; i++) {sp[i].addY(-1);}
				
			}
			else{
				g.drawImage(player1,startx*TILESIZE,starty*TILESIZE, this);
				firststep=false;
					if(c.getPointer()==0) for (int i = 0; i< NUMSPRITES; i++) {sp[i].addY(1);}
					if(c.getPointer()==1) for (int i = 0; i< NUMSPRITES; i++) {sp[i].addX(1);}
					if(c.getPointer()==2) for (int i = 0; i< NUMSPRITES; i++) {sp[i].addY(-1);}
				c.setPointer(5);
				repaint();
			}
		}
		else if (c.getPointer()==3&&!showinventory)
		{
			if(firststep==false)
			{
				g.drawImage(player2a,startx*TILESIZE,starty*TILESIZE, this);
				firststep=true;
				for (int i = 0; i< NUMSPRITES; i++) {sp[i].addX(-1);}
			}
			else{
				g.drawImage(player2,startx*TILESIZE,starty*TILESIZE, this);
				firststep=false;
				c.setPointer(6);
				for (int i = 0; i< NUMSPRITES; i++) {sp[i].addX(-1);}
				repaint();
			}
		}	
		else if (c.getPointer()==5)
		{
			g.drawImage(player1,startx*TILESIZE,starty*TILESIZE, this);
		}
		else if (c.getPointer()==6)
		{
			g.drawImage(player2,startx*TILESIZE,starty*TILESIZE, this);	
		}
		else
		{
			g.drawImage(player2,startx*TILESIZE,starty*TILESIZE, this);	
		}
		//delay(20);
		repaint();			
	}
	public void PlayerMenu(Graphics g)
	{
		hud.draw(g);
		if (showinventory)
		{
			hud.drawInventory(g,c);
		}
	}
	public void update(Graphics g) {
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
	
	public void paint(Graphics g)
	{	
		if(!battle)
		{
			p.setBattleCondition(false);
			DrawMap(g);	
			
			
			for (int i = 0; i< NUMSPRITES; i++)
         	{
			if(sp[i].isReady()&&maptracker==1) {
				sp[i].drawSprite(g);
				sp[i].setSpeed(1000);
				sp[i].start();
				sp[i].updateAnimation(g, System.currentTimeMillis());
				sp[i].allowMove(theMap[maptracker].getNeighbors(p.getX(), p.getY()));
				}
			if (maptracker==0)
			sp[i].stop();
         	}
			PlayerMenu(g);
		}
		
		else if (battle)
		{
			
			p.setBattleCondition(true);
			if(firstTimeBattle) {
				wait.suspend();
				intro.stop();
				battlemusic.play(true);
				b = new Battle(g, p, monsterImages,c,hit,sp, icons);
				firstTimeBattle = false;
			} else {
				
				if(!b.BattleSequence(g, key_space, key_enter)) {
					battle = false;
					firstTimeBattle = true;
					battlemusic.stop();
					intro.play();
					wait.resume();
				}
				key_space=false;
			}	
		}
		
	}

	public void step(char direction) {
		
		
		int currTile = theMap[maptracker].getVal(p.getX(), p.getY());
		switch(direction) {
			case 'l': p.moveLeft(); break;
			case 'r': p.moveRight(); break;
			case 'u': p.moveUp(); break;
			case 'd': p.moveDown(); break;
		}
		
		if (rand.nextInt(1000) <= BATTLEFREQUENCY * 10
			&& !td.isBattleRestricted(currTile) && maptracker == 0)
		{
			battle=true;
		}
		
		if (td.isTown(currTile))
		{
			if (maptracker==1)
			{
				maptracker=0;
				p.setX(p.getTownX());
				p.setY(p.getTownY());
			}
			else if (maptracker==0)
			{
				maptracker=1;
				p.setTownX(p.getX());
				p.setTownY(p.getY());	
			}
			System.out.println(maptracker);
			
			
			for (int i=0;i< NUMSPRITES;i++)
			{
				sp[i].resetOrigin();
				//sp[i].stop();
			}	
		}
		if (td.isBed(currTile)&&p.getHealth()!=p.getHealthMax()&&p.getGold()>=20)
		{
			p.setHealth(p.getHealthMax());
			p.pay(20);
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

