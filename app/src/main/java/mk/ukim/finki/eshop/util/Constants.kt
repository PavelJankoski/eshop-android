package mk.ukim.finki.eshop.util

class Constants {
    companion object {
        const val PRODUCT_RESULT_KEY = "productBundle"

        const val BASE_URL = "http://192.168.6.30:8080"

        // ROOM Database
        const val DATABASE_NAME = "clothing_eshop_db"
        const val CATEGORY_TABLE = "categories"
        const val WISHLIST_TABLE = "wishlist"
        const val SEARCH_TABLE = "search_history"

        const val SEARCH_HISTORY_EXTRAS = "search_history_extras"
        const val QR_CODE_PRODUCT_DETAILS_EXTRAS = "qr_code_product_details_extras"
        const val PRODUCT_CODE_EXTRAS = "product_code_extras"
    }
}