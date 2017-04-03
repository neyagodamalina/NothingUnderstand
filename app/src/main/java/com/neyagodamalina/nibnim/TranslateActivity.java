package com.neyagodamalina.nibnim;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.neyagodamalina.nibnim.json.JSONResponse;
import com.neyagodamalina.nibnim.request.Translate;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TranslateActivity extends AppCompatActivity {

    final Context context = this;
    private Retrofit retrofit;
    public static final String LOG_TAG = "neyagodamalina";

    private TextView mTextMessage;

    /**
     * Обработаем нажатие на кнопок в навигационном меню
      */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_history:
                    Intent intentH = new Intent(context, HistoryActivity.class);
                    startActivity(intentH);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        // Ссылка требование Лицензии
        TextView yandexLink = (TextView) findViewById(R.id.yandex_link);
        yandexLink.setText(Html.fromHtml(getResources().getString(R.string.html_yandex_link)));
        yandexLink.setMovementMethod(LinkMovementMethod.getInstance());


        // Навигация
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Запрос перевода
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        retrofit = new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net/") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .client(httpClient.build())
                .build();



        // Кнопка временного перевода для тестирования запроса
        Button btTranslate = (Button) findViewById(R.id.button_translate);
        btTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TranslateTask().execute((Void) null);
            }
        });

    }

    /**
     * Поток для передачи запроса в Яндекс.Переводчик
     */
    private class TranslateTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Translate service = retrofit.create(Translate.class);

                //http://78.36.13.231/projectX/mobile/interface.php?action=try_login&name=reparepa@gmail.com&password=oPHrWfn8YZ
                JSONResponse jsonResponse = service.getData("trnsl.1.1.20170403T184448Z.18208d2f735ce38d.70c4c3b2ae948888de8c8394cc7c8b38f22712dc", "Nothing no understand", "en-ru").execute().body();

                Log.i("neyagodamalina", service.toString());
                Log.i("neyagodamalina", jsonResponse.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
    }
}
