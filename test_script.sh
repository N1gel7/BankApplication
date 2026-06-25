#!/bin/bash
echo "Register Customer:"
curl -s -X POST http://localhost:8080/api/v1/auth/register -H 'Content-Type: application/json' -d '{"firstName":"John","lastName":"Doe","email":"customer@test.com","password":"password123","phoneNumber":"1234567890","dob":"1990-01-01"}'
echo -e "\nLogin Customer:"
curl -s -c cookie.txt -X POST http://localhost:8080/api/v1/auth/login -H 'Content-Type: application/json' -d '{"email":"customer@test.com","password":"password123"}'
echo -e "\nSubmit KYC:"
curl -s -b cookie.txt -X POST 'http://localhost:8080/api/v1/kyc/submit?email=customer@test.com'
echo -e "\nRegister Admin:"
curl -s -X POST http://localhost:8080/api/v1/auth/admin/create -H 'Content-Type: application/json' -d '{"firstName":"Admin","lastName":"User","email":"admin@test.com","password":"adminpassword","phoneNumber":"0987654321","dob":"1980-01-01"}'
echo -e "\nLogin Admin:"
curl -s -c admin_cookie.txt -X POST http://localhost:8080/api/v1/auth/login -H 'Content-Type: application/json' -d '{"email":"admin@test.com","password":"adminpassword"}'
echo -e "\nApprove KYC:"
curl -s -b admin_cookie.txt -X PATCH http://localhost:8080/api/v1/kyc/1/approve
echo -e "\nCreate Account:"
curl -s -w '\nHTTP_STATUS: %{http_code}' -b cookie.txt -X POST 'http://localhost:8080/api/v1/accounts?email=customer@test.com' -H 'Content-Type: application/json' -d '{"accountType":"SAVINGS"}'
