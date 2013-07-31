package org.ei.ziggy.service;

import com.google.gson.Gson;
import org.ei.ziggy.domain.form.FormSubmission;
import org.ei.ziggy.repository.AllSettings;
import org.ei.ziggy.repository.FormDataRepository;

import java.util.List;

import static java.text.MessageFormat.format;
import static org.ei.ziggy.AllConstants.*;
import static org.ei.ziggy.util.EasyMap.create;
import static org.ei.ziggy.util.Log.logError;

public class FormSubmissionService {
    private ZiggyService ziggyService;
    private FormDataRepository formDataRepository;
    private AllSettings allSettings;

    public FormSubmissionService(ZiggyService ziggyService, FormDataRepository formDataRepository, AllSettings allSettings) {
        this.ziggyService = ziggyService;
        this.formDataRepository = formDataRepository;
        this.allSettings = allSettings;
    }

    public void processSubmissions(List<FormSubmission> formSubmissions) {
        for (FormSubmission submission : formSubmissions) {
            if (!formDataRepository.submissionExists(submission.instanceId())) {
                try {
                    ziggyService.saveForm(getParams(submission), submission.instance());
                    formDataRepository.markFormSubmissionAsSynced(submission.instanceId());
                } catch (Exception e) {
                    logError(format("Form submission processing failed, with submission: {0}. Exception: {1}", submission, e));
                }
            }
            formDataRepository.updateServerVersion(submission.instanceId(), submission.serverVersion());
            allSettings.savePreviousFormSyncIndex(submission.serverVersion());
        }
    }

    private String getParams(FormSubmission submission) {
        return new Gson().toJson(
                create(INSTANCE_ID_PARAM, submission.instanceId())
                        .put(ENTITY_ID_PARAM, submission.entityId())
                        .put(FORM_NAME_PARAM, submission.formName())
                        .put(VERSION_PARAM, submission.version())
                        .map());
    }
}
