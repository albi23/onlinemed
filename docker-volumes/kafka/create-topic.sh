#!/bin/bash
# create-topic.sh


COUNTER=$1

# Wait until Kafka is ready
if ! nc -z kafka 9092; then
  sleep 1
  echo "Wait for kafka to start $COUNTER"
  echo "Wait for kafka to start $COUNTER" >> initial-setup.log
  COUNTER=$(( COUNTER + 1 ))
  /bin/bash $0 $COUNTER &
else
  # Create initial topic
  /bin/kafka-topics --create --topic onlinemed.queuing.received_mails.json --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
fi