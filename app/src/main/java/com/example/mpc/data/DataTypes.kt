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
    val email: String = "",
)

data class CaseData(
    val id: String = "",
    val usn: String = "",
    val name: String = "",
    val date: String = "",
    val time: String = "",
    val nature: String = "",
    val courseCode: String = "",
    val superintendentName: String = "",
    val superintendentContactNo: String = "",
    val superintendentDepartment: String = "",
    val squadMemberId: String = "",
    val status: String = "Pending",
    val studentStatement: String = "",
    val squadMemberStatement: String = "",
    val proofImages: List<String> = emptyList()
)