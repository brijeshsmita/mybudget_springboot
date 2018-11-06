package br.com.meussaldos.liquibase

import br.com.meussaldos.server.configuration.Datasource
import org.junit.Test
import org.junit.Assert.*

class DatasourceTest {
    private fun prepareDS():Datasource=Datasource().also{
        it.type="postgresql"
        it.host="127.0.0.1"
        it.port=5432
        it.database="meussaldos"
    }

    @Test
    fun testaJdbcStringOffline(){
        val expected="offline:postgresql"
        val result= prepareDS().also{
            it.offline=true
        }.jdbcString
        assertEquals(expected, result)
    }
    @Test
    fun testaJdbcStringPostgres(){
        val expected="jdbc:postgresql://127.0.0.1:5432/meussaldos"
        val result= prepareDS().jdbcString
        assertEquals(expected, result)
    }
    @Test(expected = IllegalArgumentException::class)
    fun testaDatasourceNaoSuportado() {
        Datasource().also{
            it.type="sqlserver"
        }.jdbcString
    }

}