package mk.ukim.finki.eshop.api

import mk.ukim.finki.eshop.api.dto.PriceRangeDto
import mk.ukim.finki.eshop.api.model.Category
import mk.ukim.finki.eshop.api.model.Product
import retrofit2.Response
import retrofit2.http.*

interface WebServices {
    @GET("/api/categories")
    suspend fun getCategories() : Response<List<Category>>

    @GET("/api/products/category/{categoryId}")
    suspend fun getProductsByCategory(@Path("categoryId") categoryId: Long) : Response<List<Product>>

    @POST("/api/products/priceRange")
    suspend fun getProductsInPriceRange(@Body dto: PriceRangeDto): Response<List<Product>>

    @GET("/api/products/filter-products")
    suspend fun getFilteredProductsForCategory(@Query(value = "categoryId") categoryId: Long, @Query(value = "searchText") searchText: String): Response<List<Product>>
}