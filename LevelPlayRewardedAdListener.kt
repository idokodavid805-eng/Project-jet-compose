package com.ironsource.mediationsdk

import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo
import com.ironsource.mediationsdk.adunit.model.LevelPlayAdError
import com.ironsource.mediationsdk.model.Reward

interface LevelPlayRewardedAdListener {
    fun onAdLoaded(adInfo: AdInfo)
    fun onAdLoadFailed(adUnitId: String, error: LevelPlayAdError)
    fun onAdDisplayed(adInfo: AdInfo)
    fun onAdDisplayFailed(adInfo: AdInfo, error: LevelPlayAdError)
    fun onAdClicked(adInfo: AdInfo)
    fun onAdRewarded(adInfo: AdInfo, reward: Reward)
    fun onAdClosed(adInfo: AdInfo)
}
