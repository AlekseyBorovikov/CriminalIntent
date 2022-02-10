package com.example.criminalintent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.criminalintent.models.Crime
import java.io.File
import java.util.*

class CrimeDetailViewModel(): ViewModel() {

    private val crimeRepository = CrimeRepository.get()
    private val crimeIdLiveData = MutableLiveData<UUID>()

    // Обновление данных каждый раз, когда crimeIdLiveData меняет value
    val crimeLiveData: LiveData<Crime?> =
        Transformations.switchMap(crimeIdLiveData) { crimeId ->
            crimeRepository.getCrime(crimeId)
        }

    fun loadCrime(crimeId: UUID){
        crimeIdLiveData.value = crimeId
    }

    fun saveCrime(crime: Crime) = crimeRepository.updateCrime(crime)

    fun getPhotoFile(crime: Crime) = crimeRepository.getPhotoFile(crime)

}