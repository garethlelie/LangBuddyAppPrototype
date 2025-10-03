package student.projects.langbuddyapp.utils

object Constants {
    // Firestore Collections
    const val USERS_COLLECTION = "users"
    const val LESSONS_COLLECTION = "lessons"
    const val LEADERBOARD_COLLECTION = "leaderboard"

    // XP Values
    const val XP_PER_QUESTION = 10
    const val XP_LESSON_COMPLETE = 50
    const val XP_STREAK_BONUS = 5

    // Languages
    val AVAILABLE_LANGUAGES = listOf("English", "isiZulu", "Sesotho", "Afrikaans")

    // Preferences Keys
    const val PREF_DARK_MODE = "dark_mode"
    const val PREF_NOTIFICATIONS = "notifications"
    const val PREF_LANGUAGE = "language_preference"
}