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

	// Total number of different possible Card types

	private static final int NUMBER_OF_TYPES = 8;

	// Minimum HP of any monster Card

	private static final int MINIMUM_HP = 20;

	// List that holds possible monster names

	private List<String> names;

	// Map holds possible monster types as keys, maps them to Lists of all
	// adjectives that correspond to each type

	private Map<String, ArrayList<String>> typesAndAdjectives;

	// Creates a CardGenerator that is ready to create Cards

	public CardGenerator() throws IOException {
		names = new ArrayList<String>();
		typesAndAdjectives = new HashMap<String, ArrayList<String>>();
		readFromAdj();
		readFromNames();
	}

	// Generates a random Card of any possible type
	public Card generateRandomCard() {
		return generateRandomCard(null);
	}

	// Generates a random Card of a specific type
	public Card generateRandomCard(String type) {
		Card card = new Card("", "", 0, 0, 0, 0, 0, 0, null);
		if (type == null) {
			pickType(card);
		} else if (!typesAndAdjectives.containsKey(type)) {
			throw new IllegalArgumentException("Invalid: Card type does not exist");
		} else {
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
		Random r = new Random();
		int randomType = r.nextInt(NUMBER_OF_TYPES);
		String type = typeArray[randomType];
		card.setType(type);
	}

	// Chooses a random adjective for the Card's name depending on the Card's
	// type

	private void pickAdjective(Card card) {

		Random r = new Random();
		List<String> adjectives = typesAndAdjectives.get(card.getType());
		int randomAdjective = r.nextInt(adjectives.size());
		String adjective = adjectives.get(randomAdjective);
		card.setName(adjective);
	}

	// Randomly chooses a noun from the names database for this Card's name

	private void pickName(Card card) {
		Random nameIndex = new Random();
		card.setName(card.getName() + " " + names.get(nameIndex.nextInt(names.size())));
	}

	private void pickStats(Card card) {

		// Apply basic random stats based on the Card level

		Random r = new Random();
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

	private void readFromAdj() throws IOException {
		File typesAndAdjs = new File("docs/adjAndType.txt");
		Scanner reader = new Scanner(typesAndAdjs);
		while (reader.hasNextLine()) {
			String type = reader.next();
			String adjective = reader.next();
			if (!typesAndAdjectives.containsKey(type)) {
				typesAndAdjectives.put(type, new ArrayList<String>());
				typesAndAdjectives.get(type).add(adjective);
			} else {
				typesAndAdjectives.get(type).add(adjective);
			}
		}
		reader.close();
	}

	// Reads names from names.txt and places them in the names List

	private void readFromNames() throws IOException {
		File namePossibilities = new File("docs/names.txt");
		Scanner reader = new Scanner(namePossibilities);
		while (reader.hasNextLine()) {
			names.add(reader.nextLine());
		}
		reader.close();
	}

}
