package org.ei.drishti.repository;

import android.content.SharedPreferences;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import io.ona.ziggy.repository.AllSettings;
import io.ona.ziggy.repository.SettingsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class AllSettingsTest {
    @Mock
    private SettingsRepository settingsRepository;
    @Mock
    private SharedPreferences preferences;

    private AllSettings allSettings;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        allSettings = new AllSettings(preferences, settingsRepository);
    }

    @Test
    public void shouldFetchReportIdentifierFromPreferences() throws Exception {
        when(preferences.getString("reporterIdentifier", "")).thenReturn("1234");

        String actual = allSettings.fetchRegisteredReporter();

        verify(preferences).getString("reporterIdentifier", "");
        assertEquals("1234", actual);
    }

    @Test
    public void shouldFetchReportPassword() throws Exception {
        when(settingsRepository.querySetting("reporterPassword", "")).thenReturn("actual password");

        String actual = allSettings.fetchReporterPassword();

        verify(settingsRepository).querySetting("reporterPassword", "");
        assertEquals("actual password", actual);
    }

    @Test
    public void shouldTrimReporterIdentifier() throws Exception {
        when(preferences.getString("reporterIdentifier", "")).thenReturn("  1234 ");

        String actual = allSettings.fetchRegisteredReporter();

        verify(preferences).getString("reporterIdentifier", "");
        assertEquals("1234", actual);
    }

    @Test
    public void shouldSavePreviousFetchIndex() throws Exception {
        allSettings.savePreviousFetchIndex("1234");

        verify(settingsRepository).updateSetting("previousFetchIndex", "1234");
    }

    @Test
    public void shouldFetchPreviousFetchIndex() throws Exception {
        when(settingsRepository.querySetting("previousFetchIndex", "0")).thenReturn("1234");

        String actual = allSettings.fetchPreviousFetchIndex();

        verify(settingsRepository).querySetting("previousFetchIndex", "0");
        assertEquals("1234", actual);
    }

    @Test
    public void shouldSavePreviousFormSyncIndex() throws Exception {
        allSettings.savePreviousFormSyncIndex("1234");

        verify(settingsRepository).updateSetting("previousFormSyncIndex", "1234");
    }

    @Test
    public void shouldFetchPreviousFormSyncIndex() throws Exception {
        when(settingsRepository.querySetting("previousFormSyncIndex", "0")).thenReturn("1234");

        String actual = allSettings.fetchPreviousFormSyncIndex();

        verify(settingsRepository).querySetting("previousFormSyncIndex", "0");
        assertEquals("1234", actual);
    }

    @Test
    public void shouldFetchIsSyncInProgress() throws Exception {
        when(preferences.getBoolean("isSyncInProgress", false)).thenReturn(true);

        assertTrue(allSettings.fetchIsSyncInProgress());
    }

    @Test
    public void shouldSaveIsSyncInProgress() throws Exception {
        SharedPreferences.Editor editor = mock(SharedPreferences.Editor.class);
        when(preferences.edit()).thenReturn(editor);
        when(editor.putBoolean("isSyncInProgress", true)).thenReturn(editor);

        allSettings.saveIsSyncInProgress(true);

        verify(editor).putBoolean("isSyncInProgress", true);
        verify(editor).commit();
    }
}
