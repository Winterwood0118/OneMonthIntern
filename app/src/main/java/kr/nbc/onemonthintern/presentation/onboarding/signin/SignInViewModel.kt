package kr.nbc.onemonthintern.presentation.onboarding.signin

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.nbc.onemonthintern.domain.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    suspend fun signIn(email: String, password: String) {
        try {
            userRepository.signIn(email, password)
        } catch (e: Exception) {
            Log.e("Unknown Error", e.toString(), e)
        }
    }
}