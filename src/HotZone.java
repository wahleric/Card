/*
 * This class represents a "hot zone" bonus that may occupy a Node on the Board. When a Card is placed in the Node,
 * the given bonus in applyZoneBonus will be applied, which varies depending on the Card placed.
 * 
 * Author: Eric Wahlquist
 */

public class HotZone implements Zone {

	private String type;

	// Default constructor

	public HotZone() {
		type = "Hot";
	}

	// Applies this special zone's bonus to the given Card

	public void applyZoneBonus(Card card) {
		if (card.getType().equalsIgnoreCase("Hot")) { // Card's AP is doubled
			card.setCurrentAP(card.getCurrentUpperAP() * 2, card.getCurrentLowerAP() * 2, card.getCurrentLeftAP() * 2,
					card.getCurrentRightAP() * 2);
		} else if (card.getType().equalsIgnoreCase("Cold")) { // Card's AP is
																// quartered
			card.setCurrentAP(card.getCurrentUpperAP() / 4, card.getCurrentLowerAP() / 4, card.getCurrentLeftAP() / 4,
					card.getCurrentRightAP() / 4);
		} else { // Card's AP is halved
			card.setCurrentAP(card.getCurrentUpperAP() / 2, card.getCurrentLowerAP() / 2, card.getCurrentLeftAP() / 2,
					card.getCurrentRightAP() / 2);
		}
	}

	// Returns the type of this Zone as a String

	public String getType() {
		return type;
	}

}
