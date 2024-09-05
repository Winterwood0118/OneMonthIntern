package kr.nbc.onemonthintern.presentation.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.nbc.onemonthintern.R
import kr.nbc.onemonthintern.databinding.ActivityOnBoardingBinding
import kr.nbc.onemonthintern.presentation.main.MainActivity
import kr.nbc.onemonthintern.presentation.onboarding.adpater.ViewPagerAdapter
import kr.nbc.onemonthintern.presentation.onboarding.input.CheckRegexFragment
import kr.nbc.onemonthintern.presentation.util.UiState
import kr.nbc.onemonthintern.presentation.util.makeShortToast
import kr.nbc.onemonthintern.presentation.util.setOnDebounceClickListener
import kr.nbc.onemonthintern.presentation.util.setVisibilityToGone
import kr.nbc.onemonthintern.presentation.util.setVisibilityToVisible

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {
    private val binding by lazy { ActivityOnBoardingBinding.inflate(layoutInflater) }
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    val sharedViewModel: OnBoardingSharedViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()
        collectState()
    }

    private fun collectState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                sharedViewModel.logInState.collectLatest {
                    when (it) {
                        is UiState.Success -> {
                            makeShortToast("로그인에 성공했습니다.")
                            launchMainActivity()
                        }
                        is UiState.Error -> makeShortToast(it.message)
                        is UiState.Loading -> {
                            // nothing to do
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                sharedViewModel.signUpState.collectLatest {
                    when (it) {
                        is UiState.Success -> sharedViewModel.signIn()
                        is UiState.Error -> makeShortToast(it.message)
                        is UiState.Loading -> {
                            // nothing to do
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                sharedViewModel.isDuplicated.collectLatest {
                    when (it) {
                        is UiState.Error -> {
                            //nothing to do
                        }

                        is UiState.Loading -> {
                            //nothing to do
                        }

                        is UiState.Success -> {
                            if (it.data) {
                                makeShortToast("가입정보가 확인되었습니다.\n비밀번호를 입력해주세요.")
                                with(binding) {
                                    vpOnBoarding.currentItem++
                                    btnReturn.setVisibilityToVisible()
                                    btnNext.setVisibilityToGone()
                                    btnSignIn.setVisibilityToVisible()
                                }
                                sharedViewModel.resetDuplicateCheck()
                            } else {
                                makeShortToast("가입정보가 없습니다.\n회원가입을 시작합니다.")
                                with(binding) {
                                    vpOnBoarding.currentItem++
                                    btnReturn.setVisibilityToVisible()
                                }
                                sharedViewModel.resetDuplicateCheck()
                            }

                        }
                    }
                }
            }
        }
    }


    private fun initView() {
        viewPagerAdapter = ViewPagerAdapter(this)

        binding.vpOnBoarding.apply {
            adapter = viewPagerAdapter
            isUserInputEnabled = false
        }

        with(binding) {
            btnNext.setOnDebounceClickListener {
                val currentFragment =
                    supportFragmentManager.findFragmentByTag("f${vpOnBoarding.currentItem}") as? CheckRegexFragment
                if (currentFragment?.checkRegex() == true) {

                    when (vpOnBoarding.currentItem) {
                        0 -> {
                            sharedViewModel.checkDuplicate()
                        }

                        2 -> {
                            it.setVisibilityToGone()
                            vpOnBoarding.currentItem++
                            btnReturn.setVisibilityToVisible()
                            btnSignUp.setVisibilityToVisible()
                        }

                        else -> {
                            vpOnBoarding.currentItem++
                            btnReturn.setVisibilityToVisible()
                        }
                    }
                }
            }

            btnReturn.setOnDebounceClickListener {
                vpOnBoarding.currentItem--
                btnSignUp.setVisibilityToGone()
                btnSignIn.setVisibilityToGone()
                btnNext.setVisibilityToVisible()
                if (vpOnBoarding.currentItem == 0) {
                    it.setVisibilityToGone()
                }

            }

            btnSignIn.setOnDebounceClickListener {
                val currentFragment =
                    supportFragmentManager.findFragmentByTag("f${vpOnBoarding.currentItem}") as? CheckRegexFragment
                if (currentFragment?.checkRegex() == true) {
                    lifecycleScope.launch {
                        sharedViewModel.signIn()
                    }
                }
            }

            btnSignUp.setOnDebounceClickListener {
                val currentFragment =
                    supportFragmentManager.findFragmentByTag("f${vpOnBoarding.currentItem}") as? CheckRegexFragment
                if (currentFragment?.checkRegex() == true) {
                    lifecycleScope.launch {
                        sharedViewModel.signUp()
                    }
                }
            }
        }
    }

    private fun launchMainActivity() {
        val intent = Intent(this@OnBoardingActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}