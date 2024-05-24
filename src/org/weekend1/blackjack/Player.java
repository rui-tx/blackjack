package org.weekend1.blackjack;

// Player class
public class Player {

    private final String name;
    private final int cautionLevel = Random.getRandomNumber(1, 5);
    private Card[] playerHandDeck = new Card[0];
    private int currentHandValue = 0;

    // constructors

    public Player(String name) {
        this.name = name;
    }

    // gets

    public String getName() {
        return this.name;
    }

    public int getNumberOfCurrentCard() {
        return this.playerHandDeck.length;
    }

    // sets

    public void resetPlayerHand() {
        this.playerHandDeck = new Card[0];
    }

    // functions

    public void giveCardToPlayerHandDeck(Card card) {

        // create new array with 1 more position
        Card[] newPlayerHandDeck = new Card[this.playerHandDeck.length + 1];

        // copy old values
        for (int i = 0; i < this.playerHandDeck.length; i++) {
            newPlayerHandDeck[i] = this.playerHandDeck[i];
        }

        // push value to last position
        newPlayerHandDeck[newPlayerHandDeck.length - 1] = card;

        // update player with the new hand deck
        this.playerHandDeck = newPlayerHandDeck;
    }

    public int checkHandValue() {

        // resets value
        this.currentHandValue = 0;
        // sums current player hand
        for (int i = 0; i < this.playerHandDeck.length; i++) {
            this.currentHandValue += this.playerHandDeck[i].getCardValue();
        }

        return this.currentHandValue;
    }

    public void showPlayerHand() {
        System.out.println(this.name + " hand");
        for (int i = 0; i < this.playerHandDeck.length; i++) {
            System.out.print("Card [" + i + "] : ");
            this.playerHandDeck[i].printCardInfo();
        }
        System.out.println("Sum of it: " + this.checkHandValue() + "\n");
    }

    public boolean wantToPlay() {
        if ( (21 - this.currentHandValue ) <= this.cautionLevel ) {
            System.out.println(">> " + this.name + " is okay with the current hand. <<");
            return false;
        }
        return true;
    }
}