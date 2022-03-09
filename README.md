In order to solve the assignment, I have decided to use Apache Beam. Few reasons to use Beam are :
* Unified batch and streaming framework
* Dataflow service provided by GCP runs Beam at its core
* Runs on multiple runner [dataflow runner, spark runner, flink runner, direct runner]

## Solution ##
* Webserver will generate log data in json format.
* This data will be pushed to pub/sub topic by webserver
* A dataflow job will be running on GCP, this job will receive stream of pub/sub messages
* Dataflow will write the json data to a GCS location
* Dataflow will also extract key attributes from the json and push it as rows to a Big query table
* Further analytics can be done on the big query table
* For infrastructure I have used terraform
* For deployment and scheduling purpose I have used github actions.

## Flow diagram ##

![alt text](https://github.com/ameshk/commercetools/blob/master/flow_diagram.png?raw=true)

## Application code ##
The code is written using java. Some specification of application code:
* Codebase is a maven project and has a pom.xml file in root folder
* Main code resides in __src/main/java__. A lot of effort is put into testing this code, the test cases reside in __src/test/java__
* Summary of code flow is - stream of json log data will be received in pubsub topic, read this stream using beam, extract key attributes from this data, store these attributes in big query and store the json logs in GCS.
* Main class : __com.commercetools.LogIngestion__
* Detailed comments are added  in the code and the functionality is separated among .java files for better understanding

## Infrastructure code ##
GCP platform is used for this solution. At a high level we would require pub/sub, bigquery services. Terraform is the best way to spin up and maintain the environment
* Terraform scripts reside in the __infra__ folder.
* Terraform modules are created and placed in __infra/modules__ folder
* Main terraform scripts reside in __infra/infrastructure/main.tf__ file, this file calls appropriate modules from infra/modules folder
* GCS storage is used as backend for terraform

## CICD ##
For deployment purposes I have chosen github actions.
* __.github/workflows__ contain yaml files that define the CICD pipeline
* __.github/workflows/infra.yaml__ file will deploy the terraform and create services/resources in GCP. Terraform commands like init, plan and apply will be executed as part of this pipeline
* __.github/workflows/dataflow_create.yaml__ will create a dataflow job. This file will run a maven deploy command that will trigger a dataflow pipeline.

## Question and answer ##

__Question__ What kind of applications can you think of that could be made possible with your
Design?

__Answer__ 
With this solution we can do some log analytics of our application.
Certain KPI’s can be calculated like:
* Rank the endpoints in descending order as per their usage in a given time frame - last day, week or month
* Identify the failures and finding the root cause
* Using this web access log we can predict the future volume of request based on past history data. Machine learning models like __ARIMA__ or __SARIMA__ can be used.
* Average time spent by a user on a website - the token can be common for a session and the session time can be calculated
* The chain of redirect done by the user or the way user navigates through the webpage and click on different links on webpage
* Analysis of the user activity how they convert into sales - User funnel flow ie. number user on landing page, number of user in checkout page and in payment page.
* The analytics can be further enhanced by overlapping with some other data like point of sales, product, customer, user etc.

__Question__ What are the limitations or drawbacks of your design?

__Answer__ The dataflow job is a streaming job and will be up and running - this may cause higher costs. To counter this we can specify the min and max nodes. We can also do the job as a batch and run on fixed intervals. Some code refactor would be necessary if we want to run it on Kubernetes, Dataproc or any other service on other cloud platform [Azure, AWS]

