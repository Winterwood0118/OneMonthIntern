package kr.nbc.onemonthintern.presentation.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint
import kr.nbc.onemonthintern.R
import kr.nbc.onemonthintern.databinding.ActivityMainBinding
import kr.nbc.onemonthintern.presentation.main.home.HomeFragment
import kr.nbc.onemonthintern.presentation.onboarding.OnBoardingActivity

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val sharedViewModel: MainSharedViewModel by viewModels()
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
        initFragment()
    }

    private fun initFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fcMain, HomeFragment())
            .addToBackStack("home")
            .commit()
    }


    private fun checkLogin(){
        Log.d("Check Login", sharedViewModel.currentUserUid.toString())

        if(sharedViewModel.currentUserUid == null) {
            val intent = Intent(this, OnBoardingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}