class Pointer {
	private int choice = 1;	
    public Pointer(int x) { choice = x; }
    public void reset() { choice = 0; }
    public int getPointer() { return choice; }
    public void setPointer(int d) { choice = d; }
} //shortest class ever