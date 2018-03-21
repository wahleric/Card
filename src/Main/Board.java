package Main;
/*
 * This class represents a board on which the game is played. The Board consists of Nodes arranged in 
 * a 5x5 square, which all begin empty. Board keeps track of the game status including the Players 
 * involved, the placement of Cards, the game deck and discard pile, the difficulty level, and the 
 * current turn number.
 * 
 * Author: Eric Wahlquist
 */

public class Board {

	public static final int CARDS_IN_DECK = 100;

	private Player human, computer;
	private Deck deck, discardPile;
	private int turn;
	private Node[][] board;
	private CardGenerator cardGenerator;
	private String difficulty;

	// Explicit private default constructor that prevents an invalid Board
	// from being created

	@SuppressWarnings("unused")
	private Board() {
	}

	// Main constructor used for creating Board objects

	public Board(Player human, Player computer) {
		this.human = human;
		this.computer = computer;
		this.turn = 1;
		this.difficulty = "";
		initializeBoard();
		initializeDecks();
	}

	// Returns the human Player of this Board

	public Player getHumanPlayer() {
		return human;
	}

	// Returns the computer Player of this Board

	public Player getComputerPlayer() {
		return computer;
	}

	// Returns the current turn number of this Board

	public int getTurn() {
		return turn;
	}

	// Returns the Card placed in a given Node on this Board. Returns null if
	// the Node is empty

	public Card getCard(int row, int column) {
		return board[row][column].getCard();
	}

	// Returns the Zone bonus that is currently on the given Node. Returns null
	// if there is no Zone bonus

	public Zone getZone(int row, int column) {
		return board[row][column].getZoneBonus();
	}

	// Returns true if this Board is full (has no open Nodes left to place Cards
	// in)

	public boolean isFull() {
	    for (int i = 0; i < 5; i++) {
	        for (int j = 0; j < 5; j++) {
	            if (getCard(i, j) == null) {
	                return false;
	            }
	        }
	    }
		return true;
	}

	// Returns the current difficulty of this Board

	public String getDifficulty() {
		return difficulty;
	}

	// Sets the current difficulty of this Board

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	// Creates a Zone bonus on a given Node on the board

	public void generateZoneBonus(Zone zone, int row, int column) {
	    board[row][column].setZoneBonus(zone);
	}

	// Places a card in a Node on the Board. Returns true if successful,
	// false otherwise.

	public boolean placeCard(Card card, int row, int column) {
		if (getCard(row, column) == null) {
			board[row][column].setCurrentCard(card);
			if (!card.getOwner().getHand().remove(card)) {
				throw new IllegalArgumentException("Invalid: Card is not in hand");
			}
			return true;
		}
		return false;
	}

	// Removes a Card from a Node on the Board, places it in the discard pile,
	// and returns it

	public Card removeCard(int row, int column) {
		Card card = getCard(row, column);
		if (card == null) {
			throw new IllegalArgumentException("Invalid: Node is empty");
		}
		discardPile.addCard(card);
		return board[row][column].removeCard();
	}

	// Draws a card from this Board's Deck into a Player's hand

	public Card drawCard(Player player) {
		Card card = deck.drawCard();
		if (player != null) {
			card.setOwner(player);
			player.getHand().add(card);
		}
		return card;
	}

	// Adds a card to this Board's Deck

	public void addCardToDeck(Card card) {
		deck.addCard(card);
	}

	// Increments the turn counter by 1

	public void incrementTurn() {
		turn++;
	}

	// Brings the board back to a clean state

	public void resetBoard() {

		// Remove all Cards and zone bonuses from the board
	    
	    for (int i = 0; i < 5; i++) {
	        for (int j = 0; j < 5; j++) {
	            if (getCard(i, j) != null) {
	                removeCard(i, j);
	            }
	            generateZoneBonus(null, i, j);
	        }
	    }

		// Return all cards from Players' hands and the discard pile

		for (Card card : getHumanPlayer().getHand()) {
			addCardToDeck(card);
		}
		for (Card card : getComputerPlayer().getHand()) {
			addCardToDeck(card);
		}
		while (!discardPile.isEmpty()) {
			addCardToDeck(discardPile.drawCard());
		}

		// Reset the Deck, Players' HP and hands, and reset the turn counter

		deck.reset();
		human.reset();
		computer.reset();
		turn = 1;

	}

	// Initializes the Board deck with a given number of Cards and creates the discard pile

	private void initializeDecks() {
		this.deck = new Deck();
		this.discardPile = new Deck();
		this.cardGenerator = new CardGenerator();
		for (int cardNumber = 0; cardNumber < CARDS_IN_DECK; cardNumber++) {
			addCardToDeck(cardGenerator.generateRandomCard());
		}
	}
	
	private void initializeBoard() {
		board = new Node[5][5];
		for (int i = 0; i < 5; i++) {
		    for (int j = 0; j < 5; j++) {
		        board[i][j] = new Node();
		    }
		}
	}

	// Returns a String representation of the current status of the board

	public String toString() {
		String s = padString("Turn: " + turn, 131) + "\n";
		String humanHP = human.toString();
		String computerHP = computer.toString();
		s += humanHP;
		int spacing = 131 - (humanHP.length() + computerHP.length());
		s += padString(" ", spacing);
		s += computerHP;
		s += "\n\n" + border();
		
		for (int row = 0; row < 5; row++) {
			s += firstLine(row);
			s += zoneLine(row);
			s += monsterNameLine(row);
			s += hPLine(row);
			s += middleLine(row);
			s += spacerLine();
			s += monsterOwnerLine(row);
			s += zoneLine(row);
			s += lastLine(row);
			s += border();
		}
		return s;
	}

	// Private helper method for toString() displays a border between card slots

	private String border() {
		String border = "\n";
		for (int dashNumber = 0; dashNumber < 131; dashNumber++) {
			border = "-" + border;
		}
		return border;
	}

	// Private helper method for toString() displays a line that describes the
	// Zone type of a Node if applicable

	private String zoneLine(int row) {
		String zoneLine = "|";
		for (int column = 0; column < 5; column++) {
			Zone zone = getZone(row, column);
			if (zone != null) {
				zoneLine += padString("* " + zone.getType().toUpperCase() + " ZONE *", 25) + "|";
			} else {
				zoneLine += spacerBox();
			}
		}
		zoneLine += "\n";
		return zoneLine;
	}

	// Private helper method for toString() displays a line with only spaces and
	// Node dividers (|)

	private String spacerLine() {
		String spacer = "|\n";
		for (int column = 0; column < 5; column++) {
			spacer = padString(" ", 25) + spacer;
			spacer = "|" + spacer;
		}
		return spacer;
	}

	// Private helper method for toString() displays one Node worth of spaces
	// followed by a Node divider (|)

	private String spacerBox() {
		String spacer = "";
		spacer += padString(" ", 25);
		spacer += "|";
		return spacer;
	}

	// Private helper method for toString() displays upper AP of each Node's
	// current card if applicable

	private String firstLine(int row) {
		String top = "|";
		for (int column = 0; column < 5; column++) {
			Card card = getCard(row, column);
			if (card == null) {
				top += spacerBox();
			} else {
				top += padString("" + card.getCurrentUpperAP(), 25);
				top += "|";
			}
		}
		top += "\n";
		return top;
	}

	// Private helper method for toString() displays lower AP of each Node's
	// current card if applicable

	private String lastLine(int row) {
		String bottom = "|";
		for (int column = 0; column < 5; column++) {
			Card card = getCard(row, column);
			if (card == null) {
				bottom += spacerBox();
			} else {
				bottom += padString("" + card.getCurrentLowerAP(), 25);
				bottom += "|";
			}
		}
		bottom += "\n";
		return bottom;
	}

	// Private helper method for toString() displays the monster name for each
	// Node's current Card if applicable

	private String monsterNameLine(int row) {
		String mNL = "|";
		for (int column = 0; column < 5; column++) {
			Card card = getCard(row, column);
			if (card == null) {
				mNL += spacerBox();
			} else {
				mNL += padString(card.getName(), 25);
				mNL += "|";
			}
		}
		mNL += "\n";
		return mNL;
	}

	// Private helper method for toString() displays each Node's monster's
	// current and maximum HP if applicable

	private String hPLine(int row) {
		String hPLine = "|";
		for (int column = 0; column < 5; column++) {
			Card card = getCard(row, column);
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

	// Private helper method for toString() displays left and right AP for each
	// Node's monster if applicable

	private String middleLine(int row) {
		String middleLine = "|";
		for (int column = 0; column < 5; column++) {
			Card card = getCard(row, column);
			if (card == null) {
				middleLine += spacerBox();
			} else {
				String leftAttack = "  " + card.getCurrentLeftAP();
				String rightAttack = card.getCurrentRightAP() + "  ";
				int spacing = 25 - (leftAttack.length() + rightAttack.length());
				String temp = leftAttack;
				temp += padString(" ", spacing);
				temp += rightAttack + "|";
				middleLine += temp;
			}
		}
		middleLine += "\n";
		return middleLine;
	}

	// Private helper method for toString() displays each Node's monster's owner
	// if applicable

	private String monsterOwnerLine(int row) {
		String mOL = "|";
		for (int column = 0; column < 5; column++) {
			Card card = getCard(row, column);
			if (card == null) {
				mOL += spacerBox();
			} else {
				mOL += padString(card.getOwner().getName(), 25);
				mOL += "|";
			}
		}
		mOL += "\n";
		return mOL;
	}

	// Private helper method for toString() pads the given String with the given
	// number of spaces, with the String as centered as possible

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
	 * The Node class represents a single space on the battlefield on which a
	 * card may be placed. It keeps track of the Node's location, the Card that
	 * is placed in it, the owner of the Card, and the current status of the
	 * Card's attributes
	 *
	 * Author: Eric Wahlquist
	 */

	private class Node {

		private Card currentCard;
		private Zone zoneBonus;

		// Constructor creates a blank Node

		public Node() {
			setCurrentCard(null);
			zoneBonus = null;
		}

		// Returns the current Card placed in this Node

		public Card getCard() {
			return currentCard;
		}

		// Returns the Zone bonus currently in place in this Node. Returns null
		// if no bonus

		public Zone getZoneBonus() {
			return zoneBonus;
		}

		// Sets the Zone bonus of this Node

		public void setZoneBonus(Zone zone) {
			zoneBonus = zone;
		}

		// Sets the current Card placed in this Node and applies any Zone
		// bonuses to it

		public void setCurrentCard(Card card) {
			this.currentCard = card;
			if (zoneBonus != null) {
				zoneBonus.applyZoneBonus(card);
			}
		}

		// Removes a Card placed in this Node and returns it

		public Card removeCard() {
			Card card = getCard();
			currentCard = null;
			return card;
		}
	}
}
