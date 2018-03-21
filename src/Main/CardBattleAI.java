package Main;

import java.util.*;

/*
 * This class contains methods used for calculations in the Card Battle game, including the AI code for the computer's turn as well as
 * end of turn and end of game procedures.
 * 
 * Author: Eric Wahlquist
 */

public class CardBattleAI {

    private final int INITIAL_DEAL_NUMBER = 5;

    private Board board;
    private Random r;

    // Private default constructor prevents an invalid CardBattleAI from being
    // created
    @SuppressWarnings("unused")
    private CardBattleAI() {
    }

    // Creates a CardBattleAI from a given board and difficulty
    public CardBattleAI(Board board) {
        this.board = board;
        r = new Random();
    }

    // Draws INITIAL_DEAL_NUMBER Cards for each player
    public void initialDraw() {
        for (int i = 0; i < INITIAL_DEAL_NUMBER; i++) {
            board.drawCard(board.getHumanPlayer());
            board.drawCard(board.getComputerPlayer());
        }
    }

    // Ends the turn on the Board and deals the appropriate damage to each Card
    // on the Board.
    public void endTurn() {

        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 5; column++) {

                // For each Node:
                // Get information about the Card at the Node
                Card card = board.getCard(row, column);

                // If the Card exists, deal appropriate damage to adjacent Cards
                if (card != null) {
                    Player owner = card.getOwner();

                    // Deal attacks to any enemy Card above this Card and apply any
                    // contamination
                    if (row > 0) {
                        Card cardAbove = board.getCard(row - 1, column);
                        if (!(cardAbove == null) && cardAbove.getOwner() != owner) {
                            cardAbove.subtractHP(card.getCurrentUpperAP());
                            if (card.getType().equals("Toxic")) {
                                cardAbove.setContaminatedTurnsLeft(5);
                            }
                        }
                    }

                    // Deal attacks to any enemy Card to the left of this Card and
                    // apply any contamination
                    if (column > 0) {
                        Card cardLeft = board.getCard(row, column - 1);
                        if (!(cardLeft == null) && cardLeft.getOwner() != owner) {
                            cardLeft.subtractHP(card.getCurrentLeftAP());
                            if (card.getType().equals("Toxic")) {
                                cardLeft.setContaminatedTurnsLeft(5);
                            }
                        }
                    }

                    // Deal attacks to any enemy Card to the right of this Card and
                    // apply any contamination
                    if (column < 4) {
                        Card cardRight = board.getCard(row, column + 1);
                        if (!(cardRight == null) && cardRight.getOwner() != owner) {
                            cardRight.subtractHP(card.getCurrentRightAP());
                            if (card.getType().equals("Toxic")) {
                                cardRight.setContaminatedTurnsLeft(5);
                            }
                        }
                    }

                    // Deal attacks to any enemy Card below this Card and apply any
                    // contamination
                    if (row < 4) {
                        Card cardBelow = board.getCard(row + 1, column);
                        if (!(cardBelow == null) && cardBelow.getOwner() != owner) {
                            cardBelow.subtractHP(card.getCurrentLowerAP());
                            if (card.getType().equals("Toxic")) {
                                cardBelow.setContaminatedTurnsLeft(5);
                            }
                        }
                    }
                    
                    // Deal any contamination damage to this Card
                    if (card.getContaminatedTurnsLeft() > 0) {
                        int contaminationDamage = r.nextInt(5) + 3;
                        int turnsLeft = card.getContaminatedTurnsLeft();
                        card.subtractHP(contaminationDamage);
                        card.setContaminatedTurnsLeft(--turnsLeft);
                    }
                }
            }
        }

        // After all damage is dealt across the board, remove any dead monsters
        // and apply the proper damage to their owners
        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 5; column++) {
                Card card = board.getCard(row, column);
                if (card != null && card.getCurrentHP() <= 0) {
                    card.getOwner().subtractHP(card.getMaxHP());
                    board.removeCard(row, column);
                }
            }
        }

        // Increment the turn counter
        board.incrementTurn();
    }

    // Initiates the AI for the computer's turn, depending on the chosen
    // difficulty level
    public void computerTurn() {
        if (board.getDifficulty().equalsIgnoreCase("Easy")) {
            easyAI();
        } else {
            mediumAndHardAI();
        }
        board.drawCard(board.getComputerPlayer());
    }

    // Provides an AI with easy difficulty (Random Card and Node choices)
    private void easyAI() {
        Card cardToPlay = null;
        int slot = -1;
        do {
            slot = r.nextInt(25);
            int randomCardIndex = r.nextInt(board.getComputerPlayer().getHand().size());
            cardToPlay = board.getComputerPlayer().getHand().get(randomCardIndex);
        } while (board.placeCard(cardToPlay, slot / 5, slot % 5));
    }

    // Provides an AI with medium difficulty (Calculates damage given vs. damage
    // taken)
    private void mediumAndHardAI() {
        Card cardToPlay = null;
        int rowToPlay = -1;
        int columnToPlay = -1;
        int maxScore = Integer.MIN_VALUE;

        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 5; column++) {
                Card cardInNode = board.getCard(row, column);
                if (cardInNode == null) {
                    for (Card card : board.getComputerPlayer().getHand()) {
                        int damageGiven = 0;
                        int damageTaken = 0;
                        int hardBonus = 0;
                        int score;

                        // Scan enemies above
                        if (row > 0) {
                            Card cardAbove = board.getCard(row - 1, column);
                            if (cardAbove != null && cardAbove.getOwner() != board.getComputerPlayer()) {
                                damageGiven += Math.min(card.getCurrentUpperAP(), cardAbove.getCurrentHP());
                                damageTaken += Math.min(cardAbove.getCurrentLowerAP(), card.getCurrentHP());
                            }
                        }

                        // Scan enemies to the left
                        if (column > 0) {
                            Card cardLeft = board.getCard(row, column - 1);
                            if (cardLeft != null && cardLeft.getOwner() != board.getComputerPlayer()) {
                                damageGiven += Math.min(card.getCurrentLeftAP(), cardLeft.getCurrentHP());
                                damageTaken += Math.min(cardLeft.getCurrentRightAP(), card.getCurrentHP());
                            }
                        }

                        // Scan enemies to the right
                        if (column < 4) {
                            Card cardRight = board.getCard(row, column + 1);
                            if (cardRight != null && cardRight.getOwner() != board.getComputerPlayer()) {
                                damageGiven += Math.min(card.getCurrentRightAP(), cardRight.getCurrentHP());
                                damageTaken += Math.min(cardRight.getCurrentLeftAP(), card.getCurrentHP());
                            }
                        }

                        // Scan enemies below
                        if (row < 4) {
                            Card cardBelow = board.getCard(row + 1, column);
                            if (cardBelow != null && cardBelow.getOwner() != board.getComputerPlayer()) {
                                damageGiven += Math.min(card.getCurrentLowerAP(), cardBelow.getCurrentHP());
                                damageTaken += Math.min(cardBelow.getCurrentUpperAP(), card.getCurrentHP());
                            }
                        }

                        // If the difficulty is "Hard", apply additional checks
                        if (board.getDifficulty().equalsIgnoreCase("Hard")) {
                            hardBonus = hardAI(row, column, card);
                        }

                        // Calculate the total score of the Card/Node combination
                        // and check if it is the best combination so far
                        score = (damageGiven - damageTaken) + hardBonus;
                        if (score > maxScore) {
                            cardToPlay = card;
                            rowToPlay = row;
                            columnToPlay = column;
                            maxScore = score;
                        }
                    }
                }
            }
        }
        board.placeCard(cardToPlay, rowToPlay, columnToPlay);
    }

    // Provides an AI with hard difficulty by taking into account more factors
    // in the placement of cards
    private int hardAI(int row, int column, Card card) {

        int potentialDamage = card.getCurrentUpperAP() + card.getCurrentLowerAP() + card.getCurrentLeftAP()
                + card.getCurrentRightAP();

        // Check nodes above for wasted potential damage
        if (row == 0) {
            potentialDamage -= card.getCurrentUpperAP();
        } else {
            Card cardAbove = board.getCard(row - 1, column);
            if (cardAbove != null && cardAbove.getOwner() == board.getComputerPlayer()) {
                potentialDamage -= card.getCurrentUpperAP();
            }
        }

        // Check nodes below for wasted potential damage
        if (row == 4) {
            potentialDamage -= card.getCurrentLowerAP();
        } else {
            Card cardBelow = board.getCard(row + 1, column);
            if (cardBelow != null && cardBelow.getOwner() == board.getComputerPlayer()) {
                potentialDamage -= card.getCurrentLowerAP();
            }
        }

        // Check nodes to left for wasted potential damage
        if (column == 0) {
            potentialDamage -= card.getCurrentLeftAP();
        } else {
            Card cardLeft = board.getCard(row, column - 1);
            if (cardLeft != null && cardLeft.getOwner() == board.getComputerPlayer()) {
                potentialDamage -= card.getCurrentLeftAP();
            }
        }

        // Check nodes to right for wasted potential damage
        if (column == 4) {
            potentialDamage -= card.getCurrentRightAP();
        } else {
            Card cardRight = board.getCard(row, column + 1);
            if (cardRight != null && cardRight.getOwner() == board.getComputerPlayer()) {
                potentialDamage -= card.getCurrentRightAP();
            }
        }

        return potentialDamage;
    }

    // Randomly has a chance to generate a bonus for any impaired monsters on
    // the board. Returns true if it happens, false otherwise
    public boolean applyImpairedBonus() {
        boolean bonusHappened = false;
        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 5; column++) {
                Card card = board.getCard(row, column);
                if (card != null && card.getType().equals("Impaired")) {
                    if (r.nextDouble() > 0.80) {
                        Card cardToAdd = board.drawCard(null);
                        card.setCurrentHP(card.getCurrentHP() + (cardToAdd.getMaxHP() / 2));
                        card.setCurrentAP(card.getCurrentUpperAP() + (cardToAdd.getCurrentUpperAP() / 2),
                                card.getCurrentLowerAP() + (cardToAdd.getCurrentLowerAP() / 2),
                                card.getCurrentLeftAP() + (cardToAdd.getCurrentLeftAP() / 2),
                                card.getCurrentRightAP() + (cardToAdd.getCurrentRightAP() / 2));
                        board.addCardToDeck(cardToAdd);
                        bonusHappened = true;
                    }
                }
            }
        }
        return bonusHappened;
    }

    // Applies a bonus to the AP of any charged monsters on the board that are
    // placed next to other charged monsters of the same team. Returns true if
    // it happens, false otherwise
    public boolean applyChargedBonus() {
        boolean bonusHappened = false;
        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 5; column++) {
                Card card = board.getCard(row, column);
                if (card != null && card.getType().equals("Charged")) {

                    // Check if the player has a charged monster to the left
                    if (column > 0) {
                        Card cardLeft = board.getCard(row, column - 1);
                        if (cardLeft != null && cardLeft.getType().equalsIgnoreCase("Charged")
                                && cardLeft.getOwner() == card.getOwner()) {
                            card.addAP(2);
                            cardLeft.addAP(2);
                            bonusHappened = true;
                        }
                    }

                    // Check if the player has a charged monster to the right
                    if (column < 4) {
                        Card cardRight = board.getCard(row, column + 1);
                        if (cardRight != null && cardRight.getType().equalsIgnoreCase("Charged")
                                && cardRight.getOwner() == card.getOwner()) {
                            card.addAP(2);
                            cardRight.addAP(2);
                            bonusHappened = true;
                        }
                    }

                    // Check if the player has a charged monster above
                    if (row > 0) {
                        Card cardAbove = board.getCard(row - 1, column);
                        if (cardAbove != null && cardAbove.getType().equalsIgnoreCase("Charged")
                                && cardAbove.getOwner() == card.getOwner()) {
                            card.addAP(2);
                            cardAbove.addAP(2);
                            bonusHappened = true;
                        }
                    }

                    // Check if the player has a charged monster below
                    if (row < 4) {
                        Card cardBelow = board.getCard(row + 1, column);
                        if (cardBelow != null && cardBelow.getType().equalsIgnoreCase("Charged")
                                && cardBelow.getOwner() == card.getOwner()) {
                            card.addAP(2);
                            cardBelow.addAP(2);
                            bonusHappened = true;
                        }
                    }
                }
            }
        }
        return bonusHappened;
    }

    // If the game is over, returns the Player who has won. Otherwise, returns a
    // new Player named "a tie" if the game was
    // a tie, or returns null if the game is not over
    public Player getWinner() {
        if (board.getHumanPlayer().getHP() > 0 && board.getComputerPlayer().getHP() <= 0) {
            return board.getHumanPlayer();
        }
        if (board.getHumanPlayer().getHP() <= 0 && board.getComputerPlayer().getHP() > 0) {
            return board.getComputerPlayer();
        }
        if (board.getHumanPlayer().getHP() <= 0 && board.getComputerPlayer().getHP() <= 0) {
            return new Player("a tie");
        }
        return null;
    }

    // Traverses the nodes on the Board and randomly creates bonus zones
    public void generateZoneBonuses() {

        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 5; column++) {

                // 10% chance for each Node to have a zone bonus of some type
                if (r.nextDouble() > 0.9) {
                    int zoneChoice = r.nextInt(2);
                    if (zoneChoice == 1) {
                        board.generateZoneBonus(new HotZone(), row, column);
                    } else {
                        board.generateZoneBonus(new ColdZone(), row, column);
                    }
                }
            }
        }
    }
}
