package com.my.newvoicetyping.keyboard;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my.newvoicetyping.R;

public class BannerPreference extends Preference {

    public BannerPreference(Context context) {
        super(context, null);
    }

    public BannerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.banner_preference, null, true);

//        if (!PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(AdsHandling.PURCHASE, false)){
//           // AdsHandling.getInstance().loadAppLovinBanner((Activity) getContext(), v.findViewById(R.id.ads_layout));
////            NewNativeAdClass.INSTANCE.adNativeAd((Activity) getContext(), false, v.findViewById(R.id.nativeAdContainerAdContainer), false, new nativeOnFailed() {
////                @Override
////                public void nativeAdOnFailed() {
////                    v.findViewById(R.id.nativeAdContainerAdContainer).setVisibility(View.GONE);
////                }
////            });
//
//        }
        return v;
    }
}