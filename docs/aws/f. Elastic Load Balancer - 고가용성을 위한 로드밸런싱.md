# Elastic Load Balancer - 고가용성을 위한 로드밸런싱

## ⚡️ 확장성(Scalability)

- 트래픽이 많아짐에 따라 처리할 수 있는 서버의 성능이나 양을 키우는 것을 의미한다.
- `수직확장`과 `수평확장`
- AWS 서비스를 이용하면 자동으로 확장하기 쉽다.

---

### 🔋 확장성(Scalability) - 수직 확장(Scale-Up)

<img width="400" alt="스크린샷 2024-10-23 오후 7 20 32" src="https://github.com/user-attachments/assets/c8d6449b-3bb6-4129-915d-a28c195865e9">

> **인스턴스 자체의 스펙을 키우는 것**

- `Scale-Up`, Vertical Scaling
- **`DB` 같이 분산하기 힘든 시스템에 주로 사용**
- 다시 줄이는 것은 `Scale-Down`

---

### 🔋 확장성(Scalability) - 수평 확장(Scale-out)

<img width="400" alt="스크린샷 2024-10-23 오후 7 20 43" src="https://github.com/user-attachments/assets/f08c8479-fe36-4f63-9c78-f4a07dfe47d4">

> **인스턴스의 개수를 늘리는 것**

- `Scale-Out`, Horizon Scaling
- AWS에서는 탄력성이라고도 부릅니다
- 일반적으로 EC2같은 서버는 수평 확장을 사용
- 다시 줄이는 것을 `Scale-In`

---

### 🔋 수직 확장 VS 수평 확장

<img width="400" alt="스크린샷 2024-10-23 오후 7 15 05" src="https://github.com/user-attachments/assets/8780c229-32cf-435e-a8df-e8b93834801d">

- 더 쉬운 방법은 수직 확장
- 수직 확장은 확장에 제한
  1. 확장 시 다운 타임이 있을 수 있습니다(유저들에게 서비스를 제공하지 못하는 시간)
  2. AWS Instance 유형의 최대 크기가 정해져 있기 때문이다.(물론 이정도의 스펙이면 웬만한 서비스들은 문제 없이 서비스 제공이 가능할 수도 있다.)
  3. 수직 확장을 사용시엔 가용역역이 하나다. 이 하나의 가용영역에 문제가 생기면 서비스를 제공할 수 없게 될 수 있다.
- **이러한 문제들과 고가용성 서비스를 위해 수평확장을 지향**

<br/>

## ⚡️ 고가용성

<img width="150" alt="스크린샷 2024-10-23 오후 7 15 20" src="https://github.com/user-attachments/assets/d4a1ace1-e0d5-4ce7-b548-aa2c3185eeaf">

<img width="300" alt="스크린샷 2024-10-23 오후 7 15 32" src="https://github.com/user-attachments/assets/9ad4f577-0c16-42d0-8fd0-9bd0c900ee83">

> **서비스가 오랜 기간 동안 지속적으로 정상 운영이 가능한 성질**

- 일반적으로 수평확장과 같이 사용되는 개념
- AWS에서 고가용성은 인스턴스나 시스템이 여러 가용영역에 가동중인 것을 의미
- 가용역역이 하나인 경우엔 이 가용역역에 문제가 발생한다면 서비스를 제공하지 못할 수 있지만, 가용역역이 2개 이상이라면 하나의 가용영역에 문제가 발생해도 다른 가용영역에서 서비스를 제공할 수 있다.

*그렇다면 트래픽을 어떻게 정상적인 인스턴스로 나눠주고 전달할까?*

<br/>

## ⚡️ 로드밸런싱

> **`로드밸런싱`: 외부에서 들어오는 트래픽을 제어해서 여러 EC2 인스턴스 서버에 균등하게 배포하는 방법** 

- 이런 역할을 하는 서비스를 로드밸런서
- AWS에서 제공하는 로드밸런서 `Elastic Load Balancer`

<br/>

## ⚡️ Elastic Load Balancer(ELB)

- AWS 에서 완전히 관리해주는 서비스
- 직접 구현이나 확장에 신경 쓰지 않아도 되고 비용도 저렴하다.
- 다른 AWS 서비스와 쉬운 통합

> **`ELB`와 `EC2`를 통합하면 `고가용성`, `확장성` 등의 장점**

### 🔋 ELB의 장점

> **보안 강화**

<img width="300" alt="스크린샷 2024-10-23 오후 7 16 04" src="https://github.com/user-attachments/assets/595f2e60-55a8-4042-80d0-0c8befcc572d">

- Load Balancer가 없이 여러 인스턴스로 분배하게 되면 트래픽은 여러 엔드포인트로 나눠지게 된다. 
- 이때 SSL 인증서를 적용하려면 나눠지는 엔드포인트 개수만큼 작업이 늘어난다.
- 수평 확장의 무한히 늘어날 수 있는 특징과도 맞지 않다.

<img width="300" alt="스크린샷 2024-10-23 오후 7 16 22" src="https://github.com/user-attachments/assets/aae9c85f-5761-4e31-9cbd-954e927a26a6">

- 이때 로드 밸런서를 사용하게 되면 여러 개의 인스턴스를 하나의 로드 밸런서 엔드포인트로 외부에서 접근할 수 있게 된다.
- 그리고 SSL 보안 설정도 하나의 엔드포인트에 대해 적용해주면 되기 때문에 아무리 많은 확장이 되더라도 추가적인 작업이 필요하지 않다.

<img width="300" alt="스크린샷 2024-10-23 오후 7 16 35" src="https://github.com/user-attachments/assets/79891e77-05ba-4d3a-add0-227ab79056ed">

- 예시로 SSL 인증서를 통한 보안을 들었지만 그 외에도 하나의 엔드포인트의 관리로 다른 보안 강화에도 유리해질 수 있다.

---

> **장애 조치**

<img width="400" alt="스크린샷 2024-10-23 오후 7 17 38" src="https://github.com/user-attachments/assets/3d19b739-8920-423a-a3ed-0129b1730f66">

- Load Balancer는 타겟으로 등록되어 있는 인스턴스의 주기적으로 상태를 확인합니다.

<img width="400" alt="스크린샷 2024-10-23 오후 7 17 50" src="https://github.com/user-attachments/assets/72e27690-cdaa-4a8e-9c9f-83d878c84bdc">

- 이때 설정해 놓은 정상 응답이 아닐 경우 해당 인스턴스는 Load Balancing 대상에서 제외되고 정상적인 응답이 오는 인스턴스로만 트래픽을 보내서 장애 조치를 하게 됩니다.
- 이런 특징은 시스템의 고가용성을 유지할 수 있게 해줍니다.

<br/>

## ⚡️ ELB의 종류

### 🔋 Classic Load Balancer
- 2009년 출시
- HTTP, HTTPS, TCP등 여러 프로토콜 지원
- AWS에서 사용을 권장하지 않음

---

### 🔋 Network Load Balancer
- 2017년 출시
- TCP, UDP 트래픽을 처리하는 4계층 로드 밸런싱을 지원
- 높은 처리량이 필요한 네트워크 애플리케이션이나 게임 서버에 사용

---

### 🔋 Application Load Balancer
- 2016년 출시
- HTTP, HTTPS, WebSocket 프로토콜 지원
- 웹 애플리케이션 라우팅에 주로 사용

---

### 🔋 Gateway Load Balancer
- 2020년 출시
- TCP, UDP, HTTP, HTTPS등 여러 프로토콜 지원
- VPC 간의 라우팅, VPC연결 등에 사용
- 가상 appliance 를 제공하는 파트너사의 VPC 로 트래픽을 보내 보안을 강화하기 위해 주로 사용한다.

<br/>

<br/>

<br/>

## ⚡️ Application Load Balancer 생성

> 로드 밸런서에도 보안 그룹이 적용된다.

<img width="1000" alt="스크린샷 2024-10-23 오후 9 12 27" src="https://github.com/user-attachments/assets/d21b7804-c48f-4bb4-8429-712c53a6ef51">

- HTTP 요청을 모든 곳에 허용하도록 인바운드 규칙을 설정해준다.

--- 

> 이제 Application Load Balancer 를 생성해준다.

<img width="500" alt="스크린샷 2024-10-23 오후 9 32 38" src="https://github.com/user-attachments/assets/ecf8e38d-ac8b-41c3-a812-1c71e4d75c15">

`Scheme` 을 선택해줘야 한다.
- `Internet-facing`: 외부 인터넷에서 요청을 받을 수 있는 옵션
- `Internal`: Private IP 만 할당 받아 VPC 내부에서만 요청할 수 있는 옵션

지금은 외부에서 접근을 허용해줘야 하기 때문에 `Internet-facing` 을 선택해준다.

`듀얼 스택`은 IPv6 까지 사용할 때 선택해주면 된다.

---

<img width="500" alt="스크린샷 2024-10-23 오후 9 34 05" src="https://github.com/user-attachments/assets/cbe9149e-da59-4ffb-9813-1daea3567d84">

다음은 VPC를 선택하고 ALB의 노드를 배치할 가용 영역을 선택해야 한다.

`Internet-facing` 으로 선택한 상태에서 Private Subnet을 선택하면 인터넷 게이트웨이로 라우팅 되지 않아 인터넷이 트래픽을 수신하지 않는다고 경고가 나온다.(이렇게 설정은 가능하지만 의미는 없다.)     
**따라서 모두 `Public-Subnet` 으로 해준다.**

---

<img width="500" alt="스크린샷 2024-10-23 오후 9 40 02" src="https://github.com/user-attachments/assets/c8780775-daaa-43c1-976c-daa850bbd5d1">

- `Listener` 는 로드 밸런서가 요청을 받을 프로토콜이나 포트를 설정하고, 
- `Routing`은 받은 요청을 전달해주는 방법을 의미합니다.
- `Listener`를 HTTP 80 포트로 설정하면 로드 밸런서는 80 포트로 요청을 받을 수 있습니다.
- `Routing` 설정: **로드 밸런서는 라우팅할 대상**을 `Target Group`이라는 단위로 설정할 수 있습니다.

---

### 🔋 Create Target Group

<img width="500" alt="스크린샷 2024-10-23 오후 9 43 09" src="https://github.com/user-attachments/assets/0f69dd27-87e0-4e2a-b681-03601c77171c">

타겟 그룹의 `Type`은 4가지가 있다. 
- `Instances`: 타겟을 인스턴스로 지정할 수 있습니다.
- `IP Address`: 대상을 IP로 직접 지정할 수 있습니다.
- `Lambda function`: 강의에서는 아직 사용해보지는 않았지만 AWS의 Lambda라는 서비스를 대상으로 지정할 수 있습니다.
- `Application Load Balancer`: 네트워크 로드 밸런서에서만 사용할 수 있고 대상을 애플리케이션 로드 밸런서로 지정해주는 타입입니다.

---

<img width="500" alt="스크린샷 2024-10-23 오후 9 44 36" src="https://github.com/user-attachments/assets/6951f31e-5ae3-48ce-a55a-b8763e880a35">

`Health checks`는 타겟 그룹에 속한 인스턴스들이 정상적인 상태가 맞는지 확인하는 기능입니다.

이 설정 상태는 타겟이 된 인스턴스에 HTTP로 헬스체크 패스에 지정된 경로로 요청을 보냈을 때 200응답이 오면 정상 상태로 판단하겠다는 설정입니다.

---

<img width="500" alt="스크린샷 2024-10-23 오후 9 47 26" src="https://github.com/user-attachments/assets/9aaef526-334b-4710-bd10-f5c7743e90f7">

`Advanced health check settings` 
- 하나씩 설명드리면, 헬스체크 포트는 헬스체크 요청을 보낼 포트를 지정하고, 
- Healthy threshold는 헬스체크 요청을 입력된 숫자만큼 연속으로 정상 응답을 받아야 정상으로 지급하는 횟수입니다.
- Unhealthy threshold는 반대로 입력된 숫자만큼 연속으로 비정상 응답을 받으면 타겟을 비정상으로 지급하는 횟수입니다.
- 타임아웃은 요청의 타임아웃을 초로 입력할 수 있고, 인터벌은 HealthCheck 요청 간격을 설정할 수 있습니다.
- Success 코드는 정상 응답의 응답 코드를 지정할 수 있습니다.

HealthCheck 설정을 기본값으로 두면 검사시간이 오래 걸려 서버가 실행되기까지 오래 걸릴 수 있다.
- Healthy threshold, Unhealthy threshold를 2로 설정하고, interval을 15초로 설정해준다.

<img width="1000" alt="스크린샷 2024-10-23 오후 9 50 28" src="https://github.com/user-attachments/assets/6c49fddd-1e96-44b7-99ff-fb71bc504010">

- Target Instance 를 private-ec2-instance 를 선택해주고, Include as pending below 를 체크해준 다음 Target Group 을 생성해준다.

---

### 🔋 AWS Global Accelerator

- AWS 의 네트워크 서비스 중 하나이다.
- 요청한 사용자의 트래픽을 AWS의 최적화된 네트워크를 사용해서 성능이나 내구성을 높일 수 있다.
- 글로벌 서비스에 사용했을 때 많은 이점들이 있는 서비스이다.
- 추가적으로 DDoS 공격을 방어하는 기능도 있어 보안적으로도 좋다.

---

## ⚡️ Load Balancer Target Group 체크

<img width="1000" alt="스크린샷 2024-10-23 오후 9 58 17" src="https://github.com/user-attachments/assets/f5bf2c19-7ac5-4293-9591-48970d155db5">

- 대상 Instance 가 Unhealthy 상태로 되어 있다.
- Application Load Balancer 에서 보내는 health check 트래픽이 인스턴스의 보안 그룹에 허용되지 않았기 때문이다.

<img width="1000" alt="스크린샷 2024-10-23 오후 10 05 48" src="https://github.com/user-attachments/assets/8f09f18c-b442-4d6d-bf29-f756c5bbedd5">

- Private-ec2-instance-sg 의 Inbound-rules 를 수정해서 Load-Balancer 보안그룹을 HTTP 트래픽을 허용하도록 설정해주고 저장하겠습니다. 
