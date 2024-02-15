#!/bin/bash
set -e

mysql -u"root" -p"$MYSQL_ROOT_PASSWORD" <<-EOSQL
    CREATE USER 'root'@'%' IDENTIFIED BY 'admin';
EOSQL

mysql -u"root" -p"$MYSQL_ROOT_PASSWORD" <<-EOSQL
    CREATE DATABASE customer_service;
    GRANT ALL PRIVILEGES ON customer_service.* TO 'root'@'%';
EOSQL

mysql -u"root" -p"$MYSQL_ROOT_PASSWORD" <<-EOSQL
    CREATE DATABASE product_service;
    GRANT ALL PRIVILEGES ON product_service.* TO 'root'@'%';
EOSQL

mysql -u"root" -p"$MYSQL_ROOT_PASSWORD" <<-EOSQL
    CREATE DATABASE transaction_service;
    GRANT ALL PRIVILEGES ON transaction_service.* TO 'root'@'%';
EOSQL

mysql -u"root" -p"$MYSQL_ROOT_PASSWORD" -e "FLUSH PRIVILEGES;"
