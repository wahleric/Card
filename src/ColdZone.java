
public class ColdZone implements Zone {
	
	private String type;

	public ColdZone() {
		type = "Cold";
	}
	
	public void applyZoneBonus(Card card) {
		if (card.getType().equalsIgnoreCase("Cold")) { // Card's HP is doubled
			card.setCurrentHP(card.getCurrentHP() * 2);
		} else if (card.getType().equalsIgnoreCase("Hot")) { // Card's HP is quartered
			card.setCurrentHP(card.getCurrentHP() / 4);
		} else { // Card's HP is halved
			card.setCurrentHP(card.getCurrentHP() / 2);
		}
	}
	
	public String getType() {
		return type;
	}
}
