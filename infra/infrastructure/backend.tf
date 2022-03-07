terraform {
  backend "gcs" {
    bucket  = var.terraform_backend
    prefix  = "terraform/state"
  }
}
