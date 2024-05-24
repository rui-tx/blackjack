package org.weekend1.blackjack;

// BlackJack class
public class BlackJack_working {

    // dica do Bruno, subclass estática 'Result'
    public static class Result {
        public final static int P1_WIN = 1;
        public final static int P2_WIN = 2;
        public final static int DRAW = 0;
        public final static int GAME_CONTINUES = -1;
        public final static int GAME_ENDED = -2;
        public final static int GAME_NOT_INITIALIZED = -3;
    }

    private final String[] cardSuits = {"Hearts", "Diamonds", "Clubs", "Spades" }; // length 4
    private final String[] cardNames = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10" , "Jack", "Queen", "King" }; // length 13
    private final Card[] cardDeck = new Card[cardSuits.length * cardNames.length]; // 52
    private final int shuffleTimes = 2500;
    private int currentCardDeckPositionForRandomPick = 0;
    private int roundNumber = 1;
    private boolean gameEnded;
    private final Player p1;
    private final Player p2;
    private final int initialNumberOfCardsForPlayers = 2;

    // constructors

    public BlackJack_working(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;

        //this.initGame();
    }

    // gets

    public String getPlayer1Name() {
        return this.p1.getName();
    }

    public String getPlayer2Name() {
        return this.p2.getName();
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    // sets


    // functions

    @Override
    public String toString() {
        String line1 = ">>> Blackjack Game Values <<<\n";
        String line2 = "p1 name: "+ this.p1.getName() + "; p2 name: " + this.p2.getName() + "\n";
        String line3 = "Game ended: "+ this.isGameEnded() + " Current/Last round: " + this.roundNumber + "\n";
        return line1 + line2 + line3;
    }

    // play 1 round of blackjack the game
    public int play() {

        // if game has ended, can't play
        if (this.gameEnded) {
            return Result.GAME_ENDED;
        }

        // if card deck is null (the 1st card) then game was not initialized
        if (this.cardDeck[0] == null) {
            this.gameEnded = true;
            return Result.GAME_NOT_INITIALIZED;
        }

        // if number of cards for player is 0 then game was not initialized
        if (this.p1.getNumberOfCurrentCard() == 0 || this.p2.getNumberOfCurrentCard() == 0) {
            this.gameEnded = true;
            return Result.GAME_NOT_INITIALIZED;
        }

        // game starts in a continue state -1
        int winner;

        //System.out.println(">> Round " + roundNumber + " <<");
        this.showCurrentPlayersHands();

        // basic blackjack rules
        winner = this.checkSimpleGameRules();
        if (winner != Result.DRAW) {
            this.gameEnded = true;
            return winner;
        }

        // Custom rule 1 -> if p1 decides not to play and the p2 get a higher hand value then p1 loses
        // BAD NAME! change this
        winner = this.checkCustomRule1();
        if (winner != Result.DRAW) {
            this.gameEnded = true;
            return winner;
        }

        //Custom rule 2 -> players can decide if they don't want to play ( because they think they have a good hand)
        // if nether of them wants to play, it's a draw (bug or feature idk)
        if (this.p1.wantToPlay()) {
            this.p1.giveCardToPlayerHandDeck(this.pickCardFromDeck());
        } else {
            if (!this.p2.wantToPlay()) {
                System.out.println(">> " + this.p1.getName() + " and " + this.p2.getName() + " don't want to play... Ending round. <<");
                return Result.DRAW;
            }
        }

        if (this.p2.wantToPlay()) {
            this.p2.giveCardToPlayerHandDeck(this.pickCardFromDeck());
        } else {
            if (!this.p1.wantToPlay()) {
                System.out.println(">> " + this.p1.getName() + " and " + this.p2.getName() + " don't want to play... Ending round. <<");
                return Result.DRAW;
            }
        }

        /*
        // XOR
        if (this.p1.wantToPlay() ^ this.p2.wantToPlay()) {

            // if p1 wants to play, so p2 does NOT
            if (this.p1.wantToPlay() ) {

                if(this.p2.checkHandValue() > this.p1.checkHandValue() && !this.p2.wantToPlay()) {
                    this.p2.giveCardToPlayerHandDeck(this.pickCardFromDeck());
                    this.roundNumber++;
                    return Result.GAME_CONTINUES;
                }

                System.out.println(">> " + this.p2.getName() + " is okay with the current hand and stops getting more cards. <<\n");

            }

            // if p2 wants to play, so p1 does NOT
            if (this.p2.wantToPlay()) {
                System.out.println(">> " + this.p1.getName() + " is okay with the current hand and stops getting more cards. <<\n");

                if(this.p1.checkHandValue() > this.p2.checkHandValue() && !this.p1.wantToPlay()) {
                    this.p1.giveCardToPlayerHandDeck(this.pickCardFromDeck());
                    this.roundNumber++;
                    return Result.GAME_CONTINUES;
                }

                System.out.println(">> " + this.p1.getName() + " is okay with the current hand and stops getting more cards. <<\n");
            }
        }

        if (this.p1.wantToPlay()) {
            this.p1.giveCardToPlayerHandDeck(this.pickCardFromDeck());
        } else {
            System.out.println(">> " + this.p1.getName() + " is okay with the current hand and stops getting more cards. <<\n");
            if (!this.p2.wantToPlay()) {
                System.out.println(">> " + this.p1.getName() + " and " + this.p2.getName() + " don't want to play... Ending round. <<");
                this.gameEnded = true;
                return Result.DRAW;
            }
        }

        if (this.p2.wantToPlay()) {
            this.p2.giveCardToPlayerHandDeck(this.pickCardFromDeck());
        } else {
            System.out.println(">> " + this.p2.getName() + " is okay with the current hand and stops getting more cards. <<\n");
            if (!this.p1.wantToPlay()) {
                System.out.println(">> " + this.p1.getName() + " and " + this.p2.getName() + " don't want to play... Ending round. <<");
                this.gameEnded = true;
                return Result.DRAW;
            }
        }
        */


        this.roundNumber++;
        return Result.GAME_CONTINUES;
    }

    // initializes the game
    public void initGame() {

        // resets end game state
        this.gameEnded = false;
        this.roundNumber = 1;

        // resets player hands
        this.resetPlayerHands();

        // make card deck
        this.makeCardDeck();

        // and shuffles it
        this.shuffleCardDeck();
        this.shuffleCardDeckRecursive(this.shuffleTimes); // just testing

        // players start with n random cards
        this.giveInitialCardsToPlayers(this.initialNumberOfCardsForPlayers);
    }

    // make card deck - should create suit of hearts of all card names, then diamonds etc...
    private void makeCardDeck() {
        int cardDeckIndex = 0;
        for (int i = 0; i < this.cardSuits.length; i++) {
            for (int j = 0; j < this.cardNames.length; j++) {
                this.cardDeck[cardDeckIndex] = new Card(this.cardNames[j], this.cardSuits[i], j+1);
                cardDeckIndex++;
            }
        }
    }

    // sort card deck
    private void shuffleCardDeck() {

        // ensures index reset
        this.currentCardDeckPositionForRandomPick = 0;

        for (int i = 0; i < this.shuffleTimes; i++) {
            // 1 - 52
            int randomIndex1 = Random.getRandomNumber(0, 24);
            int randomIndex2 = Random.getRandomNumber(25, 51);

            Card tempCard = this.cardDeck[randomIndex1];
            this.cardDeck[randomIndex1] = this.cardDeck[randomIndex2];
            this.cardDeck[randomIndex2] = tempCard;
        }
    }

    // picks the next card from the deck card
    private Card pickCardFromDeck () {
        // needs check if it's more than the deck length
        // maybe reset index and get a new shuffle
        // maybe something like this?
        if (this.currentCardDeckPositionForRandomPick == 52) {
            System.out.println("No card available, shuffling again");
            this.shuffleCardDeck();
        }

        Card card = this.cardDeck[this.currentCardDeckPositionForRandomPick];
        this.currentCardDeckPositionForRandomPick++;
        return card;
    }

    private void resetPlayerHands() {
        this.p1.resetPlayerHand();
        this.p2.resetPlayerHand();
    }

    private void showCurrentPlayersHands() {
        this.p1.showPlayerHand();
        this.p2.showPlayerHand();
    }

    // recursive
    private void giveInitialCardsToPlayers(int numberOfCards) {
        for (int i = 0; i < numberOfCards; i++) {
            this.p1.giveCardToPlayerHandDeck(this.pickCardFromDeck());
            this.p2.giveCardToPlayerHandDeck(this.pickCardFromDeck());
        }

        // check if hands of players are 21 or more, so it's a bit more fair
        if (this.p1.checkHandValue() >= 21 || this.p2.checkHandValue() >= 21) {
            System.out.println(">>> Bad initial cards, restarting players cards and shuffling the deck again. <<< \n");
            this.resetPlayerHands();
            this.shuffleCardDeck(); // maybe not needed
            this.giveInitialCardsToPlayers(numberOfCards);
        }
    }

    private int checkSimpleGameRules() {

        // 0 -> both still on the game / draw
        // 1 -> p1 wins
        // 2 -> p2 wins
        if (this.p1.checkHandValue() > 21) {
            //System.out.println(p1.getName() + " bust!");
            return Result.P2_WIN;
        }

        if (this.p2.checkHandValue() > 21) {
            //System.out.println(p2.getName() + " bust!");
            return Result.P1_WIN;
        }

        if (this.p1.checkHandValue() == 21) {
            //System.out.println(p1.getName() + " has blackjack!");
            return Result.P1_WIN;
        }

        if (this.p2.checkHandValue() == 21) {
            //System.out.println(p2.getName() + " has blackjack!");
            return Result.P2_WIN;
        }

        return Result.DRAW;
    }

    // Custom rule 1 -> players can decide if they don't want to play ( because they think they have a good hand)
    // example, if p1 decides not to play and the p2 get a higher hand value then p1 loses
    private int checkCustomRule1() {
        if (this.p1.checkHandValue() > this.p2.checkHandValue() && !this.p2.wantToPlay()) {
            System.out.println(">> " + this.p2.getName() + " is off the game so " + this.p1.getName() + " wins. <<");
            return Result.P1_WIN;
        }

        // same check for p2
        if (this.p2.checkHandValue() > this.p1.checkHandValue() && !this.p1.wantToPlay()) {
            System.out.println(">> " + this.p1.getName() + " is off the game so " + this.p2.getName() + " wins. <<");
            return Result.P2_WIN;
        }

        return Result.DRAW;
    }

    private boolean shuffleCardDeckRecursive (int shuffleTimes) {

        // ensures index reset
        this.currentCardDeckPositionForRandomPick = 0;

        int randomIndex1 = Random.getRandomNumber(0, 24);
        int randomIndex2 = Random.getRandomNumber(25, 51);

        Card tempCard = this.cardDeck[randomIndex1];
        this.cardDeck[randomIndex1] = this.cardDeck[randomIndex2];
        this.cardDeck[randomIndex2] = tempCard;

        return shuffleTimes <= 0 || shuffleCardDeckRecursive(shuffleTimes - 1);
    }

    private void debugCardDeck() {
        System.out.println("CardDeck length: " + cardDeck.length);
        for (int i = 0; i < this.cardDeck.length; i++) {
            System.out.println(this.cardDeck[i].getCardName() + " " + this.cardDeck[i].getCardSuit());
        }
    }
}

/*
            if (p1.checkHandValue() > 21) {
                //System.out.println(p1.getName() + " bust!");
                this.gameEnded = true;
                winner = 2;
                break;
            }
            if (p2.checkHandValue() > 21) {
                //System.out.println(p2.getName() + " bust!");
                this.gameEnded = true;
                winner = 1;
                break;
            }

            if (p1.checkHandValue() == 21) {
                //System.out.println(p1.getName() + " has blackjack!");
                this.gameEnded = true;
                winner = 1;
                break;
            }
            if (p2.checkHandValue() == 21) {
                //System.out.println(p2.getName() + " has blackjack!");
                this.gameEnded = true;
                winner = 2;
                break;
            }

             */

// Custom rule -> players can decide if they don't want to play ( because they think they have a good hand)
// example, if p1 decides not to play and the p2 get a higher hand value then p1 loses

            /*
            // if p1 don't want to play but is beat by a higher number them the other player, it loses
            if (p1.checkHandValue() > p2.checkHandValue() && !this.p2.wantToPlay()) {
                System.out.println(">> " + p2.getName() + " is off the game so " + p1.getName() + " wins. <<");
                winner = 1;
                break;
            }

            // same check for p2
            if (p2.checkHandValue() > p1.checkHandValue() && !this.p1.wantToPlay()) {
                System.out.println(">> " + p1.getName() + " is off the game so " + p2.getName() + " wins. <<");
                winner = 2;
                break;
            }
             */



/*
package org.weekend1.blackjack;

// BlackJack class
public class BlackJack {

    private final String[] cardSuits = {"Hearts", "Diamonds", "Clubs", "Spades" }; // length 4
    private final String[] cardNames = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10" , "Jack", "Queen", "King" }; // length 13
    private final Card[] cardDeck = new Card[cardSuits.length * cardNames.length]; // 52
    private final int shuffleTimes = 2500;
    private int currentCardDeckPositionForRandomPick = 0;
    private boolean gameEnded;
    private final Player p1;
    private final Player p2;
    private final int initialNumberOfCardsForPlayers = 2;

    // constructors

    public BlackJack(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    // gets

    public String getPlayer1Name() {
        return this.p1.getName();
    }

    public String getPlayer2Name() {
        return this.p2.getName();
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    // sets


    // functions

    // play the game
    // 0 -> draw
    // 1 -> p1 wins
    // 2 -> p2 wins
    public int play() {
        System.out.println("\t>>> Game Start <<<\n");

        int winner = 0; // game starts in a draw state
        int roundNumber = 1;
        this.initGame();

        while ( !gameEnded ) {
            System.out.println(">> Round " + roundNumber + " <<");
            this.showCurrentPlayersHands();

            // basic blackjack rules
            winner = this.checkSimpleGameRules();
            if (winner != 0) {
                break;
            }

            // Custom rule 1 -> if p1 decides not to play and the p2 get a higher hand value then p1 loses
            // BAD NAME! change this
            winner = this.checkCustomRule1();
            if (winner != 0) {
                break;
            }

            //Custom rule 2 -> players can decide if they don't want to play ( because they think they have a good hand)
            // if nether of them wants to play, it's a draw (bug or feature idk)
            if (this.p1.wantToPlay()) {
                this.p1.giveCardToPlayerHandDeck(this.pickCardFromDeck());
            } else {
                if (!this.p2.wantToPlay()) {
                    System.out.println(">> " + this.p1.getName() + " and " + this.p2.getName() + " don't want to play... Ending round. <<");
                    break;
                }
            }

            if (this.p2.wantToPlay()) {
                this.p2.giveCardToPlayerHandDeck(this.pickCardFromDeck());
            } else {
                if (!this.p1.wantToPlay()) {
                    System.out.println(">> " + this.p1.getName() + " and " + this.p2.getName() + " don't want to play... Ending round. <<");
                    break;
                }
            }

            roundNumber++;
        }

        //System.out.println("Cards left to pick: " + ((this.cardDeck.length) - this.currentCardDeckPositionForRandomPick));
        System.out.println("\t>>> Game Ended <<<\n");
        return winner;
    }

    // initializes the game
    private void initGame() {

        // resets end game state
        this.gameEnded = false;

        // resets player hands
        this.resetPlayerHands();

        // make card deck
        this.makeCardDeck();

        // and shuffles it
        this.shuffleCardDeck();
        this.shuffleCardDeckRecursive(this.shuffleTimes); // just testing

        // players start with n random cards
        this.giveInitialCardsToPlayers(this.initialNumberOfCardsForPlayers);
    }

    // make card deck - should create suit of hearts of all card names, then diamonds etc...
    private void makeCardDeck() {
        int cardDeckIndex = 0;
        for (int i = 0; i < this.cardSuits.length; i++) {
            for (int j = 0; j < this.cardNames.length; j++) {
                this.cardDeck[cardDeckIndex] = new Card(this.cardNames[j], this.cardSuits[i], j+1);
                cardDeckIndex++;
            }
        }
    }

    // sort card deck
    private void shuffleCardDeck() {

        // ensures index reset
        this.currentCardDeckPositionForRandomPick = 0;

        for (int i = 0; i < this.shuffleTimes; i++) {
            // 1 - 52
            int randomIndex1 = Random.getRandomNumber(0, 24);
            int randomIndex2 = Random.getRandomNumber(25, 51);

            Card tempCard = this.cardDeck[randomIndex1];
            this.cardDeck[randomIndex1] = this.cardDeck[randomIndex2];
            this.cardDeck[randomIndex2] = tempCard;
        }
    }

    // picks the next card from the deck card
    private Card pickCardFromDeck () {
        // needs check if it's more than the deck length
        // maybe reset index and get a new shuffle
        // maybe something like this?
        if (this.currentCardDeckPositionForRandomPick == 52) {
            this.shuffleCardDeck();
        }

        Card card = this.cardDeck[this.currentCardDeckPositionForRandomPick];
        this.currentCardDeckPositionForRandomPick++;
        return card;
    }

    private void resetPlayerHands() {
        this.p1.resetPlayerHand();
        this.p2.resetPlayerHand();
    }

    private void showCurrentPlayersHands() {
        this.p1.showPlayerHand();
        this.p2.showPlayerHand();
    }

    // recursive
    private void giveInitialCardsToPlayers(int numberOfCards) {
        for (int i = 0; i < numberOfCards; i++) {
            this.p1.giveCardToPlayerHandDeck(this.pickCardFromDeck());
            this.p2.giveCardToPlayerHandDeck(this.pickCardFromDeck());
        }

        // check if hands of players are 21 or more, so it's a bit more fair
        if (this.p1.checkHandValue() >= 21 || this.p2.checkHandValue() >= 21) {
            System.out.println(">>> Bad initial cards, restarting players cards and shuffling the deck again. <<< \n");
            this.resetPlayerHands();
            this.shuffleCardDeck(); // maybe not needed
            this.giveInitialCardsToPlayers(numberOfCards);
        }
    }

    private int checkSimpleGameRules() {

        // 0 -> both still on the game / draw
        // 1 -> p1 wins
        // 2 -> p2 wins
        if (this.p1.checkHandValue() > 21) {
            //System.out.println(p1.getName() + " bust!");
            this.gameEnded = true;
            return 2;
        }

        if (this.p2.checkHandValue() > 21) {
            //System.out.println(p2.getName() + " bust!");
            this.gameEnded = true;
            return 1;
        }

        if (this.p1.checkHandValue() == 21) {
            //System.out.println(p1.getName() + " has blackjack!");
            this.gameEnded = true;
            return 1;
        }

        if (this.p2.checkHandValue() == 21) {
            //System.out.println(p2.getName() + " has blackjack!");
            this.gameEnded = true;
            return 2;
        }

        return 0;
    }

    // Custom rule 1 -> players can decide if they don't want to play ( because they think they have a good hand)
    // example, if p1 decides not to play and the p2 get a higher hand value then p1 loses
    private int checkCustomRule1() {
        if (this.p1.checkHandValue() > this.p2.checkHandValue() && !this.p2.wantToPlay()) {
            System.out.println(">> " + this.p2.getName() + " is off the game so " + this.p1.getName() + " wins. <<");
            return 1;
        }

        // same check for p2
        if (this.p2.checkHandValue() > this.p1.checkHandValue() && !this.p1.wantToPlay()) {
            System.out.println(">> " + this.p1.getName() + " is off the game so " + this.p2.getName() + " wins. <<");
            return 2;
        }

        return 0;
    }

    private boolean shuffleCardDeckRecursive (int shuffleTimes) {

        // ensures index reset
        this.currentCardDeckPositionForRandomPick = 0;

        int randomIndex1 = Random.getRandomNumber(0, 24);
        int randomIndex2 = Random.getRandomNumber(25, 51);

        Card tempCard = this.cardDeck[randomIndex1];
        this.cardDeck[randomIndex1] = this.cardDeck[randomIndex2];
        this.cardDeck[randomIndex2] = tempCard;

        shuffleTimes--;
        //return shuffleTimes <= 0 ? true : shuffleCardDeckRecursive(shuffleTimes);
        return shuffleTimes <= 0 || shuffleCardDeckRecursive(shuffleTimes);
    }

    private void debugCardDeck() {
        System.out.println("CardDeck length: " + cardDeck.length);
        for (int i = 0; i < this.cardDeck.length; i++) {
            System.out.println(this.cardDeck[i].getCardName() + " " + this.cardDeck[i].getCardSuit());
        }
    }
}

/*
            if (p1.checkHandValue() > 21) {
                //System.out.println(p1.getName() + " bust!");
                this.gameEnded = true;
                winner = 2;
                break;
            }
            if (p2.checkHandValue() > 21) {
                //System.out.println(p2.getName() + " bust!");
                this.gameEnded = true;
                winner = 1;
                break;
            }

            if (p1.checkHandValue() == 21) {
                //System.out.println(p1.getName() + " has blackjack!");
                this.gameEnded = true;
                winner = 1;
                break;
            }
            if (p2.checkHandValue() == 21) {
                //System.out.println(p2.getName() + " has blackjack!");
                this.gameEnded = true;
                winner = 2;
                break;
            }

             */

// Custom rule -> players can decide if they don't want to play ( because they think they have a good hand)
// example, if p1 decides not to play and the p2 get a higher hand value then p1 loses

            /*
            // if p1 don't want to play but is beat by a higher number them the other player, it loses
            if (p1.checkHandValue() > p2.checkHandValue() && !this.p2.wantToPlay()) {
                System.out.println(">> " + p2.getName() + " is off the game so " + p1.getName() + " wins. <<");
                winner = 1;
                break;
            }

            // same check for p2
            if (p2.checkHandValue() > p1.checkHandValue() && !this.p1.wantToPlay()) {
                System.out.println(">> " + p1.getName() + " is off the game so " + p2.getName() + " wins. <<");
                winner = 2;
                break;
            }
             */