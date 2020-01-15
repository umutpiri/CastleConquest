package com.piri.umut;

/**
 * Created by umut on 3/19/18.
 */

public interface AdHandler {
    public void showAds(boolean show);

    public void showRewardedVideo();

    public boolean isRewardEarned();

    public boolean isClosed();

    public boolean isFailed();

    public void signIn();

    public void signOut();

    public void rateGame();

    public void unlockAchievement(String str);

    public void submitScore(int highScore);

    public void showAchievement();

    public void showScore();

    public boolean isSignedIn();
}
