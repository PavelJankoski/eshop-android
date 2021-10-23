package mk.ukim.finki.eshop.data.source

import mk.ukim.finki.eshop.api.WebServices
import mk.ukim.finki.eshop.api.dto.*
import mk.ukim.finki.eshop.api.dto.request.FilterProductDto
import mk.ukim.finki.eshop.api.dto.request.RegisterDto
import mk.ukim.finki.eshop.api.dto.response.LoginDto
import mk.ukim.finki.eshop.api.model.*
import okhttp3.RequestBody
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
        private val webServices: WebServices
){
        suspend fun getCategories(): Response<List<Category>> {
                return webServices.getCategories()
        }

        suspend fun getProductsByCategory(categoryId: Long, userId: Long): Response<List<Product>> {
                return webServices.getProductsByCategory(categoryId, userId)
        }

        suspend fun getFilteredProductsForCategory(dto: FilterProductDto, userId: Long): Response<List<Product>> {
                return webServices.getFilteredProductsForCategory(dto, userId)
        }

        suspend fun getSearchedProducts(searchText: String, userId: Long): Response<List<Product>> {
                return webServices.getSearchedProducts(searchText, userId)
        }

        suspend fun getProductByProductCode(productCode: String, userId: Long): Response<Product> {
                return webServices.getProductByProductCode(productCode, userId)
        }

        suspend fun loginUser(body: RequestBody): Response<LoginDto> {
                return webServices.loginUser(body)
        }

        suspend fun registerUser(dto: RegisterDto): Response<Void> {
                return webServices.registerUser(dto)
        }

//        suspend fun login(dto: LoginDto): Response<AuthResponse> {
//                return webServices.login(dto)
//        }
//
//        suspend fun loginWithFacebook(dto: TokenDto): Response<AuthResponse> {
//                return webServices.loginWithFacebook(dto)
//        }
//
//        suspend fun loginWithGoogle(dto: TokenDto): Response<AuthResponse> {
//                return webServices.loginWithGoogle(dto)
//        }

        suspend fun userWithUsernameExist(username: String): Response<Boolean> {
                return webServices.existsUsername(username)
        }

        suspend fun userHasActiveShoppingCart(userId: Long): Response<Boolean> {
                return webServices.userHaveActiveShoppingCart(userId)
        }

        suspend fun getActiveShoppingCart(userId: Long): Response<ShoppingCart> {
                return  webServices.getActiveShoppingCart(userId)
        }

        suspend fun getCartItems(userId: Long): Response<List<CartItem>> {
                return webServices.getCartItems(userId)
        }

        suspend fun addProductToShoppingCart(userId: Long, productId: Long): Response<ShoppingCart> {
                return webServices.addProductToShoppingCart(productId, userId, 1)
        }

        suspend fun isProductInShoppingCart(userId: Long, productId: Long): Response<Boolean> {
                return webServices.isInShoppingCart(productId, userId)
        }

        suspend fun removeProductFromShoppingCart(userId: Long, productId: Long): Response<ShoppingCart> {
                return webServices.removeFromShoppingCart(productId, userId)
        }

        suspend fun isInCartAndFave(userId: Long, productId: Long): Response<FavCartDto> {
                return webServices.isFavAndInCart(productId, userId)
        }

        suspend fun addProductToWishlist(userId: Long, productId: Long): Response<Void> {
                return webServices.addProductToWishlist(userId, productId)
        }

        suspend fun removeProductFromWishlist(userId: Long, productId: Long): Response<Void> {
                return webServices.removeProductFromWishlist(userId, productId)
        }

        suspend fun getAllProductsInWishlist(userId: Long): Response<List<Product>> {
                return webServices.getAllProductsInWishlist(userId)
        }


        suspend fun getPaymentSheetParams(amount: Int): Response<Map<String, String>> {
                return webServices.getPaymentSheetParams(amount)
        }

        suspend fun getShoppingCarts(userId: Long): Response<List<ShoppingCart>> {
                return webServices.getShoppingCarts(userId)
        }

        suspend fun getUser(userId: Long): Response<User> {
                return webServices.getUser(userId)
        }

}