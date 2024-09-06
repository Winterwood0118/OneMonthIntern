package kr.nbc.onemonthintern.presentation.onboarding

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kr.nbc.onemonthintern.domain.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class LogInSelectViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private var _isGoogleLogInSuccess = MutableStateFlow<Boolean>(false)
    val isGoogleLogInSuccess: StateFlow<Boolean> get() = _isGoogleLogInSuccess

    suspend fun googleLogIn(token: String) {
        _isGoogleLogInSuccess.emit(userRepository.authWithGoogle(token))
    }

}