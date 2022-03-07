output "pubsub_topic_id" {
  value = google_pubsub_topic.log_topic.id
}

output "pubsub_subscription_id" {
  value = google_pubsub_subscription.log-subscription.id
}