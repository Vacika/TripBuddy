#!/usr/bin/env bash
server=127.0.0.1
db=najdiprevoz


db_user=najdiprevoz
export PGPASSWORD="najdiprevoz"

psql -h $server -U $db_user najdiprevoz -c "SET client_min_messages TO WARNING;"
psql -h $server -U $db_user najdiprevoz -c "UPDATE pg_database SET datallowconn = 'false' WHERE datname = '$db'"
psql -h $server -U $db_user najdiprevoz -c "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = '$db';"
psql -h 127.0.0.1 -U najdiprevoz najdiprevoz -c "drop database if exists najdiprevoz;"
psql -h 127.0.0.1 -U najdiprevoz najdiprevoz -c "create database najdiprevoz with owner najdiprevoz;"

#psql -h 127.0.0.1 -U najdiprevoz najdiprevoz -c "UPDATE pg_database SET datallowconn = 'true' WHERE datname = '$db';"
