package andrey.elin.nasa

sealed class NasaData {
    data class Success(val serverResponseData: NasaServerResponseData) : NasaData()
    data class Error(val error: Throwable) : NasaData()
    data class Loading(val progress: Int?) : NasaData()
}
