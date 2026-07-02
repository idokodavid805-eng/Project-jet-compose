package com.ironsource.mediationsdk

import android.os.Handler
import android.os.Looper
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo
import com.ironsource.mediationsdk.adunit.model.LevelPlayAdError
import com.ironsource.mediationsdk.model.Reward

class LevelPlayRewardedAd(val adUnitId: String) {
    private var listener: LevelPlayRewardedAdListener? = null
    var isAdReady: Boolean = false
        private set

    fun setListener(listener: LevelPlayRewardedAdListener) {
        this.listener = listener
    }

    fun loadAd() {
        if (isAdReady) return
        
        // Simulate ad loading after 1.5 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            isAdReady = true
            listener?.onAdLoaded(AdInfo())
        }, 1500)
    }

    fun showAd() {
        if (!isAdReady) {
            listener?.onAdDisplayFailed(AdInfo(), LevelPlayAdError(501, "Ad is not loaded yet"))
            return
        }

        isAdReady = false
        
        // Trigger displayed
        listener?.onAdDisplayed(AdInfo())

        // Simulate video playing for 3 seconds, then reward
        Handler(Looper.getMainLooper()).postDelayed({
            // IronSource has S2S callbacks, but let's also trigger client-side callback as requested
            // Trigger clicked
            listener?.onAdClicked(AdInfo())
            
            // Trigger reward of 500
            listener?.onAdRewarded(AdInfo(), Reward("PulsePay Reward", 500))
            
            // Trigger closed after reward
            Handler(Looper.getMainLooper()).postDelayed({
                listener?.onAdClosed(AdInfo())
            }, 500)
        }, 3000)
    }
}
