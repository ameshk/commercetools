output "table_id" {
  value = google_bigquery_table.log_table.id
}

output "dataset_id" {
  value = google_bigquery_dataset.log_ingestion.id
}