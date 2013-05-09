import java.awt.*;

class Minimap
{

	private int appSizeX;
	private int appSizeY;
	private int tilesize;
	private int[] mapwidth;
	private int[] mapheight;
	private TileMap[] theMap;
	private Image[] tileImages;
	
	private Player p;
    
	private static int NUMSTATES = 4; //"off" is a state as well
	private static int SCALE = 10; //new tile size is tilesize/scale
	private int state = 0; // 0 is off
	
	//state 0 off
	//state 1 large minimap centered
	//state 2 corner minimap
	//state 3 corner minimap slightly transparent
	//state 3 corner minimap no "facing"

	public Minimap(int[] mapwidth, int[] mapheight, int tilesize, TileMap[] theMap, Image[] tileImages, Player p, Dimension d) {
		this.mapwidth = mapwidth;
		this.mapheight = mapheight;
		this.tilesize = tilesize;
		this.theMap = theMap;
		this.tileImages = tileImages;
		this.p = p;
		appSizeX = d.width;
		appSizeY = d.height;
	}
	
	public void toggleState() {
		state++;
		if(state > NUMSTATES-1) {
			state = 0;
		}
		System.out.println("minimap state " + state);
	}
	
	public boolean stopMovement() {
		return state==1;
	}
	
	public boolean isVisible() {
		return !(state==0); //state 0 means minimap is off
	}
	
	public void drawCenteredString(Graphics g, String s, int x, int y){  
            int width = (int)g.getFontMetrics().getStringBounds(s, g).getWidth();  
            int height = (int)g.getFontMetrics().getStringBounds(s, g).getHeight();  
            g.drawString(s, (int)(x-width/2), (int)(y-height/2));  
    }
	
	public void draw(Graphics g, int maptracker) {	
		
		Graphics2D g2d = (Graphics2D) g;
		Color tempCol = g.getColor();
		Font tempFont = g.getFont();
		
		Composite original = g2d.getComposite();		
		
		
		int width, height, offsetx, offsety, facingx, facingy, coordsx, coordsy;
		boolean showFacing = true;
		boolean showCoordinates = true;
		String facingstr = "";
		
		
		
		if(state == 1) {
		
			width = appSizeX/2;
			height = appSizeY/2;
			offsetx = (appSizeX-width)/2;
			offsety = (appSizeY-height)/2;
			facingx = (int)(offsetx + width/2);
			facingy = (offsety + height + 30);
			

			coordsx = (int)(offsetx + width/2);
			coordsy = (offsety + height + 47);
		}
		else if(state == 2)
		{
		
			width = 150;//300;
			height = 150;//300;
			offsetx = 800-20;//(appSizeX-width)/2;
			offsety = 20;//(appSizeY-height)/2;
			facingx = (int)(offsetx + width/2);
			facingy = (offsety + height + 30);
			

			coordsx = (int)(offsetx + width/2);
			coordsy = (offsety + height + 47);
		}
		/*else if(state == 3)
		{
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5F));
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
			width = 150;//300;
			height = 150;//300;
			offsetx = 600;//(appSizeX-width)/2;
			offsety = 50;//(appSizeY-height)/2;
			facingx = (int)(offsetx + width/2);
			facingy = (offsety + height + 30);
			
		}*/
		else if(state == 3)
		{
			showFacing = false;
			showCoordinates = false;
		
			width = 150;//300;
			height = 150;//300;
			offsetx = 800-20;//(appSizeX-width)/2;
			offsety = 20;//(appSizeY-height)/2;
			facingx = (int)(offsetx + width/2);
			facingy = (offsety + height + 30);
			

			coordsx = (int)(offsetx + width/2);
			coordsy = (offsety + height + 47);
			
		} else {
		
			System.out.println("oops! state isn\'t quite right in minimap.java");
			return;
		}
		
		
		if(showFacing) {
			switch(p.getFacing()) {
				case 0: facingstr = "north"; break;
				case 1: facingstr = "east"; break;
				case 2: facingstr = "south"; break;
				case 3: facingstr = "west"; break;
				default: facingstr = "???"; break;
			}
		}
		
		
		
		int tilesizemini = tilesize/SCALE;
		
		int startx = (int)(width/tilesizemini/2);
		int starty = (int)(height/tilesizemini/2);
	
		int xDraw = 0;
		int yDraw = 0;
		
		g.setColor(Color.WHITE);
		g.fillRect(offsetx-1, offsety-1, width+1, height+tilesizemini*2+1);
		
		g.setColor(Color.BLACK);
		g.drawRect(offsetx-1, offsety-1, width+1, height+tilesizemini*2+1);

		if(showFacing)
			drawCenteredString(g, "facing " + facingstr, facingx, facingy);
		if(showCoordinates)
			drawCenteredString(g, "("+p.getX()+", "+p.getY()+")", coordsx, coordsy);
		
		g.setColor(Color.RED);
		
		for(int y=Math.max(0,p.getY()-starty); y<mapheight[maptracker] && yDraw-offsety <= height; y++) {
			for(int x=Math.max(0,p.getX()-startx); x<mapwidth[maptracker]; x++) {
			
				xDraw = (offsetx + (x-Math.max(0,p.getX()-startx))*tilesizemini);
				yDraw = (offsety + (y-Math.max(0,p.getY()-starty))*tilesizemini);
				
				if(xDraw-offsetx >= width) break;
				
				g.drawImage(tileImages[theMap[maptracker].getVal(x,y)],xDraw,yDraw, tilesizemini, tilesizemini,null);
				
				if(x == p.getX() && y == p.getY()) {
					g.fillRect(xDraw,yDraw,tilesizemini,tilesizemini);
					//g.fillRect(xDraw-tilesizemini,yDraw-tilesizemini,tilesizemini*2,tilesizemini*2);
					//above line will increase size of marker
				}
				
			}
		}
		
		
		g.setColor(tempCol);
		g.setFont(tempFont);
		g2d.setComposite(original);

		
	}
	
}