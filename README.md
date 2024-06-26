# Technical assessment Amiloz

## Project details
This project was developed using java 19 and springboot as the backend framework, it also uses a postgresql database and docker as the deployment tool.

## Use project
To run this project you must follow this steps:
* Clone the repository `git clone `
* Then you can use the docker compose file to run everything `docker compose up --build` or `docker compose up --build -d` if you want to run the project in detached mode. 
Beware the first time you run the command it may take a little while, as it must download the images and build the server image. 
* Then you can start using the Rest API provided by the project, that will be explained in the following sections of this document.



# Entity Design
<img src="/media/design-aniloz.drawio.png" style="display:block;float:none;margin-left:auto;margin-right:auto;width:80%">

This entity model was designed with two main things on mind:
* Separate the business core data from the authentication data, so in the event it was necessary, an authentication server could be easily created.
* Make as easily as possible the traceability of the loan process and have all the relevant data stored in the database.

# API description 
you can try all the endpoints described in this section with the attached postman collection: [AmilozDemo.postman_collection.json](AmilozDemo.postman_collection.json)
## 1) Not authenticated endpoints
### 1.1) Create client
#### Description
This endpoint can be used to create a new Amiloz client, this operation verifies username and email uniqueness 
#### Usage
`POST http://localhost:8090/usuarios`

* Body:
```json
{
  "username" : "RandyD45",
  "password" : "Hola456789",
  "name" : "Randy",
  "lastname": "Darrell",
  "email" : "randy@email.com",
  "documentId" : "789456123"
}
```

* Response:
```json
{
  "id": 2,
  "username": "RandyD45",
  "enabled": true,
  "amilozUser": {
    "id": 2,
    "email": "randy@email.com",
    "name": "Randy",
    "lastname": "Darrell",
    "documentId": "789456123"
  },
  "userRoles": [
    {
      "id": 2,
      "name": "CLIENT"
    }
  ]
}
```
### 1.2) Login
#### Description
This endpoint uses the JWT standard to create a new authentication token and a refresh token that can be later exchanged for a new jwt token, both tokens are signed with RSA 256 and different key pairs so the token can't be tampered with.
#### Usage
`POST http://localhost:8090/auth/login`

* Body:
```json
{
    "username": "RandyD45",
    "password": "Hola456789"
}
```

* Response:
```json
{
    "amilozUserId": 2,
    "accessToken": "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJSYW5keUQ0NSIsInBlcm1pc3Npb25zIjpbIkNMSUVOVCJdLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvYXV0aC1zZXJ2aWNlL2F1dGgiLCJpZCI6MiwiZXhwIjoxNzE5MzcwNzg2LCJpYXQiOjE3MTkzNzAxODZ9.NsQrHzG8ORP3aF2h4lVwvUc87ZQtuUzRIhhspYpeD7doMA9JPbjRe8D2arhnnZSDF8kF9_5SDsB3vmpywo89chmcUqCIoSF4fTM4S7UlVJ0UN_CmWuecyZRtfti1QIfRMCBfzJinOx5qFcqk8y0vO1LH4DvfGwG0YjqKvxPothai_JHA4A2YmU3gVOWPnYQT73pkCcsA5Q3KkKivaPQUvHsogQ84f72vCwdMOKlv3-_G_fUMdiOzK0ngu1wV0x1OvXBlYyckMxPmquo541_RRLkAIATLq-8aca8CBfo0aUI9Mb1fOWVAnQlbGCk_jGcwIOqmNfGbWSVXBqFReOpblg",
    "refreshToken": "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvYXV0aC1zZXJ2aWNlL2F1dGgiLCJzdWIiOiJSYW5keUQ0NSIsImlkIjoyLCJleHAiOjE3MTkzNzA4MDYsImlhdCI6MTcxOTM3MDE4Nn0.BIFN091V_Z6fovRPLqqbLoRpxpGt82fzIrt0EO38DsrpdMUXrdg9APZkZWooMo_q2Gdqx9zbP83DdbC7zW89T6grwRdBMZnpxfQy6omNXwI_5QubJuo142J_cnxdiSS-PJR6v3Pm_eM6HcuTFxLQMotl-q-ukY8Jv2bLVtp9Z5mPLrdvFn4SOyzKrMFnSegwEz2YqaizmjVA9qEx2jS0r61CFxoWBg9GFfIPGTAaiVrHWtTS8qiUmxNZ0i619xZ2176qcoTeVhE2-RDFxu2OzosVWOKgHOY3YquBZBHTque4ZlwlXEKaOjgKYOxy6Fe7GfbYuV-AChQeu5AiQeUasw",
    "expiresIn": 1800
}
```

### 1.3) Admin Login
#### Description
This endpoint uses the JWT standard to create a new authentication token and a refresh token that can be later exchanged for a new jwt token, both tokens are signed with RSA 256 and different key pairs so the token can't be tampered with.

By default, the server creates a primordial admin user with the following credentials, that can be configured via the `application-prod.yml` file  
#### Usage
`POST http://localhost:8090/auth/login`

* Body:
```json
{
  "username": "aniloz-admin",
  "password" : "Hola123456"
}
```

* Response:
```json
{
  "amilozUserId": 1,
  "accessToken": "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhbmlsb3otYWRtaW4iLCJwZXJtaXNzaW9ucyI6WyJBTUlMT1otQURNSU4iXSwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2F1dGgtc2VydmljZS9hdXRoIiwiaWQiOjEsImV4cCI6MTcxOTM3NjA2OSwiaWF0IjoxNzE5Mzc0MjY5fQ.tAgBg4ddsIDBivBI4E-4QoPerlf8nBZXMyVg_aMYIlUXJoDDL0c4XR-SaZ5JrD53_GSY9D_wysSyHTrgY0EgB4xrxpmhKgVsWaQ_mrlKTm1Pcf5ad5RmZ1-1_mAxCpxYMe3WWB8b-_WVIO6im9rABoqAsAq1YPHTYxMoINoQdyvHyeGrwoUG9j9gnHSkagxzbD-lBgUFT4DSms79EFnZQiRrkhLGeb2glvF951GHMLCRjCKQzK9KGAQUIXFhQHYZ31my0aX-hRx2rsYeiLCDtb1YOwPJYOzP0E3A2hDD50BTlP9m_LygCFlPcSTyitqjOjYRO5Fn-cArYSp-lp-P8Q",
  "refreshToken": "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvYXV0aC1zZXJ2aWNlL2F1dGgiLCJzdWIiOiJhbmlsb3otYWRtaW4iLCJpZCI6MSwiZXhwIjoxNzE5Mzc2MDg5LCJpYXQiOjE3MTkzNzQyNjl9.YQxV1i0LkJGxmKiLNaxXE6AibZaes89cV2rgDnstEhwOdGynMHFUibHhcxEz1BLtO2tVEkgojnucOl4jC4arF3rdDDyKJrhf0lYiPR6DPdzTldBP9SPkyZfAFMFnbrw6oT7DEJM95LxALEF97tQUzMH4dUKVtJx0Uaxjuwxfw9iQ7ngK_zRJGkGggR3mDyqIGo4JG6cj-AZ2DQ3t5JueQpbSz3Hch3GJFC07C_o5kzzWGxJyaHYzP9xpPIWzv9fiULsK86OmBU9HaeHFIUXjb64CSAahnYbyLJ_RvjUbtunI78Lo1J87shiDI1zPyjri8aTPyR5irIlCnkcS3F7F4w",
  "expiresIn": 1800
}
```

### 2) Authenticated endpoints
To use any of these endpoints you must add a bearer token authentication in your request.
### 2.1) Refresh token
#### Description
This endpoint exchanges the refresh token previously provided by the login endpoint to authenticate the user and create a new jwt token.
#### Usage
`POST http://localhost:8090/auth/refresh`

* Body:
```json
{
  "refreshToken": "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvYXV0aC1zZXJ2aWNlL2F1dGgiLCJzdWIiOiJSYW5keUQ0NSIsImlkIjoyLCJleHAiOjE3MTkzNzc0NzAsImlhdCI6MTcxOTM3NTY1MH0.MUMNr6c8dJ0pj3iaOGUDuN913Z2lmdkjC6dacM9RJH27unjl8fEjqJv1JYzVGWcuPQM9e1kVRsXvoOdaj6wQBlb2zblFy-8VG_e1hhDeI4_JMkJ04teXs26Ib2BrY-_vsNv0z40LXL4rdaG9XZMj1s1KD1NWZTmIPsCwzHWdgpiUGZg6CsUnyChy_ONE1yuQv2FEqsqabtxtBDHsKCQbPdCOylYaTDS8p0I-u3kdQXGZsK03YomanCWonkRVubNigBAWBiHXCGMld_pBkB0aTLoYARTUFJvVe6419xwg96Uiz5Un3IrFPTcbvbrGRNaZV0d_nwhMFqhKt3OaCUdIUA"
}
```

* Response:
```json
{
  "amilozUserId": 2,
  "accessToken": "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJSYW5keUQ0NSIsInBlcm1pc3Npb25zIjpbIkNMSUVOVCJdLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvYXV0aC1zZXJ2aWNlL2F1dGgiLCJpZCI6MiwiZXhwIjoxNzE5Mzc3NDgyLCJpYXQiOjE3MTkzNzU2ODJ9.fPBburCfJ85tBLaq34j2FKsUdh0u9THgS-GTQQSYg0aJQV9E6s4SNtCep-PN1ufmuGv5I7WkyGwCgK_238xEKEuAKxoZ6rMtGZPc6_cAImnsXXZ9ETeF7XeZK8wFiJ04iwDmMyl2Upu8LSjLZL0K0HkKBH5s3vaik5LYiTDNKq4Lx13VqzWF1Lbq6qEdDXXGYUmi_NgNEbQtkMlYVOH774s_ydV014lVgcmv7mu7SwT_gQKqqTWX3lwDQQHLvlOpAmv1rMOYs_jWg5aGb5h1UQXhLJmvlCbBrRgOzok7O_O2GNH6eQTYTQeXFSlnnwDbFSnvtVHnMexo3-IuI1-MiQ",
  "refreshToken": "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvYXV0aC1zZXJ2aWNlL2F1dGgiLCJzdWIiOiJSYW5keUQ0NSIsImlkIjoyLCJleHAiOjE3MTkzNzc1MDIsImlhdCI6MTcxOTM3NTY4Mn0.V9FrjH8vxwfqY5yLkxX93LCOMLok0UwyEY5QzcQii2ZJh-NRzTa-2opYSPhoi1yz4vLT4IHWnq5caj_Vn8G0zNLE9RXB__UPYgVA-azn7HawlJS5oh3Vbqj9n5sL6Ro9mANUMdzrfH3iaVTKhbuLe-PYWBPa20XQJzq5fS8ZRVKrpVqgsjYYE4vbBkPsfAk-QIO4J7CSXhGh6a-KQDbK861IS7Atoqn1_NybVDbzRgDMXoqWYPhBD3VmnqRU8xk5cySf1wvWZz7KC1lVOU0uR1czuFbbyUNJ97L5DCiaWS_SAeAjnzvTaOReElqI46FOLx8l2B10_FVT19o4EnHDIQ",
  "expiresIn": 1800
}
```

### 2.2) Create admin user
#### Description
This endpoint can be used to create a new Amiloz admin user, this operation verifies username and email uniqueness and also requires the authentication token of an admin user.
#### Usage
`POST http://localhost:8090/usuarios`

* Body:
```json
{
  "username" : "RandyD46",
  "password" : "Hola456789",
  "name" : "Randy",
  "lastname": "Darrell",
  "email" : "randyd@email.com",
  "documentId" : "456123"
}
```

* Response:
```json
{
  "id": 52,
  "username": "RandyD46",
  "enabled": true,
  "amilozUser": {
    "id": 52,
    "email": "randyd@email.com",
    "name": "Randy",
    "lastname": "Darrell",
    "documentId": "456123"
  },
  "userRoles": [
    {
      "id": 1,
      "name": "AMILOZ-ADMIN"
    }
  ]
}
```

### 2.3) Create loan offer
#### Description
This endpoint can be used to create a new loan offer for a client, this operation requires the authentication token of an admin user.

#### Special attributes:
* offerCreatorId: for traceability purposes the offer is created with the id of the admin user that created the offer.
* paymentTypeCode: represents the time span in which the payments are going to be made, either **WEEKLY** or **MONTHLY**
* interestTimeSpanCode: represents the time in which the interest is going to be calculated, either **WEEKLY**, **MONTHLY**, **YEARLY**
* interestTypeCode: represents the type of interest that is going to be applied, for this particular service only the **SIMPLE** type of interest is implemented but the code is built in a way where the implementations for the other types of interest can be easily added.
#### Usage
`POST http://localhost:8090/usuarios/{userId}/ofertas`

* Body:
```json
{
  "offerCreatorId" : 1,
  "totalAmount" : 20000,
  "totalInstallments" : 12,
  "interestRate" : 0.10,
  "paymentTypeCode": "WEEKLY",
  "interestTypeCode": "SIMPLE",
  "interestTimeSpanCode" : "YEARLY"
}
```

* Response:
```json
{
  "id": 203,
  "totalAmount": 20000.00,
  "totalInstallments": 12,
  "interestRate": 0.10,
  "accepted": false,
  "paymentType": {
    "id": 1,
    "code": "WEEKLY"
  },
  "interestType": {
    "id": 1,
    "code": "SIMPLE"
  },
  "interestTimeSpan": {
    "id": 2,
    "code": "YEARLY"
  }
}
```

### 2.4) Get loan offers for client
#### Description
This endpoint can be used to retrieve all the client loan offers, this operation requires the authentication token of a client user.

#### Usage
`GET http://localhost:8090/usuarios/{userId}/ofertas`


* Response:
```json
[
  {
    "id": 152,
    "totalAmount": 20000.00,
    "totalInstallments": 10,
    "interestRate": 0.15,
    "accepted": false,
    "paymentType": {
      "id": 1,
      "code": "WEEKLY"
    },
    "interestType": {
      "id": 1,
      "code": "SIMPLE"
    },
    "interestTimeSpan": {
      "id": 2,
      "code": "YEARLY"
    }
  },
  {
    "id": 202,
    "totalAmount": 30000.00,
    "totalInstallments": 10,
    "interestRate": 0.15,
    "accepted": false,
    "paymentType": {
      "id": 2,
      "code": "MONTHLY"
    },
    "interestType": {
      "id": 1,
      "code": "SIMPLE"
    },
    "interestTimeSpan": {
      "id": 2,
      "code": "YEARLY"
    }
  }
]
```

### 2.5) Create loan from loan offer
#### Description
This endpoint can be used for the client to accept and create a loan from a previous loan offer, the offer can only be accepted once, and it will be marked as accepted, this operation requires the authentication token of a client user.

#### Usage
`POST http://localhost:8090/usuarios/prestamo/{loanOfferId}`


* Response:
```json
{
  "id": 1,
  "loanOwner": {},
  "totalAmount": 20000.00,
  "totalInstallments": 12,
  "interestRate": 0.10,
  "payedInFull": false,
  "loanOffer": {
    "id": 1
  },
  "paymentType": {
    "id": 1,
    "code": "WEEKLY"
  },
  "interestType": {
    "id": 1,
    "code": "SIMPLE"
  },
  "interestTimeSpan": {
    "id": 2,
    "code": "YEARLY"
  }
}
```

### 2.6) Get loan installments calendar
#### Description
This endpoint can be used to retrieve all loan installments for a client loan, this operation requires the authentication token of a client user.

#### Special attributes
* totalAmount: it's the base amount plus the interest.
* remainingAmount: represents the remaining part to pay of the total payment if payments were made.
* isPending: this attributes turns to false if the remainingAmount is 0, meaning the installment was totally paid  

#### Usage
`GET http://localhost:8090/usuarios/prestamo/{loanOfferId}/cuotas`


* Response:
```json
[
  {
    "id": 1,
    "paymentDate": "03/07/2024",
    "baseAmount": 1666.67,
    "interestAmount": 38.89,
    "isPending": true,
    "loan": {
      "id": 1
    },
    "totalAmount": 1705.56,
    "remainingAmount": 1705.56
  },
  {
    "id": 2,
    "paymentDate": "10/07/2024",
    "baseAmount": 1666.67,
    "interestAmount": 38.89,
    "isPending": true,
    "loan": {
      "id": 1
    },
    "totalAmount": 1705.56,
    "remainingAmount": 1705.56
  },
  {
    "id": 3,
    "paymentDate": "17/07/2024",
    "baseAmount": 1666.67,
    "interestAmount": 38.89,
    "isPending": true,
    "loan": {
      "id": 1
    },
    "totalAmount": 1705.56,
    "remainingAmount": 1705.56
  },
.
.
.
]
```

### 2.7) Get client loans
#### Description
This endpoint can be used to retrieve all the client active loans, this operation requires the authentication token of a client user.

#### Usage
`GET http://localhost:8090/usuarios/{userId}/prestamos`


* Response:
```json
[
  {
    "id": 1,
    "loanOwner": {},
    "totalAmount": 20000.00,
    "totalInstallments": 12,
    "interestRate": 0.10,
    "payedInFull": false,
    "loanOffer": {
      "id": 1
    },
    "paymentType": {
      "id": 1,
      "code": "WEEKLY"
    },
    "interestType": {
      "id": 1,
      "code": "SIMPLE"
    },
    "interestTimeSpan": {
      "id": 2,
      "code": "YEARLY"
    }
  }
]
```


### 2.8) create payment
#### Description
This endpoint can be used to create a payment for a loan installment, as it was proposed this operation takes into account possible partial payments, for this reason there can be multiple payments related with one installment. This operation requires the authentication token of a client user.

#### Special attributes
* reversed: this attribute tells if a payment has been reversed, as only the non-reversed payments will be taken into account to check if an installment was paid in full

#### Usage
`POST http://localhost:8090/prestamo/pagos`
* Body:
```json
{
    "loanInstallmentId" : 1,
    "amount" : 705.56
}
```

* Response:
```json
{
  "id": 1,
  "installment": {
    "id": 1
  },
  "paymentDate": "26/06/2024",
  "amount": 705.56,
  "reversed": false
}
```

### 2.9) Reverse payment
#### Description
This endpoint can be used to reverse a previous payment for a loan installment, this operation respond either a true or false according to the result. This operation requires the authentication token of a client user. 

#### Usage
`PUT http://localhost:8090/prestamo/pagos/{idPago}/revertir`

* Response:
```text
true
```










