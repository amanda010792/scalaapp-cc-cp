/**
  * Copyright 2020 Confluent Inc.
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

  //this works 
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
  //createTopic(topicName, props)
  val producer = new KafkaProducer[String, String](props)

  val callback = new Callback {
    override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit = {
      Option(exception) match {
        case Some(err) => println(s"Failed to produce: $err")
        case None =>  println(s"Produced record at $metadata")
      }
    }
  }



  val fileName = "/home/ec2-user/endpoint.csv"
  

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

