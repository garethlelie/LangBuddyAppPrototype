package student.projects.langbuddyapp.ui.navigation

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import student.projects.langbuddyapp.auth.AuthManager
import student.projects.langbuddyapp.data.repository.LeaderboardRepository
import student.projects.langbuddyapp.data.repository.LessonRepository
import student.projects.langbuddyapp.data.repository.UserRepository
import student.projects.langbuddyapp.ui.screens.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import androidx.compose.foundation.layout.Box

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Main : Screen("main")
    object LessonDetail : Screen("lesson_detail/{lessonId}") {
        fun createRoute(lessonId: String) = "lesson_detail/$lessonId"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    authManager: AuthManager,
    userRepository: UserRepository,
    lessonRepository: LessonRepository,
    leaderboardRepository: LeaderboardRepository,
    startDestination: String = Screen.Splash.route
) {
    val context = LocalContext.current

    // Google Sign-In Launcher
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        authManager.handleGoogleSignInResult(
            task = task,
            onSuccess = { user ->
                navController.navigate(Screen.Main.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            },
            onError = { /* Handle error */ }
        )
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Splash Screen
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToAuth = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                isLoggedIn = authManager.isUserLoggedIn()
            )
        }

        // Login Screen
        composable(Screen.Login.route) {
            LoginScreen(
                authManager = authManager,
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onGoogleSignIn = {
                    val signInIntent = authManager.getGoogleSignInIntent()
                    googleSignInLauncher.launch(signInIntent)
                }
            )
        }

        // Register Screen
        composable(Screen.Register.route) {
            RegisterScreen(
                authManager = authManager,
                userRepository = userRepository,
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onGoogleSignIn = {
                    val signInIntent = authManager.getGoogleSignInIntent()
                    googleSignInLauncher.launch(signInIntent)
                }
            )
        }

        // Main Screen with Bottom Navigation
        composable(Screen.Main.route) {
            MainScreen(
                userRepository = userRepository,
                lessonRepository = lessonRepository,
                leaderboardRepository = leaderboardRepository,
                authManager = authManager,
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                },
                onNavigateToLessonDetail = { lessonId ->
                    navController.navigate(Screen.LessonDetail.createRoute(lessonId))
                }
            )
        }

        // Lesson Detail Screen
        composable(
            route = Screen.LessonDetail.route,
            arguments = listOf(navArgument("lessonId") { type = NavType.StringType })
        ) { backStackEntry ->
            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: return@composable
            LessonDetailScreen(
                lessonId = lessonId,
                lessonRepository = lessonRepository,
                userRepository = userRepository,
                onNavigateBack = { navController.popBackStack() },
                onLessonComplete = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun MainScreen(
    userRepository: UserRepository,
    lessonRepository: LessonRepository,
    leaderboardRepository: LeaderboardRepository,
    authManager: AuthManager,
    onLogout: () -> Unit,
    onNavigateToLessonDetail: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Book, contentDescription = "Lessons") },
                    label = { Text("Lessons") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Leaderboard, contentDescription = "Leaderboard") },
                    label = { Text("Leaderboard") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {  // ← ADD THIS LINE
            when (selectedTab) {
                0 -> HomeScreen(
                    userRepository = userRepository,
                    onNavigateToLessons = { selectedTab = 1 }
                )
                1 -> LessonsScreen(
                    lessonRepository = lessonRepository,
                    onLessonClick = onNavigateToLessonDetail
                )
                2 -> LeaderboardScreen(
                    leaderboardRepository = leaderboardRepository,
                    userRepository = userRepository
                )
                3 -> ProfileScreen(
                    userRepository = userRepository
                )
                4 -> SettingsScreen(
                    userRepository = userRepository,
                    authManager = authManager,
                    onLogout = onLogout
                )
            }
        }  // ← ADD THIS LINE
    }
}