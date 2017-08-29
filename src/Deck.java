import java.util.*;
import java.io.*;

/*
 * This class serves as a deck from which to draw cards in the game. It supports basic deck functions such as adding a card, 
 * drawing a card, shuffling the deck, and checking if the deck is empty.
 * 
 * Author: Eric Wahlquist
 */

public class Deck {

	// List that holds the Cards contained in this Deck

	private List<Card> deck;

	// Creates an empty Deck

	public Deck() {
		deck = new ArrayList<Card>();
	}

	// Adds a Card to the bottom of this Deck

	public void addCard(Card card) {
		deck.add(card);
	}

	// Draws a Card from the top of this Deck

	public Card drawCard() {
		if (isEmpty()) {
			throw new IllegalArgumentException("Deck is empty");
		}
		return deck.remove(0);
	}

	// Shuffles the deck

	public void shuffle() {
		Collections.shuffle(deck);
	}

	// Returns true if this Deck has no cards

	public boolean isEmpty() {
		return deck.isEmpty();
	}
}
