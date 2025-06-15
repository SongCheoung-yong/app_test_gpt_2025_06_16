package kr.ac.uc.test_2025_05_19_k.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RegionSettingViewModel : ViewModel() {
    // 지역 설정 완료 플래그
    private val _isRegionSet = MutableStateFlow(false)
    val isRegionSet: StateFlow<Boolean> get() = _isRegionSet

    // 지역 설정 완료 시 호출
    fun setRegionSet(value: Boolean) {
        _isRegionSet.value = value
    }

    // 네비게이션 이후 반드시 호출(반복 방지)
    fun resetRegionSet() {
        _isRegionSet.value = false
    }
}
