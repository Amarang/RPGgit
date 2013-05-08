import java.applet.*;
import java.net.*;

class SoundClip
{
	private AudioClip clip;
	private boolean firstTime;
	private String cID = "";
	
	public SoundClip(String clipID) {
		//System.out.println("made SoundClip");
		try {
			cID = clipID;
			URL soundURL = getClass().getResource("music/"+clipID+".wav");
			clip = Applet.newAudioClip(soundURL);
		} catch (Exception e) {
			System.out.println("failed to load " + clipID);
		}
		firstTime = true;
	}
	public String getName() { return cID;	}
	public void play() {
		if(firstTime) {
			clip.play();
			firstTime = false;
		}
	}
	
	public void play(boolean loop) {
		if(firstTime) {
			if(loop) clip.loop();
			else clip.play();
			firstTime = false;
		}
	}
	
	public void stop() {
		firstTime = true;
		clip.stop();
	 }
	 
}

