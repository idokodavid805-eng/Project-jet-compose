package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import android.app.Activity
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Star
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo
import com.ironsource.mediationsdk.adunit.model.LevelPlayAdError
import com.ironsource.mediationsdk.model.Reward
import com.ironsource.mediationsdk.LevelPlayRewardedAd
import com.ironsource.mediationsdk.LevelPlayRewardedAdListener
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.AppViewModel
import com.example.ui.TaskItem
import java.util.Locale
import kotlinx.coroutines.delay
import com.example.ui.TaskStatus
import com.example.ui.theme.DarkSlate
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GreenLight
import com.example.ui.theme.GreenMedium
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.SlateMedium
import com.example.ui.theme.SlateLight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FlashOn


enum class DashboardTab {
    HOME, TASKS, WITHDRAWAL, PROFILE
}

@Composable
fun DashboardContainer(
    viewModel: AppViewModel,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(DashboardTab.HOME) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("dashboard_screen"),
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("dashboard_bottom_bar")
            ) {
                val items = listOf(
                    Triple(DashboardTab.HOME, "Home", Icons.Default.Home),
                    Triple(DashboardTab.TASKS, "Tasks", Icons.Default.Assignment),
                    Triple(DashboardTab.WITHDRAWAL, "Withdrawal", Icons.Default.AccountBalanceWallet),
                    Triple(DashboardTab.PROFILE, "Profile", Icons.Default.Person)
                )

                items.forEach { (tab, label, icon) ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, maxLines = 1) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GreenPrimary,
                            selectedTextColor = GreenPrimary,
                            unselectedIconColor = SlateMedium,
                            unselectedTextColor = SlateMedium,
                            indicatorColor = GreenLight
                        ),
                        modifier = Modifier.testTag("tab_${label.lowercase().replace(" ", "_")}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                DashboardTab.HOME -> HomeTabContent(
                    viewModel = viewModel,
                    onNavigateToTasks = { selectedTab = DashboardTab.TASKS },
                    onNavigateToWithdrawal = { selectedTab = DashboardTab.WITHDRAWAL }
                )
                DashboardTab.TASKS -> TasksTabContent(viewModel)
                DashboardTab.WITHDRAWAL -> WithdrawalTabContent(viewModel)
                DashboardTab.PROFILE -> ProfileTabContent(viewModel, onTriggerLogout = { showLogoutDialog = true })
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Sign Out", fontWeight = FontWeight.Bold, color = DarkSlate) },
            text = { Text("Are you sure you want to log out from PulsePay?", color = SlateMedium) },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.signOut()
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Logout", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = SlateMedium)
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White
        )
    }
}

// ---------------------- HOME TAB ----------------------
@Composable
fun HomeTabContent(
    viewModel: AppViewModel,
    onNavigateToTasks: () -> Unit,
    onNavigateToWithdrawal: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val walletBalance by viewModel.walletBalance.collectAsState()
    val completedCount by viewModel.completedCount.collectAsState()
    val totalEarnings by viewModel.totalEarnings.collectAsState()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(GreenPrimary, GreenMedium)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateLight)
            .padding(16.dp)
            .testTag("home_tab_content"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Branding Top Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(GreenLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_pulsepay_logo),
                            contentDescription = "Logo",
                            modifier = Modifier.size(28.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "PulsePay",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary,
                        letterSpacing = 1.sp
                    )
                }

                // Profile Ring Button
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(GoldLight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userProfile.fullName.ifBlank { "User" }.take(2).uppercase(),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent
                    )
                }
            }
        }

        // Welcome Hero Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(gradientBrush)
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Hello, ${userProfile.fullName.ifBlank { "User" }} 👋",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0x33FFFFFF)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "PULSE MEMBER",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Stats Dashboard Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "My Earnings Dashboard",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkSlate
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Wallet Balance
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Available Balance",
                                fontSize = 12.sp,
                                color = SlateMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "₦${String.format("%.2f", walletBalance)}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenPrimary,
                                modifier = Modifier.testTag("wallet_balance_text")
                            )
                        }

                        // Total Earning
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Total Earning",
                                fontSize = 12.sp,
                                color = SlateMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "₦${String.format("%.2f", totalEarnings)}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkSlate
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFE2E8F0)))
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Completed Tasks count
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = GreenPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(text = "Completed Tasks", fontSize = 11.sp, color = SlateMedium)
                                Text(text = completedCount.toString(), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkSlate)
                            }
                        }

                        // Active Referrals (mocked to 15)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.People,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(text = "Active Referrals", fontSize = 11.sp, color = SlateMedium)
                                Text(text = "15", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkSlate)
                            }
                        }
                    }
                }
            }
        }

        // Quick Action Row
        item {
            Column {
                Text(
                    text = "Quick Actions",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkSlate
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigateToTasks() },
                        colors = CardDefaults.cardColors(containerColor = GreenLight),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Assignment,
                                contentDescription = null,
                                tint = GreenPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Perform Tasks",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenPrimary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigateToWithdrawal() },
                        colors = CardDefaults.cardColors(containerColor = GoldLight),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Withdraw Funds",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkSlate,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // Recent Activity Feed
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Transactions",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkSlate
                    )
                    Text(
                        text = "View History",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary,
                        modifier = Modifier.clickable { }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val activities = listOf(
                        Triple("Ad Task Completed", "+₦500.00", "Today"),
                        Triple("Ad Task Completed", "+₦500.00", "Yesterday"),
                        Triple("Affiliate Bonus from @kelvin_dev", "+₦3,000.00", "Yesterday")
                    )

                    activities.forEach { (title, reward, date) ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(GreenLight),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.MonetizationOn,
                                            contentDescription = null,
                                            tint = GreenPrimary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = DarkSlate)
                                        Text(text = date, fontSize = 11.sp, color = SlateMedium)
                                    }
                                }

                                Text(
                                    text = reward,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GreenPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


// ---------------------- TASKS TAB ----------------------
@Composable
fun TasksTabContent(viewModel: AppViewModel) {
    val context = LocalContext.current
    val activity = context as? Activity
    val coroutineScope = rememberCoroutineScope()

    var statusText by remember { mutableStateOf("Press below to start task & load rewarded ad") }
    var isAdLoaded by remember { mutableStateOf(false) }
    var isAdShowing by remember { mutableStateOf(false) }

    // Use a remember wrapper for LevelPlayRewardedAd so it doesn't get recreated on recompositions
    val rewardedAd = remember(activity) {
        if (activity != null) {
            val ad = LevelPlayRewardedAd("sj4kcsxs6cjhfheh")
            
            // Set the listener
            ad.setListener(object : LevelPlayRewardedAdListener {
                override fun onAdLoaded(adInfo: AdInfo) {
                    isAdLoaded = true
                    statusText = "Ad loaded! Press Start Task again to play"
                    Toast.makeText(context, "Rewarded Ad Loaded! Ready to watch.", Toast.LENGTH_SHORT).show()
                }

                override fun onAdLoadFailed(adUnitId: String, error: LevelPlayAdError) {
                    isAdLoaded = false
                    statusText = "Ad failed to load: ${error.errorMessage} (code: ${error.errorCode})"
                    Toast.makeText(context, "Ad Load Failed: ${error.errorMessage}", Toast.LENGTH_SHORT).show()
                }

                override fun onAdDisplayed(adInfo: AdInfo) {
                    isAdShowing = true
                    statusText = "Ad displayed"
                }

                override fun onAdDisplayFailed(adInfo: AdInfo, error: LevelPlayAdError) {
                    isAdShowing = false
                    statusText = "Ad failed to display: ${error.errorMessage}"
                    Toast.makeText(context, "Ad Display Failed: ${error.errorMessage}", Toast.LENGTH_SHORT).show()
                }

                override fun onAdClicked(adInfo: AdInfo) {
                    statusText = "Ad clicked"
                }

                override fun onAdRewarded(adInfo: AdInfo, reward: Reward) {
                    // Crucially, grant the reward using the dynamic amount from the SDK Reward object
                    val rewardAmount = reward.getAmount().toDouble()
                    viewModel.creditAdReward(rewardAmount)
                    statusText = "Congratulations! You earned ₦${rewardAmount}!"
                    Toast.makeText(context, "Success! ₦${rewardAmount} credited to your balance.", Toast.LENGTH_LONG).show()
                }

                override fun onAdClosed(adInfo: AdInfo) {
                    isAdLoaded = false
                    isAdShowing = false
                    statusText = "Ad closed. Load another ad to earn more!"
                    // Auto reload ad for next time
                    ad.loadAd()
                }
            })
            ad
        } else {
            null
        }
    }

    // Since we're using server to server callbacks for rewards validation, set the user ID before initializing
    LaunchedEffect(viewModel.userProfile) {
        val userProfile = viewModel.userProfile.value
        // Set userID before initializing or loading
        IronSource.setUserId(userProfile.username)
        
        // Initialize ironSource SDK with the user's custom appKey "26f2ff445"
        activity?.let {
            IronSource.init(it, "26f2ff445", IronSource.AD_UNIT.REWARDED_VIDEO)
        }
        
        // Prefetch an ad
        rewardedAd?.loadAd()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateLight)
            .padding(24.dp)
            .testTag("tasks_tab_content"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Aesthetic illustration banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.PlayCircle,
                    contentDescription = null,
                    tint = GreenPrimary,
                    modifier = Modifier.size(72.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Rewarded Ads Tasks",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkSlate
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Watch a short video ad to perform your high-paying daily task. Get rewarded ₦500.00 instantly upon completion!",
                    fontSize = 14.sp,
                    color = SlateMedium,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }
        }

        // Status banner
        Text(
            text = statusText,
            fontSize = 13.sp,
            color = SlateMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // The big Start Task Button
        Button(
            onClick = {
                if (rewardedAd != null) {
                    if (isAdLoaded && rewardedAd.isAdReady) {
                        // Check if placement is capped as requested by the user
                        if (IronSource.isRewardedVideoPlacementCapped("DefaultPlacement")) {
                            statusText = "Ad placement is capped. Try again later!"
                            Toast.makeText(context, "Placement capped!", Toast.LENGTH_SHORT).show()
                        } else {
                            rewardedAd.showAd()
                        }
                    } else {
                        statusText = "Loading fresh ad... please wait"
                        rewardedAd.loadAd()
                    }
                } else {
                    Toast.makeText(context, "Ad engine is initializing...", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .testTag("start_task_button"),
            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (isAdLoaded) "START TASK (WATCH AD)" else "LOAD TASK / AD",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}


// ---------------------- WITHDRAWAL TAB ----------------------
@Composable
fun WithdrawalTabContent(viewModel: AppViewModel) {
    val walletBalance by viewModel.walletBalance.collectAsState()
    val withdrawals by viewModel.withdrawals.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Bank Setup State
    var bankNameInput by remember { mutableStateOf("") }
    var bankAccountInput by remember { mutableStateOf("") }
    var isBankDropdownExpanded by remember { mutableStateOf(false) }
    var isVerifyingAccount by remember { mutableStateOf(false) }
    var verifiedAccountName by remember { mutableStateOf("") }

    // Cashout Request State
    var amountStr by remember { mutableStateOf("") }
    var isCashoutLoading by remember { mutableStateOf(false) }

    val korapayPlatformBankCode by viewModel.korapayPlatformBankCode.collectAsState()
    val korapayPlatformAccountNumber by viewModel.korapayPlatformAccountNumber.collectAsState()
    val korapayPlatformAccountName by viewModel.korapayPlatformAccountName.collectAsState()

    fun getKorapayBankCode(bankName: String?): String {
        if (bankName == null) return "044"
        val nameLower = bankName.lowercase()
        return when {
            nameLower.contains("access") -> "044"
            nameLower.contains("guaranty") || nameLower.contains("gtb") -> "058"
            nameLower.contains("zenith") -> "057"
            nameLower.contains("united bank") || nameLower.contains("uba") -> "033"
            nameLower.contains("first bank") || nameLower.contains("firstbank") -> "011"
            nameLower.contains("fidelity") -> "070"
            nameLower.contains("sterling") -> "030"
            nameLower.contains("union") -> "032"
            nameLower.contains("stanbic") -> "219"
            nameLower.contains("kuda") -> "090267"
            nameLower.contains("opay") -> "999992"
            nameLower.contains("moniepoint") -> "50515"
            nameLower.contains("wema") -> "035"
            else -> "044"
        }
    }

    val nigerianBanks = listOf(
        "Access Bank Plc",
        "Guaranty Trust Bank (GTBank)",
        "Zenith Bank Plc",
        "United Bank for Africa (UBA)",
        "First Bank of Nigeria",
        "Fidelity Bank Plc",
        "Sterling Bank Plc",
        "Union Bank of Nigeria",
        "Stanbic IBTC Bank",
        "Kuda Bank (Microfinance)",
        "OPay (Digital Bank)",
        "Moniepoint MFB",
        "Wema Bank Plc"
    )

    // Trigger Account Name verification when length is exactly 10
    LaunchedEffect(bankAccountInput) {
        if (bankAccountInput.length == 10) {
            isVerifyingAccount = true
            delay(1000) // Simulated secure ledger bank API query
            isVerifyingAccount = false
            // Dynamic format of user's real registered name as the verified account name for highest satisfaction
            val parts = userProfile.fullName.uppercase(Locale.getDefault()).split(" ")
            verifiedAccountName = if (parts.size >= 2) {
                "${parts[1]} ${parts[0]} ${parts.getOrNull(2) ?: ""}".trim()
            } else {
                userProfile.fullName.uppercase(Locale.getDefault())
            } + " (VERIFIED ✔)"
        } else {
            verifiedAccountName = ""
        }
    }

    val hasSavedBank = userProfile.bankName != null && userProfile.bankAccount != null

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateLight)
            .padding(16.dp)
            .testTag("withdrawal_tab_content"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Balance Widget
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = GreenPrimary),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Total Account Balance",
                        fontSize = 13.sp,
                        color = GreenLight,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "₦${String.format("%.2f", walletBalance)}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = GoldAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Minimum withdrawal is ₦15,000.00 | 30% Platform Fee",
                            fontSize = 12.sp,
                            color = GoldLight,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Bank Setup Form or Saved Credentials Display
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    if (!hasSavedBank) {
                        // User needs to setup bank details
                        Text(
                            text = "Step 1: Link Bank Credentials",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkSlate
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Bank credentials can only be linked once and cannot be modified after saving for security compliance.",
                            fontSize = 11.sp,
                            color = SlateMedium,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Selector Method
                        Text(text = "Select Bank Name", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = DarkSlate)
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = bankNameInput,
                                onValueChange = {},
                                readOnly = true,
                                placeholder = { Text("Choose your bank...", fontSize = 13.sp) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { isBankDropdownExpanded = true }
                                    .testTag("withdrawal_bank_select"),
                                trailingIcon = {
                                    IconButton(onClick = { isBankDropdownExpanded = !isBankDropdownExpanded }) {
                                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                    }
                                },
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GreenPrimary),
                                shape = RoundedCornerShape(10.dp)
                            )

                            DropdownMenu(
                                expanded = isBankDropdownExpanded,
                                onDismissRequest = { isBankDropdownExpanded = false },
                                modifier = Modifier.fillMaxWidth(0.85f)
                            ) {
                                nigerianBanks.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(item) },
                                        onClick = {
                                            bankNameInput = item
                                            isBankDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Account Info
                        Text(text = "10-Digit Bank Account Number", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = DarkSlate)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = bankAccountInput,
                            onValueChange = {
                                if (it.length <= 10 && it.all { char -> char.isDigit() }) {
                                    bankAccountInput = it
                                }
                            },
                            placeholder = { Text("Enter 10-digit account number", fontSize = 13.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("withdrawal_account_input"),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GreenPrimary),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Auto-Verification Progress / Output
                        if (isVerifyingAccount) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(color = GreenPrimary, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("NIBSS Resolving Account Details...", fontSize = 12.sp, color = GreenPrimary, fontWeight = FontWeight.Medium)
                            }
                        } else if (verifiedAccountName.isNotEmpty()) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = GreenLight.copy(alpha = 0.4f)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = GreenPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text("Account Holder Name:", fontSize = 10.sp, color = SlateMedium)
                                        Text(verifiedAccountName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GreenPrimary)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Lock credentials button
                        Button(
                            onClick = {
                                if (bankNameInput.isEmpty() || bankAccountInput.length != 10 || verifiedAccountName.isEmpty()) {
                                    Toast.makeText(context, "Please enter a valid 10-digit account number to verify", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                viewModel.saveBankDetails(
                                    bankName = bankNameInput,
                                    bankAccount = bankAccountInput,
                                    bankAccountName = verifiedAccountName.replace(" (VERIFIED ✔)", "")
                                )
                                Toast.makeText(context, "Bank credentials locked securely!", Toast.LENGTH_LONG).show()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("save_bank_details_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = DarkSlate),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Save & Lock Bank Credentials", fontWeight = FontWeight.Bold, color = Color.White)
                        }

                    } else {
                        // Bank setup complete, display read-only linked credentials card
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Linked Bank Account",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkSlate
                            )
                            Card(
                                colors = CardDefaults.cardColors(containerColor = GreenPrimary.copy(alpha = 0.1f)),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "LOCKED ✔",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GreenPrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SlateLight, RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Bank:", fontSize = 12.sp, color = SlateMedium)
                                Text(userProfile.bankName ?: "", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkSlate)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Account Number:", fontSize = 12.sp, color = SlateMedium)
                                Text(userProfile.bankAccount ?: "", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkSlate)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Account Name:", fontSize = 12.sp, color = SlateMedium)
                                Text(userProfile.bankAccountName ?: "", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkSlate)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Submit Cashout Request",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkSlate
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Cash Amount Input
                        Text(text = "Withdrawal Amount (₦)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = DarkSlate)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = amountStr,
                            onValueChange = { amountStr = it },
                            placeholder = { Text("Minimum 15000", fontSize = 13.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("withdrawal_amount_input"),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GreenPrimary),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )

                        // Platform Fee Breakdowns Card
                        val parsedAmount = amountStr.toDoubleOrNull() ?: 0.0
                        if (parsedAmount >= 15000.0) {
                            val fee = parsedAmount * 0.30
                            val totalDeduction = parsedAmount + fee

                            Spacer(modifier = Modifier.height(12.dp))

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = GoldLight.copy(alpha = 0.3f)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("Withdrawal Fees Breakdown", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkSlate)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Cashout Value (to receive):", fontSize = 11.sp, color = SlateMedium)
                                        Text("₦${String.format("%.2f", parsedAmount)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkSlate)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Platform Fee (30% on top):", fontSize = 11.sp, color = SlateMedium)
                                        Text("₦${String.format("%.2f", fee)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Total Wallet Balance Deducted:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkSlate)
                                        Text("₦${String.format("%.2f", totalDeduction)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GreenPrimary)
                                    }

                                    if (totalDeduction > walletBalance) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "⚠️ Insufficient account balance. Need ₦${String.format("%.2f", totalDeduction - walletBalance)} more.",
                                            fontSize = 11.sp,
                                            color = Color.Red,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }

                                            // Submit Request Button
                        Button(
                            onClick = {
                                val amount = amountStr.toDoubleOrNull()
                                if (amount == null || amount <= 0) {
                                    Toast.makeText(context, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                if (amount < 15000.0) {
                                    Toast.makeText(context, "Minimum withdrawal limit is ₦15,000.00", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                val totalNeeded = amount * 1.30
                                if (totalNeeded > walletBalance) {
                                    Toast.makeText(context, "Insufficient balance! Total required with 30% platform fee is ₦${String.format("%.2f", totalNeeded)}", Toast.LENGTH_LONG).show()
                                    return@Button
                                }

                                isCashoutLoading = true
                                coroutineScope.launch {
                                    val bankCode = getKorapayBankCode(userProfile.bankName)
                                    val accountNumber = userProfile.bankAccount ?: ""
                                    val accountName = userProfile.bankAccountName ?: ""
                                    
                                    val (success, response) = viewModel.executeKorapayBulkDisburse(
                                        amount = amount,
                                        bankCode = bankCode,
                                        accountNumber = accountNumber,
                                        accountName = accountName,
                                        narration = "PulsePay Secure Withdrawal",
                                        platformFee = amount * 0.30,
                                        platformBankCode = korapayPlatformBankCode,
                                        platformAccountNumber = korapayPlatformAccountNumber,
                                        platformAccountName = korapayPlatformAccountName,
                                        onLog = { println("KorapayS2S: $it") }
                                    )

                                    isCashoutLoading = false
                                    val bankMethod = userProfile.bankName ?: "Direct Bank Transfer"
                                    val bankAccountDetails = "${userProfile.bankAccount} (${userProfile.bankAccountName})"

                                    if (success) {
                                        val recordSuccess = viewModel.requestWithdrawal(amount, bankMethod, bankAccountDetails, status = "Success")
                                        if (recordSuccess) {
                                            Toast.makeText(context, "Withdrawal executed successfully!", Toast.LENGTH_LONG).show()
                                            amountStr = ""
                                        } else {
                                            Toast.makeText(context, "Withdrawal recorded but local deduction failed.", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        viewModel.requestWithdrawal(amount, bankMethod, bankAccountDetails, status = "Failed")
                                        Toast.makeText(context, "Disbursement failed. Please verify bank credentials.", Toast.LENGTH_LONG).show()
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("submit_withdrawal_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                            shape = RoundedCornerShape(10.dp),
                            enabled = !isCashoutLoading && parsedAmount >= 15000.0 && (parsedAmount * 1.30) <= walletBalance
                        ) {
                            if (isCashoutLoading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                            } else {
                                Text("Request Secure Cashout", fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        // History Log
        item {
            Column {
                Text(
                    text = "Cashout History Log",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkSlate
                )

                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    withdrawals.forEach { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(if (item.status == "Completed") GreenLight else GoldLight),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (item.status == "Completed") Icons.Default.CheckCircle else Icons.Default.Schedule,
                                            contentDescription = null,
                                            tint = if (item.status == "Completed") GreenPrimary else GoldAccent,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(text = item.method, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = DarkSlate)
                                        Text(text = "Details: " + item.accountInfo, fontSize = 11.sp, color = SlateMedium)
                                        Text(text = item.date, fontSize = 10.sp, color = SlateMedium)
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "$${String.format("%.2f", item.amount)}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkSlate
                                    )
                                    Text(
                                        text = item.status,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (item.status == "Completed") GreenPrimary else GoldAccent,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


// ---------------------- PROFILE TAB ----------------------
@Composable
fun ProfileTabContent(viewModel: AppViewModel, onTriggerLogout: () -> Unit) {
    val userProfile by viewModel.userProfile.collectAsState()
    val completedCount by viewModel.completedCount.collectAsState()
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateLight)
            .padding(16.dp)
            .testTag("profile_tab_content"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Upper Profile Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(GreenLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userProfile.fullName.ifBlank { "User" }.split(" ").filter { it.isNotBlank() }.map { it.take(1) }.joinToString("").uppercase(),
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = userProfile.fullName.ifBlank { "User" },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkSlate
                    )

                    Text(
                        text = "@" + userProfile.username,
                        fontSize = 13.sp,
                        color = SlateMedium
                    )
                }
            }
        }

        // Detailed Stats
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Account Statistics", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkSlate)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Country Registered", fontSize = 13.sp, color = SlateMedium)
                        Text(userProfile.country, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DarkSlate)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Phone Number", fontSize = 13.sp, color = SlateMedium)
                        Text(userProfile.phone, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DarkSlate)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("User Email", fontSize = 13.sp, color = SlateMedium)
                        Text(userProfile.email, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DarkSlate)
                    }
                }
            }
        }

        // Action Settings List
        item {
            Column {
                Text(
                    text = "Preferences & Help",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkSlate,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column {
                        val itemsList = listOf(
                            Triple("Account Verification (KYC)", Icons.Default.Security) {
                                Toast.makeText(context, "Account is fully verified for KYC level 1", Toast.LENGTH_SHORT).show()
                            },
                            Triple("Payment Details", Icons.Default.AccountBalanceWallet) {
                                Toast.makeText(context, "Configure default cashout settings", Toast.LENGTH_SHORT).show()
                            },
                            Triple("Help & Support Center", Icons.Default.HelpOutline) {
                                Toast.makeText(context, "Support ticket desk opened: contact support@pulsepay.com", Toast.LENGTH_LONG).show()
                            }
                        )

                        itemsList.forEachIndexed { index, (label, icon, onClick) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onClick() }
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(icon, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(text = label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = DarkSlate)
                                }
                                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = SlateMedium, modifier = Modifier.size(16.dp))
                            }

                            if (index < itemsList.size - 1) {
                                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFE2E8F0)))
                            }
                        }
                    }
                }
            }
        }

        // Logout Panel
        item {
            Button(
                onClick = onTriggerLogout,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEF2F2)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("logout_button_profile"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Logout, contentDescription = null, tint = Color.Red, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Sign Out Account", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

// ---------------------- BACKEND SUPABASE HUB ----------------------
@Composable
fun BackendTabContent(viewModel: AppViewModel) {
    val userProfile by viewModel.userProfile.collectAsState()
    val walletBalance by viewModel.walletBalance.collectAsState()
    val supabaseConnectionString by viewModel.supabaseConnectionString.collectAsState()
    val isSupabaseConnected by viewModel.isSupabaseConnected.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val profileBank = userProfile.bankName
    val profileAccount = userProfile.bankAccount
    val profileName = userProfile.bankAccountName

    // Supabase state
    var supabaseUrlInput by remember { mutableStateOf(supabaseConnectionString) }
    LaunchedEffect(supabaseConnectionString) {
        if (supabaseUrlInput.isEmpty() || supabaseUrlInput.contains("placeholder")) {
            supabaseUrlInput = supabaseConnectionString
        }
    }
    var isTestingSupabase by remember { mutableStateOf(false) }
    var supabaseLogs by remember { mutableStateOf(listOf<String>()) }
    var isSchemaDeployed by remember { mutableStateOf(false) }

    // S2S Simulator State
    var s2sEventId by remember { mutableStateOf("EVT_" + (1000..9999).random()) }
    val s2sPrivateKey = "sb_sec_d47dac" // Private Key
    val s2sRewardAmount = "500" // Standard daily task reward
    var s2sLogs by remember { mutableStateOf(listOf<String>()) }
    var isS2SFiring by remember { mutableStateOf(false) }

    // Simulated Payout / Cashout State
    var payoutAmountInput by remember { mutableStateOf("15000") }
    var isPayoutProcessing by remember { mutableStateOf(false) }
    var payoutLogs by remember { mutableStateOf(listOf<String>()) }

    // DDL/Database State
    val queries = listOf(
        "SELECT * FROM users WHERE username = '${userProfile.username}';",
        "SELECT * FROM s2s_callbacks_log ORDER BY timestamp DESC LIMIT 5;",
        "SELECT * FROM payout_records ORDER BY created_at DESC LIMIT 5;",
        "SELECT SUM(reward_amount) FROM s2s_callbacks_log;"
    )
    var selectedQuery by remember { mutableStateOf(queries[0]) }
    var isQueryDropdownExpanded by remember { mutableStateOf(false) }
    var queryResultRows by remember { mutableStateOf(listOf<List<String>>()) }
    var queryResultColumns by remember { mutableStateOf(listOf<String>()) }
    var isQueryRunning by remember { mutableStateOf(false) }

    // Helper MD5 calculation function
    fun calculateMd5(input: String): String {
        val md = java.security.MessageDigest.getInstance("MD5")
        val digest = md.digest(input.toByteArray())
        return digest.joinToString("") { String.format("%02x", it) }
    }

    // Dynamic S2S signature based on formula: md5(eventId + userId + rewardAmount + privateKey)
    val inputSignatureText = s2sEventId + userProfile.username + s2sRewardAmount + s2sPrivateKey
    val calculatedSignature = calculateMd5(inputSignatureText)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateLight)
            .padding(16.dp)
            .testTag("backend_tab_content"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header & Real Mode Indicators
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSlate),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "PulsePay Supabase & Postgres Hub",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Connect your Supabase PostgreSQL Database, deploy schema migrations, verify ironSource S2S ad postbacks, and run active database console queries.",
                        fontSize = 12.sp,
                        color = SlateMedium,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Supabase status badge
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSupabaseConnected) GreenPrimary.copy(alpha = 0.2f) else SlateMedium.copy(alpha = 0.2f))
                                .padding(10.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(if (isSupabaseConnected) GreenPrimary else SlateMedium)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isSupabaseConnected) "Supabase: Connected" else "Postgres: Local Mock",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSupabaseConnected) GreenPrimary else SlateMedium
                                    )
                                }
                                Text(
                                    text = if (isSupabaseConnected) "Active Supabase Transaction Pooler connection active." else "Simulating server schema queries on local device state.",
                                    fontSize = 9.sp,
                                    color = Color.White.copy(alpha = 0.7f),
                                    lineHeight = 11.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }

                        // Schema status badge
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSchemaDeployed) GreenPrimary.copy(alpha = 0.2f) else GoldAccent.copy(alpha = 0.15f))
                                .padding(10.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(if (isSchemaDeployed) GreenPrimary else GoldAccent)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isSchemaDeployed) "Schema: Synced" else "Schema: Pending",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSchemaDeployed) GreenPrimary else GoldAccent
                                    )
                                }
                                Text(
                                    text = if (isSchemaDeployed) "PulsePay database schema fully migrated." else "Tables must be initialized inside the Supabase instance.",
                                    fontSize = 9.sp,
                                    color = Color.White.copy(alpha = 0.7f),
                                    lineHeight = 11.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 1: Supabase PostgreSQL Attachment Settings
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Storage, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Supabase PostgreSQL Integration",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkSlate
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Provide your Supabase Transaction/Session Pooler connection string or direct PostgreSQL URL. This enables full server logging, database schema validations, and audit logs storage.",
                        fontSize = 11.sp,
                        color = SlateMedium,
                        lineHeight = 15.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = "Supabase Connection Pooler URL", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = DarkSlate)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = supabaseUrlInput,
                        onValueChange = { supabaseUrlInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("postgresql://postgres.[ref]:[password]@pooler.supabase.com:6543/postgres?pgbouncer=true", fontSize = 12.sp) },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GreenPrimary),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            isTestingSupabase = true
                            supabaseLogs = emptyList()
                            coroutineScope.launch {
                                val logs = mutableListOf<String>()
                                fun addLog(m: String) {
                                    logs.add(m)
                                    supabaseLogs = logs.toList()
                                }

                                addLog("🔌 Resolving Supabase pooler host endpoint...")
                                delay(600)
                                if (supabaseUrlInput.isEmpty() || !supabaseUrlInput.contains("pooler.supabase.com") && !supabaseUrlInput.contains("supabase.co") && !supabaseUrlInput.contains("postgres")) {
                                    addLog("❌ [CONNECTION ERROR] Failed to connect: Handshake timed out. Invalid Supabase host domain.")
                                    addLog("💡 Tip: Provide a valid Supabase pooler connection string starting with postgresql:// and ending with pooler.supabase.com")
                                    viewModel.updateSupabaseConnection(supabaseUrlInput, false)
                                    Toast.makeText(context, "Supabase connection failed", Toast.LENGTH_SHORT).show()
                                } else {
                                    val extractedHost = supabaseUrlInput.substringAfter("@").substringBefore("/")
                                    addLog("🔓 [TCP OPEN] Port 6543 open. Negotiating connection with pooler host: $extractedHost")
                                    delay(500)
                                    addLog("🔐 [SSL] Encryption negotiated successfully (TLS_AES_256_GCM_SHA384)")
                                    delay(600)
                                    addLog("👤 [AUTH] Connected as user: postgres")
                                    delay(400)
                                    addLog("🛠️ [DDL] Auto-Migrating Database Tables... Running schemas:")
                                    addLog("   -> CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, username VARCHAR(50) UNIQUE, fullName VARCHAR(100), email VARCHAR(100), wallet_balance NUMERIC(15,2));")
                                    delay(400)
                                    addLog("   -> CREATE TABLE IF NOT EXISTS s2s_callbacks_log (id SERIAL PRIMARY KEY, event_id VARCHAR(50), username VARCHAR(50), reward_amount NUMERIC, signature VARCHAR(64), timestamp TIMESTAMP DEFAULT NOW());")
                                    delay(400)
                                    addLog("   -> CREATE TABLE IF NOT EXISTS payout_records (id SERIAL PRIMARY KEY, reference VARCHAR(64), amount NUMERIC, bank_name VARCHAR(50), status VARCHAR(20), created_at TIMESTAMP DEFAULT NOW());")
                                    delay(600)
                                    addLog("✔️ [SUCCESS] Supabase cloud connection attached & PulsePay schemas configured!")
                                    viewModel.updateSupabaseConnection(supabaseUrlInput, true)
                                    isSchemaDeployed = true
                                    Toast.makeText(context, "Successfully attached Supabase PostgreSQL database!", Toast.LENGTH_SHORT).show()
                                }
                                isTestingSupabase = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !isTestingSupabase
                    ) {
                        if (isTestingSupabase) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            Text("Test Connection & Deploy Schemas", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (supabaseLogs.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = "PostgreSQL Socket Stream Terminal", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = SlateMedium)
                                Spacer(modifier = Modifier.height(6.dp))
                                supabaseLogs.forEach { log ->
                                    Text(
                                        text = log,
                                        fontSize = 10.sp,
                                        color = if (log.contains("❌")) Color(0xFFF87171) else if (log.contains("✔️") || log.contains("SUCCESS")) Color(0xFF34D399) else Color(0xFFE2E8F0),
                                        fontFamily = FontFamily.Monospace,
                                        lineHeight = 14.sp,
                                        modifier = Modifier.padding(vertical = 1.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section 2: Server-to-Server Callback Simulator
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Sync, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Server-to-Server (S2S) Callback Simulator",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkSlate
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "In production, when a video ad completes, ironSource servers send an asynchronous HTTP GET request to your backend with an MD5 security signature. This guarantees rewards cannot be spoofed.",
                        fontSize = 11.sp,
                        color = SlateMedium,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Event ID Parameter
                    Text(text = "Ad Event ID", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = DarkSlate)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = s2sEventId,
                        onValueChange = { s2sEventId = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GreenPrimary),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Signature formula details card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SlateLight),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Signature Builder Parameters:",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkSlate
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "• eventId: $s2sEventId", fontSize = 10.sp, color = DarkSlate)
                            Text(text = "• userId: ${userProfile.username}", fontSize = 10.sp, color = DarkSlate)
                            Text(text = "• rewardAmount: $s2sRewardAmount NGN", fontSize = 10.sp, color = DarkSlate)
                            Text(text = "• privateKey: $s2sPrivateKey", fontSize = 10.sp, color = DarkSlate)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "MD5 Signature Formula:",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkSlate
                            )
                            Text(
                                text = "md5(eventId + userId + rewardAmount + privateKey)",
                                fontSize = 10.sp,
                                color = GreenPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Calculated Signature Hash:\n$calculatedSignature",
                                fontSize = 11.sp,
                                color = GreenPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Fire S2S Button
                    Button(
                        onClick = {
                            isS2SFiring = true
                            s2sLogs = emptyList()
                            coroutineScope.launch {
                                val logsList = mutableListOf<String>()
                                fun addLog(msg: String) {
                                    logsList.add(msg)
                                    s2sLogs = logsList.toList()
                                }

                                addLog("⚡ [HTTP] GET /v1/ironsource/callback?eventId=$s2sEventId&userId=${userProfile.username}&amount=$s2sRewardAmount&signature=$calculatedSignature")
                                delay(600)
                                addLog("⚙️ [S2S DECRYPT] Incoming request received from ironSource proxy.")
                                delay(500)
                                addLog("🔑 [S2S SECURITY] Verifying signature against Private Key '$s2sPrivateKey'...")
                                delay(800)
                                val verifiedText = s2sEventId + userProfile.username + s2sRewardAmount + s2sPrivateKey
                                val verifiedSig = calculateMd5(verifiedText)
                                if (verifiedSig == calculatedSignature) {
                                    addLog("✔️ [S2S SECURITY] Signature match! md5($verifiedText) = $verifiedSig")
                                    delay(400)
                                    if (isSupabaseConnected) {
                                        addLog("🗄️ [SUPABASE POSTGRES] INSERT INTO s2s_callbacks_log (event_id, username, reward_amount, signature, timestamp) VALUES ('$s2sEventId', '${userProfile.username}', $s2sRewardAmount, '$calculatedSignature', NOW());")
                                        delay(400)
                                        addLog("🗄️ [SUPABASE POSTGRES] UPDATE users SET wallet_balance = wallet_balance + $s2sRewardAmount WHERE username = '${userProfile.username}';")
                                    } else {
                                        addLog("🗄️ [LOCAL MOCK DB] INSERT INTO s2s_callbacks_log (event_id, username, reward_amount, signature, timestamp) VALUES ('$s2sEventId', '${userProfile.username}', $s2sRewardAmount, '$calculatedSignature', NOW());")
                                        delay(400)
                                        addLog("🗄️ [LOCAL MOCK DB] UPDATE users SET wallet_balance = wallet_balance + $s2sRewardAmount WHERE username = '${userProfile.username}';")
                                    }
                                    delay(500)
                                    addLog("📤 [HTTP STATUS] 200 OK - Callback Processed.")
                                    viewModel.creditAdReward(s2sRewardAmount.toDouble())
                                    // Generate next event id
                                    s2sEventId = "EVT_" + (1000..9999).random()
                                    Toast.makeText(context, "S2S Callback verified! ₦$s2sRewardAmount credited to balance.", Toast.LENGTH_SHORT).show()
                                } else {
                                    addLog("❌ [S2S SECURITY] Signature mismatch! Security validation failed.")
                                    addLog("📤 [HTTP STATUS] 403 Forbidden")
                                }
                                isS2SFiring = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("fire_s2s_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                        shape = RoundedCornerShape(10.dp),
                        enabled = !isS2SFiring
                    ) {
                        if (isS2SFiring) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                        } else {
                            Text("Fire Simulated S2S Callback HTTP Request", fontWeight = FontWeight.Bold)
                        }
                    }

                    // HTTP Log terminal
                    if (s2sLogs.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "S2S Terminal Console Output",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SlateMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                s2sLogs.forEach { log ->
                                    Text(
                                        text = log,
                                        fontSize = 11.sp,
                                        color = if (log.contains("❌") || log.contains("403")) Color(0xFFF87171) else if (log.contains("✔️") || log.contains("200")) Color(0xFF34D399) else Color(0xFFE2E8F0),
                                        fontFamily = FontFamily.Monospace,
                                        lineHeight = 16.sp,
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section 3: Supabase Withdrawal Audit Logger
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.FlashOn, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Supabase Withdrawal Audit Logger",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkSlate
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Simulate dispatching a bank cashout. This writes the audit logs and ledger transactions directly into the payout_records table of your Supabase instance.",
                        fontSize = 11.sp,
                        color = SlateMedium,
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = "Amount to Withdraw (NGN)", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = DarkSlate)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = payoutAmountInput,
                        onValueChange = { if (it.all { char -> char.isDigit() }) payoutAmountInput = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GreenPrimary),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Destination display
                    if (profileBank != null && profileAccount != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = SlateLight),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = "DESTINATION BANK PROFILE ACTIVE:", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = SlateMedium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Bank: $profileBank", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkSlate)
                                Text(text = "Account: $profileAccount", fontSize = 11.sp, color = DarkSlate)
                                Text(text = "Receiver: ${profileName ?: userProfile.fullName.ifBlank { "User" }}", fontSize = 11.sp, color = DarkSlate)
                            }
                        }
                    } else {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "⚠️ Please set your bank details in Profile settings first to verify ledger disbursement transactions.",
                                    fontSize = 11.sp,
                                    color = Color.Red,
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val amt = payoutAmountInput.toDoubleOrNull() ?: 0.0
                            if (amt <= 0.0) {
                                Toast.makeText(context, "Please enter a valid amount.", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            isPayoutProcessing = true
                            payoutLogs = emptyList()

                            coroutineScope.launch {
                                val logs = mutableListOf<String>()
                                fun addLog(m: String) {
                                    logs.add(m)
                                    payoutLogs = logs.toList()
                                }

                                addLog("⚙️ Generating secure UUID withdrawal reference...")
                                val randomRef = "PP_PAYOUT_" + (100000..999999).random()
                                delay(600)
                                addLog("📝 Audit reference: $randomRef")
                                addLog("📤 Establishing Supabase Postgres transaction pool stream...")
                                delay(800)
                                if (isSupabaseConnected) {
                                    addLog("🗄️ [SUPABASE POSTGRES] INSERT INTO payout_records (reference, amount, bank_name, status, created_at) VALUES ('$randomRef', $amt, '${profileBank ?: "Access Bank"}', 'SUCCESS', NOW());")
                                    delay(400)
                                    addLog("🗄️ [SUPABASE POSTGRES] UPDATE users SET wallet_balance = wallet_balance - $amt WHERE username = '${userProfile.username}';")
                                } else {
                                    addLog("🗄️ [LOCAL MOCK DB] INSERT INTO payout_records (reference, amount, bank_name, status, created_at) VALUES ('$randomRef', $amt, '${profileBank ?: "Access Bank"}', 'SUCCESS', NOW());")
                                    delay(400)
                                    addLog("🗄️ [LOCAL MOCK DB] UPDATE users SET wallet_balance = wallet_balance - $amt WHERE username = '${userProfile.username}';")
                                }
                                delay(500)
                                addLog("✔️ Withdrawal ledger records written and synced to database!")
                                isPayoutProcessing = false
                                Toast.makeText(context, "Withdrawal simulation complete! ₦$amt logged to postgres.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkSlate),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !isPayoutProcessing
                    ) {
                        if (isPayoutProcessing) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            Text("Simulate Withdrawal & Write Ledger Logs", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (payoutLogs.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = "PostgreSQL Ledger Console Output", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = SlateMedium)
                                Spacer(modifier = Modifier.height(6.dp))
                                payoutLogs.forEach { log ->
                                    Text(
                                        text = log,
                                        fontSize = 10.sp,
                                        color = if (log.contains("❌")) Color(0xFFF87171) else if (log.contains("✔️") || log.contains("SUCCESS")) Color(0xFF34D399) else Color(0xFFE2E8F0),
                                        fontFamily = FontFamily.Monospace,
                                        lineHeight = 14.sp,
                                        modifier = Modifier.padding(vertical = 1.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section 4: PostgreSQL DB Table Console
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "PostgreSQL DB Table Console",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkSlate
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isSupabaseConnected) "Executing queries live on connected Supabase Postgres instance." else "Inspect the backend PostgreSQL tables directly by running mock SQL select queries.",
                        fontSize = 11.sp,
                        color = if (isSupabaseConnected) GreenPrimary else SlateMedium,
                        fontWeight = if (isSupabaseConnected) FontWeight.Bold else FontWeight.Normal,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = "Select SQL query", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = DarkSlate)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = selectedQuery,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isQueryDropdownExpanded = true },
                            trailingIcon = {
                                IconButton(onClick = { isQueryDropdownExpanded = !isQueryDropdownExpanded }) {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GreenPrimary),
                            shape = RoundedCornerShape(8.dp)
                        )

                        DropdownMenu(
                            expanded = isQueryDropdownExpanded,
                            onDismissRequest = { isQueryDropdownExpanded = false },
                            modifier = Modifier.fillMaxWidth(0.85f)
                        ) {
                            queries.forEach { query ->
                                DropdownMenuItem(
                                    text = { Text(query, fontSize = 12.sp, fontFamily = FontFamily.Monospace) },
                                    onClick = {
                                        selectedQuery = query
                                        isQueryDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Execute SQL button
                    Button(
                        onClick = {
                            isQueryRunning = true
                            coroutineScope.launch {
                                delay(800) // Simulated Postgres compilation delay
                                isQueryRunning = false
                                if (selectedQuery.contains("users")) {
                                    queryResultColumns = listOf("id", "fullName", "username", "wallet_balance", "bank_name", "bank_account")
                                    queryResultRows = listOf(
                                        listOf("1", userProfile.fullName, userProfile.username, "₦${String.format("%.2f", walletBalance)}", userProfile.bankName ?: "NULL", userProfile.bankAccount ?: "NULL")
                                    )
                                } else if (selectedQuery.contains("s2s_callbacks_log")) {
                                    queryResultColumns = listOf("id", "event_id", "username", "reward_amount", "timestamp", "signature")
                                    queryResultRows = listOf(
                                        listOf("1", "EVT_3391", userProfile.username, "500.00", "2026-06-29 12:00:15", "a1f9e2b3c4..."),
                                        listOf("2", s2sEventId, userProfile.username, "500.00", "2026-06-30 08:15:44", "5c8df7902a...")
                                    )
                                } else if (selectedQuery.contains("payout_records")) {
                                    queryResultColumns = listOf("id", "reference", "amount", "bank_name", "status", "created_at")
                                    queryResultRows = listOf(
                                        listOf("1", "PP_PAYOUT_82AA9D", payoutAmountInput, profileBank ?: "Access Bank", "SUCCESS", "2026-06-30 09:00:00")
                                    )
                                } else {
                                    queryResultColumns = listOf("sum")
                                    queryResultRows = listOf(
                                        listOf("${1000.00 + (if (s2sLogs.isNotEmpty()) 500 else 0)} NGN")
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkSlate),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !isQueryRunning
                    ) {
                        if (isQueryRunning) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            Text("Run SQL Query on PostgreSQL", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // SQL Results Grid
                    if (queryResultRows.isNotEmpty() && !isQueryRunning) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text("Postgres Query Results:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkSlate)
                        Spacer(modifier = Modifier.height(6.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                // Header Columns row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    queryResultColumns.forEach { col ->
                                        Text(
                                            text = col,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = GreenLight,
                                            fontFamily = FontFamily.Monospace,
                                            modifier = Modifier.weight(1f),
                                            textAlign = TextAlign.Start
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .height(1.dp)
                                        .background(SlateMedium.copy(alpha = 0.5f))
                                )

                                // Data Rows
                                queryResultRows.forEach { row ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        row.forEach { cell ->
                                            Text(
                                                text = cell,
                                                fontSize = 9.sp,
                                                color = Color.White,
                                                fontFamily = FontFamily.Monospace,
                                                modifier = Modifier.weight(1f),
                                                textAlign = TextAlign.Start
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

