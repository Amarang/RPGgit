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



class Pointer
{
	private int choice = 1;
    private boolean l = true;
    private boolean r = true;
    private boolean u = true;
    private boolean d = true;
	Random rand = new Random(); 
	
    
    public Pointer(int x) {
    	
        choice = x;
    }
    
    public void reset() {
    	choice = 0;
    }
    
    public int getPointer() { return choice; }
    public void setPointer(int d) { choice = d; }
   // public void run() { if(canRun()) run=true; }
    

}

