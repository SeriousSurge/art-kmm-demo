package network.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

//actual fun isNetworkAvailable(): Boolean {
//    val context: Context by inject()
//    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//    val network = connectivityManager.activeNetwork ?: return false
//    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
//    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
//}