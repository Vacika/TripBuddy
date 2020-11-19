#!/usr/bin/env bash
server=127.0.0.1
db=najdiprevoz


db_user=najdiprevoz
export PGPASSWORD="najdiprevoz"

psql -h $server -U $db_user template1 -c "SET client_min_messages TO WARNING;"
psql -h $server -U $db_user template1 -c "UPDATE pg_database SET datallowconn = 'false' WHERE datname = '$db';"
psql -h $server -U $db_user template1 -c "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = '$db';"
psql -h $server -U najdiprevoz template1 -c "drop database if exists $db ;"
psql -h $server -U najdiprevoz template1 -c "create database $db with owner $db ;"

#psql -h 127.0.0.1 -U najdiprevoz najdiprevoz -c "UPDATE pg_database SET datallowconn = 'true' WHERE datname = '$db';"
