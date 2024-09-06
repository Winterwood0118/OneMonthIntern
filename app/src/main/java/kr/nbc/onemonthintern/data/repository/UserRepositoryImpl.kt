package kr.nbc.onemonthintern.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
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

    override suspend fun getUserData(): UserEntity {
        val userUid = getUserUid()
        var userData: UserEntity? = null
        Log.d("uid", userUid)
        firestore.collection("userData").document(userUid).addSnapshotListener { snapshot, e ->
            e?.let {
                Log.e("Get User Data Error", e.toString(), e)
                return@addSnapshotListener
            }
            userData = snapshot?.toObject<UserResponse>()?.toEntity()
                ?: throw Exception("Get User Data Error")
        }
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
        if (checkFirstLogIn() && logInResult) {
            val email = firebaseAuth.currentUser?.email ?: ""
            val name = firebaseAuth.currentUser?.displayName ?: ""
            val phoneNumber = firebaseAuth.currentUser?.phoneNumber ?: ""
            val userEntity = UserEntity(email, name, phoneNumber)
            setUserData(userEntity)
        }
        return logInResult
    }

    private suspend fun checkFirstLogIn(): Boolean {
        val userUid = firebaseAuth.currentUser?.uid
        userUid?.let {
            val snapshot = firestore.collection("userData").document(userUid).get().await()
            return snapshot.data?.isEmpty() ?: true
        }
        return false
    }
}