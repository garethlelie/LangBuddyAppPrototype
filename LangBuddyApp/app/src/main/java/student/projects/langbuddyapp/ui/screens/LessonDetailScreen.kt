package student.projects.langbuddyapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import student.projects.langbuddyapp.data.model.Lesson
import student.projects.langbuddyapp.data.repository.LessonRepository
import student.projects.langbuddyapp.data.repository.UserRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonDetailScreen(
    lessonId: String,
    lessonRepository: LessonRepository,
    userRepository: UserRepository,
    onNavigateBack: () -> Unit,
    onLessonComplete: () -> Unit
) {
    var lesson by remember { mutableStateOf<Lesson?>(null) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var showResult by remember { mutableStateOf(false) }
    var correctAnswers by remember { mutableStateOf(0) }
    var isLessonComplete by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(lessonId) {
        scope.launch {
            val result = lessonRepository.getLessonById(lessonId)
            result.onSuccess { fetchedLesson ->
                lesson = fetchedLesson
            }
            isLoading = false
        }
    }

    if (isLoading || lesson == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (isLessonComplete) {
        // Lesson Complete Screen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(32.dp)
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Lesson Complete!",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "You answered $correctAnswers out of ${lesson?.questions?.size} correctly!",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(32.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🎉",
                            style = MaterialTheme.typography.displayLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "+${lesson?.xpReward} XP Earned!",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = onLessonComplete,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Go Home", style = MaterialTheme.typography.titleMedium)
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = {
                        currentQuestionIndex = 0
                        selectedAnswer = null
                        showResult = false
                        correctAnswers = 0
                        isLessonComplete = false
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Try Again", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    } else {
        // Question Screen
        val currentQuestion = lesson?.questions?.getOrNull(currentQuestionIndex)

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("${lesson?.title} - Question ${currentQuestionIndex + 1}/${lesson?.questions?.size}") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp)
            ) {
                // Progress Bar
                LinearProgressIndicator(
                    progress = (currentQuestionIndex + 1).toFloat() / (lesson?.questions?.size ?: 1).toFloat(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Question
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = currentQuestion?.questionText ?: "",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Answer Options
                currentQuestion?.options?.forEach { option ->
                    val isSelected = selectedAnswer == option
                    val isCorrect = option == currentQuestion.correctAnswer
                    val backgroundColor = when {
                        !showResult -> if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                        isCorrect -> MaterialTheme.colorScheme.tertiaryContainer
                        isSelected && !isCorrect -> MaterialTheme.colorScheme.errorContainer
                        else -> MaterialTheme.colorScheme.surface
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = backgroundColor),
                        onClick = {
                            if (!showResult) {
                                selectedAnswer = option
                            }
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            if (showResult && isCorrect) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Correct",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Explanation (shown after answer)
                if (showResult) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (selectedAnswer == currentQuestion?.correctAnswer) "Correct! ✓" else "Incorrect ✗",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = currentQuestion?.explanation ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Submit/Next Button
                Button(
                    onClick = {
                        if (!showResult) {
                            // Show result
                            showResult = true
                            if (selectedAnswer == currentQuestion?.correctAnswer) {
                                correctAnswers++
                            }
                        } else {
                            // Move to next question or complete
                            if (currentQuestionIndex < (lesson?.questions?.size ?: 0) - 1) {
                                currentQuestionIndex++
                                selectedAnswer = null
                                showResult = false
                            } else {
                                // Lesson complete
                                scope.launch {
                                    userRepository.getCurrentUser().onSuccess { user ->
                                        user?.let {
                                            userRepository.updateXP(it.uid, lesson?.xpReward ?: 0)
                                        }
                                    }
                                }
                                isLessonComplete = true
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = selectedAnswer != null,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = if (!showResult) "Submit" else if (currentQuestionIndex < (lesson?.questions?.size ?: 0) - 1) "Next" else "Complete",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}