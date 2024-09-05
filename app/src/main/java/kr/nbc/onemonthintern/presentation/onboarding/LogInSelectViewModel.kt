package kr.nbc.onemonthintern.presentation.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kr.nbc.onemonthintern.domain.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class LogInSelectViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private var _isGoogleLogInSuccess = MutableLiveData(false)
    val isGoogleLogInSuccess: LiveData<Boolean> get() = _isGoogleLogInSuccess

    suspend fun googleLogIn(token: String): Boolean {
        _isGoogleLogInSuccess.postValue(userRepository.authWithGoogle(token))
        return userRepository.authWithGoogle(token)
    }

/*    fun setIsGoogleLogInSuccess(boolean: Boolean) {
        _isGoogleLogInSuccess.value = boolean
    }*/

}