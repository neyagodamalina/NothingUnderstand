package com.neyagodamalina.nibnim;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.widget.ToggleButton;

import com.neyagodamalina.nibnim.data.TranslationUnit;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Тесты основного TranslateActivity
 */
public class TranslateActivityTest {


    @Rule
    public ActivityTestRule<TranslateActivity> mActivityRule = new ActivityTestRule<>(
            TranslateActivity.class);



    @Before
    public void setUp() throws Exception {

    }

    // Проверим работу перевода
    @Test
    public void testTranslate() throws Exception {
        onView(withId(R.id.etBeforeTranslate)).perform(replaceText("яблоко"));
        onView(withId(R.id.btTranslate)).perform(click());
        onView(withId(R.id.tvAfterTranslate)).check(matches(withText("Apple")));
    }

    // Проверим, что если такой перевод уже есть в истории,
    // то повторного запроса к сервису Яндекс.Переводчик не будет.
    // Результатом перевода будет экземпляр из истории, а не новый экземпляр TranslationUnit.
    @Test
    public void testTranslateFromHistory() throws Exception {
        onView(withId(R.id.etBeforeTranslate)).perform(replaceText("яблоко"));
        onView(withId(R.id.btTranslate)).perform(click());
        ToggleButton tbFavorite = (ToggleButton) mActivityRule.getActivity().findViewById(R.id.tbFavorite);
        TranslationUnit translationUnit1 = (TranslationUnit) tbFavorite.getTag();   // В кнопке добавить в ибранное в Tag лежит экземпляр перевода

        onView(withId(R.id.etBeforeTranslate)).perform(replaceText("яблоко"));
        onView(withId(R.id.btTranslate)).perform(click());
        TranslationUnit translationUnit2 = (TranslationUnit) tbFavorite.getTag();

        assertEquals(translationUnit1, translationUnit2);
    }

    // Проверим, что при повторном переводе одного и того же слова,
    // в историю второй перевод не добавится.
    // Можность списока истории должна быть равна 1.
    @Test
    public void testRepeatTranslateHistoryList() throws Exception {
        onView(withId(R.id.etBeforeTranslate)).perform(replaceText("яблоко"));
        onView(withId(R.id.btTranslate)).perform(click());

        onView(withId(R.id.etBeforeTranslate)).perform(replaceText("яблоко"));
        onView(withId(R.id.btTranslate)).perform(click());

        onView(withId(R.id.navigation)).perform(click()); // переходим в активити История
        onView (withId(R.id.lvHistory)).check(ViewAssertions.matches(com.neyagodamalina.nibnim.test.Matchers.withListSize(1)));
    }

    // Проверим, что если переводить пустой текст, то запроса
    // к сервису Яндекс.Переводчик не будет.
    // Результат перевода будет null.
    @Test
    public void testTranslateCleanText() throws Exception {
        onView(withId(R.id.etBeforeTranslate)).perform(replaceText(""));
        onView(withId(R.id.btTranslate)).perform(click());
        ToggleButton tbFavorite = (ToggleButton) mActivityRule.getActivity().findViewById(R.id.tbFavorite);
        TranslationUnit translationUnit = (TranslationUnit) tbFavorite.getTag();   // В кнопке добавить в ибранное в Tag лежит экземпляр перевода
        assertNull(translationUnit);
    }

    // Проверим, что если переводить текст, состоящий из пробелов, то запроса
    // к сервису Яндекс.Переводчик не будет.
    // Результат перевода будет null.
    @Test
    public void testTranslateSpaceText() throws Exception {
        onView(withId(R.id.etBeforeTranslate)).perform(replaceText(" \n "));
        onView(withId(R.id.btTranslate)).perform(click());
        ToggleButton tbFavorite = (ToggleButton) mActivityRule.getActivity().findViewById(R.id.tbFavorite);
        TranslationUnit translationUnit = (TranslationUnit) tbFavorite.getTag();   // В кнопке добавить в ибранное в Tag лежит экземпляр перевода
        assertNull(translationUnit);
    }
}