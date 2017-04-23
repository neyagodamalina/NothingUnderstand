package com.neyagodamalina.nibnim;

import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.widget.ListView;

import com.neyagodamalina.nibnim.data.TranslationUnit;
import com.neyagodamalina.nibnim.ui.TranslationUnitAdapter;

import java.util.LinkedList;

/**
 * Активити со списком Избранного и Навингацией
 */
public class FavoriteActivity extends CommonActivity {

    private ListView favoriteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);


        //region Навигация
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(getOnNavigationItemSelectedListener());
        navigation.getMenu().getItem(2).setChecked(true);
        //endregion
        //region Подготовка списка Избранного
        LinkedList<TranslationUnit> translationFavoriteList = TranslateActivity.getTranslationFavoriteList();
        favoriteList = (ListView) findViewById(R.id.lvFavorite);
        TranslationUnitAdapter adapter = new TranslationUnitAdapter(this, R.layout.list_item, translationFavoriteList);
        favoriteList.setAdapter(adapter);
        //endregion
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }
}
