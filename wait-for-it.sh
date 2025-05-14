#!/usr/bin/env bash
# wait-for-it.sh

host="$1"
port="$2"

until nc -z "$host" "$port"; do
  echo "[$(date)] Waiting for $host:$port to be ready..."
  sleep 2
done

exec "${@:3}"
