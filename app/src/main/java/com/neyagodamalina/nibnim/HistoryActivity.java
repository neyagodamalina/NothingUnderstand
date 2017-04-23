package com.neyagodamalina.nibnim;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.widget.ListView;

import com.neyagodamalina.nibnim.data.TranslationUnit;
import com.neyagodamalina.nibnim.ui.TranslationUnitAdapter;


import java.util.LinkedList;


/**
 * Активити со списком Истории и Навигацией
 */

public class HistoryActivity extends CommonActivity {

    private ListView historyList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //region Навигация
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(getOnNavigationItemSelectedListener());
        navigation.getMenu().getItem(1).setChecked(true);
        //endregion

        //region Подготовка списка с историей
        LinkedList<TranslationUnit> translationHistoryList = TranslateActivity.getTranslationHistoryList();
        historyList = (ListView) findViewById(R.id.lvHistory);
        TranslationUnitAdapter adapter = new TranslationUnitAdapter(this, R.layout.list_item, translationHistoryList);
        historyList.setAdapter(adapter);
        //endregion

    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }

}
