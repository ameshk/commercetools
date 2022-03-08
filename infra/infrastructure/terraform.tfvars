terraform_backend = "backend-bucket"

project = "test-project"

# pubsub config
topic_name = "log-topic"
subscription_name = "log-subscription"

# bigquery config
dataset_id = "web-log"
friendly_name = "web-log-v"
location = "EU"
table_id = "logs"
table_schema = "[  {    "name": "url",    "type": "STRING",    "mode": "NULLABLE"  },  {    "name": "region",    "type": "STRING",    "mode": "NULLABLE"  },  {    "name": "method",    "type": "STRING",    "mode": "NULLABLE"  },  {    "name": "status",    "type": "INTEGER",    "mode": "NULLABLE"  },  {    "name": "log_type",    "type": "STRING",    "mode": "NULLABLE"  },  {    "name": "received_time",    "type": "BIGINT",    "mode": "NULLABLE"  }]"

# log storage bucket name
log_bucket_name = "log-bucket"
log_bucket_location = "EU"

# temp storage bucket name
temp_bucket_name = "temp-bucket"
temp_bucket_location = "EU"
