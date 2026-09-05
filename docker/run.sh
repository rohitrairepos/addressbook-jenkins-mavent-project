#!/usr/bin/env sh
set -eu

echo "Starting Addressbook application..."
echo "Container port: 8080"
exec java ${JAVA_OPTS:-} -jar /app/app.jar
