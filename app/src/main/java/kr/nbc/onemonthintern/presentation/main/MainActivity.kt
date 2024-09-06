package kr.nbc.onemonthintern.presentation.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.nbc.onemonthintern.R
import kr.nbc.onemonthintern.databinding.ActivityMainBinding
import kr.nbc.onemonthintern.presentation.model.UserModel
import kr.nbc.onemonthintern.presentation.onboarding.LogInSelectActivity
import kr.nbc.onemonthintern.presentation.util.UiState
import kr.nbc.onemonthintern.presentation.util.makeShortToast
import kr.nbc.onemonthintern.presentation.util.setOnDebounceClickListener

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mainViewModel: MainViewModel by viewModels()
    private var userData: UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        collectUserDataState()
        checkLogin()
        initView()
    }

    private fun collectUserDataState() {
        lifecycleScope.launch {
            mainViewModel.currentUserData.collectLatest {
                when (it) {
                    is UiState.Error -> {
                        makeShortToast("유저 데이터 로딩 에러")
                    }

                    is UiState.Loading -> {
                        //nothing to do
                    }

                    is UiState.Success -> {
                        userData = it.data
                    }
                }
            }
        }
    }


    private fun checkLogin() {
        Log.d("Check Login", mainViewModel.currentUserUid.toString())
        mainViewModel.getUserUid()
        lifecycleScope.launch {
            mainViewModel.currentUserUid.collectLatest {
                when (it) {
                    is UiState.Loading -> {
                    }

                    is UiState.Error -> {
                        makeShortToast("로그인 정보가 없습니다. 로그인 화면으로 이동합니다.")
                        launchLogInSelectActivity()
                    }

                    is UiState.Success -> {
                        mainViewModel.getUserData()
                    }
                }
            }
        }

    }

    private fun initView() {
        with(binding) {
            btnSignOut.setOnDebounceClickListener {
                lifecycleScope.launch {
                    mainViewModel.signOut()
                    makeShortToast("로그아웃하였습니다.\n로그인 화면으로 이동합니다.")
                    launchLogInSelectActivity()
                }

            }

            btnWithDrawl.setOnDebounceClickListener {
                lifecycleScope.launch {
                    mainViewModel.withDrawl()
                    makeShortToast("회원탈퇴하였습니다.\n로그인 화면으로 이동합니다.")
                    launchLogInSelectActivity()
                }
            }

            btnCheckData.setOnDebounceClickListener {
                userData?.let {
                    userDataDialog(it)
                }
            }
        }
    }

    private fun launchLogInSelectActivity() {
        val intent = Intent(this, LogInSelectActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun userDataDialog(userModel: UserModel) {
        val dialogBuilder = AlertDialog.Builder(this).apply {
            setTitle("${userModel.name}님의 정보입니다.")
            setMessage("Email: ${userModel.email}\n" + "PhoneNumber: ${userModel.phoneNumber}")
            setPositiveButton("확인") { dialog, _ ->
                dialog.dismiss()
            }
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }
}