package kr.nbc.onemonthintern.presentation.onboarding.signup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.nbc.onemonthintern.domain.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel@Inject constructor(
    private val userRepository: UserRepository
) :ViewModel() {
}