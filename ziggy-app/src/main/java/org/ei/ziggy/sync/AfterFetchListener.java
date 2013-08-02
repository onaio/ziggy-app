package org.ei.ziggy.sync;

import org.ei.ziggy.domain.FetchStatus;

public interface AfterFetchListener {
    void afterFetch(FetchStatus fetchStatus);
}
