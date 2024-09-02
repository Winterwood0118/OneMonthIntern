package kr.nbc.onemonthintern.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kr.nbc.onemonthintern.domain.repository.UserRepository
import kr.nbc.onemonthintern.presentation.util.UiState
import kr.nbc.onemonthintern.presentation.util.toModel
import kr.nbc.onemonthintern.presentation.model.UserModel
import javax.inject.Inject

@HiltViewModel
class MainSharedViewModel @Inject constructor(
    private val userRepository: UserRepository
):ViewModel() {
    private val _currentUserData = MutableStateFlow<UiState<UserModel>>(UiState.Loading)
    val currentUserData get() = _currentUserData

    fun getUserData() {
        viewModelScope.launch {
            try{
                _currentUserData.emit(UiState.Success(userRepository.getUserData().toModel()))
            }catch (e: Exception){
                _currentUserData.emit(UiState.Error(e.toString()))
            }
        }
    }
}