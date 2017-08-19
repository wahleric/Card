import java.util.*;
/*
 * This class represents a Player object in the game. It keeps track of a single contestant, including the name of the
 * contestant, their current hand, and their HP.
 * 
 * Author: Eric Wahlquist
 */
public class Player {

	private String name;
	private List<Card> hand;
	private int hP;
	
	
	//Explicit private default constructor that prevents empty Players from being created
	private Player() {
	}
	
	//Main constructor used for creating Player objects
	public Player(String name) {
		this.name = name;
		hand = new ArrayList<Card>();
		hP = 200;
	}
	
	//Returns the name of this Player
	public String getName() {
		return name;
	}
	
	//Returns the hand of Cards currently held by this Player
	public List<Card> getHand() {
		return hand;
	}

	//Returns the HP of this Player
	public int getHP() {
		return hP;
	}
	
	//Sets the name of this Player
	public void setName(String name) {
		this.name = name;
	}
	
	//Sets the HP of this Player
	public void setHP(int hP) {
		this.hP = hP;
	}
	
	//Returns a string that displays this Player's name as well as win count
	public String toString() {
		return name;
	}
	
}
