package com.neyagodamalina.nibnim.test;

import android.util.EventLogTags;
import android.view.View;
import android.widget.ListView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created by ser on 23.04.2017.
 */

public class Matchers {

    public static Matcher<View> withListSize(final int size) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("В списке должна быть " + size + " строка");
            }

            @Override
            public boolean matchesSafely(final View view) {
                return ((ListView) view).getCount() == size;
            }

        };
    }

}
