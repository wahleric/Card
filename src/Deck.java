import java.util.*;
import java.io.*;
/*
 * This class serves as a deck from which to draw cards in the game. Upon creation, it auto-generates a database of random
 * monster cards by reading in information from monster txt files. It supports the functions to add a card, draw a card, 
 * and shuffle the deck.
 * 
 * Author: Eric Wahlquist
 */
public class Deck {
	
	private final int NUMBER_OF_CARDS = 100;
	private final int NUMBER_OF_TYPES = 8;
	private final int MINIMUM_HP = 20;
	
	private List<Card> deck;
	private List<String> names;
	private List<String> neutralAdj;
	private List<String> durableAdj;
	private List<String> chargedAdj;
	private List<String> impairedAdj;
	private List<String> feralAdj;
	private List<String> toxicAdj;
	private List<String> hotAdj;
	private List<String> coldAdj;
	
	//Creates a Deck of Cards, each with randomly generated names and stats
	public Deck() throws IOException {
		deck = new ArrayList<Card>();
		names = new ArrayList<String>();
		neutralAdj = new ArrayList<String>();
		durableAdj = new ArrayList<String>();
		chargedAdj = new ArrayList<String>();
		impairedAdj = new ArrayList<String>();
		feralAdj = new ArrayList<String>();
		toxicAdj = new ArrayList<String>();
		hotAdj = new ArrayList<String>();
		coldAdj = new ArrayList<String>();
		generateDeck();
		shuffle();
	}
	
	//Adds a Card to the bottom of this Deck
	public void addCard(Card card) {
		deck.add(card);
	}
	
	//Draws a Card from the top of this Deck
	public Card drawCard() {
		if (isEmpty()) {
			throw new IllegalArgumentException("Deck is empty");
		}
		return deck.remove(0);
	}
	
	//Shuffles the deck
	public void shuffle() {
		Collections.shuffle(deck);
	}
	
	//Returns true if this Deck has no cards
	public boolean isEmpty() {
		return deck.isEmpty();
	}
	
	//Generates a given number of Cards with random names and stats
	private void generateDeck() throws IOException{
		readFromAdj();
		readFromNames();
		for (int i = 0; i < NUMBER_OF_CARDS; i++) {
			Card card = new Card("", "", 0, 0, 0, 0, 0, 0, null);
			pickTypeAndAdjective(card);
			pickName(card);
			Random level = new Random();
			card.setLevel(level.nextInt(5) + 1);
			pickStats(card);
			card.resetCard();
			addCard(card);
		}
	}
	
	//Randomly assigns a type to this Card and adds a corresponding adjective to this Card's name
	private void pickTypeAndAdjective(Card card) {
		Random r = new Random();
		switch (r.nextInt(NUMBER_OF_TYPES)) {
			case 0:
				card.setType("Neutral");
				card.setName(neutralAdj.get(r.nextInt(neutralAdj.size())));
				break;
			case 1:
				card.setType("Durable");
				card.setName(durableAdj.get(r.nextInt(durableAdj.size())));
				break;
			case 2:
				card.setType("Charged");
				card.setName(chargedAdj.get(r.nextInt(chargedAdj.size())));
				break;
			case 3:
				card.setType("Impaired");
				card.setName(impairedAdj.get(r.nextInt(impairedAdj.size())));
				break;
			case 4:
				card.setType("Feral");
				card.setName(feralAdj.get(r.nextInt(feralAdj.size())));
				break;
			case 5:
				card.setType("Toxic");
				card.setName(toxicAdj.get(r.nextInt(toxicAdj.size())));
				break;
			case 6:
				card.setType("Hot");
				card.setName(hotAdj.get(r.nextInt(hotAdj.size())));
				break;
			case 7:
				card.setType("Cold");
				card.setName(coldAdj.get(r.nextInt(coldAdj.size())));
				break;
			default:
				card.setType("ERROR");
				card.setName("ERROR");
		}
	}
	
	//Randomly chooses a noun from the names database for this Card's name
	private void pickName(Card card) {
		Random nameIndex = new Random();
		card.setName(card.getName() + " " + names.get(nameIndex.nextInt(names.size())));
	}
	
	//Assigns stats to a card based upon the Card's level and type
	private void pickStats(Card card) {
		//Apply basic random stats based on the Card level
		Random r = new Random();
		int maxHP = (MINIMUM_HP + r.nextInt(11)) * card.getLevel();
		int upperAP = r.nextInt(10) * card.getLevel();
		int lowerAP = r.nextInt(10) * card.getLevel();
		int leftAP = r.nextInt(10) * card.getLevel();
		int rightAP = r.nextInt(10) * card.getLevel();
		//Durable creatures have double HP and 1/4 AP
		if (card.getType().equals("Durable")) {
			maxHP *= 2;
			upperAP /= 4;
			lowerAP /= 4;
			leftAP /= 4;
			rightAP /= 4;
		}
		//Impaired creatures have all stats quartered
		if (card.getType().equals("Impaired")) {
			maxHP /= 4;
			upperAP /= 4;
			lowerAP /= 4;
			leftAP /= 4;
			rightAP /= 4;
		}
		//Feral creatures have 1/4 HP and double AP
		if (card.getType().equals("Feral")) {
			maxHP /= 4;
			upperAP *= 2;
			lowerAP *= 2;
			leftAP *= 2;
			rightAP *= 2;
		}
		//Toxic monsters have 3/4 HP and AP
		if (card.getType().equals("Toxic")) {
			maxHP = (maxHP * 3) / 4;
			upperAP = (upperAP * 3) / 4;
			lowerAP = (lowerAP * 3) / 4;
			leftAP = (leftAP * 3) / 4;
			rightAP = (rightAP * 3) / 4;
		}
		//Set the calculated stats in the Card
		card.setMaxHP(maxHP);
		card.setInitialAP(upperAP, lowerAP, leftAP, rightAP);
	}
	
	//Private helper method for generateDeck reads adjectives from adjAndType.txt file and places them in their appropriate List
	private void readFromAdj() throws IOException {
		File adjectives = new File("adjAndType.txt");
		Scanner reader = new Scanner(adjectives);
		while (reader.hasNextLine()) {
			String type = reader.next();
			String adjective = reader.next();
			switch (type) {
				case "Neutral":
					neutralAdj.add(adjective);
					break;
				case "Durable":
					durableAdj.add(adjective);
					break;
				case "Charged":
					chargedAdj.add(adjective);
					break;
				case "Impaired":
					impairedAdj.add(adjective);
					break;
				case "Feral":
					feralAdj.add(adjective);
					break;
				case "Toxic":
					toxicAdj.add(adjective);
					break;
				case "Hot":
					hotAdj.add(adjective);
					break;
				case "Cold":
					coldAdj.add(adjective);
					break;
				default:
					throw new IllegalArgumentException("Unrecognized monster type");
			}
			reader.nextLine();
		}
		reader.close();
	}
	
	//Private helper method for generateDeck reads names from names.txt and places them in the names List
	private void readFromNames() throws IOException {
		File namePossibilities = new File("names.txt");
		Scanner reader = new Scanner(namePossibilities);
		while (reader.hasNextLine()) {
			names.add(reader.nextLine());
		}
		reader.close();
	}
}
