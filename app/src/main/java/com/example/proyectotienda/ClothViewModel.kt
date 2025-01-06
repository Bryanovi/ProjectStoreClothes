import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyectotienda.domain.ClothDomain

class ClothViewModel : ViewModel() {
    private val _animesList = MutableLiveData<List<ClothDomain>>()
    private val _seriesList = MutableLiveData<List<ClothDomain>>()
    private val _gamesList = MutableLiveData<List<ClothDomain>>()

    val animesList: LiveData<List<ClothDomain>>
        get() = _animesList

    val seriesList: LiveData<List<ClothDomain>>
        get() = _seriesList

    val gamesList: LiveData<List<ClothDomain>>
        get() = _gamesList

    fun setAnimesList(animes: List<ClothDomain>) {
        _animesList.value = animes
    }

    fun setSeriesList(series: List<ClothDomain>) {
        _seriesList.value = series
    }

    fun setGamesList(games: List<ClothDomain>) {
        _gamesList.value = games
    }
}
