package com.ironsource.mediationsdk

import android.app.Activity

object IronSource {
    enum class AD_UNIT {
        REWARDED_VIDEO,
        INTERSTITIAL,
        BANNER
    }

    private var appKey: String? = null
    private var isInitialized: Boolean = false
    private var userId: String? = null

    fun init(activity: Activity, appKey: String, vararg adUnits: AD_UNIT) {
        this.appKey = appKey
        this.isInitialized = true
        android.util.Log.d("IronSourceMock", "IronSource initialized with appKey: $appKey and ad units: ${adUnits.joinToString()}")
    }

    fun setUserId(userId: String) {
        this.userId = userId
        android.util.Log.d("IronSourceMock", "UserId set to: $userId")
    }

    fun isRewardedVideoPlacementCapped(placementName: String): Boolean {
        // Return false to simulate that it's NOT capped (so ads can always play in the demo!)
        android.util.Log.d("IronSourceMock", "isRewardedVideoPlacementCapped called for placement: $placementName")
        return false
    }

    fun onResume(activity: Activity) {
        android.util.Log.d("IronSourceMock", "onResume notified for activity: ${activity.localClassName}")
    }

    fun onPause(activity: Activity) {
        android.util.Log.d("IronSourceMock", "onPause notified for activity: ${activity.localClassName}")
    }
}
