package kr.nbc.onemonthintern.domain.repository

import kr.nbc.onemonthintern.domain.model.UserEntity

interface UserRepository {
    suspend fun signUp(email: String, password: String, userEntity: UserEntity)
    suspend fun signIn(email: String, password: String)
    suspend fun signOut()
    suspend fun withDrawl()
    suspend fun isDuplicateEmail(email: String): Boolean

    suspend fun getUserUid(): String
    suspend fun getUserData(userUid: String): UserEntity
    suspend fun setUserData(userEntity: UserEntity)

    suspend fun authWithGoogle(token: String): Boolean
}