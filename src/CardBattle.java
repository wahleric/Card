import java.util.*;
import java.io.*;

/*
 * This class is a game client for the Card Battle data structures. When run, it will continuously play the game, 
 * resetting after each game until the player decides to quit.
 * 
 * Author: Eric Wahlquist
 */


public class CardBattle {

	//Main method runs game
	public static void main(String[] args) throws IOException {
		Scanner keyboard = new Scanner(System.in);
		intro(keyboard);
		Player human = getPlayerName(keyboard);
		Player computer = new Player("Computer");
		
	}
	
	//Prints the introduction to the console
	public static void intro(Scanner keyboard) throws IOException {
		System.out.println("Welcome to Card Battle! Would you like to read the instructions (y/n)?");
		String answer = keyboard.nextLine();
		while (!(answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n"))) {
			System.out.println("Please type y for \"yes\" or n for \"no\". Would you like to read the instructions (y/n)?");
			answer = keyboard.next();
		}
		if (answer.equalsIgnoreCase("y")) {
			instructions();
			pressEnterToContinue();
		}
	}
	
	public static void instructions() throws IOException {
		File file = new File("Instructions.txt");
		Scanner reader = new Scanner(file);
		while (reader.hasNextLine()) {
			String line = reader.nextLine();
			System.out.println(line);
		}
	}
	
	public static Player getPlayerName(Scanner keyboard) {
		System.out.println("What is your name?");
		String name = keyboard.nextLine();
		System.out.println(name);
		return new Player(name);
	}
	
	public static void wait(int seconds) {
		long waitTime = seconds * 1000;
		long time = System.currentTimeMillis();
		long timesUp = time + waitTime;
		while (System.currentTimeMillis() < timesUp) {
		}
	}
	
	public static void pressEnterToContinue() {
		System.out.println("Press Enter key to continue...");
		try { 
			System.in.read();
		} catch(Exception e){}
	}
}
