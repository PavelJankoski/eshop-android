package mk.ukim.finki.eshop.data.source

import mk.ukim.finki.eshop.api.WebServices
import mk.ukim.finki.eshop.api.dto.*
import mk.ukim.finki.eshop.api.dto.request.CreateEditAddressDto
import mk.ukim.finki.eshop.api.dto.request.FilterProductDto
import mk.ukim.finki.eshop.api.dto.request.RegisterDto
import mk.ukim.finki.eshop.api.dto.request.TokenDto
import mk.ukim.finki.eshop.api.dto.response.LoginDto
import mk.ukim.finki.eshop.api.model.*
import mk.ukim.finki.eshop.util.Constants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Part
import retrofit2.http.Path
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

        suspend fun loginWithGoogle(body: TokenDto): Response<LoginDto> {
                return webServices.loginWithGoogle(body)
        }

        suspend fun loginWithFacebook(body: TokenDto): Response<LoginDto> {
                return webServices.loginWithFacebook(body)
        }

        suspend fun registerUser(dto: RegisterDto): Response<Void> {
                return webServices.registerUser(dto)
        }

        suspend fun getUserInfo(userId: Long): Response<User> {
                return webServices.getUserInfo(userId)
        }

        suspend fun updateUserInfo(userId: Long,
                                   image: MultipartBody.Part?,
                                   name: RequestBody,
                                   surname: RequestBody,
                                   phoneNumber: RequestBody): Response<Unit> {
                return webServices.updateUserInfo(userId, image, name, surname, phoneNumber)
        }

        suspend fun getAddressesForUser(userId: Long): Response<List<Address>> {
                return webServices.getAddressesForUser(userId)
        }

        suspend fun createAddressForUser(userId: Long, body: CreateEditAddressDto): Response<Address> {
                return webServices.createAddressesForUser(userId, body)
        }

        suspend fun editAddressForUser(addressId: Long, userId: Long, body: CreateEditAddressDto): Response<Address> {
                return webServices.editAddressesForUser(addressId, userId, body)
        }

        suspend fun deleteAddress(addressId: Long): Response<Unit> {
                return webServices.deleteAddress(addressId)
        }

        suspend fun getWishlistProductsForUser(userId: Long): Response<List<Product>> {
                return webServices.getWishlistProductsForUser(userId)
        }

        suspend fun addProductToWishlistForUser(productId: Long, userId: Long): Response<Long> {
                return webServices.addProductToWishlistForUser(productId, userId)
        }

        suspend fun removeProductFromWishlistForUser(productId: Long, userId: Long): Response<Long> {
                return webServices.removeProductFromWishlistForUser(productId, userId)
        }

        suspend fun getReviewsForProduct(productId: Long): Response<List<Review>> {
                return webServices.getReviewsForProduct(productId)
        }

        suspend fun getReviewsByRatingForProduct(productId: Long, rating: Float): Response<List<Review>> {
                return webServices.getReviewsByRatingForProduct(productId, rating)
        }

        suspend fun getOrderItemsForUser(userId: Long): Response<List<OrderItem>> {
                return webServices.getOrderItemsForUser(userId)
        }

}