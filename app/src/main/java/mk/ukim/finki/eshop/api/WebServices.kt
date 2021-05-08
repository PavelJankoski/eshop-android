package mk.ukim.finki.eshop.api

import mk.ukim.finki.eshop.api.model.Category
import mk.ukim.finki.eshop.api.model.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface WebServices {
    @GET("/api/categories")
    suspend fun getCategories() : Response<List<Category>>

    @GET("/api/products/category/{categoryId}")
    suspend fun getProductsByCategory(@Path("categoryId") categoryId: Long) : Response<List<Product>>
}