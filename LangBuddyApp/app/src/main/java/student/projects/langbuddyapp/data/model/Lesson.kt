package student.projects.langbuddyapp.data.model

data class Lesson(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val chapter: Int = 1,
    val language: String = "isiZulu",
    val xpReward: Int = 10,
    val questions: List<Question> = emptyList(),
    val isCompleted: Boolean = false
)