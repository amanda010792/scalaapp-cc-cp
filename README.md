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

create endpoint topic in Confluent Cloud (this will be our origin topic for endpoint, which will be mirrored to our ship cluster) <br /> 

### confluent platform (ship) 

create topic 

### cluster linking set up 

## running the demo 

### downloading the mock data 
