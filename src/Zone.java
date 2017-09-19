/*
 * This interface represents a zone bonus that may occupy a node on the board. When a card is placed in the node,
 * the given bonus in applyZoneBonus will be applied, which varies depending on the card placed and the type of Zone it is.
 * 
 * Author: Eric Wahlquist
 */

public interface Zone {

	// Applies this special zone's bonus to the given Card
	public void applyZoneBonus(Card card);

	// Returns the type of this Zone as a String
	public String getType();

}
