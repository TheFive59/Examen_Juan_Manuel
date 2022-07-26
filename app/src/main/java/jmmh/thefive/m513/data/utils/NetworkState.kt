package jmmh.thefive.m513.data.repository
enum class Status {
    RUNNING,
    SUCCESS,
    FAILED,
    ENDOFLIST,
}
class NetworkState(val status: Status, val msg: String) {
    companion object {
        val LOADED: NetworkState
        val LOADING: NetworkState
        val ERROR: NetworkState
        val ENDOFLIST: NetworkState
        init {
            LOADED = NetworkState(Status.SUCCESS, "Con exito")
            LOADING = NetworkState(Status.RUNNING, "Cargando")
            ERROR = NetworkState(Status.FAILED, "Ah ocurrido algun error")
            ENDOFLIST = NetworkState(Status.FAILED, "Fin de la paginacion")
        }
    }
}