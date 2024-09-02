package kr.nbc.onemonthintern.domain.repository

import kr.nbc.onemonthintern.domain.model.UserEntity

interface UserRepository {
    suspend fun signUp(email: String, password: String, userEntity: UserEntity)
    suspend fun signIn(email: String, password: String, userEntity: UserEntity): UserEntity
    suspend fun signOut()
    suspend fun withDrawl()

    suspend fun getUserUid(): String
    suspend fun getUserData(): UserEntity
    suspend fun setUserData(email: String, userEntity: UserEntity)
}