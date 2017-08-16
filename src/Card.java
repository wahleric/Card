/*
 * This class represents a monster card used in the game. Each card has a name of the monster (name), a description
 * of the monster (description), its health (hP), it's upper, lower, left, and right attack points (AP), and an owner 
 * (player).
 * 
 * Author: Eric Wahlquist
 */
public class Card {

	private String name;
	private String description;
	private int maxHP;
	private int upperAP;
	private int lowerAP;
	private int leftAP;
	private int rightAP;
	
	//Explicit private default constructor that prevents invalid Cards from being created
	private Card() {
	}
	
	//Main constructor used for creating Card objects
	public Card(String name, String description, int hP, int upperAP, int lowerAP, int leftAP, int rightAP) {
		this.name = name;
		this.description = description;
		this.maxHP = hP;
		this.upperAP = upperAP;
		this.lowerAP = lowerAP;
		this.leftAP = leftAP;
		this.rightAP = rightAP;
	}
	
	//Returns the name of the monster represented by this Card
	public String getName() {
		return name;
	}
	
	//Returns the description of the monster represented by this Card
	public String getDescription() {
		return description;
	}
	
	//Returns the maximum HP (health points) of the monster represented by this Card
	public int getMaxHP() {
		return maxHP;
	}
	
	//Returns the upper AP (attack points) of the monster represented by this Card
	public int getUpperAP() {
		return upperAP;
	}
	
	//Returns the lower AP (attack points) of the monster represented by this Card
	public int getLowerAP() {
		return lowerAP;
	}
	
	//Returns the left AP (attack points) of the monster represented by this Card
	public int getLeftAP() {
		return leftAP;
	}
	
	//Returns the right AP (attack points) of the monster represented by this Card
	public int getRightAP() {
		return rightAP;
	}
	
	//Sets the name of the monster represented by this Card
	public void setName(String name) {
		this.name = name;
	}
	
	//Sets the description of the monster represented by this Card
	public void setDescription(String description) {
		this.description = description;
	}
	
	//Sets the maximum HP (health points) of the monster represented by this Card
	public void setMaxHP(int hP) {
		this.maxHP = hP;
	}
	
	//Sets the AP (attack points) of the monster represented by this Card
	public void setAP(int upperAP, int lowerAP, int leftAP, int rightAP) {
		this.upperAP = upperAP;
		this.lowerAP = lowerAP;
		this.leftAP = leftAP;
		this.rightAP = rightAP;
	}
	
	//Returns a string that displays the name and stats of the monster represented by this Card
	public String toString() {
		String s = name + ":\n\n";
		s += description + "\n\n";
		s += "Maximum HP: " + maxHP + "\n";
		s += "Upper AP: " + upperAP + "\n";
		s += "Lower AP: " + lowerAP + "\n";
		s += "Left AP: " + leftAP + "\n";
		s += "Right AP: " + rightAP + "\n";
		return s;
	}
}
