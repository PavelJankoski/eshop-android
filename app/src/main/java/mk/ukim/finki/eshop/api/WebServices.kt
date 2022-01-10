package mk.ukim.finki.eshop.api

import mk.ukim.finki.eshop.api.dto.*
import mk.ukim.finki.eshop.api.dto.request.*
import mk.ukim.finki.eshop.api.dto.response.LoginDto
import mk.ukim.finki.eshop.api.model.*
import mk.ukim.finki.eshop.util.Constants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface WebServices {
    @GET("/api/product-catalog-service/categories")
    suspend fun getCategories() : Response<List<Category>>

    @GET("/api/product-catalog-service/products/{categoryId}")
    suspend fun getProductsByCategory(
        @Path(Constants.CATEGORY_ID_PARAM) categoryId: Long,
        @Query(Constants.USER_ID_PARAM) userId: Long
    ) : Response<List<Product>>

    @POST("/api/product-catalog-service/products/filter")
    suspend fun getFilteredProductsForCategory(
        @Body dto: FilterProductDto,
        @Query(value = Constants.USER_ID_PARAM) userId: Long
    ): Response<List<Product>>

    @GET("/api/product-catalog-service/products/search")
    suspend fun getSearchedProducts(
        @Query(value = Constants.SEARCH_TEXT_PARAM) searchText: String,
        @Query(value = Constants.USER_ID_PARAM) userId: Long
    ): Response<List<Product>>

    @GET("/api/product-catalog-service/products/code/{code}")
    suspend fun getProductByProductCode(
        @Path(value = Constants.CODE_PARAM) productCode: String,
        @Query(value = Constants.USER_ID_PARAM) userId: Long
    ): Response<Product>

    @POST("/api/users-api-gateway/oauth/token")
    suspend fun loginUser(
        @Body body: RequestBody
    ): Response<LoginDto>

    @POST("/api/users-api-gateway/users/oauth/google")
    suspend fun loginWithGoogle(
        @Body body: TokenDto
    ): Response<LoginDto>

    @POST("/api/users-api-gateway/users/oauth/facebook")
    suspend fun loginWithFacebook(
        @Body body: TokenDto
    ): Response<LoginDto>

    @POST("/api/users-api-gateway/users/register")
    suspend fun registerUser(@Body dto: RegisterDto): Response<Void>

    @GET("/api/users-api-gateway/users/{userId}")
    suspend fun getUserInfo(
        @Path(value = Constants.USER_ID_PARAM) userId: Long,
    ): Response<User>

    @Multipart
    @PATCH("/api/users-api-gateway/users/{userId}")
    suspend fun updateUserInfo(
        @Path(value = Constants.USER_ID_PARAM) userId: Long,
        @Part image: MultipartBody.Part?,
        @Part("name") name: RequestBody,
        @Part("surname") surname: RequestBody,
        @Part("phoneNumber") phoneNumber: RequestBody
    ): Response<Unit>

    @GET("/api/users-api-gateway/addresses/{userId}")
    suspend fun getAddressesForUser(
        @Path(value = Constants.USER_ID_PARAM) userId: Long,
    ): Response<List<Address>>

    @POST("/api/users-api-gateway/addresses/{userId}")
    suspend fun createAddressesForUser(
        @Path(value = Constants.USER_ID_PARAM) userId: Long,
        @Body body: CreateEditAddressDto
    ): Response<Address>

    @PATCH("/api/users-api-gateway/addresses/{addressId}/users/{userId}")
    suspend fun editAddressesForUser(
        @Path(value = Constants.ADDRESS_ID_PARAM) addressId: Long,
        @Path(value = Constants.USER_ID_PARAM) userId: Long,
        @Body body: CreateEditAddressDto
    ): Response<Address>

    @PATCH("/api/users-api-gateway/addresses/delete/{addressId}")
    suspend fun deleteAddress(
        @Path(value = Constants.ADDRESS_ID_PARAM) addressId: Long,
    ): Response<Unit>

    @GET("/api/product-catalog-service/wishlists/{userId}")
    suspend fun getWishlistProductsForUser(
        @Path(value = Constants.USER_ID_PARAM) userId: Long
    ) : Response<List<Product>>

    @PATCH("/api/product-catalog-service/wishlists/add/{productId}/users/{userId}")
    suspend fun addProductToWishlistForUser(
        @Path(value = Constants.PRODUCT_ID_PARAM) productId: Long,
        @Path(value = Constants.USER_ID_PARAM) userId: Long
    ) : Response<Long>

    @PATCH("/api/product-catalog-service/wishlists/remove/{productId}/users/{userId}")
    suspend fun removeProductFromWishlistForUser(
        @Path(value = Constants.PRODUCT_ID_PARAM) productId: Long,
        @Path(value = Constants.USER_ID_PARAM) userId: Long
    ) : Response<Long>

    @GET("/api/product-catalog-service/reviews/{productId}")
    suspend fun getReviewsForProduct(
        @Path(value = Constants.PRODUCT_ID_PARAM) productId: Long
    ) : Response<List<Review>>

    @GET("/api/product-catalog-service/reviews/by-rating/{productId}")
    suspend fun getReviewsByRatingForProduct(
        @Path(value = Constants.PRODUCT_ID_PARAM) productId: Long,
        @Query(value = Constants.RATING_PARAM) rating: Float
    ) : Response<List<Review>>

    @GET("/api/order-management-service/orders/{userId}")
    suspend fun getOrderItemsForUser(
        @Path(value = Constants.USER_ID_PARAM) userId: Long,
    ) : Response<List<OrderItem>>

    @POST("/api/order-management-service/order-items/add-to-order")
    suspend fun addProductToBag(
        @Body body: AddProductToBagDto
    ) : Response<Unit>

    @PATCH("/api/order-management-service/order-items/remove-from-order")
    suspend fun removeProductFromBag(
        @Body body: RemoveProductFromBagDto
    ) : Response<Unit>

}