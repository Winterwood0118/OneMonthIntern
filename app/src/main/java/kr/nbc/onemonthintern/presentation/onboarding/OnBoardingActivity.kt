package kr.nbc.onemonthintern.presentation.onboarding

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint
import kr.nbc.onemonthintern.R
import kr.nbc.onemonthintern.databinding.ActivityOnBoardingBinding
import kr.nbc.onemonthintern.presentation.onboarding.adpater.ViewPagerAdapter
import kr.nbc.onemonthintern.presentation.onboarding.input.CheckRegexFragment
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

    }

    private fun initView(){
        viewPagerAdapter = ViewPagerAdapter(this)

        binding.vpOnBoarding.apply {
            adapter = viewPagerAdapter
            isUserInputEnabled = false
        }

        with(binding){
            btnNext.setOnClickListener {
                val currentFragment = supportFragmentManager.findFragmentByTag("f${vpOnBoarding.currentItem}") as? CheckRegexFragment
                if (currentFragment?.checkRegex() == true){
                    vpOnBoarding.currentItem ++
                    if (vpOnBoarding.currentItem == 1 && sharedViewModel.isDuplicated){
                        it.setVisibilityToGone()
                        btnSignIn.setVisibilityToVisible()
                        btnReturn.setVisibilityToVisible()
                    }

                    if (vpOnBoarding.currentItem == 3 && sharedViewModel.isDuplicated){
                        it.setVisibilityToGone()
                        btnSignUp.setVisibilityToVisible()
                        btnReturn.setVisibilityToVisible()
                    }
                }
            }
        }
    }
}