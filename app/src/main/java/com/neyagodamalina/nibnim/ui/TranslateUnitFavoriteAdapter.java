package com.neyagodamalina.nibnim.ui;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.neyagodamalina.nibnim.data.TranslationUnit;

import java.util.List;

/**
 * Created by developer on 10.04.2017.
 */

public class TranslateUnitFavoriteAdapter extends TranslateUnitHistoryAdapter {
    public TranslateUnitFavoriteAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<TranslationUnit> objects) {
        super(context, resource, objects);
    }


    /**
     * Подготовим View из тех элементов списка, которые помеченны как Избранные isFavorite == true
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TranslationUnit unit = getTranslateUnit(position);
        if (!unit.isFavorite()){
            this.remove(unit);
            return null;
        }
        return super.getView(position, convertView, parent);
    }


}
