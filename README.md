# scalaapp-cc-cp

## demo overview 

### schemas 

[guest info](https://www.mockaroo.com/609a0780) 

[endpoint](https://www.mockaroo.com/8b192a90)

## prerequisites

[Install Latest Confluent CLI](https://docs.confluent.io/confluent-cli/current/install.html) : Ensure it is a 2.x version

## cluster set ups 

### confluent cloud (shore)

#### resource setup 
[Provision a Confluent Cloud Environmnet through the AWS Marketplace](https://docs.confluent.io/cloud/current/billing/ccloud-aws-payg.html) <br />
create an environment for your pilot cluster <br /> 
[Provision a dedicated cluster in Confluent Cloud](https://docs.confluent.io/cloud/current/clusters/create-cluster.html) : select 1 CKU in the region of your choice on AWS <br />


#### user setup 
[SSO setup](https://docs.confluent.io/cloud/current/access-management/authenticate/sso/sso.html) <br />

#### topic creation

log into confluent cloud using the confluent cli and save your credentials <br />
`confluent login --save` <br /> <br />
list your environments and cluster and tell confluent to use them. make sure to also store your cluster id. <br />
`confluent environment list` <br />
ensure the environment you created is selected (has a * by it). if not, run the following command: <br />
`confluent environment use <environmnet id>` <br />
list your clusters <br />
`confluent kafka cluster list` <br />
ensure the cluster you created is select (has a * by it). if not, run the following command: <br /> 
`confluent kafka cluster use <cluster id>` <br />
create an api key for the cli to use <br />
`confluent api-key create --resource <cluster id>` <br />
use the api key created <br /> 
`confluent api-key use <api-key>` <br /> <br />

create endpoint topic in Confluent Cloud (this will be our origin topic for endpoint, which will be mirrored to our ship cluster) <br /> 
`confluent kafka topic create endpoint --partitions 3` <br /> 
you can confirm the topic was created by viewing the topics tab in the cluster on your Confluent Cloud Dashboard or by running the following command: <br />
`confluent kafka topic list` <br /> 

### confluent platform (ship) 

#### cluster setup 

Download Confluent Platform (must be CP 7.0.1) and deploy 3 brokers and 3 zookeepers (1 bk & 1 zk per vm) <br />
[CP Install Guide](https://docs.confluent.io/platform/current/installation/installing_cp/overview.html)

#### topic creation 

guestInfo topic will be created in the cluster linking set up - make sure this gets done <br />

### cluster linking set up 

follow the [Cluster linking hybrid tutorial](https://docs.confluent.io/platform/current/multi-dc-deployments/cluster-linking/hybrid-cp.html) but **make sure to change the CP (ship) cluster topic to guestInfo and the CC (shore) cluster topic to endpoint**. guestInfo topic needs to be created here, but if you created your endpoint topic when setting up your CC cluster you do not need to create the topic here. Make sure you pass the checks that cluster links are set up before moving on to the demo! 

## running the demo 

### downloading the mock data 

download endpoint data (take note of the full path of where you download this for producers) <br />
`curl "https://api.mockaroo.com/api/8b192a90?count=1000&key=09606b20" > "endpoint.csv"` <br /> <br /> 
download guestInfo data (take note of the full path of where you download this for producers) <br /> 
`curl "https://api.mockaroo.com/api/609a0780?count=1000&key=09606b20" > "guestInfo.csv"` <br /> <br /> 

### updating the producers/consumers and configuration files 

In EndpointProducer.scala, update var fileName to point to your endpoint mock data <br /> 
`var fileName = <path to your endpoint mock data` <br /> 
In java.config, add bootstrap server to CC and an API key and secret (for cluster, can create via UI or CLI) <br /> 

### running the code to show cluster linking replication 

produce records to endpoint topic on CC <br />
`sbt "runMain io.confluent.examples.clients.scala.EndpointProducer <java config> endpoint"` <br />
show that records have been written to CC <br /> 
`confluent kafka topic consume endpoint --from-beginning` <br />
show that records have been mirrored to CP <br /> 
`kafka-console-consumer --topic endpoint --from-beginning --bootstrap-server <bootstrap server> --consumer.config $CONFLUENT_CONFIG/CP-command.config` <br />
produce records to guestInfo topic on CP <br /> 
`sbt "runMain io.confluent.examples.clients.scala.GuestInfoProducer"` <br />
show that records have been written to CP <br /> 
`kafka-console-consumer --topic guestInfo --from-beginning --bootstrap-server <bootstrap server> --consumer.config $CONFLUENT_CONFIG/CP-command.config` <br />
show that records have been mirrored to CC <br /> 
`confluent kafka topic consume guestInfo --from-beginning` <br /> 
consume records from guestInfo on CC using scala consumer <br /> 
`sbt "runMain io.confluent.examples.clients.scala.Consumer <java config> guestInfo"` <br /> 
<br /> <br /> 
code can be added to count number of records produced/consumed to ensure there is no data loss <br /> 
<br /> 
view mirror lag: <br /> 
`confluent kafka mirror list --cluster <CC-CLUSTER-ID>`








