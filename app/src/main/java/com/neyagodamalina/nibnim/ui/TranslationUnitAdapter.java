package com.neyagodamalina.nibnim.ui;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.neyagodamalina.nibnim.R;
import com.neyagodamalina.nibnim.TranslateActivity;
import com.neyagodamalina.nibnim.data.TranslationUnit;

import java.util.List;

/**
 * Адаптер для заполнения строки списка Истории и Избранного
 */

public class TranslationUnitAdapter extends ArrayAdapter<TranslationUnit> {


    public TranslationUnitAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<TranslationUnit> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

        TranslationUnit unit = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);
        }

        ((TextView) convertView.findViewById(R.id.tvListItemTextBeforeTranslate)).setText(unit.getTextBeforeTranslate());
        ((TextView) convertView.findViewById(R.id.tvListItemTextAfterTranslate)).setText(unit.getTextAfterTranslate());
        ((TextView) convertView.findViewById(R.id.tvRuEn)).setText(unit.getDirectionTranslate());
        ToggleButton buttonFavorite = (ToggleButton) convertView.findViewById(R.id.tbListItemIsFavorite);
        // в тэг запомним позицию, чтобы в обработке события добавления в "Избранное" понять какой перевод был нажат
        buttonFavorite.setTag(position);
        buttonFavorite.setChecked(unit.isFavorite());
        buttonFavorite.setOnClickListener(onClickListener);

        return convertView;

    }

    public TranslationUnit getTranslationUnit(int position) {
        return this.getItem(position);
    }

    // обработчик для favorite, добавляет или удаляет из избранного
    CompoundButton.OnClickListener onClickListener = new CompoundButton.OnClickListener(){

        @Override
        public void onClick(View v) {
            ToggleButton buttonFavorite = (ToggleButton) v;
            TranslationUnit unit = getTranslationUnit((Integer) buttonFavorite.getTag());
            // устанавливаем признак избранного/неизбранного
            unit.setFavorite(buttonFavorite.isChecked());
            // если избранный, добавляем его в список Избранного
            if (buttonFavorite.isChecked())
                TranslateActivity.getTranslationFavoriteList().addFirst(unit);
            else
                TranslateActivity.getTranslationFavoriteList().remove(unit);

        }
    };
}
