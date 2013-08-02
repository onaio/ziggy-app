package org.ei.ziggy.sync;

import android.content.Context;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.ei.ziggy.ZiggyContext;
import org.ei.ziggy.domain.FetchStatus;
import org.ei.ziggy.service.FormSubmissionSyncService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;

import static org.ei.ziggy.domain.FetchStatus.fetched;
import static org.ei.ziggy.domain.FetchStatus.nothingFetched;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class UpdateTaskTest {
    @Mock
    private ProgressIndicator progressIndicator;
    @Mock
    private Context androidContext;
    @Mock
    private ZiggyContext context;
    @Mock
    private FormSubmissionSyncService formSubmissionSyncService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void shouldShowProgressBarsWhileFetchingAlerts() throws Exception {
        progressIndicator = mock(ProgressIndicator.class);
        ZiggyContext.setInstance(context);
        when(formSubmissionSyncService.sync()).thenReturn(fetched);

        UpdateTask UpdateTask = new UpdateTask(null, formSubmissionSyncService, progressIndicator);
        UpdateTask.updateFromServer(new AfterFetchListener() {
            public void afterFetch(FetchStatus status) {
                assertEquals(fetched, status);
            }
        });

        InOrder inOrder = inOrder(formSubmissionSyncService, progressIndicator);
        inOrder.verify(progressIndicator).setVisible();
        inOrder.verify(formSubmissionSyncService).sync();
        inOrder.verify(progressIndicator).setInvisible();
    }

    @Test
    public void shouldNotUpdateDisplayIfNothingWasFetched() throws Exception {
        ZiggyContext.setInstance(context);

        when(formSubmissionSyncService.sync()).thenReturn(nothingFetched);

        UpdateTask UpdateTask = new UpdateTask(null, formSubmissionSyncService, progressIndicator);
        UpdateTask.updateFromServer(new AfterFetchListener() {
            public void afterFetch(FetchStatus status) {
                assertEquals(nothingFetched, status);
            }
        });
    }
}
