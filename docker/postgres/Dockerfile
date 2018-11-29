
FROM postgres:10

LABEL br.com.mybudget.version="0.0.1-snapshot"
LABEL author="Victor Franca"
LABEL email="victorhugof@gmail.com"
LABEL vendor1="MyBudget"
LABEL br.com.mybudget.release-date="2018-11-23"

# Set Postgresql env variables
ENV LC_ALL=C.UTF-8
ENV POSTGRES_DB=meussaldos
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres
ENV POSTGRES_PORT=5432


USER root

# Copy the sql file
COPY input_files/import-postgres.sql /docker-entrypoint-initdb.d/