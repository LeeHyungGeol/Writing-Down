# AWS EC2 - 전세계에 서비스 배포

## ⚡️ EC2(Elastic Compute Cloud)

> **`가상서버를 호스팅, 대여`할 수 있는 AWS 의 서비스**

- `대여한 가상머신`을 `EC2 인스턴스`
- 다른 AWS 서비스들과 통합해서 주로 사용

## ⚡️ EC2의 특징

- `확장성과 유연성` - AWS 에 로그인만 되어 있으면 언제, 어디서 든지 대여할 수 있고, 용량 확장이나 축소가 쉬움
- `빠른 배포` - 배포가 굉장히 빠르다. 직접 서버 환경을 구축하는 것 보다 훨씬 빠르게 배포가 가능하다. 대여하기만 하면 AWS 에서 빠르게 사용할 수 있도록 배포
- `안정성과 가용성` - 여러 가용영역과 리전에 인스턴스를 배포해 장애가 발생해도 문제없이 서비스를 제공할 수 있다.(가용성 확보)
- `유연한 요금 체계` - CPU, 메모리, 예약, 상시 사용 등의 요금제로 비용 최적화
- `서비스 통합` - 다른 AWS 서비스와 통합 쉬움(RDS, S3, ELB, Auto Scaling Group 등 수많은 서비스들을 권한 부여나 간단한 조직만으로 통합할 수도 있다.)

## ⚡️ AMI(Amazon Machine Image)
> **EC2 인스턴스를 생성하는데 필요한 OS나 소프트웨어가 포함된 템플릿**

- AWS 에서 미리 리눅스나 윈도우 같은 유명한 OS 로 구성한 템플릿으로 만들어둔 것도 있고,
- 개발자가 직접 만들어서 사용도 가능

## ⚡️ 인스턴스 유형

> **EC2 인스턴스의 CPU 나 Memory 를 다양한 요구사항에 따라 최적화된 종류를 의미한다.**

- `범용`, `컴퓨팅 최적화`, `메모리 최적화`, `가속화된 컴퓨팅`, `스토리지 최적화`, `고성능 컴퓨팅` 등이 있다.
- 여러가지 특징을 가진 인스턴스 유형으로 서비스의 특성에 맞게 사용해서 자원을 효율적으로 사용 가능

여기서는 범용 인스턴스를 사용해보겠다.

## ⚡️ 키 페어

> **EC2 인스턴스에 접속하기 위해 사용되는 공개키와 개인키의 조합**

- `공개키`는 EC2 인스턴스에 저장되고, 
- `개인키`는 사용자에 제공된다.
- 사용자는 제공받은 개인키를 통해 EC2 인스턴스에 접속
- 이러한 기능을 통해 **EC2의 보안이 강화된 접근이 가능하다.**

## ⚡️ EC2의 스토리지

EC2 스토리지는 일반적으로 2가지가 사용된다.

- `EBS(Elastic Block Storage)` - 한번에 하나의 인스턴스에만 연결하는 스토리지
  - 백업이 쉽게 가능하고, 
  - 용량을 중단 없이 증가할 수 있는 장점이 있다.
- `EFS(Elastic File System)` - 여러 인스턴스가 동시에 접근할 수 있는 관리형 파일 시스템
  - 주로 파일을 관리하기 위해 사용된다. 
  - 여러 인스턴스가 동시에 파일에 접근할 수 있다.
  - 여러 가용영역에 복제되어 관리돼 내구성과 가용성이 좋고 자동으로 용량이 확장되거나 줄어들어 용량에 대한 관리가 필요없다.

여기서는 `EBS` 를 사용해보겠다.

<img width="300" alt="스크린샷 2024-10-18 오후 10 10 01" src="https://github.com/user-attachments/assets/ce20cc43-c2b9-4aa4-9593-e98f357a1085">

- `EC2 다이어그램`
  - VPC 의 `Public Subnet` 에 EC2 를 배포한다.
  - `SSH 22번 Port` 와 `HTTP 80번 Port `를 `열려있는 보안 그룹에 연결`한다.
  - 외부에서는 `Internet Gateway` 를 통해 통신한다.

<br/>

<br/>

<br/>

### 🔋 AMI

<img width="1000" alt="스크린샷 2024-10-18 오후 10 34 12" src="https://github.com/user-attachments/assets/7f61a969-1a06-47c2-bade-826c1862da0c">

이미지의 `AMI` 들은 AWS 에서 미리 만들어놓은 템플릿 들이다.
- 그중에서 `Amazon Linux` 는 AWS 에서 직접 만든 Linux 이다.
- 여기서는 Amazon Linux 를 사용한다.

`아키텍쳐`는 `ARM` 을 사용한다. **상대적으로 비용이 저렴하고 성능도 조금 더 좋기 때문이다.**

---

### 🔋 인스턴스 유형

<img width="1000" alt="스크린샷 2024-10-18 오후 10 38 28" src="https://github.com/user-attachments/assets/e6984182-bb4e-48ff-a019-c5e6c6253e23">

여기서는 `범용`인 `t4g.small` 을 선택한다.
- `t` 는 범용 인스턴스를 의미한다.
- `4` 는 T 유형의 세대수를 의미한다.
- `t4g` 유형은 `ARM 아키텍쳐`를 지원하는 유형이고, 무엇보다 프리티어로 사용할 수 있다.

---

### 🔋 키 페어

<img width="500" alt="스크린샷 2024-10-18 오후 10 43 00" src="https://github.com/user-attachments/assets/b5abf9ac-a091-45ef-b849-e5557f221264">

- `Key Pair Type` 은 `암호화 방식`을 선택하는 옵션이다. 여기서는 `RSA` 를 선택한다.
- `Private Key File Format` 은 윈도우 7,8 과 같은 옛날 버젼일 경우 `PPK` 를 사용하고, 
  - 원도우 10 이상, Linux, Mac OS 는 `PEM` 을 사용한다. 

---

### 🔋 네트워크 설정

<img width="1000" alt="스크린샷 2024-10-19 오전 3 08 04" src="https://github.com/user-attachments/assets/0eb86b12-dd76-4815-9ba8-354670c3f1db">

- 서브넷을 Public Subnet 중 아무거나 하나 선택한다.
- 외부에서 접근할 수 있도록 Public IP 를 할당하기 위해 Auto-assign Public IP 를 `Enable` 로 설정한다.

---

### 🔋 보안그룹 설정

<img width="1000" alt="스크린샷 2024-10-19 오전 3 10 38" src="https://github.com/user-attachments/assets/7061047f-4317-42b1-a19f-ab5d99968f59">

- `Security group name(보안 그룹 이름)`, `Description(보안 그룹 설명)`을 작성한다.
- 인스턴스에 접속하기 위해 기본으로 설정되어 있는 `SSH(22 port)` 와 서버에 `http 요청`을 하기 위해 `80 port` 를 열어준다.
  - `Source Type` 을 `Anywhere` 로 설정하면 어디서든 접속이 가능하다.

---

### 🔋 스토리지 설정

- 기본정으로 `EBS`에 관한 설정이다.
- `EFS` 를 사용하려면 아래 `File System Edit` 을 클릭해서 설정해주면 된다.

---

### 🔋AWS CloudShell SSH 접속 명령어

<img width="1000" alt="스크린샷 2024-10-19 오전 3 35 59" src="https://github.com/user-attachments/assets/84811cc0-59b5-4299-8457-a92f18d8cd94">

`ssh 사용자명@ec2_public_ip -i pem파일 위치`

- ex) os가 Amazon Linux이고 퍼블릭ip가 43.202.68.226인 경우
  - `ssh ec2-user@43.202.68.226 -i /home/prac/aws/burger-ec2.pem`
  - `ssh ec2-user@3.38.185.156 -i goopang-key-pair.pem`

### 🔋 AWS CloudShell 에서 PEM키 파일 권한 변경

`chmod 400 pem파일 위치`

- ex) `chmod 400 /Users/prac/aws/burger-ec2.pem`
- `chmod 400 goopang-key-pair.pem`

---

<img width="1000" alt="스크린샷 2024-10-19 오전 3 40 21" src="https://github.com/user-attachments/assets/54c93639-75bb-4571-8595-adbfeb71d039">

<img width="1000" alt="스크린샷 2024-10-19 오전 3 37 33" src="https://github.com/user-attachments/assets/7b520dc3-0b7f-4858-857e-930c47f297b2">

- `EC2 Instance Connect` 라는 서비스에서 임시로 사용할 `PEM키` 를 만들어서 연결이 가능하게 해준다.
- 주의할 부분은 `Private Subnet 에 배치된 EC2` 는 다른 방법으로 접속해야 한다.

<br/>

<br/>

<br/>

## ⚡️ 애플리케이션 빌드 및 배포

> `EC2 Instance Connect` 라는 서비스로 인스턴스에 접속하여 빌드 및 배포해준다.

### 🔋 Git 설치 명령어

- `sudo yum update`
- `sudo yum install git`

### 🔋 JDK 설치 명령어

- `sudo yum install -y java-17-amazon-corretto-devel`

### 🔋 Git Clone 명령어

<img width="1000" alt="스크린샷 2024-10-19 오전 3 50 03" src="https://github.com/user-attachments/assets/f84c0923-718b-4b46-a30e-ee2e2295a891">

`git clone -b 2_monolithic_cloud https://github.com/burger-2023/aws-operation-prac.git`
- 생성된 project 로 이동: `cd aws-operation-prac`

### 🔋 빌드 및 배포

- `./gradlew build`
- `sudo java -jar build/libs/aws-msa-monolithic-prac-0.1.jar`

<img width="1000" alt="스크린샷 2024-10-19 오전 3 51 35" src="https://github.com/user-attachments/assets/47924d5d-e071-4365-88dc-8fab368c19e8">

- public ip/health_check: `3.38.185.156/health_check`

<br/>

## ⚡️ EC2 배포 과정

<img width="500" alt="스크린샷 2024-10-19 오전 3 56 05" src="https://github.com/user-attachments/assets/77a5f67c-f4b1-4c2d-ae5a-68fa2a00aa9c">

- 퍼블릭 서브넷에 EC2 배포
- 보안그룹을 HTTP와 SSH를 열어둔 상태로 적용
- PEM키를 사용해 SSH접속
- 더 쉬운 접속 방법인 EC2 인스턴스 커넥트 사용
- HTTP 요청 테스트

<br/>

## ⚡️ 개선할 부분

- 서버가 지금은 Public Subnet 에 배포가 되어 있다.
  - 문제가 되는 이유는 Public Subnet 은 누구나 쉽게 접근할 수 있는 것이다.
  - DDoS 나 트래픽 변조 같은 공격을 서버가 직접적으로 당할 수도 있다.

1. EC2 인스턴스를 `Private Subnet` 으로 이전 
2. `Public Subnet`에 `Bastion Host` 배치해서 

EC2 인스턴스로 안전하게 접속할 수 있는 구조로 개선
