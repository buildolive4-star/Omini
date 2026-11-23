package com.example

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ai.configureAppCheck
import com.example.data.AppRoute
import com.example.ui.components.AnimatedSkyPinkBackground
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.AiAssistantScreen
import com.example.ui.screens.CustomerDetailScreen
import com.example.ui.screens.FloatingGlassBottomBar
import com.example.ui.screens.FoodDeliveryScreen
import com.example.ui.screens.GoodsTransportScreen
import com.example.ui.screens.GroceryOwnerPanelScreen
import com.example.ui.screens.GroceryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MultiServiceCartScreen
import com.example.ui.screens.OrdersHubScreen
import com.example.ui.screens.ParcelDeliveryScreen
import com.example.ui.screens.PartnerDashboardScreen
import com.example.ui.screens.PartnerRegistrationScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RestaurantOwnerPanelScreen
import com.example.ui.screens.RideBookingScreen
import com.example.ui.screens.RoleAuthGateScreen
import com.example.ui.screens.ServicesDirectoryScreen
import com.example.ui.screens.SkyPulseGlassDrawer
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.theme.SkyPulseTheme
import com.example.viewmodel.SuperAppViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // STEP 1: Configure Firebase App Check with launch intent before UI or ViewModels
        configureAppCheck(this, intent)

        enableEdgeToEdge()

        setContent {
            SkyPulseSuperAppRoot()
        }
    }
}

@Composable
fun SkyPulseSuperAppRoot(
    viewModel: SuperAppViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    SkyPulseTheme(darkGlassTheme = state.isDarkGlassMode) {
        SkyPulseSuperAppScaffold(state = state, viewModel = viewModel)
    }
}

@Composable
private fun SkyPulseSuperAppScaffold(
    state: com.example.viewmodel.SuperAppUiState,
    viewModel: SuperAppViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val voiceSearchLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spoken = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()
            if (!spoken.isNullOrBlank()) {
                if (state.currentRoute == AppRoute.AiAssistant) {
                    viewModel.sendAiChatQuery(spoken)
                } else {
                    viewModel.updateSearchQuery(spoken)
                    viewModel.showMessage("Voice search: \"$spoken\"")
                }
            }
        }
    }

    val launchVoiceSearch: () -> Unit = {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to search rides, food, grocery, or ask AI…")
        }
        try {
            voiceSearchLauncher.launch(intent)
        } catch (_: Exception) {
            viewModel.showMessage("Voice input unavailable on this device. Type in the search bar instead.")
        }
    }

    LaunchedEffect(state.snackbarMessage) {
        state.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    if (state.currentRoute == AppRoute.Splash) {
        SplashScreen(onFinished = viewModel::finishSplash)
        return
    }

    val showBottomBar = state.currentRoute in setOf(
        AppRoute.Home,
        AppRoute.Services,
        AppRoute.Orders,
        AppRoute.Wallet,
        AppRoute.Profile
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = showBottomBar || drawerState.isOpen,
        drawerContent = {
            SkyPulseGlassDrawer(
                state = state,
                onNavigate = { route -> viewModel.navigateTo(route) },
                onToggleTheme = { viewModel.toggleDarkGlassTheme() },
                onLogout = { viewModel.logoutRolePanels() },
                onCloseDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        AnimatedSkyPinkBackground {
            Scaffold(
                containerColor = Color.Transparent,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                bottomBar = {
                    if (showBottomBar) {
                        FloatingGlassBottomBar(
                            currentRoute = state.currentRoute,
                            liveOrdersCount = state.orders.count { it.isLive },
                            onSelect = { tabRoute -> viewModel.navigateTo(tabRoute, clearStack = true) }
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .padding(top = innerPadding.calculateTopPadding())
                ) {
                    AnimatedContent(
                        targetState = state.currentRoute,
                        transitionSpec = {
                            (fadeIn(tween(220)) + slideInHorizontally(tween(220)) { it / 14 }) togetherWith
                                (fadeOut(tween(180)) + slideOutHorizontally(tween(180)) { -it / 14 })
                        },
                        label = "route_transition"
                    ) { route ->
                        when (route) {
                            AppRoute.Splash -> SplashScreen(onFinished = viewModel::finishSplash)
                            AppRoute.Home -> HomeScreen(
                                state = state,
                                viewModel = viewModel,
                                onOpenDrawer = { scope.launch { drawerState.open() } },
                                onVoiceSearchClick = launchVoiceSearch
                            )
                            AppRoute.Services -> ServicesDirectoryScreen(
                                state = state,
                                viewModel = viewModel,
                                onOpenDrawer = { scope.launch { drawerState.open() } }
                            )
                            AppRoute.Orders -> OrdersHubScreen(state = state, viewModel = viewModel)
                            AppRoute.Wallet -> WalletScreen(state = state, viewModel = viewModel)
                            AppRoute.Profile -> ProfileScreen(state = state, viewModel = viewModel)
                            AppRoute.Ride -> RideBookingScreen(state = state, viewModel = viewModel)
                            AppRoute.Food -> FoodDeliveryScreen(state = state, viewModel = viewModel)
                            AppRoute.Grocery -> GroceryScreen(state = state, viewModel = viewModel)
                            AppRoute.Parcel -> ParcelDeliveryScreen(state = state, viewModel = viewModel)
                            AppRoute.Transport -> GoodsTransportScreen(state = state, viewModel = viewModel)
                            AppRoute.MultiCart -> MultiServiceCartScreen(state = state, viewModel = viewModel)
                            AppRoute.AiAssistant -> AiAssistantScreen(
                                state = state,
                                viewModel = viewModel,
                                onVoicePromptClick = launchVoiceSearch
                            )
                            is AppRoute.CustomerDetail -> CustomerDetailScreen(
                                section = route.section,
                                state = state,
                                viewModel = viewModel
                            )
                            is AppRoute.RoleAuthGate -> RoleAuthGateScreen(
                                targetRole = route.targetRole,
                                viewModel = viewModel
                            )
                            AppRoute.PartnerDashboard -> PartnerDashboardScreen(state = state, viewModel = viewModel)
                            AppRoute.PartnerRegistration -> PartnerRegistrationScreen(state = state, viewModel = viewModel)
                            AppRoute.RestaurantPanel -> RestaurantOwnerPanelScreen(state = state, viewModel = viewModel)
                            AppRoute.ShopPanel -> GroceryOwnerPanelScreen(state = state, viewModel = viewModel)
                            AppRoute.AdminPanel -> AdminPanelScreen(state = state, viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}
