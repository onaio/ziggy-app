package org.ei.ziggy.service;

import org.ei.ziggy.domain.form.FormSubmission;
import org.ei.ziggy.repository.FormDataRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.ei.ziggy.util.FormSubmissionBuilder.create;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FormSubmissionRouterTest {
    @Mock
    private FormDataRepository formDataRepository;
    @Mock
    private VillageRegistrationHandler villageRegistrationHandler;

    private FormSubmissionRouter submissionRouter;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        submissionRouter = new FormSubmissionRouter(formDataRepository, villageRegistrationHandler);
    }

    @Test
    public void shouldDelegateVillageRegistrationFormSubmissionHandlingToVillageRegistrationHandler() throws Exception {
        FormSubmission formSubmission = create().withFormName("village_profile").withInstanceId("instance id 1").withVersion("122").build();
        when(formDataRepository.fetchFromSubmission("instance id 1")).thenReturn(formSubmission);

        submissionRouter.route("instance id 1");

        verify(formDataRepository).fetchFromSubmission("instance id 1");
        verify(villageRegistrationHandler).handle(formSubmission);
    }
}
