package list.utils

actual fun getCountryNameFromCode(countryCode: String): String {
    return java.util.Locale("", countryCode).displayCountry
}