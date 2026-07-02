package com.ironsource.mediationsdk.model

class Reward(private val name: String = "PulsePay Coins", private val amount: Int = 500) {
    fun getRewardName(): String = name
    fun getAmount(): Int = amount
}
