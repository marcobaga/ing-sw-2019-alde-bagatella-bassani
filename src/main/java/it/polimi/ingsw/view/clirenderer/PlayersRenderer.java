package it.polimi.ingsw.view.clirenderer;

import it.polimi.ingsw.view.ClientModel;

import java.util.List;
import static it.polimi.ingsw.view.clirenderer.MainRenderer.RESET;

/**
 * Class creating a bidimensional String array containing information about other players
 */
public class PlayersRenderer {

    private static final int PLAYERS_WIDTH = 65;
    private static final int LINES_PER_PLAYER = 7;
    private static final int PADDING = 3;
    private static final int SECOND_COLUMN = 24;
    private static final String DAMAGE_TAG = "Damage:";
    private static final String MARK_TAG = "Marks:";

    /**
     * Standard constructor
     */
    private PlayersRenderer(){}

    /**
     * Creates a bidimensional String array containing all data about other players
     * @param clientModel       reference to the model
     * @return                  graphical representation of other players
     */
    public static String[][] get(ClientModel clientModel){

        List<ClientModel.SimplePlayer> players = clientModel.getPlayers();
        int playerNum = players.size();
        String[] names = new String[playerNum];
        String[] weapons = new String[playerNum];
        String[][] ammo = new String[playerNum][PLAYERS_WIDTH];
        String[] deaths = new String[playerNum];
        for(int i=0; i<playerNum; i++){
            names[i] = "";
            weapons[i] = "";
            for(int j=0; j<ammo[i].length; j++){
                ammo[i][j] = " ";
            }
            deaths[i] = "";
        }

        buildStrings(playerNum, players, clientModel, names, weapons, ammo, deaths);

        int playersHeight = (playerNum-1)*LINES_PER_PLAYER + 1;
        String[][] box = new String[playersHeight][PLAYERS_WIDTH];
        for(int i=0; i<box.length; i++){
            for(int j =0; j<box[i].length; j++){
                box[i][j] = " ";
            }
        }

        printStrings(playerNum, playersHeight, clientModel, box, names, ammo, weapons, deaths);

        return MainRenderer.trimBox(box, PADDING, PLAYERS_WIDTH);
    }


    /**
     * Fills in a set of Strings which will be printed to the array.
     *
     * @param playerNum     number of players
     * @param players       list of players
     * @param clientModel   model of the game state
     * @param names         names of the player, still empty
     * @param weapons       weapons held by each player, still empty
     * @param ammo          ammo owned by each player, still empty
     * @param deaths        times each player has died so far, still empty
     */
    private static void buildStrings(int playerNum, List<ClientModel.SimplePlayer> players, ClientModel clientModel, String[] names, String[] weapons, String[][]ammo, String[] deaths) {
        for (int i = 0; i < playerNum; i++) {
            ClientModel.SimplePlayer p = players.get(i);
            names[i] = p.getUsername() + (p.getStatus().equalsIgnoreCase("basic") ? "" : " [" + p.getStatus() + "]") + (p.isFlipped() ? " [FLIPPED]" : "");
            if (clientModel.getCurrentPlayer().getId() == p.getId()) {
                names[i] = names[i] + " [current]";
            }
            weapons[i] = "Weapons: ";
            List<ClientModel.SimpleWeapon> currentWeapons = p.getWeapons();
            if (currentWeapons.isEmpty()) {
                weapons[i] = weapons[i] + "none";
            } else {
                for (int j = 0; j < currentWeapons.size(); j++) {
                    weapons[i] = weapons[i] + currentWeapons.get(j).getName();
                    if (currentWeapons.get(j).isLoaded()) {
                        weapons[i] = weapons[i] + "*";
                    }
                    if (j != currentWeapons.size() - 1) {
                        weapons[i] = weapons[i].concat(", ");
                    }
                }
            }
            int count;
            for (count = 0; count < "Ammo: ".length(); count++) {
                ammo[i][count] = String.valueOf("Ammo: ".charAt(count));
            }
            for (int j = 0; j < p.getBlueAmmo(); j++) {
                ammo[i][count] = ClientModel.getEscapeCode("blue") + "|" + RESET;
                count++;
            }
            for (int j = 0; j < p.getRedAmmo(); j++) {
                ammo[i][count] = ClientModel.getEscapeCode("red") + "|" + RESET;
                count++;
            }
            for (int j = 0; j < p.getYellowAmmo(); j++) {
                ammo[i][count] = ClientModel.getEscapeCode("yellow") + "|" + RESET;
                count++;
            }
            deaths[i] = "Deaths: " + p.getDeaths() + " (next death awards " + p.getNextDeathAwards() + " points)";
        }
    }

    /**
     * Places the Strings previously built on the bidimensional array.
     *
     * @param playerNum         number of players
     * @param playersHeight     height of the array representing players
     * @param clientModel       model of the game state
     * @param box               the array that will be filled with Strings
     * @param names             names of the player, still empty
     * @param ammo              ammo owned by each player, still empty
     * @param weapons           weapons held by each player, still empty
     * @param deaths            times each player has died so far, still empty
     */
    private static void printStrings(int playerNum, int playersHeight, ClientModel clientModel, String[][]box, String[] names, String[][]ammo, String[]weapons, String[] deaths){
        int row =0;
        for(int i=0; i<playerNum && row*LINES_PER_PLAYER<playersHeight; i++){

            if(i+1 != clientModel.getPlayerID()) {

                for (int j = 0; j < names[i].length()&&j+PADDING<PLAYERS_WIDTH; j++) {
                    box[row * LINES_PER_PLAYER + 1][j + PADDING] = ClientModel.getEscapeCode(clientModel.getPlayer(i+1).getColor()) + (names[i].charAt(j)) + RESET;
                }
                for (int j = 0; j < DAMAGE_TAG.length()&&j+PADDING<PLAYERS_WIDTH; j++) {
                    box[row * LINES_PER_PLAYER + 2][j + PADDING] = String.valueOf(DAMAGE_TAG.charAt(j));
                }

                int k = 0;
                for (int color : clientModel.getPlayer(i+1).getDamageID()) {
                    if(k+PADDING+DAMAGE_TAG.length()>PLAYERS_WIDTH){
                        break;
                    }
                    box[row * LINES_PER_PLAYER + 2][k + PADDING+DAMAGE_TAG.length()] = ClientModel.getEscapeCode(clientModel.getPlayer(color).getColor()) + "●" + RESET;
                    k++;
                }

                for (int j = 0; j < MARK_TAG.length(); j++) {
                    if(j+SECOND_COLUMN>PLAYERS_WIDTH){
                        break;
                    }
                    box[row * LINES_PER_PLAYER + 2][j + SECOND_COLUMN] = String.valueOf(MARK_TAG.charAt(j));
                }
                k = 0;

                for (int color : clientModel.getPlayer(i+1).getMarksID()) {
                    if(k+SECOND_COLUMN+MARK_TAG.length()>PLAYERS_WIDTH){
                        break;
                    }
                    box[row * LINES_PER_PLAYER + 2][k + SECOND_COLUMN + MARK_TAG.length()] = ClientModel.getEscapeCode(clientModel.getPlayer(color).getColor()) + "◎" + RESET;
                    k++;
                }

                for(int j = 0; j<ammo[i].length&&j+PADDING<PLAYERS_WIDTH; j++){
                    box[row * LINES_PER_PLAYER + 3][j+PADDING] = ammo[i][j];
                }

                for (int j = 0; j < weapons[i].length()&&j+PADDING<PLAYERS_WIDTH; j++) {
                    box[row * LINES_PER_PLAYER + 4][j + PADDING] = String.valueOf(weapons[i].charAt(j));
                }

                String hand = "Cards in hand: " + clientModel.getPlayer(i+1).getCardNumber();
                for(int j = 0; j < hand.length()&&j+PADDING<PLAYERS_WIDTH; j++){
                    box[row * LINES_PER_PLAYER + 5][j + PADDING] = String.valueOf(hand.charAt(j));
                }

                for(int j=0; j<deaths[i].length()&&j+PADDING<PLAYERS_WIDTH; j++){
                    box[row * LINES_PER_PLAYER + 6][j + PADDING] = String.valueOf(deaths[i].charAt(j));
                }
            } else{
                row--;
            }
            row++;
        }
    }
}
