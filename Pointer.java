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

