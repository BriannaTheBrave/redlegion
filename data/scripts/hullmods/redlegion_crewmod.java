package data.scripts.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.util.Misc;

public class redlegion_crewmod extends BaseHullMod {

    private static final float GRIT_HULL_BOOST = 1.1f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getHullBonus().modifyMult("redlegion_crewmod", GRIT_HULL_BOOST);
    }

    @Override //All you need is this to be honest. The framework will do everything on its own.
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        if (ship.getVariant().hasHullMod("CHM_commission")) {
            ship.getVariant().removeMod("CHM_commission");
        }
        // This is to remove the unnecessary dummy hull mod. Unless the player want it... but nah!
    }

    @Override
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        return Math.round((GRIT_HULL_BOOST-1)*100)+"%";
    }
}
