package br.com.meussaldos.liquibase

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class VersionLoader(private val properties: Properties) {

    constructor(): this(Properties().also{
        it.load(VersionLoader::class.java.getResourceAsStream("/META-INF/maven/br.com.meussaldos/meussaldos-liquibase/pom.properties"))
    })

    fun resolveVersion():String = properties
            .getProperty("version")
            .replace("-SNAPSHOT", SimpleDateFormat("yyMMddHHmmssZ").format(Date()))
}