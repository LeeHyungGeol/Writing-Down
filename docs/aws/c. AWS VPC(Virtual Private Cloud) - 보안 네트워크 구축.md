# AWS VPC(Virtual Private Cloud) - 보안 네트워크 구축

<img width="500" alt="스크린샷 2024-10-18 오전 12 02 56" src="https://github.com/user-attachments/assets/656b5007-6d5e-480c-8746-3e46143cc092">

## ⚡️ 가상의 사설 네트워크 공간

- AWS의 다른 서비스들을 배치할 수 있다.
- VPC 는 생성할 때 `사용할 특정 IP 주소 범위를 지정`해야 하는데, VPC 에 배치되는 서비스들은 **내부나 외부와 통신**하기 위해 이 `IP 주소`를 부여받게 된다.
- VPC 에는 `외부 인터넷과 통신할 수 있는 공간`과 `외부로부터 단절된 공간`으로 나눠서 관리
- 이런 특징들과 여러가지 VPC의 구성요소들을 활용해서 **보안 강화**

## ⚡️ VPC 의 주요 구성요소
                                                                                                                                            
아래 리스트의 구성 요소들을 합쳐놓은 다이어그램이다.

<img width="500" alt="스크린샷 2024-10-17 오후 11 06 02" src="https://github.com/user-attachments/assets/8cc2e81f-4be4-44ca-805b-bc57e9a7f04f">

- 리전, 가용영역 - AWS의 데이터센터가 나눠진 단위
- VPC - 리전에 배치된 프라이빗 네트워크
- 서브넷 - VPC 내부의 IP 주소 범위, 외부 접근을 제한할 수 있음
- 인터넷 게이트웨이 - VPC와 인터넷 간의 통신을 가능하게 하는 게이트웨이
- NAT 게이트웨이 - 프라이빗 서브넷에서 외부로 통신을 가능하게 하는 게이트웨이
- 라우팅 테이블 - 트래픽이 어디로 라우팅되어야 하는지 정의하는 규칙의 집합
- 보안그룹, 네트워크ACL - 리소스에 대한 트래픽 필터링 규칙

이것보다 더 다양한 기능들이 있지만 여기서 사용할 것들만 알아보자.

### 🔋 Region

<img width="300" alt="스크린샷 2024-10-17 오후 11 14 07" src="https://github.com/user-attachments/assets/e4c34a38-4c03-4b40-9270-82275d53b337">

> **물리적으로 분리된 AWS `데이터 센터`의 집합**

- 버지니아, 도쿄, 시드니 등 세계 여러 지역에 위치
- **이렇게 분리해서 구성한 이유는 서비스하는 지역에 최대한 가까운 곳에서 서버를 배치할 수 있도록 하기 위해서이다.**
- 각 지역의 리전은 문자와 숫자로된 이름을 부여받는다.
- 서울은`ap-northeast-2` 

### 🔋 가용영역(Availability Zone) - AZ

리젼과 가용영역은 엄밀히 따지면 VPC 에 포함되는 영역은 아니다.

<img width="300" alt="스크린샷 2024-10-17 오후 11 17 33" src="https://github.com/user-attachments/assets/b6fbf999-a050-42a6-b100-c371d3e847ce">

>  하나의 리전을 물리적으로 나눠놓은 `데이터 센터`

- 전력 공급이나 네트워크 연결같이 물리적인 부분을 모두 독립적으로 사용
- 하나의 데이터 센터에서 서비스를 하게 되었을 때, AWS 가 관리를 문제가 없도록 하겠지만 혹시라도 문제가 발생했을 때, **같은 Region 내에 문제가 발생하지 않은 `다른 데이터 센터(Region)`에서 서비스를 제공하기 위해서이다.**
  - 이러한 특징을 `고가용성`이라고 한다.
- 서울리전에는 총 4개의 가용영역
- `ap-northeast-2a ~ ap-northeast-2d`
- 만약 `ap-northeast-2a` Region 에서 문제가 발생하면 `ap-northeast-2b`, `c`, `d` 에서는 정상적으로 서비스를 제공할 수 있다.
 
<img width="300" alt="스크린샷 2024-10-17 오후 11 18 15" src="https://github.com/user-attachments/assets/a74c5485-be36-46cd-a3a7-7d5dcb6a05d1">

- 가용영역 다이어그램

<br/>

<br/>

## ⚡️ 서브넷(Subnet)

> **VPC 내부에서 VPC 가 가지고 있는 IP 범위 를 더 작게 나눈 단위**

- **무조건 하나의 가용영역에 포함되어야 합니다.**
- public 서브넷, private 서브넷 두가지 종류가 존재
  - public: 외부에서 접근 가능
  - private: 외부에서 접근 불가능
- 보안과 가용성을 위해 이런 특징들을 활용

<img width="300" alt="스크린샷 2024-10-17 오후 11 29 22" src="https://github.com/user-attachments/assets/5d9c639e-c314-4c1f-a841-f9d3be4f4a77">

- 서브넷 다이어그램

## ⚡️ 인터넷 게이트웨이

> VPC 는 기본적으로 외부와 통신할 수 없다. **VPC 내에서 `외부 인터넷과 통신`할 수 있게 해주는 서비스**
 
- VPC 내부에 있는 리소스의 트래픽이 외부로 나갈 수 있고,
- public IP 를 부여한다면, 외부 인터넷에서 VPC 내부 리소스로 접근할 때 필요하다.
- 인터넷 게이트웨이는 VPC 단위로 배치한다.

<img width="300" alt="스크린샷 2024-10-17 오후 11 29 43" src="https://github.com/user-attachments/assets/eacd4af2-3334-4e8a-b3c3-aae2953c7a6d">

- 인터넷 게이트웨이 다이어그램
- public 서브넷에 있는 리소스가 외부 인터넷과 통신하기 위한 경로를 표시한 다이어그램
  - 다이어그램에 표시되어 있는 것 처럼 VPC 내부의 리소스들이 외부로 나가려면 인터넷 게이트웨이를 거쳐야 한다. 

## ⚡️ NAT Gateway

>  **VPC 내부의 리소스가 외부 인터넷으로 나갈 수 있게 해주는 서비스**

- 인터넷 게이트웨이와 다르게 **외부 인터넷에서 내부로 접근할 수는 없음**
- 주로 보안을 강화하기 위해 사용(외부에서 내부로 접근을 제한해야 할 때 사용)

<img width="500" alt="스크린샷 2024-10-17 오후 11 30 40" src="https://github.com/user-attachments/assets/bc6cb1d2-6d5e-4d07-b1c3-b657f439f90c">

- `NAT 게이트웨이 통신`
1. VPC 내부에 있는 서버에서 외부로 요청을 보낸다. (EX: OS 업데이트, 프로그램 다운로드). 이때 소스 IP 는 리소스의 private IP 가 되고, NAT Gateway 로 트래픽이 전달된다.
2. NAT Gateway 는 전달받은 트래픽의 소스 IP 를 NAT Gateway 의 public IP 로 변경하고, 다시 목적지로 전송한다.(이떄 NAT Gateway 도 Internet Gateway 를 통과해야 하는데 그림에선 생략했다.)
3. 전달받은 목적지에서는 소스 IP 가 NAT Gateway 의 IP 로 되어있어 전달하려는 대상 IP 의 NAT Gateway 의 IP 로 전달한다.
4. 전달받은 NAT Gateway 는 다시 대상 IP 를 처음에 보낸 리소스의 IP 로 수정하고 Server 로 전달하게 된다.
- 이러한 과정에서 리소스는 외부와 통신 과정에서 노출되지 않게 되어 보안을 강화할 수 있게 된다.

<img width="300" alt="스크린샷 2024-10-17 오후 11 31 11" src="https://github.com/user-attachments/assets/26a87075-4fc7-4749-a3d5-b54041516dc0">

- NAT Gateway 다이어그램
- **NAT Gateway 는 `Public 서브넷에 배치`되어야 한다.**

## ⚡️ 라우팅 테이블

> 서브넷의 트래픽을 어떻게 어디로 전달할지 정의하는 요소

- **`Routing Table` 은 `하나의 Subnet` 과 연결된다.**
- Internet Gateway 에 라우팅 되도록 정의가 된 Routing Table 과 연결된 Subnet 은 Public Subnet 이 된다. 
- **Public Subnet 의 Routing Table 은 Internet Gateway 에 라우팅 되도록 정의**
- **Private Subnet 의 Routing Table 은 NAT Gateway 에 라우팅 되도록 정의**

<img width="500" src="https://github.com/user-attachments/assets/fe6ed713-1e6d-4534-8646-b3c60ca4b6fa">

<img width="500" alt="스크린샷 2024-10-17 오후 11 59 56" src="https://github.com/user-attachments/assets/6e6ea8a8-8eae-45e3-aa44-c7ddc7be5520">

<br/>

<br/>

<br/>

<img width="1000" alt="스크린샷 2024-10-18 오후 3 39 58" src="https://github.com/user-attachments/assets/fdd478f8-52b3-4daf-8f11-6cb22fb457f0">

<img width="500" alt="스크린샷 2024-10-18 오전 12 15 50" src="https://github.com/user-attachments/assets/94cf7f30-6496-4dea-bc8b-1e0d426973bd">

### 🔋 IPv4 CIDR

> VPC 에서 사용할 IP 주소 범위: CIDR 블럭

**EX) `10.0.0.0/16`**

- `10.0.0.0`: 네트워크의 시작 IP 주소를 나타낸다. 
- `/16`: 전체 IP 주소 중 네트워크를 식별하는 부분의 길이를 나타낸다.
- 이 경우에는 IP 주소의 처음 16 bit 가 네트워크를 식별하는데 사용된다.
- 이렇게 설정하면 앞 16 bit 는 고정된 `10.0.0.0` 부터 시작해서 `10.0.255.255` 까지 대략 `65,536(256 x 256)` 개의 IP 주소를 VPC 에서 사용할 수 있게 된다.

### 🔋 Tenancy

> VPC 를 AWS 의 물리적인 하드웨어에 어떻게 배치할지를 지정하는 옵션

- `Dedicated`: 구성될 하드웨어를 다른 유저와 같은 하드웨에에 구성하지 않고 전용으로 사용하게 된다.
- `Default`: 특별한 보안 요구사항이 있지 않은 이상 `default` 로 설정하면 된다.

### 🔋 Subnet CIDR 블럭

- Subnet 의 CIDR 블럭은 선택한 VPC 의 CIDR 블럭 범위 내에 있어야 한다.
  - EX) VPC CIDR: `10.0.0.0/16` 이면 Subnet CIDR: `10.0` 까지는 고정으로 입력해줘야 한다.
- `10.0.1.0/24` 이렇게 설정하면 10.0.1.0 ~ 10.0.1.255 총 256개의 IP 주소를 사용할 수 있다.
  - 그런데 사실은 10.0.1.0 ~ 10.0.1.4, 마지막 IP 인 10.0.1.255 는 AWS 에서 IP 주소를 예약 사용하고 있어서
  - 이 5개를 뺀 251개의 IP 주소를 사용할 수 있다.

### 🔋 Internet Gateway

Internet Gateway 는 처음 생성되면 `Detached` 상태이다. VPC 에 연결해야 사용할 수 있다.

원하는 VPC 를 선택해서 Attach 하면 된다.

### 🔋 NAT Gateway

<img width="1000" alt="스크린샷 2024-10-18 오전 12 40 37" src="https://github.com/user-attachments/assets/8938be7f-c497-4e9c-bca2-84c4df432e7e">

<img width="500" alt="스크린샷 2024-10-18 오전 12 48 08" src="https://github.com/user-attachments/assets/31133938-f640-42ba-abd3-3485eed50a80">


- `NAT Gateway` 는 안정적인 운영을 하기 위해서 각 영역당 하나씩 배치해주는게 맞다.
- NAT Gateway 는 프리티어가 없고 비용이 많이 발생할 수 있다. 여기서는 하나만 생성한다.
- `Connectivity type` 은 외부 인터넷으로 트래픽이 나가게 하려면 `Public` 을 선택하면 된다.
- Elastic IP: 고정된 공용 IP 주소로 NAT Gateway 가 Internet 과 통신할 때 사용된다.
  - 즉, Elastic IP 를 사용해 Private Server 내의 Server 가 Internet 에 안전하게 연결될 수 있게 된다.

### 🔋 Routing Table

<img width="1000" alt="스크린샷 2024-10-18 오전 12 52 02" src="https://github.com/user-attachments/assets/eabd216b-c4b9-465d-8ca7-767eba5c4650">

- `Subnet Associations` 탭에서 연결된 Subnet 을 관리할 수 있다.

<img width="1000" alt="스크린샷 2024-10-18 오전 12 55 35" src="https://github.com/user-attachments/assets/d35ccc69-55cf-4a1a-bba9-d18500759e56">

- `Edit routes` 를 누르면 라우팅 테이블을 수정할 수 있다.
  - 인터넷 게이트웨이로 트래픽이 갈 수 있도록 route 를 추가하자.
  - Destination: `0.0.0.0`으로 설정해서 모든 트래픽을 지정하고 
  - Target: `Internet Gateway` 로 설정하면 된다.
  - 이 라우팅 테이블에 연결된 서브넷의 트래픽이 인터넷 게이트웨이로 갈 수 있다는 설정이 된다.
  - 즉, 이 라우팅 테이블과 연결된 서브넷은 Public Subnet 이 된다.

<br/>

<br/>

<br/>

## ⚡️ 보안그룹(Security Group)

> 인스턴스 단위의 가상 방화벽

- 인바운드와 아웃바운드 트래픽을 허용 설정
- `인바운드`는 외부에서 보안그룹 내부로 들어오는 트래픽,
- `아웃바운드`는 보안그룹 내부에서 외부로 나가는 트래픽
- 허용 대상을 IP나 다른 보안그룹을 기준으로도 가능

**보안그룹은 상태를 기억하는 Stateful 한 성질**

<img width="500" alt="스크린샷 2024-10-18 오후 7 53 38" src="https://github.com/user-attachments/assets/fc476fcf-9512-4007-9d39-6d3fea29fe78">

- **보안그룹 인바운드 규칙 예시**
  - 이렇게 보안 그룹은 IP 나 보안 그룹을 대상으로 트래픽을 허용할 수 있다.
  - CIDR 이나 CIDR 세트인 관리형 접두사 목록으로도 트래픽을 허용해줄 수 있다.

<img width="300" alt="스크린샷 2024-10-18 오후 7 54 19" src="https://github.com/user-attachments/assets/99c27d44-a640-4204-bf12-ff3c1c1b3a12">

- **보안그룹의 Stateful**
  - `인바운드에서 허용된 트래픽`은 허용된 상태가 유지되어서 `응답이 나갈 때도 허용되는 특징`이다.
  - **`응답으로 나가는 트래픽`과 `아웃바운드 트래픽`을 헷갈리면 안된다.**

## ⚡️ 네트워크 ACL(Network Access Control List)

> 서브넷 단위의 가상 방화벽

-  서브넷에 속한 인스턴스에게 적용
-  트래픽을 허용과 `거부`(보안그룹과 달리 거부를 지정할 수 있다.)
-  대상을 IPv4, IPv6, CIDR으로만 지정 가능

**네트워크 ACL은 Stateless(보안그룹과 반대로 상태를 유지하지 않는 Stateless)**

<img width="300" alt="스크린샷 2024-10-18 오후 8 08 49" src="https://github.com/user-attachments/assets/5809e187-cdf2-425e-a850-aa59f18fba3f">

- `네트워크 ACL의 Stateless`
  - 해당 트래픽의 응답이 나가야 하는데 이때 아웃바운드 규칙에 의해 해당 트래픽은 나가는게 거부가 되어서 요청을 보낸 곳에서 응답을 받지 못하게 된다.
  - 같은 설정의 보안그룹은 허용이 되어있어서 응답이 나갈 수 있게 된다.
  - 이것이 보안그룹과 네트워크 ACL 의 가장 큰 차이점이고 보안 설정을 할 때 유의해서 해야 한다.

<img width="300" alt="스크린샷 2024-10-18 오후 8 09 00" src="https://github.com/user-attachments/assets/53fe1478-8468-42e0-9db3-0515e99cc917">

- `보안그룹`과 `네트워크 ACL`
  - 그림의 맨 오른쪽에 보이는 것이 **네트워크 ACL** 이고 인스턴스 입장에서 `1차 방어벽`이라고 보면 된다.
  - 서브넷의 안쪽, 인스턴스를 감싸고 있는게 **보안그룹**으로 `2차 방어벽`이라고 보면 된다.

<br/>

<br/>

# 💡 개선이 필요한 부분

- **네트워크는 구축이 다 되었지만**, 아직 실제로 `애플리케이션이 실행될 서버가 없다.`
- `AWS EC2` 를 통해 서버를 직접 배포해보자
