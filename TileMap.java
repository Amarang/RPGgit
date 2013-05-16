class TileMap
{
	private int MAPWIDTH;
	private int MAPHEIGHT;
	private byte matrix[][];
	
	private byte special1[][];
	private byte special2[][];
	private byte special3[][];
	private byte special4[][];
	private byte special5[][];
	private byte special6[][];
	
	TileData td = new TileData();
	
	public TileMap(int MAPWIDTH, int MAPHEIGHT, String fileName) {
		//System.out.println("made TileMap");
		matrix   = new byte[MAPHEIGHT][MAPWIDTH];
		special1 = new byte[MAPHEIGHT][MAPWIDTH];
		special2 = new byte[MAPHEIGHT][MAPWIDTH];
		special3 = new byte[MAPHEIGHT][MAPWIDTH];
		special4 = new byte[MAPHEIGHT][MAPWIDTH];
		special5 = new byte[MAPHEIGHT][MAPWIDTH];
		special6 = new byte[MAPHEIGHT][MAPWIDTH];
		
		for(int w=0; w<MAPWIDTH;w++) {
			for(int h=0; h<MAPHEIGHT;h++) {
				matrix[h][w]=-1;
				special1[h][w]=-1;
				special2[h][w]=-1;
				special3[h][w]=-1;
				special4[h][w]=-1;
				special5[h][w]=-1;
				special6[h][w]=-1;
			}
		}
			
		Load l = new Load();

		String[][] mapArr = l.readFileToArray(fileName, " ");
		
		for(int i=0; i < mapArr.length; i++) {
			for(int j=0; j < mapArr[0].length; j++) {
				matrix[i][j] = (byte)((int)mapArr[i][j].charAt(0) - 65);
				if(mapArr[i][j].length() <= 1) continue;
					special1[i][j] = (byte) ((int)mapArr[i][j].charAt(1) - 65);
				if(mapArr[i][j].length() <= 2) continue;
					special2[i][j] = (byte) ((int)mapArr[i][j].charAt(2) - 65);
				if(mapArr[i][j].length() <= 3) continue;
					special3[i][j] = (byte) ((int)mapArr[i][j].charAt(3) - 65);
				if(mapArr[i][j].length() <= 4) continue;
					special4[i][j] = (byte) ((int)mapArr[i][j].charAt(4) - 65);
				if(mapArr[i][j].length() <= 5) continue;
					special5[i][j] = (byte) ((int)mapArr[i][j].charAt(5) - 65);
				if(mapArr[i][j].length() <= 6) continue;
					special6[i][j] = (byte) ((int)mapArr[i][j].charAt(6) - 65);
			}
			
		}		
	}

	public int getVal(int x, int y) { return matrix[y][x]; }
	public int getSpecial1(int x, int y) { return special1[y][x]; }
	public int getSpecial2(int x, int y) { return special2[y][x]; }
	public int getSpecial3(int x, int y) { return special3[y][x]; }
	public int getSpecial4(int x, int y) { return special4[y][x]; }
	public int getSpecial5(int x, int y) { return special5[y][x]; }
	public int getSpecial6(int x, int y) { return special6[y][x]; }
	
	public int getFacing(int x, int y, int facing) {
		int f = -1;
		switch(facing) {
			case 0: try { f = (y == 0) ? -1 : matrix[y-1][x]; } catch (Exception e) { f = -1; } break;
			case 1: try { f = (x == MAPWIDTH-1) ? -1 : matrix[y][x+1]; } catch (Exception e) { f = -1; } break;
			case 2: try { f = (y == MAPHEIGHT-1) ? -1 : matrix[y+1][x]; } catch (Exception e) { f = -1; } break;
			case 3: try { f = (x == 0) ? -1 : matrix[y][x-1]; } catch (Exception e) { f = -1; } break;
		}
		return f;
	}
	
	public int[] getFacingCoords(int x, int y, int facing) {
		int[] coords = {x, y};
		switch(facing) {
			case 0: coords[1] = (y == 0) ? -1 : y-1; break;
			case 1: coords[0] = (x == MAPWIDTH-1) ? -1 : x+1; break;
			case 2: coords[1] = (y == MAPHEIGHT-1) ? -1 : y+1; break;
			case 3: coords[0] = (x == 0) ? -1 : x-1; break;
		}
		return coords;
	}
	
	public int getWidth() { return MAPWIDTH; }
	public int getHeight() { return MAPHEIGHT; }
	
	public void printMap(int type) {
		switch(type) {
			case 1: printArray(matrix); break;
			case 2: printArray(special1); break;
			case 3: printArray(special2); break;
			default: printArray(matrix); break;
		}
	}
	
	private void printArray(byte[][] arr) {
		for(int i = 0; i < arr.length; i++)
        {
            String buff = new String();
            for(int j = 0; j < arr[0].length; j++)
				buff += Integer.toString(arr[i][j]) + " ";
            System.out.println(buff);
        }
	}
	
	public int[] getNeighbors(int x, int y) {
		int l, r, u, d;
		try { l = (x == 0) ? -1 : matrix[y][x-1]; } catch (Exception e) { l = -1; }
		try { r = (x == MAPWIDTH-1) ? -1 : matrix[y][x+1]; } catch (Exception e) { r = -1; }
		try { u = (y == 0) ? -1 : matrix[y-1][x]; } catch (Exception e) { u = -1; }
		try { d = (y == MAPHEIGHT-1) ? -1 : matrix[y+1][x]; } catch (Exception e) { d = -1; }

		if(td.isVertRestricted(matrix[y][x])) {	u = -1;	d = -1;	}
		if(td.isHorizRestricted(matrix[y][x])) { l = -1; r = -1; }
		return new int[] {l, r, u, d};
	}
	
	public boolean within(Player a, Sprite b, int distance) {
		return ( (a.getX()-b.getX())*(a.getX()-b.getX()) +
				(a.getY()-b.getY())*(a.getY()-b.getY()) <= distance*distance);
	}
	
}