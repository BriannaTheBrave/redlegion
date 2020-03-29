package scripts.campaign.customstart;

import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.SpecialItemData;
import com.fs.starfarer.api.campaign.rules.MemKeys;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.characters.CharacterCreationData;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.rulecmd.FireBest;
import com.fs.starfarer.api.impl.campaign.rulecmd.newgame.NGCAddStartingShipsByFleetType;
import static com.fs.starfarer.api.impl.campaign.rulecmd.newgame.NGCAddStartingShipsByFleetType.addStartingDModScript;
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
        PlayerFactionStore.setPlayerFactionIdNGC("redlegion");

        CharacterCreationData data = (CharacterCreationData) memoryMap.get(MemKeys.LOCAL).get("$characterData");

        NGCAddStartingShipsByFleetType.generateFleetFromVariantIds(dialog, data, null, ships);

        addStartingDModScript(memoryMap.get(MemKeys.LOCAL));
        data.getStartingCargo().addItems(CargoAPI.CargoItemType.RESOURCES, Commodities.CREW, 600);
        data.getStartingCargo().addItems(CargoAPI.CargoItemType.RESOURCES, Commodities.SUPPLIES, 350);
        data.getStartingCargo().addItems(CargoAPI.CargoItemType.RESOURCES, Commodities.FUEL, 500);
        data.getStartingCargo().addItems(CargoAPI.CargoItemType.RESOURCES, Commodities.ALPHA_CORE, 1);
        data.getStartingCargo().addItems(CargoAPI.CargoItemType.RESOURCES, Commodities.MARINES, 150);
        data.getStartingCargo().addItems(CargoAPI.CargoItemType.RESOURCES, Commodities.HEAVY_MACHINERY, 50);
        data.getStartingCargo().addItems(CargoAPI.CargoItemType.RESOURCES, Commodities.HAND_WEAPONS, 25);

        FireBest.fire(null, dialog, memoryMap, "ExerelinNGCStep4");
    }
}
