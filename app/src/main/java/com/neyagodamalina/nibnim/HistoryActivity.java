package com.neyagodamalina.nibnim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.neyagodamalina.nibnim.data.TranslationUnit;
import com.neyagodamalina.nibnim.ui.TranslateUnitFavoriteAdapter;
import com.neyagodamalina.nibnim.ui.TranslateUnitHistoryAdapter;

import java.util.LinkedList;

public class HistoryActivity extends AppCompatActivity {

    private ListView historyList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        LinkedList<TranslationUnit> translationList = TranslateActivity.getTranslationList();
        for (int i = 1; i < 10; i++) {
            translationList.add(new TranslationUnit("hello " + i, " привет " + i, "ru-en"));
        }

        translationList.get(0).setFavorite(true);
        translationList.get(2).setFavorite(true);

        historyList = (ListView) findViewById(R.id.lvHistory);
        TranslateUnitFavoriteAdapter adapter = new TranslateUnitFavoriteAdapter(this, R.layout.list_item, translationList);
        historyList.setAdapter(adapter);

    }

    public void onTest(View view) {
        String buf = "";
        LinkedList<TranslationUnit> translationList = TranslateActivity.getTranslationList();
        for (TranslationUnit u : translationList
                ) {
            buf += u.isFavorite() + " ";
        }
        Log.i(Constants.LOG_TAG, buf);
    }
}
