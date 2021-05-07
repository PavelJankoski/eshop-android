package mk.ukim.finki.eshop.data.source

import kotlinx.coroutines.flow.Flow
import mk.ukim.finki.eshop.data.dao.CategoriesDao
import mk.ukim.finki.eshop.data.model.CategoriesEntity
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val categoriesDao: CategoriesDao
){
    fun readCategories(): Flow<List<CategoriesEntity>> {
        return categoriesDao.readCategories()
    }

    suspend fun insertCategories(categoriesEntity: CategoriesEntity) {
        return categoriesDao.insertCategories(categoriesEntity)
    }
}