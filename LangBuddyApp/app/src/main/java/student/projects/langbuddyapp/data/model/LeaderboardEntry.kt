package student.projects.langbuddyapp.data.model

data class LeaderboardEntry(
    val uid: String = "",
    val displayName: String = "",
    val xp: Int = 0,
    val streak: Int = 0,
    val rank: Int = 0,
    val photoUrl: String = ""
)