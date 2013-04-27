import java.util.*;
import java.util.Map;
import java.util.HashMap;

class TileData
{
	private Map<Integer, Integer> soundMap = new HashMap<Integer, Integer>();
	
	
	private boolean NOCLIP = true;
	
	private int[] restricted = new int[] {-1, 2, 3}; //-1 is outside map
	private int[] walkRestricted = new int[] {-1, 2, 3};
	private int[] battleRestricted = new int[] {-1, 4};
	private int town = 4;
	
    
    public TileData() {
		
		/// soundMap.put(id of tile, id of sound)
		soundMap.put(1, 0); //grass
		soundMap.put(5, 1); //wood
		soundMap.put(6, 1); //wood
		
	}
	
	public int[] getRestricted() {
		return restricted;
	}
	
	public boolean isWalkRestricted(int t)
	{
		if(NOCLIP && t != -1) return false;
		
		for(int i = 0; i < walkRestricted.length; i++)
			if(t == walkRestricted[i])
				return true;
		return false;
	}
	public boolean isTown(int t)
	{
		if (t==town)
			return true;
		else return false;
	}
	public boolean isBattleRestricted(int t)
	{
		for(int i = 0; i < battleRestricted.length; i++)
			if(t == battleRestricted[i])
				return true;
		return false;
	}
	
	public int getSoundID(int tileID) {
		if(soundMap.get(tileID) != null) {
		return soundMap.get(tileID);
		} else {
			
			return -1;
		}
		
	}
		
}