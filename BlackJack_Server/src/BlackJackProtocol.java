
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * a class to moderate the input and output between the client and the server. The class with check the state of the game and the input received and respond approprietly
 */
public class BlackJackProtocol {
    private Player player;
    private BlackJackGame2 bj;
    private DataInputStream in;
    private DataOutputStream out;
    public BlackJackProtocol(){

    }
    public BlackJackProtocol(Player player, BlackJackGame2 bj , DataInputStream in, DataOutputStream out){
        this.in = in;
        this.out = out;
        this.player = player;
        this.bj = bj;
    }

    /**
     * method to accept the users input and respond depending on the game state
     *
     * @param state state of the black jack game
     */
    public void acceptInput(GameState state ){
        try {
            //let the player know who he/she/they is
            if( state == GameState.GameStarted){
                out.writeUTF("Game has Started, you are " + player.getName());
            }
            //if the state is dealing catds then deal a card to the player
            else if(state == GameState.DealingCards){
                player.hit(bj.dealCard());
                //show the player their hand
                out.writeUTF("Card dealt to you. Your current Hand is " + player.showHand() + " Hand Value = " + player.calculateHandValue());
            }
            //play out the players turn
            else if(state == GameState.PlayerTurn) {
                // build the table string so that the player has visibility of the other players hand and the single card of the dealer
                StringBuilder tableString = new StringBuilder();
                tableString.append("\n");
                for(int i=0; i < bj.getPlayersInGame().length; i++){
                    if(i == player.getId()){
                        tableString.append(bj.getPlayersInGame()[i].toString() + " <--You\n");
                    }
                    else{
                        tableString.append(bj.getPlayersInGame()[i].toString() + "\n ");
                    }

                }
                // show user the table
                out.writeUTF(tableString.toString() + "\nIt is now your turn!");
                out.writeUTF("0");

                String line = "start Turn";
                int count = 0;
                //while the player has not declared to stay then keep going
                while(line.equals("stay") == false){
                    if (in.available() > 0) {
                        StringBuilder hitOutput = new StringBuilder();
                        //System.out.println("in is available");
                        line = in.readUTF();
                        System.out.println(line);
                        //if player hits then deal the player a card and check if the user went over
                        if(line.equals("hit")){
                            //take the card from the deck
                            Card dealtCard = bj.dealCard();
                            //deal the card
                            hitOutput.append(dealtCard.toString() + " dealt");
                            player.hit(dealtCard);
                            if(player.calculateHandValue() == -1){
                                //let the user know they bust
                                hitOutput.append("\nBust! your hand value exceeded 21 Press enter");
                                out.writeUTF(hitOutput.toString());
                                break;
                            }
                            else{
                                //let the user know their hand
                                hitOutput.append("\nYou: " + player.toString());
                                out.writeUTF(hitOutput.toString());
                            }

                            //System.out.println(player.getName() + " hit!!");
                        }
                    }
                }
            }
            //display the results if the game is over
            else if(bj.getState() == GameState.GameEnd){
                out.writeUTF(bj.displayResults());

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
