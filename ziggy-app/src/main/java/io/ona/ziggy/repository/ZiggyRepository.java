package io.ona.ziggy.repository;

import info.guardianproject.database.sqlcipher.SQLiteDatabase;

public abstract class ZiggyRepository {
    protected Repository masterRepository;

    public void updateMasterRepository(Repository repository) {
        this.masterRepository = repository;
    }

    abstract protected void onCreate(SQLiteDatabase database);
}
