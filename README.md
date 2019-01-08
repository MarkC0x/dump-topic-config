# Dump Topic Config

Simple Kafka AdminClient program to dump out the entire list of configuration parameters for a specified topic.  I find this useful while trying to debug issues, and it prints out the full list of stored values, plus whether the values stored are known to be `Default`, `Read-Only`, or `Senstive` by Topic or Broker.  For the `Default` output the output also states which ConfigSource the value comes from.

`Usage: DumpTopicConfig <bootstrap-server:port> <topic-name>`

Hidden option to list all topics on a given cluster:

`Usage: DumpTopicConfig <bootstrap-server:port> --list`
