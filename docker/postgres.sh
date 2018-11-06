#!/bin/bash
postgres_data_volume='postgres-data'
postgres_container_name='postgres'
network_postgres_ip=172.19.0.2
network_name=redeInfraInterna
if [ "x$(ps --no-headers -o comm 1)x"=="xsystemdx"  ] && [ "$(systemctl is-active docker.service)" == 'inactive' ] ; then
    echo 'Inicializando serviço do docker'
    sudo systemctl start docker.service;
fi
cd "$(dirname ${0})"

if [ "x$(docker volume ls | grep -o ""$postgres_data_volume"")x" == 'xx' ] ; then
  echo 'Criando volume para persistir os dados do postgres entre inicializações'
  docker volume create --name "${postgres_data_volume}"
else
    echo "Volume do postgres já criado"
fi
echo docker run -d --rm \
         --ip "$network_postgres_ip" \
         --network "$network_name" \
         --network-alias "$postgres_container_name" \
         --name $postgres_container_name \
         -p 5432:5432 \
         -v "$postgres_data_volume":/var/lib/postgresql/data postgres:10-alpine
postgres_sid=$(docker run -d --rm \
    --ip "$network_postgres_ip" \
    --network "$network_name" \
    --network-alias "$postgres_container_name" \
    -p 5432:5432 \
    --name $postgres_container_name \
    -v "$postgres_data_volume":/var/lib/postgresql/data postgres:10-alpine)
postgres_instance_ip=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' "$postgres_container_name")
echo "postgres [ $postgres_sid ] on ip < $postgres_instance_ip >"