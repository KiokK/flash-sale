#!/usr/bin/env bash

set -euo pipefail

BOOTSTRAP="${BOOTSTRAP_SERVER:-kafka:19092}"
RF="${REPLICATION_FACTOR:-1}"                # 1 in light profile; 3 in base
RETENTION_MS="${RETENTION_MS:-604800000}"    # 7 days
KT=/opt/kafka/bin/kafka-topics.sh

create() {
  local name="$1" partitions="$2"
  "$KT" --bootstrap-server "$BOOTSTRAP" --create --if-not-exists \
    --topic "$name" --partitions "$partitions" --replication-factor "$RF" \
    --config "retention.ms=$RETENTION_MS"
}

for t in order.events order.commands \
         inventory.commands inventory.replies inventory.events \
         payment.commands payment.replies \
         ticket.commands ticket.replies \
         saga.events; do
  create "$t" 6
done

for t in order inventory payment ticket saga; do
  create "$t.dlq" 3
done

echo "Topics:"
"$KT" --bootstrap-server "$BOOTSTRAP" --list
