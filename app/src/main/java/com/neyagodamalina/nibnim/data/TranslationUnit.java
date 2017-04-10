package com.neyagodamalina.nibnim.data;

/**
 * Единица перевода. Не важно в какую сторону был перевод
 */



public class TranslationUnit {
    private String textBeforeTranslate;
    private String textAfterTranslate;
    private String directionTranslate;
    private boolean isFavorite;

    public TranslationUnit(String textBeforeTranslate, String textAfterTranslate, String directionTranslate) {
        this.textBeforeTranslate = textBeforeTranslate;
        this.textAfterTranslate = textAfterTranslate;
        this.directionTranslate = directionTranslate;
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

    public String getDirectionTranslate() {
        return directionTranslate;
    }

    public void setDirectionTranslate(String directionTranslate) {
        this.directionTranslate = directionTranslate;
    }
}
