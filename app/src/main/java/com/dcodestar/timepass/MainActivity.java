package com.dcodestar.timepass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.security.PrivateKey;

public class MainActivity extends AppCompatActivity {

    private RewardedAd rewardedAd;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        rewardedAd = createAndLoadRewardedAd();
    }

    public void showRewardedAd(View view) {
        if (!rewardedAd.isLoaded()) {
            Log.d(TAG, "showRewardedAd: ad still loading");
        } else {
            RewardedAdCallback rewardedAdCallback = new RewardedAdCallback() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    Log.d(TAG, "congratulations, you earned " + rewardItem.toString() + " & " + rewardItem.getAmount());
                }

                @Override
                public void onRewardedAdClosed() {
                    rewardedAd=createAndLoadRewardedAd();
                }
            };
            rewardedAd.show(this, rewardedAdCallback);
        }
    }
    public RewardedAd createAndLoadRewardedAd() {
        RewardedAd rewardedAd = new RewardedAd(this,
                getResources().getString(R.string.apikey));
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
                Log.d(TAG, "onRewardedAdLoaded: ad loaded successfully");
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
                Log.d(TAG, "onRewardedAdFailedToLoad: ad failed to load with error code " + errorCode );
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }
}
