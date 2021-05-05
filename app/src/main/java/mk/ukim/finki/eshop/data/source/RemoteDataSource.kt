package mk.ukim.finki.eshop.data.source

import mk.ukim.finki.eshop.api.WebServices
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
        private val webServices: WebServices
){
}