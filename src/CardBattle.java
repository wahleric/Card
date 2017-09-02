import java.io.*;

/*
 * This class is a game client for the Card Battle data structures. When run, it will continuously play Card Battle, 
 * resetting after each game until the player decides to quit.
 * 
 * Author: Eric Wahlquist
 */

public class CardBattle {

	public static final int PLAYER_MAX_HP = 500;

	// Main method runs game
	public static void main(String[] args) throws IOException {
		Board board = new Board(new Player("Human", PLAYER_MAX_HP), new Player("Computer", PLAYER_MAX_HP));
		CardBattleIO io = new CardBattleIO(board);
		io.intro();
		CardBattleAI ai = new CardBattleAI(board, io.pickDifficulty());
		ai.initializeDeck();
		boolean playAgain = true;
		while (playAgain) {
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
