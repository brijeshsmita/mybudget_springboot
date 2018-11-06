package br.com.meussaldos.server.configuration

class Datasource {
    var name: String? = null
    var host: String? = null
    var database: String? = null
    var port: Int? = null
    var user: String? = null
    var pass: String? = null
    var type: String? = null
    var domains: List<String>? = emptyList()
    var offline: Boolean = false

    val jdbcString:String get() = when(type){
        "postgresql" -> {
            if (offline){
                "offline:$type"
            } else {
                "jdbc:$type://$host:$port/$database"
            }
        }
        else -> throw IllegalArgumentException("Not supported database type [$type]")
    }
}