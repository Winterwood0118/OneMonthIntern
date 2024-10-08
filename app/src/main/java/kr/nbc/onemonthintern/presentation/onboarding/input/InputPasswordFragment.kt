package kr.nbc.onemonthintern.presentation.onboarding.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import kr.nbc.onemonthintern.databinding.FragmentInputPasswordBinding
import kr.nbc.onemonthintern.presentation.onboarding.OnBoardingSharedViewModel
import kr.nbc.onemonthintern.presentation.util.checkPasswordRegex
import kr.nbc.onemonthintern.presentation.util.makeShortToast

@AndroidEntryPoint
class InputPasswordFragment : Fragment(), CheckRegexFragment {
    private var _binding: FragmentInputPasswordBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: OnBoardingSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun checkRegex(): Boolean {
        val password = binding.etPassword.text.toString()
        val result = password.checkPasswordRegex()

        if (!result) makeShortToast("잘못된 비밀번호 형식입니다.(6 ~ 12자 영문, 숫자)")
        else sharedViewModel.inputPassword(password)

        return result
    }
}