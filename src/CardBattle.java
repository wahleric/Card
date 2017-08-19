import java.util.*;
import java.io.*;

/*
 * This class is a game client for the Card Battle data structures. When run, it will continuously play Card Battle, 
 * resetting after each game until the player decides to quit.
 * 
 * Author: Eric Wahlquist
 */


public class CardBattle {

	//Main method runs game
	public static void main(String[] args) throws IOException {
		Scanner keyboard = new Scanner(System.in);
		intro(keyboard);
		Player human = getPlayerName(keyboard);
		Player computer = new Player("Computer");
		Battlefield board = new Battlefield(human, computer);
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
		if (rollDice()) { //Human goes first
			while (board.getWinner() == null) {
				humanTurn(board, keyboard);
				updateBoard(board);
				wait(1);
				computerTurn(board);
				updateBoard(board);
				wait(1);
				board.endOfTurn();
				wait(3);
			}
		} else { //Computer goes first
			while (board.getWinner() == null) {
				computerTurn(board);
				updateBoard(board);
				wait(1);
				humanTurn(board, keyboard);
				updateBoard(board);
				wait(1);
				board.endOfTurn();
			}
		}
		Player winner = board.getWinner();
		System.out.println("The winner of the game was " + winner.getName());
	}
	
	public static void humanTurn(Battlefield board, Scanner keyboard) {
		System.out.println("Your turn:");
		wait(1);
		System.out.println("Your cards: ");
		wait(1);
		showCards(board);
		chooseAndPlaceCard(keyboard, board);
		//After receiving answer, place that card in the node and refresh board printout
	}
	
	public static void computerTurn(Battlefield board) {
		
	}
	
	public static void updateBoard(Battlefield board) {
		for (int i = 0; i < 100; i++) {
			System.out.println("");
		}
		System.out.println(board.toString());
	}
	
	//Shows the human's cards including names and stats
	public static void showCards(Battlefield board) {
		for (Card card : board.getPlayer1().getHand()) {
			System.out.print(board.getPlayer1().getHand().indexOf(card) + 1 + ". ");
			System.out.println(card.getName());
			System.out.print("Level: " + card.getLevel() + " HP: " + card.getMaxHP() + " Upper AP: " + card.getUpperAP());
			System.out.println(" Lower AP: " + card.getLowerAP() + " Left AP: " + card.getLeftAP() + " Right AP: " + card.getRightAP());
		}
	}
	
	public static void chooseAndPlaceCard(Scanner keyboard, Battlefield board) {
		keyboard.reset();
		Player human = board.getPlayer1();
		int cardNumber = -1;
		System.out.println("Which card would you like to place (enter the number)?");
		while (cardNumber < 1 || cardNumber > human.getHand().size()) {
			cardNumber = keyboard.nextInt();
			if (cardNumber < 1 || cardNumber > human.getHand().size()) {
				System.out.println("Please enter a valid card number (1 - " + human.getHand().size() + ")");
			}
		}
		Card card = human.getHand().get(cardNumber - 1);
		System.out.println("Which space would you like to place the card in (1 - 25)?");
		int nodeNumber = -1;
		while (nodeNumber < 1  || nodeNumber > 25 || board.getCardAtNode(nodeNumber) != null) {
			nodeNumber = keyboard.nextInt();
			if (nodeNumber < 1  || nodeNumber > 25) {
				System.out.println("Please enter a valid place number (1 - 25)");
			} else if (board.getCardAtNode(nodeNumber) != null) {
				System.out.println("Space is taken. Please enter a valid place number (1 - 25)");
			}
		}
		board.placeCard(human, card, nodeNumber);
	}
	
	//Takes a card from the deck and places it in a Player's hand
	public static void drawCard(Battlefield board, Player player) {
		board.addCardToHand(board.removeCardFromDeck(), player);
	}
	
	public static void initialDraw(Battlefield board) {
		for (int i = 0; i < 10; i++) {
			drawCard(board, board.getPlayer1());
			drawCard(board, board.getPlayer2());
		}
	}
	
	//Rolls a 20-sided dice to see who goes first. Returns true if human wins the roll, returns false otherwise
	public static boolean rollDice() {
		System.out.println("Rolling the dice to see who goes first...");
		wait(2);
		Random r = new Random();
		int humanRoll = r.nextInt(20) + 1;
		int computerRoll = r.nextInt(20) + 1;
		System.out.println("Your roll: " + humanRoll);
		wait(1);
		System.out.println("Computer's roll: " + computerRoll);
		wait(1);
		if (humanRoll > computerRoll) {
			System.out.println("You won the roll! You will make the first move.");
			return true;
		}
		System.out.println("Your opponent wont the roll! They will make the first move.");
		return false;
	}
	
	//Prints the introduction to the console
	public static void intro(Scanner keyboard) throws IOException {
		System.out.println("Welcome to Card Battle! Would you like to read the instructions (y/n)?");
		String answer = keyboard.nextLine();
		while (!(answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n"))) {
			System.out.println("Please type y for \"yes\" or n for \"no\". Would you like to read the instructions (y/n)?");
			answer = keyboard.next();
		}
		if (answer.equalsIgnoreCase("y")) {
			instructions();
			pressEnterToContinue();
		}
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
	
	//Prompts the player for a name and takes input from the keyboard as an answer
	public static Player getPlayerName(Scanner keyboard) {
		System.out.println("What is your name?");
		String name = keyboard.nextLine();
		System.out.println(name);
		return new Player(name);
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
