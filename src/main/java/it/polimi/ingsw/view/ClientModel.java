package it.polimi.ingsw.view;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the state of the game. This model is sensibly reduced compared to that of the server.
 * All methods are getters and setters and no game logic is contained in this model. Only information necessary
 * to display the state of the game is contained. This model can be modified with incremental updates, but, for
 * the sake of consistency, is replaced with a copy of the server model at the beginning of each turn.
 *
 * @author  marcobaga, BassaniRiccardo
 */
public class ClientModel {

    private List<SimpleSquare> squares;
    private List<SimplePlayer> players;

    private int weaponCardsLeft;
    private int powerUpCardsLeft;

    private int mapID;
    private boolean[][] leftWalls;
    private boolean[][] topWalls;

    private int currentPlayerId;
    private List<SimplePlayer> killShotTrack;
    private int skullsLeft;
    private int points;
    private List<String> powerUpInHand;
    private List<String> colorPowerUpInHand;
    private int playerID;

    public ClientModel(){
        //attributes in this class need to be initialized one at a time
    }

    /**
     * A simplified version of Square, containing what the user should see.
     */
    public class SimpleSquare {

        int id;
        boolean spawnPoint;
        List<SimpleWeapon> weapons;
        int blueAmmo;
        int redAmmo;
        int yellowAmmo;
        boolean powerup;

        public SimpleSquare(int id, boolean spawnPoint, List<SimpleWeapon> weapons, int redAmmo, int blueAmmo, int yellowAmmo, boolean powerup) {
            this.id = id;
            this.spawnPoint = spawnPoint;
            this.weapons = weapons;
            this.redAmmo = redAmmo;
            this.blueAmmo = blueAmmo;
            this.yellowAmmo = yellowAmmo;
            this.powerup = powerup;
        }

        public int getBlueAmmo() { return blueAmmo; }
        void setBlueAmmo(int blueAmmo) { this.blueAmmo = blueAmmo; }
        public int getRedAmmo() { return redAmmo; }
        void setRedAmmo(int redAmmo) { this.redAmmo = redAmmo; }
        public int getYellowAmmo() { return yellowAmmo; }
        void setYellowAmmo(int yellowAmmo) { this.yellowAmmo = yellowAmmo; }
        public boolean isPowerup() { return powerup; }
        public void setPowerup(boolean powerup) { this.powerup = powerup; }
        public int getId(){return id;}
        boolean isSpawnPoint() {return spawnPoint;}
        public List<SimpleWeapon> getWeapons() {       return weapons;    }
        public void setWeapons(List<SimpleWeapon> weapons) {   this.weapons = weapons; }

        void removeWeapon(String name){
            for(SimpleWeapon w : weapons){
                if(w.getName().equals(name)){
                    weapons.remove(w);
                    return;
                }
            }
        }
    }

    /**
     * A simplified version of Player, containing only information about other players that the user should see.
     */
    public class SimplePlayer{

        private int id;
        private String color;
        private int cardNumber;
        private List<Integer> damage;
        private List<Integer> marks;
        private List<SimpleWeapon> weapons;
        private SimpleSquare position;
        private String username;
        private int redAmmo;
        private int blueAmmo;
        private int yellowAmmo;
        private boolean flipped;
        private boolean inGame;
        private int points;
        private int deaths;
        private int nextDeathAwards;
        private String status;

        public SimplePlayer(int id, String color, int cardNumber, List<Integer> damage, List<Integer> marks, List<SimpleWeapon> weapons, SimpleSquare position, String username, int redAmmo, int blueAmmo, int yellowAmmo, boolean inGame, boolean flipped, int points, int deaths, int nextDeathAwards, String status) {
            this.id = id;
            this.color = color;
            this.cardNumber = cardNumber;
            this.damage = damage;
            this.marks = marks;
            this.weapons = weapons;
            this.position = position;
            this.username = username;
            this.redAmmo = redAmmo;
            this.blueAmmo = blueAmmo;
            this.yellowAmmo = yellowAmmo;
            this.inGame = inGame;
            this.flipped = flipped;
            this.points = points;
            this.deaths = deaths;
            this.nextDeathAwards = nextDeathAwards;
            this.status = status;
        }

        void setStatus(String status, boolean flipped){
            this.flipped = flipped;
            this.status = status;
        }
        public int getNextDeathAwards(){
            return nextDeathAwards;
        }

        void setNextDeathAwards(int nextDeathAwards){
            this.nextDeathAwards = nextDeathAwards;
        }

        public String getStatus(){
            return this.status;
        }

        public boolean isFlipped() {
            return this.flipped;
        }

        boolean getInGame(){
            return this.inGame;
        }

        void setInGame(boolean inGame){
            this.inGame = inGame;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public int getCardNumber() {
            return cardNumber;
        }

        void setCardNumber(int cardNumber) {
            this.cardNumber = cardNumber;
        }

        public List<Integer> getDamageID() {
            return damage;
        }

        public List<Integer> getMarksID() {
            return marks;
        }

        public List<SimpleWeapon> getWeapons() {
            return weapons;
        }

        public void setWeapons(List<SimpleWeapon> weapons) {
            this.weapons = weapons;
        }

        public SimpleSquare getPosition() {
            return position;
        }

        public void setPosition(SimpleSquare position) {
            this.position = position;
        }

        public String getUsername() {
            return username;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public int getDeaths() {
            return deaths;
        }

        void addDeath() {
            this.deaths++;
        }

        /**
         * Complex getters and setters
         */
        List<SimplePlayer> toSimplePlayerList(List<Integer> ids, List<SimplePlayer> players){

            List<SimplePlayer> toReturn = new ArrayList<>();

            for (Integer i: ids){
                for(SimplePlayer p : players)
                    if(p.getId()==i)
                        toReturn.add(p);
            }
            return toReturn;
        }

        List<SimplePlayer> getDamage(List<SimplePlayer> players){ return toSimplePlayerList (damage, players); }

        List<SimplePlayer> getMark(List<SimplePlayer> players){ return toSimplePlayerList (marks, players); }

        void pickUpWeapon(String name){

            for(SimpleWeapon w : this.position.getWeapons()){
                if(w.getName().equals(name)){
                    this.position.getWeapons().remove(w);
                    this.weapons.add(w);
                    return;
                }
            }
        }

        void discardWeapon(String name){
            for(SimpleWeapon w : this.position.getWeapons()){
                if(w.getName().equals(name)){
                    this.position.getWeapons().add(w);
                    this.weapons.remove(w);
                    return;
                }
            }
        }

        void addAmmo(int r, int b, int y){
            redAmmo=redAmmo+r;
            blueAmmo=blueAmmo+b;
            yellowAmmo=yellowAmmo+y;
        }

        void subAmmo(int r, int b, int y){
            redAmmo=redAmmo-r;
            blueAmmo=blueAmmo-b;
            yellowAmmo=yellowAmmo-y;
        }

        SimpleWeapon getWeapon(String name){
            for(SimpleWeapon s : weapons){
                if(s.getName().equals(name)){
                    return s;
                }
            }
            return new SimpleWeapon("error", false);
        }

        public int getBlueAmmo() {
            return blueAmmo;
        }

        public int getRedAmmo() {
            return redAmmo;
        }

        public int getYellowAmmo() {
            return yellowAmmo;
        }
    }


    /**
     * A simplified version of Weapon, containing only what the user should see.
     */
    public class SimpleWeapon{
        String name;
        boolean loaded;

        public SimpleWeapon(String name, boolean loaded) {
            this.name = name;
            this.loaded = loaded;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isLoaded() {
            return this.loaded;
        }

        void setLoaded(boolean loaded) {
            this.loaded = loaded;
        }
    }


    /**
     * Getters and setters related to the whole model
     */
    public List<SimpleSquare> getSquares() {
        return squares;
    }

    public void setSquares(List<SimpleSquare> squares) {
        this.squares = squares;
    }

    public List<SimplePlayer> getPlayers() {
        return players;
    }

    public void setPlayers(List<SimplePlayer> players) {
        this.players = players;
    }

    public int getWeaponCardsLeft() {
        return weaponCardsLeft;
    }

    public void setWeaponCardsLeft(int weaponCardsLeft) {
        this.weaponCardsLeft = weaponCardsLeft;
    }

    public int getPowerUpCardsLeft() {
        return powerUpCardsLeft;
    }

    public void setPowerUpCardsLeft(int powerUpCardsLeft) {
        this.powerUpCardsLeft = powerUpCardsLeft;
    }

    public int getMapID() {
        return mapID;
    }

    public void setMapID(int mapID) {
        this.mapID = mapID;
    }

    public SimplePlayer getCurrentPlayer() {
        return getPlayer(currentPlayerId);
    }

    public void setCurrentPlayerId(int currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public List<SimplePlayer> getKillShotTrack() {
        return killShotTrack;
    }

    public void setKillShotTrack(List<SimplePlayer> killShotTrack) {
        this.killShotTrack = killShotTrack;
    }

    public int getPoints(){return this.points;}

    public void setPoints(int points){this.points = points;}

    public List<String> getPowerUpInHand() {
        return powerUpInHand;
    }

    public void setPowerUpInHand(List<String> powerUpInHand) {
        this.powerUpInHand = powerUpInHand;
    }

    public List<String> getColorPowerUpInHand() {return colorPowerUpInHand;}

    public void setColorPowerUpInHand(List<String> colorPowerUpInHand) {this.colorPowerUpInHand = colorPowerUpInHand;}

    public int getSkullsLeft() {return skullsLeft;}

    public void setSkullsLeft(int skullsLeft) {this.skullsLeft = skullsLeft;}

    public int getPlayerID() {return playerID;}

    public void setPlayerID(int playerID) {this.playerID = playerID;}

    public boolean[][] getLeftWalls() { return leftWalls;}

    public void setLeftWalls(boolean[][] leftWalls) { this.leftWalls = leftWalls; }

    public boolean[][] getTopWalls() { return topWalls; }

    public void setTopWalls(boolean[][] topWalls) { this.topWalls = topWalls;}

    void removeSkulls(int n){
        if(n<skullsLeft) {
            skullsLeft = skullsLeft - n;
        }
    }

    void moveTo(int player, int square) {
        SimplePlayer p = getPlayer(player);
        SimpleSquare s = getSquare(square);
        p.setPosition(s);
    }

    public void damage(int player, JsonArray damage){
        List<Integer> list = getPlayer(player).getDamageID();
        list.clear();
        for(JsonElement j : damage){
            list.add((j.getAsInt()));
        }
    }

    public void mark(int player, JsonArray marks){
        List<Integer> list = getPlayer(player).getMarksID();
        list.clear();
        for(JsonElement j : marks){
            list.add((j.getAsInt()));
        }
    }

    public SimplePlayer getPlayer(int id){
        for(SimplePlayer s : players){
            if(s.getId() == id){
                return s;
            }
        }
        if(!players.isEmpty()) {
            return players.get(0);
        } else {
            return new SimplePlayer(0, "blue", 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), getSquare(0), "default", 0, 0, 0, false, false, 0, 0, 0, "BASIC");
        }
    }

    public SimpleSquare getSquare(int id){
        for(SimpleSquare s : squares){
            if(s.getId()==id){
                return s;
            }
        }
        if(!squares.isEmpty()) {
            return squares.get(0);
        } else {
            return new SimpleSquare(0, false, new ArrayList<>(), 0, 0, 0, false);
        }
    }

    /**
     * Static method returning ASCII escape codes
     *
     * @param color     required color
     * @return          ASCII escape code as a String
     */
    public static String getEscapeCode(String color){
        if(color==null){
            return "\u001b[35m";
        }
        switch(color){
            case "black": return "\u001b[30m";
            case "red": return "\u001b[31m";
            case "green": return "\u001b[32m";
            case "yellow": return "\u001b[33m";
            case "blue": return "\u001b[34m";
            case "purple": return "\u001b[35m";
            case "cyan": return "\u001b[36m";
            case "grey": return "\u001b[37m";
            case "reset": return "\u001b[0m";
            default: return "";
        }
    }
}