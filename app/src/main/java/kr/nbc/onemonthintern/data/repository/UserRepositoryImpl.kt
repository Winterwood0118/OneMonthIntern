package kr.nbc.onemonthintern.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import kr.nbc.onemonthintern.data.model.UserResponse
import kr.nbc.onemonthintern.data.util.toEntity
import kr.nbc.onemonthintern.data.util.toResponse
import kr.nbc.onemonthintern.domain.model.UserEntity
import kr.nbc.onemonthintern.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : UserRepository {
    override suspend fun signUp(email: String, password: String, userEntity: UserEntity) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        setUserData(userEntity)
    }

    override suspend fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun isDuplicateEmail(email: String): Boolean {
        val snapshot = firestore.collection("userData").whereEqualTo("email", email).get().await()
        Log.d("duple", snapshot.toString())
        return !snapshot.isEmpty
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun withDrawl() {
        val userUid = firebaseAuth.currentUser?.uid ?: throw Exception("Do Not Login")
        firestore.collection("userData").document(userUid).delete().await()
        firebaseAuth.currentUser?.delete()?.await()
    }

    override suspend fun getUserData(userUid: String): UserEntity {
        val snapshot = firestore.collection("userData").document(userUid).get().await()
        val userData = snapshot?.toObject<UserResponse>()?.toEntity()
        return userData ?: throw Exception("Get User Data Error")
    }

    override suspend fun setUserData(userEntity: UserEntity) {
        val userUid = getUserUid()
        val userData = userEntity.toResponse()
        firestore.collection("userData").document(userUid).set(userData)
    }

    override suspend fun getUserUid(): String {
        return firebaseAuth.currentUser?.uid ?: throw Exception("User Not Login")
    }

    override suspend fun authWithGoogle(token: String): Boolean {
        val credential = GoogleAuthProvider.getCredential(token, null)
        var logInResult = false
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Google LogIn Success", "구글 로그인 성공")
                logInResult = true
            } else {
                Log.d("Google LogIn Failure", "구글 로그인 실패")
                logInResult = false
            }
        }.await()
        val currentUser = firebaseAuth.currentUser

        if (checkFirstLogIn(currentUser) && logInResult) {
            val email = currentUser?.email ?: ""
            val name = currentUser?.displayName ?: ""
            val phoneNumber = currentUser?.phoneNumber ?: ""
            val userEntity = UserEntity(email, name, phoneNumber)
            setUserData(userEntity)
        }
        return logInResult
    }

    private suspend fun checkFirstLogIn(currentUser: FirebaseUser?): Boolean {
        val userUid = currentUser?.uid
        userUid?.let {
            val snapshot = firestore.collection("userData").document(userUid).get().await()
            return snapshot.data?.isEmpty() ?: true
        }
        return false
    }
}