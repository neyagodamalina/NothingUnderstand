package com.neyagodamalina.nibnim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.neyagodamalina.nibnim.ui.TranslateUnitAdapter;

import java.util.LinkedList;

public class HistoryActivity extends AppCompatActivity {

    private TextView mTextMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        LinkedList<TranslationUnit> list = new LinkedList<TranslationUnit>();
        for (int i = 1; i < 50; i++){
            list.add(new TranslationUnit("hello " + i, " привет " + i));
        }

        ListView historyList = (ListView) findViewById(R.id.lvHistory);
        TranslateUnitAdapter adapter = new TranslateUnitAdapter(this, R.layout.list_item, list);
        historyList.setAdapter(adapter);

    }

}
