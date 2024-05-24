package org.weekend1.blackjack;

// Card Class
public class Card {

    private final String cardName;
    private final String cardSuit;
    private final int cardValue;

    // constructors

    public Card(String name, String suit, int value) {
        this.cardName = name;
        this.cardSuit = suit;
        this.cardValue = value;
    }

    // gets

    public String getCardName() {
        return this.cardName;
    }
    public String getCardSuit() {
        return this.cardSuit;
    }
    public int getCardValue() {
        return this.cardValue;
    }

    // sets

    // functions

    public void printCardInfo() {
        System.out.println(this.cardName + " of " + this.cardSuit + ": " + this.cardValue);
    }
}