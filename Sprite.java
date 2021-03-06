import java.awt.*;
import java.util.Random;


class Sprite
{
	private boolean multiframe = false;
	private boolean isReady = false;
	private boolean running = false;
	private int ID = -1;
	private int x, y;
	private int originx, originy;
	private Image image;
	private Image[] frames;
	private int numFrames = 5;
	private int TILESIZE = 20;
	private int currentFrame = 0;
	Random rand = new Random();
	private long previousTime = 0;
	private int msPerFrame = 100;
	
	private int framesPerDirection;
	
	private boolean l = true;
    private boolean r = true;
    private boolean u = true;
    private boolean d = true;
    
    private int counter = 0;
	
	private Player p;
	
	int xmove;
	int ymove;
	public Sprite() {
		isReady = false;
	}
	TileData td = new TileData();
	public Sprite(Image image, int x, int y, int TS, int ID) {
		//System.out.println("instantiated single image sprite");
		isReady = true;
		multiframe = false;
		this.ID = ID;
		this.x = x;
		this.y = y;
		this.image = image;
		originx=x;
		originy=y;
		TILESIZE = TS;
	}
	
	public Sprite(Image[] images, int x, int y, int TS, int ID) {
		//System.out.println("instantiated multi-image sprite");
		isReady = true;
		multiframe = true;
		this.ID = ID;
		this.x = x;
		this.y = y;
		originx=x;
		originy=y;
		this.frames = images;
		numFrames = images.length;
		TILESIZE = TS;
	}
	
	public Sprite(Image[] images, Player p, int TS) {
		//System.out.println("instantiated multi-image sprite");
		isReady = true;
		multiframe = true;
		this.x = p.getX();
		this.y = p.getY();
		this.p = p;
		originx=x;
		originy=y;
		this.frames = images;
		numFrames = images.length;
		framesPerDirection = numFrames >> 2;
		TILESIZE = TS;
	}
	public void addX (int d0) {x+=d0;}
	public void addY (int d0) {y+=d0;}
	public int getID() { return ID; }
	public void setID(int ID) { this.ID = ID; }
	public void setPos(int x, int y) { this.x = x; this.y = y; }
	public int getX() { return x; }
	public int getY() { return y; }
	public void resetOrigin() { originx=x; originy=y; }
	public void reset() { x=originx; y=originy;}
	
	public boolean isReady() { return isReady; }
	
	public boolean isEmpty() {
		 if(multiframe) return (frames.length == 0);
		 else return (image == null);
	}
	
	public void drawSprite(Graphics g) {
		//System.out.println("S " + x + ", " + y);
		g.drawImage(image,x*(TILESIZE),y*(TILESIZE), null);
	}
	
	public void drawFrame(Graphics g, int frame) {
		//g.drawImage(frames[frame],x*(TILESIZE),y*(TILESIZE), this);
		try {
		g.drawImage(frames[frame],x*(TILESIZE),y*(TILESIZE), null);
		} catch (Exception e) { System.out.println("tried to get frame " + frame); }
	}
	
	public void drawFrame2(Graphics g, int frame, int px, int py) {
		
		int startx=800/2/TILESIZE;//MAPWIDTH / 2;
		int starty=600/2/TILESIZE;//MAPHEIGHT / 2;
		try {
		g.drawImage(frames[frame],
				   (startx-px)*TILESIZE + x*TILESIZE,
				   (starty-py)*TILESIZE + y*TILESIZE, null);
		} catch (Exception e) { System.out.println("tried to get frame " + frame); }
	}
	
	//public void start() { running = true; }
	public void start() { running = true; this.counter = 0;}
	public void stop() { this.running = false; }
	
	public void setSpeed(int msPerFrame) {
		this.msPerFrame = msPerFrame;
	}
	
	public void updateAnimation(Graphics g, long time) {
		if(running) {
			drawFrame(g, currentFrame);
			if(previousTime == 0 || time - previousTime >= msPerFrame) {
				
				currentFrame++;
				previousTime = time;
			}
		}
		if(currentFrame == numFrames) {
			currentFrame = 0;
		}
	}
	
	public void updateAnimationP(Graphics g, long time) {
		//update animation for player
		int facing = p.getFacing();
		
		drawFrame(g, facing*framesPerDirection);
		//so first 4 images must be cardinal directions (U R D L) (N E S W)
		if(running) {
			drawFrame(g, facing*framesPerDirection+counter);
			if(previousTime == 0 || time - previousTime >= msPerFrame) {
				counter++;
				previousTime = time;
			}
			if(counter == framesPerDirection) {
				stop();
			}
		}
	}
	
	public void updateAnimationRand(Graphics g, long time, Player p) {
		if(running) {
		
			drawFrame2(g, currentFrame, p.getX(), p.getY());
			
			
			if(previousTime == 0 || time - previousTime >= msPerFrame) {
			
				currentFrame=rand.nextInt(5);
				xmove=rand.nextInt(3);
				ymove=rand.nextInt(3);
				int XorY = rand.nextInt(3);
				if(XorY == 0) {
					if (xmove==0){moveLeft();}
					if (xmove==2){moveRight();}
				}
				if(XorY == 2) {
					if (ymove==0){moveDown();}
					if (ymove==2){moveUp();}
				}
				previousTime = time;
			}
		}
		if(currentFrame == numFrames) currentFrame = 0;
	}
	public void moveLeft() { if(canMoveLeft()) x--; }
    public void moveRight() { if(canMoveRight()) x++; }
    public void moveUp() { if(canMoveUp()) y--; }
    public void moveDown() { if(canMoveDown()) y++; }
	private boolean canMoveLeft() { return l; }
    private boolean canMoveRight() { return r; }
    private boolean canMoveUp() { return u; }
    private boolean canMoveDown() { return d; }    
	public void allowMove(int[] directions) {
		l = td.isWalkRestricted(directions[0]) ? false : true;
		r = td.isWalkRestricted(directions[1]) ? false : true;
		u = td.isWalkRestricted(directions[2]) ? false : true;
		d = td.isWalkRestricted(directions[3]) ? false : true;
	}
}