package kr.nbc.onemonthintern.presentation.onboarding.adpater

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.nbc.onemonthintern.presentation.onboarding.input.InputEmailFragment
import kr.nbc.onemonthintern.presentation.onboarding.input.InputNameFragment
import kr.nbc.onemonthintern.presentation.onboarding.input.InputPasswordFragment
import kr.nbc.onemonthintern.presentation.onboarding.input.InputPhoneNumberFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private val fragmentList = mutableListOf(
        InputEmailFragment(),
        InputPasswordFragment(),
        InputNameFragment(),
        InputPhoneNumberFragment()
    )

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position] as Fragment
    }
}