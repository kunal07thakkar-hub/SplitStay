
package com.splitstay.app

import android.content.Context
import com.splitstay.app.navigation.AppNavigation
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.google.firebase.FirebaseApp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.splitstay.app.ui.theme.SplitStayTheme
import com.splitstay.app.ui.theme.SecondaryText
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private lateinit var connectivityManager: ConnectivityManager
    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    private val internetAvailable = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        internetAvailable.value = checkInternet()

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                runOnUiThread {
                    internetAvailable.value = checkInternet()
                }
            }

            override fun onLost(network: Network) {
                runOnUiThread {
                    internetAvailable.value = checkInternet()
                }
            }

            override fun onCapabilitiesChanged(
                network: Network,
                capabilities: NetworkCapabilities
            ) {
                runOnUiThread {
                    internetAvailable.value = checkInternet()
                }
            }
        }

        networkCallback = callback

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)
        FirebaseApp.initializeApp(this)
        setContent {
            SplitStayTheme {
                AppNavigation(
                    internetAvailable = internetAvailable.value
                )
            }
        }
    }

    private fun checkInternet(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_VALIDATED
        )
    }

    override fun onDestroy() {
        networkCallback?.let {
            connectivityManager.unregisterNetworkCallback(it)
        }
        super.onDestroy()
    }
}


@Composable
fun SplitStaySplashScreen(
    internetAvailable: Boolean,
    onContinue: () -> Unit
) {
    LaunchedEffect(internetAvailable) {
        if (internetAvailable) {
            delay(2200)
            onContinue()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.splitstay_splash),
            contentDescription = "SplitStay Logo and Tagline",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 90.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (internetAvailable) {
                Text(
                    text = "Connecting with SplitStay",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(12.dp))

                LoadingDots()
            } else {
                Text(
                    text = "Please connect to the Internet",
                    color = SecondaryText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Waiting for connection...",
                    color = SecondaryText,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun LoadingDots() {
    val transition = rememberInfiniteTransition(label = "loading")

    val dot1 by transition.animateFloat(
        initialValue = 0.25f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot1"
    )

    val dot2 by transition.animateFloat(
        initialValue = 0.25f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot2"
    )

    val dot3 by transition.animateFloat(
        initialValue = 0.25f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 400),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot3"
    )

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .alpha(dot1)
                .background(
                    MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.small
                )
        )
        Box(
            modifier = Modifier
                .size(8.dp)
                .alpha(dot2)
                .background(
                    MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.small
                )
        )
        Box(
            modifier = Modifier
                .size(8.dp)
                .alpha(dot3)
                .background(
                    MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.small
                )
        )
    }
}

@Composable
fun SplitStayNextScreen() {
    RoleSelectionScreen(
        onStudentClick = {
            // Student login will be added next
        },
        onOwnerClick = {
            // Property owner login will be added next
        }
    )
}
