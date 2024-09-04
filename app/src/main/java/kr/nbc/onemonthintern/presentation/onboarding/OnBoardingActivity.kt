package kr.nbc.onemonthintern.presentation.onboarding

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint
import kr.nbc.onemonthintern.R
import kr.nbc.onemonthintern.databinding.ActivityOnBoardingBinding
import kr.nbc.onemonthintern.presentation.onboarding.adpater.ViewPagerAdapter

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {
    private val binding by lazy { ActivityOnBoardingBinding.inflate(layoutInflater) }
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewPagerAdapter = ViewPagerAdapter(this)

        binding.vpOnBoarding.apply {
            adapter = viewPagerAdapter
            isUserInputEnabled = false
        }

    }
}