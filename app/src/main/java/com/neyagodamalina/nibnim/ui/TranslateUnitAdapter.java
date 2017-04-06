package com.neyagodamalina.nibnim.ui;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.neyagodamalina.nibnim.R;
import com.neyagodamalina.nibnim.TranslationUnit;

import java.util.List;

/**
 * Адаптер для заполнения строки списка Истории и Любимых
 */

public class TranslateUnitAdapter extends ArrayAdapter<TranslationUnit> {


    public TranslateUnitAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<TranslationUnit> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        TranslationUnit unit = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);
        }

        ((TextView) convertView.findViewById(R.id.tvListItemTextBeforeTranslate)).setText(unit.getTextBeforeTranslate());
        ((TextView) convertView.findViewById(R.id.tvListItemTextAfterTranslate)).setText(unit.getTextAfterTranslate());

        return convertView;

    }


}
