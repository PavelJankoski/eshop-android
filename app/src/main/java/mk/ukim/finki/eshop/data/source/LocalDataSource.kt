package mk.ukim.finki.eshop.data.source

import kotlinx.coroutines.flow.Flow
import mk.ukim.finki.eshop.data.dao.CategoriesDao
import mk.ukim.finki.eshop.data.dao.SearchDao
import mk.ukim.finki.eshop.data.dao.WishlistDao
import mk.ukim.finki.eshop.data.model.CategoriesEntity
import mk.ukim.finki.eshop.data.model.SearchEntity
import mk.ukim.finki.eshop.data.model.WishlistEntity
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val categoriesDao: CategoriesDao,
    private val wishlistDao: WishlistDao,
    private val searchDao: SearchDao
){
    // CATEGORIES
    fun readCategories(): Flow<List<CategoriesEntity>> {
        return categoriesDao.readCategories()
    }

    suspend fun insertCategories(categoriesEntity: CategoriesEntity) {
        return categoriesDao.insertCategories(categoriesEntity)
    }

    // WISHLIST
    fun readWishlistProducts(): Flow<List<WishlistEntity>> {
        return wishlistDao.readWishlistProducts()
    }

    suspend fun isProductInWishlist(id: Int): Int {
        return wishlistDao.isProductInWishlist(id)
    }

    suspend fun insertProductInWishlist(product: WishlistEntity) {
        wishlistDao.insertWishlistProduct(product)
    }

    suspend fun deleteProductFromWishlist(id: Int){
        wishlistDao.deleteProductFromWishlist(id)
    }

    // SEARCH HISTORY
    suspend fun insertSearchText(searchEntity: SearchEntity) {
        return searchDao.insertSearchText(searchEntity)
    }

    fun readSearchHistory(): Flow<List<SearchEntity>> {
        return searchDao.readSearchHistory()
    }

    suspend fun deleteSearchText(id: Int) {
        return searchDao.deleteSearch(id)
    }

    suspend fun deleteAllSearchHistory() {
        return searchDao.deleteAllSearchHistory()
    }
}