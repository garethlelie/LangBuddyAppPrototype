package student.projects.langbuddyapp.data.model

data class Question(
    val id: String = "",
    val questionText: String = "",
    val questionType: QuestionType = QuestionType.MULTIPLE_CHOICE,
    val options: List<String> = emptyList(),
    val correctAnswer: String = "",
    val explanation: String = ""
)

enum class QuestionType {
    MULTIPLE_CHOICE,
    TRANSLATION,
    MATCHING
}