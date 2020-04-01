package data.scripts.world;


import com.fs.starfarer.api.campaign.SectorAPI;

import data.scripts.world.systems.Argon;
//import data.scripts.world.systems.Dawn;
//import data.scripts.world.systems.Gale;
import data.scripts.world.systems.Tradewind;

public class RedLegionGen {

	public void generate(SectorAPI sector) {
            (new Argon()).generate(sector);
//            (new Dawn()).generate(sector);
//            (new Gale()).generate(sector);
            (new Tradewind()).generate(sector);
                                               
        }

}
