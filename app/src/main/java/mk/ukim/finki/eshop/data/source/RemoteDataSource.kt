package mk.ukim.finki.eshop.data.source

import mk.ukim.finki.eshop.api.WebServices
import mk.ukim.finki.eshop.api.dto.*
import mk.ukim.finki.eshop.api.model.*
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
        private val webServices: WebServices
){
        suspend fun getCategories(): Response<List<Category>> {
                return webServices.getCategories()
        }

        suspend fun getProductsByCategory(categoryId: Long): Response<List<Product>> {
                return webServices.getProductsByCategory(categoryId)
        }

        suspend fun getProductsInPriceRange(dto: PriceRangeDto): Response<List<Product>> {
                return webServices.getProductsInPriceRange(dto)
        }

        suspend fun getFilteredProductsForCategory(categoryId: Long, searchText: String): Response<List<Product>> {
                return webServices.getFilteredProductsForCategory(categoryId, searchText)
        }

        suspend fun registerUser(dto: RegisterDto) {
                webServices.registerUser(dto)
        }

        suspend fun login(dto: LoginDto): Response<AuthResponse> {
                return webServices.login(dto)
        }

        suspend fun loginWithFacebook(dto: TokenDto): Response<AuthResponse> {
                return webServices.loginWithFacebook(dto)
        }

        suspend fun loginWithGoogle(dto: TokenDto): Response<AuthResponse> {
                return webServices.loginWithGoogle(dto)
        }

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

        suspend fun addProductToShoppingCart(userId: Long, productId: Int): Response<ShoppingCart> {
                return webServices.addProductToShoppingCart(productId, userId, 1)
        }

        suspend fun isProductInShoppingCart(userId: Long, productId: Long): Response<Boolean> {
                return webServices.isInShoppingCart(productId, userId)
        }

        suspend fun removeProductFromShoppingCart(userId: Long, productId: Int): Response<ShoppingCart> {
                return webServices.removeFromShoppingCart(productId, userId)
        }

        suspend fun isInCartAndFave(userId: Long, productId: Int): Response<FavCartDto> {
                return webServices.isFavAndInCart(productId, userId)
        }

}