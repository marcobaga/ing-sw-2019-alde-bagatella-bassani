//come utilizzare final?

package it.polimi.ingsw.model;
import java.util.ArrayList;
import java.util.List;

/**
 * Class modeling a weapon card.
 *
 * @author  marcobaga
 */
public class Weapon implements Card {

    public enum WeaponName{

        LOCK_RIFLE, MACHINE_GUN, THOR, PLASMA_GUN, WHISPER, ELECTROSCYTHE, TRACTOR_BEAM,
        VORTEX_CANNON, FURNACE,HEATSEEKER, HELLION, FLAMETHROWER, GRENADE_LAUNCHER, ROCKET_LAUNCHER,
        RAILGUN, CYBERBLADE, ZX2, SHOTGUN, POWER_GLOVE, SCHOCKWAVE, SLEDGEHAMMER

    }

    private final WeaponName weaponName;
    private boolean loaded;
    private final Color color;
    private final AmmoPack fullCost;
    private final AmmoPack reducedCost;
    private Player holder;
    private final List<FireMode> fireModeList;
    private FireMode currentFireMode;
    private List<Player> mainTargets;
    private List<Player> optionalTargets;


    /**
     * Constructs a weapon with the weapon name, the weapon color, the cost to reload the weapon after it is been used,
     * the cost to reload the weapon after it is been collected and the list of the weapon firemodes.
     *
     * @param weaponName        the weapon name.
     * @param color             the weapon color.
     * @param fullCost          the cost to reload the weapon after it is been used.
     * @param reducedCost       the cost to reload the weapon after it is been collected.
     * @param fireModeList      the list of firemodes.
     */
    public Weapon(WeaponName weaponName, Color color, AmmoPack fullCost, AmmoPack reducedCost, List<FireMode> fireModeList) {

        this.weaponName = weaponName;
        this.loaded = false;
        this.color = color;
        this.fullCost = fullCost;
        this.reducedCost = reducedCost;
        this.holder = null;
        this.fireModeList = (ArrayList<FireMode>)fireModeList;
        this.currentFireMode = null;
        this.mainTargets = new ArrayList<>();
        this.optionalTargets = new ArrayList<>();

    }


    /**
     * Getters
     */

    public WeaponName getWeaponName() { return weaponName; }

    public boolean isLoaded() { return loaded; }

    public Color getColor(){ return this.color; }

    public AmmoPack getFullCost() {
        return fullCost;
    }

    public AmmoPack getReducedCost() {
        return reducedCost;
    }

    public Player getHolder() {
        return holder;
    }

    public List<FireMode> getFireModeList() { return fireModeList; }

    public FireMode getCurrentFireMode() {
        return currentFireMode;
    }

    public List<Player> getMainTargets() {
        return mainTargets;
    }

    public List<Player> getOptionalTargets() {
        return optionalTargets;
    }


    /**
     * Setters
     */

    public void setLoaded(boolean loaded) { this.loaded = loaded; }

    public void setHolder(Player holder) {
        this.holder = holder;
    }

    public void setCurrentFireMode(FireMode currentFireMode) {
        this.currentFireMode = currentFireMode;
    }


    public void setMainTargets(List<Player> mainTargets) {
        this.mainTargets = (ArrayList<Player>)mainTargets;
    }

    public void setOptionalTargets(List<Player> optionalTargets) {
        this.optionalTargets = optionalTargets;
    }


    /**
     *Lists the firemodes which can be used since they can hit a target
     *
     * @return      the list of available firemodes
     */
    public List<FireMode> listAvailableFireModes(){

        List<FireMode> available = new ArrayList<>();
        for (FireMode f : fireModeList) {
            if (f.isAvailable() && (holder.hasEnoughAmmo(f.getCost()))) {
                available.add(f);
            }
        }
        return available;

    }


    /**
     *Reloads the current weapon
     *
     * @return      true if and only if the weapon was not already loaded
     */
    public boolean reload(){

        if (loaded){
            return false;
        }
        this.loaded = true;
        return true;

    }

}