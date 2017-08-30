import java.io.File;
import java.io.IOException;
import java.util.*;

/*
 * This class handles the input from the keyboard for Card Battle as well as printing to the console.
 */

public class CardBattleIO {

	private Board board;
	private static Scanner reader;

	// Private explicit constructor prevents an invalid CardBattleIO from being
	// created

	private CardBattleIO() {
	}

	// Creates a CardBattleIO from a given Board

	public CardBattleIO(Board board) {
		reader = new Scanner(System.in);
		this.board = board;
	}

	// Prompts the Player to choose a difficulty level

	public String pickDifficulty() {
		System.out.println("Please choose a difficulty level [1 - 3]:");
		System.out.println("1. Easy");
		System.out.println("2. Medium");
		System.out.println("3. Hard");
		int choice = -1;
		while (choice < 1 || choice > 3) {
			try {
				choice = reader.nextInt();
			} catch (InputMismatchException noInt) {
				reader.next();
			}
			if (choice < 1 || choice > 3) {
				System.out.println("Please enter a valid number [1 - 3]");
			}
		}
		if (choice == 1) {
			return "Easy";
		} else if (choice == 2) {
			return "Medium";
		} else if (choice == 3) {
			return "Hard";
		}
		throw new IllegalArgumentException("Invalid difficulty");
	}

	// Shows the human's cards including names and stats

	public void showPlayersCards() {
		System.out.println("Your cards: \n");
		for (Card card : board.getHumanPlayer().getHand()) {
			System.out.print(board.getHumanPlayer().getHand().indexOf(card) + 1 + ". ");
			System.out.println(card.getName());
			System.out.print("Level: " + card.getLevel() + " Type: " + card.getType() + " HP: " + card.getMaxHP()
					+ " Upper AP: " + card.getCurrentUpperAP());
			System.out.println(" Lower AP: " + card.getCurrentLowerAP() + " Left AP: " + card.getCurrentLeftAP()
					+ " Right AP: " + card.getCurrentRightAP());
		}
		System.out.println("");
	}
	
	// Controls the 
	
	public void humanTurn() {
		System.out.println("Your turn!\n");
		CardBattleAI.wait(2);
		showPlayersCards();
		Card card = pickCard();
		int nodeNumber = pickNode();
		board.placeCard(board.getHumanPlayer(), card, nodeNumber);
		board.drawCard(board.getHumanPlayer());
	}

	// Prints the current status of the board to the console

	public void printBoard() {
		System.out.println(board);
	}

	// Prompts the user to pick a Card from their hand to place on the board and
	// returns that Card

	public Card pickCard() {
		int cardNumber = -1;
		System.out.println("Which card would you like to place (enter the number)?");
		while (cardNumber < 1 || cardNumber > board.getHumanPlayer().getHand().size()) {
			try {
				cardNumber = reader.nextInt();
			} catch (InputMismatchException noInt) {
				reader.next();
			}
			if (cardNumber < 1 || cardNumber > board.getHumanPlayer().getHand().size()) {
				System.out.println(
						"Please enter a valid card number [1 - " + board.getHumanPlayer().getHand().size() + "]");
			}
		}
		Card card = board.getHumanPlayer().getHand().get(cardNumber - 1);
		reader.reset();
		return card;
	}

	// Prompts the user to pick a Node to place their Card in and returns that
	// Node's number

	public int pickNode() {
		int nodeNumber = -1;
		System.out.println("Which node would you like to place the card in (1 - 25)?");
		while (nodeNumber < 1 || nodeNumber > 25 || board.getCardAtNode(nodeNumber) != null) {
			try {
				nodeNumber = reader.nextInt();
			} catch (InputMismatchException noInt) {
				reader.next();
			}
			if (nodeNumber < 1 || nodeNumber > 25) {
				System.out.println("Please enter a valid node number (1 - 25)");
			} else if (board.getCardAtNode(nodeNumber) != null) {
				System.out.println("Node is occupied. Please enter a different valid node number (1 - 25)");
			}
		}
		reader.reset();
		return nodeNumber;
	}

	// Prints the introduction to the console

	public void intro() throws IOException {
		System.out.println("Welcome to Card Battle! Would you like to read the instructions (y/n)?");
		String answer = reader.nextLine();
		while (!(answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n"))) {
			System.out.println(
					"Please type y for \"yes\" or n for \"no\". Would you like to read the instructions (y/n)?");
			answer = reader.nextLine();
		}
		if (answer.equalsIgnoreCase("y")) {
			instructions();
			pressEnterToContinue();
		}
		System.out.println("What is your name?");
		String name = reader.nextLine();
		reader.reset();
		board.getHumanPlayer().setName(name);
	}

	// Rolls a 20-sided dice to see who goes first. Returns true if human wins
	// the roll, returns false otherwise

	public boolean rollDice() {
		System.out.println("Rolling the dice to see who goes first...\n");
		wait(2);
		Random r = new Random();
		int humanRoll = r.nextInt(20) + 1;
		int computerRoll = r.nextInt(20) + 1;
		System.out.println("Your roll: " + humanRoll);
		wait(1);
		System.out.println("Computer's roll: " + computerRoll + "\n");
		CardBattleAI.wait(1);
		if (humanRoll > computerRoll) {
			System.out.println("You won the roll! You will make the first move.\n");
			return true;
		}
		System.out.println("Your opponent won the roll! They will make the first move.\n");
		wait(2);
		return false;
	}

	// Reads the game instructions in from Instructions.txt

	public static void instructions() throws IOException {
		File file = new File("Instructions.txt");
		Scanner fileReader = new Scanner(file);
		while (fileReader.hasNextLine()) {
			String line = fileReader.nextLine();
			System.out.println(line);
		}
		fileReader.close();
	}

	// Prompts the player to play again and takes input from the keyboard as an
	// answer

	public boolean askToPlayAgain() {
		System.out.println("Would you like to play again (y/n)?");
		String answer = reader.next();
		while (!(answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n"))) {
			System.out.println("Please type y for \"yes\" or n for \"no\". Would you like to play again (y/n)?");
			answer = reader.next();
		}
		reader.reset();
		if (answer.equalsIgnoreCase("y")) {
			return true;
		}
		return false;
	}

	// Waits the given number of seconds before continuing the program

	public static void wait(int seconds) {
		long waitTime = seconds * 1000;
		long time = System.currentTimeMillis();
		long timesUp = time + waitTime;
		while (System.currentTimeMillis() < timesUp) {
		}
	}

	// Waits until the player presses the enter key before continuing the
	// program

	public static void pressEnterToContinue() {
		System.out.println("Press Enter key to continue...");
		try {
			System.in.read();
		} catch (Exception e) {
		}
	}

	// Checks for a winner and returns who won

	public void showWinner() {
		Player winner = board.getWinner();
		System.out.println("The winner of the game was " + winner.getName() + "!");
	}
}