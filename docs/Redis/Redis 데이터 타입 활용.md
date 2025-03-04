# Redis 데이터 타입 활용

## 📌 One-Time Password

<img width="500" alt="Image" src="https://github.com/user-attachments/assets/94339cb8-9f42-41d0-8fb9-95d926e5890d" />

One-Time Password 인증을 위해 사용되는 임시 비밀번호(e.g. 6자리 랜덤 숫자)

---

## 📌 Distributed Lock

<img width="500" alt="Image" src="https://github.com/user-attachments/assets/01fc3580-4749-4919-bad6-99b12fd124e7" />

Distributed Lock 분산 환경의 다수의 프로세스에서 동일한 자원에 접근할 때, 동시성 문제 해결

---

## 📌 Rate Limiter

<img width="500" alt="Image" src="https://github.com/user-attachments/assets/7a122d49-7a57-4420-b489-cdab88fccbff" />

Rate Limiter 시스템 안정성/보안을 위해 요청의 수를 제한하는 기술 IP-Based, User-Based, Application-Based, etc.
Fixed-window Rate Limiting 고정된 시간(e.g. 1분) 안에 요청 수를 제한하는 방법

---

## 📌 SNS Activity Feed 

<img width="500" alt="Image" src="https://github.com/user-attachments/assets/8df57ed5-3ef6-4cb6-b2ce-974b40fd0f1e" />

Activity Feed: 사용자 또는 시스템과 관련된 활동이나 업데이트를 시간순으로 정렬하여 보여주는 기능
Fan-Out 단일 데이터를 한 소스에서 여러 목적지로 동시에 전달하는 메시징 패턴

---

## 📌 Shopping Cart 

<img width="500" alt="Image" src="https://github.com/user-attachments/assets/f54da1ce-656c-4f26-9a85-af200e36afa1" />

Shopping Cart: 사용자가 구매를 원하는 상품을 임시로 모아두는 가상의 공간
특징 수시로 변경이 발생할 수 있고, 실제 구매로 이어지지 않을 수도 있다

---

## 📌 Login Session

<img width="500" alt="Image" src="https://github.com/user-attachments/assets/76028aa2-cc8b-4ded-aad2-226c85e268d6" />

Login Session
동시 로그인 제한 제한
사용자의 로그인 상태를 유지하기 위한 기술
로그인시 세션의 개수를 제한하여, 동시에 로그인 가능한 디바이스 개수

---

## 📌 Sliding Window Rate Limiter

<img width="500" alt="Image" src="https://github.com/user-attachments/assets/7a11b6cb-8964-46a9-a06b-905de3fcc0e9" />

Sliding Window Rate Limiter 시간에 따라 Window를 이동시켜 동적으로 요청수를 조절하는 기술
vs. Fixed Window Fixed Window는 window 시간마다 허용량이 초기화 되지만, Sliding Window는 시간이 경과함에 따라 window가 같이 움직인다.

---

## 📌 Geofencing

<img width="500" alt="Image" src="https://github.com/user-attachments/assets/ca5a22be-d7cb-4441-8dcc-a84b6f1fa794" />

Geofencing 위치를 활용하여 지도 상의 가상의 경계 또는 지리적 영역을 정의하는 기술
명령어 $ GEOADD gang-nam:burgers 127.025705 37.501272 five-guys
127.025699 37.502775 shake-shack 127.028747 37.498668 mc-donalds 127.027531 37.498847 burger-king
$ GEORADIUS gang-nam:burgers 127.027583 37.497928 0.5 km

---

## 📌 Online Status

<img width="500" alt="Image" src="https://github.com/user-attachments/assets/13d7b354-aa22-4abb-b393-43d2afa95c2a" />

Online Status 사용자의 현재 상태를 표시하는 기능
특징 실시간성을 완벽히 보장하지는 않는다. 수시로 변경되는 값이다.

---

## 📌 Visitors Count

<img width="500" alt="Image" src="https://github.com/user-attachments/assets/1600757f-58d6-45d0-84c3-ebb2a0233303" />

Visitors Count Approximation
경우
명령어 $ PFADD today:users user:1:1693494070 user:1:1693494071 user:2:1693494071
$ PFCOUNT today:users

---

## 📌 Unique Events

<img width="500" alt="Image" src="https://github.com/user-attachments/assets/213948a0-4419-44f5-acd0-976bbe1fd895" />

Unique Events 동일 요청이 중복으로 처리되지 않기 위해 빠르게 해당 item이 중복인지 확인하는 방법
