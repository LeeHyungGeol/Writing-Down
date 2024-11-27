# 💡 MSA를 AWS 클라우드로 이전 3 - ASG, ALB

## ⚡️ MSA와 ALB

- 모노리식 아키텍쳐 - ALB 가 하나의 타겟그룹으로 로드밸런싱
- MSA - ALB 가 여러 타겟그룹으로 로드밸런싱
  - 이때 로드밴런싱하는 방법은 여러가지가 있다.
  - `경로 기반 라우팅`: **URL의 경로를 기반으로 요청을 라우팅** 
    - `/user/*` -> 유저 서비스, `/cart/*` -> 장바구니 서비스
  - `호스트 기반 라우팅`: **서브 도메인을 기준으로 라우팅** 
    - `user.goopang.com` -> 유저 서비스, `cart.goopang.com` -> 장바구니 서비스
  - `가중치 기반 라우팅`, 
  - `HTTP 메서드 기반 라우팅`, 
  - `IP주소 기반 라우팅` 등이 있다.

여기서는 일반적으로 사용되는 **경로 기반 라우팅**을 사용하겠다.

## ⚡️ MSA와 ASG

- 서비스별로 독립적으로 확장, 축소한다.
  - 이 부분은 서비스의 개수만큼 관리할 그룹이 늘어나긴 하지만 필요한 서비스에 대해서만 확장이 가능해져 비용적으로 효율적일 수 있고 
  - 서비스별로 확장을 다르게 설정해줄 수도 있습니다.
- 기능이 나눠져 작은 서비스별로 배포되어 있기 때문에 시작 시간이 더 빨라질 수도 있습니다.

<br/>

<br/>

<br/>

## ⚡️ ALB 통합

### 🔋 Create Target Group

<img width="700" alt="스크린샷 2024-11-26 오후 8 09 09" src="https://github.com/user-attachments/assets/e8f697c4-9bcc-450a-b1a6-f66c648d8a9c">

<img width="700" alt="스크린샷 2024-11-26 오후 8 11 08" src="https://github.com/user-attachments/assets/658cedbc-20fd-4b3f-8bf9-42965c27a653">

<img width="700" alt="스크린샷 2024-11-26 오후 8 10 11" src="https://github.com/user-attachments/assets/3dfb561e-8833-439e-8a3a-503ca6f52c1c">

user-service-group, product-service-group, cart-service-group 를 생성해준다.

--- 

### 🔋 ALB

`Listener and rules` 를 수정해준다.

<img width="700" alt="스크린샷 2024-11-26 오후 8 32 21" src="https://github.com/user-attachments/assets/e0118114-9806-4daf-93ef-f6ab676cc5a3">

**Listener 를 처음 생성할 때**는 `가중치 기반 라우팅`만 설정할 수 있다.
- 기본 경로의 라우팅은 오류 처리를 하도록 설정한다.
- `Return fixed response`: **생성된 listener 의 요청은 고정된 응답값을 반환하게 된다.**

---

<img width="700" alt="스크린샷 2024-11-26 오후 8 34 09" src="https://github.com/user-attachments/assets/e9903c44-e3c3-4af0-9b3e-82cf070d9d99">

- `add rule`

<img width="700" alt="스크린샷 2024-11-26 오후 8 35 43" src="https://github.com/user-attachments/assets/5bd2e991-ca64-4c83-b3ed-7385d771f11f">

- `add condition`

<img width="700" alt="스크린샷 2024-11-26 오후 8 37 03" src="https://github.com/user-attachments/assets/43e5619d-7908-4b77-87e5-a0d30608b5f6">

- `Define rule actions`

<img width="700" alt="스크린샷 2024-11-26 오후 8 37 49" src="https://github.com/user-attachments/assets/f12b9e88-2c76-4972-875e-1f24570cc8c2">

- `Set rule priority`
- **`Default 규칙`은 가장 마지막 우선순위로 설정된다.**

postman 을 통해서 alb 의 DNS name 을 이용하여 테스트 해본다.

user-service-routing-rule, product-service-routing-rule, cart-service-routing-rule 을 생성해준다.

<br/>

<br/>

<br/>

## ⚡️ ASG 통합

### 🔋 Create Launch Template

> **이전보다 더 간단하게 `Launch Template` 를 생성한다.**

- instance 를 선택후 `Actions > Image and templates > Create template from instance`
- 이렇게 하면 선택한 인스턴스를 생성할 때 설정했던 값들이 미리 다 입력이 되어있다.

<img width="600" alt="스크린샷 2024-11-26 오후 9 05 25" src="https://github.com/user-attachments/assets/952de396-7aac-46ca-b00e-bd02e483c1df">

<img width="600" alt="스크린샷 2024-11-26 오후 9 06 27" src="https://github.com/user-attachments/assets/348b524c-c6c7-4245-8904-bdf309e3565c">

- subnet 은 launch template 에서 설정하지 않도록 한다.
- `Shutdown behavior`, `Stop - Hibernate behavior`: 모두 launch template 에서 설정하지 않도록 한다.
- 인스턴스를 종료했을 때와, hibernate 상태에서 어떻게 동작할지를 설정하는 옵션이다.

---

cart-service-template 을 생성할 때 User-Data 부분을 수정해준다.

- user-service, product-service 의 url 부분을 수정해준다.
- 지금은 하나의 instance 의 ip 가 명시적으로 표시되어 있지만,
- 이제는 각각의 서비스가 로드 밸런싱 되도록 라우팅 설정을 했기 때문에, **`ALB 의 DNS name` 을 사용해야 한다.**

### 🔋 Create Auto Scaling Group

<img width="600" alt="스크린샷 2024-11-26 오후 9 19 50" src="https://github.com/user-attachments/assets/da6ecb6e-4b71-45de-ae57-acd788f2d298">

- template 을 하나 설정해준다.

<img width="600" alt="스크린샷 2024-11-26 오후 9 21 49" src="https://github.com/user-attachments/assets/d8862725-f8de-4f4c-a54e-1d0f8f40c934">

- public-subnet **2개**를 선택해준다.

<img width="600" alt="스크린샷 2024-11-26 오후 9 24 34" src="https://github.com/user-attachments/assets/8b4c121f-cc36-4257-895b-8f97e3ef05f7">

- load balancer 를 이미 생성해두었기 때문에 Attach to an existing load balancer 를 선택해준다. 

<img width="600" alt="스크린샷 2024-11-26 오후 9 26 01" src="https://github.com/user-attachments/assets/4fdb4939-c032-462e-9467-f078570fd926">

- docker 덕분에 애플리케이션 시작 시간이 빨라졌기 때문에 health_check 시간도 30초로 변경한다.

user-service-group, product-service-group, cart-service-group 를 생성해준다.

<br/>

<br/>

<br/>

## ⚡️ 정리

### 🔋 MSA와 ALB, ASG

<img width="600" alt="스크린샷 2024-11-26 오후 9 40 52" src="https://github.com/user-attachments/assets/7bc647e2-b71a-4b50-85b9-2de91174c685">

- ALB - 경로 기반 라우팅으로 URL 에 맞게 요청을 서비스에 라우팅
- ASG - 서비스 단위로 트래픽에 따라 스케일링 되도록 설정

### 🔋 개선이 필요한 부분
- 퍼블릭 서브넷에 배치된 EC2
- 이전처럼 private-subnet 에 배치하고 NAT Gateway 를 사용하면 되지만 그렇게 하면 높은 비용이 문제가 된다.

### 🔋 해결 방안

- **`NAT Gateway`, `NAT Instance` 없이 이런 문제를 해결하는 방법을 알아본다.**
- **`VPC Endpoint` 서비스를 사용해본다.**

- 운영을 기준으로 하여 `NAT instance` 를 사용하는 경우는 제외한다.
- **이유는 `NAT instance`는 확장이나 가용성 측면에서 직접 제어해야 하기 때문에 효율적인 운영을 하기에는 부적절하기 때문이다.**

