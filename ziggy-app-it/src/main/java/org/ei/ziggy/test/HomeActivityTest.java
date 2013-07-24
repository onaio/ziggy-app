package org.ei.ziggy.test;

import android.test.ActivityInstrumentationTestCase2;
import org.ei.ziggy.view.activity.HomeActivity;

public class HomeActivityTest extends ActivityInstrumentationTestCase2<HomeActivity> {
    public HomeActivityTest() {
        super(HomeActivity.class);
    }

    public void testActivity() {
        HomeActivity activity = getActivity();
        assertNotNull(activity);
    }
}

