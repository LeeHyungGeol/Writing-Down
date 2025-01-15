# Redis 특수 명령어

## 📌 데이터 만료 - EXPIRE
Expiration 데이터를 특정시간 이후에 만료 시키는 기능
TTL(Time To Live) 데이터가 유효한 시간(초 단위)
특징 데이터 조회 요청시에 만료된 데이터는 조회되지 않음
데이터가 만료되자마자 삭제하지 않고, 만료로 표시했다가 백그라운드에서 주기적으로 삭제

### 📝 명령어 
- $ SET greeting hello 
- $ EXPIRE greeting 10
- $ TTL greeting 
- $ GET greeting
- $ SETEX greeting 10 hello

---

## 📌 SET NX/XX
NX: 해당 Key가 존재하지 않는 경우에만 SET
XX: 해당 Key가 이미 존재하는 경우에만 SET
Null Reply: SET이 동작하지 않은 경우 (nil) 응답

### 📝 명령어
  
- `$ SET greeting hello NX` 
- `$ SET greeting hello XX`

---

## 📌 Pub/Sub

<img width="500" alt="스크린샷 2025-01-14 오전 12 40 32" src="https://github.com/user-attachments/assets/352fb1a9-98d1-48fe-b472-f262191240b6" />

Pub/Sub Publisher와 Subscriber가 서로 알지 못해도 통신이 가능하도록 decoupling 된 패턴 Publisher는 Subscriber에게 직접 메시지를 보내지 않고, Channel에 Publish Subscriber는 관심이 있는 Channel을 필요에 따라 Subscribe하며 메시지 수신
vs. Stream 메시지가 보관되는 Stream과 달리 Pub/Sub은 Subscribe 하지 않을 때 발행된 메시지 수신 불가

### 📝 명령어 

- `$ SUBSCRIBE ch:order ch:payment` 
- `$ PUBLISH ch:order new-order`
- `$ PUBLISH ch:payment new-payment`

---

## 📌 Pipeline

<img width="500" alt="스크린샷 2025-01-14 오전 12 40 42" src="https://github.com/user-attachments/assets/443d2990-1e0c-484c-815c-e839348b5447" />

Pipelining
다수의 commands를 한 번에 요청하여 네트워크 성능을 향상 시키는 기술 Round-Trip Times 최소화
대부분의 클라이언트 라이브러리에서 지원
Round-Trip Times
Request / Response 모델에서 발생하는 네트워크 지연 시간

---

## 📌 Transaction
Transaction
없음
Atomicity vs. Pipeline 기술

다수의 명령을 하나의 트랜잭션으로 처리 -> 원자성(Atomicity) 보장
중간에 에러가 발생하면 모든 작업 Rollback
하나의 트랜잭션이 처리되는 동안 다른 클라이언트의 요청이 중간에 끼어들 수
All or Nothing / 모든 작업이 적용되거나 하나도 적용되지 않거나
Pipeline은 네트워크 퍼포먼스 향상을 위해 여러개의 명령어를 한 번에 요청
Transcation은 작업의 원자성을 보장하기 위해 다수의 명령을 하나처럼 처리하는 Pipeline과 Transaction을 동시에 사용 가능

### 📝 명령어

- `$ MULTI`
- `$ INCR foo` 
- `$ DISCARD` 
- `$ EXEC`

---

