package org.ei.ziggy.service;

import org.ei.ziggy.domain.form.FormSubmission;
import org.ei.ziggy.repository.FormDataRepository;

import java.util.HashMap;
import java.util.Map;

import static org.ei.ziggy.AllConstants.FormNames.VILLAGE_REGISTRATION;
import static org.ei.ziggy.util.Log.logWarn;

public class FormSubmissionRouter {
    private final Map<String, FormSubmissionHandler> handlerMap;
    private FormDataRepository formDataRepository;

    public FormSubmissionRouter(FormDataRepository formDataRepository,
                                VillageRegistrationHandler villageRegistrationHandler) {
        this.formDataRepository = formDataRepository;
        handlerMap = new HashMap<String, FormSubmissionHandler>();
        handlerMap.put(VILLAGE_REGISTRATION, villageRegistrationHandler);
    }

    public void route(String instanceId) {
        FormSubmission submission = formDataRepository.fetchFromSubmission(instanceId);
        FormSubmissionHandler handler = handlerMap.get(submission.formName());
        if (handler == null) {
            logWarn("Could not find a handler due to unknown form submission: " + submission);
        } else {
            handler.handle(submission);
        }
    }
}
