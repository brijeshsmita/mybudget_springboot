#!/bin/bash
export DNS_LIST=( e3a.com.br em3anos.com em3anos.com.br emtresanos.com emtresanos.com.br )

it=0
length=${#DNS_LIST[*]}
outputFolder=${1:-'target/httpd/config.d'}

function registerCertificate(){
    local domain="${1}"
    if [ "${outputFolder}" == 'target/httpd/config.d' ] ; then
        echo "Registrando certificados para ${domain}"
        echo "certbot -q -n -d ${domain} --apache certonly"
    else
        echo "Registrando certificados para ${domain}"
        certbot -q -n -d ${domain} --apache certonly
    fi
}

function configureReverseProxy(){
    local index=${1}
    local dns="${2}"
    local outputFileName="010-dns-${index}-${dns}.conf"
    echo "${outputFolder}/${outputFileName}"
    rm -rfv "${outputFolder}/010-dns-*.conf"

    mkdir -p "${outputFolder}"
    echo -e "
<VirtualHost *:80>\n\
  ServerName ${dns}\n\
  Redirect permanent / https://${dns}/\n\
</VirtualHost>\n\
\n\
<VirtualHost *:443>\n\
  SSLEngine on\n\
  # Your key file has been saved at:\n\
  SSLCertificateFile /etc/letsencrypt/live/${dns}/fullchain.pem\n\
  SSLCertificateKeyFile /etc/letsencrypt/live/${dns}/privkey.pem\n\
  ServerName ${dns}\n\
  RequestHeader set X-Forwarded-Proto \"https\"\n\
  #${dns}\n\
  Redirect permanent \"/\" \"https://${dns}/e3a\" 
  <Location /meussaldos>\n\
      RewriteEngine on\n\
      ProxyPass  ajp://127.0.0.1:8009/e3a/ ttl=120 ping=1 timeout=1800\n\
      ProxyPassReverse ajp://127.0.0.1:8009/e3a/\n\
      AllowOverride None\n\
  </Location>\n\
  <Location /meussaldos/>\n\
      RewriteEngine on\n\
      ProxyPass  ajp://127.0.0.1:8009/e3a/ ttl=120 ping=1 timeout=1800\n\
      ProxyPassReverse ajp://127.0.0.1:8009/e3a/\n\
      AllowOverride None\n\
  </Location>\n\
  <Location /e3a>\n\
      RewriteEngine on\n\
      ProxyPass  ajp://127.0.0.1:8009/e3a/ ttl=120 ping=1 timeout=1800\n\
      ProxyPassReverse ajp://127.0.0.1:8009/e3a/\n\
      AllowOverride None\n\
  </Location>\n\
  <Location /e3a/>\n\
      RewriteEngine on\n\
      ProxyPass  ajp://127.0.0.1:8009/e3a/ ttl=120 ping=1 timeout=1800\n\
      ProxyPassReverse ajp://127.0.0.1:8009/e3a/\n\
      AllowOverride None\n\
  </Location>\n\
</VirtualHost>" > "${outputFolder}/${outputFileName}"
}

while [ ${it} -lt ${length} ] ; do
    registerCertificate "${DNS_LIST[$it]}"
    registerCertificate "www.${DNS_LIST[$it]}"
    registerCertificate "beta.${DNS_LIST[$it]}"
    configureReverseProxy "$it" "${DNS_LIST[$it]}"
    configureReverseProxy "$it" "www.${DNS_LIST[$it]}"
    configureReverseProxy "$it" "beta.${DNS_LIST[$it]}"
    ((it=1+$it))
done
