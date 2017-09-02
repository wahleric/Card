
public class HotZone implements Zone {

	private String type;
	
	public HotZone() {
		type = "Hot";
	}
	
	public void applyZoneBonus(Card card) {
		if (card.getType().equalsIgnoreCase(type)) { // Card's AP is doubled
			card.setCurrentAP(card.getCurrentUpperAP() * 2, card.getCurrentLowerAP() * 2, card.getCurrentLeftAP() * 2,
					card.getCurrentRightAP() * 2);
		} else if (card.getType().equalsIgnoreCase("Cold")) { // Card's AP is quartered
			card.setCurrentAP(card.getCurrentUpperAP() / 4, card.getCurrentLowerAP() / 4, card.getCurrentLeftAP() / 4,
					card.getCurrentRightAP() / 4);
		} else { // Card's AP is halved
			card.setCurrentAP(card.getCurrentUpperAP() / 2, card.getCurrentLowerAP() / 2, card.getCurrentLeftAP() / 2,
					card.getCurrentRightAP() / 2);
		}
	}
	
	public String getType() {
		return type;
	}

}
