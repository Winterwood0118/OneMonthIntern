package kr.nbc.onemonthintern.presentation.main.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.nbc.onemonthintern.domain.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {

}