package org.ei.ziggy;

import android.content.Context;
import org.ei.ziggy.repository.*;
import org.ei.ziggy.service.FormSubmissionRouter;
import org.ei.ziggy.service.VillageRegistrationHandler;
import org.ei.ziggy.service.ZiggyFileLoader;
import org.ei.ziggy.util.Session;

public class ZiggyContext {
    private static ZiggyContext ziggyContext;

    private Context applicationContext;

    private ZiggyFileLoader ziggyFileLoader;
    private Session session;

    private FormSubmissionRouter formSubmissionRouter;
    private VillageRegistrationHandler villageRegistrationHandler;

    private Repository repository;
    private SettingsRepository settingRepository;
    private VillageRepository villageRepository;
    private FormDataRepository formDataRepository;

    private AllVillages allVillages;

    public static ZiggyContext getInstance() {
        if (ziggyContext == null) {
            ziggyContext = new ZiggyContext();
        }
        return ziggyContext;
    }

    private Repository initRepository() {
        if (repository == null) {
            repository = new Repository(this.applicationContext, session().setPassword("Password"),
                    settingRepository(),
                    villageRepository(),
                    formDataRepository());
        }
        return repository;
    }

    private Session session() {
        if (session == null) {
            session = new Session();
        }
        return session;
    }

    private SettingsRepository settingRepository() {
        if (settingRepository == null) {
            settingRepository = new SettingsRepository();
        }
        return settingRepository;
    }

    private VillageRepository villageRepository() {
        if (villageRepository == null) {
            villageRepository = new VillageRepository();
        }
        return villageRepository;
    }

    public FormDataRepository formDataRepository() {
        if (formDataRepository == null) {
            formDataRepository = new FormDataRepository();
        }
        return formDataRepository;
    }

    public ZiggyContext updateApplicationContext(Context applicationContext) {
        this.applicationContext = applicationContext;
        return this;
    }

    public ZiggyFileLoader ziggyFileLoader() {
        initRepository();
        if (ziggyFileLoader == null) {
            ziggyFileLoader = new ZiggyFileLoader("www/ziggy", "www/form", applicationContext.getAssets());
        }
        return ziggyFileLoader;
    }

    public FormSubmissionRouter formSubmissionRouter() {
        initRepository();
        if (formSubmissionRouter == null) {
            formSubmissionRouter = new FormSubmissionRouter(formDataRepository(), villageRegistrationHandler());
        }
        return formSubmissionRouter;
    }

    private VillageRegistrationHandler villageRegistrationHandler() {
        if (villageRegistrationHandler == null) {
            villageRegistrationHandler = new VillageRegistrationHandler();
        }
        return villageRegistrationHandler;
    }

    public AllVillages allVillages() {
        initRepository();
        if (allVillages == null) {
            allVillages = new AllVillages(villageRepository());
        }
        return allVillages;
    }
}
