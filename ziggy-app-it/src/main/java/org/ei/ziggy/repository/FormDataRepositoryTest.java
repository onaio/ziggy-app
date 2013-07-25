package org.ei.ziggy.repository;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.ei.ziggy.domain.Village;
import org.ei.ziggy.domain.form.FormSubmission;
import org.ei.ziggy.util.FormSubmissionBuilder;
import org.ei.ziggy.util.Session;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.ei.ziggy.domain.SyncStatus.PENDING;
import static org.ei.ziggy.domain.SyncStatus.SYNCED;
import static org.ei.ziggy.util.EasyMap.create;

public class FormDataRepositoryTest extends AndroidTestCase {
    private FormDataRepository repository;
    private VillageRepository villageRepository;

    @Override
    protected void setUp() throws Exception {
        repository = new FormDataRepository();
        villageRepository = new VillageRepository();
        Session session = new Session().setPassword("password").setRepositoryName("ziggy.db" + new Date().getTime());
        new Repository(new RenamingDelegatingContext(getContext(), "test_"), session, villageRepository, repository);
    }

    public void testShouldRunQueryAndGetUniqueResult() throws Exception {
        Map<String, String> details = create("Hello", "There").put("Also", "This").put("someKey", "someValue").map();
        Village village = new Village("entity id 1", "Village name", "village code", details);
        villageRepository.add(village);
        String sql = MessageFormat.format("select * from village where village.id = ''{0}''", village.entityId());

        String result = repository.queryUniqueResult(sql);

        Map<String, String> fieldValues = new Gson().fromJson(result, new TypeToken<Map<String, String>>() {
        }.getType());
        assertEquals(village.entityId(), fieldValues.get("id"));
        assertEquals(village.name(), fieldValues.get("name"));
        assertEquals("someValue", fieldValues.get("someKey"));
    }

    public void testReturnsEmptyResultWhenQueryResultsAreEmpty() throws Exception {
        String sql = MessageFormat.format("select * from village where village.id = ''{0}''", "");

        String result = repository.queryUniqueResult(sql);

        Map<String, String> fieldValues = new Gson().fromJson(result, new TypeToken<Map<String, String>>() {
        }.getType());
        assertEquals(0, fieldValues.size());
    }

    public void testShouldRunQueryAndGetListAsResult() throws Exception {
        Map<String, String> details = create("Hello", "There").put("Also", "This").put("someKey", "someValue").map();
        Village firstVillage = new Village("entity id 1", "Village 1", "village code 1", details);
        Village secondVillage = new Village("entity id 2", "Village 1", "village code 2", details);
        villageRepository.add(firstVillage);
        villageRepository.add(secondVillage);
        String sql = MessageFormat.format("select * from village where village.name = ''{0}''", "Village 1");

        String results = repository.queryList(sql);

        List<Map<String, String>> fieldValues = new Gson().fromJson(results, new TypeToken<List<Map<String, String>>>() {
        }.getType());
        assertEquals(firstVillage.entityId(), fieldValues.get(0).get("id"));
        assertEquals(secondVillage.entityId(), fieldValues.get(1).get("id"));
    }

    public void testShouldSaveFormSubmission() throws Exception {
        Map<String, String> params = create("instanceId", "id 1").put("entityId", "entity id 1").put("formName", "form name").map();
        String paramsJSON = new Gson().toJson(params);

        String instanceId = repository.saveFormSubmission(paramsJSON, "instance");

        FormSubmission actualFormSubmission = repository.fetchFromSubmission("id 1");
        assertEquals(new FormSubmission("id 1", "entity id 1", "form name", "instance", "some version", PENDING), actualFormSubmission);
        assertNotNull(actualFormSubmission);
        assertEquals("id 1", instanceId);
    }

    public void testShouldCheckFormSubmissionExistence() throws Exception {
        FormSubmission firstFormSubmission = FormSubmissionBuilder.create().withInstanceId("instance id 1").withVersion("122").build();
        repository.saveFormSubmission(firstFormSubmission);

        assertTrue(repository.submissionExists("instance id 1"));
        assertFalse(repository.submissionExists("invalid instance Id"));
    }

    public void testShouldSaveNewVillage() throws Exception {
        Map<String, String> fields =
                create("id", "entity id 1")
                        .put("name", "bherya")
                        .put("code", "bherya 1")
                        .put("population", "1000")
                        .put("toilets", "100")
                        .map();
        String fieldsJSON = new Gson().toJson(fields);

        String entityId = repository.saveEntity("village", fieldsJSON);

        Village savedVillage = villageRepository.findByEntityID(entityId);
        Map<String, String> expectedDetails = create("population", "1000").put("toilets", "100").map();
        Village expectedVillage = new Village(entityId, "bherya", "bherya 1", expectedDetails);
        assertEquals(expectedVillage, savedVillage);
    }

    public void testShouldUpdateVillage() throws Exception {
        Map<String, String> fields =
                create("id", "entity id 1")
                        .put("name", "bherya")
                        .put("code", "bherya 1")
                        .put("population", "1000")
                        .put("toilets", "100")
                        .map();
        String fieldsJSON = new Gson().toJson(fields);
        Map<String, String> oldDetails = create("population", "100").put("toilets", "10").put("cattle", "1").map();
        Village oldVillage = new Village("entity id 1", "old village name", "old village code", oldDetails);
        villageRepository.add(oldVillage);

        String entityId = repository.saveEntity("village", fieldsJSON);

        assertEquals(entityId, "entity id 1");
        Village savedVillage = villageRepository.findByEntityID(entityId);
        Map<String, String> expectedDetails = create("population", "1000").put("toilets", "100").put("cattle", "1").map();
        Village expectedVillage = new Village("entity id 1", "bherya", "bherya 1", expectedDetails);
        assertEquals(expectedVillage, savedVillage);
    }

    public void testShouldFetchPendingFormSubmissions() throws Exception {
        FormSubmission firstSubmission = new FormSubmission("id 1", "entity id 1", "form name", "instance 1", "some version", PENDING);
        FormSubmission secondSubmission = new FormSubmission("id 2", "entity id 2", "form name", "instance 2", "some other version", PENDING);
        FormSubmission thirdSubmission = new FormSubmission("id 3", "entity id 3", "form name", "instance 3", "some other version", SYNCED);
        repository.saveFormSubmission(firstSubmission);
        repository.saveFormSubmission(secondSubmission);
        repository.saveFormSubmission(thirdSubmission);

        List<FormSubmission> pendingFormSubmissions = repository.getPendingFormSubmissions();

        assertEquals(asList(firstSubmission, secondSubmission), pendingFormSubmissions);
    }

    public void testShouldMarkPendingFormSubmissionsAsSynced() throws Exception {
        FormSubmission firstSubmission = new FormSubmission("id 1", "entity id 1", "form name", "instance 1", "some version", PENDING);
        FormSubmission secondSubmission = new FormSubmission("id 2", "entity id 2", "form name", "instance 2", "some other version", PENDING);
        FormSubmission thirdSubmission = new FormSubmission("id 3", "entity id 3", "form name", "instance 3", "some other version", PENDING);
        repository.saveFormSubmission(firstSubmission);
        repository.saveFormSubmission(secondSubmission);
        repository.saveFormSubmission(thirdSubmission);

        repository.markFormSubmissionsAsSynced(asList(firstSubmission, secondSubmission));

        assertEquals(firstSubmission.setSyncStatus(SYNCED), repository.fetchFromSubmission("id 1"));
        assertEquals(secondSubmission.setSyncStatus(SYNCED), repository.fetchFromSubmission("id 2"));
        assertEquals(thirdSubmission, repository.fetchFromSubmission("id 3"));
    }

    public void testShouldMarkPendingFormSubmissionsAsSyncedByInstanceId() throws Exception {
        FormSubmission firstSubmission = new FormSubmission("instance 1", "entity id 1", "form name", "instance 1", "some version", PENDING);
        FormSubmission secondSubmission = new FormSubmission("instance 2", "entity id 2", "form name", "instance 2", "some other version", PENDING);
        FormSubmission thirdSubmission = new FormSubmission("instance 3", "entity id 3", "form name", "instance 3", "some other version", PENDING);
        repository.saveFormSubmission(firstSubmission);
        repository.saveFormSubmission(secondSubmission);
        repository.saveFormSubmission(thirdSubmission);

        repository.markFormSubmissionAsSynced("instance 1");
        repository.markFormSubmissionAsSynced("instance 2");

        assertEquals(firstSubmission.setSyncStatus(SYNCED), repository.fetchFromSubmission("instance 1"));
        assertEquals(secondSubmission.setSyncStatus(SYNCED), repository.fetchFromSubmission("instance 2"));
        assertEquals(thirdSubmission, repository.fetchFromSubmission("instance 3"));
    }

    public void testShouldUpdateServerVersionByInstanceId() throws Exception {
        FormSubmission firstSubmission = new FormSubmission("instance 1", "entity id 1", "form name", "instance 1", "some version", SYNCED);
        FormSubmission secondSubmission = new FormSubmission("instance 2", "entity id 2", "form name", "instance 2", "some other version", SYNCED);
        FormSubmission thirdSubmission = new FormSubmission("instance 3", "entity id 3", "form name", "instance 3", "some other version", PENDING);
        repository.saveFormSubmission(firstSubmission);
        repository.saveFormSubmission(secondSubmission);
        repository.saveFormSubmission(thirdSubmission);

        repository.updateServerVersion("instance 1", "0");
        repository.updateServerVersion("instance 2", "1");

        assertEquals(firstSubmission.setServerVersion("0"), repository.fetchFromSubmission("instance 1"));
        assertEquals(secondSubmission.setServerVersion("1"), repository.fetchFromSubmission("instance 2"));
        assertEquals(thirdSubmission, repository.fetchFromSubmission("instance 3"));
    }
}
