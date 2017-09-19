/*
 * This class represents a "cold zone" bonus that may occupy a node on the board. When a card is placed in the node,
 * the given bonus in applyZoneBonus will be applied, which varies depending on the card placed.
 * 
 * Author: Eric Wahlquist
 */

public class ColdZone implements Zone {

	private String type;

	// Default constructor

	public ColdZone() {
		type = "Cold";
	}

	// Applies this special zone's bonus to the given Card

	public void applyZoneBonus(Card card) {
		if (card.getType().equalsIgnoreCase("Cold")) { // Card's HP is doubled
			card.setCurrentHP(card.getCurrentHP() * 2);
		} else if (card.getType().equalsIgnoreCase("Hot")) { // Card's HP is
																// quartered
			card.setCurrentHP(card.getCurrentHP() / 4);
		} else { // Card's HP is halved
			card.setCurrentHP(card.getCurrentHP() / 2);
		}
	}

	// Returns the type of this Zone as a String

	public String getType() {
		return type;
	}
}
