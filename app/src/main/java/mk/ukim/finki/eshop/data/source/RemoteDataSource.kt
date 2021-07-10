package mk.ukim.finki.eshop.data.source

import mk.ukim.finki.eshop.api.WebServices
import mk.ukim.finki.eshop.api.dto.LoginDto
import mk.ukim.finki.eshop.api.dto.PriceRangeDto
import mk.ukim.finki.eshop.api.dto.RegisterDto
import mk.ukim.finki.eshop.api.dto.TokenDto
import mk.ukim.finki.eshop.api.model.AuthResponse
import mk.ukim.finki.eshop.api.model.Category
import mk.ukim.finki.eshop.api.model.Product
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

}