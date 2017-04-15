package com.neyagodamalina.nibnim;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.neyagodamalina.nibnim.data.TranslationUnit;
import com.neyagodamalina.nibnim.json.JSONResponse;
import com.neyagodamalina.nibnim.request.Translate;

import java.io.IOException;
import java.util.LinkedList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TranslateActivity extends AppCompatActivity {

    final Context context = this;
    private Retrofit retrofit;

    private String CURRENT_DIRECTION_LANG = "ru-en"; // Направление перевода


    private static LinkedList<TranslationUnit> translationHistoryList = new LinkedList<TranslationUnit>();
    private static LinkedList<TranslationUnit> translationFavoriteList = new LinkedList<TranslationUnit>();

    EditText mTextBeforeTranslation;
    TextView mTextAfterTranslation;

    public static LinkedList<TranslationUnit> getTranslationHistoryList()
    {
        return translationHistoryList;
    }

    public static LinkedList<TranslationUnit> getTranslationFavoriteList() {
        return translationFavoriteList;
    }

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

        //region Ссылка удовлетворение требований Лицензии
        TextView yandexLink = (TextView) findViewById(R.id.yandex_link);
        yandexLink.setText(Html.fromHtml(getResources().getString(R.string.html_yandex_link)));
        yandexLink.setMovementMethod(LinkMovementMethod.getInstance());
        //endregion

        //region Навигация
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //endregion

        //region Включение логирования и подготовка запроса перевода
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net/") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .client(httpClient.build())
                .build();
        //endregion


        mTextBeforeTranslation = (EditText) findViewById(R.id.etBeforeTranslate);
        mTextAfterTranslation = (TextView) findViewById(R.id.tvAfterTranslate);

        mTextBeforeTranslation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            /**
             * Если введен пробел иди enter, начнем переводить
             */
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((count != 0) && ((s.charAt(start) == ' ') || (s.charAt(start) == '\n'))) {
                    new TranslateTask().execute(mTextBeforeTranslation.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        //region Кнопка перевода
        Button btTranslate = (Button) findViewById(R.id.btTranslate);
        btTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TranslateTask().execute(mTextBeforeTranslation.getText().toString());
                // Если уже много срок, спрячем клавиатуру, чтобы было видно перевод
                if (mTextBeforeTranslation.getLineCount() > 5) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        //endregion

        //region Переключатель
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.ru_en:
                        CURRENT_DIRECTION_LANG = "ru-en";
                        break;
                    case R.id.en_ru:
                        CURRENT_DIRECTION_LANG = "en-ru";
                        break;
                    default:
                        break;
                }
            }
        });
        //endregion

    }

    /**
     * Поток для передачи запроса в Яндекс.Переводчик
     */

    private class TranslateTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... text) {
            // Прежде чем отправлять запрос обратимся к истории. Если там уже есть такой запрос,
            // покажем перевод из истории и запрос отправлять не будем.
            LinkedList<TranslationUnit> translationList = TranslateActivity.getTranslationHistoryList();
            String textBeforeTranslation = text[0].toLowerCase().trim();
            for (TranslationUnit unit: translationList) {
                if (unit.getTextBeforeTranslate().toLowerCase().trim().equals(textBeforeTranslation))
                    return unit.getTextAfterTranslate();
            }

            JSONResponse jsonResponse = null;
            try {

                Translate service = retrofit.create(Translate.class);

                jsonResponse = service.getData("trnsl.1.1.20170403T184448Z.18208d2f735ce38d.70c4c3b2ae948888de8c8394cc7c8b38f22712dc", text[0], CURRENT_DIRECTION_LANG).execute().body();

                Log.i(Constants.LOG_TAG, "Response: " + jsonResponse.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Добавим результат в историю переводов
            translationList.addFirst(new TranslationUnit(text[0], jsonResponse.getText().get(0), CURRENT_DIRECTION_LANG));
            return jsonResponse.getText().get(0);
        }


        @Override
        protected void onPostExecute(String textAfterTranslate) {
            super.onPostExecute(textAfterTranslate);
            mTextAfterTranslation.setText(textAfterTranslate);

        }
    }
}
