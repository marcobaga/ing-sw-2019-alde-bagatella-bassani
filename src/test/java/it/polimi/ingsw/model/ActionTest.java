package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Action;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Player;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Tests the override of equals for confrontations between Actions
 */

public class ActionTest {


    /**
     * Tests the confrontation between an Action and itself
     */
    @Test
    public void equalsSameObject() {

        //Instantiates an Action
        Action a1=new Action(1,true,false,false);

        //Checks that it's equal to itself
        assertTrue(a1.equals(a1));
    }


    /**
     * Tests the confrontation between an Action and an object that is not an Action
     */
    @Test
    public void equalsDifferentKindOfObject() {

        //Instantiates an Action and a Player
        Action a1=new Action(1,true,false,false);
        Player p1=new Player(1, Player.HeroName.VIOLET, new Board());

        //Checks that an Action it's not equal to a Player
        assertFalse(a1.equals(p1));
    }


    /**
     * Tests the confrontation between two Actions that are different
     */
    @Test
    public void equalsDifferentActions() {

        //Instantiates 2 Actions that are different
        Action a1=new Action(1,true,false,false);
        Action a2=new Action(0,false,true,false);

        //Checks that they're not equal
        assertFalse(a1.equals(a2));
    }


    /**
     * Tests the confrontation between two equals Actions
     */
    @Test
    public void equalsEqualsActions() {

        //Instantiates 2 Actions that are equal
        Action a1=new Action(1,true,false,false);
        Action a2=new Action(1,true,false,false);

        //Checks that they're equal
        assertTrue(a1.equals(a2));
    }


    /**
     * Tests the hashCode generation of two equals Actions
     */
    @Test
    public void hashCodeTest() {

        //Instantiates 2 Actions that are different
        Action a1=new Action(1,true,false,false);
        Action a2=new Action(1,true,false,false);

        //Checks that they're not equal
        assertEquals(a1.hashCode(),a2.hashCode());
    }


    /**
     * Tests the toString() method.
     */
    @Test
    public void actionToString() {

        //Instantiates 2 Actions that are different
        Action a1=new Action(1,true,false,false);
        Action a2=new Action(3,false,false,false);

        //Checks that they're not equal
        assertEquals("Move up to 1 squares. Collect.",a1.toString());
        assertEquals("Move up to 3 squares.",a2.toString());

    }

}