/**
  * Adapted from: https://github.com/confluentinc/examples/tree/7.0.1-post/clients/cloud/scala
  */
package io.confluent.examples.clients.scala

import java.io.FileReader
import java.time.Duration
import java.util.{Collections, Properties}
import java.util

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import io.confluent.examples.clients.scala.model.RecordJSON

import scala.collection.JavaConversions._

  object Consumer extends App {
    val configFileName = args(0)
    val topicName = args(1)
    val props = buildProperties(configFileName)
    val consumer = new KafkaConsumer[String, String](props)
    val MAPPER = new ObjectMapper
    consumer.subscribe(Collections.singletonList(topicName))
    while(true) {
      //println("Polling")
      val records = consumer.poll(Duration.ofSeconds(1))
      for (record <- records) {
        val key = record.key()
        val value = record.value()
        println(s"Consumed record with key $key and value $value")
      }
    }
    consumer.close()
    
    def buildProperties(configFileName: String): Properties = {
      val properties = new Properties()
      properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
      properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
      properties.put(ConsumerConfig.GROUP_ID_CONFIG, "scala_example_group")
      properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
      properties.load(new FileReader(configFileName))
      properties
    }

}
