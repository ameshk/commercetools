In order to solve the assignment, I have decided to use Apache Beam. Few reasons to use Beam are :
Unified batch and streaming framework
Dataflow service provided by GCP runs Beam at its core
Runs on multiple runner [dataflow runner, spark runner, flink runner, direct runner]

Markup :  # Heading 1 #
Flow diagram
![alt text](https://github.com/ameshk/commercetools/blob/master/flow_diagram.png?raw=true)

The code is written using java. Some specification of application code:
Codebase is a maven project and has a pom.xml file in root folder
Main code resides in src/main/java. Lot of effort is put into testing this code, the test cases resides in src/test/java
Summary of code flow is - stream of json log data will be received in pubsub topic, read this stream using beam, extract key attributes from this data, store these attributes in big query and store the json logs in GCS.
Main class : com.commercetools.LogIngestion
Detailed comments are added  in the code and the functionality is separated among .java files for better understanding

Infrastructure: GCP platform is used for this solution. At a high level we would require pub/sub, bigquery services. Terraform scripts are the best ways spin up and maintain the environment
Terraform scripts resided in the infra folder.
Terraform modules are created and placed in infra/modules folder
Main terraform scripts resides in infra/infrastructure/main.tf file, this file calls appropriate modules from infra/modules folder
GCS storage is used as backend for terraform

For deployment purposes I have chosen github actions.
.github/workflows contain yaml files that define the CICD pipeline
.github/workflows/infra.yaml file will deploy the terraform and create services/resources in GCP. Terraform commands like init, plan and apply will be executed as part of this pipeline
.github/workflows/dataflow_create.yaml will create a dataflow job. This file will run a maven deploy command that will trigger a dataflow pipeline.
