/*
 * This class represents a monster card used in the game. Each card has a name of the monster (name), a monster type, 
 * its monster level, its health (hP), and it's upper, lower, left, and right attack points (AP).
 * 
 * Author: Eric Wahlquist
 */

public class Card {

	// Monster's information

	private String name;
	private String type;
	private int level;
	private Player owner;

	// Monster's stats

	private int maxHP;
	private int initialUpperAP;
	private int initialLowerAP;
	private int initialLeftAP;
	private int initialRightAP;

	private int currentHP;
	private int currentUpperAP;
	private int currentLowerAP;
	private int currentLeftAP;
	private int currentRightAP;

	// Keeps track of number of turns left of any contamination on this monster

	int contaminatedTurnsLeft;

	// Explicit private default constructor that prevents invalid Cards from
	// being created

	@SuppressWarnings("unused")
	private Card() {
	}

	// Main constructor used for creating Card objects
	public Card(String name, String type, int level, int hP, int upperAP, int lowerAP, int leftAP, int rightAP,
			Player owner) {
		setName(name);
		setType(type);
		setLevel(level);
		setOwner(owner);
		setMaxHP(hP);
		setInitialAP(upperAP, lowerAP, leftAP, rightAP);
		setCurrentHP(maxHP);
		setCurrentAP(upperAP, lowerAP, leftAP, rightAP);
		setContaminatedTurnsLeft(0);
	}

	// Returns a String of the name of the monster represented by this Card
	public String getName() {
		return name;
	}

	// Returns a String of the type of the monster represented by this Card
	public String getType() {
		return type;
	}

	// Returns the level of the monster represented by this Card as an integer
	public int getLevel() {
		return level;
	}

	// Returns the Player that owns this Card
	public Player getOwner() {
		return owner;
	}

	// Returns the maximum HP (health points) of the monster represented by this
	// Card as an integer
	public int getMaxHP() {
		return maxHP;
	}

	// Returns the initial upper AP (attack points) of the monster represented
	// by this Card as an integer
	public int getInitialUpperAP() {
		return initialUpperAP;
	}

	// Returns the initial lower AP (attack points) of the monster represented
	// by this Card as an integer
	public int getInitialLowerAP() {
		return initialLowerAP;
	}

	// Returns the initial left AP (attack points) of the monster represented by
	// this Card as an integer
	public int getInitialLeftAP() {
		return initialLeftAP;
	}

	// Returns the initial right AP (attack points) of the monster represented
	// by this Card as an integer
	public int getInitialRightAP() {
		return initialRightAP;
	}

	// Returns the current HP of the monster represented by this Card as an
	// integer
	public int getCurrentHP() {
		return currentHP;
	}

	// Returns the current upper AP of the monster represented by this Card as
	// an integer
	public int getCurrentUpperAP() {
		return currentUpperAP;
	}

	// Returns the current lower AP of the monster represented by this Card as
	// an integer
	public int getCurrentLowerAP() {
		return currentLowerAP;
	}

	// Returns the current left AP of the monster represented by this Card as an
	// integer
	public int getCurrentLeftAP() {
		return currentLeftAP;
	}

	// Returns the current right AP of the monster represented by this Card as
	// an integer
	public int getCurrentRightAP() {
		return currentRightAP;
	}

	// Returns the number of turns left on this Card's contamination status as
	// an integer
	public int getContaminatedTurnsLeft() {
		return contaminatedTurnsLeft;
	}

	// Sets the name of the monster represented by this Card
	public void setName(String name) {
		this.name = name;
	}

	// Sets the type of the monster represented by this Card
	public void setType(String type) {
		this.type = type;
	}

	// Sets the level of the monster represented by this Card
	public void setLevel(int level) {
		this.level = level;
	}

	// Sets the current owner of this Card
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	// Sets the maximum HP (health points) of the monster represented by this
	// Card
	public void setMaxHP(int hP) {
		this.maxHP = hP;
	}

	// Sets the AP (attack points) of the monster represented by this Card
	public void setInitialAP(int initialUpperAP, int initialLowerAP, int initialLeftAP, int initialRightAP) {
		this.initialUpperAP = initialUpperAP;
		this.initialLowerAP = initialLowerAP;
		this.initialLeftAP = initialLeftAP;
		this.initialRightAP = initialRightAP;
	}

	// Sets the current HP (health points) of the monster represented by this
	// card
	public void setCurrentHP(int currentHP) {
		this.currentHP = currentHP;
	}

	// Sets the current AP (attack points) of the monster represented by this
	// Card
	public void setCurrentAP(int currentUpperAP, int currentLowerAP, int currentLeftAP, int currentRightAP) {
		this.currentUpperAP = currentUpperAP;
		this.currentLowerAP = currentLowerAP;
		this.currentLeftAP = currentLeftAP;
		this.currentRightAP = currentRightAP;
	}

	// Sets this Card to be contaminated (was hit by a toxic monster) until a
	// given turn number
	public void setContaminatedTurnsLeft(int numberOfTurns) {
		contaminatedTurnsLeft = numberOfTurns;
	}
	
	//Adds the given amount of HP to this Card's current HP
	
	public void addHP(int amountToAdd) {
		currentHP += amountToAdd;
	}
	
	//Subtracts the given amount of HP from this Card's current HP
	
	public void subtractHP(int amountToSubtract) {
		currentHP -= amountToSubtract;
	}

	// Resets all current stats to their initial numbers and clears the owner
	public void reset() {
		setCurrentHP(maxHP);
		setCurrentAP(initialUpperAP, initialLowerAP, initialLeftAP, initialRightAP);
		setOwner(null);
		setContaminatedTurnsLeft(0);
	}

	// Returns a string that displays the information of the monster represented
	// by this Card
	public String toString() {
		String s = name + ":\n\n";
		s += "Type: " + type + "\n";
		s += "Monster level: " + level + "\n";
		s += "Maximum HP: " + maxHP + "\n";
		s += "Upper AP: " + currentUpperAP + "\n";
		s += "Lower AP: " + currentLowerAP + "\n";
		s += "Left AP: " + currentLeftAP + "\n";
		s += "Right AP: " + currentRightAP + "\n";
		return s;
	}
}
