package data.scripts.world;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorGeneratorPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Factions;


public class RedLegionFactionRelations implements SectorGeneratorPlugin {

    //Just call initFactionRelationships: this is only intended as a means to set faction relations at start
    @Override
    public void generate(SectorAPI sector) {
        initFactionRelationships(sector);
    }

    public static void initFactionRelationships(SectorAPI sector) {
        FactionAPI rel = sector.getFaction("redlegion");
        FactionAPI player = sector.getFaction(Factions.PLAYER);

        //Set hostile to everyone else
        for (FactionAPI other : Global.getSector().getAllFactions()) {
            if (!other.getId().contains("redlegion")) {
                rel.setRelationship(other.getId(), -0.1f);
            }
        }

        //Sets player relations
        player.setRelationship(rel.getId(), RepLevel.NEUTRAL);
        //player.setRelationship(rel.getId(), RepLevel.COOPERATIVE); //testing only

        //but not pirates and dabble
        rel.setRelationship("pirates",-0.60f);
        rel.setRelationship("diableavionics", 0.0f);
        rel.setRelationship("HMI", 0.25f);
        rel.setRelationship("hegemony", -1f);
        rel.setRelationship("persean_league", 0.25f);
        rel.setRelationship("luddic_path", -1f);
        rel.setRelationship("luddic_church", -0.85f);
        rel.setRelationship("tritachyon", 0.4f);
        //todo other mod factions!
    }
}
