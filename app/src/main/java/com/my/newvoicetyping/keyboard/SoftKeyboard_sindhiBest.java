package com.my.newvoicetyping.keyboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.os.Build;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.text.method.MetaKeyKeyListener;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;

import com.my.newvoicetyping.Appmainactivity;
import com.my.newvoicetyping.R;
import com.my.newvoicetyping.ui.Themes.ThemeFragment;

import java.util.ArrayList;
import java.util.List;

import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

@SuppressLint("NewApi")
public class SoftKeyboard_sindhiBest extends InputMethodService implements
        KeyboardView.OnKeyboardActionListener,
        SharedPreferences.OnSharedPreferenceChangeListener {
    static final boolean DEBUG = false;

    /**
     * This boolean indicates the optional example code for performing
     * processing of hard keys in addition to regular text generation from
     * on-screen interaction. It would be used for input_sindhiBest methods that perform
     * language translations (such as converting text entered on a QWERTY
     * keyboard to Chinese), but may not be used for input_sindhiBest methods that are
     * primarily intended to be used for on-screen text entry.
     */
    static final boolean PROCESS_HARD_KEYS = true;
    private InputMethodManager mInputMethodManager;

    private LatinKeyboardView_sindhiBest mInputView;
    private CandidateView_sindhiBest mCandidateView;
    private CompletionInfo[] mCompletions;
    private StringBuilder mComposing = new StringBuilder();
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean mVibrate, mSound, mPreview;
    private Vibrator vibrator;
    private boolean urShift = false;

    private int count = R.drawable.bg_keyboard_white;
    private int keyCount = R.layout.keytheme_1;
    private int lngCode = 0x0;

    private int theme = R.drawable.bg_keyboard_white;
    private int shirtKey[] = {R.drawable.board_theme_white, R.drawable.board_theme__black,
            R.drawable.board_theme_aqua, R.drawable.board_theme__red, R.drawable.board_theme_green,
            R.drawable.board_theme_meroon, R.drawable.board_theme_pink, R.drawable.board_theme_dark_green,
            R.drawable.board_theme_purple, R.drawable.board_theme_yellow, R.drawable.board_theme_blue,
            R.drawable.board_theme_12, R.drawable.board_theme_13, R.drawable.board_theme_14,
            R.drawable.board_theme_15
    };

    Drawable mDrawableTheme;

    // Keep track of the last composing text that was manually corrected by user
    private String mUserComposing = "";
    private List<String> mPredictions;
    private String[][] mWordChoices;
    private Dictionary_sindhiBest mDictionary;
    private boolean mPredictionOn;
    private boolean mCompletionOn;
    private boolean mAutoCompletionOn;
    private int mLastDisplayWidth;
    private boolean mCapsLock;
    private boolean mCapsLocked;
    private long mMetaState;
    private LatinKeyboard_sindhiBest mCurKeyboard;
    private String mWordSeparators;
    private String mSpecialSeparators;
    private String mAutoSpace = "";
    private static final List<String> EMPTY_LIST = new ArrayList<>();
    private LatinKeyboard_sindhiBest urSymbolsKeyboard, urSymbolsCKeyboard;
    private LatinKeyboard_sindhiBest mUrduKeyboard, mUrduKeyboardC;
    private LatinKeyboard_sindhiBest mEngKeyboard, mEngKeyboardC, mEngKeyboardShift,
            enSymbolsKeyboard, enSymbolsCKeyboard, smilykeyboard;
    private LatinKeyboard_sindhiBest emojiKeyboard, assending_order;
    /*edit for add emoji library*/
    EmojiconsPopup popup;

    // private boolean isLandscape = false;
    // private boolean isPortrait = false;

    /**
     * MainActivity_sindhiBest initialization of the input_sindhiBest method component.  Be sure to call to
     * super class.
     */
    @SuppressLint("NewApi")
    @Override
    public void onCreate() {

        super.onCreate();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        count = mSharedPreferences.getInt("myTheme", 0);
        lngCode = mSharedPreferences.getInt("INPUT_LANGUAGE", 0);
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mWordSeparators = getResources().getString(R.string.word_separators);
        mSpecialSeparators = getResources().getString(
                R.string.special_separators);

        mDictionary = Dictionary_sindhiBest.getInstance(getResources().openRawResource(
                R.raw.dict));
        EMPTY_LIST.add("the");
        EMPTY_LIST.add("of");
        EMPTY_LIST.add("and");
        EMPTY_LIST.add("to");
        EMPTY_LIST.add("in");
        EMPTY_LIST.add("for");
        EMPTY_LIST.add("is");
        EMPTY_LIST.add("on");
        EMPTY_LIST.add("that");
        EMPTY_LIST.add("by");
        EMPTY_LIST.add("with");
        EMPTY_LIST.add("this");
        EMPTY_LIST.add("i");
    }

    /**
     * This is the point where you can do all of your UI initialization. It is
     * called after creation and any configuration change.
     */

    @Override
    public void onInitializeInterface() {
        if (mUrduKeyboard != null) {
            // Configuration changes can happen after the keyboard gets
            // recreated,
            // so we need to be able to re-build the keyboards if the available
            // space has changed.
            int displayWidth = getMaxWidth();
            if (displayWidth == mLastDisplayWidth)
                return;
            mLastDisplayWidth = displayWidth;
        }

        mUrduKeyboard = new LatinKeyboard_sindhiBest(this, R.xml.qwerty_ur);
        mUrduKeyboardC = new LatinKeyboard_sindhiBest(this, R.xml.qwerty_ur_c);
        urSymbolsKeyboard = new LatinKeyboard_sindhiBest(this, R.xml.symbols_ur);
        urSymbolsCKeyboard = new LatinKeyboard_sindhiBest(this, R.xml.symbols_ur2);
        enSymbolsKeyboard = new LatinKeyboard_sindhiBest(this, R.xml.symbols_eng);
        enSymbolsCKeyboard = new LatinKeyboard_sindhiBest(this, R.xml.symbols_eng2);
        mEngKeyboard = new LatinKeyboard_sindhiBest(this, R.xml.qwerty_eng);
        mEngKeyboardC = new LatinKeyboard_sindhiBest(this, R.xml.qwerty_eng_c);
        mEngKeyboardShift = new LatinKeyboard_sindhiBest(this, R.xml.qwerty_eng_shift);
        smilykeyboard = new LatinKeyboard_sindhiBest(this, R.xml.smily_iconkeyboard);
        emojiKeyboard = new LatinKeyboard_sindhiBest(this, R.xml.qwerty_emoji);
        assending_order = new LatinKeyboard_sindhiBest(this, R.xml.assending_order);
    }

    /**
     * Called by the framework when your view for creating input_sindhiBest needs to be
     * generated. This will be called the first time your input_sindhiBest method is
     * displayed, and every time it needs to be re-created such as due to a
     * configuration change.
     */
    @SuppressLint("InflateParams")
    @Override
    public View onCreateInputView() {
        Log.d("OnCreateInputView111", "OnCreateInputView111444444");
        count = mSharedPreferences.getInt("myTheme", 0);
        setKbThemes(count);
        //     mInputView = (LatinKeyboardView_sindhiBest) getLayoutInflater().inflate(R.layout.keytheme_1, null);
        //       count = mSharedPreferences.getInt("theme", 0);
        mInputView.setOnKeyboardActionListener(this);
        loadPreferences(mSharedPreferences);
        loadPreviewPreferences(mSharedPreferences);
        loadAutocompletionPreferences(mSharedPreferences);
        loadPredictionPreferences(mSharedPreferences);
        setKeyboardLanguage();
        mInputView.setPreviewEnabled(false);

    /*    mInputView = (KeyboardView) getLayoutInflater().inflate(
                R.layout.keytheme_2, null);
        mInputView.setBackgroundResource(R.drawable.buttonbgselector);
        mInputView.setOnKeyboardActionListener(this);
        mInputView.setKeyboard(mQwertyKeyboard);*/
        /*edit for add emoji library*/
//        this.popup = new EmojiconsPopup(this.mInputView, this);
//        this.popup.setSizeForSoftKeyboard();
//        this.popup.setSize(FrameLayout.LayoutParams.MATCH_PARENT, /*FrameLayout.LayoutParams.MATCH_PARENT*/mInputView.getKeyboard().getHeight());
        /*end emoji library*/
        return mInputView;
    }

    /**
     * Called by the framework when your view for showing candidates needs to be
     * generated, like {@link #onCreateInputView}.
     */
    @Override
    public View onCreateCandidatesView() {
        LayoutInflater li = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View wordBar = li.inflate(R.layout.mylayout_sindhibest, null);
        final LinearLayout ll = wordBar.findViewById(R.id.wordsbar);

        mCandidateView = new CandidateView_sindhiBest(this);
        mCandidateView.setService(this);
        setCandidatesViewShown(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 0.5f;
        ll.setLayoutParams(params);

//        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean(AdsHandling.PURCHASE, false)) {
//            AdsHandling.getInstance().loadAdaptiveBanner(this, ll.findViewById(R.id.adView));
//        }
        ll.addView(mCandidateView);

        return ll;

    }


    @Override
    public void onComputeInsets(InputMethodService.Insets outInsets) {
        super.onComputeInsets(outInsets);
        if (!isFullscreenMode()) {
            outInsets.contentTopInsets = outInsets.visibleTopInsets;
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @SuppressWarnings("deprecation")
    public void setKbThemes(int count) {
        try {
            mInputView = (LatinKeyboardView_sindhiBest) getLayoutInflater().inflate(R.layout.keytheme_1, null);
            mDrawableTheme = getResources().getDrawable(R.color.keyboard_bg_white);
            if (count == R.drawable.board_theme_white) {
                mInputView = (LatinKeyboardView_sindhiBest) getLayoutInflater().inflate(R.layout.keytheme_1, null);
                mDrawableTheme = getResources().getDrawable(R.color.keyboard_bg_white);
            }
            if (count == R.drawable.board_theme__black) {
                mInputView = (LatinKeyboardView_sindhiBest) getLayoutInflater().inflate(R.layout.keytheme_2, null);
                mDrawableTheme = getResources().getDrawable(R.color.keyboard_bg_black);
            }
            if (count == R.drawable.board_theme_aqua) {
                mInputView = (LatinKeyboardView_sindhiBest) getLayoutInflater().inflate(R.layout.keytheme_3, null);
                mDrawableTheme = getResources().getDrawable(R.color.keyboard_bg_aqua);
            }
            if (count == R.drawable.board_theme__red) {
                mInputView = (LatinKeyboardView_sindhiBest) getLayoutInflater().inflate(R.layout.keytheme_4, null);
                mDrawableTheme = getResources().getDrawable(R.color.board_theme__red);
            }
            if (count == R.drawable.board_theme_green) {
                mInputView = (LatinKeyboardView_sindhiBest) getLayoutInflater().inflate(R.layout.keytheme_5, null);
                mDrawableTheme = getResources().getDrawable(R.color.board_theme_green);
            }
            if (count == R.drawable.board_theme_meroon) {
                mInputView = (LatinKeyboardView_sindhiBest) getLayoutInflater().inflate(R.layout.keytheme_6, null);
                mDrawableTheme = getResources().getDrawable(R.color.board_theme_meroon);
            }
  /*
            if (count == R.drawable.board_theme_pink) {
                mInputView = (LatinKeyboardView_sindhiBest) getLayoutInflater().inflate(R.layout.keytheme_7, null);
                mDrawableTheme = getResources().getDrawable(R.color.board_theme_pink);
            }
            if (count == R.drawable.board_theme_dark_green) {
                mInputView = (LatinKeyboardView_sindhiBest) getLayoutInflater().inflate(R.layout.keytheme_8, null);
                mDrawableTheme = getResources().getDrawable(R.color.board_theme_dark_green);
            }
            if (count == R.drawable.board_theme_purple) {
                mInputView = (LatinKeyboardView_sindhiBest) getLayoutInflater().inflate(R.layout.keytheme_9, null);
                mDrawableTheme = getResources().getDrawable(R.color.board_theme_purple);
            }
            if (count == R.drawable.board_theme_yellow) {
                mInputView = (LatinKeyboardView_sindhiBest) getLayoutInflater().inflate(R.layout.keytheme_10, null);
                mDrawableTheme = getResources().getDrawable(R.color.board_theme_yellow);
            }
            if (count == R.drawable.board_theme_blue) {
                mInputView = (LatinKeyboardView_sindhiBest) getLayoutInflater().inflate(R.layout.keytheme_11, null);
                mDrawableTheme = getResources().getDrawable(R.color.board_theme_blue);
            }
            if (count == R.drawable.board_theme_12) {
                mInputView = (LatinKeyboardView_sindhiBest) getLayoutInflater().inflate(R.layout.keytheme_12, null);
                mDrawableTheme = getResources().getDrawable(R.color.board_theme_12);
            }
            if (count == R.drawable.board_theme_13) {
                mInputView = (LatinKeyboardView_sindhiBest) getLayoutInflater().inflate(R.layout.keytheme_13, null);
                mDrawableTheme = getResources().getDrawable(R.color.board_theme_13);
            }
            if (count == R.drawable.board_theme_14) {
                mInputView = (LatinKeyboardView_sindhiBest) getLayoutInflater().inflate(R.layout.keytheme_14, null);
                mDrawableTheme = getResources().getDrawable(R.color.board_theme_14);
            }
            if (count == R.drawable.board_theme_15) {
                mInputView = (LatinKeyboardView_sindhiBest) getLayoutInflater().inflate(R.layout.keytheme_15, null);
                mDrawableTheme = getResources().getDrawable(R.color.board_theme_15);*/


            mInputView.setBackgroundDrawable(mDrawableTheme);
            Log.d("KeyboardApplied", String.valueOf(count));

        } catch (OutOfMemoryError e) {
        }
    }

/*
    private void themes() {
        editor = mSharedPreferences.edit();
          setKbThemes(count);

           editor.putInt("myTheme", count);
          editor.commit();
        ////

        if (theme < 15) {
            theme++;
            setKbThemes(shirtKey[theme]);
            editor.putInt("myTheme", shirtKey[theme]);

        } else {
            theme = 0;
            editor.putInt("myTheme", shirtKey[theme]);
            setKbThemes(shirtKey[theme]);
        }
        editor.commit();
    }
*/

    private void setLandscapeKbThemes() {
        try {
            //        mDrawableTheme = getResources().getDrawable(themes[1]);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mInputView.setBackground(mDrawableTheme);
            } else {
                mInputView.setBackgroundDrawable(mDrawableTheme);
            }
        } catch (OutOfMemoryError e) {
        }
    }

    private void setKeyboardLanguage() {
        if (lngCode == 0x0) {
            mInputView.setKeyboard(mEngKeyboard);
        } else {
            mInputView.setKeyboard(mEngKeyboard);
        }
    }

    /**
     * This is the main_sindhiBest point where we do our initialization of the input_sindhiBest method
     * to begin operating on an application. At this point we have been bound to
     * the client, and are now receiving all of the detailed information about
     * the target of our edits.
     */
    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);
        setInputView(onCreateInputView());
        // Reset our state. We want to do this even if restarting, because
        // the underlying state of the text editor could have changed in any
        // way.
        mComposing.setLength(0);
        updateCandidates();

        if (!restarting) {
            // Clear shift states.
            mMetaState = 0;
        }

        mPredictionOn = false;
        mCompletionOn = false;
        mCompletions = null;

        // We are now going to initialize our state based on the type of
        // text being edited.
        switch (attribute.inputType & InputType.TYPE_MASK_CLASS) {
            case InputType.TYPE_CLASS_NUMBER:
            case InputType.TYPE_CLASS_DATETIME:
                // Numbers and dates default to the symbols keyboard, with
                // no extra features.
                mCurKeyboard = enSymbolsKeyboard;
                break;

            case InputType.TYPE_CLASS_PHONE:
                // Phones will also default to the symbols keyboard, though
                // often you will want to have a dedicated phone keyboard.
                mCurKeyboard = enSymbolsKeyboard;
                break;

            case InputType.TYPE_CLASS_TEXT:
                // This is general text editing. We will default to the
                // normal alphabetic keyboard, and assume that we should
                // be doing predictive text (showing candidates as the
                // user types).
                if (lngCode == 0x0) {
                    mCurKeyboard = mUrduKeyboard;
                } else {
                    mCurKeyboard = mEngKeyboard;
                }
                loadPredictionPreferences(mSharedPreferences);
                //setSuggestions(EMPTY_LIST, null, true, true);
                // mPredictionOn = true;

                // We now look for a few special variations of text that will
                // modify our behavior.
                int variation = attribute.inputType & InputType.TYPE_MASK_VARIATION;
                if (variation == InputType.TYPE_TEXT_VARIATION_PASSWORD
                        || variation == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    // Do not display predictions / what the user is typing
                    // when they are entering a password.
                    mPredictionOn = false;
                }

                if (variation == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                        || variation == InputType.TYPE_TEXT_VARIATION_URI
                        || variation == InputType.TYPE_TEXT_VARIATION_FILTER) {
                    // Our predictions are not useful for e-mail addresses
                    // or URIs.
                    mPredictionOn = false;
                }

                if ((attribute.inputType & InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE) != 0) {
                    // If this is an auto-complete text view, then our predictions
                    // will not be shown and instead we will allow the editor
                    // to supply their own. We only show the editor's
                    // candidates when in fullscreen mode, otherwise relying
                    // own it displaying its own UI.
                    mPredictionOn = false;
                    mCompletionOn = isFullscreenMode();
                }

                // We also want to look at the current state of the editor
                // to decide whether our alphabetic keyboard should start out
                // shifted.
                updateShiftKeyState(attribute);
                break;

            default:
                // For all unknown input_sindhiBest types, default to the alphabetic
                // keyboard with no special features.
                if (lngCode == 0x0) {
                    mCurKeyboard = mUrduKeyboard;
                } else {
                    mCurKeyboard = mEngKeyboard;
                }
                updateShiftKeyState(attribute);
        }

        // Update the label on the enter key, depending on what the application
        // says it will do.
        mCurKeyboard.setImeOptions(getResources(), attribute.imeOptions);
    }

    /**
     * This is called when the user is done editing a field. We can use this to
     * reset our state.
     */
    @Override
    public void onFinishInput() {
        super.onFinishInput();

        // Clear current composing text and candidates.
        mComposing.setLength(0);
        updateCandidates();

        // We only hide the candidates window when finishing input_sindhiBest on
        // a particular editor, to avoid popping the underlying application
        // up and down if the user is entering text into the bottom of
        // its window.
        setCandidatesViewShown(false);

        mCurKeyboard = mUrduKeyboard;
        if (mInputView != null) {
            mInputView.closing();
        }
    }

    @Override
    public void onStartInputView(EditorInfo attribute, boolean restarting) {
        super.onStartInputView(attribute, restarting);
        // Apply the selected keyboard to the input_sindhiBest view.
        lngCode = mSharedPreferences.getInt("INPUT_LANGUAGE", 0);
        setKeyboardLanguage();
        mInputView.closing();
    }

    /**
     * Deal with the editor reporting movement of its cursor.
     */
    @Override
    public void onUpdateSelection(int oldSelStart, int oldSelEnd,
                                  int newSelStart, int newSelEnd, int candidatesStart,
                                  int candidatesEnd) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd,
                candidatesStart, candidatesEnd);

        // If the current selection in the text view changes, we should
        // clear whatever candidate text we have.
        if (mComposing.length() > 0
                && (newSelStart != candidatesEnd || newSelEnd != candidatesEnd)) {


            mComposing.setLength(0);
            updateCandidates();
            InputConnection ic = getCurrentInputConnection();
            if (ic != null) {
                ic.finishComposingText();
            }
        }
    }

    /**
     * This tells us about completions that the editor has determined based on
     * the current text in it. We want to use this in fullscreen mode to show
     * the completions ourself, since the editor can not be seen in that
     * situation.
     */
    @Override
    public void onDisplayCompletions(CompletionInfo[] completions) {
        if (mCompletionOn) {
            mCompletions = completions;
            if (completions == null) {
                setSuggestions(null, null, false, false);
                return;
            }

            List<String> stringList = new ArrayList<String>();
            for (int i = 0; i < completions.length; i++) {
                CompletionInfo ci = completions[i];
                if (ci != null)
                    stringList.add(ci.getText().toString());
            }
            setSuggestions(stringList, null, true, true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration conf) {
        // If orientation changed while predicting, commit the change
        InputConnection ic = getCurrentInputConnection();
        if (mComposing.length() > 0) {
            if (ic != null) {
                ic.commitText(mComposing, 1);
            }
        }
        super.onConfigurationChanged(conf);
        // // Checks the orientation of the screen
        // if (conf.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        // isLandscape = false;
        // Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        // } else if (conf.orientation == Configuration.ORIENTATION_PORTRAIT){
        // isLandscape = true;
        // Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        // }
    }

    /**
     * This translates incoming hard key events in to edit operations on an
     * InputConnection. It is only needed when using the PROCESS_HARD_KEYS
     * option.
     */
    private boolean translateKeyDown(int keyCode, KeyEvent event) {
        mMetaState = MetaKeyKeyListener.handleKeyDown(mMetaState, keyCode,
                event);
        int c = event.getUnicodeChar(MetaKeyKeyListener
                .getMetaState(mMetaState));
        mMetaState = MetaKeyKeyListener.adjustMetaAfterKeypress(mMetaState);
        InputConnection ic = getCurrentInputConnection();
        if (c == 0 || ic == null) {
            return false;
        }

        @SuppressWarnings("unused")
        boolean dead = false;

        if ((c & KeyCharacterMap.COMBINING_ACCENT) != 0) {
            dead = true;
            c = c & KeyCharacterMap.COMBINING_ACCENT_MASK;
        }

        if (mComposing.length() > 0) {
            char accent = mComposing.charAt(mComposing.length() - 1);
            int composed = KeyEvent.getDeadChar(accent, c);

            if (composed != 0) {
                c = composed;
                mComposing.setLength(mComposing.length() - 1);
            }
        }

        onKey(c, null);

        return true;
    }

    /**
     * Use this to monitor key events being delivered to the application. We get
     * first crack at them, and can either resume them or let them continue to
     * the app.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                // The InputMethodService already takes care of the back
                // key for us, to dismiss the input_sindhiBest method if it is shown.
                // However, our keyboard could be showing a pop-up window
                // that back should dismiss, so we first allow it to do that.
                if (event.getRepeatCount() == 0 && mInputView != null) {
                    if (mInputView.handleBack()) {
                        return true;
                    }
                }
                break;

            case KeyEvent.KEYCODE_DEL:
                // Special handling of the delete key: if we currently are
                // composing text for the user, we want to modify that instead
                // of let the application to the delete itself.
                if (mComposing.length() > 0) {
                    onKey(Keyboard.KEYCODE_DELETE, null);
                    return true;
                }
                break;

            case KeyEvent.KEYCODE_ENTER:
                // Let the underlying text editor always handle these.
                return false;

            default:
                // For all other keys, if we want to do transformations on
                // text being entered with a hard keyboard, we need to process
                // it and do the appropriate action.
                if (PROCESS_HARD_KEYS) {
                    if (keyCode == KeyEvent.KEYCODE_SPACE
                            && (event.getMetaState() & KeyEvent.META_ALT_ON) != 0) {
                        // A silly example: in our input_sindhiBest method, Alt+Space
                        // is a shortcut for 'android' in lower case.
                        InputConnection ic = getCurrentInputConnection();
                        if (ic != null) {
                            // First, tell the editor that it is no longer in the
                            // shift state, since we are consuming this.
                            ic.clearMetaKeyStates(KeyEvent.META_ALT_ON);
                            keyDownUp(KeyEvent.KEYCODE_A);
                            keyDownUp(KeyEvent.KEYCODE_N);
                            keyDownUp(KeyEvent.KEYCODE_D);
                            keyDownUp(KeyEvent.KEYCODE_R);
                            keyDownUp(KeyEvent.KEYCODE_O);
                            keyDownUp(KeyEvent.KEYCODE_I);
                            keyDownUp(KeyEvent.KEYCODE_D);
                            // And we consume this event.
                            return true;
                        }
                    }
                    if (mPredictionOn && translateKeyDown(keyCode, event)) {
                        return true;
                    }
                }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                // The InputMethodService already takes care of the back
                // key for us, to dismiss the input_sindhiBest method if it is shown.
                // However, our keyboard could be showing a pop-up window
                // that back should dismiss, so we first allow it to do that.
                if (event.getRepeatCount() == 0 && mInputView != null) {
                    if (mInputView.handleBack()) {
                        return true;
                    }
                }
                break;

            case KeyEvent.KEYCODE_DEL:
                // Special handling of the delete key: if we currently are
                // composing text for the user, we want to modify that instead
                // of let the application to the delete itself.
                if (mComposing.length() > 0) {
                    onKey(Keyboard.KEYCODE_DELETE, null);
                    return true;
                }
                break;

            case KeyEvent.KEYCODE_ENTER:
                // Let the underlying text editor always handle these.
                return false;

            default:
                // For all other keys, if we want to do transformations on
                // text being entered with a hard keyboard, we need to process
                // it and do the appropriate action.
                if (PROCESS_HARD_KEYS) {
                    if (keyCode == KeyEvent.KEYCODE_SPACE
                            && (event.getMetaState() & KeyEvent.META_ALT_ON) != 0) {
                        // A silly example: in our input_sindhiBest method, Alt+Space
                        // is a shortcut for 'android' in lower case.
                        InputConnection ic = getCurrentInputConnection();
                        if (ic != null) {
                            // First, tell the editor that it is no longer in the
                            // shift state, since we are consuming this.
                            ic.clearMetaKeyStates(KeyEvent.META_ALT_ON);
                            keyDownUp(KeyEvent.KEYCODE_A);
                            keyDownUp(KeyEvent.KEYCODE_N);
                            keyDownUp(KeyEvent.KEYCODE_D);
                            keyDownUp(KeyEvent.KEYCODE_R);
                            keyDownUp(KeyEvent.KEYCODE_O);
                            keyDownUp(KeyEvent.KEYCODE_I);
                            keyDownUp(KeyEvent.KEYCODE_D);
                            // And we consume this event.
                            return true;
                        }
                    }
                    if (mPredictionOn && translateKeyDown(keyCode, event)) {
                        return true;
                    }
                }
        }
        return super.onKeyLongPress(keyCode, event);
    }

    /**
     * Use this to monitor key events being delivered to the application. We get
     * first crack at them, and can either resume them or let them continue to
     * the app.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // If we want to do transformations on text being entered with a hard
        // keyboard, we need to process the up events to update the meta key
        // state we are tracking.
        if (PROCESS_HARD_KEYS) {
            if (mPredictionOn) {
                mMetaState = MetaKeyKeyListener.handleKeyUp(mMetaState,
                        keyCode, event);
            }
        }

        return super.onKeyUp(keyCode, event);
    }

    /**
     * Helper function to commit any text being composed in to the editor.
     */
    private void commitTyped(InputConnection inputConnection) {
        commitTyped(inputConnection, 0);
    }

    private void commitTyped(InputConnection inputConnection, int index) {
        try {

            if (mComposing.length() > 0) {
                List<String> suggestions = mCandidateView != null ? mCandidateView
                        .getSuggestions() : mPredictions;
                if (suggestions != null && suggestions.size() > index) {
                    String prediction = suggestions.get(index);
                    if (mAutoSpace == "ADD_SPACE") {
                        inputConnection.commitText(prediction + " ", 1);
                        mAutoSpace = "";
                    } else {
                        inputConnection.commitText(prediction, 1);
                    }
                } else {
                    inputConnection.commitText(mComposing, 1);
                }
                mComposing.setLength(0);
                updateCandidates();
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Helper to update the shift state of our keyboard based on the initial
     * editor state.
     */
    private void updateShiftKeyState(EditorInfo attr) {
        try {
            if (attr != null && mInputView != null
                    && mUrduKeyboard == mInputView.getKeyboard()) {
                int caps = 0;
                EditorInfo ei = getCurrentInputEditorInfo();
                if (ei != null && ei.inputType != InputType.TYPE_NULL) {
                    caps = getCurrentInputConnection().getCursorCapsMode(
                            attr.inputType);
                }
                mCapsLock = (mCapsLocked || caps != 0);
                mInputView.setShifted(mCapsLock);
            }
        } catch (Exception ignored) {
        }

    }

    /**
     * Helper to determine if a given character code is alphabetic.
     */
    private boolean isAlphabet(int code) {
        if (Character.isLetter(code) || Character.isDigit(code)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Helper to send a key down / key up pair to the current editor.
     */
    private void keyDownUp(int keyEventCode) {
        try {
            getCurrentInputConnection().sendKeyEvent(
                    new KeyEvent(KeyEvent.ACTION_DOWN, keyEventCode));
            getCurrentInputConnection().sendKeyEvent(
                    new KeyEvent(KeyEvent.ACTION_UP, keyEventCode));
        } catch (Exception ignored) {
        }

    }

    /**
     * Helper to send a character to the editor as raw key events.
     */
    private void sendKey(int keyCode) {
        switch (keyCode) {
            case '\n':
                keyDownUp(KeyEvent.KEYCODE_ENTER);
                break;
            default:
                if (keyCode >= '0' && keyCode <= '9') {
                    keyDownUp(keyCode - '0' + KeyEvent.KEYCODE_0);
                } else {
                    getCurrentInputConnection().commitText(
                            String.valueOf((char) keyCode), 1);
                }
                break;
        }
    }

    // Implementation of KeyboardViewListener
    public void onKey(int primaryCode, int[] keyCodes) {
        try {
            if (isWordSeparator(primaryCode)) {
                if (isSpecialSeparator(primaryCode)) {
                    handleCharacter(primaryCode, keyCodes);
                    Keyboard currentKeyboard = mInputView.getKeyboard();
                } else {
                    // Handle separator
                    commitTyped(getCurrentInputConnection());
                    sendKey(primaryCode);
                    updateShiftKeyState(getCurrentInputEditorInfo());
                }
            } else if (primaryCode == Keyboard.KEYCODE_DELETE) {
                handleBackspace();
            } else if (primaryCode == Keyboard.KEYCODE_SHIFT) {
                handleShift();
            } else if (primaryCode == Keyboard.KEYCODE_CANCEL) {
                handleClose();
                return;
            } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {
                handleModeChange();
            } else if (primaryCode == LatinKeyboardView_sindhiBest.KEYCODE_ENGLISH_SHIFT) {
                handleEngShift();
                return;
            } else if (primaryCode == LatinKeyboardView_sindhiBest.KEYCODE_ENGLISH_BACK) {
                handleEngBack();
                return;
            } else if (primaryCode == LatinKeyboardView_sindhiBest.KEYCODE_ENGLISH) {
                enableEnglish();
                return;
            } else if (primaryCode == LatinKeyboardView_sindhiBest.KEYCODE_URDU) {
                enableUrdu();
                return;
            } else if (primaryCode == LatinKeyboardView_sindhiBest.KEYCODE_CHANGE) {
                handleChange();
            } else if (primaryCode == LatinKeyboardView_sindhiBest.KEYCODE_ENGLISH_SYMBOLS) {
                handleEngSymbols();
            } else if (primaryCode == LatinKeyboardView_sindhiBest.KEYCODE_EMOJI) {
                handleEmoji();
            } else if (primaryCode == LatinKeyboardView_sindhiBest.KEYCODE_ASSENDING) {
                handleAssending();
            } else if (primaryCode == LatinKeyboardView_sindhiBest.KEYCODE_SMEMOJI) {
                handleAssending();
            } else if (primaryCode == LatinKeyboardView_sindhiBest.KEYCODE_SETTING) {
                setting();
            }
            /*edit for add emoji library*/
            else if (primaryCode == LatinKeyboardView_sindhiBest.KEYCODE_ALLEMOJI) {
               allemoji();
            } else if (primaryCode == LatinKeyboardView_sindhiBest.KEYCODE_THEMES) {
                //  startActivity(this, ThemeFragment);
         //       themes();
               /* Intent intent = new Intent(getApplicationContext(), Appmainactivity.class);
                intent.putExtra("key", "themeCheck");
                getApplicationContext().startActivity(intent);*/
                NavController nav = new NavController(getApplicationContext());
                nav.findDestination(R.id.nav_host_fragment_content_appmainactivity);
                nav.navigate(R.id.themeFragment);
                Toast.makeText(this, "sadasdasd", Toast.LENGTH_SHORT).show();

            } else {
                if (primaryCode >= 65 && primaryCode <= 90 && !mCapsLock) {
                    handleEngBack();
                }
                if (urShift) {
                    handleUrBack();
                }
                handleCharacter(primaryCode, keyCodes);
            }
        } catch (Exception ignored) {
        }

    }
    private void allemoji() {
        this.popup.showAtBottom();
        this.popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {
            public void onEmojiconClicked(Emojicon paramAnonymousEmojicon) {
                if (paramAnonymousEmojicon != null) {
                    getCurrentInputConnection().commitText(mComposing + paramAnonymousEmojicon.getEmoji(), 1);
                    mComposing.append(paramAnonymousEmojicon.getEmoji());
                }
            }
        });

        this.popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {
            public void onEmojiconBackspaceClicked(View paramAnonymousView) {
                SoftKeyboard_sindhiBest.this.getCurrentInputConnection().sendKeyEvent(new KeyEvent(0L, 0L, 0, 67, 0, 0, 0, 0, 6));
            }
        });

        this.popup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {
            /*comment for stable emojis when press more then one mojis*/
            public void onKeyboardClose() {

					/*if (SoftKeyboard_sindhiBest.this.popup.isShowing()) {

						SoftKeyboard_sindhiBest.this.popup.dismiss();
					}*/
            }

            public void onKeyboardOpen(int paramAnonymousInt) {
            }
        });

    }
    private void setting() {
        Intent intent = new Intent(SoftKeyboard_sindhiBest.this, PreferenceActivity_voicetyping.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void handleEngBack() {
        mInputView.setKeyboard(mEngKeyboard);
        mCapsLock = false;

    }

    private void handleUrBack() {
        mInputView.setKeyboard(mUrduKeyboard);
        urShift = false;

    }


    /*edit for add emoji library*/
//    private void allemoji() {
//        this.popup.showAtBottom();
//        this.popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {
//            public void onEmojiconClicked(Emojicon paramAnonymousEmojicon) {
//                if (paramAnonymousEmojicon != null) {
//                    getCurrentInputConnection().commitText(mComposing + paramAnonymousEmojicon.getEmoji(), 1);
//                    mComposing.append(paramAnonymousEmojicon.getEmoji());
//                }
//            }
//        });
//
//        this.popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {
//            public void onEmojiconBackspaceClicked(View paramAnonymousView) {
//                SoftKeyboard_sindhiBest.this.getCurrentInputConnection().sendKeyEvent(new KeyEvent(0L, 0L, 0, 67, 0, 0, 0, 0, 6));
//            }
//        });
//
//        this.popup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {
//            /*comment for stable emojis when press more then one mojis*/
//            public void onKeyboardClose() {
//
//					/*if (SoftKeyboard_sindhiBest.this.popup.isShowing()) {
//
//						SoftKeyboard_sindhiBest.this.popup.dismiss();
//					}*/
//            }
//
//            public void onKeyboardOpen(int paramAnonymousInt) {
//            }
//        });
//
//    }

    /*end here emoji lib*/
    public void onText(CharSequence text) {
        try {
            InputConnection ic = getCurrentInputConnection();
            if (ic == null)
                return;
            ic.beginBatchEdit();
            commitTyped(ic);
            ic.commitText(text, 0);
            ic.endBatchEdit();
            updateShiftKeyState(getCurrentInputEditorInfo());
        } catch (Exception ignored) {
        }

    }

    /**
     * Update the list of available candidates from the current composing text.
     * This will need to be filled in by however you are determining candidates.
     */
    private void updateCandidates() {
        try {
            if (!mCompletionOn) {
                if (mComposing.length() > 0) {
                    ArrayList<String> suggestions = new ArrayList<>();
                    if (mPredictions == null || mPredictions.size() <= 0) {
                        suggestions.add(mComposing.toString());
                    } else {
                        for (int i = 0; i < mPredictions.size(); i++) {
                            suggestions.add(mPredictions.get(i));
                        }
                    }
                    setSuggestions(suggestions, mWordChoices, true, true);
                } else {
                    setSuggestions(null, null, false, false);
                }
            }
        } catch (Exception ignored) {
        }

    }

    /**
     * Turn off prediction/suggestions if it's not already off.
     *
     * @return A boolean indicating the previous state of whether predictions
     * were turned on.
     */
    public boolean turnOffPredictionsIfNeeded() {
        if (mPredictionOn) {
            mPredictionOn = false;
            return true;
        }
        return false;
    }

    /**
     * Turn on/off prediction based on specified parameter.
     *
     * @param enabled A boolean indicating whether to turn on prediction.
     */
    public void enablePredictions(boolean enabled) {
        mPredictionOn = enabled;
    }

    /**
     * Update internal prediction values & candidate suggestions for Keyboard
     * language. predictions are just auto-completions so this method directly
     * updates candidate suggestions in the candidate view.
     */
    private void updatePredictions() {
        try {
            getCurrentInputConnection().setComposingText(mComposing, 1);
            String query = mComposing.toString().toLowerCase();
            List<String> suggestions = (List<String>) mDictionary.complete(query);
            for (int i = 0; i < suggestions.size(); i++) {
                suggestions.set(i, StringUtil_sindhiBest.normalizeWordCasePreserve(
                        mComposing.toString(), suggestions.get(i)));
            }
            setSuggestions(suggestions, null, true, true);
        } catch (Exception ignored) {
        }
    }


    public void setSuggestions(List<String> suggestions,
                               String[][] wordChoices, boolean completions, boolean typedWordValid) {
        System.out.println("completions>> " + suggestions);
        System.out.println("wordChoices>> " + wordChoices);
        System.out.println("completions>> " + completions);
        System.out.println("typedWordValid>> " + typedWordValid);
        if (suggestions != null && suggestions.size() > 0) {
            setCandidatesViewShown(true);
        } else if (isExtractViewShown()) {
            setCandidatesViewShown(true);
        }
        if (mCandidateView != null) {
            mCandidateView.setSuggestions(suggestions, wordChoices,
                    mComposing.toString(), completions, typedWordValid);
        }
    }

    private void handleBackspace() {
        final int length = mComposing.length();
        if (length > 1) {
            mComposing.delete(length - 1, length);
            updatePredictions();

        } else if (length > 0) {
            mComposing.setLength(0);
            // this clears internal prediction values so we can
            // call it regardless of the current language mode
            getCurrentInputConnection().commitText("", 0);
            updateCandidates();
        } else {
            keyDownUp(KeyEvent.KEYCODE_DEL);
        }
        updateShiftKeyState(getCurrentInputEditorInfo());
    }

    private void handleShift() {
        if (mInputView == null) {
            return;
        }

        LatinKeyboard_sindhiBest currentKbd = mInputView.getKeyboard();
        if (currentKbd == mEngKeyboard) {
            mInputView.setKeyboard(mEngKeyboardC);
            mCapsLock = false;
        } else if (currentKbd == enSymbolsKeyboard) {
            mInputView.setKeyboard(enSymbolsCKeyboard);
        } else if (currentKbd == enSymbolsCKeyboard) {
//
//			Intent intent = new Intent(SoftKeyboard_sindhiBest.this, MainActivity_sindhiBest.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(intent);
            mInputView.setKeyboard(smilykeyboard);
        } else if (currentKbd == smilykeyboard) {
            mInputView.setKeyboard(enSymbolsKeyboard);

        } else if (currentKbd == emojiKeyboard) {
            mInputView.setKeyboard(emojiKeyboard);
        } else if (currentKbd == assending_order) {
            mInputView.setKeyboard(assending_order);
        }
    }

    private void handleEngShift() {
        mInputView.setKeyboard(mEngKeyboardShift);
        mCapsLock = true;
    }

    private void handleChange() {
        if (mInputView == null)
            return;
        LatinKeyboard_sindhiBest currentKeyboard = mInputView.getKeyboard();
        if (currentKeyboard == mUrduKeyboard) {
            mInputView.setKeyboard(mUrduKeyboardC);
            urShift = true;
        } else if (currentKeyboard == mUrduKeyboardC) {
            mInputView.setKeyboard(mUrduKeyboard);
            urShift = false;
        } else if (currentKeyboard == urSymbolsKeyboard) {
            mInputView.setKeyboard(urSymbolsCKeyboard);
        } else if (currentKeyboard == urSymbolsCKeyboard) {
            //	mInputView.setKeyboard(urSymbolsKeyboard);
            mInputView.setKeyboard(smilykeyboard);
        } else if (currentKeyboard == smilykeyboard) {
            mInputView.setKeyboard(urSymbolsKeyboard);
        } else if (currentKeyboard == emojiKeyboard) {
            mInputView.setKeyboard(emojiKeyboard);
        } else if (currentKeyboard == assending_order) {
            mInputView.setKeyboard(mUrduKeyboardC);
            urShift = false;
        }


    }

    private void handleCharacter(int primaryCode, int[] keyCodes) {
        if (mVibrate) {
            vibrateOnChars((char) primaryCode);

        }
        if (mSound) {
            soundOnChars((char) primaryCode);
        }
        if (isInputViewShown()) {
            if (mInputView.isShifted()) {
                primaryCode = Character.toUpperCase(primaryCode);
            }
        }
        if (mPredictionOn) {
            if (isAlphabet(primaryCode)) {
                mComposing.append((char) primaryCode);
                updatePredictions();
            } else {
                if (mAutoCompletionOn) {
                    commitTyped(getCurrentInputConnection());
                    commitTextAsIs(primaryCode);
                } else {
                    InputConnection inputConnection = getCurrentInputConnection();
                    inputConnection.commitText(mComposing, 1);
                    commitTextAsIs(primaryCode);
                }

            }

        } else {
            commitTextAsIs(primaryCode);
        }
        updateShiftKeyState(getCurrentInputEditorInfo());
    }

    private void commitTextAsIs(int primaryCode) {
        try {
            getCurrentInputConnection().commitText(String.valueOf(Character.toChars(primaryCode)), 1);
        } catch (Exception ignored) {
        }

    }

    private void soundOnChars(char primaryCode) {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        float vol = (float) 0.5;
        am.playSoundEffect(AudioManager.FX_KEY_CLICK, vol);

    }

    private void vibrateOnChars(char primaryCode) {
        try {
            vibrator.vibrate(15);
        } catch (Exception ignored) {
        }


    }

    private void handleClose() {
        commitTyped(getCurrentInputConnection());
        requestHideSelf(0);
        mInputView.closing();
    }

    private void enableEnglish() {
        if (mInputView == null)
            return;
        LatinKeyboard_sindhiBest currentKeyboard = mInputView.getKeyboard();
        if (currentKeyboard == mUrduKeyboard
                || currentKeyboard == mUrduKeyboardC
                || currentKeyboard == urSymbolsKeyboard
                || currentKeyboard == urSymbolsCKeyboard
                || currentKeyboard == enSymbolsKeyboard
                || currentKeyboard == enSymbolsCKeyboard
                || currentKeyboard == smilykeyboard
                || currentKeyboard == emojiKeyboard
                || currentKeyboard == assending_order) ;

        {
            mInputView.setKeyboard(mEngKeyboard);
            setLanguageCode(0x1);
        }
    }

    private void enableUrdu() {
        if (mInputView == null)
            return;
        LatinKeyboard_sindhiBest currentKeyboard = mInputView.getKeyboard();
        if (currentKeyboard == mEngKeyboard || currentKeyboard == mEngKeyboardC
                || currentKeyboard == mEngKeyboardShift
                || currentKeyboard == enSymbolsKeyboard
                || currentKeyboard == enSymbolsCKeyboard
                || currentKeyboard == smilykeyboard
                || currentKeyboard == emojiKeyboard
                || currentKeyboard == assending_order) ;
        {
            mInputView.setKeyboard(mUrduKeyboard);
            setLanguageCode(0x0);
        }
    }

    private void handleModeChange() {
        if (mInputView == null)
            return;

        LatinKeyboard_sindhiBest current = mInputView.getKeyboard();
        if (current == urSymbolsKeyboard || current == urSymbolsCKeyboard)
            current = mUrduKeyboard;
        else
            current = urSymbolsKeyboard;

        if (current == urSymbolsKeyboard)
            current.setShifted(false);

        mInputView.setKeyboard(current);
    }

    private void handleEngSymbols() {
        if (mInputView == null)
            return;

        LatinKeyboard_sindhiBest currentKb = mInputView.getKeyboard();
//		if (currentKb == mEngKeyboard || currentKb == mEngKeyboardC
//				|| currentKb == mEngKeyboardShift) {
        mInputView.setKeyboard(enSymbolsKeyboard);
//		}

    }

    private void handleEmoji() {
        if (mInputView == null)
            return;

        LatinKeyboard_sindhiBest currentKb = mInputView.getKeyboard();
//		if (currentKb == mEngKeyboard || currentKb == mEngKeyboardC
//				|| currentKb == mEngKeyboardShift) {
        mInputView.setKeyboard(emojiKeyboard);
//		}

    }

    private void handleAssending() {
        if (mInputView == null)
            return;

        LatinKeyboard_sindhiBest currentKb = mInputView.getKeyboard();
//		if (currentKb == mEngKeyboard || currentKb == mEngKeyboardC
//				|| currentKb == mEngKeyboardShift) {
        mInputView.setKeyboard(assending_order);
//		}

    }


    private void showad() {
        Intent intent = new Intent(SoftKeyboard_sindhiBest.this, PreferenceActivity_voicetyping.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void handleSMEmoji() {
        if (mInputView == null)
            return;

        LatinKeyboard_sindhiBest currentKb = mInputView.getKeyboard();
//		if (currentKb == mEngKeyboard || currentKb == mEngKeyboardC
//				|| currentKb == mEngKeyboardShift) {
        mInputView.setKeyboard(smilykeyboard);
//		}

    }

    public void setLanguageCode(int code) {
        editor = mSharedPreferences.edit();
        editor.putInt("INPUT_LANGUAGE", code);

        editor.commit();
    }

    public boolean isWordSeparator(int code) {
        return mWordSeparators.contains(String.valueOf((char) code));
    }

    public boolean isSpecialSeparator(int code) {
        return mSpecialSeparators.contains(String.valueOf((char) code));
    }

    public void pickDefaultCandidate() {
        pickSuggestionManually(0);
    }

    public void pickSuggestionManually(int index) {
        if (mCompletionOn && mCompletions != null && index >= 0
                && index < mCompletions.length) {
            CompletionInfo ci = mCompletions[index];
            System.out.println("ci ->->->> " + ci);
            getCurrentInputConnection().commitCompletion(ci);
            if (mCandidateView != null) {
                mCandidateView.clear();
            }
            updateShiftKeyState(getCurrentInputEditorInfo());
        } else if (mComposing.length() > 0) {
            System.out.println("index " + index);
            mAutoSpace = "ADD_SPACE";
            commitTyped(getCurrentInputConnection(), index);
        } else if (mComposing.length() == 0) {
            System.out.println("index " + index);
            mAutoSpace = "ADD_SPACE";
            commitTyped(getCurrentInputConnection(), index);
        }
    }

    public void updateComposingTextFromUserCorrections(String text) {
        try {
            if (text != null) {
                getCurrentInputConnection().setComposingText(text, 1);
                mUserComposing = text;
            }
        } catch (Exception ignored) {
        }

    }

    public void swipeRight() {
        if (mCompletionOn) {
            pickDefaultCandidate();
        }
    }

    public void swipeLeft() {
        handleBackspace();
    }

    public void swipeDown() {
        handleClose();
    }

    public void swipeUp() {
    }

    public void onPress(int primaryCode) {

        if (primaryCode == -1 || primaryCode == -2 || primaryCode == -11
                || primaryCode == -12 || primaryCode == -14
                || primaryCode == -17 || primaryCode == -15
                || primaryCode == 10 || primaryCode == 32
                || primaryCode == -100 || primaryCode == -200
                || primaryCode == -16) {
            mInputView.setPreviewEnabled(false);

        } else {
            try {
                if (mPreview) {
                    mInputView.setPreviewEnabled(true);
                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onRelease(int primaryCode) {
        mInputView.setPreviewEnabled(false);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPrefs,
                                          String prefKey) {
        loadPreferences(sharedPrefs);
        loadPreviewPreferences(sharedPrefs);
        loadAutocompletionPreferences(sharedPrefs);
        loadPredictionPreferences(sharedPrefs);
    }

    private void loadPredictionPreferences(SharedPreferences sharedPrefs) {
        mPredictionOn = mSharedPreferences.getBoolean("prefPrediction", true);

    }

    private void loadAutocompletionPreferences(SharedPreferences sharedPrefs) {
        mAutoCompletionOn = mSharedPreferences.getBoolean("prefAutoComplate",
                false);

    }

    private void loadPreviewPreferences(SharedPreferences sharedPrefs) {
        mPreview = mSharedPreferences.getBoolean("prefKeyPreview", true);
    }

    private void loadPreferences(SharedPreferences sharedPrefs) {
        mVibrate = sharedPrefs.getBoolean("prefVibrate", true);
        mSound = mSharedPreferences.getBoolean("prefSound", true);
    }
}
