### 회원가입


```
http://localhost:8080/api/auth/signup

{
  "username": "임정혁",
  "email": "wjdgur@gdgoc.com",
  "password": "thisispwd",
  "role": "USER"
}

1
```

<img width="1258" height="555" alt="image" src="https://github.com/user-attachments/assets/2b52666b-1f5a-413c-b174-378708407fbe" />

### 로그인

```
http://localhost:8080/api/auth/login

{
  "email": "wjdgur@gdgoc.com",
  "password": "thisispwd"
}

{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3amRndXJAZ2Rnb2MuY29tIiwiYXV0aCI6IlVTRVIiLCJpYXQiOjE3NjI2MjQwMDksImV4cCI6MTc2MjYyNzYwOX0.MIs8jZuuavUsPsaavuWxannNfrlZayQj-8luhh6TGgo",
    "tokenType": "Bearer"
}
```

<img width="1257" height="631" alt="image" src="https://github.com/user-attachments/assets/e725ea46-a5ad-453c-8311-e9e8fb6e7356" />



### 내 정보 조회

<img width="1243" height="651" alt="image" src="https://github.com/user-attachments/assets/1e93b5e8-0143-4c04-bff0-92054924589a" />

### Todo 생성

```
http://localhost:8080/api/todos

{
  "title": "이상윤 교수님 필기 대신 해드리기",
  "description": "이상윤 교수님은 생각보다 엄청난 악필이시다.",
  "priority": "HIGH",
  "dueDate": "2025-11-09"
}

{
    "id": 1,
    "userId": 1,
    "username": "임정혁",
    "title": "이상윤 교수님 필기 대신 해드리기",
    "description": "이상윤 교수님은 생각보다 엄청난 악필이시다.",
    "completed": false,
    "priority": "HIGH",
    "dueDate": "2025-11-09",
    "createdAt": "2025-11-09T02:54:49.893817",
    "updatedAt": "2025-11-09T02:54:49.893817"
}



```

<img width="1225" height="757" alt="image" src="https://github.com/user-attachments/assets/fb5ce041-9056-4b2f-9fbc-d6e868cb83c1" />

### 모든 Todo 조회

```
http://localhost:8080/api/todos
```

<img width="1259" height="796" alt="image" src="https://github.com/user-attachments/assets/e7f73a6b-f73b-4dc1-b641-9e5f5941db36" />

### 특정 Todo 조회

```
http://localhost:8080/api/todos/1
```

<img width="1240" height="763" alt="image" src="https://github.com/user-attachments/assets/87585cbf-0459-44b4-bf24-2f0ef5594017" />

### Todo 수정

```
http://localhost:8080/api/todos/1

{
  "title": "이상윤 교수님 연필 잡는 법 가르쳐드리기",
  "completed": true,
  "priority": "URGENT"
}

{
    "id": 1,
    "userId": 1,
    "username": "임정혁",
    "title": "이상윤 교수님 연필 잡는 법 가르쳐드리기",
    "description": "이상윤 교수님은 생각보다 엄청난 악필이시다.",
    "completed": true,
    "priority": "URGENT",
    "dueDate": "2025-11-09",
    "createdAt": "2025-11-09T02:54:49.893817",
    "updatedAt": "2025-11-09T02:54:49.893817"
}


```

<img width="1269" height="771" alt="image" src="https://github.com/user-attachments/assets/87e8d6f2-a924-4be0-b41a-7b357314abac" />

### 완료된 (미완료) Todo 필터링

```
http://localhost:8080/api/todos/filter/completed?completed={완료 상태}

[
    {
        "id": 1,
        "userId": 1,
        "username": "임정혁",
        "title": "이상윤 교수님 연필 잡는 법 가르쳐드리기",
        "description": "이상윤 교수님은 생각보다 엄청난 악필이시다.",
        "completed": true,
        "priority": "URGENT",
        "dueDate": "2025-11-09",
        "createdAt": "2025-11-09T02:54:49.893817",
        "updatedAt": "2025-11-09T02:58:43.407226"
    }
]

```

<img width="1313" height="775" alt="image" src="https://github.com/user-attachments/assets/63cec842-7f56-4655-8250-d9e2fd5a81ed" />


<img width="1279" height="548" alt="image" src="https://github.com/user-attachments/assets/d298ac13-4fde-40ab-be64-12e4ce45b00b" />

### 우선순위로 필터링

```
http://localhost:8080/api/todos/filter/priority?priority={우선순위}

[
    {
        "id": 2,
        "userId": 1,
        "username": "임정혁",
        "title": "이상윤 교수님 필기 대신 해드리기",
        "description": "이상윤 교수님은 생각보다 엄청난 악필이시다.",
        "completed": false,
        "priority": "LOW",
        "dueDate": "2025-11-09",
        "createdAt": "2025-11-09T03:01:44.625612",
        "updatedAt": "2025-11-09T03:01:44.625612"
    },
    {
        "id": 3,
        "userId": 1,
        "username": "임정혁",
        "title": "토마토 가서 밥 먹기",
        "description": "토마토 밥 맛있음",
        "completed": false,
        "priority": "LOW",
        "dueDate": "2025-11-01",
        "createdAt": "2025-11-09T03:02:12.725613",
        "updatedAt": "2025-11-09T03:02:12.725613"
    }
]

```

<img width="1265" height="833" alt="image" src="https://github.com/user-attachments/assets/d47b7018-1760-4b3e-a160-2fd0db4254c1" />

### 마감일 지난 Todo 조회

```
http://localhost:8080/api/todos/filter/overdue

[
    {
        "id": 3,
        "userId": 1,
        "username": "임정혁",
        "title": "토마토 가서 밥 먹기",
        "description": "토마토 밥 맛있음",
        "completed": false,
        "priority": "LOW",
        "dueDate": "2025-11-01",
        "createdAt": "2025-11-09T03:02:12.725613",
        "updatedAt": "2025-11-09T03:02:12.725613"
    }
]

```

<img width="1286" height="805" alt="image" src="https://github.com/user-attachments/assets/0a1f4425-6659-4876-a2f2-f97a5bce2bd5" />

### Todo 삭제

```
http://localhost:8080/api/todos/1
```

<img width="1234" height="575" alt="image" src="https://github.com/user-attachments/assets/dca39fea-2829-4ce4-957a-b1473c49ebc3" />





