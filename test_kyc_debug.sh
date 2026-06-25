#!/bin/bash
curl -i -b cookies.txt -X POST "http://localhost:8080/api/v1/kyc/submit?email=customer@test.com"
