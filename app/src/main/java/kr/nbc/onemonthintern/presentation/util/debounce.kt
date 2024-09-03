package kr.nbc.onemonthintern.presentation.util

import android.os.Handler
import android.os.Looper
import android.view.View

fun View.setOnDebounceClickListener(
    debounceTime: Long = 500L,
    unit: (View) -> Unit
) { // 입력값(시간)을 이용해 첫 버튼 클릭부터 입력값보다 짧은 간격의 모든 클릭 무시
    var lastClickTime = 0L
    val handler = Handler(Looper.getMainLooper())

    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > debounceTime) {
            lastClickTime = currentTime
            handler.post {
                unit(it)
            }
        }
    }
}