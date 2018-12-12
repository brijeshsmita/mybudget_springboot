package br.com.meussaldos.liquibase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.util.Properties

class VersionLoaderTest {

    @Test
    fun testaVersaoSnapshot(){
        val passedVersion = "1.0-SNAPSHOT"
        val resultVersion=VersionLoader(Properties().also{
            it["version"] = passedVersion
        }).resolveVersion()
        assertNotEquals(passedVersion, resultVersion)
    }
    @Test
    fun testaVersaoSemSnapshot(){
        val passedVersion = "1.0"
        val resultVersion=VersionLoader(Properties().also{
            it["version"] = passedVersion
        }).resolveVersion()
        assertEquals(passedVersion, resultVersion)
    }

}