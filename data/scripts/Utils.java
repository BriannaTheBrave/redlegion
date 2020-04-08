package data.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.DerelictShipEntityPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Entities;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.SalvageSpecialAssigner;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.ShipRecoverySpecial;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin.*;
import com.fs.starfarer.api.util.Misc;
import org.lazywizard.lazylib.MathUtils;

import java.util.ArrayList;

public class Utils {
    //Shorthand function for adding a market -- this is derived from tahlan mod
    public static MarketAPI addMarketplace(String factionID, SectorEntityToken primaryEntity, ArrayList<SectorEntityToken> connectedEntities, String name,
                                           int popSize, ArrayList<String> marketConditions, ArrayList<String> submarkets, ArrayList<String> industries, float tariff,
                                           boolean isFreePort, boolean floatyJunk) {
        EconomyAPI globalEconomy = Global.getSector().getEconomy();
        String planetID = primaryEntity.getId();
        String marketID = planetID + "_market"; //IMPORTANT this is a naming convention for markets. didn't want to have to pass in another variable :D

        MarketAPI newMarket = Global.getFactory().createMarket(marketID, name, popSize);
        newMarket.setFactionId(factionID);
        newMarket.setPrimaryEntity(primaryEntity);
        newMarket.getTariff().modifyFlat("generator", tariff);
        //newMarket.getTariff().setBaseValue(tariff);

        //Add submarkets, if any
        if (null != submarkets) {
            for (String market : submarkets) {
                newMarket.addSubmarket(market);
            }
        }

        //Add conditions
        for (String condition : marketConditions) {
            newMarket.addCondition(condition);
        }

        //Add industries
        for (String industry : industries) {
            newMarket.addIndustry(industry);
        }

        //Set free port
        newMarket.setFreePort(isFreePort);

        //Add connected entities, if any
        if (null != connectedEntities) {
            for (SectorEntityToken entity : connectedEntities) {
                newMarket.getConnectedEntities().add(entity);
            }
        }

        //set market in global, factions, and assign market, also submarkets
        globalEconomy.addMarket(newMarket, floatyJunk);
        primaryEntity.setMarket(newMarket);
        primaryEntity.setFaction(factionID);

        if (null != connectedEntities) {
            for (SectorEntityToken entity : connectedEntities) {
                entity.setMarket(newMarket);
                entity.setFaction(factionID);
            }
        }

        //Finally, return the newly-generated market
        return newMarket;
    }

    //for adding debris with some randomness
    public static SectorEntityToken addDebris(StarSystemAPI system, float durationDays, int baseSalvageXP, boolean isDiscoverable, DebrisFieldSource source) {
        if (durationDays < 0f) {
            durationDays = 30f;
        }

        if (baseSalvageXP < 0) {
            baseSalvageXP = 500;
        }

        if (system == null) {
            //todo log error and such
        }

        if (source == null) {
            source = DebrisFieldSource.MIXED;
        }

        float amount = StarSystemGenerator.random.nextFloat() * 200f + 100f;
        float density = StarSystemGenerator.random.nextFloat() * 0.5f + 0.5f;
        DebrisFieldParams params = new DebrisFieldParams(
                amount / density, // field radius - should not go above 1000 for performance reasons
                density / 3f, // density, visual - affects number of debris pieces
                durationDays, // duration in days to persist
                0f); // days the field will keep generating glowing pieces. 0 for the entirety of its existence
        params.source = source;
        params.baseSalvageXP = baseSalvageXP;
        SectorEntityToken debris = Misc.addDebrisField(system, params, StarSystemGenerator.random);
        debris.setDiscoverable(isDiscoverable);
        debris.setSensorProfile(amount);
        return debris; //dont forget to set what to orbit when you get it back!
    }

    //for adding derelict ships -- todo make a version with random variant by faction picking!
    public static void addDerelictShip (StarSystemAPI system, SectorEntityToken focus, String variantId, ShipRecoverySpecial.ShipCondition condition, float orbitRadius, boolean recoverable) {
        DerelictShipEntityPlugin.DerelictShipData params = new DerelictShipEntityPlugin.DerelictShipData(new ShipRecoverySpecial.PerShipData(variantId, condition), true);
        SectorEntityToken ship = BaseThemeGenerator.addSalvageEntity(system, Entities.WRECK, Factions.NEUTRAL, params);
        ship.setDiscoverable(true); //all wrecks should be discoverable, helps draw attention

        float orbitDays = orbitRadius / (10f + (float) Math.random() * 5f); //randomly sets orbit duration so longer radius makes it take a bit longer too with some randomness to avoid consistency
        ship.setCircularOrbit(focus, MathUtils.getRandomNumberInRange(0f,360f), orbitRadius, orbitDays);

        //forces recoverable logic. If you want random, use this to make a boolean when calling function for a 50-50 chance: (Math.random()<0.5)
        if (recoverable) {
            SalvageSpecialAssigner.ShipRecoverySpecialCreator recoveryCreator = new SalvageSpecialAssigner.ShipRecoverySpecialCreator(null, 0, 0, false, null, null);
            Misc.setSalvageSpecial(ship, recoveryCreator.createSpecial(ship, null));
        }
    }
}
