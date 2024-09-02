package kr.nbc.onemonthintern.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    fun provideFireBaseAuthentication():FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    fun provideFireBaseFireStore():FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
}