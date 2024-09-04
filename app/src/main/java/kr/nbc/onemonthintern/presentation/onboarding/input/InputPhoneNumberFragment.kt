package kr.nbc.onemonthintern.presentation.onboarding.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import kr.nbc.onemonthintern.databinding.FragmentInputPhoneNumberBinding
import kr.nbc.onemonthintern.presentation.onboarding.OnBoardingSharedViewModel
import kr.nbc.onemonthintern.presentation.util.checkPasswordRegex
import kr.nbc.onemonthintern.presentation.util.checkPhoneNumberRegex
import kr.nbc.onemonthintern.presentation.util.makeShortToast

@AndroidEntryPoint
class InputPhoneNumberFragment : Fragment(), CheckRegexFragment {
    private var _binding: FragmentInputPhoneNumberBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: OnBoardingSharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputPhoneNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun checkRegex(): Boolean {
        val phoneNumber = binding.etPhoneNumber.text.toString()
        val result = phoneNumber.checkPhoneNumberRegex()

        if (!result) makeShortToast("잘못된 전화번호 형식입니다.")
        else sharedViewModel.inputPhoneNumber(phoneNumber)

        return result
    }
}