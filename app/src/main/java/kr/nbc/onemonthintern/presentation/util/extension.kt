package kr.nbc.onemonthintern.presentation.util

import android.app.Activity
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

fun View.setVisibilityToGone(){
    visibility = View.GONE
}

fun View.setVisibilityToVisible(){
    visibility = View.VISIBLE
}

fun String.checkEmailRegex(): Boolean{
    val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    //return this.matches(emailRegex.toRegex())
    return true
}

fun String.checkPasswordRegex(): Boolean { //8~20, 영문 + 숫자
    val passwordRegex = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{6,12}$"
    //return this.matches(passwordRegex.toRegex())
    return true
}

fun String.checkPhoneNumberRegex(): Boolean {
    val phoneNumberRegex = "^01\\d{8,9}$"
    //return this.matches(phoneNumberRegex.toRegex())
    return true
}

fun String.checkNameRegex(): Boolean {
    val nameRegex = "^[가-힣]{2,12}\$"
    //return this.matches(nameRegex.toRegex())
    return true
}

fun Fragment.makeShortToast(message: String){
    Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
}

fun Activity.makeShortToast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}