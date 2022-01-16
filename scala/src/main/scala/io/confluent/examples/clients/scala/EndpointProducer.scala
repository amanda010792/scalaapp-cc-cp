/**
  * Created using example code from Confluent examples here: https://github.com/confluentinc/examples/tree/7.0.1-post/clients/cloud/scala
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


object EndpointProducer extends App {
  val configFileName = args(0)
  val topicName = args(1)
  val MAPPER = new ObjectMapper
  val props = buildProperties(configFileName)
  val producer = new KafkaProducer[String, String](props)

  val callback = new Callback {
    override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit = {
      Option(exception) match {
        case Some(err) => println(s"Failed to produce: $err")
        case None =>  println(s"Produced record at $metadata")
      }
    }
  }



  val fileName = <path to endpoint mock data> 
  

    for (line <- Source.fromFile(fileName).getLines().drop(1)) { // Dropping the column names
      // Extract Key
      val key = line.split(","){0}

      // Prepare the record to send
      val record: ProducerRecord[String, String] = new ProducerRecord[String, String](topicName, key, line)

      // Send to topic
      producer.send(record)
    }

    producer.flush()
    producer.close()
    println("Wrote records to " + topicName)

  def buildProperties(configFileName: String): Properties = {
    val properties: Properties = new Properties
    properties.load(new FileReader(configFileName))
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    properties
  }

}

