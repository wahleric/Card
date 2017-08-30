import java.io.IOException;
import java.util.*;

/*
 * This class contains methods used for calculations in the Card Battle game, including the AI code for the computer's turn as well as
 * end of turn and end of game procedures.
 */

public class CardBattleAI {
	
	private final int INITIAL_DEAL_NUMBER = 5;
	private static final int CARDS_IN_DECK = 100;

	private String difficulty;
	private Board board;
	private Random r;

	private CardBattleAI() {
	}

	public CardBattleAI(Board board, String difficulty) {
		if (!(difficulty.equalsIgnoreCase("Easy") || difficulty.equalsIgnoreCase("Medium")
				|| difficulty.equalsIgnoreCase("Hard"))) {
			throw new IllegalArgumentException("Invalid difficulty");
		}
		this.difficulty = difficulty;
		this.board = board;
		r = new Random();
	}

	// Initializes the board deck with a given number of Cards

	public void initializeDeck() throws IOException {
		CardGenerator generator = new CardGenerator();
		for (int cardNumber = 1; cardNumber <= CARDS_IN_DECK; cardNumber++) {
			board.addCard(generator.generateRandomCard());
		}
	}

	// Draws INITIAL_DEAL_NUMBER Cards for each player

	public void initialDraw() {
		for (int i = 0; i < INITIAL_DEAL_NUMBER; i++) {
			board.drawCard(board.getHumanPlayer());
			board.drawCard(board.getComputerPlayer());
		}
	}

	// Ends the turn on the Board and deals the appropriate damage to each Card
	// on the Board.

	public void endTurn() {
		for (int nodeNumber = 1; nodeNumber < 26; nodeNumber++) {
			// For each Node:
			// Get information about the Card at the Node
			Card card = board.getCardAtNode(nodeNumber);
			// If the Card exists, deal appropriate damage to adjacent Cards
			if (card != null) {
				Player owner = card.getOwner();
				// Deal attacks to any enemy Card below this Card and apply any
				// contamination
				if (nodeNumber > 5) {
					Card cardAbove = board.getCardAtNode(nodeNumber - 5);
					if (!(cardAbove == null) && cardAbove.getOwner() != owner) {
						cardAbove.subtractHP(card.getCurrentUpperAP());
						if (card.getType().equals("Toxic")) {
							cardAbove.setContaminatedTurnsLeft(5);
						}
					}
				}

				// Deal attacks to any enemy Card to the left of this Card and
				// apply any contamination
				if ((nodeNumber - 1) % 5 != 0) {
					Card cardLeft = board.getCardAtNode(nodeNumber - 1);
					if (!(cardLeft == null) && cardLeft.getOwner() != owner) {
						cardLeft.subtractHP(card.getCurrentLeftAP());
						if (card.getType().equals("Toxic")) {
							cardLeft.setContaminatedTurnsLeft(5);
						}
					}
				}

				// Deal attacks to any enemy Card to the right of this Card and
				// apply any contamination
				if (nodeNumber % 5 != 0) {
					Card cardRight = board.getCardAtNode(nodeNumber + 1);
					if (!(cardRight == null) && cardRight.getOwner() != owner) {
						cardRight.subtractHP(card.getCurrentRightAP());
						if (card.getType().equals("Toxic")) {
							cardRight.setContaminatedTurnsLeft(5);
						}
					}
				}

				// Deal attacks to any enemy Card below this Card
				if (nodeNumber < 21) {
					Card cardBelow = board.getCardAtNode(nodeNumber + 5);
					if (!(cardBelow == null) && cardBelow.getOwner() != owner) {
						cardBelow.subtractHP(card.getCurrentLowerAP());
						if (card.getType().equals("Toxic")) {
							cardBelow.setContaminatedTurnsLeft(5);
						}
					}
				}
			}
		}

		// Deal any contamination damage across the board (3-7 HP randomly per
		// turn)
		for (int nodeNumber = 1; nodeNumber < 26; nodeNumber++) {
			Card card = board.getCardAtNode(nodeNumber);
			int contaminationDamage = r.nextInt(5) + 3;
			if (card != null && card.getContaminatedTurnsLeft() > 0) {
				int turnsLeft = card.getContaminatedTurnsLeft();
				card.subtractHP(contaminationDamage);
				card.setContaminatedTurnsLeft(--turnsLeft);
			}
		}

		// After all damage is dealt across the board, remove any dead monsters
		// and apply the proper damage to their owners
		for (int nodeNumber = 1; nodeNumber < 26; nodeNumber++) {
			Card card = board.getCardAtNode(nodeNumber);
			if (card != null && card.getCurrentHP() <= 0) {
				card.getOwner().subtractHP(card.getMaxHP());
				board.removeCard(nodeNumber);
			}
		}
		// Increment the turn counter
		board.incrementTurn();
	}

	public void computerTurn() {
		if (difficulty.equalsIgnoreCase("Easy")) {
			easyAI();
		} else if (difficulty.equalsIgnoreCase("Medium")) {
			mediumAI();
		} else {
			hardAI();
		}
		board.drawCard(board.getComputerPlayer());
	}

	// Provides an AI with easy difficulty (Random Card and Node choices)

	private void easyAI() {
		int nodeToPlace = r.nextInt(25) + 1;
		int randomCardIndex = r.nextInt(board.getComputerPlayer().getHand().size());
		Card cardToPlace = board.getComputerPlayer().getHand().get(randomCardIndex);
		board.placeCard(board.getComputerPlayer(), cardToPlace, nodeToPlace);
	}

	// Provides an AI with medium difficulty (Calculates damage given vs. damage
	// taken)

	private void mediumAI() {
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
					
					// Scan enemies above
					if (nodeNumber > 5) {
						Card cardAbove = board.getCardAtNode(nodeNumber - 5);
						if (cardAbove != null && cardAbove.getOwner() != board.getComputerPlayer()) {
							damageGiven += Math.min(card.getCurrentUpperAP(), cardAbove.getCurrentHP());
							damageTaken += Math.min(cardAbove.getCurrentLowerAP(), card.getCurrentHP());
						}
					}
					
					// Scan enemies to the left
					if ((nodeNumber - 1) % 5 != 0) {
						Card cardLeft = board.getCardAtNode(nodeNumber - 1);
						if (cardLeft != null && cardLeft.getOwner() != board.getComputerPlayer()) {
							damageGiven += Math.min(card.getCurrentLeftAP(), cardLeft.getCurrentHP());
							damageTaken += Math.min(cardLeft.getCurrentRightAP(), card.getCurrentHP());
						}
					}
					
					// Scan enemies to the right
					if ((nodeNumber % 5) != 0) {
						Card cardRight = board.getCardAtNode(nodeNumber + 1);
						if (cardRight != null && cardRight.getOwner() != board.getComputerPlayer()) {
							damageGiven += Math.min(card.getCurrentRightAP(), cardRight.getCurrentHP());
							damageTaken += Math.min(cardRight.getCurrentLeftAP(), card.getCurrentHP());
						}
					}
					
					// Scan enemies below
					if (nodeNumber < 21) {
						Card cardBelow = board.getCardAtNode(nodeNumber + 5);
						if (cardBelow != null && cardBelow.getOwner() != board.getComputerPlayer()) {
							damageGiven += Math.min(card.getCurrentLowerAP(), cardBelow.getCurrentHP());
							damageTaken += Math.min(cardBelow.getCurrentUpperAP(), card.getCurrentHP());
						}
					}
					
					score = damageGiven - damageTaken;
					if (score > maxScore) {
						cardToPlay = card;
						nodeToPlay = nodeNumber;
						maxScore = score;
					}
				}
			}
		}
		board.placeCard(board.getComputerPlayer(), cardToPlay, nodeToPlay);
	}

	private void hardAI() {

	}

	// Randomly has a chance to generate a bonus for any impaired monsters on
	// the board

	public void checkImpairedBonus() {
		for (int nodeNumber = 1; nodeNumber < 26; nodeNumber++) {
			Card card = board.getCardAtNode(nodeNumber);
			if (card != null && card.getType().equals("Impaired")) {
				Random r = new Random();
				if (r.nextDouble() > 0.7) {
					Card cardToAdd = board.drawCard(null);
					System.out.println("A " + cardToAdd.getName() + " sees the impaired " + card.getName()
							+ " and comes to its aid!");
					card.setCurrentHP(card.getCurrentHP() + (cardToAdd.getMaxHP() / 2));
					card.setCurrentAP(card.getCurrentUpperAP() + (cardToAdd.getCurrentUpperAP() / 2),
							card.getCurrentLowerAP() + (cardToAdd.getCurrentLowerAP() / 2),
							card.getCurrentLeftAP() + (cardToAdd.getCurrentLeftAP() / 2),
							card.getCurrentRightAP() + (cardToAdd.getCurrentRightAP() / 2));
					board.addCard(cardToAdd);
				}
			}
		}
	}

	// Waits the given number of seconds before continuing the program

	public static void wait(int seconds) {
		long waitTime = seconds * 1000;
		long time = System.currentTimeMillis();
		long timesUp = time + waitTime;
		while (System.currentTimeMillis() < timesUp) {
		}
	}
}
