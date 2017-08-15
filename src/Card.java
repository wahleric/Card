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
	private int hP;
	private int upperAP;
	private int lowerAP;
	private int leftAP;
	private int rightAP;
	private Player owner;
	
	public Card() {
		this("", "", 0, 0, 0, 0, 0, null);
	}
	
	public Card(String name, String description, int hP, int upperAP, int lowerAP, int leftAP, int rightAP, Player owner) {
		this.name = name;
		this.description = description;
		this.hP = hP;
		this.upperAP = upperAP;
		this.lowerAP = lowerAP;
		this.leftAP = leftAP;
		this.rightAP = rightAP;
		this.owner = owner;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public int getHP() {
		return hP;
	}
	
	public int getUpperAP() {
		return upperAP;
	}
	
	public int getLowerAP() {
		return lowerAP;
	}
	
	public int getLeftAP() {
		return leftAP;
	}
	
	public int getRightAP() {
		return rightAP;
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setHP(int hP) {
		this.hP = hP;
	}
	
	public void setAP(int upperAP, int lowerAP, int leftAP, int rightAP) {
		this.upperAP = upperAP;
		this.lowerAP = lowerAP;
		this.leftAP = leftAP;
		this.rightAP = rightAP;
	}
	
	public void setOwner(Player owner) {
		this.owner = owner;
	}
	
	public String toString() {
		return name + ": " + hP;
	}
}
