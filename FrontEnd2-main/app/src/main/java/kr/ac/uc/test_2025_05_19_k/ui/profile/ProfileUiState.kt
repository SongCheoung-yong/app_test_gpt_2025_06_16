package kr.ac.uc.test_2025_05_19_k.ui.profile

data class ProfileUiState(
    val name: String = "",
    val gender: String = "",
    val phoneNumber: String = "",
    val birthYear: Int = 2000,
    val errorMessage: String? = null
)

data class ProfileState(

    val name: String = "",
    val gender: String = "",
    val phone: String = "",
    val birth: String = "",
    val location: String = "",
    val interests: List<String> = emptyList()
)

