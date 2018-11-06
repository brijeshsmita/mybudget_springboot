#!/bin/bash
if [ "x$(ps --no-headers -o comm 1)x"=="xsystemdx"  ] && [ "$(systemctl is-active docker.service)" == 'inactive' ] ; then
    echo 'Inicializando serviço do docker'
    sudo systemctl start docker.service;
fi
cd "$(dirname ${0})"

subnet='172.19.0.0/16'
gateway='172.19.0.1'
user_created_network_name=redeInfraInterna
if [ 0 -eq $(docker network ls | grep -c "$user_created_network_name") ] ; then
    echo "Criando rede $user_created_network_name";
    docker network create \
        --subnet "$subnet" \
        --gateway "$gateway" \
        --driver bridge $user_created_network_name
fi
echo "Rede '$user_created_network_name' criada em subrede '$subnet' via o gateway '$gateway'"
