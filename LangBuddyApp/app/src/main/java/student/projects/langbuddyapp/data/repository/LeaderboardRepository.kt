package student.projects.langbuddyapp.data.repository

import student.projects.langbuddyapp.data.model.LeaderboardEntry
import student.projects.langbuddyapp.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class LeaderboardRepository {

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection(Constants.USERS_COLLECTION)

    // Get top users by XP
    suspend fun getTopUsers(limit: Int = 10): Result<List<LeaderboardEntry>> {
        return try {
            val snapshot = usersCollection
                .orderBy("xp", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()

            val entries = snapshot.documents.mapIndexed { index, doc ->
                LeaderboardEntry(
                    uid = doc.id,
                    displayName = doc.getString("displayName") ?: "Anonymous",
                    xp = doc.getLong("xp")?.toInt() ?: 0,
                    streak = doc.getLong("streak")?.toInt() ?: 0,
                    rank = index + 1,
                    photoUrl = doc.getString("photoUrl") ?: ""
                )
            }

            Result.success(entries)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get user rank
    suspend fun getUserRank(uid: String, userXP: Int): Result<Int> {
        return try {
            val snapshot = usersCollection
                .whereGreaterThan("xp", userXP)
                .get()
                .await()

            val rank = snapshot.size() + 1
            Result.success(rank)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}