# About Redis

`Remote Dictionary Server`

> Redis 는 In-Memory Key-Value 저장소로, 데이터를 메모리에 저장하고 빠르게 조회할 수 있는 오픈소스 데이터베이스이다.

- 다수의 서버를 사용하는 분산 환경의 서버가 공통으로 사용할 수 있는 해시 테이블
- Remote 라는 의미는 Redis 가 각각의 서버에 local 하게 존재하지 않고 다수의 서버에서 공통으로 사용할 수 있도록 원격에 존재한다는 의미
- Dictionary 라는 의미는 Key-Value 저장소를 의미한다.
- 표준 C 로 작성된 오픈소스 In-Memory 데이터 저장소(Open Source In-Memory Data Store written in ANSI C)
  - In-Memory 데이터 저장소: Redis 가 백업을 제외한 모든 데이터를 Ram 에 저장하기 때문이다.

<br/>

---

## 📌 **Redis 특징**

- `In-Memory`: 모든 데이터를 RAM 에 저장(백업/스냅샷 제외: 백업/스냅샷은 디스크를 일부 사용)
- `Single-Threaded`: 단일 스레드로 모든 task 처리
- `Cluster Mode`: **다중 노드**에 데이터를 **분산 저장**하여 안정성 & 고가용성 제공 
- `Persistence`: Redis 는 In-Memory DB 라는 특성상 **주로 휘발성** 데이터를 저장하지만,
  - `RDB(Redis Database) + AOF(Append only file)` 라는 옵션을 통해 **영속성** 옵션 제공 
- `Pub/Sub`: Pub/Sub 패턴을 지원하여 손쉬운 어플리케이션 개발(e.g 채팅, 알림

<br/>

---

## 📌 **Redis 장점**

- `높은 성능⭐️⭐️`: 모든 데이터를 메모리에 저장하기 때문에 매우 빠른 읽기/쓰기 속도 보장 
- `다양한 Data Type 지원⭐️`: Redis에서 지원하는 Data type을 잘 활용하여 다양한 기능 구현 
- `다양한 클라이언트 라이브러리`: Python, Java, JavaScript 등 다양한 언어로 작성된 클라이언트 라이브러리 지원하기 때문에 백엔드와 쉽게 연동 가능 
- `다양한 사례 / 강한 커뮤니티`: Redis를 활용하여 비슷한 문제를 해결한 사례가 많고, 커뮤니티 도움 받기 쉬움

<br/>

---

## 📌 **Redis 활용 사례**

- `Caching`: `임시 비밀번호(One-Time Password)`, `로그인 세션(Session)` 과 같은 데이터를 Redis 에 캐싱했다가 사용하는 경우가 가장 많다.
- `Rate Limiter`: `Fixed-Window` / `Sliding-Window Rate Limiter(비율 계산기)`
  - 보통 서버에서 특정 API 에 대한 요청 횟수를 제한하기 위해 사용하는 기술
  - Redis 를 이용하면 간단하게 구현 가능
- `Message Broker`: `메시지 큐(Message Queue)`
  - Redis 의 list 나 streams 같은 데이터 타입을 활용하여 Message Broker 를 구현하여 다양한 서비스 간의 커플링을 줄일 수 있다. 
- `실시간 분석 / 계산`: `순위표(Rank / Leaderboard)`, `반경 탐색(Geofencing)`, `실시간 방문자 수 계산(Visitors Count)`
- `실시간 채팅`: `Pub/Sub 패턴`

<br/>

---

## 📌 **Redis 의 Persistence**

- `Persistence(영속성)`: 
  - Redis는 **주로 캐시**로 사용되지만 데이터 영속성을 위한 옵션도 제공한다. 
  - 데이터 손실 방지를 위해 `SSD`와 같은 **영구적인 저장 장치에 데이터 저장**
- `RDB(Redis Database)`: **특정 시간에 스냅샷(snapshot)을 생성하는 기술**이다.
  - Point-in-time Snapshot -> 재난 복구(Disaster Recovery) 또는 복제에 주로 사용
  - **장애가 발생했을 때 특정 시점에 스냅샷으로 빠르게 캐시를 되돌리거나 동일한 데이터를 가진 캐시를 복제할 때 주로 사용된다.**
  - 스냅샷의 특성상 새로운 스냅샷이 생성되기 이전에 일부 데이터 유실의 위험이 있고, 
  - 스냅샷 생성 중에 전체적인 redis 서버의 성능 저하가 발생하여 클라이언트 요청 지연 발생 
- `AOF(Append Only File)`: 
  - Redis에 적용되는 Write 작업을 모두 log로 저장 
  - **데이터 유실의 위험이 적지만, 재난 복구시 Write 작업을 다시 적용하기 때문에 스냅샷 방식 보다 복구되는 속도가 느림** 
- `RDB + AOF`: 함께 사용하는 옵션 제공

<br/>

---

## 📌 **Caching**

> `캐싱(Caching)`
- 데이터를 빠르게 읽고 처리하기 위해 임시로 저장하는 기술
- 계산된 값을 임시로 저장해두고, 동일한 계산 / 요청 발생시 다시 계산하지 않고 저장된 값 바로 사용
- `캐싱(Caching)`은 기술 이름이고, `캐시(Cache)` = **임시 저장소**이다.

**사용 사례**
- `CPU 캐시`: CPU와 RAM의 속도 차이로 발생하는 지연을 줄이기 위해 L1, L2, L3 캐시 사용
- `웹 브라우저 캐싱:` 웹 브라우저가 웹 페이지 데이터를 로컬 저장소에 저장하여 해당 페이지 재방문시 사용
- `DNS 캐싱 `: 이전에 조회한 도메인 이름과 해당하는 IP 주소를 저장하여 재요청시 사용
  - 직접 도메인 연결을 할 떄, `TTL`설정 옵션을 하게 되는데, 이때 1분, 5분에 따라서 해당 데이터의 캐싱 시간이 결정된다.
- `데이터베이스 캐싱`: MySQL과 같은 데이터베이스는 버퍼풀이라는 곳 자주 사용되는 데이터를 메모리에 미리 올려놓는다. 
  - 조회나 계산 결과를 저장하여 재요청시 사용
- `CDN`: 원본 서버의 컨텐츠를 PoP 서버에 저장하여 사용자와 가까운 서버에서 요청 처리
- `어플리케이션 캐싱`: 어플리케이션에서 데이터나 계산 결과를 캐싱하여 반복적 작업

---

### 📍**Cache hit / Cache miss / Cache aside pattern**

- `Cache hit`: 캐시에 데이터가 존재하여 빠르게 처리되는 경우
- `Cache miss`: 캐시에 데이터가 존재하지 않아 원본 데이터를 조회하는 경우

<img width="500" alt="스크린샷 2024-12-20 오후 3 48 03" src="https://github.com/user-attachments/assets/427cc6b0-3f7c-4edc-a8a1-35316bc35cc6" />

**Cache aside pattern**

- `Cache miss` 발생시, 캐시에 데이터가 없는 경우, 원본 스토리지에서 데이터를 조회하여 캐시에 저장하는 패턴
- 가장 흔하고 반드시 알아야 하는 패턴
- 이외에도, `Write-through`, `Write-behind`, `Write-around` 등 다양한 캐시 패턴이 존재

<img width="500" alt="스크린샷 2024-12-20 오후 3 48 30" src="https://github.com/user-attachments/assets/c9b2e004-c7a6-4e99-b41c-4564402fec28" />
