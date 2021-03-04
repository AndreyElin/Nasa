package andrey.elin.nasa

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFragmentViewModel (
    private val liveDataForViewToObserve: MutableLiveData<NasaData> = MutableLiveData(),
    private val retrofitImpl: NasaRetrofitImpl = NasaRetrofitImpl()
    ) :
    ViewModel() {

        fun getData(): LiveData<NasaData> {
            sendServerRequest()
            return liveDataForViewToObserve
        }

        private fun sendServerRequest() {
            liveDataForViewToObserve.value = NasaData.Loading(null)
            val apiKey: String = "DEMO_KEY"
            if (apiKey.isBlank()) {
                NasaData.Error(Throwable("You need API key"))
            } else {
                retrofitImpl.getRetrofitImpl().getPictureOfTheDay(apiKey).enqueue(object :
                    Callback<NasaServerResponseData> {
                    override fun onResponse(
                        call: Call<NasaServerResponseData>,
                        response: Response<NasaServerResponseData>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            liveDataForViewToObserve.value =
                                NasaData.Success(response.body()!!)
                        } else {
                            val message = response.message()
                            if (message.isNullOrEmpty()) {
                                liveDataForViewToObserve.value =
                                    NasaData.Error(Throwable("Unidentified error"))
                            } else {
                                liveDataForViewToObserve.value =
                                    NasaData.Error(Throwable(message))
                            }
                        }
                    }

                    override fun onFailure(call: Call<NasaServerResponseData>, t: Throwable) {
                        liveDataForViewToObserve.value = NasaData.Error(t)
                    }
                })
            }
        }
}