import java.io.*;
import java.util.*;

/*
 * This class represents a board on which the game is played. The Battlefield consists of Card Nodes arranged in 
 * a 5x5 board, which begins empty. Battlefield keeps track of the game status including the placement of Cards as
 * well as the cards in each Player's hand.
 * 
 * Author: Eric Wahlquist
 */
public class Battlefield {
	
	private Player human;
	private Player computer;
	private int turn;
	private Node[] board;
	private Deck deck;
	private List<Card> discardPile;
	
	//Explicit private default constructor that prevents a null battlefield from being created
	private Battlefield() {
	}
	
	//Main constructor used for creating Battlefield objects
	public Battlefield(Player human, Player computer) throws IOException {
		this.human = human;
		this.computer = computer;
		turn = 1;
		deck = new Deck();
		discardPile = new ArrayList<Card>();
		//Construct the board
		board = new Node[25];
		for (int i = 0; i < 25; i++) {
			board[i] = new Node(i + 1);
		}
	}
	
	//Returns the human Player of this board
	public Player getHumanPlayer() {
		return human;
	}
	
	//Returns the computer Player of this board
	public Player getComputerPlayer() {
		return computer;
	}
	
	//Returns the current turn number of this board
	public int getTurn() {
		return turn;
	}
	
	//Returns the Card placed in a given Node on the board. Returns null if the Node is empty
	public Card getCardAtNode(int nodeNumber) {
		return board[nodeNumber - 1].getCard();
	}
	
	//Draws a card from the deck into a Player's hand
	public Card drawCard(Player player) {
		if (deckIsEmpty()) {
			throw new IllegalArgumentException("Invalid: Deck is empty");
		}
		Card card = deck.drawCard();
		card.setOwner(player);
		if (player != null) {
			player.getHand().add(card);
		}
		return card;
	}
	
	//Adds a card to the deck
	private void addCardToDeck(Card card) {
		deck.addCard(card);
	}
	
	//Returns true if the deck of all monster cards to draw from is empty; false otherwise
	public boolean deckIsEmpty() {
		return deck.isEmpty();
	}
	
	//Places a card in a Node on the Battlefield. Returns true if successful, false otherwise.
	public boolean placeCard(Player player, Card card, int nodeNumber) {
		if (getCardAtNode(nodeNumber) == null) {
			board[nodeNumber - 1].setCurrentCard(card);
			if (player.getHand().contains(card)) {
				player.getHand().remove(card);
			} else {
				throw new IllegalArgumentException("Invalid: Card is not in hand");
			}
			return true;
		}
		return false;
	}
	
	//Places the given card on the discard pile
	public void discard(Card card) {
		discardPile.add(card);
	}
	
	//Removes a Card from a Node on the Battlefield and returns it
	private Card removeCard(int nodeNumber) {
		Card card = getCardAtNode(nodeNumber);
		if (card == null) {
			throw new IllegalArgumentException("Invalid: Node is empty");
		}
		board[nodeNumber - 1].setCurrentCard(null);
		return card;
	}
	
	//Ends the turn and initiates all attacks
	public void endOfTurn() {
		for (int nodeNumber = 1; nodeNumber < 26; nodeNumber++) {
			//For each Node:
			//Get information about the Card at the Node
			Card card = getCardAtNode(nodeNumber);
			//If the Card exists, deal appropriate damage to adjacent Cards
			if (card != null) {
				Player owner = card.getOwner();
				//Deal attacks to any enemy Card below this Card and apply any contamination
				if (nodeNumber > 5) {
					Card cardAbove = getCardAtNode(nodeNumber - 5);
					if (!(cardAbove == null) && cardAbove.getOwner() != owner) {
						cardAbove.setCurrentHP(cardAbove.getCurrentHP() - card.getCurrentUpperAP());
						if (card.getType().equals("Toxic")) {
							cardAbove.setContaminatedTurnsLeft(5);
						}
					}
				}
			
				//Deal attacks to any enemy Card to the left of this Card and apply any contamination
				if ((nodeNumber - 1) % 5 != 0) {
					Card cardLeft = getCardAtNode(nodeNumber - 1);
					if (!(cardLeft == null) && cardLeft.getOwner() != owner) {
						cardLeft.setCurrentHP(cardLeft.getCurrentHP() - card.getCurrentLeftAP());
						if (card.getType().equals("Toxic")) {
							cardLeft.setContaminatedTurnsLeft(5);
						}
					}
				}
			
				//Deal attacks to any enemy Card to the right of this Card and apply any contamination
				if (nodeNumber % 5 != 0) {
					Card cardRight = getCardAtNode(nodeNumber + 1);
					if (!(cardRight == null) && cardRight.getOwner() != owner) {
						cardRight.setCurrentHP(cardRight.getCurrentHP() - card.getCurrentRightAP());
						if (card.getType().equals("Toxic")) {
							cardRight.setContaminatedTurnsLeft(5);
						}
					}
				}
			
				//Deal attacks to any enemy Card  below this Card
				if (nodeNumber < 21) {
					Card cardBelow = getCardAtNode(nodeNumber + 5);
					if (!(cardBelow == null) && cardBelow.getOwner() != owner) {
						cardBelow.setCurrentHP(cardBelow.getCurrentHP() - card.getCurrentLowerAP());
						if (card.getType().equals("Toxic")) {
							cardBelow.setContaminatedTurnsLeft(5);
						}
					}
				}
			}
		}
		
		//Deal any contamination damage across the board (3-7 HP randomly per turn)
		for (int nodeNumber = 1; nodeNumber < 26; nodeNumber++) {
			Card card = getCardAtNode(nodeNumber);
			Random r = new Random();
			int contaminationDamage = r.nextInt(5) + 3;
			if (card != null && card.getContaminatedTurnsLeft() > 0) {
				int turnsLeft = card.getContaminatedTurnsLeft();
				card.setCurrentHP(card.getCurrentHP() - contaminationDamage);
				card.setContaminatedTurnsLeft(--turnsLeft);
			}
		}
		
		//After all damage is dealt across the board, remove any dead monsters and apply the proper damage to their owners
		for (int nodeNumber = 1; nodeNumber < 26; nodeNumber++) {
			Card card = getCardAtNode(nodeNumber);
			if (card != null && card.getCurrentHP() <= 0) {
				card.getOwner().setHP(card.getOwner().getHP() - card.getMaxHP());
				card.resetCard();
				discardPile.add(removeCard(nodeNumber));
			}
		}
		//Increment the turn counter
		turn++;
	}
	
	//Brings the board back to a clean state
	public void resetBoard() {
		
		//Remove all Cards from the board and return them to the deck
		for (int nodeNumber = 1; nodeNumber < 26; nodeNumber++) {
			if (!(getCardAtNode(nodeNumber) == null)) {
				addCardToDeck(removeCard(nodeNumber));
			}
		}
		
		//Remove all Cards from Players' hands and discard pile and put them back in the deck
		for (Card card : human.getHand()) {
			card.setOwner(null);
			addCardToDeck(card);
		}
		for (Card card : computer.getHand()) {
			card.setOwner(null);
			addCardToDeck(card);
		}
		for (Card card : discardPile) {
			card.setOwner(null);
			addCardToDeck(card);
		}
		human.getHand().clear();
		computer.getHand().clear();
		discardPile.clear();
		
		//Shuffle the deck, reset Players' HP, and reset the turn counter
		deck.shuffle();;
		human.setHP(Player.PLAYER_MAX_HP);
		computer.setHP(Player.PLAYER_MAX_HP);
		turn = 1;
	}
	
	//Returns the Player that has won
	//Returns a new Player named "a Tie" if the game was a tie, and returns null if the game is not over.
	public Player getWinner() {
		if (human.getHP() > 0 && computer.getHP() <= 0) {
			return human;
		}
		if (human.getHP() <= 0 && computer.getHP() > 0) {
			return computer;
		}
		if (human.getHP() <= 0 && computer.getHP() <= 0) {
			return new Player("a Tie");
		}
		return null;
	}
	
	//Returns a String representation of the current status of the board
	public String toString() {
		String s = padString("Turn: " + turn, 131) + "\n";
		String humanHP = human.toString();
		String computerHP = computer.toString();
		s += humanHP;
		int spacing = 131 - (humanHP.length() + computerHP.length());
		for (int space = 0; space < spacing; space++) {
			s += " ";
		}
		s += computerHP;
		s += "\n\n" + border();
		//Do the following for each level of the board
		for (int level = 0; level < 5; level++) {
			s += firstLine(level);
			s += spacerLine();
			s += monsterNameLine(level);
			s += hPLine(level);
			s += middleLine(level);
			s += spacerLine();
			s += monsterOwnerLine(level);
			s += spacerLine();
			s += lastLine(level);
			s += border();
			
		}
		return s;	
	}
	
	//Private helper method for toString()
	private String border() {
		String border = "\n";
		for (int dashNumber = 0; dashNumber < 131; dashNumber++) {
			border = "-" + border;
		}
		return border;
	}
	
	//USE SPACERBOX IN SPACERLINE?
	
	//Private helper method for toString()
	private String spacerLine() {
		String spacer = "|\n";
		for (int boxNumber = 1; boxNumber < 6; boxNumber++) {
			for (int spaceNumber = 0; spaceNumber < 25; spaceNumber++) {
				spacer = " " + spacer;
			}
			spacer = "|" + spacer;
		}
		return spacer;
	}
	
	//Private helper method for toString()
	private String spacerBox() {
		String spacer = "";
		for (int spaceNumber = 0; spaceNumber < 25; spaceNumber++) {
			spacer += " ";
		}
		spacer += "|";
		return spacer;
	}
	
	//Private helper method for toString()
	private String firstLine(int level) {
		String top = "|";
		for (int boxNumber = 1; boxNumber < 6; boxNumber++) {
			Card card = getCardAtNode(5 * level + boxNumber);
			if (card == null) {
				top += spacerBox();
			} else {
				String upperAP = "" + card.getCurrentUpperAP();
				top += padString(upperAP, 25);
				top += "|";
			}
		}
		top += "\n";
		return top;
	}
	
	//Private helper method for toString()
	private String lastLine(int level) {
		String bottom = "|";
		for (int boxNumber = 1; boxNumber < 6; boxNumber++) {
			Card card = getCardAtNode(5 * level + boxNumber);
			if (card == null) {
				bottom += spacerBox();
			} else {
				String lowerAP = "" + card.getCurrentLowerAP();
				bottom += padString(lowerAP, 25);
				bottom += "|";
			}
		}
		bottom += "\n";
		return bottom;
	}
	
	//Private helper method for toString()
	private String monsterNameLine(int level) {
		String mNL = "|";
		for (int boxNumber = 1; boxNumber < 6; boxNumber++) {
			Card card = getCardAtNode(5 * level + boxNumber);
			if (card == null) {
				mNL += spacerBox();
			} else {
				String name = card.getName() + "";
				mNL += padString(name, 25);
				mNL += "|";
			}
		}
		mNL += "\n";
		return mNL;
	}
	
	//Private helper method for toString()
	private String hPLine(int level) {
		String hPLine = "|";
		for (int boxNumber = 1; boxNumber < 6; boxNumber++) {
			Card card = getCardAtNode(5 * level + boxNumber);
			if (card == null) {
				hPLine += spacerBox();
			} else {
				String hP = card.getCurrentHP() + "/" + card.getMaxHP() + " HP";
				if (card.getContaminatedTurnsLeft() > 0) {
					hP += " (Contaminated)";
				}
				hPLine += padString(hP, 25);
				hPLine += "|";
			}
		}
		hPLine += "\n";
		return hPLine;
	}
	
	//Private helper method for toString()
	private String middleLine(int level) {
		String middleLine = "|";
		for (int boxNumber = 1; boxNumber < 6; boxNumber++) {
			Card card = getCardAtNode(5 * level + boxNumber);
			if (card == null) {
				middleLine += spacerBox();
			} else {
				String leftAttack = "  " + card.getCurrentLeftAP();
				String rightAttack = card.getCurrentRightAP() + "  ";
				int spacing = 25 - (leftAttack.length() + rightAttack.length());
				String temp = leftAttack;
				for (int spaceNumber = 0; spaceNumber < spacing; spaceNumber++) {
					temp += " ";
				}
				temp += rightAttack + "|";
				middleLine += temp;
			}
		}
		middleLine += "\n";
		return middleLine;
	}
	
	//Private helper method for toString()
	private String monsterOwnerLine(int level) {
		String mOL = "|";
		for (int boxNumber = 1; boxNumber < 6; boxNumber++) {
			Card card = getCardAtNode(5 * level + boxNumber);
			if (card == null) {
				mOL += spacerBox();
			} else {
				String owner = card.getOwner().getName();
				mOL += padString(owner, 25);
				mOL += "|";
			}
		}
		mOL += "\n";
		return mOL;
	}
	
	//Private helper method for toString()
	private String padString(String toPad, int totalSpaces) {
		String s = "";
		int spacing = totalSpaces - toPad.length();
		for (int spaceNumber = 0; spaceNumber < (spacing / 2); spaceNumber++) {
			s += " ";
		}
		s += toPad;
		for (int spaceNumber = 0; spaceNumber < spacing - (spacing / 2); spaceNumber++) {
			s += " ";
		}
		return s;
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
		
		//Explicit private default constructor prevents creation of invalid Node
		private Node() {
		}
		
		//Constructor takes a node number and creates a blank node with that number
		public Node(int nodeNumber) {
			setNodeNumber(nodeNumber);
			setCurrentCard(null);
		}
		
		//Returns the number of this Node
		public int getNodeNumber() {
			return nodeNumber;
		}
		
		//Returns the current Card placed in this Node
		public Card getCard() {
			return currentCard;
		}
		
		//Sets the number of this Node on the Battlefield
		public void setNodeNumber(int nodeNumber) {
			this.nodeNumber = nodeNumber;
		}
		
		//Sets the current Card placed in this Node
		public void setCurrentCard(Card card) {
			this.currentCard = card;
		}
		
		//Removes a Card placed in this node and returns it
		public Card removeCard() {
			Card card = getCard();
			currentCard = null;
			return card;
		}
	}
}
