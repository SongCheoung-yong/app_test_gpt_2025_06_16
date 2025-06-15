package kr.ac.uc.test_2025_05_19_k.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

// 예시: InterestSelectViewModel.kt
class InterestSelectViewModel : ViewModel() {
    var userName by mutableStateOf("")
    var gender by mutableStateOf("")
    var phone by mutableStateOf("")
    var birth by mutableStateOf("")

    fun setUserInfo(name: String, gender: String, phone: String, birth: String) {
        this.userName = name
        this.gender = gender
        this.phone = phone
        this.birth = birth
    }
}
