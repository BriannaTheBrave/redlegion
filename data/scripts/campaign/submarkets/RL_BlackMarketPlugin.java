package data.scripts.campaign.submarkets;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import com.fs.starfarer.api.util.Misc;
import exerelin.campaign.submarkets.Nex_BlackMarketPlugin;
import scripts.campaign.submarkets.submarketShared;
import com.fs.starfarer.api.impl.campaign.ids.Factions;

public class RL_BlackMarketPlugin extends Nex_BlackMarketPlugin {
    @Override
    public void updateCargoPrePlayerInteraction() {
        if (okToUpdateShipsAndWeapons()) {
            super.updateCargoPrePlayerInteraction(); //must do this first or anything you add isnt real!

            // this was already done in super method, so what we're doing is doubling weapon/fighter counts
            int weapons = 4 + Math.max(0, market.getSize() - 3) + (Misc.isMilitary(market) ? 5 : 0);
            int fighters = 2 + Math.max(0, (market.getSize() - 3) / 2) + (Misc.isMilitary(market) ? 2 : 0);

            // just kidding, it's only 50% more
            //weapons /= 2;
            //fighters /= 2;

            WeightedRandomPicker<String> factionPicker = new WeightedRandomPicker<String>();
            factionPicker.add(market.getFactionId(), 15f - market.getStabilityValue());
            factionPicker.add(Factions.INDEPENDENT, 4f);
            factionPicker.add(submarket.getFaction().getId(), 6f);
            Global.getLogger(this.getClass()).info("REDLEGION MARKET WEAPONS BASE: " + (weapons * submarketShared.getMinWepMult()) + " is this pancake?");
            for (int x = 1; x < submarketShared.getMinWepMult(); x++){
                Global.getLogger(this.getClass()).info("flapjack " + x);
                addWeapons(weapons, weapons, 1, market.getFactionId());
                addFighters(fighters, fighters, 1, market.getFactionId());
            }

            getCargo().sort();
        }
        else {
            super.updateCargoPrePlayerInteraction(); //this makes sure the normal commodities get worked out
        }

    }
}
