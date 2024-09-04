package kr.nbc.onemonthintern.presentation.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.nbc.onemonthintern.R
import kr.nbc.onemonthintern.databinding.ActivityMainBinding
import kr.nbc.onemonthintern.presentation.onboarding.OnBoardingActivity
import kr.nbc.onemonthintern.presentation.util.UiState
import kr.nbc.onemonthintern.presentation.util.makeShortToast
import kr.nbc.onemonthintern.presentation.util.setOnDebounceClickListener

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        checkLogin()
        initView()
    }


    private fun checkLogin() {
        Log.d("Check Login", mainViewModel.currentUserUid.toString())
        lifecycleScope.launch {
            mainViewModel.currentUserUid.collectLatest {
                when (it) {
                    is UiState.Loading -> {
                        mainViewModel.getUserUid()
                    }

                    is UiState.Error -> {
                        makeShortToast("로그인 화면으로 이동합니다.")
                        launchOnBoardingActivity()
                    }

                    is UiState.Success -> {
                        //nothing to do
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
                    launchOnBoardingActivity()
                }

            }

            btnWithDrawl.setOnDebounceClickListener {
                lifecycleScope.launch {
                    mainViewModel.withDrawl()
                    launchOnBoardingActivity()
                }
            }
        }
    }

    private fun launchOnBoardingActivity() {
        val intent = Intent(this, OnBoardingActivity::class.java)
        startActivity(intent)
        finish()
    }
}