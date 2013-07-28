package org.ei.ziggy.view.controller;

import com.google.gson.Gson;
import org.ei.ziggy.domain.Village;
import org.ei.ziggy.repository.AllVillages;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.ei.ziggy.util.EasyMap.mapOf;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class VillageListControllerTest {
    @Mock
    private AllVillages allVillages;

    private VillageListController controller;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        controller = new VillageListController(allVillages);
    }

    @Test
    public void shouldReturnAllVillagesAsJSON() throws Exception {
        List<Village> villages = asList(
                new Village("entity 1", "village 1", "1", mapOf("population", "1000")),
                new Village("entity 2", "village 2", "2", mapOf("population", "2000")));
        when(allVillages.getVillages()).thenReturn(villages);

        String villagesJSON = controller.get();

        assertEquals(new Gson().toJson(villages), villagesJSON);
    }
}
