package student.projects.langbuddyapp.data.repository

import student.projects.langbuddyapp.data.model.User
import student.projects.langbuddyapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class UserRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val usersCollection = db.collection(Constants.USERS_COLLECTION)

    // Create or update user
    suspend fun createOrUpdateUser(user: User): Result<Unit> {
        return try {
            usersCollection.document(user.uid).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get user by ID
    suspend fun getUser(uid: String): Result<User?> {
        return try {
            val document = usersCollection.document(uid).get().await()
            val user = document.toObject(User::class.java)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get current user
    suspend fun getCurrentUser(): Result<User?> {
        val uid = auth.currentUser?.uid ?: return Result.success(null)
        return getUser(uid)
    }

    // Update XP
    suspend fun updateXP(uid: String, xpToAdd: Int): Result<Unit> {
        return try {
            val docRef = usersCollection.document(uid)
            db.runTransaction { transaction ->
                val snapshot = transaction.get(docRef)
                val currentXP = snapshot.getLong("xp")?.toInt() ?: 0
                transaction.update(docRef, "xp", currentXP + xpToAdd)
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Update streak
    suspend fun updateStreak(uid: String): Result<Int> {
        return try {
            val docRef = usersCollection.document(uid)
            val snapshot = docRef.get().await()
            val lastLogin = snapshot.getLong("lastLoginDate") ?: 0
            val currentStreak = snapshot.getLong("streak")?.toInt() ?: 0

            val today = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val lastLoginDate = Calendar.getInstance().apply {
                timeInMillis = lastLogin
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val daysDifference = ((today - lastLoginDate) / (1000 * 60 * 60 * 24)).toInt()

            val newStreak = when {
                daysDifference == 0 -> currentStreak // Same day
                daysDifference == 1 -> currentStreak + 1 // Next day
                else -> 1 // Streak broken
            }

            docRef.update(
                mapOf(
                    "streak" to newStreak,
                    "lastLoginDate" to System.currentTimeMillis()
                )
            ).await()

            Result.success(newStreak)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Update settings
    suspend fun updateSettings(
        uid: String,
        darkMode: Boolean? = null,
        notifications: Boolean? = null,
        language: String? = null
    ): Result<Unit> {
        return try {
            val updates = mutableMapOf<String, Any>()
            darkMode?.let { updates["darkModeEnabled"] = it }
            notifications?.let { updates["notificationsEnabled"] = it }
            language?.let { updates["preferredLanguage"] = it }

            if (updates.isNotEmpty()) {
                usersCollection.document(uid).update(updates).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}