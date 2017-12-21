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
	private int currentHP;
	private List<Card> hand;

	// Explicit private default constructor that prevents empty Players from
	// being created

	@SuppressWarnings("unused")
	private Player() {
	}

	// Main constructor used for creating Player objects

	public Player(String name) {
		this.name = name;
		currentHP = PLAYER_MAX_HP;
		hand = new ArrayList<Card>();
	}

	// Returns the name of this Player

	public String getName() {
		return name;
	}

	// Returns the current HP of this Player

	public int getHP() {
		return currentHP;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	// Returns the hand of Cards currently held by this Player

	public List<Card> getHand() {
		return hand;
	}

	// Adds a given amount of HP to this Player's health

	public void addHP(int amountToAdd) {
		currentHP += amountToAdd;
	}

	// Subtracts a given amount of HP from this Player's health

	public void subtractHP(int amountToSubtract) {
		currentHP -= amountToSubtract;
	}

	// Resets this Player, clearing the Player's hand and restoring it to max HP

	public void reset() {
		hand.clear();
		currentHP = PLAYER_MAX_HP;
	}

	// Returns a string that displays this Player's name as well as HP

	public String toString() {
		return getName() + ": " + currentHP + "/" + PLAYER_MAX_HP + " HP";
	}

}
