package kr.nbc.onemonthintern.presentation.onboarding.adpater

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.nbc.onemonthintern.presentation.onboarding.input.InputEmailFragment
import kr.nbc.onemonthintern.presentation.onboarding.input.InputPasswordFragment

class ViewPagerAdapter(fragment:Fragment): FragmentStateAdapter(fragment) {
    private val fragmentList = listOf(
        InputEmailFragment(),
        InputPasswordFragment(),

    )

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun createFragment(position: Int): Fragment {
        TODO("Not yet implemented")
    }
}