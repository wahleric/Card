import java.util.*;

/*
 * This class represents a Player in Card Battle. It keeps track of a single contestant, including the name of the
 * contestant, their current hand, and their HP.
 * 
 * Author: Eric Wahlquist
 */

public class Player {
	
	public static final int PLAYER_MAX_HP = 500;
	
	private String name;
	private List<Card> hand;
	private int maxHP;
	private int currentHP;
	
	
	//Explicit private default constructor that prevents empty Players from being created
	
	@SuppressWarnings("unused")
	private Player() {
	}
	
	//Main constructor used for creating Player objects
	
	public Player(String name) {
		setName(name);
		hand = new ArrayList<Card>();
		maxHP = PLAYER_MAX_HP;
		currentHP = PLAYER_MAX_HP;
	}
	
	//Returns the name of this Player
	
	public String getName() {
		return name;
	}
	
	//Returns the hand of Cards currently held by this Player
	
	public List<Card> getHand() {
		return hand;
	}

	//Returns the max HP of this Player
	
	public int getMaxHP() {
		return maxHP;
	}
	
	//Returns the current HP of this Player
	
	public int getCurrentHP() {
		return currentHP;
	}
	
	//Sets the name of this Player
	
	public void setName(String name) {
		this.name = name;
	}
	
	//Adds a given amount of HP to this Player's health
	
	public void addHP(int amountToAdd) {
		currentHP += amountToAdd;
	}
	
	//Subtracts a given amount of HP from this Player's health
	
	public void subtractHP(int amountToSubtract) {
		currentHP -= amountToSubtract;
	}
	
	//Resets this Player, clearing the Player's hand and restoring it to max HP
	
	public void reset() {
		hand.clear();
		currentHP = maxHP;
	}
	
	//Returns a string that displays this Player's name as well as HP
	
	public String toString() {
		return getName() + ": " + currentHP + "/" + maxHP + " HP";
	}
	
}
