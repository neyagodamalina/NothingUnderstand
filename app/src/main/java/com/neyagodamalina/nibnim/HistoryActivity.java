package com.neyagodamalina.nibnim;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.neyagodamalina.nibnim.data.TranslationUnit;
import com.neyagodamalina.nibnim.ui.TranslationUnitAdapter;


import java.util.LinkedList;


/**
 * Активити со списком Истории и Навингацией
 */


public class HistoryActivity extends CommonActivity {

    private ListView historyList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        LinkedList<TranslationUnit> translationHistoryList = TranslateActivity.getTranslationHistoryList();
/*
        for (int i = 1; i < 10; i++) {
            translationHistoryListList.add(new TranslationUnit("hello " + i, " привет " + i, "ru-en"));
        }

        translationHistoryListList.get(0).setFavorite(true);
        translationHistoryListList.get(2).setFavorite(true);
*/

        //region Навигация
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(getOnNavigationItemSelectedListener());
        navigation.getMenu().getItem(1).setChecked(true);
        //endregion

        historyList = (ListView) findViewById(R.id.lvHistory);
        TranslationUnitAdapter adapter = new TranslationUnitAdapter(this, R.layout.list_item, translationHistoryList);
        historyList.setAdapter(adapter);

    }

    public void onTest(View view) {
        String buf = "";
        LinkedList<TranslationUnit> translationList = TranslateActivity.getTranslationHistoryList();
        for (TranslationUnit u : translationList
                ) {
            buf += u.isFavorite() + " ";
        }
        Log.i(Constants.LOG_TAG, buf);
    }
}
