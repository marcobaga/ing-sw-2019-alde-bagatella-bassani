//TODO test three remaining power ups

package it.polimi.ingsw.model;

import java.util.Arrays;
import java.util.List;

import it.polimi.ingsw.controller.BoardConfigurer;
import it.polimi.ingsw.controller.PowerUpFactory;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Player;
import it.polimi.ingsw.model.cards.Color;
import it.polimi.ingsw.model.cards.PowerUp;
import it.polimi.ingsw.model.exceptions.NoMoreCardsException;
import it.polimi.ingsw.model.exceptions.NotAvailableAttributeException;
import it.polimi.ingsw.model.exceptions.UnacceptableItemNumberException;
import org.junit.Test;
import static org.junit.Assert.*;


public class PowerUpTest {

    /**
     * Checks that a power up is correctly marked as available
     */
    @Test
    public void isAvailable() throws UnacceptableItemNumberException, NoMoreCardsException, NotAvailableAttributeException {
        Board b = BoardConfigurer.getInstance().simulateScenario();
        PowerUp p = (PowerUp) b.getPowerUpDeck().drawCard();
        while(p.getName()!= PowerUp.PowerUpName.TARGETING_SCOPE){
            p = (PowerUp) b.getPowerUpDeck().drawCard();
        }
        p.setHolder(b.getPlayers().get(0));
        b.getPlayers().get(0).getPowerUpList().add(p);
        p = b.getPlayers().get(0).getPowerUpList().get(0);
        b.getPlayers().get(1).setJustDamaged(true);
        assertTrue(p.isAvailable());
    }

    /**
     * Checks that a power up is not available when no targets can be seen
     */
    @Test
    public void isAvailable2() throws UnacceptableItemNumberException, NoMoreCardsException, NotAvailableAttributeException {
        Board b = BoardConfigurer.getInstance().simulateScenario();
        PowerUp p = (PowerUp) b.getPowerUpDeck().drawCard();
        while(p.getName()!= PowerUp.PowerUpName.TARGETING_SCOPE){
            p = (PowerUp) b.getPowerUpDeck().drawCard();
        }
        p.setHolder(b.getPlayers().get(4));
        b.getPlayers().get(4).getPowerUpList().add(p);
        p = b.getPlayers().get(4).getPowerUpList().get(0);
        assertFalse(p.isAvailable());
    }

    /**
     * Checks that a power up is not available when players are visible but cannot be selected
     */
    @Test
    public void isAvailable3() throws UnacceptableItemNumberException, NoMoreCardsException, NotAvailableAttributeException {
        Board b = BoardConfigurer.getInstance().simulateScenario();
        PowerUp p = (PowerUp) b.getPowerUpDeck().drawCard();
        while(p.getName()!= PowerUp.PowerUpName.TARGETING_SCOPE){
            p = (PowerUp) b.getPowerUpDeck().drawCard();
        }
        p.setHolder(b.getPlayers().get(0));
        b.getPlayers().get(0).getPowerUpList().add(p);
        p = b.getPlayers().get(0).getPowerUpList().get(0);
        assertFalse(p.isAvailable());
    }

    /**
     * Checks that the power up applies its effect
     */
    @Test
    public void applyEffects() throws UnacceptableItemNumberException, NoMoreCardsException, NotAvailableAttributeException {
        Board b = BoardConfigurer.getInstance().simulateScenario();
        PowerUp p = (PowerUp) b.getPowerUpDeck().drawCard();
        while(p.getName()!= PowerUp.PowerUpName.TARGETING_SCOPE){
            p = (PowerUp) b.getPowerUpDeck().drawCard();
        }
        p.setHolder(b.getPlayers().get(0));
        b.getPlayers().get(0).getPowerUpList().add(p);
        p = b.getPlayers().get(0).getPowerUpList().get(0);
        p.applyEffects(Arrays.asList(b.getPlayers().get(1)), p.getHolder().getPosition());
        assertTrue(b.getPlayers().get(1).isJustDamaged());
    }

    /**
     * Checks that targets are selected when there are some
     */
    @Test
    public void findTargets() throws UnacceptableItemNumberException, NoMoreCardsException, NotAvailableAttributeException {
        Board b = BoardConfigurer.getInstance().simulateScenario();
        PowerUp p = (PowerUp) b.getPowerUpDeck().drawCard();
        while(p.getName()!= PowerUp.PowerUpName.TARGETING_SCOPE){
            p = (PowerUp) b.getPowerUpDeck().drawCard();
        }
        p.setHolder(b.getPlayers().get(0));
        b.getPlayers().get(0).getPowerUpList().add(p);
        p = b.getPlayers().get(0).getPowerUpList().get(0);
        b.getPlayers().get(1).setJustDamaged(true);
        assertFalse(p.findTargets().isEmpty());
        List<Player> ap = p.findTargets().get(0);
        assertTrue(ap.contains(b.getPlayers().get(1)));
    }

    /**
     * Checks that targets are selected when there are none
     */
    @Test
    public void findTargets2() throws UnacceptableItemNumberException, NoMoreCardsException, NotAvailableAttributeException {
        Board b = BoardConfigurer.getInstance().simulateScenario();
        PowerUp p = (PowerUp) b.getPowerUpDeck().drawCard();
        while(p.getName()!= PowerUp.PowerUpName.TARGETING_SCOPE){
            p = (PowerUp) b.getPowerUpDeck().drawCard();
        }
        p.setHolder(b.getPlayers().get(0));
        b.getPlayers().get(0).getPowerUpList().add(p);
        p = b.getPlayers().get(0).getPowerUpList().get(0);
        assertTrue(p.findTargets().isEmpty());
    }

    /**
     * Checks that destinationFinder is working correctly (Note: the only power up implemented so far always returns null as intended)
     */
    @Test
    public void findDestinations() throws UnacceptableItemNumberException, NoMoreCardsException, NotAvailableAttributeException {
        Board b = BoardConfigurer.getInstance().simulateScenario();
        PowerUp p = (PowerUp) b.getPowerUpDeck().drawCard();
        while(p.getName()!= PowerUp.PowerUpName.TARGETING_SCOPE){
            p = (PowerUp) b.getPowerUpDeck().drawCard();
        }
        p.setHolder(b.getPlayers().get(0));
        b.getPlayers().get(0).getPowerUpList().add(p);
        p = b.getPlayers().get(0).getPowerUpList().get(0);
        assertTrue(p.findDestinations(b.getPlayers()).isEmpty());
        p.setHolder(b.getPlayers().get(0));
        b.getPlayers().get(0).getPowerUpList().add(p);
        p = b.getPlayers().get(0).getPowerUpList().get(0);
        assertTrue(p.findDestinations(b.getPlayers()).isEmpty());
    }


    /**
     * Tests the method toString() of the enumeration PowerUpName.
     */
    @Test
    public void powerUpNameToString(){
        assertEquals("Targeting Scope", PowerUp.PowerUpName.TARGETING_SCOPE.toString() );
        assertEquals("Newton", PowerUp.PowerUpName.NEWTON.toString());
        assertEquals("Tagback Grenade", PowerUp.PowerUpName.TAGBACK_GRENADE.toString());
        assertEquals("Teleporter", PowerUp.PowerUpName.TELEPORTER.toString());

    }


    /**
     * Tests the method toString() of the class PowerUp.
     */
    @Test
    public void powerUpToString(){

        PowerUpFactory powerUpFactory = new PowerUpFactory(new Board());
        PowerUp powerUp = powerUpFactory.createPowerUp(PowerUp.PowerUpName.TARGETING_SCOPE, Color.YELLOW);

        //the name and the color are correct
        System.out.println("\nTesting Powerup.toString().\nYellow Targeting Scope. The output is printed to console since it is not possible to check the color of a string in another way.\n" );
        //it is shown through a println() since it is not possible to check the color of a string in another way
        System.out.println(powerUp.toString());

    }

}