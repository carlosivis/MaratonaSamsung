package br.com.example.maratonasamsung.modoEstudo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.example.maratonasamsung.domain.repository.DoencasRepository
import br.com.example.maratonasamsung.model.Responses.DoencasResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ChooseViewModel (private val repository: DoencasRepository): ViewModel(){
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(Dispatchers.Main +viewModelJob)
    private val _response: MutableLiveData<List<DoencasResponse>> = MutableLiveData()

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()

    private val _error: MutableLiveData<Throwable> = MutableLiveData()

    val response: LiveData<List<DoencasResponse>> get() = _response

    val loading: LiveData<Boolean> get() = _loading

    val error: LiveData<Throwable> get() = _error

    fun doencas(){
        viewModelScope.launch {
            _loading.value=true
                try {
                    _response.value = repository.doencas()
                    _loading.value = false
                }
                catch (t:Throwable){
                    _error.value = t
                }
                finally {
                    _loading.value =false
                }
        }
    }

}