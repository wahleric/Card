/*
 * This interface represents a Zone bonus that may occupy a Node on the Board. When a Card is placed in the Node,
 * the given bonus in applyZoneBonus will be applied, which varies depending on the Card placed and the type of Zone it is.
 * 
 * Author: Eric Wahlquist
 */

public interface Zone {

	// Applies this special Zone's bonus to the given Card
	public void applyZoneBonus(Card card);

	// Returns the type of this Zone as a String
	public String getType();

}
