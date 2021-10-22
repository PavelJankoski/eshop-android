package mk.ukim.finki.eshop.api

import mk.ukim.finki.eshop.api.dto.*
import mk.ukim.finki.eshop.api.model.*
import retrofit2.Response
import retrofit2.http.*

interface WebServices {
    @GET("/api/product-catalog-service/categories")
    suspend fun getCategories() : Response<List<Category>>

    @GET("/api/product-catalog-service/products/{categoryId}")
    suspend fun getProductsByCategory(
        @Path("categoryId") categoryId: Long,
        @Query(value = "userId") userId: Long
    ) : Response<List<Product>>

    @POST("/api/product-catalog-service/products/filter")
    suspend fun getFilteredProductsForCategory(
        @Body dto: PriceRangeDto,
        @Query(value = "userId") userId: Long
    ): Response<List<Product>>

    @GET("/api/product-catalog-service/products/search")
    suspend fun getSearchedProducts(
        @Query(value = "searchText") searchText: String,
        @Query(value = "userId") userId: Long
    ): Response<List<Product>>

    @GET("/api/product-catalog-service/products/code/{code}")
    suspend fun getProductByProductCode(
        @Path("code") productCode: String,
        @Query(value = "userId") userId: Long
    ): Response<Product>

    @POST("/api/users")
    suspend fun registerUser(@Body dto: RegisterDto)

    @POST("/api/authenticate")
    suspend fun login(@Body dto: LoginDto): Response<AuthResponse>

    @POST("/api/authenticate/social-login/facebook")
    suspend fun loginWithFacebook(@Body dto: TokenDto): Response<AuthResponse>

    @POST("/api/authenticate/social-login/google")
    suspend fun loginWithGoogle(@Body dto: TokenDto): Response<AuthResponse>

    @GET("/api/users/{userId}")
    suspend fun getUserInfo(
        @Path("userId") userId: Long
    ): Response<User>

    @GET("/api/users/exists/{username}")
    suspend fun existsUsername(
        @Path("username") username: String
    ): Response<Boolean>

    @GET("/api/shopping-cart/exists-active/{userId}")
    suspend fun userHaveActiveShoppingCart(
        @Path("userId") userId: Long
    ): Response<Boolean>

    @GET("/api/shopping-cart/get-active/{userId}")
    suspend fun getActiveShoppingCart(
        @Path("userId") userId: Long
    ): Response<ShoppingCart>

    @GET("/api/shopping-cart/products-fixed-order/{userId}")
    suspend fun getCartItems(
        @Path("userId") userId: Long
    ): Response<List<CartItem>>

    @PATCH("/api/shopping-cart/add-product/{productId}/{userId}/{copies}")
    suspend fun addProductToShoppingCart(
        @Path("productId") productId: Long,
        @Path("userId") userId: Long,
        @Path("copies") copies: Int
    ): Response<ShoppingCart>

    @GET("/api/shopping-cart/is-in-shopping-cart/{userId}/{productId}")
    suspend fun isInShoppingCart(
        @Path("productId") productId: Long,
        @Path("userId") userId: Long
    ): Response<Boolean>

    @PATCH("/api/shopping-cart/remove-product/{productId}/{userId}")
    suspend fun removeFromShoppingCart(
        @Path("productId") productId: Long,
        @Path("userId") userId: Long
    ): Response<ShoppingCart>

    @GET("/api/shopping-cart/fav-cart/{productId}/{userId}")
    suspend fun isFavAndInCart(
        @Path("productId") productId: Long,
        @Path("userId") userId: Long
    ): Response<FavCartDto>


    @GET("/api/users/add-fave/{userId}/{productId}")
    suspend fun addProductToWishlist(
        @Path("userId") userId: Long,
        @Path("productId") productId: Long
    ): Response<Void>

    @GET("/api/users/remove-fave/{userId}/{productId}")
    suspend fun removeProductFromWishlist(
        @Path("userId") userId: Long,
        @Path("productId") productId: Long
    ): Response<Void>

    @GET("/api/users/all-fave/{userId}")
    suspend fun getAllProductsInWishlist(
        @Path("userId") userId: Long
    ): Response<List<Product>>

    @POST("/api/strpe-mobile-endpoint/payment-sheet/{amount}")
    suspend fun getPaymentSheetParams(
        @Path("amount") amount: Int
    ): Response<Map<String, String>>

    @GET("/api/shopping-cart/all/{userId}")
    suspend fun getShoppingCarts(
        @Path("userId") userId: Long
    ): Response<List<ShoppingCart>>

    @GET("/api/users/{userId}")
    suspend fun getUser(
        @Path("userId") userId: Long
    ): Response<User>

}