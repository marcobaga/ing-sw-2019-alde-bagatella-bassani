package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.cards.AmmoPack;
import it.polimi.ingsw.model.cards.AmmoTile;
import it.polimi.ingsw.model.cards.PowerUp;
import it.polimi.ingsw.model.cards.Weapon;
import it.polimi.ingsw.model.exceptions.NotAvailableAttributeException;
import it.polimi.ingsw.view.ClientModel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class containing static methods to build update messages to be sent to clients as JsonObjects
 *
 * @author marcobaga, BassaniRiccardo
 */

public class Updater {

    private static final Logger LOGGER = Logger.getLogger("serverLogger");

    private static final String UPD_HEADER = "UPD";

    public static final String RELOAD_UPD = "reload";
    public static final String POWER_UP_DECK_REGEN_UPD ="powerUpDeckRegen";
    public static final String REMOVE_SKULL_UPD = "skullRemoved";
    public static final String DRAW_POWER_UP_UPD = "drawPowerUp";
    public static final String DISCARD_POWER_UP_UPD = "discardPowerUp";
    public static final String DISCARD_WEAPON_UPD = "discardWeapon";
    public static final String PICKUP_WEAPON_UPD = "pickupWeapon";
    public static final String USE_AMMO_UPD = "useAmmo";
    public static final String ADD_AMMO_UPD = "addAmmo";
    public static final String MOVE_UPD = "move";
    public static final String STATUS_UPD = "status";
    public static final String ADD_DEATH_UPD = "addDeath";
    public static final String DAMAGE_UPD = "damage";
    public static final String MARK_UPD = "mark";
    public static final String REMOVE_MARKS = "removeMarks";

    public static final String ADD_WEAPON_UPD = "addWeapon";
    public static final String REMOVE_WEAPON_UPD = "removeWeapon";
    public static final String SET_IN_GAME_UPD = "setInGame";
    public static final String REMOVE_AMMO_TILE_UPD = "removeAmmoTile";
    public static final String MODEL_UPD = "model";
    public static final String RENDER_UPD = "render";

    private static final String HEAD_PROP = "head";
    public static final String TYPE_PROP = "type";

    public static final String PLAYER_PROP = "player";
    public static final String WEAPON_PROP = "weapon";
    public static final String SQUARE_PROP = "square";

    public static final String LOADED_PROP = "loaded";
    public static final String CARDS_NUMBER_PROP = "cardNumber";
    public static final String SKULL_NUMBER_PROP = "skullNumber";
    private static final String KILLER_PROP = "killer";
    private static final String OVERKILL_PROP = "overkill";
    public static final String POWER_UP_NAME_PROP = "powerUpName";
    public static final String POWER_UP_COLOR_PROP = "powerUpColor";
    public static final String RED_AMMO_PROP = "redAmmo";
    public static final String BLUE_AMMO_PROP = "blueAmmo";
    public static final String YELLOW_AMMO_PROP = "yellowAmmo";
    public static final String PLAYER_LIST_PROP = "playerList";
    public static final String BOOLEAN_PROP = "boolean";
    public static final String MODEL_PROP = "model";
    public static final String POINTS_PROP = "points";
    public static final String STATUS_PROP = "status";


    /**
     * Updater private constructor.
     */
    private Updater(){}


    /**
     * Returns a serialization of a "loaded" update.
     *
     * @param msg       the type of update.
     * @param w         the weapon that can be loaded or unloaded.
     * @param loaded    whether the weapon in loaded or unloaded.
     * @return          a serialization of the update.
     */
    public static JsonObject get(String msg, Weapon w, boolean loaded) {
        JsonObject j = getFreshUpdate(msg);
        j.addProperty(WEAPON_PROP, w.getWeaponName().toString());
        j.addProperty(LOADED_PROP, loaded);
        //loaded
        return j;
    }


    /**
     * Returns a serialization of a "deck regeneration" update.
     *
     * @param msg       the type of update.
     * @param quantity  the number of cards in the regenerated deck.
     * @return          a serialization of the update.
     */
    public static JsonObject get(String msg, int quantity) {
        JsonObject j = getFreshUpdate(msg);
        j.addProperty(CARDS_NUMBER_PROP, quantity);
        //pDeckRegen
        return j;
    }


    /**
     * Returns a serialization of a "skull removed" update.
     *
     * @param msg       the type of update.
     * @param quantity  the number of cards in the regenerated deck.
     * @param p         the killer.
     * @param ok        whether an overkill occurred.
     * @return          a serialization of the update.
     */
    public static JsonObject get(String msg, int quantity, Player p, boolean ok) {
        JsonObject j = getFreshUpdate(msg);
        j.addProperty(SKULL_NUMBER_PROP, quantity);
        j.addProperty(KILLER_PROP, p.getId());
        j.addProperty(OVERKILL_PROP, ok);
        return j;
        //skullRemoved
    }

    /**
     * Returns a serialization of a "draw/discard powerup" update.
     *
     * @param s         the type of update.
     * @param p         the holder.
     * @param powerUp   the powerup to draw/discard.
     * @return          a serialization of the update.
     */
    public static JsonObject get(String s, Player p, PowerUp powerUp) {
        JsonObject j = getFreshUpdate(s);
        j.addProperty(PLAYER_PROP, p.getId());
        j.addProperty(POWER_UP_NAME_PROP, powerUp.getName().toString());
        j.addProperty(POWER_UP_COLOR_PROP, powerUp.getColor().toStringLowerCase());
        return j;
        //drawPowerUp and discardPowerup
    }

    /**
     * Returns a serialization of a "draw/discard weapon" update.
     *
     * @param s         the type of update.
     * @param p         the holder.
     * @param w         the weapon to draw/discard.
     * @return          a serialization of the update.
     */
    public static JsonObject get(String s, Player p, Weapon w) {
        JsonObject j = getFreshUpdate(s);
        j.addProperty(PLAYER_PROP, p.getId());
        j.addProperty(WEAPON_PROP, w.getWeaponName().toString());
        return j;
        //discardWeapon and pickUpWeapon
    }


    /**
     * Returns a serialization of a "use/add ammo" update.
     *
     * @param s         the type of update.
     * @param p         the player who uses/gains an ammo.
     * @param a         the amount of ammo.
     * @return          a serialization of the update.
     */
    public static JsonObject get(String s, Player p, AmmoPack a) {

        JsonObject j = getFreshUpdate(s);
        j.addProperty(PLAYER_PROP, p.getId());
        j.addProperty(RED_AMMO_PROP, a.getRedAmmo());
        j.addProperty(BLUE_AMMO_PROP, a.getBlueAmmo());
        j.addProperty(YELLOW_AMMO_PROP, a.getYellowAmmo());

        return j;
        //useAmmo, addAmmo
    }


    /**
     * Returns a serialization of a "move" update.
     *
     * @param s         the type of update.
     * @param p         the player who moves.
     * @param square   the destination square.
     * @return          a serialization of the update.
     */
    public static JsonObject get(String s, Player p, Square square) {
        JsonObject j = getFreshUpdate(s);
        j.addProperty(PLAYER_PROP, p.getId());
        j.addProperty(SQUARE_PROP, square.getId());
        return j;
        //move
    }


    /**
     * Returns a serialization of a "status" update.
     *
     * @param s         the type of update.
     * @param p         the status.
     * @return          a serialization of the update.
     */
    public static JsonObject get(String s, Player p) {
        JsonObject j = getFreshUpdate(s);
        j.addProperty(PLAYER_PROP, p.getId());
        j.addProperty(STATUS_PROP, p.getStatus().toString());
        j.addProperty(BOOLEAN_PROP, p.isFlipped());
        return j;
        //status
    }


    /**
     * Returns a serialization of a "add death" update.
     *
     * @param s         the type of update.
     * @param p         the player who died.
     * @param points    the number of point the player will give the next time he will die.
     * @return          a serialization of the update.
     */
    public static JsonObject get(String s, Player p, int points){
        JsonObject j = getFreshUpdate(s);
        j.addProperty(PLAYER_PROP, p.getId());
        j.addProperty(POINTS_PROP, points);
        return j;
        //addDeath
    }


    /**
     * Returns a serialization of a "damaged/marked/removeMarks" update.
     *
     * @param s         the type of update.
     * @param p         the player.
     * @param l         the updated list of damages or marks.
     * @return          a serialization of the update.
     */
    public static JsonObject get(String s, Player p, List<Player> l) {
        JsonObject j = getFreshUpdate(s);
        j.addProperty(PLAYER_PROP, p.getId());
        JsonArray array = new JsonArray();
        for (Player shooter : l) {
            array.add(shooter.getId());
        }
        j.add(PLAYER_LIST_PROP, array);
        return j;
        //damaged, marked
    }

    /**
     * Returns a serialization of a "weapon removed/added" update.
     *
     * @param s         the type of update.
     * @param sq        the square.
     * @param w         the weapon to add/remove.
     * @return          a serialization of the update.
     */
    public static JsonObject get(String s, Square sq, Weapon w) {
        JsonObject j = getFreshUpdate(s);
        j.addProperty(SQUARE_PROP, sq.getId());
        j.addProperty(WEAPON_PROP, w.toString());
        return j;
        //weaponRemoved & addWeapon
    }

    /**
     * Returns a serialization of a "set in game" update.
     *
     * @param s         the type of update.
     * @param p         the player.
     * @param in        whether the player is in the game.
     * @return          a serialization of the update.
     */
    public static JsonObject get(String s, Player p, boolean in) {
        JsonObject j = getFreshUpdate(s);
        j.addProperty(PLAYER_PROP, p.getId());
        j.addProperty(BOOLEAN_PROP, in);
        return j;
        //setInGame
    }

    /**
     * Returns a serialization of a "remove ammo tile" update.
     *
     * @param s         the type of update.
     * @param square    the square.
     * @return          a serialization of the update.
     */
    public static JsonObject get(String s, Square square){
        JsonObject j = getFreshUpdate(s);
        j.addProperty(SQUARE_PROP, square.getId());
        return j;
        //removeAmmoTile
    }

    /**
     * Creates a message that updates the ClientModel of the specified player on the specified board and returns it.
     *
     * @param board         the game board.
     * @param player        the player who will receive the updated model.
     * @return              the updated model.
     */
    public static JsonObject getModel(Board board, Player player) {


        ClientModel cm = new ClientModel();

        //simpleSquares
        List<ClientModel.SimpleSquare> simpleSquares = new ArrayList<>();
        for (Square s : board.getMap()){
            if (board.getSpawnPoints().contains(s))     simpleSquares.add(toSimpleSquare((WeaponSquare)s));
            else  simpleSquares.add(toSimpleSquare((AmmoSquare) s));
        }
        cm.setSquares(simpleSquares);


        //simplePlayers, currentPlayer, killShotTrack
        List<ClientModel.SimplePlayer> simplePlayers = new ArrayList<>();
        List<ClientModel.SimplePlayer> killers = new ArrayList<>(12);

        for (Player p : board.getPlayers()){
            ClientModel.SimplePlayer simplePlayer = createSimplePlayer(p, board);
            simplePlayers.add(simplePlayer);
        }

        cm.setPlayers(simplePlayers);
        cm.setCurrentPlayerId(board.getCurrentPlayer().getId());
        cm.setPlayerID(player.getId());
        cm.setPoints(player.getPoints());

        //creates the killShotTrack
        try {
            for (Player killer : board.getKillShotTrack().getKillers()) {
                    killers.add(cm.getPlayer(killer.getId()));
                }
        } catch (NotAvailableAttributeException e){ LOGGER.log(Level.SEVERE, "NotAvailableAttribute exception thrown while getting the killshot track");}

        cm.setKillShotTrack(killers);
        try {
            cm.setSkullsLeft(board.getKillShotTrack().getSkullsLeft());
        } catch (NotAvailableAttributeException e) {
            LOGGER.log(Level.SEVERE, "Impossible to get the number of skulls left, all skulls removed.");
            cm.setSkullsLeft(0);
        }

        //decks size
        cm.setPowerUpCardsLeft(board.getPowerUpDeck().getDrawable().size());
        cm.setWeaponCardsLeft(board.getWeaponDeck().getDrawable().size());
        cm.setMapID(board.getId());

        //powerUpInHand (of the selected player)
        List<String> powerUpInHand = new ArrayList<>();
        List<String> colorPowerUpInHand = new ArrayList<>();
        for (PowerUp powerUp: player.getPowerUpList()){
            powerUpInHand.add(powerUp.getName().toString());
            colorPowerUpInHand.add(powerUp.getColor().toStringLowerCase());
        }
        cm.setPowerUpInHand(powerUpInHand);
        cm.setColorPowerUpInHand(colorPowerUpInHand);

        Gson gson = new Gson();
        String json = gson.toJson(cm);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(HEAD_PROP, UPD_HEADER);
        jsonObject.addProperty(TYPE_PROP, MODEL_UPD);
        jsonObject.addProperty(MODEL_PROP, json);

        return jsonObject;
    }


    /**
     * Creates a SimplePlayer, hence a simplified version of a specified player to be saved on the client.
     *
     * @param p             the Player to take date form.
     * @param board         the player board.
     * @return              the created SimplePlayer.
     */
    private static ClientModel.SimplePlayer createSimplePlayer(Player p, Board board){

        //create a new simplePlayer
        List<Integer> damages = new ArrayList<>();
        for (Player shooter : p.getDamages()){
            damages.add(shooter.getId());
        }
        List<Integer> marks = new ArrayList<>();
        for (Player marker : p.getMarks()){
            marks.add(marker.getId());
        }
        List<ClientModel.SimpleWeapon> weapons = new ArrayList<>();
        for (Weapon weapon : p.getWeaponList()){
            weapons.add(toSimpleWeapon(weapon));
        }
        ClientModel.SimpleSquare position = null;
        boolean isInGame = false;
        try {
            if (board.getSpawnPoints().contains(p.getPosition())) position = toSimpleSquare((WeaponSquare)p.getPosition());
            else position = toSimpleSquare((AmmoSquare)p.getPosition());
            isInGame = true;

        } catch (NotAvailableAttributeException e) {
            LOGGER.log(Level.FINE, "The player is not on the board, is in game remains false");
        }

        return new ClientModel().new SimplePlayer(p.getId(), p.getColor(), p.getPowerUpList().size(), damages, marks, weapons, position, p.getUsername(), p.getAmmoPack().getRedAmmo(), p.getAmmoPack().getBlueAmmo(), p.getAmmoPack().getYellowAmmo(), isInGame, p.isFlipped(), p.getPoints(), p.getDeaths(), p.getPointsToGive(), p.getStatus().toString());

    }


    /**
     * Helper method creating the common structure of most update messages.
     * @param msg   type of the update message
     * @return      a partial update message
     */
    private static JsonObject getFreshUpdate(String msg) {
        JsonObject j = new JsonObject();
        j.addProperty(HEAD_PROP, UPD_HEADER);
        j.addProperty(TYPE_PROP, msg);
        return j;
    }

    /**
     * Builds a message asking the client to render the board state
     *
     * @return      render message
     */
    public static JsonObject getRenderMessage() {
        JsonObject j = new JsonObject();
        j.addProperty(HEAD_PROP, UPD_HEADER);
        j.addProperty(TYPE_PROP, RENDER_UPD);
        return j;
    }

   /**
     * Converts a Weapon in a SimpleWeapon
     *
     * @param weapon    full model to convert
     * @return          reduced model
    */
    private static ClientModel.SimpleWeapon toSimpleWeapon(Weapon weapon){
        return new ClientModel().new SimpleWeapon(weapon.toString(), weapon.isLoaded());
    }


    /**
     * Converts a WeaponSquare in a SimpleSquare
     *
     * @param square    full model to convert
     * @return          reduced model
     */
    private static ClientModel.SimpleSquare toSimpleSquare(WeaponSquare square){
        List<ClientModel.SimpleWeapon> weapons = new ArrayList<>();
        for (Weapon weapon : square.getWeapons()){
            weapons.add(toSimpleWeapon(weapon));
        }
        return new ClientModel().new SimpleSquare(square.getId(), true, weapons, 0, 0, 0, false);
    }

    /**
     * Converts an AmmoSquare in a SimpleSquare
     *
     * @param square    full model to convert
     * @return          reduced model
     */
    private static ClientModel.SimpleSquare toSimpleSquare(AmmoSquare square){
        int redAmmo =0;
        int blueAmmo =0;
        int yellowAmmo =0;
        boolean powerUp = false;
        try {
            AmmoTile ammoTile = square.getAmmoTile();
            redAmmo = ammoTile.getAmmoPack().getRedAmmo();
            blueAmmo = ammoTile.getAmmoPack().getBlueAmmo();
            yellowAmmo = ammoTile.getAmmoPack().getYellowAmmo();
            powerUp = ammoTile.hasPowerUp();
        } catch (NotAvailableAttributeException e){
            LOGGER.log(Level.FINE, "No ammotile on the square: 0,0,0, false is displayed.");
        }
        return new ClientModel().new SimpleSquare(square.getId(), false, new ArrayList<>(), redAmmo, blueAmmo, yellowAmmo, powerUp);
    }
}