import java.util.Map;
import java.util.HashMap;

class TileData
{
	private Map<Integer, Integer> soundMap = new HashMap<Integer, Integer>();
	
	
	private boolean NOCLIP = false;
	
	//private int[] restricted = new int[] {-1, 2, 3}; //-1 is outside map
	private int[] walkRestricted = new int[] {-1, 2, 3, 10, 14, 15, 16, 18, 20, 21, 23, 28, 29, 30, 32};
	private int[] battleRestricted = new int[] {-1, 4};

	private int[] vertRestricted = new int[] {5, 6, 24, 25, 26}; // horizontal bridges
	private int[] horizRestricted = new int[] {}; // vertical bridges
	
	//private int town = 4;
	private int bed = 12;
    
    public TileData() {
		//System.out.println("made TileData");
		/// soundMap.put(id of tile, id of sound)
		soundMap.put(1, 0); //grass
		soundMap.put(5, 1); //wood bridge
		soundMap.put(6, 1); //wood bridge
		
	}
	
	/*public int[] getRestricted() {
		return restricted;
	}*/
	
	public boolean isWalkRestricted(int t)
	{
		if(NOCLIP && t != -1) return false;
		
		for(int i = 0; i < walkRestricted.length; i++)
			if(t == walkRestricted[i])
				return true;
		return false;
	}
	public boolean isVertRestricted(int t)
	{
		if(NOCLIP && t != -1) return false;
		
		for(int i = 0; i < vertRestricted.length; i++)
			if(t == vertRestricted[i])
				return true;
		return false;
	}
	public boolean isHorizRestricted(int t)
	{
		if(NOCLIP && t != -1) return false;
		
		for(int i = 0; i < horizRestricted.length; i++)
			if(t == horizRestricted[i])
				return true;
		return false;
	}
	/*public boolean isTown(int t)
	{
		if (t==town)
			return true;
		else return false;
	}*/
	public boolean isBed(int t)
	{
		if (t==bed)
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