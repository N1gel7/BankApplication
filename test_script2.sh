#!/bin/bash
echo "Deposit 1000 into Customer 1:"
curl -s -w '\nHTTP_STATUS: %{http_code}' -b cookie.txt -X POST http://localhost:8080/api/v1/transactions/deposit -H 'Content-Type: application/json' -d '{"senderEmail":"customer@test.com","amount":1000}'
echo -e "\n\nRegister Customer 2:"
curl -s -X POST http://localhost:8080/api/v1/auth/register -H 'Content-Type: application/json' -d '{"firstName":"Jane","lastName":"Doe","email":"customer2@test.com","password":"password123","phoneNumber":"0987654322","dob":"1995-01-01"}'
echo -e "\nLogin Customer 2:"
curl -s -c cookie2.txt -X POST http://localhost:8080/api/v1/auth/login -H 'Content-Type: application/json' -d '{"email":"customer2@test.com","password":"password123"}'
echo -e "\nSubmit KYC 2:"
curl -s -b cookie2.txt -X POST 'http://localhost:8080/api/v1/kyc/submit?email=customer2@test.com'
echo -e "\nApprove KYC 2 (Admin):"
curl -s -b admin_cookie.txt -X PATCH http://localhost:8080/api/v1/kyc/2/approve
echo -e "\nCreate Account 2:"
curl -s -b cookie2.txt -X POST 'http://localhost:8080/api/v1/accounts?email=customer2@test.com' -H 'Content-Type: application/json' -d '{"accountType":"SAVINGS"}'
echo -e "\n\nTransfer 500 from Customer 1 to Customer 2:"
curl -s -w '\nHTTP_STATUS: %{http_code}' -b cookie.txt -X POST http://localhost:8080/api/v1/transactions/transfer -H 'Content-Type: application/json' -d '{"senderEmail":"customer@test.com","receiverEmail":"customer2@test.com","amount":500}'
