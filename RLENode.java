/* RLENode.java */

/**
 *  RLENode is a class used internally by the RunLengthEncoding class.
 *  The RLENodes are meant to be used in a doubly linked list with
 *  one head and tail sentinel node marking the start and end of the
 *  list respectively.
 */

public class RLENode {

    public RLENode prev, next;
    private boolean head;
    private boolean tail;
    private int tag;
    private int iters;
    private int hunger;

    public RLENode(){
	prev = null;
	next = null;
	tag = Ocean.EMPTY;
	iters = 1;
	hunger = -100;
	head = false;
	tail = false;
    }

    public RLENode(int n) {
	switch (n) {
	case Ocean.EMPTY:
	    prev = null;
	    next = null;
	    tag = Ocean.EMPTY;
	    iters = 1;
	    hunger = -100;
	    head = false;
	    tail = false;
	    break;
	case Ocean.FISH:
	    prev = null;
	    next = null;
	    tag = Ocean.FISH;
	    iters = 1;
	    hunger = -100;
	    head = false;
	    tail = false;
	    break;
	case Ocean.SHARK:
	    prev = null;
	    next = null;
	    tag = Ocean.SHARK;
	    iters = 1;
	    hunger = 0;
	    head = false;
	    tail = false;
	    break;
	case -1:
	    prev = null;
	    next = null;
	    tag = -1;
	    iters = -111;
	    head = true;
	    tail = false;
	    break;
	case -2:
	    prev = null;
	    next = null;
	    tag = -2;
	    iters= -110;
	    head = false;
	    tail = true;
	    break;
	default:
	    System.out.println("Error: invalid input option");
	    System.out.println("Valid Options: ");
	    System.out.println("-1 for head RLENode, ");
	    System.out.println("-2 for tail RLENode, ");
	    System.out.println("any corresponding Ocean class constant");
	    System.exit(0);
	}
    }

    public boolean isHead() {
	return head;
    }

    public boolean isTail() {
	return tail;
    }

    public void setEmpty(int runsize) {
	tag = Ocean.EMPTY;
	iters = runsize;
	hunger = -100;
    }

    public void setFish(int runsize) {
	tag = Ocean.FISH;
	iters = runsize;
	hunger = -100;
    }
    
    public void setShark(int runsize, int hunger) {
	tag = Ocean.SHARK;
	iters = runsize;
	this.hunger = hunger;
    }

    public void setIters(int n) {
	iters = n;
    }
    
    public void setHunger(int n) {
	hunger = n;
    }
    
    public int tag() {
	return tag;
    }

    public int iters(){
	return iters;
    }

    public int hunger() {
	return hunger;
    }


}
