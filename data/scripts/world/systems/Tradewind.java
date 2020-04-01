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
import data.scripts.Utils;

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
        final float redmondDist = 270f;
        final float jabezDist = 450f;
        final float stable1Dist = 585f;
        final float asteroidBelt1Dist = 740f;
        final float jumpInnerDist = 755f;
        final float vlastaDist = 760f;
        final float tandaDist = 900f;
        final float stable2Dist = 1200f;
        final float stable3Dist = 1200f;
        final float ervarDist = 1350f;
        final float jumpOuterDist = 1355f;
        final float koenDist = 1650f;


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
        redmond.getMarket().addCondition(Conditions.RUINS_SCATTERED);
        redmond.getMarket().addCondition(Conditions.VERY_HOT);
        redmond.getMarket().addCondition(Conditions.NO_ATMOSPHERE);
        redmond.getMarket().addCondition(Conditions.IRRADIATED);
        redmond.getMarket().addCondition(Conditions.LOW_GRAVITY);
        redmond.getMarket().addCondition(Conditions.RARE_ORE_SPARSE);
        redmond.getMarket().addCondition(Conditions.ORE_MODERATE);

        //Luddic Church World, Barren   Jabez
        PlanetAPI jabez = system.addPlanet("jabez",
                tradewindStar,
                "Jabez",
                "barren2",
                360f*(float)Math.random(),
                180f,
                jabezDist,
                210f);

        jabez.setCustomDescriptionId("redlegion_tradewind_jabez"); //reference descriptions.csv

        MarketAPI jabez_market = Utils.addMarketplace("luddic_church", jabez, null,
                "Jabez",
                4,
                new ArrayList<>(
                        Arrays.asList(
                                Conditions.POPULATION_4,
                                Conditions.ORE_RICH,
                                Conditions.NO_ATMOSPHERE,
                                Conditions.COLD,
                                Conditions.LUDDIC_MAJORITY,
                                Conditions.RUINS_EXTENSIVE
                        )
                ),
                new ArrayList<>(
                        Arrays.asList(
                                Submarkets.SUBMARKET_OPEN,
                                Submarkets.SUBMARKET_STORAGE,
                                Submarkets.SUBMARKET_BLACK
                                //todo how to add luddic knight submarket?
                        )
                ),
                new ArrayList<>(
                        Arrays.asList(
                                Industries.POPULATION,
                                Industries.SPACEPORT,
                                Industries.MINING, //todo make a custom industry called tech-purge which is tech-mining but it doesnt make items, only destroys them
                                Industries.TECHMINING,
                                Industries.ORBITALSTATION,
                                Industries.GROUNDDEFENSES,
                                Industries.WAYSTATION,
                                Industries.PATROLHQ
                        )
                ),
                0.30f,
                false,
                false);

        //stable point 1
        SectorEntityToken stableLoc1 = system.addCustomEntity("tradewind_stableloc_1", "Stable Location", "stable_location", Factions.NEUTRAL);
        stableLoc1.setCircularOrbit(tradewindStar, MathUtils.getRandomNumberInRange(0f,360f),stable1Dist, 520);

        //Ruins Rich Bombarded World    Vlasta
        //Put this in a belt -- it was annihilated and the debris is still around
        system.addAsteroidBelt(tradewindStar, 700, asteroidBelt1Dist, 800, 250, 400, Terrain.ASTEROID_BELT, "Shattered Heart");
        system.addRingBand(tradewindStar, "misc", "rings_asteroids08", 256f, 0, Color.gray, 256f, asteroidBelt1Dist, 400f);

        PlanetAPI vlasta = system.addPlanet("vlasta",
                tradewindStar,
                "Vlasta",
                "barren-bombarded",
                360f*(float)Math.random(),
                225f,
                vlastaDist,
                421f);

        tradewindStar.setCustomDescriptionId("redlegion_tradewind_vlasta"); //reference descriptions.csv
        vlasta.getMarket().addCondition(Conditions.NO_ATMOSPHERE);
        vlasta.getMarket().addCondition(Conditions.RUINS_WIDESPREAD);
        vlasta.getMarket().addCondition(Conditions.IRRADIATED);
        vlasta.getMarket().addCondition(Conditions.VERY_COLD);
        vlasta.getMarket().addCondition(Conditions.LOW_GRAVITY);
        vlasta.getMarket().addCondition(Conditions.ORE_RICH);
        vlasta.getMarket().addCondition(Conditions.RARE_ORE_RICH);
        vlasta.getMarket().addCondition(Conditions.TECTONIC_ACTIVITY);
        vlasta.getMarket().addCondition(Conditions.ORBITAL_BURNS); //todo might not be what I think it is

        //Inner Jump Point
        JumpPointAPI jumpPoint1 = Global.getFactory().createJumpPoint(
                "inner_jump",
                "Inner System Jump");
        jumpPoint1.setCircularOrbit(system.getEntityById("tradewind"), 90, jumpInnerDist, 222);

        jumpPoint1.setStandardWormholeToHyperspaceVisual();
        system.addEntity(jumpPoint1);

        //Red Legion Gas Giant          Tanda
        PlanetAPI tanda = system.addPlanet("tanda",
                tradewindStar,
                "Tanda",
                "gas_giant",
                360f*(float)Math.random(),
                500,
                tandaDist,
                521f);

        tanda.setCustomDescriptionId("redlegion_tradewind_tanda"); //reference descriptions.csv
        MarketAPI tanda_market = Utils.addMarketplace("redlegion", tanda, null,
                "Tanda",
                5,
                new ArrayList<>(
                        Arrays.asList(
                                Conditions.POPULATION_5,
                                Conditions.RUINS_EXTENSIVE,
                                Conditions.HIGH_GRAVITY,
                                Conditions.STEALTH_MINEFIELDS,
                                Conditions.AI_CORE_ADMIN,
                                Conditions.VOLATILES_PLENTIFUL,
                                Conditions.DENSE_ATMOSPHERE
                        )
                ),
                new ArrayList<>(
                        Arrays.asList(
                                Submarkets.GENERIC_MILITARY,
                                Submarkets.SUBMARKET_OPEN,
                                Submarkets.SUBMARKET_STORAGE,
                                Submarkets.SUBMARKET_BLACK
                        )
                ),
                new ArrayList<>(
                        Arrays.asList(
                                Industries.POPULATION,
                                Industries.MEGAPORT,
                                Industries.MINING,
                                Industries.BATTLESTATION_HIGH,
                                Industries.HEAVYBATTERIES,
                                Industries.PATROLHQ,
                                Industries.WAYSTATION
                        )
                ),
                0.18f,
                true,
                true);

        tanda_market.addIndustry(Industries.FUELPROD, new ArrayList<String>(Arrays.asList(Items.SYNCHROTRON))); //couldn't find another way to add w/ synch!
        tanda_market.getIndustry(Industries.PATROLHQ).setAICoreId(Commodities.BETA_CORE);
        tanda_market.getIndustry(Industries.BATTLESTATION_HIGH).setAICoreId(Commodities.ALPHA_CORE);
        tanda_market.getIndustry(Industries.MEGAPORT).setAICoreId(Commodities.BETA_CORE);
        tanda_market.getIndustry(Industries.MINING).setAICoreId(Commodities.GAMMA_CORE);
        tanda_market.getIndustry(Industries.POPULATION).setAICoreId(Commodities.BETA_CORE);
        tanda_market.getIndustry(Industries.WAYSTATION).setAICoreId(Commodities.GAMMA_CORE);
        tanda_market.getIndustry(Industries.FUELPROD).setAICoreId(Commodities.ALPHA_CORE);

        system.addRingBand(tanda, "misc", "rings_dust0", 256f, 3, Color.white, 256f, 800, 360f, null, null);

        //Tri-Tachyon Outpost, desert   Siraj
        PlanetAPI siraj = system.addPlanet("siraj",
                tanda,
                "Siraj",
                "desert1",
                360f*(float)Math.random(),
                150f,
                785,
                180f);

        siraj.setCustomDescriptionId("redlegion_tradewind_siraj"); //reference descriptions.csv

        MarketAPI siraj_market = Utils.addMarketplace("tritachyon", siraj, null,
                "Siraj",
                4,
                new ArrayList<>(
                        Arrays.asList(
                                Conditions.POPULATION_4,
                                Conditions.ORE_SPARSE,
                                Conditions.RARE_ORE_ABUNDANT,
                                Conditions.FARMLAND_POOR,
                                Conditions.HABITABLE,
                                Conditions.HOT,
                                Conditions.DESERT,
                                Conditions.OUTPOST,
                                Conditions.ORGANICS_TRACE,
                                Conditions.RUINS_WIDESPREAD
                        )
                ),
                new ArrayList<>(
                        Arrays.asList(
                                Submarkets.SUBMARKET_OPEN,
                                Submarkets.SUBMARKET_STORAGE,
                                Submarkets.SUBMARKET_BLACK
                        )
                ),
                new ArrayList<>(
                        Arrays.asList(
                                Industries.POPULATION,
                                Industries.SPACEPORT,
                                Industries.MINING,
                                Industries.TECHMINING,
                                Industries.ORBITALSTATION_HIGH,
                                Industries.GROUNDDEFENSES
                        )
                ),
                0.18f,
                true,
                false);

        //stable point 2
        SectorEntityToken stableLoc2 = system.addCustomEntity("tradewind_stableloc_2", "Stable Location", "stable_location", Factions.NEUTRAL);
        stableLoc2.setCircularOrbit(tradewindStar, 100,stable2Dist, 820);

        //stable point 3
        SectorEntityToken stableLoc3 = system.addCustomEntity("tradewind_stableloc_3", "Stable Location", "stable_location", Factions.NEUTRAL);
        stableLoc3.setCircularOrbit(tradewindStar, 200,stable3Dist, 820);

        //Luddic Church Station         Ervar
        SectorEntityToken ervar = system.addCustomEntity("ervar", "Ervar", "station_lowtech2", "luddic_church");
        ervar.setCircularOrbitPointingDown(tradewindStar,0,ervarDist,999);
        ervar.setCustomDescriptionId("redlegion_tradewind_ervar");
        MarketAPI ervar_market = Utils.addMarketplace("luddic_church", ervar, null,
                "Ervar",
                4,
                new ArrayList<>(
                        Arrays.asList(
                                Conditions.POPULATION_4,
                                Conditions.NO_ATMOSPHERE,
                                Conditions.OUTPOST,
                                Conditions.LUDDIC_MAJORITY
                        )
                ),
                new ArrayList<>(
                        Arrays.asList(
                                Submarkets.GENERIC_MILITARY, //todo luddic knights
                                Submarkets.SUBMARKET_STORAGE
                        )
                ),
                new ArrayList<>(
                        Arrays.asList(
                                Industries.POPULATION,
                                Industries.SPACEPORT,
                                Industries.BATTLESTATION,
                                Industries.HEAVYBATTERIES,
                                Industries.MILITARYBASE,
                                Industries.HEAVYINDUSTRY,
                                Industries.WAYSTATION
                        )
                ),
                0.18f,
                false,
                false);

        // outer jump
        JumpPointAPI jumpPoint2 = Global.getFactory().createJumpPoint(
                "outer_jump",
                "Outer System Jump");
        jumpPoint2.setCircularOrbit(system.getEntityById("tradewind"), 10, jumpOuterDist, 999);

        jumpPoint2.setStandardWormholeToHyperspaceVisual();
        system.addEntity(jumpPoint2);

        //Frozen Ruins Rich World       Koen
        PlanetAPI koen = system.addPlanet("koen",
                tradewindStar,
                "Koen",
                "frozen",
                360*(float)Math.random(),
                190f,
                koenDist,
                1421f);

        koen.setCustomDescriptionId("redlegion_tradewind_koen"); //reference descriptions.csv
        koen.getMarket().addCondition(Conditions.RUINS_EXTENSIVE);
        koen.getMarket().addCondition(Conditions.VERY_COLD);
        koen.getMarket().addCondition(Conditions.ABANDONED_STATION); //todo what dis?
        koen.getMarket().addCondition(Conditions.THIN_ATMOSPHERE);
        koen.getMarket().addCondition(Conditions.DARK);
        koen.getMarket().addCondition(Conditions.ORE_RICH);
        koen.getMarket().addCondition(Conditions.RARE_ORE_RICH);

        //todo add 2 derelicts, a probe, and 3 blocks of salvage, plus a large asteroid field and a nebula, 2 small asteroid fields,

        system.autogenerateHyperspaceJumpPoints(true, true);

        HyperspaceTerrainPlugin plugin = (HyperspaceTerrainPlugin) Misc.getHyperspaceTerrain().getPlugin();
        NebulaEditor editor = new NebulaEditor(plugin);
        float minRadius = plugin.getTileSize() * 2f;

        float radius = system.getMaxRadiusInHyperspace();
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius, 0, 360f);
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius, 0, 360f, 0.25f);
    }
}
