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

	private Player player1;
	private Player player2;
	private Node[] board;
	private List<Card> allCards;
	private int turn;
	
	//Explicit private default constructor that prevents a null battlefield from being created
	private Battlefield() {
	}
	
	//Main constructor used for creating Battlefield objects
	public Battlefield(Player player1, Player player2) throws IOException {
		this.player1 = player1;
		this.player2 = player2;
		board = new Node[25];
		for (int i = 0; i < 25; i++) {
			board[i] = new Node(i + 1);
		}
		allCards = new ArrayList<Card>();
		initDatabase();
		turn = 1;
	}
	
	//Returns Player 1
	public Player getPlayer1() {
		return player1;
	}
	
	//Returns Player 2
		public Player getPlayer2() {
			return player2;
		}
	
	//Returns the node located at the given position on the board. The Node numbers increase from left to right, top
	//to bottom. Node 1 is located at the upper left, and Node 25 is located at the lower right.
	public Node getNode(int nodeNumber) {
		nodeNumber--;
		if (nodeNumber < 0 || nodeNumber > 24) {
			throw new IllegalArgumentException("Invalid node reference");
		}
		return board[nodeNumber];
	}
	
	//Places a card in a Node on the Battlefield
	public void placeCard(Player player, Card card, int nodeNumber) {
		if (!getNode(nodeNumber).isEmpty()) {
			throw new IllegalArgumentException("Invalid Node: Node already has a Card");
		}
		board[nodeNumber - 1].setCurrentCard(card);
		board[nodeNumber - 1].setOwner(player);
	}
	
	//Removes a Card from a Node on the Battlefield and returns it
	public Card removeCard(int nodeNumber) {
		if (board[nodeNumber - 1].isEmpty()) {
			throw new IllegalArgumentException("Invalid Card: Node is empty");
		}
		Card card = board[nodeNumber - 1].getCard();
		board[nodeNumber - 1].setCurrentCard(null);
		board[nodeNumber - 1].setOwner(null);
		return card;
	}
	
	//Ends the turn and initiates all attacks
	//NEEDS SUPPORT FOR CHECKING OWNERS
	public void endOfTurn() {
		for (int i = 0; i < board.length; i++) {
			Player owner = board[i].getOwner();
			if (i >= 5) {
				Node nodeAbove = board[i - 5];
				if (!nodeAbove.isEmpty()) {
					nodeAbove.setCurrentHP(nodeAbove.getHP() - board[i].getUpperAP());
				}
			}
			if (i % 5 != 0) {
				Node nodeLeft = board[i - 1];
				if (!nodeLeft.isEmpty()) {
					nodeLeft.setCurrentHP(nodeLeft.getHP() - board[i].getLeftAP());
				}
			}
			if ((i - 4) % 5 != 0) {
				Node nodeRight = board[i + 1];
				if (!nodeRight.isEmpty()) {
					nodeRight.setCurrentHP(nodeRight.getHP() - board[i].getRightAP());
				}
			}
			if (i <= 19) {
				Node nodeBelow = board[i + 5];
				if (!nodeBelow.isEmpty()) {
					nodeBelow.setCurrentHP(nodeBelow.getHP() - board[i].getLowerAP());
				}
			}
		}
		turn++;
	}
	
	
	//Returns a String representation of the current status of the board
	public String toString() {
		String s = padString("Turn: " + turn, 131);
		s += "\n" + border();
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
	
	//Initializes the database of all monster cards by reading from monsters.txt and loads them into the game 
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
				}
				allCards.add(new Card(name, description, level, hP, upperAP, lowerAP, leftAP, rightAP));
			}
		}
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
