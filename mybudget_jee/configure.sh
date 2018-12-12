#!/bin/bash

WILDFLY_HOME=/opt/wildfly

${WILDFLY_HOME}/bin/jboss-cli.sh -c <<EOF
  xa-data-source \
    disable --name=meussaldos
EOF
${WILDFLY_HOME}/bin/jboss-cli.sh -c <<EOF
  xa-data-source \
      remove --name=meussaldos
EOF
${WILDFLY_HOME}/bin/jboss-cli.sh -c <<EOF
  /subsystem=mail/mail-session=mail-meussaldos/server=smtp:remove
EOF
${WILDFLY_HOME}/bin/jboss-cli.sh -c <<EOF
  /subsystem=mail/mail-session=mail-meussaldos:remove
EOF
${WILDFLY_HOME}/bin/jboss-cli.sh -c <<EOF
  /socket-binding-group=standard-sockets/remote-destination-outbound-socket-binding=gmail-smtp:remove
EOF

${WILDFLY_HOME}/bin/jboss-cli.sh -c <<EOF
module remove \
  --name=org.postgresql.jdbc
EOF

${WILDFLY_HOME}/bin/jboss-cli.sh -c <<EOF
module add \
  --name=org.postgresql.jdbc \
  --resources=lib/postgresql-42.2.2.jar \
  --dependencies=javax.api,javax.transaction.api
EOF

##sudo -u wildfly /opt/wildfly/bin/jboss-cli.sh -c

${WILDFLY_HOME}/bin/jboss-cli.sh -c <<EOF
connect
batch

/subsystem=datasources/jdbc-driver=postgresql:add( \
    driver-name=postgresql, \
    driver-module-name=org.postgresql.jdbc, \
    driver-xa-datasource-class-name=org.postgresql.xa.PGXADataSource \
)

xa-data-source add \
       --name=meussaldos \
       --driver-name=postgresql \
       --jndi-name=java:jboss/datasources/meussaldos \
       --user-name=postgres \
       --password=postgres \
       --use-ccm=false \
       --max-pool-size=25 \
       --blocking-timeout-wait-millis=5000 \
       --xa-datasource-properties=ServerName=127.0.0.1,PortNumber=5432,DatabaseName=meussaldos

xa-data-source enable --name=meussaldos

/subsystem=undertow/server=default-server/ajp-listener=ajplistener:add(socket-binding=ajp, scheme=http, enabled=true)

/socket-binding-group=standard-sockets/remote-destination-outbound-socket-binding=gmail-smtp:add(host=smtp.gmail.com,port=465)
/subsystem=mail/mail-session=mail-meussaldos:add(jndi-name="java:jboss/mail/meussaldos")
/subsystem=mail/mail-session=mail-meussaldos/server=smtp:add(outbound-socket-binding-ref=gmail-smtp,ssl=true,username='er**************l.com', password='pcph********rfzz')

run-batch
:reload

EOF


curl -L -v -u 'j****4' -X GET 'https://ci.plenusoft.io/nexus/service/rest/beta/search/assets/download?repository=maven-privilleged-group&group=br.com.meussaldos&name=meussaldos&version=1.0-BETA001&maven.extension=war'
curl -L -v -u 'j****4' -X GET 'https://ci.plenusoft.io/nexus/service/rest/beta/search/assets/download?repository=maven-privilleged-group&group=br.com.meussaldos&name=meussaldos-liquibase&version=1.0-BETA001&maven.extension=tar.gz'
