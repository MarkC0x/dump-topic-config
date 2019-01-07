package com.markcox.kafka.adminclient;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.config.ConfigResource;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class DumpTopicConfig {

        public static void main(String[] args) throws InterruptedException, ExecutionException {

            if (args.length != 2) {
                System.out.println("Usage: DumpTopicConfig <bootstrap-server:port> <topic-name>");
                System.out.println();
                System.exit(0);
            }

            Properties properties = new Properties();
            properties.setProperty("bootstrap.servers", args[0]);

            AdminClient admin = AdminClient.create(properties);

            if (Objects.equals(args[1], new String("--list"))) {
                ListTopicsResult topics = admin.listTopics();
                Set<String> topicNames = topics.names().get();
                for(String topic: topicNames) {
                    System.out.println(topic);
                }
                System.exit(0);
            }

            Collection<ConfigResource> configResourceCollection = new ArrayList<>();
            ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, args[1]);
            configResourceCollection.add(configResource);

            DescribeConfigsResult configsInfo = admin.describeConfigs(configResourceCollection);
            while (!configsInfo.all().isDone()) { }

            Map<ConfigResource, Config> configMap = configsInfo.all().get();
            Config config = configMap.get(configResource);

            System.out.println();
            System.out.print(String.format("%-50s", "Configuration Parameter"));
            System.out.print(String.format("%-30s", "Value"));
            System.out.print(String.format("%-15s", "Default"));
            System.out.print(String.format("%-15s", "Read Only"));
            System.out.print(String.format("%-15s", "Sensitive"));
            System.out.print(String.format("%-15s", "Source"));
            System.out.println();
            System.out.print(String.format("%-50s", "-----------------------"));
            System.out.print(String.format("%-30s", "-----"));
            System.out.print(String.format("%-15s", "-------"));
            System.out.print(String.format("%-15s", "---------"));
            System.out.print(String.format("%-15s", "---------"));
            System.out.print(String.format("%-15s", "-----"));
            System.out.println();

            // Sort config.entries()
            List<ConfigEntry> configList = new ArrayList(config.entries());
            Collections.sort(configList,
                    (objectOne, objectTwo) -> objectOne.name().compareTo(objectTwo.name()));

            for (ConfigEntry configEntry : configList) {
                System.out.print(String.format("%-50s", configEntry.name()));
                System.out.print(String.format("%-30s", configEntry.value()));
                System.out.print(String.format("%-15s", configEntry.isDefault()));
                System.out.print(String.format("%-15s", configEntry.isReadOnly()));
                System.out.print(String.format("%-15s", configEntry.isSensitive()));
                System.out.print(String.format("%-15s", configEntry.source()));
                System.out.println();
            }
            System.out.println();

        }
}
