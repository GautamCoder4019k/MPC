package com.example.mpc.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object Login

@Serializable
data object Home

@Serializable
data object AddStudent

@Serializable
data object AddSquadMember

@Serializable
data object AdminHome

@Serializable
data object Record

@Serializable
data object Filter

@Serializable
data object AddCase

@Serializable
data object AddRemainingDetails

@Serializable
data object AddProof

@Serializable
data class CaseDetail(val id: String = "")