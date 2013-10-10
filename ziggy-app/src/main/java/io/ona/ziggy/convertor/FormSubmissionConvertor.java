package org.ei.ziggy.convertor;

import org.ei.ziggy.domain.form.FormSubmission;
import org.ei.ziggy.dto.FormSubmissionDTO;

import java.util.ArrayList;
import java.util.List;

import static org.ei.ziggy.domain.SyncStatus.SYNCED;

public class FormSubmissionConvertor {
    public static List<FormSubmission> toDomain(List<FormSubmissionDTO> formSubmissionsDto) {
        List<FormSubmission> submissions = new ArrayList<FormSubmission>();
        for (FormSubmissionDTO formSubmission : formSubmissionsDto) {
            submissions.add(new FormSubmission(
                    formSubmission.instanceId(),
                    formSubmission.entityId(),
                    formSubmission.formName(),
                    formSubmission.instance(),
                    formSubmission.clientVersion(),
                    SYNCED,
                    formSubmission.formDataDefinitionVersion(),
                    formSubmission.serverVersion()
            ));
        }
        return submissions;
    }
}
