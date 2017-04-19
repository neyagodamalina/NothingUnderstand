package com.neyagodamalina.nibnim;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

/**
 * Created by developer on 19.04.2017.
 */

public class CommonActivity extends AppCompatActivity {

    private AppCompatActivity context = this;

    /**
     * Обработаем нажатие на кнопок в навигационном меню
     */


    public BottomNavigationView.OnNavigationItemSelectedListener getOnNavigationItemSelectedListener() {
        return new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_translate:
                        //Intent intentT = new Intent(context, TranslateActivity.class);
                        //startActivity(intentT);
                        context.finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        return true;
                    case R.id.navigation_history:
                        Intent intentH = new Intent(context, HistoryActivity.class);
                        startActivity(intentH);
                        if (context instanceof FavoriteActivity){
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        }
                        else
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        return true;
                    case R.id.navigation_favorite:
                        Intent intentF = new Intent(context, FavoriteActivity.class);
                        startActivity(intentF);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        return true;
                }
                return false;
            }
        };
    }




}