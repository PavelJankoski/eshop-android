package mk.ukim.finki.eshop.util

import androidx.lifecycle.MutableLiveData
import javax.inject.Singleton

@Singleton
class GlobalVariables {
    companion object {
        var productsInBagNumber : MutableLiveData<Int> = MutableLiveData(0)
    }
}