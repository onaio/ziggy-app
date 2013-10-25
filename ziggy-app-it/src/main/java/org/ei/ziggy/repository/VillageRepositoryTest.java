package io.ona.ziggy.repository;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import io.ona.ziggy.domain.Village;
import io.ona.ziggy.util.Session;

import java.util.Date;
import java.util.Map;

import static java.util.Arrays.asList;
import static io.ona.ziggy.util.EasyMap.mapOf;

public class VillageRepositoryTest extends AndroidTestCase {
    private VillageRepository repository;

    @Override
    protected void setUp() throws Exception {
        repository = new VillageRepository();
        Session session = new Session().setPassword("password").setRepositoryName("ziggy.db" + new Date().getTime());
        new Repository(new RenamingDelegatingContext(getContext(), "test_"), session, repository);
    }

    public void testShouldInsertVillage() throws Exception {
        Map<String, String> details = mapOf("some-key", "some-value");
        Village village = new Village("Entity X", "Village X", "Code X", details);

        repository.add(village);

        assertEquals(asList(village), repository.allVillages());
    }

    public void testShouldFindVillageByEntityId() throws Exception {
        Map<String, String> details = mapOf("some-key", "some-value");
        Village firstVillage = new Village("Entity X", "Village X", "Code X", details);
        Village secondVillage = new Village("Entity Y", "Village Y", "Code Y", details);

        repository.add(firstVillage);
        repository.add(secondVillage);

        assertEquals(firstVillage, repository.findByEntityID("Entity X"));
        assertEquals(secondVillage, repository.findByEntityID("Entity Y"));
    }

    public void testShouldFindAllVillages() throws Exception {
        Map<String, String> details = mapOf("some-key", "some-value");
        Village firstVillage = new Village("Entity X", "Village X", "Code X", details);
        Village secondVillage = new Village("Entity Y", "Village Y", "Code Y", details);

        repository.add(firstVillage);
        repository.add(secondVillage);

        assertEquals(asList(firstVillage, secondVillage), repository.allVillages());
    }
}
