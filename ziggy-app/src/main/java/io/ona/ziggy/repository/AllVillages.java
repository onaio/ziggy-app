package org.ei.ziggy.repository;

import org.ei.ziggy.domain.Village;

import java.util.List;

public class AllVillages {
    private VillageRepository repository;

    public AllVillages(VillageRepository repository) {
        this.repository = repository;
    }

    public List<Village> getVillages() {
        return repository.allVillages();
    }
}
