package kr.ac.uc.test_2025_05_19_k.model.request

data class GroupCreateRequest(
    val title: String,
    val description: String,
    val requirements: String,
    val interest: String,
    val maxMembers: Int,
    val locationName: String
)