package kr.nbc.onemonthintern.presentation.onboarding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.nbc.onemonthintern.R
import kr.nbc.onemonthintern.databinding.ActivityLogInSelectBinding
import kr.nbc.onemonthintern.presentation.main.MainActivity
import kr.nbc.onemonthintern.presentation.util.makeShortToast
import kr.nbc.onemonthintern.presentation.util.setOnDebounceClickListener

@AndroidEntryPoint
class LogInSelectActivity : AppCompatActivity() {
    private val viewModel: LogInSelectViewModel by viewModels()
    private lateinit var googleSignInClient: GoogleSignInClient
    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogleToken(account.idToken!!)
            } catch (e: ApiException) {
                makeShortToast("구글 로그인 실패")
            }

        }

    private val binding by lazy { ActivityLogInSelectBinding.inflate(layoutInflater) }
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
        observeLogInComplete()
        initGoogleAuth()
    }

    private fun initView() {
        binding.btnEmailLogIn.setOnDebounceClickListener {
            launchOnBoardingActivity()
        }

        binding.ivGoogleLogIn.setOnDebounceClickListener {
            googleLogIn()
        }
    }

    private fun observeLogInComplete() {
        lifecycleScope.launch {
            viewModel.isGoogleLogInSuccess.collectLatest { result ->
                if (result) {
                    makeShortToast("구글 로그인에 성공했습니다.")
                    launchMainActivity()
                }
            }
        }
    }

    private fun launchMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun launchOnBoardingActivity() {
        val intent = Intent(this, OnBoardingActivity::class.java)
        startActivity(intent)
    }

    private fun initGoogleAuth() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(getString(R.string.google_client_key))
            .requestIdToken(getString(R.string.google_client_key))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun googleLogIn() {
        val googleIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(googleIntent)
    }

    private fun firebaseAuthWithGoogleToken(token: String) {
        lifecycleScope.launch {
            viewModel.googleLogIn(token)
        }
    }

}