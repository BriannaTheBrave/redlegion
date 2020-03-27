package data.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorAPI;
import data.scripts.world.RedLegionFactionRelations;
import exerelin.campaign.SectorManager;
import data.scripts.world.RedLegionGen;


public class RedLegionModPlugin extends BaseModPlugin
{
    private static void initRedLegion()
        {
            new RedLegionGen().generate(Global.getSector());
        }
    @Override
    public void onNewGame() {
        SectorAPI sector = Global.getSector();
        //initRedLegion();
        Global.getLogger(this.getClass()).info("Hooray RedLegion mod plugin in a jar is loaded!");

        //If we have Nexerelin and random worlds enabled, don't spawn our manual systems
        boolean haveNexerelin = Global.getSettings().getModManager().isModEnabled("nexerelin");
        if (!haveNexerelin || SectorManager.getManager().isCorvusMode()){
            initRedLegion();
            //new tahlan_Rubicon().generate(sector); //logic from tahlan
        }

        if (!haveNexerelin) {
            //Legio Infernalis relations
            RedLegionFactionRelations.initFactionRelationships(sector);
        }
    }
}
