variable "terraform_backend" {}

variable "project" {}

# pubsub config
variable "topic_name" {}
variable "subscription_name" {}

# bigquery config
variable "dataset_id" {}
variable "friendly_name" {}
variable "location" {}
variable "table_id" {}
variable "table_schema" {}

# log storage bucket name
variable "log_bucket_name" {}
variable "log_bucket_location" {}

# temp storage bucket name
variable "temp_bucket_name" {}
variable "temp_bucket_location" {}