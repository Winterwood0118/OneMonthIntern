package kr.nbc.onemonthintern.presentation.onboarding

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.nbc.onemonthintern.domain.repository.UserRepository
import kr.nbc.onemonthintern.presentation.model.UserModel
import kr.nbc.onemonthintern.presentation.util.toEntity
import javax.inject.Inject

@HiltViewModel
class OnBoardingSharedViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private var _isDuplicate = true
    val isDuplicated get() = _isDuplicate

    private var _emailInput: String? = null
    val emailInput get() = _emailInput!!
    private var _passwordInput: String? = null
    val passwordInput get() = _passwordInput!!
    private var _nameInput: String? = null
    val nameInput get() = _nameInput!!
    private var _phoneNumberInput: String? = null
    val phoneNumberInput get() = _phoneNumberInput!!

    suspend fun signUp() {
        val userModel = UserModel(emailInput, nameInput, phoneNumberInput)
        try {
            userRepository.signUp(emailInput, passwordInput, userModel.toEntity())
        } catch (e: Exception) {
            Log.e("Unknown Error", e.toString(), e)
        }
    }

    fun inputEmail(email: String){
        _emailInput = email
    }

    fun inputPassword(password: String){
        _passwordInput = passwordInput
    }

    fun inputName(name: String){
        _nameInput = name
    }

    fun inputPhoneNumber(phoneNumber: String){
        _phoneNumberInput = phoneNumber
    }

    suspend fun checkDuplicate(email: String){
        viewModelScope.launch {
            try {
                _isDuplicate = userRepository.isDuplicateEmail(email)
            }catch (e: Exception){
                Log.e("Unknown Error", e.toString(), e)
                _isDuplicate = true
            }
        }
    }

}