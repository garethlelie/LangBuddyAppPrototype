package student.projects.langbuddyapp.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest

class AuthManager(private val context: Context) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("YOUR_WEB_CLIENT_ID") // Replace with your Web Client ID (or ensure default_web_client_id is in strings.xml)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    // -----------------------
    // Email/Password Registration
    // -----------------------
    fun signUpWithEmail(
        email: String,
        password: String,
        fullName: String,
        onSuccess: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    ) {
        if (email.isBlank() || password.isBlank() || fullName.isBlank()) {
            onError("Please fill all fields")
            return
        }

        if (password.length < 6) {
            onError("Password must be at least 6 characters")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .build()

                    user?.updateProfile(profileUpdates)?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            onSuccess(user)
                        } else {
                            onError(it.exception?.message ?: "Failed to update profile")
                        }
                    } ?: run {
                        onError("User is null after registration")
                    }
                } else {
                    onError(task.exception?.message ?: "Registration failed")
                }
            }
    }

    // -----------------------
    // Email/Password Login
    // -----------------------
    fun loginWithEmail(
        email: String,
        password: String,
        onSuccess: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    ) {
        if (email.isBlank() || password.isBlank()) {
            onError("Please fill all fields")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess(auth.currentUser)
                } else {
                    onError(task.exception?.message ?: "Login failed")
                }
            }
    }

    // -----------------------
    // Google Sign-In Intent
    // -----------------------
    fun getGoogleSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    // -----------------------
    // Handle Google Sign-In Result
    // -----------------------
    fun handleGoogleSignInResult(
        task: Task<GoogleSignInAccount>,
        onSuccess: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!, onSuccess, onError)
        } catch (e: ApiException) {
            onError("Google sign-in failed: ${e.message}")
            Log.w("AuthManager", "Google sign-in failed", e)
        } catch (e: Exception) {
            onError("Google sign-in error: ${e.message}")
            Log.w("AuthManager", "Google sign-in error", e)
        }
    }

    private fun firebaseAuthWithGoogle(
        idToken: String,
        onSuccess: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess(auth.currentUser)
                } else {
                    onError(task.exception?.message ?: "Authentication failed")
                }
            }
    }

    // -----------------------
    // Password Reset
    // -----------------------
    fun sendPasswordResetEmail(
        email: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (email.isBlank()) {
            onError("Please enter your email")
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception?.message ?: "Failed to send reset email")
                }
            }
    }

    // -----------------------
    // Sign Out
    // -----------------------
    fun signOut() {
        auth.signOut()
        if (::googleSignInClient.isInitialized) {
            googleSignInClient.signOut()
        }
    }

    // -----------------------
    // Get Current User
    // -----------------------
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    // -----------------------
    // Check if user is logged in
    // -----------------------
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // -----------------------
    // MOCK / BYPASS for testing
    // -----------------------
    /**
     * Bypass authentication and invoke onSuccess immediately.
     * Useful for quick UI testing where you don't want to sign in via Firebase.
     */
    fun mockLogin(onSuccess: () -> Unit) {
        // Bypass real authentication
        onSuccess()
    }
}
