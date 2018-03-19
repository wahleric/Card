package Main;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/*
 * This class provides a way to generate random Cards. Options include to generate a random Card of any type or to generate a Card
 * of a specific type. Cards generated are also given random stats that depend on their types.
 * 
 * Author: Eric Wahlquist
 */

public class CardGenerator {

	// General information about monster cards

	private static final int NUMBER_OF_TYPES = 8;
	private static final int MINIMUM_HP = 20;

	// List holds possible monster names

	private List<String> names;

	// Map holds possible monster types as keys, maps them to Lists of all
	// adjectives that correspond to each type

	private Map<String, ArrayList<String>> typesAndAdjectives;

	// Random number generator for picking random Card attributes

	private Random r;

	// Creates a CardGenerator that is ready to create Cards

	public CardGenerator() {
		names = new ArrayList<String>();
		typesAndAdjectives = new HashMap<String, ArrayList<String>>();
		r = new Random();
		readFromAdjectives();
		readFromNames();
	}

	// Generates a random Card of any possible type

	public Card generateRandomCard() {
		return generateRandomCard(null);
	}

	// Generates a random Card of a specific type

	public Card generateRandomCard(String type) {
		Card card = new Card("", "", 0, 0, 0, 0, 0, 0, null);
		if (type == null) { // Choose a random type
			pickType(card);
		} else if (!typesAndAdjectives.containsKey(type)) { // Type is invalid
			throw new IllegalArgumentException("Invalid: Card type does not exist");
		} else { // Apply the given type to the card
			card.setType(type);
		}
		pickAdjective(card);
		pickName(card);
		pickStats(card);
		return card;
	}

	// Randomly assigns a type to this Card

	private void pickType(Card card) {

		String[] typeArray = new String[NUMBER_OF_TYPES];
		typesAndAdjectives.keySet().toArray(typeArray);
		int randomType = r.nextInt(NUMBER_OF_TYPES);
		String type = typeArray[randomType];
		card.setType(type);
	}

	// Chooses a random adjective for the Card's name depending on the Card's
	// type

	private void pickAdjective(Card card) {

		List<String> adjectives = typesAndAdjectives.get(card.getType());
		int randomAdjective = r.nextInt(adjectives.size());
		String adjective = adjectives.get(randomAdjective);
		card.setName(adjective);
	}

	// Randomly chooses a noun from the names database for the Card's name and
	// adds it to the existing adjective

	private void pickName(Card card) {
		card.setName(card.getName() + " " + names.get(r.nextInt(names.size())));
	}

	// Assigns random stats to the Card, which vary based on Card type and level

	private void pickStats(Card card) {

		// Choose a random card level and apply basic random stats based on it

		card.setLevel(r.nextInt(5) + 1);
		int maxHP = (MINIMUM_HP + r.nextInt(11)) * card.getLevel();
		int upperAP = r.nextInt(11) * card.getLevel();
		int lowerAP = r.nextInt(11) * card.getLevel();
		int leftAP = r.nextInt(11) * card.getLevel();
		int rightAP = r.nextInt(11) * card.getLevel();

		// Durable creatures have double HP and 1/4 AP

		if (card.getType().equals("Durable")) {
			maxHP *= 2;
			upperAP /= 4;
			lowerAP /= 4;
			leftAP /= 4;
			rightAP /= 4;
		}

		// Impaired creatures have all stats quartered

		if (card.getType().equals("Impaired")) {
			maxHP /= 4;
			upperAP /= 4;
			lowerAP /= 4;
			leftAP /= 4;
			rightAP /= 4;
		}

		// Feral creatures have 1/4 HP and double AP

		if (card.getType().equals("Feral")) {
			maxHP /= 4;
			upperAP *= 2;
			lowerAP *= 2;
			leftAP *= 2;
			rightAP *= 2;
		}

		// Toxic monsters have 1/2 HP and 1/2 AP

		if (card.getType().equals("Toxic")) {
			maxHP /= 2;
			upperAP /= 2;
			lowerAP /= 2;
			leftAP /= 2;
			rightAP /= 2;
		}

		// Apply the generated stats to the Card

		card.setMaxHP(maxHP);
		card.setInitialAP(upperAP, lowerAP, leftAP, rightAP);
		card.reset();
	}

	// Reads types and adjectives from adjAndType.txt file and organizes them
	// into a map

	private void readFromAdjectives() {
		try {
			File typesAndAdjs = new File("docs/adjAndType.txt");
			Scanner reader = new Scanner(typesAndAdjs);
			while (reader.hasNextLine()) {
				String type = reader.next();
				String adjective = reader.next();
				if (!typesAndAdjectives.containsKey(type)) { // Add the type to
																// the map
					typesAndAdjectives.put(type, new ArrayList<String>());
					typesAndAdjectives.get(type).add(adjective);
				} else { // Add the adjective to the map
					typesAndAdjectives.get(type).add(adjective);
				}
			}
			reader.close();
		} catch (IOException e) {
			System.out.println("Error reading adjAndType.txt");
		}
	}

	// Reads names from names.txt and places them in the names List

	private void readFromNames() {
		try {
			File namePossibilities = new File("docs/names.txt");
			Scanner reader = new Scanner(namePossibilities);
			while (reader.hasNextLine()) {
				names.add(reader.nextLine());
			}
			reader.close();
		} catch (IOException e) {
			System.out.println("Error reading names.txt");
		}

	}

}
