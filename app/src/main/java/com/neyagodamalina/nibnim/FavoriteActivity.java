package com.neyagodamalina.nibnim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.neyagodamalina.nibnim.data.TranslationUnit;
import com.neyagodamalina.nibnim.ui.TranslationUnitAdapter;

import java.util.LinkedList;

public class FavoriteActivity extends AppCompatActivity {

    private ListView favoriteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        LinkedList<TranslationUnit> translationFavoriteList = TranslateActivity.getTranslationFavoriteList();
/*
        for (int i = 1; i < 10; i++) {
            translationHistoryListList.add(new TranslationUnit("hello " + i, " привет " + i, "ru-en"));
        }

        translationHistoryListList.get(0).setFavorite(true);
        translationHistoryListList.get(2).setFavorite(true);
*/

        favoriteList = (ListView) findViewById(R.id.lvFavorite);
        TranslationUnitAdapter adapter = new TranslationUnitAdapter(this, R.layout.list_item, translationFavoriteList);
        favoriteList.setAdapter(adapter);
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
