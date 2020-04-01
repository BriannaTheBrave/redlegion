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

public class Argon {
	public void generate(SectorAPI sector) {
		
		StarSystemAPI system = sector.createStarSystem("Argon");
		system.getLocation().set(-19000,19000);

		system.setBackgroundTextureFilename("graphics/redlegion/backgrounds/backgroundargon.jpg");
		
		// create the star and generate the hyperspace anchor for this system
		PlanetAPI argonStar = system.initStar("Argon", // unique id for this star 
										"star_red_giant",  // id in planets.json
										    1100f, 		  // radius (in pixels at default zoom)
										    450); // corona radius, from star edge
		system.setLightColor(new Color(239, 155, 128)); // light color in entire system, affects all entities

        //setup all distances here
        final float asteroids1Dist = 2750f;
        final float argonDeltaDist = 1850f;
        final float argonPrimeDist = 3500f;
        final float stable1Dist = 4200f;
        final float argonPhiDist = 4800f;
        final float asteroidBelt1Dist = 5700f;
        final float argonBetaDist = 6500f;
        final float stable2Dist = 7100f;
        final float strolluckDist = 8450f;
        final float asteroids2Dist = 8900f;
        final float argonGateDist = 9500f;
        final float argonMajorisDist = 11400f;
        final float argonGammaDist = 14100f;
        final float stable3Dist = 15200f;
        final float asteroidBelt2Dist = 15700f;
        final float bloodDist = 17200f;
        final float hye_steelDist = 24700f;

        final float jumpInnerDist = 3050f;
        final float jumpOuterDist = 8400f;
        final float jumpFringeDist = 16700f;

        final float majorisRad = 670f;

        final float systemTariff = .28f;

		SectorEntityToken argonAF1 = system.addTerrain(Terrain.ASTEROID_FIELD,
				new AsteroidFieldParams(
					200f, // min radius
					300f, // max radius
					8, // min asteroid count
					16, // max asteroid count
					4f, // min asteroid radius
					16f, // max asteroid radius
					"Asteroids Field")); // null for default name
		argonAF1.setCircularOrbit(argonStar, 130, asteroids1Dist, 240);

        // Argon Delta: Shitty Volcanic world
        PlanetAPI argonDelta = system.addPlanet("argon_delta",
                argonStar,
                "Argon Delta",
                "lava",
                360f*(float)Math.random(),
                150f,
                argonDeltaDist,
                128f);

        argonDelta.setCustomDescriptionId("redlegion_argon_argondelta"); //reference descriptions.csv
        PlanetConditionGenerator.generateConditionsForPlanet(argonDelta, StarAge.AVERAGE);

        // Argon Prime: Terran homeworld
        PlanetAPI argonPrime = system.addPlanet("argon_prime",
                argonStar,
                "Argon Prime",
                "terran",
                30,
                220f,
                argonPrimeDist,
                320f);

        argonPrime.setCustomDescriptionId("redlegion_argon_argonprime"); //reference descriptions.csv

        MarketAPI argonPrime_market = Utils.addMarketplace("redlegion", argonPrime, null,
                "Argon Prime",
                6,
                new ArrayList<>(
                        Arrays.asList(
                                Conditions.POPULATION_6,
                                Conditions.ORE_RICH,
                                Conditions.RARE_ORE_ABUNDANT,
                                Conditions.FARMLAND_BOUNTIFUL,
                                Conditions.HABITABLE,
                                Conditions.ORGANIZED_CRIME,
                                Conditions.TERRAN,
                                Conditions.REGIONAL_CAPITAL,
                                Conditions.STEALTH_MINEFIELDS,
                                Conditions.AI_CORE_ADMIN
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
                                Industries.STARFORTRESS,
                                Industries.HEAVYBATTERIES,
                                Industries.HIGHCOMMAND,
                                Industries.WAYSTATION
                        )
                ),
                systemTariff,
                true,
                true);

        argonPrime_market.addIndustry(Industries.ORBITALWORKS, new ArrayList<String>(Arrays.asList(Items.PRISTINE_NANOFORGE))); //couldn't find another way to add w/ forge!
        argonPrime_market.getIndustry(Industries.HIGHCOMMAND).setAICoreId(Commodities.ALPHA_CORE);
        argonPrime_market.getIndustry(Industries.STARFORTRESS).setAICoreId(Commodities.ALPHA_CORE);
        argonPrime_market.getIndustry(Industries.MEGAPORT).setAICoreId(Commodities.ALPHA_CORE);
        argonPrime_market.getIndustry(Industries.ORBITALWORKS).setAICoreId(Commodities.ALPHA_CORE);
        argonPrime_market.getIndustry(Industries.POPULATION).setAICoreId(Commodities.BETA_CORE);
        argonPrime_market.getIndustry(Industries.WAYSTATION).setAICoreId(Commodities.GAMMA_CORE);
//        FactionAPI redfaction = argonPrime_market.getFaction();
//        PersonAPI theAdmin = redfaction.createRandomPerson();
//        theAdmin.setAICoreId(Commodities.ALPHA_CORE);
//        argonPrime_market.setAdmin(theAdmin);

                //add first stable loc
        SectorEntityToken stableLoc1 = system.addCustomEntity("argon_stableloc_1", "Stable Location", "stable_location", Factions.NEUTRAL);
        stableLoc1.setCircularOrbit(argonStar, MathUtils.getRandomNumberInRange(0f,360f),stable1Dist, 520);

		// White Citadel: Orbital station around Argon Prime, in addition to the normal Starfort
        SectorEntityToken whiteCitadel = system.addCustomEntity("argon_white_citadel", "White Citadel", "station_hightech3", "redlegion");
        whiteCitadel.setCircularOrbitPointingDown(argonPrime,360*(float)Math.random(), 660,18f);
        whiteCitadel.setCustomDescriptionId("redlegion_argon_whitecitadel");
        MarketAPI whiteCitadel_market = Utils.addMarketplace("redlegion", whiteCitadel, null,
                "White Citadel",
                5,
                new ArrayList<>(
                        Arrays.asList(
                                Conditions.POPULATION_5,
                                Conditions.NO_ATMOSPHERE,
                                Conditions.OUTPOST,
                                Conditions.AI_CORE_ADMIN
                        )
                ),
                new ArrayList<>(
                        Arrays.asList(
                                Submarkets.GENERIC_MILITARY,
                                Submarkets.SUBMARKET_STORAGE
                        )
                ),
                new ArrayList<>(
                        Arrays.asList(
                                Industries.POPULATION,
                                Industries.MEGAPORT,
                                Industries.STARFORTRESS_HIGH,
                                Industries.HEAVYBATTERIES,
                                Industries.HIGHCOMMAND,
                                Industries.ORBITALWORKS,
                                Industries.WAYSTATION,
                                Industries.REFINING
                        )
                ),
                systemTariff,
                false,
                false);

        whiteCitadel_market.getIndustry(Industries.HIGHCOMMAND).setAICoreId(Commodities.ALPHA_CORE);
        whiteCitadel_market.getIndustry(Industries.STARFORTRESS_HIGH).setAICoreId(Commodities.ALPHA_CORE);
        whiteCitadel_market.getIndustry(Industries.ORBITALWORKS).setAICoreId(Commodities.ALPHA_CORE);
        whiteCitadel_market.getIndustry(Industries.REFINING).setAICoreId(Commodities.BETA_CORE);
        whiteCitadel_market.getIndustry(Industries.HEAVYBATTERIES).setAICoreId(Commodities.GAMMA_CORE);
        whiteCitadel_market.getIndustry(Industries.MEGAPORT).setAICoreId(Commodities.BETA_CORE);
        whiteCitadel_market.getIndustry(Industries.WAYSTATION).setAICoreId(Commodities.GAMMA_CORE);

        //Inner Jump Point
        // Inner System Jump
        JumpPointAPI jumpPoint = Global.getFactory().createJumpPoint(
                "inner_jump",
                "Inner System Jump");
        jumpPoint.setCircularOrbit(system.getEntityById("Argon"), 10, jumpInnerDist, 335);
        //jumpPoint.setRelatedPlanet(argonMajoris);

        jumpPoint.setStandardWormholeToHyperspaceVisual();
        system.addEntity(jumpPoint);

		// Argon Phi: Shitty Radioactive world
        PlanetAPI argonPhi = system.addPlanet("argon_phi",
                argonStar,
                "Argon Phi",
                "irradiated",
                360f*(float)Math.random(),
                220f,
                argonPhiDist,
                128f);

        argonPhi.setCustomDescriptionId("redlegion_argon_argonphi"); //reference descriptions.csv
        PlanetConditionGenerator.generateConditionsForPlanet(argonPhi, StarAge.AVERAGE);

        //asteroid belt1 ring
        system.addAsteroidBelt(argonStar, 1000, asteroidBelt1Dist, 800, 250, 400, Terrain.ASTEROID_BELT, "Inner Band");
        system.addRingBand(argonStar, "misc", "rings_asteroids0", 256f, 3, Color.gray, 256f, asteroidBelt1Dist-200, 250f);
        system.addRingBand(argonStar, "misc", "rings_asteroids0", 256f, 0, Color.gray, 256f, asteroidBelt1Dist, 350f);
        system.addRingBand(argonStar, "misc", "rings_asteroids0", 256f, 2, Color.gray, 256f, asteroidBelt1Dist+200, 400f);

		// Argon Beta: Organic Rich world
        PlanetAPI argonBeta = system.addPlanet("argon_beta",
                argonStar,
                "Argon Beta",
                "terran-eccentric",
                360f*(float)Math.random(),
                255f,
                argonBetaDist,
                675f);

        argonBeta.setCustomDescriptionId("redlegion_argon_argonbeta"); //reference descriptions.csv

        MarketAPI argonBeta_market = Utils.addMarketplace("redlegion", argonBeta, null,
                "Argon Beta",
                4,
                new ArrayList<>(
                        Arrays.asList(
                                Conditions.POPULATION_4,
                                Conditions.ORE_SPARSE,
                                Conditions.RARE_ORE_SPARSE,
                                Conditions.FARMLAND_RICH,
                                Conditions.LOW_GRAVITY,
                                Conditions.ORGANIZED_CRIME,
                                Conditions.TERRAN,
                                Conditions.ORGANICS_ABUNDANT,
                                Conditions.RURAL_POLITY
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
                                Industries.FARMING,
                                Industries.STARFORTRESS_MID,
                                Industries.HEAVYBATTERIES,
                                Industries.PATROLHQ,
                                Industries.WAYSTATION,
                                Industries.LIGHTINDUSTRY
                        )
                ),
                systemTariff,
                true,
                true);

        argonBeta_market.getIndustry(Industries.PATROLHQ).setAICoreId(Commodities.BETA_CORE);
        argonBeta_market.getIndustry(Industries.STARFORTRESS_MID).setAICoreId(Commodities.ALPHA_CORE);
        argonBeta_market.getIndustry(Industries.SPACEPORT).setAICoreId(Commodities.GAMMA_CORE);
        argonBeta_market.getIndustry(Industries.LIGHTINDUSTRY).setAICoreId(Commodities.BETA_CORE);

        //add second stable loc
        SectorEntityToken stableLoc2 = system.addCustomEntity("argon_stableloc_2", "Stable Location", "stable_location", Factions.NEUTRAL);
        stableLoc2.setCircularOrbit(argonStar, MathUtils.getRandomNumberInRange(0f,360f),stable2Dist, 720);

		// Strolluck: Mineral Rich world
        PlanetAPI strolluck = system.addPlanet("argon_strolluck",
                argonStar,
                "Strolluck",
                "desert",
                360f*(float)Math.random(),
                315f,
                strolluckDist,
                975f);

        strolluck.setCustomDescriptionId("redlegion_argon_strolluck"); //reference descriptions.csv

        MarketAPI strolluck_market = Utils.addMarketplace("redlegion", strolluck, null,
                "Strolluck",
                4,
                new ArrayList<>(
                        Arrays.asList(
                                Conditions.POPULATION_4,
                                Conditions.ORE_ULTRARICH,
                                Conditions.RARE_ORE_ULTRARICH,
                                Conditions.VOLATILES_PLENTIFUL,
                                Conditions.HIGH_GRAVITY,
                                Conditions.ORGANIZED_CRIME,
                                Conditions.AI_CORE_ADMIN,
                                Conditions.HOT,
                                Conditions.DESERT
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
                                Industries.STARFORTRESS_MID,
                                Industries.HEAVYBATTERIES,
                                Industries.PATROLHQ,
                                Industries.WAYSTATION,
                                Industries.REFINING
                        )
                ),
                systemTariff,
                true,
                true);

        strolluck_market.getIndustry(Industries.PATROLHQ).setAICoreId(Commodities.BETA_CORE);
        strolluck_market.getIndustry(Industries.STARFORTRESS_MID).setAICoreId(Commodities.ALPHA_CORE);
        strolluck_market.getIndustry(Industries.SPACEPORT).setAICoreId(Commodities.GAMMA_CORE);
        strolluck_market.getIndustry(Industries.REFINING).setAICoreId(Commodities.BETA_CORE);

        //asteroids 2
        SectorEntityToken argonAF2 = system.addTerrain(Terrain.ASTEROID_FIELD,
                new AsteroidFieldParams(
                        400f, // min radius
                        500f, // max radius
                        16, // min asteroid count
                        24, // max asteroid count
                        4f, // min asteroid radius
                        18f, // max asteroid radius
                        "Asteroid Field")); // null for default name
        argonAF2.setCircularOrbit(argonStar, 130, asteroids2Dist, 940);

        // inactive ring
        SectorEntityToken gate = system.addCustomEntity("argon_gate", // unique id
                "Argon Gate", // name - if null, defaultName from custom_entities.json will be used
                "inactive_gate", // type of object, defined in custom_entities.json
                null); // faction
        gate.setCircularOrbit(system.getEntityById("Argon"), 0, argonGateDist, 850);



		// Argon Majoris: Gas Giant with 3 moons, Argon Minoris I, II, III
        PlanetAPI argonMajoris = system.addPlanet("argon_majoris",
                argonStar,
                "Argon Majoris",
                "gas_giant",
                360f*(float)Math.random(),
                majorisRad,
                argonMajorisDist,
                1021f);

        argonMajoris.setCustomDescriptionId("redlegion_argon_argonmajoris"); //reference descriptions.csv
        PlanetConditionGenerator.generateConditionsForPlanet(argonMajoris, StarAge.AVERAGE);

        PlanetAPI fieldro = system.addPlanet("fieldro",
                argonMajoris,
                "Fieldro",
                "barren2",
                360f*(float)Math.random(),
                90f,
                majorisRad+200,
                215);

        fieldro.setCustomDescriptionId("redlegion_argon_fieldro"); //reference descriptions.csv
        PlanetConditionGenerator.generateConditionsForPlanet(fieldro, StarAge.AVERAGE);

        PlanetAPI yawsplu = system.addPlanet("yawsplu",
                argonMajoris,
                "Yawsplu",
                "desert1",
                360f*(float)Math.random(),
                65f,
                majorisRad+550,
                315);

        yawsplu.setCustomDescriptionId("redlegion_argon_yawsplu"); //reference descriptions.csv
        PlanetConditionGenerator.generateConditionsForPlanet(yawsplu, StarAge.AVERAGE);

        PlanetAPI douven = system.addPlanet("douven",
                argonMajoris,
                "Douven",
                "barren-desert",
                360f*(float)Math.random(),
                110f,
                majorisRad+750,
                615);

        douven.setCustomDescriptionId("redlegion_argon_douven"); //reference descriptions.csv
        PlanetConditionGenerator.generateConditionsForPlanet(douven, StarAge.AVERAGE);

        // outer jump
        JumpPointAPI jumpPoint2 = Global.getFactory().createJumpPoint(
                "outer_jump",
                "Outer System Jump");
        jumpPoint2.setCircularOrbit(system.getEntityById("Argon"), 10, jumpOuterDist, 1335);

        jumpPoint2.setStandardWormholeToHyperspaceVisual();
        system.addEntity(jumpPoint2);

		// Argon Gamma: Terrible, useless Dead World
        PlanetAPI argonGamma = system.addPlanet("argon_gamma",
                argonStar,
                "Argon Gamma",
                "barren-bombarded",
                360f*(float)Math.random(),
                320f,
                argonGammaDist,
                1421f);

        argonGamma.setCustomDescriptionId("redlegion_argon_argonGamma"); //reference descriptions.csv
        PlanetConditionGenerator.generateConditionsForPlanet(argonGamma, StarAge.AVERAGE);

        //second asteroid ring, much wider belt
        system.addAsteroidBelt(argonStar, 2000, asteroidBelt2Dist, 1000, 1250, 1400, Terrain.ASTEROID_BELT, "Outer Band");
        system.addRingBand(argonStar, "misc", "rings_asteroids0", 512f, 3, Color.darkGray, 512f, asteroidBelt2Dist-300, 1250f);
        system.addRingBand(argonStar, "misc", "rings_asteroids0", 512f, 0, Color.gray, 512f, asteroidBelt2Dist, 1350f);
        system.addRingBand(argonStar, "misc", "rings_asteroids0", 512f, 2, Color.darkGray, 512f, asteroidBelt2Dist+300, 1400f);

        //add second stable loc
        SectorEntityToken stableLoc3 = system.addCustomEntity("argon_stableloc_3", "Stable Location", "stable_location", Factions.NEUTRAL);
        stableLoc3.setCircularOrbit(argonStar, MathUtils.getRandomNumberInRange(0f,360f),stable3Dist, 1720);

		// Blood Keep - the Blood Knight Citadel: Far orbital near the fringe point for garrison, place where strike forces report back and regroup
        SectorEntityToken bloodKeep = system.addCustomEntity("argon_blood_keep", "Blood Keep", "station_hightech2", "redlegion");
        bloodKeep.setCircularOrbitPointingDown(argonStar,0,bloodDist,4000f);
        bloodKeep.setCustomDescriptionId("redlegion_argon_bloodkeep");
        MarketAPI bloodKeep_market = Utils.addMarketplace("redlegion", bloodKeep, null,
                "Blood Keep",
                4,
                new ArrayList<>(
                        Arrays.asList(
                                Conditions.POPULATION_4,
                                Conditions.NO_ATMOSPHERE,
                                Conditions.OUTPOST,
                                Conditions.AI_CORE_ADMIN
                        )
                ),
                new ArrayList<>(
                        Arrays.asList(
                                Submarkets.GENERIC_MILITARY,
                                Submarkets.SUBMARKET_STORAGE,
                                Submarkets.SUBMARKET_BLACK
                        )
                ),
                new ArrayList<>(
                        Arrays.asList(
                                Industries.POPULATION,
                                Industries.SPACEPORT,
                                Industries.BATTLESTATION_HIGH,
                                Industries.HEAVYBATTERIES,
                                Industries.MILITARYBASE,
                                Industries.ORBITALWORKS,
                                Industries.WAYSTATION
                        )
                ),
                systemTariff,
                false,
                false);

        bloodKeep_market.getIndustry(Industries.MILITARYBASE).setAICoreId(Commodities.BETA_CORE);
        bloodKeep_market.getIndustry(Industries.BATTLESTATION_HIGH).setAICoreId(Commodities.ALPHA_CORE);
        bloodKeep_market.getIndustry(Industries.ORBITALWORKS).setAICoreId(Commodities.BETA_CORE);

        // fringe jump
        JumpPointAPI jumpPoint3 = Global.getFactory().createJumpPoint(
                "fringe_jump",
                "Fringe System Jump");
        jumpPoint3.setCircularOrbit(system.getEntityById("Argon"), 2, jumpFringeDist, 4000f);

        jumpPoint3.setStandardWormholeToHyperspaceVisual();
        system.addEntity(jumpPoint3);

		// Hye-Steel: Useful world way far out, ruins, decivilized
        PlanetAPI hye_steel = system.addPlanet("hye_steel",
                argonStar,
                "Hye-Steel",
                "frozen",
                360*(float)Math.random(),
                190f,
                hye_steelDist,
                3421f);

        hye_steel.setCustomDescriptionId("redlegion_argon_hye_steel"); //reference descriptions.csv
        hye_steel.getMarket().addCondition(Conditions.RUINS_WIDESPREAD);
        hye_steel.getMarket().addCondition(Conditions.VERY_COLD);
        hye_steel.getMarket().addCondition(Conditions.DECIVILIZED);
        hye_steel.getMarket().addCondition(Conditions.DARK);
        hye_steel.getMarket().addCondition(Conditions.ORE_ULTRARICH);
        hye_steel.getMarket().addCondition(Conditions.RARE_ORE_MODERATE);

        // SectorEntityToken relay1 = system.addCustomEntity(null,null,"comm_relay_makeshift","hegemony"); // faction
	    // relay1.setCircularOrbitPointingDown(ivree3, 320, 900, 25);

		system.autogenerateHyperspaceJumpPoints(true, false);
		
        HyperspaceTerrainPlugin plugin = (HyperspaceTerrainPlugin) Misc.getHyperspaceTerrain().getPlugin();
        NebulaEditor editor = new NebulaEditor(plugin);
        float minRadius = plugin.getTileSize() * 2f;

        float radius = system.getMaxRadiusInHyperspace();
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius, 0, 360f);
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius, 0, 360f, 0.25f);
    }
}