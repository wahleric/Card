import java.io.*;

/*
 * This class is a game client for the Card Battle data structures. When run, it will continuously play Card Battle, 
 * resetting after each game until the player decides to quit.
 * 
 * Author: Eric Wahlquist
 */

public class CardBattleConsole {

	// Main method runs Card Battle

	public static void main(String[] args) throws IOException {
		Board board = new Board(new Player("Human"), new Player("Computer"));
		CardBattleIO io = new CardBattleIO(board);
		CardBattleAI ai = new CardBattleAI(board);
		io.intro();
		io.pickDifficulty();
		boolean playAgain = true;
		while (playAgain) { // Go through the game loop until the player decides
							// to stop
			gameLoop(board, ai, io);
			CardBattleIO.wait(2);
			if (ai.getWinner() != null) {
				CardBattleIO.showWinner(ai.getWinner());
			}
			CardBattleIO.wait(2);
			playAgain = CardBattleIO.askToPlayAgain();
		}
		System.out.println("Thank you for playing!");
	}

	// gameLoop controls the loop of each individual match

	public static void gameLoop(Board board, CardBattleAI ai, CardBattleIO io) {
		board.resetBoard();
		ai.generateZoneBonuses();
		ai.initialDraw();
		if (CardBattleIO.rollDice()) { // Human won the toss and has the
										// advantage of going second each turn
			while (ai.getWinner() == null) {
				if (ai.applyImpairedBonus()) {
					CardBattleIO.showImpairedBonus();
					CardBattleIO.wait(2);
				}
				if (ai.applyChargedBonus()) {
					CardBattleIO.showChargedBonus();
					CardBattleIO.wait(2);
				}
				io.printBoard();
				if (!io.boardIsFull()) {
					CardBattleIO.showTurn(board.getComputerPlayer());
					CardBattleIO.wait(2);
					ai.computerTurn();
					io.printBoard();
				}
				CardBattleIO.wait(2);
				if (!io.boardIsFull()) {
					CardBattleIO.showTurn(board.getHumanPlayer());
					CardBattleIO.wait(2);
					io.humanTurn();
					io.printBoard();
				}
				CardBattleIO.wait(2);
				ai.endTurn();
				if (ai.getWinner() != null) {
					io.printBoard();
				} else {
					CardBattleIO.roundOver();
					CardBattleIO.wait(2);
				}
			}
		} else { // Computer won the toss and has the advantage of going second
					// each turn
			while (ai.getWinner() == null) {
				if (ai.applyImpairedBonus()) {
					CardBattleIO.showImpairedBonus();
					CardBattleIO.wait(2);
				}
				if (ai.applyChargedBonus()) {
					CardBattleIO.showChargedBonus();
					CardBattleIO.wait(2);
				}
				io.printBoard();
				if (!io.boardIsFull()) {
					CardBattleIO.showTurn(board.getHumanPlayer());
					CardBattleIO.wait(2);
					io.humanTurn();
					io.printBoard();
				}
				CardBattleIO.wait(2);
				if (!io.boardIsFull()) {
					CardBattleIO.showTurn(board.getComputerPlayer());
					CardBattleIO.wait(2);
					ai.computerTurn();
					io.printBoard();
				}
				CardBattleIO.wait(2);
				ai.endTurn();
				if (ai.getWinner() != null) {
					io.printBoard();
				} else {
					CardBattleIO.roundOver();
					CardBattleIO.wait(2);
				}
			}
		}
	}
}
