package br.com.meussaldos.liquibase

import br.com.meussaldos.server.configuration.Datasource
import br.com.meussaldos.server.configuration.ServerConfiguration
import liquibase.exception.LiquibaseException
import liquibase.integration.commandline.Main
import org.apache.commons.cli.*
import org.yaml.snakeyaml.Yaml
import java.io.FileInputStream
import java.io.InputStream

object Executor {

    private fun execParams(datasource:Datasource, domain:String):MutableList<String> =
        mutableListOf("--defaultsFile=config/$domain.properties",
            "--url=${datasource.jdbcString}",
            "--username=${datasource.user}",
            "--password=${datasource.pass}")


    private fun tag(datasource:Datasource, domain:String, version:String){
        val execParams=execParams(datasource, domain).also{
            it.add("tag")
            it.add(version)
        }
        println(execParams.joinToString(System.lineSeparator()))
        Main.run(execParams.toTypedArray())
    }
    private fun update(datasource:Datasource, domain:String){
        val execParams=execParams(datasource, domain).also{
            it.add("update")
        }
        println(execParams.joinToString(System.lineSeparator()))
        Main.run(execParams.toTypedArray())
    }
    private fun updateSQL(datasource:Datasource, domain:String) {
        val execParams=execParams(datasource, domain).also{
            it.add("updateSQL")
        }
        println(execParams.joinToString(System.lineSeparator()))
        Main.run(execParams.toTypedArray())
    }
    private fun futureRollbackSQL(datasource:Datasource, domain:String) {
        val execParams=execParams(datasource, domain).also{
            it.add("futureRollbackSQL")
        }
        println(execParams.joinToString(System.lineSeparator()))
        Main.run(execParams.toTypedArray())
    }
    private fun rollbackSQL(datasource:Datasource, domain:String, version:String){
        val execParams=execParams(datasource, domain).also{
            it.add("rollbackSQL")
            it.add(version)
        }
        println(execParams.joinToString(System.lineSeparator()))
        Main.run(execParams.toTypedArray())
    }
    private fun rollback(datasource:Datasource, domain:String, version:String){
        val execParams=execParams(datasource, domain).also{
            it.add("rollback")
            it.add(version)
        }
        println(execParams.joinToString(System.lineSeparator()))
        Main.run(execParams.toTypedArray())
    }
    private fun resolveVersion():String = VersionLoader().resolveVersion()

    private fun rollbackSQL(datasource: Datasource) {
        val version=resolveVersion()
        println(datasource.name)
        datasource.domains?.forEach { domain ->
            rollback(datasource, domain, "pre-$version")
        }
    }
    private fun rollback(datasource: Datasource) {
        val version=resolveVersion()
        println(datasource.name)
        datasource.domains?.forEach { domain ->
            rollback(datasource, domain, "pre-$version")
        }
    }
    private fun updateSQL(datasource: Datasource) {
        println(datasource.name)
        datasource.domains?.forEach { domain ->
            try {
                updateSQL(datasource, domain)
            } catch (e:LiquibaseException) {
                throw e
            }
        }
    }
    private fun futureRollbackSQL(datasource: Datasource) {
        println(datasource.name)
        datasource.domains?.forEach { domain ->
            try {
                futureRollbackSQL(datasource, domain)
            } catch (e:LiquibaseException) {
                throw e
            }
        }
    }
    private fun update(datasource: Datasource) {
        val version= resolveVersion()
        println(datasource.name)
        datasource.domains?.forEach { domain ->
            tag(datasource, domain, "pre-$version")
            try {
                update(datasource, domain)
                tag(datasource, domain, version)
            } catch (e:LiquibaseException) {
		try {
                    rollback(datasource, domain, "pre-$version")
		} catch (e2:LiquibaseException) {}
                throw e
            }
        }
    }

    private fun readConfig(file:String):List<Datasource> {
        return readConfig(FileInputStream(file))
    }
    private fun readConfig(file:InputStream):List<Datasource> {
        return file.use{
            Yaml().loadAs(file, ServerConfiguration::class.java).datasources ?: emptyList()
        }
    }

    private fun resolveAction(cmd: String):(Datasource)->Unit = when (cmd.toLowerCase()) {
        "rollback" -> Executor::rollback
        "rollbacksql" -> Executor::rollbackSQL
        "futurerollbacksql" -> Executor::futureRollbackSQL
        "updatesql" -> Executor::updateSQL
        "update" -> Executor::update
        else -> throw IllegalArgumentException("Argument --action=$cmd is not valid")
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val options= Options()
        options.addOption(Option("f", "arquivo de configurações yaml").also{
            it.isRequired=true
            it.args=1
            it.argName="arquivo"
            it.valueSeparator='='
            it.type=String::class.java
        }).addOption(Option("action", "action").also{
            it.isRequired=false
            it.args=1
            it.argName="action"
            it.valueSeparator='='
            it.type=String::class.java
        })

        val parser:CommandLineParser = GnuParser()
        val cmd=parser.parse(options, args)
        if (cmd.hasOption("f")) {
            val operation:(Datasource)->Unit=if (cmd.hasOption("action")) {
                resolveAction(cmd.getOptionValue("action"))
            } else {
                Executor::update
            }
            listOf(cmd.getOptionValue("f")).stream().flatMap {
                Executor.readConfig(it).stream()
            }.forEach(operation)
        } else {

        }

    }

}
