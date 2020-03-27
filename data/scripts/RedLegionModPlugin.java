package data.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;

import data.scripts.world.RedLegionGen;


public class RedLegionModPlugin extends BaseModPlugin
{
    private static void initRedLegion()
        {
            new RedLegionGen().generate(Global.getSector());
        }
    @Override
    public void onNewGame() {
        initRedLegion();
        Global.getLogger(this.getClass()).info("Hooray my mod plugin in a jar is loaded!");
    }
}
