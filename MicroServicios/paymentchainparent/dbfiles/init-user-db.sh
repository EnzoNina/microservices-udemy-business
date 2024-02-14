#!/bin/bash
set -e

mysql -u"root" -p"$MYSQL_ROOT_PASSWORD" <<-EOSQL
    CREATE USER 'root'@'%' IDENTIFIED BY 'admin';
    CREATE DATABASE customer_service;
    CREATE DATABASE product_service;
    CREATE DATABASE transaction_service;
    GRANT ALL PRIVILEGES ON customer_service.* TO 'root'@'%';
    GRANT ALL PRIVILEGES ON product_service.* TO 'root'@'%';
    GRANT ALL PRIVILEGES ON transaction_service.* TO 'root'@'%';
    FLUSH PRIVILEGES;
EOSQL
