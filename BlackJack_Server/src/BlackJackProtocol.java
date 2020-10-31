
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;

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

    public void acceptInput(String input, GameState state ){
        String output = "";
        System.out.println("im boutta say something to " + player.getName());
        try {
            if( state == GameState.GameStarted){
                out.writeUTF("Game has Started, you are " + player.getName());
            }
            else if(state == GameState.DealingCards){
                player.hit(bj.dealCard());
                out.writeUTF("Card dealt to you. Your current Hand is " + player.showHand() + "Hand Value = " + player.calculateHandValue());
            }
            else if(state == GameState.PlayerTurn) {
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
                out.writeUTF(tableString.toString() + "\nIt is now your turn!");
                out.writeUTF("0");

                String line = "start Turn";
                int count = 0;
                while(line.equals("stay") == false){
                    if (in.available() > 0) {
                        StringBuilder hitOutput = new StringBuilder();
                        //System.out.println("in is available");
                        line = in.readUTF();
                        System.out.println(line);
                        if(line.equals("hit")){
                            Card dealtCard = bj.dealCard();
                            hitOutput.append(dealtCard.toString() + " dealt");
                            player.hit(dealtCard);
                            if(player.calculateHandValue() == -1){
                                hitOutput.append("\nBust! your hand value exceeded 21");
                                out.writeUTF(hitOutput.toString());
                                break;
                            }
                            else{
                                hitOutput.append("\nYou: " + player.toString());
                                out.writeUTF(hitOutput.toString());
                            }

                            //System.out.println(player.getName() + " hit!!");
                        }
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
