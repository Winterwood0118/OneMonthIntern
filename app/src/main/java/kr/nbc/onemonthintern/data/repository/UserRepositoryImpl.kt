package kr.nbc.onemonthintern.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kr.nbc.onemonthintern.data.util.toEntity
import kr.nbc.onemonthintern.data.util.toResponse
import kr.nbc.onemonthintern.data.model.UserResponse
import kr.nbc.onemonthintern.domain.model.UserEntity
import kr.nbc.onemonthintern.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl@Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : UserRepository {
    override suspend fun signUp(email: String, password: String, userEntity: UserEntity) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        setUserData(email, userEntity)
    }

    override suspend fun signIn(email: String, password: String, userEntity: UserEntity): UserEntity {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
        val userUid = firebaseAuth.currentUser?.uid?: throw Exception("Do Not Login")
        val snapshot = firestore.collection("userData").document(userUid).get().await()
        val userData = snapshot.toObject(UserResponse::class.java)?.toEntity()
        return userData?: throw Exception("Do Not Login")
    }

    override suspend fun signOut(){
        firebaseAuth.signOut()
    }
    override suspend fun withDrawl(){
        val userUid = firebaseAuth.currentUser?.uid?: throw Exception("Do Not Login")
        firestore.collection("userData").document(userUid).delete().await()
        firebaseAuth.currentUser?.delete()?.await()
    }

    override suspend fun getUserData(): UserEntity{
        val userUid = getUserUid()
        var userData: UserEntity? = null
        firestore.collection("userData").document(userUid).addSnapshotListener{ snapshot, e ->
            e?.let {
                Log.e("Get User Data Error", e.toString(), e)
                return@addSnapshotListener
            }
            userData = snapshot?.toObject(UserResponse::class.java)?.toEntity()?: throw Exception("Get User Data Error")
        }
        return userData?: throw Exception("Get User Data Error")
    }
    override suspend fun setUserData(email: String, userEntity: UserEntity) {
        val userUid = getUserUid()
        val userData = userEntity.toResponse()
        firestore.collection("userData").document(userUid).set(userData)
    }

    override suspend fun getUserUid(): String{
        return firebaseAuth.currentUser?.uid?: throw Exception("User Not Login")
    }
}