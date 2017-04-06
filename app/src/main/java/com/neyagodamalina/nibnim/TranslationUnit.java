package com.neyagodamalina.nibnim;

/**
 * Created by developer on 06.04.2017.
 */

public class TranslationUnit {
    private String textBeforeTranslate;
    private String textAfterTranslate;
    private boolean isFavorite;

    public TranslationUnit(String textBeforeTranslate, String textAfterTranslate) {
        this.textBeforeTranslate = textBeforeTranslate;
        this.textAfterTranslate = textAfterTranslate;
    }


    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getTextBeforeTranslate() {
        return textBeforeTranslate;
    }

    public void setTextBeforeTranslate(String textBeforeTranslate) {
        this.textBeforeTranslate = textBeforeTranslate;
    }

    public String getTextAfterTranslate() {
        return textAfterTranslate;
    }

    public void setTextAfterTranslate(String textAfterTranslate) {
        this.textAfterTranslate = textAfterTranslate;
    }
}
