package kr.nbc.onemonthintern.presentation.onboarding.signin

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.nbc.onemonthintern.domain.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class SignInViewModel@Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

}