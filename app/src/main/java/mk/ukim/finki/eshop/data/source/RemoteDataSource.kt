package mk.ukim.finki.eshop.data.source

import mk.ukim.finki.eshop.api.WebServices
import mk.ukim.finki.eshop.api.dto.request.*
import mk.ukim.finki.eshop.api.dto.response.LoginDto
import mk.ukim.finki.eshop.api.dto.response.StripePaymentSheet
import mk.ukim.finki.eshop.api.dto.response.orderhistory.OrderHistory
import mk.ukim.finki.eshop.api.dto.response.orderhistorydetails.OrderHistoryDetails
import mk.ukim.finki.eshop.api.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val webServices: WebServices
) {
    suspend fun getCategories(): Response<List<Category>> {
        return webServices.getCategories()
    }

    suspend fun getProductsByCategory(categoryId: Long, userId: Long?): Response<List<Product>> {
        return webServices.getProductsByCategory(categoryId, userId)
    }

    suspend fun getFilteredProductsForCategory(
        dto: FilterProductDto,
        userId: Long?
    ): Response<List<Product>> {
        return webServices.getFilteredProductsForCategory(dto, userId)
    }

    suspend fun getSearchedProducts(searchText: String, userId: Long?): Response<List<Product>> {
        return webServices.getSearchedProducts(searchText, userId)
    }

    suspend fun getProductByProductCode(productCode: String, userId: Long?): Response<Product> {
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

    suspend fun getUserInfo(userId: Long?): Response<User> {
        return webServices.getUserInfo(userId)
    }

    suspend fun updateUserInfo(
        userId: Long?,
        image: MultipartBody.Part?,
        name: RequestBody,
        surname: RequestBody,
        phoneNumber: RequestBody
    ): Response<Unit> {
        return webServices.updateUserInfo(userId, image, name, surname, phoneNumber)
    }

    suspend fun getAddressesForUser(userId: Long?): Response<List<Address>> {
        return webServices.getAddressesForUser(userId)
    }

    suspend fun createAddressForUser(userId: Long?, body: CreateEditAddressDto): Response<Address> {
        return webServices.createAddressesForUser(userId, body)
    }

    suspend fun editAddressForUser(
        addressId: Long,
        userId: Long?,
        body: CreateEditAddressDto
    ): Response<Address> {
        return webServices.editAddressesForUser(addressId, userId, body)
    }

    suspend fun deleteAddress(addressId: Long): Response<Unit> {
        return webServices.deleteAddress(addressId)
    }

    suspend fun getWishlistProductsForUser(userId: Long?): Response<List<Product>> {
        return webServices.getWishlistProductsForUser(userId)
    }

    suspend fun addProductToWishlistForUser(productId: Long, userId: Long?): Response<Long> {
        return webServices.addProductToWishlistForUser(productId, userId)
    }

    suspend fun removeProductFromWishlistForUser(productId: Long, userId: Long?): Response<Long> {
        return webServices.removeProductFromWishlistForUser(productId, userId)
    }

    suspend fun getReviewsForProduct(productId: Long): Response<List<Review>> {
        return webServices.getReviewsForProduct(productId)
    }

    suspend fun getReviewsByRatingForProduct(
        productId: Long,
        rating: Float
    ): Response<List<Review>> {
        return webServices.getReviewsByRatingForProduct(productId, rating)
    }

    suspend fun getOrderItemsForUser(userId: Long?): Response<List<OrderItem>> {
        return webServices.getOrderItemsForUser(userId)
    }

    suspend fun addProductToBag(body: AddProductToBagDto): Response<Unit> {
        return webServices.addProductToBag(body)
    }

    suspend fun removeProductFromBag(body: RemoveProductFromBagDto): Response<Unit> {
        return webServices.removeProductFromBag(body)
    }

    suspend fun changeQuantityInBag(body: ChangeQuantityInBagDto): Response<Unit> {
        return webServices.changeQuantityInBag(body)
    }

    suspend fun getItemsInBagForUser(userId: Long?): Response<Int> {
        return webServices.getItemsInBagForUser(userId)
    }

    suspend fun getOrderDetailsForUser(userId: Long?): Response<OrderDetails> {
        return webServices.getOrderDetailsForUser(userId)
    }

    suspend fun getPaymentSheetParams(amount: Float): Response<StripePaymentSheet> {
        return webServices.getPaymentSheetParams(amount)
    }

    suspend fun placeOrder(userId: Long?): Response<Unit> {
        return webServices.placeOrder(userId)
    }

    suspend fun getOrderHistory(userId: Long?): Response<OrderHistory> {
        return webServices.getOrderHistory(userId)
    }

    suspend fun getOrderHistoryDetails(orderId: Long): Response<OrderHistoryDetails> {
        return webServices.getOrderHistoryDetails(orderId)
    }

    suspend fun leaveReview(dto: LeaveReviewDto): Response<Unit> {
        return webServices.leaveReview(dto)
    }
}