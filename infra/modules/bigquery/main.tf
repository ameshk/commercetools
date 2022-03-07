resource "google_bigquery_dataset" "log_ingestion" {
  dataset_id                  = var.dataset_id
  friendly_name               = var.friendly_name
  location                    = var.location
}

resource "google_bigquery_table" "log_table" {
  dataset_id = google_bigquery_dataset.log_ingestion.dataset_id
  table_id   = var.table_id

  schema = var.table_schema

  depends_on = [google_bigquery_dataset.log_ingestion]
}