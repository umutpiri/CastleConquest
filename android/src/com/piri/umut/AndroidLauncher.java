package com.piri.umut;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;

public class AndroidLauncher extends AndroidApplication implements AdHandler, RewardedVideoAdListener {
	private static final String TAG = "AndroidLauncher";
	private final static int requestCode = 1;
	protected AdView adView;
	private GameHelper gameHelper;
	private final int SHOW_ADS = 1;
	private final int HIDE_ADS = 0;
	private RewardedVideoAd rewardedVideoAd;
	private boolean rewarded;
	private boolean closed;
	private boolean isFailed;


	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SHOW_ADS:
					adView.setVisibility(View.VISIBLE);
					break;
				case HIDE_ADS:
					adView.setVisibility(View.GONE);
					break;
			}
		}
	};
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useGyroscope = true;
		rewarded = false;
		gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		gameHelper.enableDebugLog(false);

		GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener() {
			@Override
			public void onSignInFailed() {
			}

			@Override
			public void onSignInSucceeded() {
			}
		};

		RelativeLayout layout = new RelativeLayout(this);
		View gameView = initializeForView(new MyWorld(this), config);
		layout.addView(gameView);
		rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
		rewardedVideoAd.setRewardedVideoAdListener(this);

		adView = new AdView(this);
		adView.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				int visibility = adView.getVisibility();
				adView.setVisibility(View.GONE);
				adView.setVisibility(visibility);
				Log.i(TAG, "Ad Loaded...");
			}
		});
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId("ca-app-pub-8980970186700232/5371100933");

		AdRequest.Builder builder = new AdRequest.Builder();
		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT
		);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		layout.addView(adView, adParams);
		adView.loadAd(builder.build());

		setContentView(layout);
		loadRewardedVideoAd();
		gameHelper.setup(gameHelperListener);
	}

	@Override
	public void showAds(boolean show) {
		handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
	}

	private void loadRewardedVideoAd() {
		rewardedVideoAd.loadAd("ca-app-pub-8980970186700232/1224752615", new AdRequest.Builder().build());
	}

	@Override
	public void showRewardedVideo() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (rewardedVideoAd.isLoaded()) {
					rewardedVideoAd.show();
					isFailed = false;
				}
				loadRewardedVideoAd();
			}
		});
	}

	@Override
	public boolean isRewardEarned() {
		boolean temp = rewarded;
		rewarded = false;
		return temp;
	}

	@Override
	public boolean isClosed() {
		boolean temp = closed;
		closed = false;
		return temp;
	}

	@Override
	public boolean isFailed() {
		return isFailed;
	}

	@Override
	protected void onStart() {
		super.onStart();
		gameHelper.onStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		gameHelper.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		gameHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void signIn() {
		try {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					gameHelper.beginUserInitiatedSignIn();
				}
			});
		} catch (Exception e) {
			//Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void signOut() {
		try {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					gameHelper.signOut();
				}
			});
		} catch (Exception e) {
			//Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void rateGame() {
		String str = "Your PlayStore Link";
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
	}

	@Override
	public void unlockAchievement(String str) {
		Games.Achievements.unlock(gameHelper.getApiClient(), str);
	}

	@Override
	public void submitScore(int highScore) {
		if (isSignedIn()) {
			Games.Leaderboards.submitScore(gameHelper.getApiClient(), "CgkIxLe0nq0MEAIQAA", highScore);
		}
	}

	@Override
	public void showAchievement() {
		if (isSignedIn()) {
			startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), requestCode);
		} else {
			signIn();
		}
	}

	@Override
	public void showScore() {
		if (isSignedIn()) {
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
					"CgkIxLe0nq0MEAIQAA"), requestCode);
		} else {
			signIn();
		}
	}

	@Override
	public boolean isSignedIn() {
		return gameHelper.isSignedIn();
	}

	@Override
	public void onRewardedVideoAdLoaded() {
		isFailed = false;
	}

	@Override
	public void onRewardedVideoAdOpened() {
		isFailed = false;
	}

	@Override
	public void onRewardedVideoStarted() {
	}

	@Override
	public void onRewardedVideoAdClosed() {
		closed = true;
		loadRewardedVideoAd();
	}

	@Override
	public void onRewarded(RewardItem rewardItem) {
		rewarded = true;
	}

	@Override
	public void onRewardedVideoAdLeftApplication() {
		closed = true;
	}

	@Override
	public void onRewardedVideoAdFailedToLoad(int i) {
		isFailed = true;
	}
}
