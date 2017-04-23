package com.neyagodamalina.nibnim;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.neyagodamalina.nibnim.data.TranslationUnit;
import com.neyagodamalina.nibnim.json.JSONResponse;
import com.neyagodamalina.nibnim.request.Translate;

import java.util.LinkedList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Главная Активити с переводом и Навигацией
 */

public class TranslateActivity extends CommonActivity {

    private String CURRENT_DIRECTION_LANG = "ru-en"; // Направление перевода
    private EditText mTextBeforeTranslation;
    private TextView mTextAfterTranslation;
    private Retrofit retrofit;
    private ToggleButton tbFavorite;
    private BottomNavigationView navigation;

    private static LinkedList<TranslationUnit> translationHistoryList = new LinkedList<TranslationUnit>();
    private static LinkedList<TranslationUnit> translationFavoriteList = new LinkedList<TranslationUnit>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        // Переопределим переход между activities
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        //region Текст и ссылка Переведено сервисом "Яндекс.Переводчик" (удовлетворение требований Лицензии)
        TextView yandexLink = (TextView) findViewById(R.id.yandex_link);
        yandexLink.setText(Html.fromHtml(getResources().getString(R.string.html_yandex_link)));
        yandexLink.setMovementMethod(LinkMovementMethod.getInstance());
        //endregion

        //region Навигация
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(getOnNavigationItemSelectedListener());
        navigation.getMenu().getItem(0).setChecked(true);
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

        //region Перевод по символу "пробел" или "enter"
        mTextBeforeTranslation  = (EditText) findViewById(R.id.etBeforeTranslate);
        mTextAfterTranslation   = (TextView) findViewById(R.id.tvAfterTranslate);
        mTextBeforeTranslation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            /**
             * Если введен пробел иди enter, начнем переводить
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Если тект для перевода состоит из пробелов, переводить не будем
                if (s.toString().trim().length() == 0)
                    return;
                if ((count != 0) && ((s.charAt(start) == ' ') || (s.charAt(start) == '\n'))) {
                    new TranslateTask().execute(mTextBeforeTranslation.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //endregion

        //region Кнопка перевода
        final Button btTranslate = (Button) findViewById(R.id.btTranslate);
        btTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Если тект для перевода состоит из пробелов, переводить не будем
                if (mTextBeforeTranslation.getText().toString().trim().length() == 0)
                    return;
                new TranslateTask().execute(mTextBeforeTranslation.getText().toString());
                // Если уже много срок, спрячем клавиатуру, чтобы было видно перевод
                if (mTextBeforeTranslation.getLineCount() > Constants.NUM_LINES_WHEN_HIDE_KEYBOARD) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
        //endregion

        //region Кнопка "крестик" для очистки оригинала текста
        Button btCleanTextBeforeTranslate = (Button) findViewById(R.id.btCleanTextBeforeTranslate);
        btCleanTextBeforeTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextBeforeTranslation.setText("");
                mTextAfterTranslation.setText("");
                tbFavorite.setChecked(false);
                tbFavorite.setEnabled(false);

            }
        });
        //endregion

        //region Переключатель направления перевода
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

        //region Кнопка Добавить/Удалить из Избранного переведенный текст
        tbFavorite = (ToggleButton) findViewById(R.id.tbFavorite);
        tbFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CompoundButton buttonView = (CompoundButton) v;
                    // Из тега кнопки возьмем заранее сохраненный там экземпляр перевода
                    TranslationUnit unit = (TranslationUnit) buttonView.getTag();
                    // Установим статус Избранный/Неизбранный
                    unit.setFavorite(buttonView.isChecked());
                    // Добавим/Удалим из списка избранного
                    if (buttonView.isChecked())
                        TranslateActivity.getTranslationFavoriteList().addFirst(unit);
                    else
                        TranslateActivity.getTranslationFavoriteList().remove(unit);
                } catch (ClassCastException e) {
                    Log.e(Constants.LOG_TAG, "No TranslationUnit in button" + e.getLocalizedMessage());
                } catch (NullPointerException e) {
                    Log.e(Constants.LOG_TAG, "TranslationUnit in button is null" + e.getLocalizedMessage());
                }

            }
        });
        //endregion
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Выделим иконку "Перевод" нижнего меню
        navigation.getMenu().getItem(0).setChecked(true);
    }


    public static LinkedList<TranslationUnit> getTranslationHistoryList() {
        return translationHistoryList;
    }

    public static LinkedList<TranslationUnit> getTranslationFavoriteList() {
        return translationFavoriteList;
    }


    /**
     * Поток для передачи запроса в Яндекс.Переводчик
     */

    private class TranslateTask extends AsyncTask<String, Void, TranslationUnit> {

        @Override
        protected TranslationUnit doInBackground(String... text) {
            // Прежде чем отправлять запрос обратимся к истории. Если там уже есть такой запрос,
            // покажем перевод из истории и запрос отправлять не будем.
            LinkedList<TranslationUnit> translationList = TranslateActivity.getTranslationHistoryList();
            String textBeforeTranslation = text[0].toLowerCase().trim();
            for (TranslationUnit unit : translationList) {
                if (unit.getTextBeforeTranslate().toLowerCase().trim().equals(textBeforeTranslation))
                    return unit;
            }

            JSONResponse jsonResponse;
            try {

                Translate service = retrofit.create(Translate.class);

                jsonResponse = service.getData(Constants.YANDEX_API_KEY, text[0], CURRENT_DIRECTION_LANG).execute().body();
                // Сервис вернул ошибку
                if (jsonResponse == null) throw new Exception("Error in response. See log OkHttp.");
                Log.d(Constants.LOG_TAG, "Response: " + jsonResponse.toString());
                // Добавим результат в историю переводов, создав экземпляр Перевода.
                TranslationUnit unit = new TranslationUnit(text[0], jsonResponse.getText().get(0), CURRENT_DIRECTION_LANG);
                translationList.addFirst(unit);
                return unit;

            } catch (Exception e) {
                Log.e(Constants.LOG_TAG, e.getLocalizedMessage());
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(TranslationUnit unit) {
            if (unit == null) return;
            mTextAfterTranslation.setText(unit.getTextAfterTranslate());
            // Положим в тег кнопки объект перевода, чтобы была возможность обработать ее нажатие и добавить этот перевод в Избранное
            tbFavorite.setTag(unit);
            tbFavorite.setChecked(unit.isFavorite());
            tbFavorite.setEnabled(true);

        }
    }
}
