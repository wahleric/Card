import java.util.*;
import java.io.*;

/*
 * This class is a game client for the Card Battle data structures. When run, it will continuously play Card Battle, 
 * resetting after each game until the player decides to quit.
 * 
 * Author: Eric Wahlquist
 */

public class CardBattle {
	
	public static final int INITIAL_DEAL_NUMBER = 10;

	//Main method runs game
	public static void main(String[] args) throws IOException {
		Scanner keyboard = new Scanner(System.in);
		Battlefield board = new Battlefield(intro(keyboard), new Player("Computer"));
		boolean playAgain = true;
		while (playAgain) {
			gameLoop(board, keyboard);
			playAgain = askToPlayAgain(keyboard);
		}
		System.out.println("Thank you for playing!");
	}
	
	public static void gameLoop(Battlefield board, Scanner keyboard) {
		board.resetBoard();
		initialDraw(board);
		updateBoard(board);
		if (rollDice()) { //Human goes first
			while (board.getWinner() == null) {
				humanTurn(board, keyboard);
				computerTurn(board);
				checkImpairedBonus(board);
				board.endOfTurn();
				wait(2);
			}
		} else { //Computer goes first
			while (board.getWinner() == null) {
				computerTurn(board);
				humanTurn(board, keyboard);
				checkImpairedBonus(board);
				board.endOfTurn();
				wait(2);
			}
		}
		Player winner = board.getWinner();
		System.out.println("The winner of the game was " + winner.getName() + "!");
	}
	
	public static void humanTurn(Battlefield board, Scanner keyboard) {
		System.out.println("Your turn!\n");
		wait(2);
		showCards(board);
		chooseAndPlaceCard(keyboard, board);
		updateBoard(board);
		drawCard(board, board.getHumanPlayer());
		wait(1);
	}
	
	//AI calculates best place to put cards
	public static void computerTurn(Battlefield board) {
		Card cardToPlay = null;
		int nodeToPlay = -1;
		int maxScore = -1;
		for (int nodeNumber = 1; nodeNumber < 26; nodeNumber++) {
			Card cardInNode = board.getCardAtNode(nodeNumber);
			if (cardInNode == null) {
				for (Card card : board.getComputerPlayer().getHand()) {
					int damageGiven = 0;
					int damageTaken = 0;
					int score;
					//CALCUATIONS HERE
					//Scan enemies above
					if (nodeNumber > 5) {
						Card cardAbove = board.getCardAtNode(nodeNumber - 5);
						if (cardAbove != null && cardAbove.getOwner() != board.getComputerPlayer()) {
							damageGiven += Math.min(card.getCurrentUpperAP(), cardAbove.getCurrentHP());
							damageTaken += Math.min(cardAbove.getCurrentLowerAP(), card.getCurrentHP());
						}
					}
					//Scan enemies to the left
					if ((nodeNumber - 1) % 5 != 0) {
						Card cardLeft = board.getCardAtNode(nodeNumber - 1);
						if (cardLeft != null && cardLeft.getOwner() != board.getComputerPlayer()) {
							damageGiven += Math.min(card.getCurrentLeftAP(), cardLeft.getCurrentHP());
							damageTaken += Math.min(cardLeft.getCurrentRightAP(), card.getCurrentHP());
						}
					}
					//Scan enemies to the right
					if ((nodeNumber % 5) != 0) {
						Card cardRight = board.getCardAtNode(nodeNumber + 1);
						if (cardRight != null && cardRight.getOwner() != board.getComputerPlayer()) {
							damageGiven += Math.min(card.getCurrentRightAP(), cardRight.getCurrentHP());
							damageTaken += Math.min(cardRight.getCurrentLeftAP(), card.getCurrentHP());
						}
					}
					//Scan enemies below
					if (nodeNumber < 21) {
						Card cardBelow = board.getCardAtNode(nodeNumber + 5);
						if (cardBelow != null && cardBelow.getOwner() != board.getComputerPlayer()) {
							damageGiven += Math.min(card.getCurrentLowerAP(), cardBelow.getCurrentHP());
							damageTaken += Math.min(cardBelow.getCurrentUpperAP(), card.getCurrentHP());
						}
					}
					score = damageGiven - damageTaken;
					//CALCULATIONS OVER
					if (score > maxScore) {
						cardToPlay = card;
						nodeToPlay = nodeNumber;
						maxScore = score;
					}
				}
			}
		}
		//board.placeCard(board.getComputerPlayer(), cardToPlay, nodeToPlay);
		updateBoard(board);
		drawCard(board, board.getComputerPlayer());
		wait(1);
	}
	
	//Prints the current status of the board to the console
	public static void updateBoard(Battlefield board) {
		System.out.println(board);
	}
	
	//Shows the human's cards including names and stats
	public static void showCards(Battlefield board) {
		System.out.println("Your cards: \n");
		for (Card card : board.getHumanPlayer().getHand()) {
			System.out.print(board.getHumanPlayer().getHand().indexOf(card) + 1 + ". ");
			System.out.println(card.getName());
			System.out.print("Level: " + card.getLevel() + " Type: " + card.getType() + " HP: " + card.getMaxHP() + " Upper AP: " + card.getCurrentUpperAP());
			System.out.println(" Lower AP: " + card.getCurrentLowerAP() + " Left AP: " + card.getCurrentLeftAP() + " Right AP: " + card.getCurrentRightAP());
		}
		System.out.println("");
	}
	
	//Prompts the user for a Card to place and a space to place it in
	public static void chooseAndPlaceCard(Scanner keyboard, Battlefield board) {
		keyboard.reset();
		Player human = board.getHumanPlayer();
		int cardNumber = -1;
		System.out.println("Which card would you like to place (enter the number)?");
		while (cardNumber < 1 || cardNumber > human.getHand().size()) {
			try {
				cardNumber = keyboard.nextInt();
			} catch (InputMismatchException noInt) {
				keyboard.next();
			}
			if (cardNumber < 1 || cardNumber > human.getHand().size()) {
				System.out.println("Please enter a valid card number (1 - " + human.getHand().size() + ")");
			}
		}
		Card card = human.getHand().get(cardNumber - 1);
		System.out.println("Which node would you like to place the card in (1 - 25)?");
		int nodeNumber = -1;
		while (nodeNumber < 1  || nodeNumber > 25 || board.getCardAtNode(nodeNumber) != null) {
			try {
				nodeNumber = keyboard.nextInt();
			} catch (InputMismatchException noInt){
				keyboard.next();
			}
			if (nodeNumber < 1  || nodeNumber > 25) {
				System.out.println("Please enter a valid node number (1 - 25)");
			} else if (board.getCardAtNode(nodeNumber) != null) {
				System.out.println("Node is occupied. Please enter a different valid node number (1 - 25)");
			}
		}
		board.placeCard(human, card, nodeNumber);
	}
	
	//Randomly generates a bonus for any impaired monsters on the board
	public static void checkImpairedBonus(Battlefield board) {
		for (int nodeNumber = 1; nodeNumber < 26; nodeNumber++) {
			Card card = board.getCardAtNode(nodeNumber);
			if (card != null && card.getType().equals("Impaired")) {
				Random r = new Random();
				if (r.nextDouble() > 0.7) {
					Card cardToAdd = board.drawCard(null);
					System.out.println("A " + cardToAdd.getName() + " sees the impaired " + card.getName() + " and comes to its aid!");
					card.setCurrentHP(card.getCurrentHP() + (cardToAdd.getMaxHP() / 2));
					card.setCurrentAP(card.getCurrentUpperAP() + (cardToAdd.getCurrentUpperAP() / 2), card.getCurrentLowerAP() + (cardToAdd.getCurrentLowerAP() / 2), 
							card.getCurrentLeftAP() + (cardToAdd.getCurrentLeftAP() / 2), card.getCurrentRightAP() + (cardToAdd.getCurrentRightAP() / 2));
					board.discard(cardToAdd);
				}
			}
		}
	}
	
	//Takes a card from the deck and places it in a Player's hand
	public static void drawCard(Battlefield board, Player player) {
		board.drawCard(player);
	}
	
	//Draws INITIAL_DEAL_NUMBER Cards for each player
	public static void initialDraw(Battlefield board) {
		for (int i = 0; i < INITIAL_DEAL_NUMBER; i++) {
			drawCard(board, board.getHumanPlayer());
			drawCard(board, board.getComputerPlayer());
		}
	}
	
	//Rolls a 20-sided dice to see who goes first. Returns true if human wins the roll, returns false otherwise
	public static boolean rollDice() {
		System.out.println("Rolling the dice to see who goes first...\n");
		wait(2);
		Random r = new Random();
		int humanRoll = r.nextInt(20) + 1;
		int computerRoll = r.nextInt(20) + 1;
		System.out.println("Your roll: " + humanRoll);
		wait(1);
		System.out.println("Computer's roll: " + computerRoll + "\n");
		wait(1);
		if (humanRoll > computerRoll) {
			System.out.println("You won the roll! You will make the first move.\n");
			return true;
		}
		System.out.println("Your opponent won the roll! They will make the first move.\n");
		wait(2);
		return false;
	}
	
	//Prints the introduction to the console
	public static Player intro(Scanner keyboard) throws IOException {
		System.out.println("Welcome to Card Battle! Would you like to read the instructions (y/n)?");
		String answer = keyboard.nextLine();
		while (!(answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n"))) {
			System.out.println("Please type y for \"yes\" or n for \"no\". Would you like to read the instructions (y/n)?");
			answer = keyboard.nextLine();
		}
		if (answer.equalsIgnoreCase("y")) {
			instructions();
			pressEnterToContinue();
		}
		System.out.println("What is your name?");
		String name = keyboard.nextLine();
		return new Player(name);
	}
	
	//Reads the game instructions in from Instructions.txt
	public static void instructions() throws IOException {
		File file = new File("Instructions.txt");
		Scanner reader = new Scanner(file);
		while (reader.hasNextLine()) {
			String line = reader.nextLine();
			System.out.println(line);
		}
	}
	
	//Prompts the player to play again and takes input from the keyboard as an answer
	public static boolean askToPlayAgain(Scanner keyboard) {
		System.out.println("Would you like to play again (y/n)?");
		String answer = keyboard.next();
		while (!(answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n"))) {
			System.out.println("Please type y for \"yes\" or n for \"no\". Would you like to play again (y/n)?");
			answer = keyboard.next();
		}
		if (answer.equalsIgnoreCase("y")) {
			return true;
		}
		return false;
	}
	
	//Waits the given number of seconds before continuing the program
	public static void wait(int seconds) {
		long waitTime = seconds * 1000;
		long time = System.currentTimeMillis();
		long timesUp = time + waitTime;
		while (System.currentTimeMillis() < timesUp) {
		}
	}
	
	//Waits until the player presses the enter key before continuing the program
	public static void pressEnterToContinue() {
		System.out.println("Press Enter key to continue...");
		try { 
			System.in.read();
		} catch(Exception e){}
	}
}
