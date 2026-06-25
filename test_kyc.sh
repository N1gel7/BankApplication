#!/bin/bash
# 1. Login as customer
COOKIE=$(curl -s -c cookies.txt -X POST http://localhost:8080/api/v1/auth/login -H "Content-Type: application/json" -d '{"email": "customer@test.com", "password": "password123"}')
# 2. Submit KYC
curl -s -b cookies.txt -X POST "http://localhost:8080/api/v1/kyc/submit?email=customer@test.com"
# 3. Login as admin
ADMIN_COOKIE=$(curl -s -c admin_cookies.txt -X POST http://localhost:8080/api/v1/auth/login -H "Content-Type: application/json" -d '{"email": "superadmin@test.com", "password": "superpassword"}')
# 4. Check pending
curl -s -b admin_cookies.txt http://localhost:8080/api/v1/kyc/pending
