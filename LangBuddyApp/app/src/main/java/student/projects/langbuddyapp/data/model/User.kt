package student.projects.langbuddyapp.data.model

data class User(
    val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val xp: Int = 0,
    val streak: Int = 0,
    val lastLoginDate: Long = 0,
    val languageLearning: String = "isiZulu",
    val preferredLanguage: String = "English",
    val darkModeEnabled: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)