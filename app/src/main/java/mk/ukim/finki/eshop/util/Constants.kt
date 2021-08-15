package mk.ukim.finki.eshop.util

class Constants {
    companion object {
        const val PRODUCT_RESULT_KEY = "productBundle"

        const val BASE_URL = "http://192.168.1.108:8080"

        // ROOM Database
        const val DATABASE_NAME = "clothing_eshop_db"
        const val CATEGORY_TABLE = "categories"
        const val WISHLIST_TABLE = "wishlist"
        const val SEARCH_TABLE = "search_history"

        const val SEARCH_HISTORY_EXTRAS = "search_history_extras"
        const val QR_CODE_PRODUCT_DETAILS_EXTRAS = "qr_code_product_details_extras"
        const val PRODUCT_CODE_EXTRAS = "product_code_extras"


        // DATA STORE - PREFERENCE KEYS
        const val PREFERENCE_NAME = "eshop-preferences"
        const val PREFERENCE_JSON_WEB_TOKEN = "jwt"
        const val PREFERENCE_USER_ID = "userId"
        const val PREFERENCE_SHOPPING_CART_ID = "shoppingCartId"

        //DEFAULT VALUES
        const val DEFAULT_JWT = "1111111111111111111111111111111111111"
        const val DEFAULT_USER_ID = -1
        const val DEFAULT_SHOPPING_CART_ID = -1

        // STATE KEYS
        const val LOGIN_STATE = "authenticated"

        // Google sign in
        const val GOOGLE_TYPE = "google"
        // NOTE: CLIENT ID FOR WEB APP SAME ON BACKEND AND IN APP!!!!!!!!!!!!
        const val GOOGLE_CLIENT_ID = "467495057025-lqpkn4mpl6bor6b6ga9d0hdo68prmvi4.apps.googleusercontent.com"

        // NO INTERNET CONNECTION
        const val NO_INTERNET_CONNECTION_ERROR_MESSAGE = "Please check your internet connection..."
    }
}