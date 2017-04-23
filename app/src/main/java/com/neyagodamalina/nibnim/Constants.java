package com.neyagodamalina.nibnim;

/**
 * Интерфейс для констант
 */

interface Constants {
    // API-key Yandex
    String YANDEX_API_KEY = "trnsl.1.1.20170403T184448Z.18208d2f735ce38d.70c4c3b2ae948888de8c8394cc7c8b38f22712dc";

    // Тег для логирования. Удобно настроить фильтр logcat
    String LOG_TAG = "niBniM";

    // Количество строк в тексте до перевода, при превышении которого
    // после совершения перевода скрываем клавиатуру,
    // чтобы она не загораживала текст после перевода
    int NUM_LINES_WHEN_HIDE_KEYBOARD = 4;

}
