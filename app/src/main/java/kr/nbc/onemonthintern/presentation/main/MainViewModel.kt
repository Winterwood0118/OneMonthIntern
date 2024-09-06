package kr.nbc.onemonthintern.presentation.main

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
import kr.nbc.onemonthintern.presentation.util.toModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _currentUserData = MutableStateFlow<UiState<UserModel>>(UiState.Loading)
    val currentUserData: StateFlow<UiState<UserModel>> get() = _currentUserData

    private var _currentUserUid: MutableStateFlow<UiState<String?>> =
        MutableStateFlow(UiState.Loading)
    val currentUserUid: StateFlow<UiState<String?>> get() = _currentUserUid

    fun getUserData() {
        viewModelScope.launch {
            try {
                _currentUserData.emit(UiState.Success(userRepository.getUserData((currentUserUid.value as UiState.Success).data!!).toModel()))
                Log.d("getUserData", currentUserData.toString())
            } catch (e: Exception) {
                _currentUserData.emit(UiState.Error(e.toString()))
                Log.e("getUserData", e.toString(), e)

            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            userRepository.signOut()
        }
    }

    fun withDrawl() {
        viewModelScope.launch {
            userRepository.withDrawl()
        }
    }

    fun getUserUid() {
        viewModelScope.launch {
            try {
                _currentUserUid.emit(UiState.Success(userRepository.getUserUid()))
            } catch (e: Exception) {
                _currentUserUid.emit(UiState.Error(e.toString()))
            }
        }
    }

}