package list.utils

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.localizedStringForCountryCode

actual fun getCountryNameFromCode(countryCode: String): String {
    val locale = NSLocale.currentLocale
    return locale.localizedStringForCountryCode(countryCode) ?: countryCode
}