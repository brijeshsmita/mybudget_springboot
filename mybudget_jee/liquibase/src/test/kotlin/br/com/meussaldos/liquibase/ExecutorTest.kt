package br.com.meussaldos.liquibase

import org.junit.Test
import java.io.File
import java.nio.file.Paths

class ExecutorTest {

//    @Test
    fun futureRollbackSQL(){
        val configFile = Paths.get(ExecutorTest::class.java.getResource("/applicationConfig.yml").toURI()).toFile()
        println(configFile.parent)
        try {
            Executor.main(arrayOf("--f=${configFile.absolutePath}", "--action=futureRollbackSQL"))
        } finally {
            File("databasechangelog.csv").delete()
        }
    }
//    @Test
    fun updateSQL(){
        val configFile = Paths.get(ExecutorTest::class.java.getResource("/applicationConfig.yml").toURI()).toFile()
        println(configFile.parent)
        try {
            Executor.main(arrayOf("--f=${configFile.absolutePath}", "--action=updateSQL"))
        } finally {
            File("databasechangelog.csv").delete()
        }
    }
}

object ExecutorTestMainMethod {

    @JvmStatic
    fun main(args: Array<String>) {
        //val absoluteFile=Paths.get(ExecutorTest::class.java.getResource("/applicationConfig.yml").toURI()).toFile().absolutePath
        Executor.main(args)
        //Executor.main(arrayOf("--f=$absoluteFile", "--rollback"))
    }

}
