/**
  * Adapted from: https://github.com/confluentinc/examples/tree/7.0.1-post/clients/cloud/scala
  */

package io.confluent.examples.clients.scala

import java.io.FileReader
import java.util.Properties
import java.util.Optional

import org.apache.kafka.clients.producer._
import org.apache.kafka.clients.admin.{AdminClient, NewTopic}
import org.apache.kafka.common.errors.TopicExistsException
import java.util.Collections

import io.confluent.examples.clients.scala.model.RecordJSON
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

import scala.util.Try
import scala.io.Source


object GuestInfoProducer extends App {
  val  props = new Properties()
 props.put("bootstrap.servers", <bootstrap servers for CP>)
  
 props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
 props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

 val producer = new KafkaProducer[String, String](props)
   
 val TOPIC="guestInfo"
 val fileName = <guestInfo mock data file path> 

 for (line <- Source.fromFile(fileName).getLines().drop(1)) { // Dropping the column names
      // Extract Key
      val key = line.split(","){0}

      // Prepare the record to send
      val record = new ProducerRecord[String, String](TOPIC, key, line)
      println(key)
      // Send to topic
      producer.send(record)
  }

 producer.close()


}

