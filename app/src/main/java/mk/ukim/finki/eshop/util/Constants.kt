package mk.ukim.finki.eshop.util

class Constants {
    companion object {
        const val PRODUCT_RESULT_KEY = "productBundle"

        // ROOM Database
        const val DATABASE_NAME = "clothing_eshop_db"
        const val CATEGORY_TABLE = "categories"
        const val WISHLIST_TABLE = "wishlist"
        const val SEARCH_TABLE = "search_history"

        // DATA STORE - PREFERENCE KEYS
        const val PREFERENCE_NAME = "eshop-preferences"
        const val PREFERENCE_JSON_WEB_TOKEN = "jwt"
        const val PREFERENCE_USER_ID = "userId"

        // ENCRYPTED SHARED PREFERENCES
        const val ENCRYPTED_SHARED_PREFERENCES_NAME = "secure-storage_eshop"

        //DEFAULT VALUES
        const val DEFAULT_JWT = "1111111111111111111111111111111111111"
        const val DEFAULT_USER_ID = -1

        // STATE KEYS
        const val LOGIN_STATE = "authenticated"

        // Google sign in
        const val GOOGLE_TYPE = "google"

        // NOTE: CLIENT ID FOR WEB APP SAME ON BACKEND AND IN APP!!!!!!!!!!!!
        const val GOOGLE_CLIENT_ID = "467495057025-lqpkn4mpl6bor6b6ga9d0hdo68prmvi4.apps.googleusercontent.com"

        // Stripe publishable key
        const val STRIPE_PUBLISHABLE_KEY = "pk_test_51H0AUpKlrDn4L6TppUjWu90nNVVYxLPfCctgG9DoaMIdMB4SSGbM4SIb1vvxldSyzw7f9k2TRGnZ2OnHYJFGXEO300ZAVYUir2"

        // RETROFIT PARAMS
        const val CATEGORY_ID_PARAM = "categoryId"
        const val USER_ID_PARAM = "userId"
        const val SEARCH_TEXT_PARAM = "searchText"
        const val CODE_PARAM = "code"
        const val USERNAME_PARAM = "username"
        const val GRANT_TYPE_PARAM = "grant_type"
        const val PASSWORD_PARAM = "password"
        const val CLIENT_ID_PARAM = "client_id"
        const val CLIENT_SECRET_PARAM = "client_secret"

    }
}