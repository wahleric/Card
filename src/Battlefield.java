import java.util.*;

/*
 * This class represents a board on which the game is played. The Battlefield consists of Card Nodes arranged in 
 * a 5x5 square. The board begins empty, and keeps track of the game status
 * 
 */
public class Battlefield {

	private Player player1;
	private Player player2;
	private Node[] map;
	
	//Explicit private default constructor that prevents a null battlefield from being created
	private Battlefield() {
	}
	
	//Main constructor used for creating Battlefield objects
	public Battlefield(Player player1, Player player2) {
		this.player1 = player1;
		this.player2 = player2;
		map = new Node[25];
		for (int i = 0; i < 25; i++) {
			map[i].setNodeNumber(i + 1);
		}
	}
	
	//Returns the node located at the given position on the board. The Node numbers increase from left to right, top
	//to bottom. Node 1 is located at the upper left, and Node 25 is located at the lower right.
	public Node getNode(int nodeNumber) {
		nodeNumber--;
		if (nodeNumber < 1 || nodeNumber > 25) {
			throw new IllegalArgumentException("Invalid node reference");
		}
		
		else return map[nodeNumber - 1];
	}
	
	//Places a card in a Node on the Battlefield
	public void placeCard(Player player, Card card, int nodeNumber) {
		map[nodeNumber - 1].setCurrentCard(card);
		map[nodeNumber - 1].setOwner(player);
	}
	
	/*
	 * The Node class represents a single space on the battlefield on which a card may be placed. It keeps track of the Node's
	 * location, the Card that is placed in it, the owner of the Card, and the current status of the Card's attributes
	 *
	 * Author: Eric Wahlquist
	 */
	private class Node {
		
		private int nodeNumber;
		private Card currentCard;
		private int currentHP;
		private int currentUpperAP;
		private int currentLowerAP;
		private int currentLeftAP;
		private int currentRightAP;
		private Player owner;
		
		//Explicit private default constructor prevents creation of invalid Node
		private Node() {
		}
		
		//Constructor takes a node number and creates a blank node with that number
		public Node(int nodeNumber) {
			this.nodeNumber = nodeNumber;
			currentCard = null;
			currentHP = 0;
			currentUpperAP = 0;
			currentLowerAP = 0;
			currentLeftAP = 0;
			currentRightAP = 0;
			owner = null;
		}
		
		//Returns the number of this Node
		public int getNodeNumber() {
			return nodeNumber;
		}
		
		//Returns the current Card placed in this Node
		public Card getCurrentCard() {
			return currentCard;
		}
		
		//Returns the current HP of the monster represented by the Card in this Node
		public int getCurrentHP() {
			return currentHP;
		}
		
		//Returns the current upper AP of the monster represented by the Card in this Node
		public int getCurrentUpperAP() {
			return currentUpperAP;
		}
		
		//Returns the current lower AP of the monster represented by the Card in this Node
		public int getCurrentLowerAP() {
			return currentLowerAP;
		}
		
		//Returns the current left AP of the monster represented by the Card in this Node
		public int getCurrerntLeftAP() {
			return currentLeftAP;
		}
		
		//Returns the current right AP of the monster represented by the Card in this Node
		public int getCurrentRightAP() {
			return currentRightAP;
		}
		
		//Returns the owner of the Card in this Node
		public Player getCurrentOwner() {
			return owner;
		}
		
		//Sets the number of this Node on the Battlefield
		public void setNodeNumber(int nodeNumber) {
			this.nodeNumber = nodeNumber;
		}
		
		//Sets the current Card placed in this Node
		public void setCurrentCard(Card currentCard) {
			this.currentCard = currentCard;
			this.currentHP = currentCard.getMaxHP();
			this.currentUpperAP = currentCard.getUpperAP();
			this.currentLowerAP = currentCard.getLowerAP();
			this.currentLeftAP = currentCard.getLeftAP();
			this.currentRightAP = currentCard.getRightAP();
		}
		
		//Sets the current HP of the monster represented by the Card placed in this Node
		public void setCurrentHP(int currentHP) {
			checkNode();
			this.currentHP = currentHP;
		}
		
		//Sets the current AP for all four sides of the monster represented by the Card
		public void setCurrentAP(int currentUpperAP, int currentLowerAP, int currentLeftAP, int currentRightAP) {
			checkNode();
			this.currentUpperAP = currentUpperAP;
			this.currentLowerAP = currentLowerAP;
			this.currentLeftAP = currentLeftAP;
			this.currentRightAP = currentRightAP;
		}
		
		public void setOwner(Player owner) {
			this.owner = owner;
		}
		
		//Returns true if this Node is empty (no Card is placed), false otherwise
		public boolean isEmpty() {
			if (currentCard == null) {
				return true;
			}
			return false;
		}
		
		//Private helper method checks that a Card is in this Node before performing operations
		private void checkNode() {
			if (this.isEmpty()) {
				throw new IllegalArgumentException("Invalid Card reference");
			}
		}
	}
}
