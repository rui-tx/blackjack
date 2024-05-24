package org.weekend1.blackjack;

public class Main {
    public static void main(String[] args) {
        BlackJack blackjack = new BlackJack(new Player("Dealer"), new Player("Player"));

        int p1Wins = 0;
        int p2Wins = 0;
        int totalGames = 1;
        for (int i = 0; i < totalGames; i++) {

            // initializes the game for each game round.
            blackjack.initGame();

            System.out.println(">>> Game Start <<<\n");
            while (!blackjack.isGameEnded()) {
                int checkRoundResult = blackjack.play();
                switch (checkRoundResult) {
                    case BlackJack.Result.P1_WIN:
                        System.out.println("Winner is: " + blackjack.getPlayer1Name() + "\n");
                        p1Wins++;
                        break;
                    case BlackJack.Result.P2_WIN:
                        System.out.println("Winner is: " + blackjack.getPlayer2Name() + "\n");
                        p2Wins++;
                        break;
                    case BlackJack.Result.DRAW:
                        System.out.println("It's a draw!\n");
                        break;
                    case BlackJack.Result.DRAW_REFUSAL:
                        System.out.println("Nether player want to continue, it's a draw!\n");
                        break;
                    case BlackJack.Result.GAME_CONTINUES:
                        // next round
                        System.out.println(">> Dealing again! <<");
                        break;

                    // error cases
                    case BlackJack.Result.GAME_ENDED:
                        System.out.println("Game has ended, can't play again without initializing the game.");
                        return;
                    case BlackJack.Result.GAME_NOT_INITIALIZED:
                        System.out.println("Game not initialized.");
                        return;
                }
            }

            System.out.println(">>> Game Ended <<<\n");
        }

        System.out.println(">>> Results after " + totalGames + " rounds <<<");
        System.out.println(blackjack.getPlayer1Name() + " won " + p1Wins + " rounds...");
        System.out.println(blackjack.getPlayer2Name() + " won " + p2Wins + " rounds...");

        int winner = getWinner(p1Wins, p2Wins);
        if (winner > 0) {
            System.out.println("Winner is: " + blackjack.getPlayer1Name());
            return;
        }

        if (winner < 0) {
            System.out.println("Winner is: " + blackjack.getPlayer2Name());
            return;
        }

        System.out.println("It's a draw!");
    }

    public static int getWinner(int p1, int p2) {
        return p1 - p2;
    }
}