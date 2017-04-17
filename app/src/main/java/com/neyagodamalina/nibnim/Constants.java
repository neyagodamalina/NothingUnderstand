package com.neyagodamalina.nibnim;

/**
 * Интерфейс для констант
 */

public interface Constants {
    // Тег для логирования. Удобно настроить фильтр logcat
    public static final String LOG_TAG = "niBniM";

    // Количество строк в тексте до перевода, при превышении которого
    // после совершения перевода скрываем клавиатуру,
    // чтобы она не загораживала текст после перевода
    public static final int NUM_LINES_WHEN_HIDE_KEYBOARD = 4;
}
