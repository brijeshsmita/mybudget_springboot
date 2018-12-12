#!/usr/bin/env groovy

def joinModelToMap(map, model){
    def resultMap=[:]
    resultMap.groupId=(map.groupId ?: (model.groupId ?: model.parent.groupId))
    resultMap.artifactId=(map.artifactId ?: (model.artifactId ?: model.parent.artifactId))
    resultMap.version=(map.version ?: (model.version ?: model.parent.version))
    resultMap.packaging=(map.packaging ?: (model.packaging ?: model.parent.packaging))
    resultMap
}

def resolveUrl(model) {
    echo "${model.groupId}"
    echo "${model.version}"
    echo "${model.artifactId}"
    echo "${model.packaging}"
    "https://ci.plenusoft.io/nexus/repository/sigen-staging/${model.groupId.replaceAll('\\.','/')}/${model.artifactId}/${model.version}/${model.artifactId}-${model.version}.${model.packaging}"
}

pipeline {
    agent none
    environment {
        projectName = 'Em 3 Anos'
        stagingRepository = 'sigen-staging::default::https://ci.plenusoft.io/nexus/repository/sigen-staging'
        telegramChatId = credentials('chat-id-notificacoes-meus-saldos')
        telegramApiKey = credentials('meus-saldos-bot-api-key')
        senhaNexusUsuarioJenkins = credentials('senhaNexusUsuarioJenkins')
        deployServerAddress = '35.199.91.34'
        nextVersion = resolveVersion('pom.xml')
    }
    stages {

        stage('Build') {
            agent any
            tools {
                maven 'local-maven'
            }


            steps {
                echo 'Before build'
                configFileProvider([configFile(fileId: 'sigen-maven-settings', targetLocation: "${pwd()}/.localRepo/settings.xml")]) {
                    sh "mvn -U -s '${pwd()}/.localRepo/settings.xml' -DaltDeploymentRepository=${env.stagingRepository} versions:set -DgenerateBackupPoms=false -DnewVersion='${env.nextVersion}'"
                    sh "mvn -s '${pwd()}/.localRepo/settings.xml' -DaltDeploymentRepository=${env.stagingRepository} clean deploy"
                }
                cobertura autoUpdateHealth: false, autoUpdateStability: false, coberturaReportFile: 'target/site/cobertura/coverage.xml', conditionalCoverageTargets: '70, 0, 0', failNoReports: false, failUnhealthy: false, failUnstable: false, lineCoverageTargets: '80, 0, 0', maxNumberOfBuilds: 5, methodCoverageTargets: '80, 0, 0', sourceEncoding: 'ASCII', zoomCoverageChart: false
                checkstyle canComputeNew: false, canRunOnFailed: true, defaultEncoding: '', healthy: '', pattern: '**/checkstyle-result.xml', unHealthy: ''
                echo 'After build'
            }


            post {
                always {
                    cleanWs()
                }


                success {
                    telegram(apiKey: telegramApiKey, chatId: telegramChatId) {
                        sendMessage("""Aguardando aprovação de versão ${env.nextVersion} para o projeto ${env.projectName}
                                    <a href='${env.RUN_DISPLAY_URL}'>Construção em andamento</a>
                                """)
                    }
                    milestone label: 'PRE APROVAÇÃO DE TESTES', ordinal: 2
                }


                failure {
                    telegram(apiKey: telegramApiKey, chatId: telegramChatId) {
                        sendMessage("""Falha no estágio de Build do projeto ${env.projectName}
                                    <a href='${env.RUN_DISPLAY_URL}'>Construção defeituosa</a>
                                """)
                    }
                }
            }
        }




        stage('Testing Phase') {
            failFast true
            parallel {
                stage('Test On Windows') {
                    steps {
                        echo "run-tests.bat"
                        sleep(10)
                    }
                    post {
                        always {
                            echo "Done with run tests on linux"
                        }
                        failure {
                            telegram(apiKey: telegramApiKey, chatId: telegramChatId) {
                                sendMessage("""Falha no estágio de testes no windows do projeto ${env.projectName}
                                    <a href='${env.RUN_DISPLAY_URL}'>Construção defeituosa</a>
                                """)
                            }
                        }
                    }
                }
                stage('Test On Linux') {
                    steps {
                        echo "run-tests.sh"
                        sleep(10)
                    }
                    post {
                        always {
                            echo "Done with run tests on linux"
                        }
                        failure {
                            telegram(apiKey: telegramApiKey, chatId: telegramChatId) {
                                sendMessage("""Falha no estágio de testes no linux do projeto ${env.projectName}
                                    <a href='${env.RUN_DISPLAY_URL}'>Construção defeituosa</a>
                                """)
                            }
                        }
                    }
                }
                stage('Manual Testing') {
                    when {
                        branch 'master'
                    }
                    options {
                        timeout(time: 3, unit: 'DAYS')
                    }
                    input {
                        message "Aprovar testes manuais?"
//                        parameters {
//                            booleanParam(description: 'Deseja aprovar a tag?', name: 'confirmarTag', defaultValue: false)
//                        }
                    }
                    steps {
                        echo "Testes Manuais"
                    }
                    post {
                        always {
                            echo "Done with manual testing"
                        }
                        failure {
                            telegram(apiKey: telegramApiKey, chatId: telegramChatId) {
                                sendMessage("""Falha no estágio de testes manuais do projeto ${env.projectName}
                                    <a href='${env.RUN_DISPLAY_URL}'>Construção defeituosa</a>
                                """)
                            }
                        }
                    }
                }
            }
            post {
                success {
                    milestone label: 'PÓS APROVAÇÃO DE TESTES', ordinal: 3
                    telegram(apiKey: telegramApiKey, chatId: telegramChatId) {
                        sendMessage("""Aguardando aprovação para aplicar versão ${env.nextVersion} do projeto ${env.projectName} em ambiente de produção
                                    <a href='${env.RUN_DISPLAY_URL}'>Construção em andamento</a>""")
                    }
                }
            }
        }


        stage('Deploy Phase') {
            when {
                branch 'master'
            }
            parallel {
                stage('Deploy Phase') {
                    agent any
                    options {
                        timeout(time: 3, unit: 'DAYS')
                    }
                    input {
                        message "Aplicar em ambiente de produção?"
                        parameters {
                            booleanParam(description: 'Deseja aprovar a aplicação da versão ${env.nextVersion} em ambiente de produção?', name: 'deployToProduction', defaultValue: false)
                        }
                    }
                    steps {
                        milestone label: 'PRE DEPLOY', ordinal: 4
                        echo "deployToProduction [${params.deployToProduction}]"
                        script {
                            ssh {
                                sshAgent('jenkins-ssh-key')
                                user('wildfly')
                                host(env.deployServerAddress)
                                //DOWNLOAD DOS ARTEFATOS
                                cmd("curl -L -u 'jenkins:${senhaNexusUsuarioJenkins}' -o 'meussaldos-${env.nextVersion}.war' '${resolveUrl(joinModelToMap([version: env.nextVersion], readMavenPom(file: 'webapp/pom.xml')))}'")
                                cmd("curl -L -u 'jenkins:${senhaNexusUsuarioJenkins}' -o 'meussaldos-liquibase-${env.nextVersion}.tar.gz' '${resolveUrl(joinModelToMap([packaging:'tar.gz',version:env.nextVersion],readMavenPom(file: 'liquibase/pom.xml')))}'")
                                cmd('sudo systemctl stop wildfly')
                                //APLICAR LIQUIBASE
                                cmd('rm -rfv meussaldos-liquibase')
                                cmd("mkdir -p 'meussaldos-liquibase-${env.nextVersion}'")
                                cmd("tar xf 'meussaldos-liquibase-${env.nextVersion}.tar.gz' -C 'meussaldos-liquibase-${env.nextVersion}'")
                                cmd("java -jar 'meussaldos-liquibase-${env.nextVersion}/meussaldos-liquibase-${env.nextVersion}.jar' --f='/home/wildfly/meussaldos-ds-config.yml'")
                                //REALIZAR DEPLOY
                                cmd('rm -rfv /opt/wildfly/standalone/deployments/meussaldos.war* /opt/wildfly/standalone/deployments/e3a.war*')
                                cmd("cp -fv 'meussaldos-${env.nextVersion}.war' '/opt/wildfly/standalone/deployments/e3a.war'")
                                cmd('touch \'/opt/wildfly/standalone/deployments/e3a.war.dodeploy\'')
                                cmd('sudo systemctl start wildfly')
                                exec()
                            }
                        }
                        echo 'REALIZAR AQUI O DEPLOY PARA O AMBIENTE DE PRODUÇÃO NO GOOGLE'
                        milestone label: 'PÓS DEPLOY', ordinal: 5
                    }
                    post {
                        always {
                            cleanWs()
                        }
                        success {
                            telegram(apiKey: telegramApiKey, chatId: telegramChatId) {
                                sendMessage("""Nova versão '${env.nextVersion}' aplicada para o projeto ${env.projectName}""")
                            }
                        }
                    }
                }
            }

        }
    }

    post {
        always {
            echo "Fim do build"
        }
        success {
            echo 'Eventos de sucesso'
        }
        failure {
            echo 'Eventos de falha'
        }
    }

    }
