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
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.impl.campaign.procgen.*;
import com.fs.starfarer.api.impl.campaign.procgen.themes.DerelictThemeGenerator;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.BaseSalvageSpecial;
import com.fs.starfarer.api.impl.campaign.terrain.AsteroidFieldTerrainPlugin.AsteroidFieldParams;
import com.fs.starfarer.api.impl.campaign.terrain.BaseRingTerrain;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin.DebrisFieldParams;
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
        system.setLightColor(new Color(255, 255, 223)); // light color in entire system, affects all entities

        //setup all distances here
        final float redmondDist = 1270f;
        final float jabezDist = 2850f;
        final float stable1Dist = 2885f;
        final float asteroids1Dist = 3150f;
        final float asteroidBelt1Dist = 4440f;
        final float jumpInnerDist = 4755f;
        final float vlastaDist = 4960f;
        final float asteroids2Dist = 5500f;
        final float tandaDist = 7000f;
        final float stable2Dist = 7455f;
        final float stable3Dist = 7455f;
        final float ervarDist =11650f;
        final float jumpOuterDist = 12155f;
        final float koenDist = 12650f;
        final float probeDist = 14000f;

        final float redTariff = .28f;
        final float otherTariff = .30f; //todo move to static class full of these

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
                                Submarkets.SUBMARKET_BLACK,
                                Submarkets.GENERIC_MILITARY //todo test this is luddic knights
                        )
                ),
                new ArrayList<>(
                        Arrays.asList(
                                Industries.POPULATION,
                                Industries.SPACEPORT,
                                Industries.MINING,
                                Industries.TECHMINING, //todo make a custom industry called tech-purge which is tech-mining but it doesnt make items, only destroys them
                                Industries.ORBITALSTATION,
                                Industries.GROUNDDEFENSES,
                                Industries.WAYSTATION,
                                Industries.PATROLHQ
                        )
                ),
                otherTariff,
                false,
                false);

        //adding debris around jabez to represent recent fighting
        SectorEntityToken debris1 = Utils.addDebris(system, 300, 500, true, DebrisFieldTerrainPlugin.DebrisFieldSource.BATTLE);
        debris1.setCircularOrbit(jabez, MathUtils.getRandomNumberInRange(0f,360f), 300 + jabez.getRadius(), 60);

        //stable point 1
        SectorEntityToken stableLoc1 = system.addCustomEntity("tradewind_stableloc_1", "Stable Location", "stable_location", Factions.NEUTRAL);
        stableLoc1.setCircularOrbit(tradewindStar, MathUtils.getRandomNumberInRange(0f,360f),stable1Dist, 520);

        SectorEntityToken tradeAF1 = system.addTerrain(Terrain.ASTEROID_FIELD,
                new AsteroidFieldParams(
                        200f, // min radius
                        300f, // max radius
                        8, // min asteroid count
                        16, // max asteroid count
                        4f, // min asteroid radius
                        16f, // max asteroid radius
                        "Asteroid Field")); // null for default name
        tradeAF1.setCircularOrbit(tradewindStar, MathUtils.getRandomNumberInRange(0f,360f), asteroids1Dist, 540);

        //Ruins Rich Bombarded World    Vlasta
        //Put this in a belt -- it was annihilated and the debris is still around
        system.addAsteroidBelt(tradewindStar, 700, asteroidBelt1Dist, 800, 250, 400, Terrain.ASTEROID_BELT, "Shattered Heart");
        //system.addRingBand(tradewindStar, "misc", "rings_asteroids0", 256f, 0, Color.gray, 256f, asteroidBelt1Dist, 400f);

        //adding a derelict station in the belt for player to exploit
        SectorEntityToken stationDerelict1 = DerelictThemeGenerator.addSalvageEntity(system, Entities.STATION_RESEARCH, Factions.DERELICT);
        stationDerelict1.setId("redlegion_tradewind_derelict1");
        stationDerelict1.setCircularOrbit(tradewindStar, 110, asteroidBelt1Dist, 421f);
        //Misc.setDefenderOverride(stationDerelict1, new DefenderDataOverride("factionID", 1f, 100, 250)); //no need to set more, 100-250 FP
        CargoAPI extraStationSalvage1 = Global.getFactory().createCargo(true);
        extraStationSalvage1.addCommodity(Commodities.BETA_CORE, 2);
        BaseSalvageSpecial.setExtraSalvage(extraStationSalvage1, stationDerelict1.getMemoryWithoutUpdate(), -1);

        PlanetAPI vlasta = system.addPlanet("vlasta",
                tradewindStar,
                "Vlasta",
                "barren-bombarded",
                180,
                225f,
                vlastaDist,
                421f);

        vlasta.setCustomDescriptionId("redlegion_tradewind_vlasta"); //reference descriptions.csv
        vlasta.getMarket().addCondition(Conditions.NO_ATMOSPHERE);
        vlasta.getMarket().addCondition(Conditions.RUINS_WIDESPREAD);
        vlasta.getMarket().addCondition(Conditions.IRRADIATED);
        vlasta.getMarket().addCondition(Conditions.VERY_COLD);
        vlasta.getMarket().addCondition(Conditions.LOW_GRAVITY);
        vlasta.getMarket().addCondition(Conditions.ORE_RICH);
        vlasta.getMarket().addCondition(Conditions.RARE_ORE_RICH);
        vlasta.getMarket().addCondition(Conditions.TECTONIC_ACTIVITY);

        //Inner Jump Point
        JumpPointAPI jumpPoint1 = Global.getFactory().createJumpPoint(
                "inner_jump",
                "Inner System Jump");
        jumpPoint1.setCircularOrbit(system.getEntityById("tradewind"), 90, jumpInnerDist, 222);

        jumpPoint1.setStandardWormholeToHyperspaceVisual();
        system.addEntity(jumpPoint1);

        SectorEntityToken tradeAF2 = system.addTerrain(Terrain.ASTEROID_FIELD,
                new AsteroidFieldParams(
                        250f, // min radius
                        400f, // max radius
                        30, // min asteroid count
                        48, // max asteroid count
                        8f, // min asteroid radius
                        16f, // max asteroid radius
                        "Asteroid Field")); // null for default name
        tradeAF2.setCircularOrbit(tradewindStar, MathUtils.getRandomNumberInRange(0f,360f), asteroids2Dist, 390);

        //Red Legion Gas Giant          Tanda
        PlanetAPI tanda = system.addPlanet("tanda",
                tradewindStar,
                "Tanda",
                "gas_giant",
                360f*(float)Math.random(),
                400,
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
                redTariff,
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

        system.addRingBand(tanda, "misc", "rings_dust0", 256f, 2, Color.yellow, 256f, 700, 360f, null, null);

        //adding debris around tanda to represent recent fighting
        SectorEntityToken debris2 = Utils.addDebris(system, 300, 500, true, DebrisFieldTerrainPlugin.DebrisFieldSource.BATTLE);
        debris2.setCircularOrbit(tanda, MathUtils.getRandomNumberInRange(0f,360f), 300 + tanda.getRadius(), 60);

        //Tri-Tachyon Outpost, desert   Siraj
        PlanetAPI siraj = system.addPlanet("siraj",
                tanda,
                "Siraj",
                "desert1",
                360f*(float)Math.random(),
                90f,
                1285,
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
                otherTariff,
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
                                Submarkets.GENERIC_MILITARY, //todo test
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
                otherTariff,
                false,
                false);

        SectorEntityToken derelict2 = DerelictThemeGenerator.addSalvageEntity(system, Entities.WRECK, Factions.DERELICT);
        derelict2.setId("redlegion_tradewind_derelict2");
        derelict2.setCircularOrbit(ervar, MathUtils.getRandomNumberInRange(0f,360f), 500 + ervar.getRadius(), 75);
        //derelict2.addDropRandom(); //hmm

        // outer jump
        JumpPointAPI jumpPoint2 = Global.getFactory().createJumpPoint(
                "outer_jump",
                "Outer System Jump");
        jumpPoint2.setCircularOrbit(system.getEntityById("tradewind"), 10, jumpOuterDist, 999);

        jumpPoint2.setStandardWormholeToHyperspaceVisual();
        system.addEntity(jumpPoint2);

        //adding debris around ervar to represent recent fighting
        SectorEntityToken debris3 = Utils.addDebris(system, 300, 500, true, DebrisFieldTerrainPlugin.DebrisFieldSource.BATTLE);
        debris3.setCircularOrbit(ervar, MathUtils.getRandomNumberInRange(0f,360f), 300 + ervar.getRadius(), 60);

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
        koen.getMarket().addCondition(Conditions.THIN_ATMOSPHERE);
        koen.getMarket().addCondition(Conditions.DARK);
        koen.getMarket().addCondition(Conditions.ORE_RICH);
        koen.getMarket().addCondition(Conditions.RARE_ORE_RICH);

        // Asharu abandoned station
        SectorEntityToken neutralStation = system.addCustomEntity("tradewind_abandoned_station",
                "Abandoned Terraforming Platform", "station_side06", "neutral");

        neutralStation.setCircularOrbitPointingDown(system.getEntityById("koen"), 45, 275, 30);
        neutralStation.setCustomDescriptionId("tradewind_platform");
        neutralStation.setInteractionImage("illustrations", "abandoned_station2");
        Misc.setAbandonedStationMarket("tradewind_abandoned_station_market", neutralStation);
        neutralStation.getMarket().getSubmarket(Submarkets.SUBMARKET_STORAGE).getCargo().addMothballedShip(FleetMemberType.SHIP, "kite_Starting", null); //todo use a RedLegion ship later
        neutralStation.getMarket().getSubmarket(Submarkets.SUBMARKET_STORAGE).getCargo().addCommodity(Commodities.SUPPLIES, 25);
        neutralStation.getMarket().getSubmarket(Submarkets.SUBMARKET_STORAGE).getCargo().addCommodity(Commodities.FUEL, 16);

        SectorEntityToken koenAF1 = system.addTerrain(Terrain.ASTEROID_FIELD,
                new AsteroidFieldParams(
                        55f, // min radius
                        175f, // max radius
                        32, // min asteroid count
                        48, // max asteroid count
                        8f, // min asteroid radius
                        20f, // max asteroid radius
                        "Asteroid Field")); // null for default name
        koenAF1.setCircularOrbit(koen, 45, 275, 30);

        //here is the probe that has drifted away
        SectorEntityToken probe = DerelictThemeGenerator.addSalvageEntity(system, Entities.DERELICT_SURVEY_PROBE, Factions.DERELICT);
        probe.setId("redlegion_tradewind_probe");
        probe.setCircularOrbit(tradewindStar, MathUtils.getRandomNumberInRange(0f,360f), probeDist, 2000f);
        SalvageEntityGenDataSpec.DropData probeData = new SalvageEntityGenDataSpec.DropData();
        probeData.addRandomWeapons(1, 5);
        probeData.addCommodity(Commodities.VOLATILES, 40);
        probe.addDropRandom(probeData);

        system.autogenerateHyperspaceJumpPoints(true, false);

        HyperspaceTerrainPlugin plugin = (HyperspaceTerrainPlugin) Misc.getHyperspaceTerrain().getPlugin();
        NebulaEditor editor = new NebulaEditor(plugin);
        float minRadius = plugin.getTileSize() * 2f;

        float radius = system.getMaxRadiusInHyperspace();
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius, 0, 360f);
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius, 0, 360f, 0.25f);
    }
}
