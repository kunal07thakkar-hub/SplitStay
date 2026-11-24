
package com.splitstay.app.navigation

import kotlinx.serialization.Serializable

@Serializable
data object Splash

@Serializable
data object RoleSelection

// Navigation graph routes
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

// Dashboard destinations
@Serializable
data object StudentHome

@Serializable
data object OwnerHome
