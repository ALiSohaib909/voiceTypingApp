package com.my.newvoicetyping.keyboard;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.inputmethodservice.Keyboard;
import android.view.inputmethod.EditorInfo;

import com.my.newvoicetyping.R;

//import com.pocketclub.pakflagkeyboard.R;

public class LatinKeyboard_sindhiBest extends Keyboard {

	static final int KEYCODE_ENGLISH_SHIFT = -11;
	static final int KEYCODE_ENGLISH_BACK = -12;
	static final int KEYCODE_ENGLISH = -15;
	static final int KEYCODE_URDU = -14;
	static final int KEYCODE_SETTING = -16;
	static final int KEYCODE_THEMES = -17;
	static final int KEYCODE_CHANGE = -100;
	static final int KEYCODE_ENGLISH_SYMBOLS = -200;

	private Key mEnterKey;

	public LatinKeyboard_sindhiBest(Context context, int xmlLayoutResId) {
		super(context, xmlLayoutResId);
	}

	@Override
	protected Key createKeyFromXml(Resources res, Row parent, int x, int y,
			XmlResourceParser parser) {
		Key key = new LatinKey(res, parent, x, y, parser);
		if (key.codes[0] == 10)
			mEnterKey = key;
		return key;
	}



	void setImeOptions(Resources res, int options) {
		if (mEnterKey == null)
			return;

		switch (options
				& (EditorInfo.IME_MASK_ACTION | EditorInfo.IME_FLAG_NO_ENTER_ACTION)) {
		case EditorInfo.IME_ACTION_GO:
			mEnterKey.iconPreview = null;
			mEnterKey.icon = null;
			mEnterKey.label = res.getText(R.string.label_go_key);
			break;
		case EditorInfo.IME_ACTION_NEXT:
			mEnterKey.iconPreview = null;
			mEnterKey.icon = null;
			mEnterKey.label = res.getText(R.string.label_next_key);
			break;
		case EditorInfo.IME_ACTION_SEARCH:
			mEnterKey.icon = res.getDrawable(R.drawable.sym_keyboard_search_holo_dark);
			mEnterKey.label = null;
			break;
		case EditorInfo.IME_ACTION_SEND:
			mEnterKey.iconPreview = null;
			mEnterKey.icon = null;
			mEnterKey.label = res.getText(R.string.label_send_key);
			break;
		default:
			mEnterKey.icon = res.getDrawable(R.drawable.next);
			mEnterKey.label = null;
			break;
		}
	}

	static class LatinKey extends Key {
		public LatinKey(Resources res, Row parent, int x, int y,
				XmlResourceParser parser) {
			super(res, parent, x, y, parser);
		}

		@Override
		public boolean isInside(int x, int y) {
			return super.isInside(x, codes[0] == KEYCODE_CANCEL ? y - 10 : y);
		}
	}

}