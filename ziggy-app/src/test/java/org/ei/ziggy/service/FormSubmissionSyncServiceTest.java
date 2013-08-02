package org.ei.ziggy.service;

import com.google.gson.Gson;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.ei.ziggy.domain.FetchStatus;
import org.ei.ziggy.domain.Response;
import org.ei.ziggy.domain.form.FormSubmission;
import org.ei.ziggy.dto.FormSubmissionDTO;
import org.ei.ziggy.repository.AllSettings;
import org.ei.ziggy.repository.FormDataRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.ei.ziggy.domain.FetchStatus.fetched;
import static org.ei.ziggy.domain.FetchStatus.nothingFetched;
import static org.ei.ziggy.domain.ResponseStatus.failure;
import static org.ei.ziggy.domain.ResponseStatus.success;
import static org.ei.ziggy.domain.SyncStatus.PENDING;
import static org.ei.ziggy.domain.SyncStatus.SYNCED;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class FormSubmissionSyncServiceTest {
    @Mock
    private FormDataRepository repository;
    @Mock
    private HTTPAgent httpAgent;
    @Mock
    private AllSettings allSettings;
    @Mock
    private FormSubmissionService formSubmissionService;

    private FormSubmissionSyncService service;
    private List<FormSubmissionDTO> expectedFormSubmissionsDto;
    private List<FormSubmission> submissions;
    private String formInstanceJSON;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        service = new FormSubmissionSyncService(formSubmissionService, httpAgent, repository, allSettings);

        formInstanceJSON = "{form:{bind_type: 'village'}}";
        submissions = asList(new FormSubmission("id 1", "entity id 1", "form name", formInstanceJSON, "123", PENDING));
        expectedFormSubmissionsDto = asList(new FormSubmissionDTO(
                "reporter id 1", "id 1", "entity id 1", "form name", formInstanceJSON, "123"));
        when(allSettings.fetchRegisteredReporter()).thenReturn("reporter id 1");
        when(repository.getPendingFormSubmissions()).thenReturn(submissions);
    }

    @Test
    public void shouldPushPendingFormSubmissionsToServerAndMarkThemAsSynced() throws Exception {
        when(httpAgent.postJSONRequest("http://formhub.org" + "/form-submissions", new Gson().toJson(expectedFormSubmissionsDto)))
                .thenReturn(new Response<String>(success, null));

        service.pushToServer();

        inOrder(allSettings, httpAgent, repository);
        verify(allSettings).fetchRegisteredReporter();
        verify(httpAgent).postJSONRequest("http://formhub.org" + "/form-submissions", new Gson().toJson(expectedFormSubmissionsDto));
        verify(repository).markFormSubmissionsAsSynced(submissions);
    }

    @Test
    public void shouldNotMarkPendingSubmissionsAsSyncedIfPostFails() throws Exception {
        when(httpAgent.postJSONRequest("http://formhub.org" + "/form-submissions", new Gson().toJson(expectedFormSubmissionsDto)))
                .thenReturn(new Response<String>(failure, null));

        service.pushToServer();

        verify(repository).getPendingFormSubmissions();
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void shouldNotPushIfThereAreNoPendingSubmissions() throws Exception {
        when(repository.getPendingFormSubmissions()).thenReturn(Collections.<FormSubmission>emptyList());

        service.pushToServer();

        verify(repository).getPendingFormSubmissions();
        verifyNoMoreInteractions(repository);
        verifyZeroInteractions(allSettings);
        verifyZeroInteractions(httpAgent);
    }

    @Test
    public void shouldPullFormSubmissionsFromServerAndDelegateToProcessing() throws Exception {
        List<FormSubmission> expectedFormSubmissions = asList(new FormSubmission("id 1", "entity id 1", "form name", formInstanceJSON, "123", SYNCED));
        when(allSettings.fetchPreviousFormSyncIndex()).thenReturn("122");
        when(httpAgent.fetch("http://formhub.org/form-submissions?reporter-id=reporter id 1&timestamp=122")).thenReturn(new Response<String>(success, new Gson().toJson(this.expectedFormSubmissionsDto)));

        FetchStatus fetchStatus = service.pullFromServer();

        assertEquals(fetched, fetchStatus);
        verify(httpAgent).fetch("http://formhub.org/form-submissions?reporter-id=reporter id 1&timestamp=122");
        verify(formSubmissionService).processSubmissions(expectedFormSubmissions);
    }

    @Test
    public void shouldReturnNothingFetchedStatusWhenNoFormSubmissionsAreGotFromServer() throws Exception {
        when(allSettings.fetchPreviousFormSyncIndex()).thenReturn("122");
        when(httpAgent.fetch("http://formhub.org/form-submissions?reporter-id=reporter id 1&timestamp=122")).thenReturn(new Response<String>(success, new Gson().toJson(Collections.emptyList())));

        FetchStatus fetchStatus = service.pullFromServer();

        assertEquals(nothingFetched, fetchStatus);
        verify(httpAgent).fetch("http://formhub.org/form-submissions?reporter-id=reporter id 1&timestamp=122");
        verifyZeroInteractions(formSubmissionService);
    }

    @Test
    public void shouldNotDelegateToProcessingIfPullFails() throws Exception {
        when(allSettings.fetchPreviousFormSyncIndex()).thenReturn("122");
        when(httpAgent.fetch("http://formhub.org/form-submissions?reporter-id=reporter id 1&timestamp=122")).thenReturn(new Response<String>(failure, null));

        FetchStatus fetchStatus = service.pullFromServer();

        assertEquals(FetchStatus.fetchedFailed, fetchStatus);
        verifyZeroInteractions(formSubmissionService);
    }
}
