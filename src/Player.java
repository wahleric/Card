/*
 * This class represents a Player object in the game. It keeps track of a single contestant, including the name of the
 * contestant and how many games they have won.
 * 
 * Author: Eric Wahlquist
 */
public class Player {

	private String name;
	private int wins;
	
	//Explicit private default constructor that prevents empty Players from being created
	private Player() {
	}
	
	//Main constructor used for creating Player objects
	public Player(String name, int wins) {
		this.name = name;
		this.wins = 0;
	}
	
	//Returns the name of this Player
	public String getName() {
		return name;
	}
	
	//Returns the win count of this Player
	public int getWins() {
		return wins;
	}
	
	//Sets the name of this Player
	public void setName(String name) {
		this.name = name;
	}
	
	//Increments the win count of this Player by 1
	public void addWin() {
		wins++;
	}
	
	//Returns a string that displays this Player's name as well as win count
	public String toString() {
		return name + ": " + wins + " wins";
	}
	
}
