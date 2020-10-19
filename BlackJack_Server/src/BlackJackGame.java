import java.io.File;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class BlackJackGame {
    private Player dealer;
    private ArrayList<Player> playersInGame;
    private String gameState;
    private Player currentPlayer;
    public Deck cardDeck;
    public BlackJackGame(String fileName){
        this.cardDeck = new Deck(fileName);
        this.playersInGame = new ArrayList<>();
        this.dealer = new Player("Dealer", true);
        this.gameState = "not started";
    }

    public void playGame(){
        checkGameState();
    }

    public String addPlayer(String playerName){
        playerName = playerName.trim();
        if(playersInGame.size() < 5){
            playersInGame.add(new Player(playerName, false));
            return "player '" + playerName + "' added";
        }
        else{
            return "Max players reached";
        }


    }

    public void initializeGame(){
        playersInGame.clear();
        Scanner sc = new Scanner(System.in);
        boolean initializeFinished = false;
        System.out.println("Welcome to Black Jack!");
        while(!initializeFinished){
            if(playersInGame.size() < 2){
                System.out.println("What is the Name of Player" + (playersInGame.size() + 1) + "?");
                System.out.println(addPlayer(sc.nextLine()));
            }
            else if(playersInGame.size() == 5){
                System.out.println("Player Room is full Game Will Now Start");
                initializeFinished = true;
            }
            else{
                System.out.println("Are there anymore players? Y/N");
                String ynAnswer = sc.nextLine().toLowerCase();
                if(ynAnswer.equals("y")){
                    System.out.println("What is the Name of Player" + (playersInGame.size() + 1) + "?");
                    System.out.println(addPlayer(sc.nextLine()));
                }
                else if(ynAnswer.equals("n")){
                    System.out.println("Game Will Now Start");
                    initializeFinished = true;
                }
                else{
                    System.out.println("Invalid Input try again");
                }

            }
        }
        playersInGame.add(dealer);
    }

    public void checkGameState(){

        if(gameState.equals("not started")){
            initializeGame();
            gameState = "started";
            checkGameState();
        }
        else if(gameState.equals("started")){
            dealCards();
            for(Player player : playersInGame){
                if(player.isDealer()){
                    System.out.println(player.getName() + "'s Hand: " + player.showFirstOnly());
                }
                else{
                    System.out.println(player.getName() + "'s Hand: " + player.showHand());
                }

            }
            gameState = "playing";
            checkGameState();
        }
        else if(gameState.equals("playing")){
            for(Player player : playersInGame ){
                if(player.isDealer()){
                    dealerTurn();
                }else{
                    playerTurn(player);
                }

            }
            showCards();
            gameState = "game over";
            checkGameState();
        }
        else if(gameState.equals("game over")){
            gameState = determineReplay();
            checkGameState();
        }
    }

    public void dealCards(){
        for(Player player : playersInGame){
            Card[] hand = new Card[]{
                    cardDeck.removeFromDeck(),
                    cardDeck.removeFromDeck()
            };
            player.setHand(hand);
        }


    }

    public void playerTurn(Player player){
        System.out.println("");
        System.out.println(player.getName() + "'s turn!");
        System.out.println("=================================");
        Scanner sc = new Scanner(System.in);
        String userInput = "";
        while(!userInput.equals("n") && player.calculateHandValue() != -1){
            System.out.println("Your hand is currently " + player.showHand() + " value = " + player.calculateHandValue() + ". Would you like to hit? Y/N");
            userInput = sc.nextLine().toLowerCase();
            if(userInput.equals("y")){
                Card hitCard = cardDeck.removeFromDeck();
                System.out.println(player.getName() + " hit and received a " + hitCard.getName());
                player.hit(hitCard);
            }
            else{
                System.out.println("Invalid Input! Try again");
            }
        }
        int handValue = player.calculateHandValue();
        if(handValue == - 1){
            System.out.println("You went over 21 your hand is a bust!");
        }
        else{
            System.out.println("You have decided to keep your hand with the value of" + handValue);
        }

    }

    public void dealerTurn(){
        System.out.println("");
        System.out.println("Dealers Turn!");
        System.out.println("=================================");
        while(dealer.calculateHandValue()  != -1){
            if(dealer.calculateHandValue() < 17) {
                dealer.hit(cardDeck.removeFromDeck());
            }
            else {
                System.out.println("Dealer stays");
                break;
            }

        }
        int dealerHand = dealer.calculateHandValue();
        if(dealerHand == -1){
            System.out.println("Dealer Busts, everyone wins");

        }
        else{
            System.out.println("Dealers turn has ended");
            System.out.println("");
        }
    }

    public void showCards(){
        int highestScore = 0;
        for(Player player : playersInGame){
            int handValue = player.calculateHandValue();
            if(handValue > highestScore){
                highestScore = handValue;
            }
        }
        if(dealer.calculateHandValue() != -1){
            for(Player player : playersInGame){
                int handValue = player.calculateHandValue();
                if(handValue == highestScore){
                    System.out.println(player.getName() + " Won!");
                }
            }
        }

        for(Player player : playersInGame){
            System.out.println("");
            System.out.println(player.toString());
        }

    }


    public String determineReplay(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Would you like to play again? Y/N");
        String userInput = sc.nextLine().toLowerCase();
        boolean validInput = false;
        String gameState = "";
        while(validInput == false){
            if(userInput.equals("y")){
                playersReturnCardsToDeck();
                validInput = true;
                gameState = "started";
            }
            else if(userInput.equals("n")){
                playersReturnCardsToDeck();
                validInput = true;
                gameState = "not started";
            }
            else{
                System.out.println("invalid input. Must be y or n");
            }
        }
        return gameState;

    }

    public void playersReturnCardsToDeck(){
        for(Player player : playersInGame){
            cardDeck.returnHandToDeck(player.returnCards());
        }
    }



}
