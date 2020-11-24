/*
 * Copyright 2016 Jacek Marchwicki <jacek.marchwicki@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.jacekmarchwicki.changesdetector.example;

import android.content.Intent;
import androidx.test.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import javax.annotation.Nonnull;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@Ignore
public class ChangesDetectorActivityTest {

    @Rule
    public ActivityTestRule<ChangesDetectorActivity> activityRule
            = new ActivityTestRule<>(
            ChangesDetectorActivity.class,
            true,     // initialTouchMode
            false);   // launchActivity. False to customize the intent

    @Test
    public void startViaIntent_launchApp() {

        activityRule.launchActivity(newIntent());

        onView(withId(R.id.list_activity_recycler))
                .check(matches(isDisplayed()));
    }

    @Test
    public void clickOnButton_doesNotCrash() {
        activityRule.launchActivity(newIntent());

        onView(withId(R.id.list_activity_fab))
                .perform(click());

        onView(withId(R.id.list_activity_recycler))
                .check(matches(isDisplayed()));
    }

    @Nonnull
    private Intent newIntent() {
        return new Intent(InstrumentationRegistry.getTargetContext(), ChangesDetectorActivity.class);
    }

}