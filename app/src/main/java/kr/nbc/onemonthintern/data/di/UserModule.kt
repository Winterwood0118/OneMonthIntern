package kr.nbc.onemonthintern.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.nbc.onemonthintern.data.repository.UserRepositoryImpl
import kr.nbc.onemonthintern.domain.repository.UserRepository

@Module
@InstallIn(SingletonComponent::class)
internal interface UserModule {
    @Binds
    fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}