resource "google_pubsub_topic" "log_topic" {
  name = var.topic_name
}

resource "google_pubsub_subscription" "log-subscription" {
  name  = var.subscription_name
  topic = google_pubsub_topic.log_topic.name

  depends_on = [google_pubsub_topic.log_topic]
}