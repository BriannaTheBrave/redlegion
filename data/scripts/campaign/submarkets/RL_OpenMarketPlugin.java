package data.scripts.campaign.submarkets;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.submarkets.OpenMarketPlugin;
import com.fs.starfarer.api.util.Misc;
import exerelin.campaign.submarkets.Nex_OpenMarketPlugin;
import scripts.campaign.submarkets.submarketShared;

public class RL_OpenMarketPlugin extends Nex_OpenMarketPlugin {
    @Override
    public void updateCargoPrePlayerInteraction() {
        if (okToUpdateShipsAndWeapons()) {
            super.updateCargoPrePlayerInteraction(); //must do this first or anything you add isnt real!
            // this was already done in super method, so what we're doing is doubling weapon/fighter counts
            int weapons = 2 + Math.max(0, market.getSize() - 3) + (Misc.isMilitary(market) ? 5 : 0);
            int fighters = 1 + Math.max(0, (market.getSize() - 3) / 2) + (Misc.isMilitary(market) ? 2 : 0);

            // just kidding, it's only 50% more
            //weapons /= 2;
            //fighters /= 2;
            //weapons = weapons * submarketShared.getMaxWepMult();
            //fighters = fighters * submarketShared.getMinWepMult();
            Global.getLogger(this.getClass()).info("REDLEGION MARKET WEAPONS BASE: " + weapons + " is this pancake?");
            //Global.getLogger(this.getClass()).info("REDLEGION MARKET FIGHTERS: " + fighters + " is this pancake?");
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
