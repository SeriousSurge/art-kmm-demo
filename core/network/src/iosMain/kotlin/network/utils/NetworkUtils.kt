package network.utils

import platform.SystemConfiguration.SCNetworkReachabilityCreateWithName
import platform.SystemConfiguration.SCNetworkReachabilityFlags
import platform.SystemConfiguration.SCNetworkReachabilityGetFlags

//actual fun isNetworkAvailable(): Boolean {
//    val reachability = SCNetworkReachabilityCreateWithName(null, "www.google.com")
//    val flags = cValue<SCNetworkReachabilityFlags>()
//    if (SCNetworkReachabilityGetFlags(reachability, flags)) {
//        return flags.readValue().toInt() and SCNetworkReachabilityFlags.kSCNetworkReachabilityFlagsReachable != 0
//    }
//    return false
//}