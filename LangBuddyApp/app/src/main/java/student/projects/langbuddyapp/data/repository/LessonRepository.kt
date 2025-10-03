package student.projects.langbuddyapp.data.repository

import student.projects.langbuddyapp.data.model.Lesson
import student.projects.langbuddyapp.data.model.Question
import student.projects.langbuddyapp.data.model.QuestionType
import student.projects.langbuddyapp.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class LessonRepository {

    private val db = FirebaseFirestore.getInstance()
    private val lessonsCollection = db.collection(Constants.LESSONS_COLLECTION)

    // Get all lessons
    suspend fun getAllLessons(): Result<List<Lesson>> {
        return try {
            val snapshot = lessonsCollection.get().await()
            val lessons = snapshot.documents.mapNotNull { it.toObject(Lesson::class.java) }

            // If no lessons exist, create sample data
            if (lessons.isEmpty()) {
                val sampleLessons = createSampleLessons()
                sampleLessons.forEach { lesson ->
                    lessonsCollection.document(lesson.id).set(lesson).await()
                }
                Result.success(sampleLessons)
            } else {
                Result.success(lessons)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get lesson by ID
    suspend fun getLessonById(id: String): Result<Lesson?> {
        return try {
            val document = lessonsCollection.document(id).get().await()
            val lesson = document.toObject(Lesson::class.java)
            Result.success(lesson)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Create sample lessons for demo
    private fun createSampleLessons(): List<Lesson> {
        return listOf(
            Lesson(
                id = "lesson1",
                title = "Greetings",
                description = "Learn basic greetings in isiZulu",
                chapter = 1,
                language = "isiZulu",
                xpReward = 50,
                questions = listOf(
                    Question(
                        id = "q1",
                        questionText = "What is 'Hello' in isiZulu?",
                        questionType = QuestionType.MULTIPLE_CHOICE,
                        options = listOf("Sawubona", "Yebo", "Hamba", "Ngiyabonga"),
                        correctAnswer = "Sawubona",
                        explanation = "Sawubona is the common greeting in isiZulu"
                    ),
                    Question(
                        id = "q2",
                        questionText = "How do you say 'Thank you'?",
                        questionType = QuestionType.MULTIPLE_CHOICE,
                        options = listOf("Sawubona", "Ngiyabonga", "Hamba kahle", "Unjani"),
                        correctAnswer = "Ngiyabonga",
                        explanation = "Ngiyabonga means 'Thank you' in isiZulu"
                    ),
                    Question(
                        id = "q3",
                        questionText = "What does 'Yebo' mean?",
                        questionType = QuestionType.MULTIPLE_CHOICE,
                        options = listOf("No", "Yes", "Maybe", "Hello"),
                        correctAnswer = "Yes",
                        explanation = "Yebo means 'Yes' in isiZulu"
                    ),
                    Question(
                        id = "q4",
                        questionText = "How do you ask 'How are you?'",
                        questionType = QuestionType.MULTIPLE_CHOICE,
                        options = listOf("Unjani", "Ngiyaphila", "Sawubona", "Hamba"),
                        correctAnswer = "Unjani",
                        explanation = "Unjani means 'How are you?' in isiZulu"
                    ),
                    Question(
                        id = "q5",
                        questionText = "What is 'Goodbye'?",
                        questionType = QuestionType.MULTIPLE_CHOICE,
                        options = listOf("Sawubona", "Ngiyabonga", "Hamba kahle", "Yebo"),
                        correctAnswer = "Hamba kahle",
                        explanation = "Hamba kahle means 'Goodbye' in isiZulu"
                    )
                )
            ),
            Lesson(
                id = "lesson2",
                title = "Numbers",
                description = "Learn numbers 1-10 in isiZulu",
                chapter = 2,
                language = "isiZulu",
                xpReward = 50,
                questions = listOf(
                    Question(
                        id = "q1",
                        questionText = "What is 'One' in isiZulu?",
                        questionType = QuestionType.MULTIPLE_CHOICE,
                        options = listOf("Kunye", "Kubili", "Kuthathu", "Kune"),
                        correctAnswer = "Kunye",
                        explanation = "Kunye means 'One' in isiZulu"
                    ),
                    Question(
                        id = "q2",
                        questionText = "What is 'Five' in isiZulu?",
                        questionType = QuestionType.MULTIPLE_CHOICE,
                        options = listOf("Kune", "Kuhlanu", "Isithupha", "Isikhombisa"),
                        correctAnswer = "Kuhlanu",
                        explanation = "Kuhlanu means 'Five' in isiZulu"
                    )
                )
            )
        )
    }
}