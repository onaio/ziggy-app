package org.ei.ziggy.view.controller;

import com.google.gson.Gson;
import org.ei.ziggy.repository.AllVillages;

public class VillageListController {
    private AllVillages allVillages;

    public VillageListController(AllVillages allVillages) {
        this.allVillages = allVillages;
    }

    public String get() {
        return new Gson().toJson(allVillages.getVillages());
    }
}
