#!/bin/sh

# Ensure the app always talks to the Railway MySQL instance when running locally.
# Respect any pre-defined DB_URL/DB_USER/DB_PASSWORD so Railway deploys can inject their own values.
if [ -z "$DB_URL" ]; then
  export DB_URL="jdbc:mysql://switchback.proxy.rlwy.net:40439/railway?serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=true&connectTimeout=30000&socketTimeout=60000"
fi

if [ -z "$DB_USER" ]; then
  export DB_USER="root"
fi

if [ -z "$DB_PASSWORD" ]; then
  export DB_PASSWORD="RfpxZHvphzVbjvKwVWOLqBFsucCezmtb"
fi
