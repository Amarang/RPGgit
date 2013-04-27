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


class Animator
{
	Images[] frames;
	
	public volatile boolean running = false;
	private long previousTime, speed;
	private int frameAtPause, currentFrame;
	
	public Animator(Images[] frames) {
		this.frames = frames;
	}
	public void setSpeed(long speed) {
		this.speed = speed; // in milliseconds
	}
	public void update(long time) {
	//time in milliseconds (System.currentTimeMillis() ?)
		if(running) {
			if(time - previousTime >= speed) {
				//update animation
				try {
				
				} catch() {
				
				}
				
				previousTime = time;
			}
		}
	}
	public void start() {
		running = true;
	}
	public void stop() {
		running = false;
	}
	public void pause() {
	
	}
}