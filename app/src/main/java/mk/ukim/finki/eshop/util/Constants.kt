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
        const val PREFERENCE_EMAIL = "email"

        // STATE KEYS
        const val LOGIN_STATE = "authenticated"

        // Sign in type
        const val GOOGLE_TYPE = "google"
        const val FACEBOOK_TYPE = "facebook"

        // Stripe publishable key
        const val STRIPE_PUBLISHABLE_KEY =
            "pk_test_51KNnzhJzZYu9pfEvZ1eZzMQ1raUfrs0nUa9ayGlefRCuJVrXHBsgitAWYj09NAXjK5GLOKONlz06h2af4VCwLpmv00fDOW7BQN"

        // RETROFIT PARAMS
        const val CATEGORY_ID_PARAM = "categoryId"
        const val PRODUCT_ID_PARAM = "productId"
        const val USER_ID_PARAM = "userId"
        const val ORDER_ID_PARAM = "orderId"
        const val ADDRESS_ID_PARAM = "addressId"
        const val SEARCH_TEXT_PARAM = "searchText"
        const val CODE_PARAM = "code"
        const val USERNAME_PARAM = "username"
        const val RATING_PARAM = "rating"
        const val GRANT_TYPE_PARAM = "grant_type"
        const val PASSWORD_PARAM = "password"
        const val CLIENT_ID_PARAM = "client_id"
        const val CLIENT_SECRET_PARAM = "client_secret"
    }
}