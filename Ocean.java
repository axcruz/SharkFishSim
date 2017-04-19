/* Ocean.java */

/**
 *  The Ocean class defines an object that models an ocean full of sharks and
 *  fish.
 */

public class Ocean {
    public final static int EMPTY = 0;
    public final static int SHARK = 1;
    public final static int FISH = 2;

    private OceanNode[][] OceanList;
    private int OceanHeight;
    private int OceanWidth;
    private int starveTime;

    /**
     *  Ocean() is a constructor that creates an empty ocean having width i and
     *  height j, in which sharks starve after starveTime timesteps.
     *  @param i is the width of the ocean.
     *  @param j is the height of the ocean.
     *  @param starveTime is the number of timesteps sharks survive without food.
     */

    public Ocean(int i, int j, int starveTime) {
	if ((i <= 0) || (j <= 0)) {
	    System.out.println("Error: invalid dimensions");
	    System.exit(0);
	} if (starveTime <= 0) {
	    System.out.println("Error: invalid starve time");
	    System.exit(0);
	} else {
	    OceanWidth = i;
	    OceanHeight = j;
	    this.starveTime = starveTime;
	    OceanList = new OceanNode[OceanWidth][OceanHeight];
	    for (int c = 0; c < OceanWidth; c++) {
		for (int r = 0; r < OceanHeight; r++) {
		    OceanList[c][r] = new OceanNode();
		}
	    }
	}
    }
 
    /**
     *  wrap() is a helper method that adjusts coordinates such that they "wrap"
     *  around the edges of the ocean. xwrap and ywrap are variations for easier
     *  implementation.
     *  @param n is the coordinate.
     *  @param b is the base dimension of the ocean.
     *  @return the "wrapped" coordinate.
     */

    public static int wrap(int n, int b) {
	int wrapped;
	if (n >= 0) {
	    wrapped = (n % b);
	} else {
	    wrapped = ((n % b + b) % b);
	}
	return wrapped;
    }	

    // use on x-coordinates only.
    public int xwrap(int x) {
	return wrap(x, OceanWidth);
    }
    
    // use on y-coordinates only.
    public int ywrap(int y) {
	return wrap(y, OceanHeight);
    }

    /**
     *  width() returns the width of an Ocean object.
     *  @return the width of the ocean.
     */

    public int width() {
	return OceanWidth;
    }

    /**
     *  height() returns the height of an Ocean object.
     *  @return the height of the ocean.
     */

    public int height() {
	return OceanHeight;
    }

    /**
     *  starveTime() returns the number of timesteps sharks survive without food.
     *  @return the number of timesteps sharks survive without food.
     */

    public int starveTime() {
	return starveTime;
    }

    /**
     *  addFish() places a fish in cell (x, y) if the cell is empty.  If the
     *  cell is already occupied, leave the cell as it is.
     *  @param x is the x-coordinate of the cell to place a fish in.
     *  @param y is the y-coordinate of the cell to place a fish in.
     */

    public void addFish(int x, int y) {
	int xcoord = xwrap(x);
	int ycoord = ywrap(y);
	OceanNode cur = OceanList[xcoord][ycoord];
	cur.setFish();
    }

    /**
     *  addShark() (with two parameters) places a newborn shark in cell (x, y) if
     *  the cell is empty.  A "newborn" shark is equivalent to a shark that has
     *  just eaten.  If the cell is already occupied, leave the cell as it is.
     *  @param x is the x-coordinate of the cell to place a shark in.
     *  @param y is the y-coordinate of the cell to place a shark in.
     */

    public void addShark(int x, int y) {
	int xcoord = xwrap(x);
	int ycoord = ywrap(y);
	OceanNode cur = OceanList[xcoord][ycoord];
	cur.setShark();
    }

    /**
     *  cellContents() returns EMPTY if cell (x, y) is empty, FISH if it contains
     *  a fish, and SHARK if it contains a shark.
     *  @param x is the x-coordinate of the cell whose contents are queried.
     *  @param y is the y-coordinate of the cell whose contents are queried.
     */

    public int cellContents(int x, int y) {
	int xcoord = xwrap(x);
	int ycoord = ywrap(y);
	OceanNode cur = OceanList[xcoord][ycoord];
	return cur.tag();
    }

    /**
     *  timeStep() performs a simulation timestep as described in README.
     *  @return an ocean representing the elapse of one timestep.
     */

    public Ocean timeStep() {
	Ocean nextOcean = new Ocean(OceanWidth, OceanHeight, starveTime);
	for(int c = 0; c < OceanWidth; c++) {
	    for (int r = 0; r < OceanHeight; r++) {
	        int[] around = surroundings(c, r);
		int fishnum = around[0];
		int sharksnum = around[1];
		OceanNode cur = OceanList[c][r];
		OceanNode nxt = nextOcean.OceanList[c][r];  
		switch (cur.tag()) {
		 case EMPTY:
		    if ((fishnum >= 2) && (sharksnum <= 1)) {
			nxt.setFish();
		    } 
		    if ((fishnum >= 2) && (sharksnum >= 2)) {
			nxt.setShark();
		    }
		    break;
		 case FISH:
		    if (sharksnum == 0) {
			nxt.setFish();
			    }
		    if (sharksnum == 1) {
			nxt.setEmpty();
		    } 
		    if (sharksnum >= 2) {
			nxt.setShark();
		    }
		    break;
		 case SHARK:
		    if (fishnum > 0) {
			cur.feed();
			nxt = cur;
		    } else {
			cur.starve();
			nxt = cur;
			if (nxt.hunger() > starveTime) {
			    nxt.setEmpty();
			} 
		    }
		    break;
		 default: break;
		}
		nextOcean.OceanList[c][r] = nxt;
	    }
	}
	return nextOcean;
    }

    /**
     *  surroundings() returns an array containing the contents of a cells 
     *  surroundings.
     *  The index at zero returns the number of fish surrounding the cell.
     *  The index at one returns the number of sharks surrounding the cell.
     */
    
    public int[] surroundings(int x, int y) {
	int[] tally = new int[2];
	int xcoord = xwrap(x);
	int ycoord = ywrap(y);
	for (int c = xcoord - 1; c <= xcoord + 1; c++) {
	    for (int r = ycoord - 1; r <= ycoord + 1; r++) {
		switch (cellContents(xwrap(c), ywrap(r))) {
		 case FISH:
		    tally[0]++;
		    break;
		 case SHARK:
		    tally[1]++;
		    break;
		 default: break;
		}
	    }
	}	
	switch (cellContents(x, y)) {
	 case FISH:
	    tally[0]--;
	    break;
	 case SHARK:
	    tally[1]--;
	    break;
	 default: break;
	}
	return tally;
    }

    /**
     *  addShark() (with three parameters) places a shark in cell (x, y) if the
     *  cell is empty.  The shark's hunger is represented by the third parameter.
     *  If the cell is already occupied, leave the cell as it is.  You will need
     *  this method to help convert run-length encodings to Oceans.
     *  @param x is the x-coordinate of the cell to place a shark in.
     *  @param y is the y-coordinate of the cell to place a shark in.
     *  @param feeding is an integer that indicates the shark's hunger.  You may
     *         encode it any way you want; for instance, "feeding" may be the
     *         last timestep the shark was fed, or the amount of time that has
     *         passed since the shark was last fed, or the amount of time left
     *         before the shark will starve.  It's up to you, but be consistent.
     */

    public void addShark(int x, int y, int feeding) {
	int xcoord = xwrap(x);
	int ycoord = ywrap(y);
	addShark(xcoord, ycoord);
	OceanNode cur = OceanList[xcoord][ycoord];
	for (int j = 0; j < feeding; j++) {
	    cur.starve();
	}	
    }

    /**
     *  sharkFeeding() returns an integer that indicates the hunger of the shark
     *  in cell (x, y), using the same "feeding" representation as the parameter
     *  to addShark() described above.  If cell (x, y) does not contain a shark,
     *  then its return value is undefined--that is, anything you want.
     *  Normally, this method should not be called if cell (x, y) does not
     *  contain a shark.  You will need this method to help convert Oceans to
     *  run-length encodings.
     *  @param x is the x-coordinate of the cell whose contents are queried.
     *  @param y is the y-coordinate of the cell whose contents are queried.
     */

    public int sharkFeeding(int x, int y) {
	int xcoord = xwrap(x);
	int ycoord = ywrap(y);
	OceanNode cur = OceanList[xcoord][ycoord];
	return cur.hunger();
	}


}
