package student.projects.langbuddyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import student.projects.langbuddyapp.auth.AuthManager
import student.projects.langbuddyapp.data.repository.LeaderboardRepository
import student.projects.langbuddyapp.data.repository.LessonRepository
import student.projects.langbuddyapp.data.repository.UserRepository
import student.projects.langbuddyapp.ui.navigation.NavGraph
import student.projects.langbuddyapp.ui.theme.LangBuddyTheme

class MainActivity : ComponentActivity() {

    private lateinit var authManager: AuthManager
    private lateinit var userRepository: UserRepository
    private lateinit var lessonRepository: LessonRepository
    private lateinit var leaderboardRepository: LeaderboardRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize managers and repositories
        authManager = AuthManager(this)
        userRepository = UserRepository()
        lessonRepository = LessonRepository()
        leaderboardRepository = LeaderboardRepository()

        setContent {
            LangBuddyTheme {
                val navController = rememberNavController()

                NavGraph(
                    navController = navController,
                    authManager = authManager,
                    userRepository = userRepository,
                    lessonRepository = lessonRepository,
                    leaderboardRepository = leaderboardRepository
                )
            }
        }
    }
}