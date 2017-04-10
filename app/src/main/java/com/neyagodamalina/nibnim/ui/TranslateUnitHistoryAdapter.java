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
import com.neyagodamalina.nibnim.data.TranslationUnit;

import java.util.List;

/**
 * Адаптер для заполнения строки списка Истории и Избранного
 */

public class TranslateUnitHistoryAdapter extends ArrayAdapter<TranslationUnit> {


    public TranslateUnitHistoryAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<TranslationUnit> objects) {
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
        ToggleButton buttonFavorite = (ToggleButton) convertView.findViewById(R.id.tbListItemIsFavorite);
        // в тэг запомним позицию, чтобы в обработке события добавления в "Избранное" понять какой первод был нажат
        buttonFavorite.setTag(position);
        buttonFavorite.setChecked(unit.isFavorite());
        buttonFavorite.setOnCheckedChangeListener(myCheckChangeList);

        return convertView;

    }

    public TranslationUnit getTranslateUnit(int position) {
        return this.getItem(position);
    }

    // обработчик для favorite, добавляет или удаляет из избранного
    CompoundButton.OnCheckedChangeListener myCheckChangeList = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            getTranslateUnit((Integer) buttonView.getTag()).setFavorite(isChecked);
        }
    };

}