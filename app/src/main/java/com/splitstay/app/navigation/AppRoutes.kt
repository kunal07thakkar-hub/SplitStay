
package com.splitstay.app.navigation

import kotlinx.serialization.Serializable

@Serializable
data object Splash

@Serializable
data object RoleSelection

// Navigation graphs
@Serializable
data object AuthGraph

@Serializable
data object StudentGraph

@Serializable
data object OwnerGraph

// Authentication destinations
@Serializable
data object StudentAuth

@Serializable
data object OwnerAuth

@Serializable
data object EmailVerification

// Dashboard destinations
@Serializable
data object StudentHome

@Serializable
data object OwnerHome

// Owner onboarding destinations
@Serializable
data object OwnerKycRequired

@Serializable
data object OwnerPendingApproval
