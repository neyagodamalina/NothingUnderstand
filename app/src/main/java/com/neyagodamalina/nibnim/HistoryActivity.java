package com.neyagodamalina.nibnim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.neyagodamalina.nibnim.ui.TranslateUnitAdapter;

import java.util.LinkedList;

public class HistoryActivity extends AppCompatActivity {

    private ListView historyList;
    private LinkedList<TranslationUnit> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        list = new LinkedList<TranslationUnit>();
        for (int i = 1; i < 10; i++) {
            list.add(new TranslationUnit("hello " + i, " привет " + i));
        }


        historyList = (ListView) findViewById(R.id.lvHistory);
        TranslateUnitAdapter adapter = new TranslateUnitAdapter(this, R.layout.list_item, list);
        historyList.setAdapter(adapter);

    }

    public void onTest(View view) {
        String buf = "";
        for (TranslationUnit u : list
                ) {
            buf += u.isFavorite() + " ";
        }
        Log.i(Constants.LOG_TAG, buf);
    }
}
