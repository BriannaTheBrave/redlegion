package scripts.campaign.customstart;

import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.SpecialItemData;
import com.fs.starfarer.api.campaign.rules.MemKeys;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.characters.CharacterCreationData;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.rulecmd.AddRemoveCommodity;
import com.fs.starfarer.api.impl.campaign.rulecmd.FireBest;
import com.fs.starfarer.api.impl.campaign.rulecmd.newgame.NGCAddStartingShipsByFleetType;
import static com.fs.starfarer.api.impl.campaign.rulecmd.newgame.NGCAddStartingShipsByFleetType.addStartingDModScript;

import exerelin.campaign.ExerelinSetupData;
import exerelin.campaign.PlayerFactionStore;
import exerelin.campaign.customstart.CustomStart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CrimsonGuardStart extends CustomStart {
    protected List<String> ships = new ArrayList<>(Arrays.asList(new String[]{
            "hammerhead_Balanced",
            "apogee_Balanced",
            "falcon_p_Strike",
            "wolf_Starting",
            "swp_sunder_u_sta",
            "brawler_tritachyon_Standard",
            "colossus_Standard"
    }));

    @Override
    public void execute(InteractionDialogAPI dialog, Map<String, MemoryAPI> memoryMap) {
        CharacterCreationData data = (CharacterCreationData) memoryMap.get(MemKeys.LOCAL).get("$characterData");

        PlayerFactionStore.setPlayerFactionIdNGC("redlegion");
        data.setDifficulty("normal");
        ExerelinSetupData.getInstance().easyMode = false;

        NGCAddStartingShipsByFleetType.generateFleetFromVariantIds(dialog, data, null, ships);

        addStartingDModScript(memoryMap.get(MemKeys.LOCAL));
        data.getStartingCargo().getCredits().add(250000);
        AddRemoveCommodity.addCreditsGainText(250000, dialog.getTextPanel());
        data.getStartingCargo().addItems(CargoAPI.CargoItemType.RESOURCES, Commodities.CREW, 600);
        AddRemoveCommodity.addCommodityGainText(Commodities.CREW, 600, dialog.getTextPanel());
        data.getStartingCargo().addItems(CargoAPI.CargoItemType.RESOURCES, Commodities.SUPPLIES, 350);
        AddRemoveCommodity.addCommodityGainText(Commodities.SUPPLIES, 350, dialog.getTextPanel());
        data.getStartingCargo().addItems(CargoAPI.CargoItemType.RESOURCES, Commodities.FUEL, 500);
        AddRemoveCommodity.addCommodityGainText(Commodities.FUEL, 500, dialog.getTextPanel());
        data.getStartingCargo().addItems(CargoAPI.CargoItemType.RESOURCES, Commodities.ALPHA_CORE, 1);
        AddRemoveCommodity.addCommodityGainText(Commodities.ALPHA_CORE, 1, dialog.getTextPanel());
        data.getStartingCargo().addItems(CargoAPI.CargoItemType.RESOURCES, Commodities.MARINES, 150);
        AddRemoveCommodity.addCommodityGainText(Commodities.MARINES, 150, dialog.getTextPanel());
        data.getStartingCargo().addItems(CargoAPI.CargoItemType.RESOURCES, Commodities.HEAVY_MACHINERY, 50);
        AddRemoveCommodity.addCommodityGainText(Commodities.HEAVY_MACHINERY, 50, dialog.getTextPanel());
        data.getStartingCargo().addItems(CargoAPI.CargoItemType.RESOURCES, Commodities.HAND_WEAPONS, 25);
        AddRemoveCommodity.addCommodityGainText(Commodities.HAND_WEAPONS, 25, dialog.getTextPanel());

        MutableCharacterStatsAPI stats = data.getPerson().getStats();
        stats.addPoints(3);

        FireBest.fire(null, dialog, memoryMap, "ExerelinNGCStep4");
    }
}
