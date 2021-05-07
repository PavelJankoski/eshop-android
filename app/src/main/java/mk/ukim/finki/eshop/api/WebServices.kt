package mk.ukim.finki.eshop.api

import mk.ukim.finki.eshop.api.model.Category
import retrofit2.Response
import retrofit2.http.GET

interface WebServices {
    @GET("/api/categories")
    suspend fun getCategories() : Response<List<Category>>
}