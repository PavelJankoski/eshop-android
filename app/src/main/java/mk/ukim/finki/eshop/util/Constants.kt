package mk.ukim.finki.eshop.util

class Constants {
    companion object {
        const val PRODUCT_RESULT_KEY = "productBundle"

        // ROOM Database
        const val DATABASE_NAME = "clothing_eshop_db"
        const val CATEGORY_TABLE = "categories"
        const val WISHLIST_TABLE = "wishlist"
        const val SEARCH_TABLE = "search_history"

        // ENCRYPTED SHARED PREFERENCES
        const val ENCRYPTED_SHARED_PREFERENCES_NAME = "secure-storage_eshop"
        const val PREFERENCE_NAME = "eshop-preferences"
        const val PREFERENCE_TOKEN = "token"
        const val PREFERENCE_USER_ID = "userId"
        const val PREFERENCE_FULL_NAME = "fullName"
        const val PREFERENCE_EMAIL = "email"
        const val PREFERENCE_IMAGE_URL = "imageUrl"

        // STATE KEYS
        const val LOGIN_STATE = "authenticated"

        // Google sign in
        const val GOOGLE_TYPE = "google"

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