package com.my.newvoicetyping.keyboard;

import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

public class LatinKeyboardView_sindhiBest extends KeyboardView {

    static final int KEYCODE_OPTIONS = -100;
    // TODO: Move this into android.inputmethodservice.Keyboard
    static final int KEYCODE_ENGLISH_SHIFT = -11;
    static final int KEYCODE_ENGLISH_BACK = -12;
    static final int KEYCODE_ENGLISH = -15;
    static final int KEYCODE_URDU = -14;
    static final int KEYCODE_SETTING = -16;
    static final int KEYCODE_THEMES = -17;
    static final int KEYCODE_CHANGE = -100;
    static final int KEYCODE_ENGLISH_SYMBOLS = -200;
    static final int KEYCODE_EMOJI=-300;
    static final int KEYCODE_ASSENDING=-402;
    static final int KEYCODE_SMEMOJI=-301;
    static final int KEYCODE_ALLEMOJI=-999;
    static final int KEYCODE_ads=-15;
    public LatinKeyboardView_sindhiBest(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public LatinKeyboardView_sindhiBest(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public LatinKeyboard_sindhiBest getKeyboard() {
        try {
            return (LatinKeyboard_sindhiBest) super.getKeyboard();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }
}
