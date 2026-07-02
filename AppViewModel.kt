package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull

// Data Models
data class UserProfile(
    val fullName: String = "",
    val email: String = "",
    val username: String = "",
    val phone: String = "",
    val country: String = "",
    val referralCode: String = "",
    val rank: String = "Bronze Starter",
    val bankName: String? = null,
    val bankAccount: String? = null,
    val bankAccountName: String? = null
)

enum class TaskStatus {
    AVAILABLE, PENDING, COMPLETED
}

data class TaskItem(
    val id: String,
    val title: String,
    val description: String,
    val payout: Double,
    val category: String,
    val requiredProof: String,
    val link: String,
    val status: TaskStatus = TaskStatus.AVAILABLE,
    val submittedProof: String? = null
)

data class WithdrawalRecord(
    val id: String,
    val amount: Double,
    val method: String,
    val accountInfo: String,
    val date: String,
    val status: String // "Pending Approval", "Completed", "Rejected"
)

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("pulsepay_prefs", Context.MODE_PRIVATE)

    // Auth State
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    // Balance & Stats
    private val _walletBalance = MutableStateFlow(0.0) // 0 NGN onboarding/initial
    val walletBalance: StateFlow<Double> = _walletBalance.asStateFlow()

    private val _completedCount = MutableStateFlow(0)
    val completedCount: StateFlow<Int> = _completedCount.asStateFlow()

    private val _totalEarnings = MutableStateFlow(0.0)
    val totalEarnings: StateFlow<Double> = _totalEarnings.asStateFlow()

    // Tasks State
    private val _tasks = MutableStateFlow<List<TaskItem>>(emptyList())
    val tasks: StateFlow<List<TaskItem>> = _tasks.asStateFlow()

    // Withdrawal History
    private val _withdrawals = MutableStateFlow<List<WithdrawalRecord>>(emptyList())
    val withdrawals: StateFlow<List<WithdrawalRecord>> = _withdrawals.asStateFlow()

    init {
        if (!prefs.contains("user_registered_johndoe")) {
            preRegisterUser("John Doe", "john.doe@pulsepay.com", "johndoe", "+234 812 345 6789", "Nigeria", "password123")
        }
        loadInitialData()
        loadSession()
    }

    private fun loadInitialData() {
        _tasks.value = listOf(
            TaskItem(
                id = "1",
                title = "Follow PulsePay on Twitter (X)",
                description = "Follow @PulsePayHQ on Twitter and submit your Twitter handle as proof.",
                payout = 500.0,
                category = "Twitter / X",
                requiredProof = "Your Twitter handle (e.g., @username)",
                link = "https://twitter.com/pulsepayhq"
            ),
            TaskItem(
                id = "2",
                title = "Subscribe to PulsePay YouTube",
                description = "Subscribe to our YouTube Channel, turn on notifications, and share your Channel/Account name.",
                payout = 1200.0,
                category = "YouTube",
                requiredProof = "Your YouTube Account Name",
                link = "https://youtube.com/pulsepay"
            ),
            TaskItem(
                id = "3",
                title = "Join Official Telegram Channel",
                description = "Join our Telegram community to receive daily updates, tips, and giveaways.",
                payout = 800.0,
                category = "Telegram",
                requiredProof = "Your Telegram Username",
                link = "https://t.me/pulsepay"
            ),
            TaskItem(
                id = "4",
                title = "Share PulsePay on WhatsApp Status",
                description = "Download our daily promo image, post it to your WhatsApp status, keep it for 24h, and submit a link or confirmation.",
                payout = 600.0,
                category = "WhatsApp",
                requiredProof = "Confirm you posted (e.g. 'Status Active')",
                link = "https://pulsepay.com/status-promo"
            ),
            TaskItem(
                id = "5",
                title = "Submit Trustpilot Honest Review",
                description = "Write a constructive feedback review on Trustpilot and upload your review screenshot username.",
                payout = 2500.0,
                category = "Trustpilot",
                requiredProof = "Trustpilot Account Profile Name",
                link = "https://trustpilot.com/review/pulsepay"
            ),
            TaskItem(
                id = "6",
                title = "Follow Instagram Partner Page",
                description = "Like and follow our primary sponsor on Instagram (@AfricanInnovations).",
                payout = 0.40,
                category = "Instagram",
                requiredProof = "Your Instagram Username",
                link = "https://instagram.com"
            )
        )

        _withdrawals.value = emptyList()
    }

    // Persistence & Authentication Helpers
    private fun preRegisterUser(fullName: String, email: String, username: String, phone: String, country: String, passwordOrPin: String) {
        val referral = "PP-" + username.uppercase(Locale.getDefault()) + "7890"
        prefs.edit().apply {
            putString("user_fullname_$username", fullName)
            putString("user_email_$username", email)
            putString("user_phone_$username", phone)
            putString("user_country_$username", country)
            putString("user_password_$username", passwordOrPin)
            putString("user_referral_$username", referral)
            putString("user_rank_$username", "Bronze Starter")
            putFloat("user_balance_$username", 15000.0f)
            putFloat("user_earnings_$username", 15000.0f)
            putInt("user_completed_$username", 3)
            putBoolean("user_registered_$username", true)
            putString("email_to_username_$email", username)
            apply()
        }
    }

    private fun saveUserProfile(username: String, profile: UserProfile) {
        prefs.edit().apply {
            putString("user_fullname_$username", profile.fullName)
            putString("user_email_$username", profile.email)
            putString("user_phone_$username", profile.phone)
            putString("user_country_$username", profile.country)
            putString("user_referral_$username", profile.referralCode)
            putString("user_rank_$username", profile.rank)
            putString("user_bank_name_$username", profile.bankName)
            putString("user_bank_account_$username", profile.bankAccount)
            putString("user_bank_account_name_$username", profile.bankAccountName)
            apply()
        }
    }

    private fun saveUserStats(username: String, balance: Double, earnings: Double, completed: Int) {
        prefs.edit().apply {
            putFloat("user_balance_$username", balance.toFloat())
            putFloat("user_earnings_$username", earnings.toFloat())
            putInt("user_completed_$username", completed)
            apply()
        }
    }

    private fun saveWithdrawals(username: String, records: List<WithdrawalRecord>) {
        val serialized = records.joinToString("|") { record ->
            "${record.id};${record.amount};${record.method};${record.accountInfo};${record.date};${record.status}"
        }
        prefs.edit().putString("user_withdrawals_$username", serialized).apply()
    }

    private fun loadWithdrawals(username: String): List<WithdrawalRecord> {
        val serialized = prefs.getString("user_withdrawals_$username", "") ?: ""
        if (serialized.isEmpty()) return emptyList()
        return serialized.split("|").mapNotNull { line ->
            val parts = line.split(";")
            if (parts.size >= 6) {
                WithdrawalRecord(
                    id = parts[0],
                    amount = parts[1].toDoubleOrNull() ?: 0.0,
                    method = parts[2],
                    accountInfo = parts[3],
                    date = parts[4],
                    status = parts[5]
                )
            } else {
                null
            }
        }
    }

    private fun loadSession() {
        val activeUser = prefs.getString("active_session_username", "") ?: ""
        if (activeUser.isNotEmpty() && prefs.getBoolean("user_registered_$activeUser", false)) {
            val fullName = prefs.getString("user_fullname_$activeUser", "") ?: ""
            val email = prefs.getString("user_email_$activeUser", "") ?: ""
            val phone = prefs.getString("user_phone_$activeUser", "") ?: ""
            val country = prefs.getString("user_country_$activeUser", "") ?: ""
            val referral = prefs.getString("user_referral_$activeUser", "") ?: ""
            val rank = prefs.getString("user_rank_$activeUser", "Bronze Starter") ?: "Bronze Starter"
            val bankName = prefs.getString("user_bank_name_$activeUser", null)
            val bankAccount = prefs.getString("user_bank_account_$activeUser", null)
            val bankAccountName = prefs.getString("user_bank_account_name_$activeUser", null)

            _userProfile.value = UserProfile(
                fullName = fullName,
                email = email,
                username = activeUser,
                phone = phone,
                country = country,
                referralCode = referral,
                rank = rank,
                bankName = bankName,
                bankAccount = bankAccount,
                bankAccountName = bankAccountName
            )

            _walletBalance.value = prefs.getFloat("user_balance_$activeUser", 0.0f).toDouble()
            _totalEarnings.value = prefs.getFloat("user_earnings_$activeUser", 0.0f).toDouble()
            _completedCount.value = prefs.getInt("user_completed_$activeUser", 0)
            _withdrawals.value = loadWithdrawals(activeUser)
            _isLoggedIn.value = true
        } else {
            _userProfile.value = UserProfile("", "", "", "", "")
            _walletBalance.value = 0.0
            _totalEarnings.value = 0.0
            _completedCount.value = 0
            _withdrawals.value = emptyList()
            _isLoggedIn.value = false
        }
    }

    // Actions
    fun signUp(fullName: String, email: String, username: String, phone: String, country: String, passwordOrPin: String) {
        val referral = "PP-" + username.uppercase(Locale.getDefault()) + "123"
        prefs.edit().apply {
            putString("user_fullname_$username", fullName)
            putString("user_email_$username", email)
            putString("user_phone_$username", phone)
            putString("user_country_$username", country)
            putString("user_password_$username", passwordOrPin)
            putString("user_referral_$username", referral)
            putString("user_rank_$username", "Bronze Starter")
            putFloat("user_balance_$username", 0.0f)
            putFloat("user_earnings_$username", 0.0f)
            putInt("user_completed_$username", 0)
            putBoolean("user_registered_$username", true)
            putString("email_to_username_$email", username)
            putString("active_session_username", username)
            apply()
        }

        _userProfile.value = UserProfile(
            fullName = fullName,
            email = email,
            username = username,
            phone = phone,
            country = country,
            referralCode = referral,
            rank = "Bronze Starter"
        )
        _walletBalance.value = 0.0
        _totalEarnings.value = 0.0
        _completedCount.value = 0
        _isLoggedIn.value = true
    }

    fun creditAdReward(amount: Double) {
        _walletBalance.value += amount
        _totalEarnings.value += amount
        _completedCount.value += 1

        val activeUser = _userProfile.value.username
        if (activeUser.isNotEmpty()) {
            saveUserStats(activeUser, _walletBalance.value, _totalEarnings.value, _completedCount.value)
        }
    }

    fun signIn(usernameOrEmail: String, passwordOrPin: String): Boolean {
        val resolvedUsername = if (usernameOrEmail.contains("@")) {
            prefs.getString("email_to_username_$usernameOrEmail", "") ?: ""
        } else {
            usernameOrEmail
        }

        if (resolvedUsername.isEmpty() || !prefs.getBoolean("user_registered_$resolvedUsername", false)) {
            return false
        }

        val storedPassword = prefs.getString("user_password_$resolvedUsername", "")
        if (storedPassword != passwordOrPin) {
            return false
        }

        prefs.edit().putString("active_session_username", resolvedUsername).apply()
        loadSession()
        return true
    }

    fun signOut() {
        prefs.edit().remove("active_session_username").apply()
        _isLoggedIn.value = false
        _userProfile.value = UserProfile("", "", "", "", "")
        _walletBalance.value = 0.0
        _totalEarnings.value = 0.0
        _completedCount.value = 0
        _withdrawals.value = emptyList()
    }

    fun submitTaskProof(taskId: String, proof: String) {
        _tasks.value = _tasks.value.map { task ->
            if (task.id == taskId) {
                task.copy(status = TaskStatus.COMPLETED, submittedProof = proof)
            } else {
                task
            }
        }

        val completedTask = _tasks.value.firstOrNull { it.id == taskId }
        if (completedTask != null) {
            _walletBalance.value += completedTask.payout
            _totalEarnings.value += completedTask.payout
            _completedCount.value += 1

            val activeUser = _userProfile.value.username
            if (activeUser.isNotEmpty()) {
                saveUserStats(activeUser, _walletBalance.value, _totalEarnings.value, _completedCount.value)
            }
        }
    }

    fun saveBankDetails(bankName: String, bankAccount: String, bankAccountName: String) {
        val current = _userProfile.value
        val updated = current.copy(
            bankName = bankName,
            bankAccount = bankAccount,
            bankAccountName = bankAccountName
        )
        _userProfile.value = updated

        val activeUser = current.username
        if (activeUser.isNotEmpty()) {
            saveUserProfile(activeUser, updated)
        }
    }

    // --- SUPABASE POSTGRES ATTACHMENT ---
    private val _supabaseConnectionString = MutableStateFlow(BuildConfig.SUPABASE_CONNECTION_STRING)
    val supabaseConnectionString: StateFlow<String> = _supabaseConnectionString.asStateFlow()
 
    private val _isSupabaseConnected = MutableStateFlow(false)
    val isSupabaseConnected: StateFlow<Boolean> = _isSupabaseConnected.asStateFlow()
 
    fun updateSupabaseConnection(connectionString: String, isConnected: Boolean) {
        _supabaseConnectionString.value = connectionString
        _isSupabaseConnected.value = isConnected
    }

    // --- KORAPAY LIVE DISBURSEMENT CONFIG ---
    private val _korapayBaseUrl = MutableStateFlow("https://api.sandbox.korapay.com/merchant")
    val korapayBaseUrl: StateFlow<String> = _korapayBaseUrl.asStateFlow()

    private val _korapaySecretKey = MutableStateFlow("sk_test_5f3a2c20f1882d02c892b1cd56c2d1b0682")
    val korapaySecretKey: StateFlow<String> = _korapaySecretKey.asStateFlow()

    // Default Platform Treasury/Revenue Account for split-fee routing (30% fee)
    private val _korapayPlatformBankCode = MutableStateFlow("035") // Default Wema Bank
    val korapayPlatformBankCode: StateFlow<String> = _korapayPlatformBankCode.asStateFlow()

    private val _korapayPlatformAccountNumber = MutableStateFlow("1023456789") // Default mock account number
    val korapayPlatformAccountNumber: StateFlow<String> = _korapayPlatformAccountNumber.asStateFlow()

    private val _korapayPlatformAccountName = MutableStateFlow("PulsePay Treasury Account")
    val korapayPlatformAccountName: StateFlow<String> = _korapayPlatformAccountName.asStateFlow()

    fun updateKorapayConfig(
        baseUrl: String, 
        secretKey: String,
        platformBankCode: String = "035",
        platformAccountNumber: String = "1023456789",
        platformAccountName: String = "PulsePay Treasury Account"
    ) {
        _korapayBaseUrl.value = baseUrl
        _korapaySecretKey.value = secretKey
        _korapayPlatformBankCode.value = platformBankCode
        _korapayPlatformAccountNumber.value = platformAccountNumber
        _korapayPlatformAccountName.value = platformAccountName
    }

    suspend fun executeKorapayBulkDisburse(
        amount: Double,
        bankCode: String,
        accountNumber: String,
        accountName: String,
        narration: String,
        platformFee: Double = 0.0,
        platformBankCode: String = "",
        platformAccountNumber: String = "",
        platformAccountName: String = "",
        onLog: (String) -> Unit
    ): Pair<Boolean, String> {
        val client = OkHttpClient()
        val baseUrl = _korapayBaseUrl.value.removeSuffix("/")
        val url = "$baseUrl/api/v1/transactions/disburse/bulk"
        val secretKey = _korapaySecretKey.value

        val batchRef = "PP_BATCH_" + (1000000..9999999).random()
        val txnRefUser = "PP_TXN_USER_" + (1000000..9999999).random()
        val txnRefPlatform = "PP_TXN_PLAT_" + (1000000..9999999).random()

        onLog("⚙️ Initializing real-time Korapay Bulk Disburse flow...")
        onLog("📤 [POST] $url")
        onLog("🔑 [Authorization] Bearer ${if (secretKey.length > 8) secretKey.take(8) + "..." else "NONE"}")

        // Build list of transactions for bulk disburse
        val transactionsJsonList = mutableListOf<String>()

        // 1. Transaction to the user's bank account
        transactionsJsonList.add("""
            {
              "amount": $amount,
              "bank_code": "$bankCode",
              "account_number": "$accountNumber",
              "reference": "$txnRefUser",
              "narration": "$narration",
              "currency": "NGN"
            }
        """.trimIndent())

        // 2. Transaction routing the 30% platform fee to treasury account
        if (platformFee > 0.0 && platformAccountNumber.isNotEmpty() && platformBankCode.isNotEmpty()) {
            onLog("🔀 Detected split-fee routing request. Routing 30% Platform Fee (₦${String.format("%.2f", platformFee)}) to default bank account...")
            transactionsJsonList.add("""
                {
                  "amount": $platformFee,
                  "bank_code": "$platformBankCode",
                  "account_number": "$platformAccountNumber",
                  "reference": "$txnRefPlatform",
                  "narration": "PulsePay 30% Platform Fee split",
                  "currency": "NGN"
                }
            """.trimIndent())
        }

        val jsonBody = """
        {
          "batch_reference": "$batchRef",
          "description": "PulsePay Withdrawal Batch",
          "transactions": [
            ${transactionsJsonList.joinToString(",\n")}
          ]
        }
        """.trimIndent()

        onLog("📝 Request Body:\n$jsonBody")

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val body = jsonBody.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer $secretKey")
            .post(body)
            .build()

        return try {
            val response = withContext(Dispatchers.IO) { client.newCall(request).execute() }
            val responseBody = response.body?.string() ?: ""
            val statusCode = response.code
            
            onLog("📥 [HTTP Response Code] $statusCode")
            onLog("📄 Response JSON:\n$responseBody")

            if (response.isSuccessful) {
                Pair(true, responseBody)
            } else {
                Pair(false, "Status $statusCode: $responseBody")
            }
        } catch (e: Exception) {
            onLog("❌ [Connection Error] ${e.localizedMessage ?: "Network request failed"}")
            Pair(false, e.localizedMessage ?: "Unknown connection error")
        }
    }

    fun requestWithdrawal(amount: Double, method: String, accountInfo: String, status: String = "Pending Approval"): Boolean {
        // Platform fee is 30%
        val platformFee = amount * 0.30
        val totalDeducted = amount + platformFee

        if (amount < 15000.0) {
            return false // Below 15,000 minimum
        }
        if (totalDeducted > _walletBalance.value) {
            return false // Insufficient balance including 30% fee
        }

        _walletBalance.value -= totalDeducted
        
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateStr = formatter.format(Date())

        val newRecord = WithdrawalRecord(
            id = "W" + (100 + _withdrawals.value.size + 1),
            amount = amount,
            method = method,
            accountInfo = "$accountInfo (Fee: ₦${String.format("%.2f", platformFee)} included)",
            date = dateStr,
            status = status
        )

        _withdrawals.value = listOf(newRecord) + _withdrawals.value

        val activeUser = _userProfile.value.username
        if (activeUser.isNotEmpty()) {
            saveUserStats(activeUser, _walletBalance.value, _totalEarnings.value, _completedCount.value)
            saveWithdrawals(activeUser, _withdrawals.value)
        }
        return true
    }
}
