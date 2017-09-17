import java.io.*;

public class Test {

	public static void main(String[] args) throws IOException {
//		Player eric = new Player("Eric");
//		Player computer = new Player("Comp");
//		Board board = new Board(eric, computer);
//		Card impaired = new Card("", "Impaired", 1, 300, 1, 1, 1, 1, eric);
//		eric.getHand().add(impaired);
//		for (int i = 0; i < 10; i++) {
//			board.drawCard(computer);
//		}
//		board.placeCard(eric, impaired, 1);
//		board.placeCard(computer, computer.getHand().get(0), 2);
//		System.out.println(board);
//		board.endOfTurn();
//		System.out.println(board);
//		board.endOfTurn();
//		System.out.println(board);
//		board.endOfTurn();
//		System.out.println(board);
//		board.endOfTurn();
//		System.out.println(board);
//		board.endOfTurn();
//		System.out.println(board);
//		board.endOfTurn();
//		System.out.println(board);
//		board.endOfTurn();
//		System.out.println(board);
//		board.endOfTurn();
//		System.out.println(board);
		
//		
//		Deck deck = new Deck(100);
//		for (int i = 0; i < 50; i++) {
//			System.out.println(deck.drawCard());
//		}
		
		CardGenerator gen = new CardGenerator();
		Deck deck = new Deck();
		for (int i = 0; i < 50; i++) {
			deck.addCard(gen.generateRandomCard());
		}
		for (int i = 0; i < 50; i++) {
			System.out.println(deck.drawCard());
		}
	}
}
