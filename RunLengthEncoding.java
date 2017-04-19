/* RunLengthEncoding.java */

/**
 *  The RunLengthEncoding class defines an object that run-length encodes an
 *  Ocean object. The first constructor creates a run-length encoding of an 
 *  Ocean in which every cell is empty.  The second constructor creates a run-length 
 *  encoding for which the runs are provided as parameters.  The third constructor
 *  converts an Ocean object into a run-length encoding of that object.
 */

public class RunLengthEncoding {

    private RLENode head = new RLENode(-1);
    private RLENode tail = new RLENode(-2);
    private RLENode RunPointer = head;
    private int size;
    private int width;
    private int height;
    private int starveTime;
    private int RLELength;

    /**
     *  RunLengthEncoding() (with three parameters) is a constructor that creates
     *  a run-length encoding of an empty ocean having width i and height j,
     *  in which sharks starve after starveTime timesteps.
     *  @param i is the width of the ocean.
     *  @param j is the height of the ocean.
     *  @param starveTime is the number of timesteps sharks survive without food.
     */

    public RunLengthEncoding(int i, int j, int starveTime) {
	this(i, j, starveTime, new int[] {Ocean.EMPTY}, new int[] {i*j});
    }

    /**
     *  RunLengthEncoding() (with five parameters) is a constructor that creates
     *  a run-length encoding of an ocean having width i and height j, in which
     *  sharks starve after starveTime timesteps.  The runs of the run-length
     *  encoding are taken from two input arrays.  Run i has length runLengths[i]
     *  and species runTypes[i].
     *  @param i is the width of the ocean.
     *  @param j is the height of the ocean.
     *  @param starveTime is the number of timesteps sharks survive without food.
     *  @param runTypes is an array that represents the species represented by
     *         each run.  Each element of runTypes is Ocean.EMPTY, Ocean.FISH,
     *         or Ocean.SHARK.  Any run of sharks is treated as a run of newborn
     *         sharks (which are equivalent to sharks that have just eaten).
     *  @param runLengths is an array that represents the length of each run.
     *         The sum of all elements of the runLengths array should be i * j.
     */

    public RunLengthEncoding(int i, int j, int starveTime,
			     int[] runTypes, int[] runLengths) {
      	if ((i <= 0) || (j <= 0)) {
	    System.out.println("Error: invalid dimensions");
	    System.exit(0);
	} if (starveTime <= 0) {
	    System.out.println("Error: invalid starve time");
	    System.exit(0);
	} if (runTypes.length != runLengths.length) {
	    System.out.println("Error: invalid match between encoding types to run-lengths");
	    System.exit(0);
	}
	int lengthsum = 0;
	for(int k = 0; k < runLengths.length; k++) {
	    lengthsum = lengthsum + runLengths[k];
	}
	if (lengthsum != (i * j)) {
	    System.out.println("Error: invalid match between dimensions to run-lengths");
	    System.exit(0);
	}

	size = i * j;
	width = i;
	height = j;
	this.starveTime = starveTime;
	RLENode marker = head;
	int looklength = runTypes.length;
	for (int n = 0; n < looklength; n++) {
	    RLENode v = new RLENode();
	    switch (runTypes[n]) {	
	    case Ocean.EMPTY:
		v.setEmpty(runLengths[n]);
		break;
	    case Ocean.FISH:
		v.setFish(runLengths[n]);
		break;
	    case Ocean.SHARK:
		v.setShark(runLengths[n], 0);
		break;
	    default: break;
	    }
	    v.prev = marker;
	    marker.next = v;
	    marker = v;
	    RLELength++;
	}
	marker.next = tail;
	tail.prev = marker;
	squish();
    }
    
    /**
     *  squish() compresses a RLE so that there are no consecutive runs.
     */

    public void squish() {
    	RLENode marker = head.next;
	RLENode holder = new RLENode();
	while (!(marker.isTail())) {
	    if ((marker.tag() == marker.next.tag())
		&& (marker.hunger() == marker.next.hunger())) {
		holder = marker.next;
		marker.setIters(marker.iters() + holder.iters());
		marker.next = holder.next;
		holder.next.prev = marker;
		holder.next = null;
		holder.prev = null;
		RLELength--;
	    } else {
		marker = marker.next;
	    }
	}
    }

    /**
     *  restartRuns() and nextRun() are two methods that work together to return
     *  all the runs in the run-length encoding, one by one.  Each time
     *  nextRun() is invoked, it returns a different run (represented as an
     *  array of two ints), until every run has been returned.  The first time
     *  nextRun() is invoked, it returns the first run in the encoding, which
     *  contains cell (0, 0).  After every run has been returned, nextRun()
     *  returns null, which lets the calling program know that there are no more
     *  runs in the encoding.
     *
     *  The restartRuns() method resets the enumeration, so that nextRun() will
     *  once again enumerate all the runs as if nextRun() were being invoked for
     *  the first time.
     *
     *  (Note:  Don't worry about what might happen if nextRun() is interleaved
     *  with addFish() or addShark(); it won't happen.)
     */

    /**
     *  restartRuns() resets the enumeration as described above, so that
     *  nextRun() will enumerate all the runs from the beginning.
     */

    public void restartRuns() {
	RunPointer = head;
    }

    /**
     *  nextRun() returns the next run in the enumeration, as described above.
     *  If the runs have been exhausted, it returns null.  The return value is
     *  an array of two ints (constructed here), representing the type and the
     *  size of the run, in that order.
     *  @return the next run in the enumeration, represented by an array of
     *          two ints.  The int at index zero indicates the run type
     *          (Ocean.EMPTY, Ocean.SHARK, or Ocean.FISH).  The int at index one
     *          indicates the run length (which must be at least 1).
     */

    public int[] nextRun() {
	int[] output = new int[2];
	RunPointer = RunPointer.next;
	if (RunPointer.isTail()) {
	    return null;
	} else {
	    output[0] = RunPointer.tag();
	    output[1] = RunPointer.iters();
	}	  
	return output;
    }

    /**
     *  toOcean() converts a run-length encoding of an ocean into an Ocean
     *  object.  You will need to implement the three-parameter addShark method
     *  in the Ocean class for this method's use.
     *  @return the Ocean represented by a run-length encoding.
     */

    public Ocean toOcean() {
	Ocean RLEOcean = new Ocean(width, height, starveTime);
	OceanNode cur;
	RLENode marker = head.next;
	int log = marker.iters();
	for (int r = 0; r < height; r++) {
	    for (int c = 0; c < width; c++) {
		if (!(marker.isTail())) {
		    switch (marker.tag()) {
		    case Ocean.FISH:
			RLEOcean.addFish(c,r);
			break;
		    case Ocean.SHARK:
			RLEOcean.addShark(c, r, marker.hunger());
			break;
		    default: break;
		    }
		    log--;
		    if (log == 0) {
			marker = marker.next;
			log = marker.iters();
		    }
		}
	    }
	}
	return RLEOcean;
    }

    /**
     *  The following method is required for Part III.
     */

    /**
     *  RunLengthEncoding() (with one parameter) is a constructor that creates
     *  a run-length encoding of an input Ocean.  You will need to implement
     *  the sharkFeeding method in the Ocean class for this constructor's use.
     *  @param sea is the ocean to encode.
     */

    public RunLengthEncoding(Ocean sea) {
	starveTime = sea.starveTime();
	width = sea.width();
	height = sea.height();
	size = width * height;
	RLENode marker = head;
	for (int r = 0; r < height; r++) {
	    for (int c = 0; c < width; c++) {
		RLENode v = new RLENode();
		switch (sea.cellContents(c, r)) {
		case Ocean.EMPTY:
		    v.setEmpty(1);
		    break;
		case Ocean.FISH:
		    v.setFish(1);
		    break;
		case Ocean.SHARK:
		    v.setShark(1, sea.sharkFeeding(c, r));
		    break;
		default: break;
		}
		v.prev = marker;
		marker.next = v;
		marker = v;
		RLELength++;
	    }
	}
	marker.next = tail;
	tail.prev = marker;
	squish();
	check();
    }

    /**
     *  The following methods are required for Part IV.
     */

    /**
     *  addFish() places a fish in cell (x, y) if the cell is empty.  If the
     *  cell is already occupied, leave the cell as it is.  The final run-length
     *  encoding should be compressed as much as possible; there should not be
     *  two consecutive runs of sharks with the same degree of hunger.
     *  @param x is the x-coordinate of the cell to place a fish in.
     *  @param y is the y-coordinate of the cell to place a fish in.
     */

    public void addFish(int x, int y) {
	int xcoor = Ocean.wrap(x, width);
	int ycoor = Ocean.wrap(y, height);
	int loc = xcoor + (ycoor * height) + 1;
	int front = 0;
	RLENode cur = head.next;
	RLENode var = new RLENode(Ocean.FISH);	
	while (!((loc > front) && (loc <= front + cur.iters()))) {
	    front = front + cur.iters();
	    cur = cur.next;
	}
	front = front + cur.iters();
	if (cur.tag() == Ocean.EMPTY) {
	    if (cur.iters() == 1) {
		cur.setFish(1);
	    } else if (loc == front) {
		cur.setIters(cur.iters() - 1);
		var.next = cur.next;
		cur.next.prev = var;
		var.prev = cur;
		cur.next = var;
		RLELength++;
	    } else if (loc == front - cur.iters() + 1) {
		cur.setIters(cur.iters() - 1);
		var.next = cur;
		var.prev = cur.prev;
		cur.prev.next = var;
		cur.prev = var;
		RLELength++;		    
	    } else if (loc < front) {
		RLENode left = new RLENode(Ocean.EMPTY);
		RLENode right = new RLENode(Ocean.EMPTY);
		left.setIters(loc - (front - cur.iters()) - 1);	
		right.setIters(front - loc);
		cur.prev.next = left;
		left.prev = cur.prev;
		left.next = var;
		var.prev = left;
		var.next = right;
		right.prev = var;
		right.next = cur.next;
		cur.next.prev = right;
		cur.next = null;
		cur.prev = null;
		RLELength = RLELength + 2;
	    }
	}
        squish();
	check();
    }

    /**
     *  addShark() (with two parameters) places a newborn shark in cell (x, y) if
     *  the cell is empty.  A "newborn" shark is equivalent to a shark that has
     *  just eaten.  If the cell is already occupied, leave the cell as it is.
     *  The final run-length encoding should be compressed as much as possible;
     *  there should not be two consecutive runs of sharks with the same degree
     *  of hunger.
     *  @param x is the x-coordinate of the cell to place a shark in.
     *  @param y is the y-coordinate of the cell to place a shark in.
     */

    public void addShark(int x, int y) {
	int xcoor = Ocean.wrap(x, width);
	int ycoor = Ocean.wrap(y, height);
	int loc = xcoor + (ycoor * height) + 1;
	int front = 0;
	RLENode cur = head.next;
	RLENode var = new RLENode(Ocean.SHARK);	
	while (!((loc > front) && (loc <= front + cur.iters()))) {
	    front = front + cur.iters();
	    cur = cur.next;
	}
	front = front + cur.iters();
	if (cur.tag() == Ocean.EMPTY) {
	    if (cur.iters() == 1) {
		cur.setShark(1, 0);
	    } else if (loc == front) {
		cur.setIters(cur.iters() - 1);
		var.next = cur.next;
		cur.next.prev = var;
		var.prev = cur;
		cur.next = var;
		RLELength++;
	    } else if (loc == front - cur.iters() + 1) {
		cur.setIters(cur.iters() - 1);
		var.next = cur;
		var.prev = cur.prev;
		cur.prev.next = var;
		cur.prev = var;
		RLELength++;		    
	    } else if (loc < front) {
		RLENode left = new RLENode(Ocean.EMPTY);
		RLENode right = new RLENode(Ocean.EMPTY);
		left.setIters(loc - (front - cur.iters()) - 1);	
		right.setIters(front - loc);
		cur.prev.next = left;
		left.prev = cur.prev;
		left.next = var;
		var.prev = left;
		var.next = right;
		right.prev = var;
		right.next = cur.next;
		cur.next.prev = right;
		cur.next = null;
		cur.prev = null;
		RLELength = RLELength + 2;
	    }
	}
        squish();
	check();
    }

    /**
     *  check() walks through the run-length encoding and prints an error message
     *  if two consecutive runs have the same contents, or if the sum of all run
     *  lengths does not equal the number of cells in the ocean.
     */

    private void check() {
	RLENode marker = head.next;
	while (!(marker.isTail())) {
	    if ((marker.tag() == Ocean.SHARK) &&
		(marker.next.tag() == Ocean.SHARK) &&
		(marker.hunger() == marker.next.hunger())) {
		System.out.println("Error: consecutive run of sharks present");
		System.exit(0);
	    } else if ((marker.tag() != Ocean.SHARK) &&
		       ((marker.tag() == marker.next.tag()))) {
		System.out.println("Error: consecutive runs present");
		System.exit(0);
	    } else {
		marker = marker.next;
	    }
	}	
	marker = head.next;
	int lengthsums = 0;
	while (!(marker.isTail())) {
	    lengthsums = lengthsums + marker.iters();
	    marker = marker.next;
	}
	if (lengthsums != size) {
	    System.out.println("Error:invalid match between dimensions to run-lengths");
	    System.exit(0);
	}
    }


}
