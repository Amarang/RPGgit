import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;

class Utils {
	
	
	public Utils() {
	}
	
	
	
	public void drawCenteredString(Graphics g, String s, int x, int y){  
            int width = (int)g.getFontMetrics().getStringBounds(s, g).getWidth();  
            int height = (int)g.getFontMetrics().getStringBounds(s, g).getHeight();  
            g.drawString(s, (int)(x-width/2), (int)(y+height/4));  
            //above math is done using the fact that x,y for drawString
            //specifies LOWER LEFT corner of string to be drawn
            //also, height seems to be double what it should be (hence /4)
    }
	
	public void drawCenteredRect(Graphics g, int x, int y, int w, int h){  
            g.drawRect((int)(x-w/2),(int)(y-h/2),w,h);
    }
	public void drawCenteredRoundRect(Graphics g, int x, int y, int w, int h, int r){  
            g.drawRoundRect((int)(x-w/2),(int)(y-h/2),w,h, r,r);
    }
	public void fillCenteredRect(Graphics g, int x, int y, int w, int h){  
            g.fillRect((int)(x-w/2),(int)(y-h/2),w,h);
    }
	public void fillCenteredRoundRect(Graphics g, int x, int y, int w, int h, int r){  
            g.fillRoundRect((int)(x-w/2),(int)(y-h/2),w,h, r,r);
    }
	
	public void drawCenteredImage(Graphics g, Image img, int x, int y){  
            g.drawImage(img, (int)(x-img.getWidth(null)/2), (int)(y-img.getHeight(null)/2), null);
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
					
		g.drawImage(bi, xOffset, yOffset, null);
	
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
	
	public String comparison(int one, int two, String greater, String less, String equal) {
		if(one == two) return equal;
		else if(one > two) return greater;
		else return less;
	}
	
}