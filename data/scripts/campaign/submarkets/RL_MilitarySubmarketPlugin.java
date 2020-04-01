package data.scripts.campaign.submarkets;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.submarkets.OpenMarketPlugin;
import com.fs.starfarer.api.util.Misc;
import exerelin.campaign.submarkets.Nex_MilitarySubmarketPlugin;
import scripts.campaign.submarkets.submarketShared;

public class RL_MilitarySubmarketPlugin extends Nex_MilitarySubmarketPlugin {
    @Override
    public void updateCargoPrePlayerInteraction() {
        Global.getLogger(this.getClass()).info("REDLEGION MARKET WEAPONS CHECK  is this muffin?");
        if (okToUpdateShipsAndWeapons()) {
            super.updateCargoPrePlayerInteraction(); //must do this first or anything you add isnt real!
            // this was already done in super method, so what we're doing is doubling weapon/fighter counts
            int weapons = 4 + Math.max(0, market.getSize() - 3) * 2;
            int fighters = 2 + Math.max(0, market.getSize() - 3);

            // just kidding, it's only 50% more
            //weapons /= 2;
            //fighters /= 2;
            Global.getLogger(this.getClass()).info("REDLEGION MARKET WEAPONS BASE: " + weapons + " is this pancake?");
            for (int x = 1; x < submarketShared.getMinWepMult(); x++){
                Global.getLogger(this.getClass()).info("flapjack " + x);
                addWeapons(weapons, weapons, 1, market.getFactionId());
                addFighters(fighters, fighters, 1, market.getFactionId());
            }
            //todo are militaries really only making up to tier 0?? I made it 1... let's see
            getCargo().sort();
        }
        else {
            super.updateCargoPrePlayerInteraction(); //this makes sure the normal commodities get worked out
        }
    }
}
