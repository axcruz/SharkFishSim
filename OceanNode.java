/* OceanNode.java */

/**
 *  OceanNode is a class used internally by the Ocean class.
 *  Each OceanNode stores two variables: the tag of the 
 *  animal(either EMPTY,FISH, or SHARK) and the hunger of a 
 *  shark.
 */

public class OceanNode {
    
    private int tag;
    private int hunger;

    public OceanNode() {
	tag = Ocean.EMPTY;
	hunger = -10;
    }

    public void setEmpty() {
	tag = Ocean.EMPTY;
	hunger = -10;
    }

    public void setFish() {
	tag = Ocean.FISH;
	hunger = -10;
    }
    
    public void setShark() {
	tag = Ocean.SHARK;
	hunger = 0;
    }

    public void starve() {
	if (tag == Ocean.SHARK) {
	    hunger++;
	} else {
	    System.out.println("Error: not shark, cannot starve");
	    System.exit(0);
	}
    }
    
    public void feed() {
	if (tag == Ocean.SHARK) {
	    hunger = 0;
	} else {
	    System.out.println("Error: not shark, cannot feed");
	    System.exit(0);
	}
    }

    public int tag() {
	return tag;
    }

    public int hunger() {
	if (hunger == -10) {
	    System.out.println("Error: hunger value not found");
	    System.exit(0);
	}
	return hunger;
    }
 
   
}	    

	    
	
