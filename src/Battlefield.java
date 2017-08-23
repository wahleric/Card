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
	private Node[] board;
	private List<Card> deck;
	private List<Card> discardPile;
	private int turn;
	
	//Explicit private default constructor that prevents a null battlefield from being created
	private Battlefield() {
	}
	
	//Main constructor used for creating Battlefield objects
	public Battlefield(Player human, Player computer) throws IOException {
		this.human = human;
		this.computer = computer;
		board = new Node[25];
		for (int i = 0; i < 25; i++) {
			board[i] = new Node(i + 1);
		}
		deck = new ArrayList<Card>();
		discardPile = new ArrayList<Card>();
		initDatabase();
		turn = 1;
	}
	
	//Returns the human Player on this board
	public Player getHumanPlayer() {
		return human;
	}
	
	//Returns the computer Player on this board
	public Player getComputerPlayer() {
		return computer;
	}
	
	//Returns the node located at the given position on the board. The Node numbers increase from left to right, top
	//to bottom. Node 1 is located at the upper left, and Node 25 is located at the lower right.
	public Node getNode(int nodeNumber) {
		nodeNumber--;
		if (nodeNumber < 0 || nodeNumber > 24) {
			throw new IllegalArgumentException("Invalid: Node does not exist");
		}
		return board[nodeNumber];
	}
	
	public Card getCardAtNode(int nodeNumber) {
		nodeNumber--;
		if (nodeNumber < 0 || nodeNumber > 24) {
			throw new IllegalArgumentException("Invalid: Node does not exist");
		}
		return board[nodeNumber].getCard();
	}
	
	//Returns true of the deck of all monster cards to draw from is empty; false otherwise
	public boolean deckIsEmpty() {
		return deck.isEmpty();
	}
	
	//Removes a card from the deck
	public Card removeCardFromDeck() {
		if (deckIsEmpty()) {
			throw new IllegalArgumentException("Invalid: Deck is empty");
		}
		return deck.remove(0);
	}
	
	//Adds a card to the deck
	public void addCardToDeck(Card card) {
		deck.add(card);
	}
	
	//Places a card in a Node on the Battlefield. Returns true if successful, false otherwise.
	public boolean placeCard(Player player, Card card, int nodeNumber) {
		if (getNode(nodeNumber).isEmpty()) {
			getNode(nodeNumber).setCurrentCard(card);
			getNode(nodeNumber).setOwner(player);
			removeCardFromHand(card, player);
			return true;
		}
		return false;
	}
	
	//Adds a Card to a Player's hand
	public void addCardToHand(Card card, Player player) {
		player.getHand().add(card);
	}
	
	//Removes a Card from a Player's hand
	public Card removeCardFromHand(Card card, Player player) {
		for (Card cardToRemove : player.getHand()) {
			if (cardToRemove == card) {
				player.getHand().remove(card);
				return card;
			}
		}
		throw new IllegalArgumentException("Invalid: Card is not in hand");
	}
	
	//Removes a Card from a Node on the Battlefield and returns it
	public Card removeCard(int nodeNumber) {
		if (board[nodeNumber - 1].isEmpty()) {
			throw new IllegalArgumentException("Invalid: Node is empty");
		}
		Card card = getNode(nodeNumber).getCard();
		getNode(nodeNumber).setCurrentCard(null);
		getNode(nodeNumber).setOwner(null);
		return card;
	}
	
	//Ends the turn and initiates all attacks
	public void endOfTurn() {
		for (int i = 0; i < board.length; i++) {
			Player owner = board[i].getOwner();
			if (i >= 5) {
				Node nodeAbove = board[i - 5];
				if (!nodeAbove.isEmpty() && nodeAbove.getOwner() != owner) {
					nodeAbove.setCurrentHP(nodeAbove.getHP() - board[i].getUpperAP());
					nodeAbove.owner.setHP(nodeAbove.owner.getHP() - board[i].getUpperAP());
				}
			}
			if (i % 5 != 0) {
				Node nodeLeft = board[i - 1];
				if (!nodeLeft.isEmpty() && nodeLeft.getOwner() != owner) {
					nodeLeft.setCurrentHP(nodeLeft.getHP() - board[i].getLeftAP());
					nodeLeft.owner.setHP(nodeLeft.owner.getHP() - board[i].getLeftAP());
				}
			}
			if ((i - 4) % 5 != 0) {
				Node nodeRight = board[i + 1];
				if (!nodeRight.isEmpty() && nodeRight.getOwner() != owner) {
					nodeRight.setCurrentHP(nodeRight.getHP() - board[i].getRightAP());
					nodeRight.owner.setHP(nodeRight.owner.getHP() - board[i].getRightAP());
				}
			}
			if (i <= 19) {
				Node nodeBelow = board[i + 5];
				if (!nodeBelow.isEmpty() && nodeBelow.getOwner() != owner) {
					nodeBelow.setCurrentHP(nodeBelow.getHP() - board[i].getLowerAP());
					nodeBelow.owner.setHP(nodeBelow.owner.getHP() - board[i].getLowerAP());
				}
			}
		}
		for (int i = 0; i < 25; i++) {
			if (!board[i].isEmpty() && board[i].getHP() <= 0) {
				int currentHP = board[i].getHP();
				board[i].getOwner().setHP(board[i].getOwner().getHP() + (0 - currentHP));
				discardPile.add(board[i].removeCard());
			}
		}
		turn++;
	}
	
	//Calculates the damage that will be taken by a monster placed in a given space
	public void calculateDamageTaken() {
		
	}
	
	//Returns all cards from the board as well as both players hands to the deck, and re-shuffles the deck
	//FIX
	public void resetBoard() {
		for (int i = 1; i <= 25; i++) {
			if (!getNode(i).isEmpty()) {
				addCardToDeck(getNode(i).removeCard());
			}
		}
		for (Card card : human.getHand()) {
			addCardToDeck(card);
		}
		for (Card card : computer.getHand()) {
			addCardToDeck(card);
		}
		for (Card card : discardPile) {
			addCardToDeck(card);
		}
		human.getHand().clear();
		computer.getHand().clear();
		discardPile.clear();
		Collections.shuffle(deck);
		human.setHP(Player.PLAYER_MAX_HP);
		computer.setHP(Player.PLAYER_MAX_HP);
		turn = 1;
	}
	
	//Returns the Player who won the game. Returns a new Player named "a Tie" if the game was a tie, and returns null if the game is not over.
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
		String humanHP = human.getName() + ": " + human.getHP() + " HP";
		String computerHP = computer.getName() + ": " + computer.getHP() + " HP";
		s += humanHP;
		int spacing = 131 - (humanHP.length() + computerHP.length());
		for (int i = 0; i < spacing; i++) {
			s += " ";
		}
		s += computerHP;
		s += "\n\n" + border();
		//Do the following for each level of the board
		for (int i = 0; i < 5; i++) {
			s += firstLine(i);
			s += spacerLine();
			s += monsterNameLine(i);
			s += hPLine(i);
			s += middleLine(i);
			s += spacerLine();
			s += monsterOwnerLine(i);
			s += spacerLine();
			s += lastLine(i);
			s += border();
			
		}
		return s;	
	}
	
	//Private helper method for toString()
	private String border() {
		String border = "\n";
		for (int i = 0; i < 131; i++) {
			border = "-" + border;
		}
		return border;
	}
	
	//Private helper method for toString()
	private String spacerLine() {
		String spacer = "|\n";
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 25; j++) {
				spacer = " " + spacer;
			}
			spacer = "|" + spacer;
		}
		return spacer;
	}
	
	//Private helper method for toString()
	private String spacerBox() {
		String spacer = "";
		for (int j = 0; j < 25; j++) {
			spacer += " ";
		}
		spacer += "|";
		return spacer;
	}
	
	//Private helper method for toString()
	private String firstLine(int level) {
		String top = "|";
		for (int i = 0; i < 5; i++) {
			if (board[(5 * level) + i].isEmpty()) {
				top += spacerBox();
			} else {
				Card card = board[(5 * level) + i].getCard();
				String upperAP = "" + card.getUpperAP();
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
		for (int i = 0; i < 5; i++) {
			if (board[(5 * level) + i].isEmpty()) {
				bottom += spacerBox();
			} else {
				Card card = board[(5 * level) + i].getCard();
				String lowerAP = "" + card.getLowerAP();
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
		for (int i = 0; i < 5; i++) {
			if (board[(5 * level) + i].isEmpty()) {
				mNL += spacerBox();
			} else {
				Card card = board[(5 * level) + i].getCard();
				String name = card.getName() + "";
				mNL += padString(name, 25);
				mNL += "|";
			}
		}
		mNL += "\n";
		return mNL;
	}
	
	private String hPLine(int level) {
		String hPLine = "|";
		for (int i = 0; i < 5; i++) {
			if (board[(5 * level) + i].isEmpty()) {
				hPLine += spacerBox();
			} else {
				Node node = board[(5 * level) + i];
				String hP = "HP: " + node.getHP();
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
		for (int i = 0; i < 5; i++) {
			if (board[(5 * level) + i].isEmpty()) {
				middleLine += spacerBox();
			} else {
				Card card = board[(5 * level) + i].getCard();
				String leftAttack = "  " + card.getLeftAP();
				String rightAttack = card.getRightAP() + "  ";
				int spacing = 25 - (leftAttack.length() + rightAttack.length());
				String temp = leftAttack;
				for (int j = 0; j < spacing; j++) {
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
		for (int i = 0; i < 5; i++) {
			if (board[(5 * level) + i].isEmpty()) {
				mOL += spacerBox();
			} else {
				Node node = board[(5 * level) + i];
				String owner = node.getOwner().getName();
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
		for (int j = 0; j < (spacing / 2); j++) {
			s += " ";
		}
		s += toPad;
		for (int j = 0; j < spacing - (spacing / 2); j++) {
			s += " ";
		}
		return s;
	}
	
	//Initializes the database of all monster cards by reading from *monsters.txt and loads them into the game 
	//when this Battlefield is created
	private void initDatabase() throws IOException {
		for (int i = 1; i <= 5; i++) {
			File database = new File(i + "monsters.txt");
			Scanner scanner = new Scanner(database);
			while (scanner.hasNextLine()) {
				String name = scanner.nextLine();
				String description = scanner.nextLine();
				String stats = scanner.nextLine();
				Scanner statScan = new Scanner(stats);
				int level = i;
				int hP = statScan.nextInt();
				int upperAP = statScan.nextInt();
				int lowerAP = statScan.nextInt();
				int leftAP = statScan.nextInt();
				int rightAP = statScan.nextInt();
				statScan.close();
				if (description.length() > 40) {
					String newDescription = "";
					String tempString = "";
					Scanner lineScan = new Scanner(description);
					while (lineScan.hasNext()) {
						tempString += lineScan.next() + " ";
						if (!lineScan.hasNext()) {
							newDescription += tempString;
						} else if (tempString.length() > 40) {
							newDescription += tempString + "\n";
							tempString = "";
						}
					}
					description = newDescription;
					lineScan.close();
				}
				deck.add(new Card(name, description, level, hP, upperAP, lowerAP, leftAP, rightAP));
			}
			scanner.close();
		}
	}
	
	/*
	 * The Node class represents a single space on the battlefield on which a card may be placed. It keeps track of the Node's
	 * location, the Card that is placed in it, the owner of the Card, and the current status of the Card's attributes
	 *
	 * Author: Eric Wahlquist
	 */
	public class Node {
		
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
		public Card getCard() {
			return currentCard;
		}
		
		//Returns the current HP of the monster represented by the Card in this Node
		public int getHP() {
			return currentHP;
		}
		
		//Returns the current upper AP of the monster represented by the Card in this Node
		public int getUpperAP() {
			return currentUpperAP;
		}
		
		//Returns the current lower AP of the monster represented by the Card in this Node
		public int getLowerAP() {
			return currentLowerAP;
		}
		
		//Returns the current left AP of the monster represented by the Card in this Node
		public int getLeftAP() {
			return currentLeftAP;
		}
		
		//Returns the current right AP of the monster represented by the Card in this Node
		public int getRightAP() {
			return currentRightAP;
		}
		
		//Returns the owner of the Card in this Node
		public Player getOwner() {
			return owner;
		}
		
		//Sets the number of this Node on the Battlefield
		public void setNodeNumber(int nodeNumber) {
			this.nodeNumber = nodeNumber;
		}
		
		//Sets the current Card placed in this Node
		public void setCurrentCard(Card card) {
			this.currentCard = card;
			this.currentHP = card.getMaxHP();
			this.currentUpperAP = card.getUpperAP();
			this.currentLowerAP = card.getLowerAP();
			this.currentLeftAP = card.getLeftAP();
			this.currentRightAP = card.getRightAP();
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
		
		//Sets the current owner of this node
		public void setOwner(Player owner) {
			this.owner = owner;
		}
		
		//Removes a Card placed in this node and returns it
		public Card removeCard() {
			Card card = getCard();
			currentCard = null;
			currentHP = 0;
			currentUpperAP = 0;
			currentLowerAP = 0;
			currentLeftAP = 0;
			currentRightAP = 0;
			owner = null;
			return card;
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
				throw new IllegalArgumentException("Invalid: Node is empty");
			}
		}
	}
}
