package org.ei.ziggy;

import android.content.Context;
import org.ei.ziggy.repository.FormDataRepository;
import org.ei.ziggy.repository.Repository;
import org.ei.ziggy.repository.VillageRepository;
import org.ei.ziggy.service.ZiggyFileLoader;
import org.ei.ziggy.util.Session;

public class ZiggyContext {
    private static ZiggyContext ziggyContext;

    private Context applicationContext;

    private ZiggyFileLoader ziggyFileLoader;

    private Repository repository;
    private Session session;
    private FormDataRepository formDataRepository;
    private VillageRepository villageRepository;

    public static ZiggyContext getInstance() {
        if (ziggyContext == null) {
            ziggyContext = new ZiggyContext();
        }
        return ziggyContext;
    }

    private Repository initRepository() {
        if (repository == null) {
            repository = new Repository(this.applicationContext, session(), villageRepository(), formDataRepository());
        }
        return repository;
    }

    private Session session() {
        if (session == null) {
            session = new Session();
        }
        return session;
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

    public void updateApplicationContext(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ZiggyFileLoader ziggyFileLoader() {
        if (ziggyFileLoader == null) {
            ziggyFileLoader = new ZiggyFileLoader("www/ziggy", "www/form", applicationContext.getAssets());
        }
        return ziggyFileLoader;
    }

    public Object formSubmissionRouter() {
        throw new RuntimeException("Not implemented.");
    }
}
