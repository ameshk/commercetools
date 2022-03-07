provider "google" {
  project                     = var.project
}

module "pubsub" {
  source                      = "../modules/pubsub"
  subscription_name           = var.subscription_name
  topic_name                  = var.topic_name
}

module "bigquery" {
  source                      = "../modules/bigquery"
  dataset_id                  = var.dataset_id
  friendly_name               = var.friendly_name
  location                    = var.location
  table_id                    = var.table_id
  table_schema                = var.table_schema
}

module "log_storage_bucket" {
  source                      = "../modules/storage"
  bucket_location             = var.log_bucket_location
  bucket_name                 = var.log_bucket_name
}

module "temp_storage_bucket" {
  source                      = "../modules/storage"
  bucket_location             = var.temp_bucket_location
  bucket_name                 = var.temp_bucket_name
}