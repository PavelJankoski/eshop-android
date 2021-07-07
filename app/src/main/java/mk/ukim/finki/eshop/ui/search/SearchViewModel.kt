package mk.ukim.finki.eshop.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.data.model.SearchEntity
import mk.ukim.finki.eshop.data.source.Repository
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    val readSearchHistory: LiveData<List<SearchEntity>> = repository.local.readSearchHistory().asLiveData()

    fun insertSearchText(text: String) = viewModelScope.launch {
        repository.local.insertSearchText(SearchEntity(text, LocalDateTime.now()))
    }

    fun deleteSearchText(id: Int) = viewModelScope.launch {
        repository.local.deleteSearchText(id)
    }

    fun deleteAllSearchHistory() = viewModelScope.launch {
        repository.local.deleteAllSearchHistory()
    }
}
