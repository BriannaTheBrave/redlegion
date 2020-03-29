package data.scripts.world.systems;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.Condition;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.procgen.PlanetConditionGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.StarAge;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.terrain.AsteroidFieldTerrainPlugin.AsteroidFieldParams;
import com.fs.starfarer.api.impl.campaign.terrain.BaseRingTerrain;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.impl.campaign.terrain.MagneticFieldTerrainPlugin.MagneticFieldParams;
import org.lazywizard.lazylib.MathUtils;

public class Tradewind {
    public void generate(SectorAPI sector) {
        StarSystemAPI system = sector.createStarSystem("Tradewind");
        system.getLocation().set(-18000,12000);

        system.setBackgroundTextureFilename("graphics/backgrounds/background5.jpg");

        // create the star and generate the hyperspace anchor for this system
        PlanetAPI tradewindStar = system.initStar("tradewind", // unique id for this star
                "star_yellow",  // id in planets.json
                775f, 		  // radius (in pixels at default zoom)
                500); // corona radius, from star edge
        system.setLightColor(new Color(255, 255, 153)); // light color in entire system, affects all entities

        //setup all distances here
        final float redmondDist = 190f;


        //Irradiated world              Redmond
        PlanetAPI redmond = system.addPlanet("tradewind_redmond",
                tradewindStar,
                "Redmond",
                "irradiated",
                360f*(float)Math.random(),
                105f,
                redmondDist,
                28f);

        redmond.setCustomDescriptionId("redlegion_tradewind_redmond"); //reference descriptions.csv
        PlanetConditionGenerator.generateConditionsForPlanet(redmond, StarAge.AVERAGE);


        //Luddic Church World, Barren   Jabez

        //Ruins Rich Bombarded World    Vlasta
        //Put this in a belt -- it was anihilated and the debris is still around

        //Red Legion Gas Giant          Tanda

        //Tri-Tachyon Outpost, desert   Siraj

        //Luddic Church Station         Ervar

        //Frozen Ruins Rich World       Koen

        //todo add 2 derelicts, a probe, and 3 blocks of salvage, plus a large asteroid field and a nebula, 2 small asteroid fields,
    }
}
