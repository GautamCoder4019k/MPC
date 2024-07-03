package com.example.mpc.data

data class UserData(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val phoneNo: String = "",
    val department: String = ""
)

data class StudentData(
    val usn: String = "",
    val name: String = "",
    val branch: String = "",
    val sem: String = "",
    val contactNo: String = "",
    val email: String = ""
)