package kr.nbc.onemonthintern.presentation.onboarding.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.nbc.onemonthintern.domain.repository.UserRepository
import kr.nbc.onemonthintern.presentation.model.UserModel
import kr.nbc.onemonthintern.presentation.util.toEntity
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel@Inject constructor(
    private val userRepository: UserRepository
) :ViewModel() {

    suspend fun signUp(email: String, password: String, userModel: UserModel){
        try {
            userRepository.signUp(email, password, userModel.toEntity())
        } catch (e: Exception){
            Log.e("Unknown Error", e.toString(), e)
        }
    }
}