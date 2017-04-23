package com.neyagodamalina.nibnim;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import static java.security.AccessController.getContext;

/**
 * Created by developer on 19.04.2017.
 */

public class CommonActivity extends AppCompatActivity {

    private AppCompatActivity activity = this;

    /**
     * Обработаем нажатие на кнопок в навигационном меню
     */


    public BottomNavigationView.OnNavigationItemSelectedListener getOnNavigationItemSelectedListener() {
        return new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_translate:
                        // Если этот пункт меню нажат из TranslateActivity, никуда не переходим.
                        // А то закроется приложение.
                        if (activity instanceof TranslateActivity)
                            return true;
                        activity.finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        return true;
                    case R.id.navigation_history:
                        // Если этот пункт меню нажат из HistoryActivity, никуда не переходим.
                        if (activity instanceof HistoryActivity)
                            return true;
                        Intent intentH = new Intent(activity, HistoryActivity.class);
                        startActivity(intentH);
                        // Сделаем правильный сдвиг налево или направо.
                        if (activity instanceof FavoriteActivity) {
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        } else
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        return true;
                    case R.id.navigation_favorite:
                        // Если этот пункт меню нажат из FavoriteActivity, никуда не переходим.
                        if (activity instanceof FavoriteActivity)
                            return true;
                        Intent intentF = new Intent(activity, FavoriteActivity.class);
                        startActivity(intentF);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        return true;
                }
                return false;
            }
        };
    }

    @Override
    /**
     * Обработчик нажатия на верхнее меню.
     * Обработка верхнего меню такая же, как и у нижнего меню, поэтому обратимся к слушателю нижнего меню.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(true);
        return this.getOnNavigationItemSelectedListener().onNavigationItemSelected(item);
    }

    /**
     * Создание верхнего меню
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

}