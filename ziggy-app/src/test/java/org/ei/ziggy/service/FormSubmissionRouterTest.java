package org.ei.ziggy.service;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.ei.ziggy.domain.form.FormSubmission;
import org.ei.ziggy.event.Listener;
import org.ei.ziggy.repository.FormDataRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;

import static org.ei.ziggy.event.Event.FORM_SUBMITTED;
import static org.ei.ziggy.util.FormSubmissionBuilder.create;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class FormSubmissionRouterTest {
    @Mock
    private FormDataRepository formDataRepository;
    @Mock
    private VillageRegistrationHandler villageRegistrationHandler;
    @Mock
    private Listener<String> formSubmittedListener;

    private FormSubmissionRouter submissionRouter;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        submissionRouter = new FormSubmissionRouter(formDataRepository, villageRegistrationHandler);
    }

    @Test
    public void shouldNotifyFormSubmittedListenersWhenFormIsHandled() throws Exception {
        FormSubmission formSubmission = create().withFormName("village_profile").withInstanceId("instance id 1").withVersion("122").build();
        when(formDataRepository.fetchFromSubmission("instance id 1")).thenReturn(formSubmission);
        FORM_SUBMITTED.addListener(formSubmittedListener);

        submissionRouter.route("instance id 1");

        InOrder inOrder = inOrder(formDataRepository, villageRegistrationHandler, formSubmittedListener);
        inOrder.verify(formDataRepository).fetchFromSubmission("instance id 1");
        inOrder.verify(villageRegistrationHandler).handle(formSubmission);
        inOrder.verify(formSubmittedListener).onEvent("instance id 1");
    }

    @Test
    public void shouldNotifyFormSubmittedListenersWhenThereIsNoHandlerForForm() throws Exception {
        FormSubmission formSubmission = create().withFormName("form-without-handler").withInstanceId("instance id 1").withVersion("122").build();
        when(formDataRepository.fetchFromSubmission("instance id 1")).thenReturn(formSubmission);
        FORM_SUBMITTED.addListener(formSubmittedListener);

        submissionRouter.route("instance id 1");

        InOrder inOrder = inOrder(formDataRepository, formSubmittedListener);
        inOrder.verify(formDataRepository).fetchFromSubmission("instance id 1");
        inOrder.verify(formSubmittedListener).onEvent("instance id 1");
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
