In order to solve the assignment, I have decided to use Apache Beam. Few reasons to use Beam are :
* Unified batch and streaming framework
* Dataflow service provided by GCP runs Beam at its core
* Runs on multiple runner [dataflow runner, spark runner, flink runner, direct runner]

## Flow diagram ##

![alt text](https://github.com/ameshk/commercetools/blob/master/flow_diagram.png?raw=true)

## Application code ##
The code is written using java. Some specification of application code:
* Codebase is a maven project and has a pom.xml file in root folder
* Main code resides in __src/main/java__. Lot of effort is put into testing this code, the test cases resides in __src/test/java__
* Summary of code flow is - stream of json log data will be received in pubsub topic, read this stream using beam, extract key attributes from this data, store these attributes in big query and store the json logs in GCS.
* Main class : __com.commercetools.LogIngestion__
* Detailed comments are added  in the code and the functionality is separated among .java files for better understanding

## Infrastructure code ##
Infrastructure: GCP platform is used for this solution. At a high level we would require pub/sub, bigquery services. Terraform scripts are the best ways spin up and maintain the environment
* Terraform scripts resided in the __infra__ folder.
* Terraform modules are created and placed in __infra/modules__ folder
* Main terraform scripts resides in __infra/infrastructure/main.tf__ file, this file calls appropriate modules from infra/modules folder
* GCS storage is used as backend for terraform

## CICD ##
For deployment purposes I have chosen github actions.
* __.github/workflows__ contain yaml files that define the CICD pipeline
* __.github/workflows/infra.yaml__ file will deploy the terraform and create services/resources in GCP. Terraform commands like init, plan and apply will be executed as part of this pipeline
* __.github/workflows/dataflow_create.yaml__ will create a dataflow job. This file will run a maven deploy command that will trigger a dataflow pipeline.
