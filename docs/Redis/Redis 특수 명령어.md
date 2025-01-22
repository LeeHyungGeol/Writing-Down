# Redis 특수 명령어

## 📌 데이터 만료 - EXPIRE

> `Expiration`: 데이터를 특정시간 이후에 만료 시키는 기능 
> 
> `TTL(Time To Live)`: 데이터가 유효한 시간(초 단위) - ex) `TTL 60`: 해당 데이터가 저장된 이후 60초동안만 유효하다 

### ✏️ 특징

- 데이터 조회 요청시에 만료된 데이터는 조회되지 않음
- 데이터가 만료되자마자 삭제하지 않고, **만료로 표시**했다가 **백그라운드에서 주기적으로 삭제**

### 📝 명령어 

- `$ SET greeting hello` 
- `$ EXPIRE greeting 10`
- `$ TTL greeting`: 해당 데이터의 만료시간을 조회
- `$ GET greeting`
- `$ SETEX greeting 10 hello`: `SETEX`: SET with EXpiration: 데이터를 저장하면서 동시에 만료시간을 설정

---

## 📌 SET NX/XX

> `NX`: 해당 Key가 존재하지 않는 경우에만 SET
> 
> `XX`: 해당 Key가 이미 존재하는 경우에만 SET
> 
> `Null Reply`: SET이 동작하지 않은 경우 (nil) 응답

**이러한 조건부 옵션을 이용하면 GET 을 이용하여 데이터 유무를 확인한 뒤에 다시 SET 하는 번거로운 과정을 피할 수 있다.**

### 📝 명령어
  
- `$ SET greeting hello NX` 
- `$ SET greeting hello XX`

---

## 📌 Pub/Sub

<img width="500" alt="스크린샷 2025-01-14 오전 12 40 32" src="https://github.com/user-attachments/assets/352fb1a9-98d1-48fe-b472-f262191240b6" />

시스템 사이에 **메시지를 통신하는 패턴** 중 하나

**두 시스템 간의 강한 결합도를 줄일 수 있다**는 것이 가장 큰 장점

> `Pub/Sub` Publisher와 Subscriber가 서로 알지 못해도 통신이 가능하도록 decoupling 된 패턴 
> 
> `Publisher`는 Subscriber에게 직접 메시지를 보내지 않고, `Channel`에 Publish, `Subscriber`는 관심이 있는 Channel을 필요에 따라 Subscribe하며 메시지 수신


- `Redis Stream`은 **메시지가 보관**되는 `Stream`
- 과 달리 `Pub/Sub`은 **Subscribe 하지 않을 때 이미 발행된 메시지는 수신 불가**(`fire and forget 방식`)

### 📝 명령어 

- `$ SUBSCRIBE ch:order ch:payment` 
- `$ PUBLISH ch:order new-order`
- `$ PUBLISH ch:payment new-payment`

---

## 📌 Pipeline

<img width="500" alt="스크린샷 2025-01-14 오전 12 40 42" src="https://github.com/user-attachments/assets/443d2990-1e0c-484c-815c-e839348b5447" />

**`Pipelining`을 통해 Network 통신시간을 줄임으로써 전체적인 지연시간을 줄일 수 있다!!!**

> `Pipelining`:  **다수의 commands를 한 번에 요청하여 네트워크 성능을 향상 시키는 기술**

- **`Round-Trip Times` 을 줄여서 Network Times 를 최소화하는 기술**
- 대부분의 Redis 클라이언트 라이브러리에서 지원
- `Round-Trip Times`란? _Request / Response 모델에서 발생하는 네트워크 지연 시간_

---

## 📌 Transaction

> `Transaction`: 다수의 명령을 하나의 트랜잭션으로 처리 -> 원자성(Atomicity) 보장 
> 
> 중간에 에러가 발생하면 모든 작업 Rollback, 작업의 원자성을 보장하기 위해 다수의 명령을 하나처럼 처리하는 기술
>
> 하나의 트랜잭션이 처리되는 동안 다른 클라이언트의 요청이 중간에 끼어들 수 없음

- `Atomicity`: `All or Nothing` / 모든 작업이 적용되거나 하나도 적용되지 않거나
- vs. `Pipeline`은 네트워크 퍼포먼스 향상을 위해 여러개의 명령어를 한 번에 요청: 요청을 한번에 전달할 뿐 다수의 명령어가 개별적으로 수행된다.
- Pipeline과 Transaction을 **동시에 사용 가능**

### 📝 명령어 - MULTI

- `$ MULTI`: 트랜잭션 시작
- `$ INCR foo` 
- `$ DISCARD`: 트랜잭션 rollback
- `$ EXEC`: 트랜잭션 commit

---

