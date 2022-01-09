package mk.ukim.finki.eshop.ui.userinfo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject


@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {
    var updateUserResponse: MutableLiveData<NetworkResult<Unit>> = MutableLiveData()

    fun updateUserInfo(
        userId: Long,
        filePath: String?,
        name: String,
        surname: String,
        phoneNumber: String
    ) = viewModelScope.launch {
        updateUserResponse.value = NetworkResult.Loading()
        val imagePart: MultipartBody.Part? = getImageFormData(filePath)
        val namePart: RequestBody = RequestBody.create(MediaType.parse("text/plain"), name)
        val surnamePart: RequestBody = RequestBody.create(MediaType.parse("text/plain"), surname)
        val phoneNumberPart: RequestBody =
            RequestBody.create(MediaType.parse("text/plain"), phoneNumber)

        if (Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                updateUserResponse.value = handleUpdateUserInfo(
                    repository.remote.updateUserInfo(
                        userId,
                        imagePart,
                        namePart,
                        surnamePart,
                        phoneNumberPart
                    )
                )
            } catch (e: Exception) {
                updateUserResponse.value = NetworkResult.Error(e.message)
            }
        }
    }

    private fun handleUpdateUserInfo(response: Response<Unit>): NetworkResult<Unit> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }
            response.isSuccessful -> {
                NetworkResult.Success(response.body()!!)
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }

    private fun getImageFormData(filePath: String?): MultipartBody.Part? {
        var filePart: MultipartBody.Part? = null
        if (filePath != null) {
            val file = File(filePath)
            filePart = MultipartBody.Part.createFormData(
                "image",
                file.name,
                RequestBody.create(MediaType.parse("image/jpeg"), file)
            )

        }
        return filePart
    }


}