package kr.nbc.onemonthintern.presentation.main.mypage

import androidx.lifecycle.ViewModel
import kr.nbc.onemonthintern.domain.repository.UserRepository
import javax.inject.Inject

class MyPageViewModel @Inject constructor(
    userRepository: UserRepository
): ViewModel() {

}