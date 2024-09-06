package kr.nbc.onemonthintern.presentation.onboarding

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kr.nbc.onemonthintern.domain.repository.UserRepository
import kr.nbc.onemonthintern.presentation.model.UserModel
import kr.nbc.onemonthintern.presentation.util.UiState
import kr.nbc.onemonthintern.presentation.util.toEntity
import javax.inject.Inject

@HiltViewModel
class OnBoardingSharedViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private var _isDuplicate = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val isDuplicated: StateFlow<UiState<Boolean>> get() = _isDuplicate

    private var _logInState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val logInState: StateFlow<UiState<Boolean>> get() = _logInState

    private var _signUpState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val signUpState: StateFlow<UiState<Boolean>> get() = _signUpState

    private var _emailInput: String? = null
    private val emailInput get() = _emailInput!!
    private var _passwordInput: String? = null
    private val passwordInput get() = _passwordInput!!
    private var _nameInput: String? = null
    private val nameInput get() = _nameInput!!
    private var _phoneNumberInput: String? = null
    private val phoneNumberInput get() = _phoneNumberInput!!

    suspend fun signUp() {
        val userModel = UserModel(emailInput, nameInput, phoneNumberInput)
        try {
            userRepository.signUp(emailInput, passwordInput, userModel.toEntity())
            _signUpState.emit(UiState.Success(true))
            signIn()
        } catch (e: Exception) {
            Log.e("Unknown Error", e.toString(), e)
            _signUpState.emit(UiState.Error(e.toString()))
        }
    }

    suspend fun signIn() {
        _logInState.emit(UiState.Loading)
        try {
            userRepository.signIn(emailInput, passwordInput)
            _logInState.emit(UiState.Success(true))
        } catch (e: Exception) {
            Log.e("Unknown Error", e.toString(), e)
            _logInState.emit(UiState.Error(e.toString()))
        }
    }

    fun inputEmail(email: String) {
        Log.d("input", email)
        _emailInput = email
    }

    fun inputPassword(password: String) {
        Log.d("input", password)
        _passwordInput = password
    }

    fun inputName(name: String) {
        Log.d("input", name)
        _nameInput = name
    }

    fun inputPhoneNumber(phoneNumber: String) {
        Log.d("input", phoneNumber)
        _phoneNumberInput = phoneNumber
    }

    fun checkDuplicate() {
        viewModelScope.launch {
            _isDuplicate.emit(UiState.Loading)
            try {
                _isDuplicate.emit(UiState.Success(userRepository.isDuplicateEmail(emailInput)))
            } catch (e: Exception) {
                Log.e("Unknown Error", e.toString(), e)
                _isDuplicate.emit(UiState.Error(e.toString()))
            }
        }
    }

    fun resetDuplicateCheck() {
        viewModelScope.launch {
            _isDuplicate.emit(UiState.Loading)
        }
    }

}