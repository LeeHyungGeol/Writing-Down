# 💡 Auto Scaling Group - 서비스 자동 확장

## ⚡️ Auto Scaling 개념

> 시스템의 수평 확장을 자동으로 가능하게 해준다.

- 클라우드 서비스에서 로드밸런싱과 함께 사용되는 중요한 기술 
- 확장하는 시점은 시스템의 `CPU 사용률`, `메모리 사용률 `같은 지표의 임계점으로 확장 설정

## ⚡️ Auto Scaling Group

> Auto Scaling을 위해 AWS에서 제공하는 서비스

- Application Load Balancer와 쉬운 통합 제공
- 관리되는 인스턴스가 정상 동작을 하지 않는 인스턴스면 종료하고, 새로운 인스턴스를 시작
- AWS 에서 제공하는 시스템의 여러가지 모니터링 지표로 조정 정책 설정

## ⚡️ 조정 정책

> `조정 정책`: 인스턴스의 개수 scaling 하는 정책

조정 정책의 종류 3가지
1. **동적 크기 조정 정책** 
2. **예약된 조정 정책** 
3. **예측 크기 조정 정책**

- 조정 정책에 의해 조정되는 인스턴스의 개수는 사용자가 미리 지정한 최소, 최대 인스턴스 개수를 넘을 수 없다.

### 🔋 동적 크기 조정 정책

- `대상 추적 크기 조정`: **`Target Group` 의 지표를 원하는 수치로 유지하도록 인스턴스의 수를 조정**
  - EX) 대상 그룹의 평균 CPU 사용률을 50%로 유지하도록 인스턴스의 수를 늘리거나 줄이도록 한다.

- `단순 크기 조정` - **`Target Group` 의 지표가 임계치를 넘으면 확장, 내려가면 축소**
  - EX) 대상 그룹의 평균 CPU 사용률이 60%가 넘으면 추가, 40%보다 낮아지면 제거

- `단계 크기 조정` - **지정한 값들을 넘거나 떨어짐에 따라 1개, 2개, 그 이상의 인스턴스를 늘리는 정책**
  - EX) 대상 그룹의 평균 CPU 사용률이 40%가 넘으면 1개 추가, 60%가 넘으면 2개 추가

---

### 🔋 예약된 조정 정책

> **특정 시간에 한 번 조정하거나 반복적으로 매일 또는 매주 같은 특정 주기로 조정 설정이 가능한 정책**

- EX) 매일 오전 9시부터 트래픽이 증가하는 서비스가 있다면 예약된 조정 정책을 사용해서 매일 8시 40분에 인스턴스를 추가하도록 예약 설정할 수 있다.
- EX) 반복적이지 않은 특정 이벤트의 트래픽이 증가가 예상된다면 이벤트 시작 전에 인스턴스를 추가하고 이벤트가 끝난 후에 제거하는 예약 설정을 할 수도 있다.

---

### 🔋 예측 크기 조정 정책

> **예측 크기 조정 정책은 머신러닝 모델을 통해 학습된 트래픽 패턴으로 예측된 트래픽에 맞게 스케일링 되는 조정 정책**

- 가장 최근에 나온 조정 정책


## ⚡️ AMI - Amazon Machine Image

> `AMI`: **인스턴스를 시작하는데 필요한 여러가지 구성요소들을 모아놓은 템플릿입**

- 인스턴스 생성에 필요한 OS나 소프트웨어가 포함된 템플릿
- 인스턴스를 생성할 때 무조건 필요하나, 동일한 구성의 인스턴스를 생성하기 위해서도 유용하게 사용한다.
- 이전의 실습들에서 AWS 에서 미리 만들어 놓은 AMI 를 사용해봤다.

---

<img width="500" alt="스크린샷 2024-10-31 오후 11 54 42" src="https://github.com/user-attachments/assets/50d81888-9e45-431e-adb7-60e0c956c927">

- Git, Java 가 설치된 상태의 사용자 정의 AMI 를 만들어서 Instance 에 소프트웨어가 설치된 상태로 생성되게 할 수 있다.
- 사용자 정의 AMI 를 Auto Scaling Group 과 함께 사용시 Scaling 될 때 모두 동일한 환경으로 배포 및 더 빠른 배포가 가능하다.

**참고로 EC2 는 부팅이 되는 순간 부터 비용이 발생한다. 이렇게 준비된 AMI 를 사용하면 설치 시간이 줄어서 소소하게 비용도 줄일 수 있다.**

## ⚡️ 시작 템플릿 - launch template

> **AMI 와 인스턴스 유형, 인스턴스 역할 같은 생성 정보를 정의하는 템플릿**

- `Auto Scaling Group` 은 시작 템플릿을 기반으로 인스턴스를 생성

<br/>

<br/>

<br/>

## ⚡️ Custom AMI 만들기

> Custom AMI는 포함시키려는 소프트웨어가 설치된 EC2 인스턴스를 통해서 만들 수 있다.

우선, Public Subnet 에 배치되고 goopang-public-sg 보안 그룹에 연결된 EC2 인스턴스(name: goopang-ami-instance)를 하나 생성한다.

그리고 해당 인스턴스에 접속해서 소프트웨어들을 설치해준다.

<img width="500" alt="스크린샷 2024-11-01 오전 12 24 17" src="https://github.com/user-attachments/assets/214132fd-a65f-4ed6-8c71-024480cc6469">

- 아래의 명령어들로 Git, Java 소프트웨어들을 수동으로 EC2 에 설치해준다.

```shell
#!/bin/bash

#패키지 업데이트
sudo yum update -y

#Java, Git 설치
sudo yum install -y java-17-amazon-corretto-devel
sudo yum install -y git
```

---

### 🔋 Create image

EC2 콘솔로 돌아가서 EC2 인스턴스를 선택하고 `Action > Image and templates > Create image`

<img width="800" alt="스크린샷 2024-11-01 오전 12 30 56" src="https://github.com/user-attachments/assets/2a4d69d7-b32e-4ec4-97c6-d933794ece69">

- 용량을 설정해주고, Delete on Termination 설정은 EC2 Instance가 종료될 때 해당 스토리지도 같이 삭제되는 옵션입니다.
- 활성화해주고 생성해주겠습니다.

---

### 🔋 Create launch template

<img width="500" alt="스크린샷 2024-11-01 오전 12 45 17" src="https://github.com/user-attachments/assets/57cf4d96-3671-41d3-8068-8d2c1f371ca5">

- `Auto Scaling guidance`: Auto Scaling 에 사용하기 위한 체크

<img width="500" alt="스크린샷 2024-11-01 오전 12 44 52" src="https://github.com/user-attachments/assets/aef86ef1-9fe7-4f92-be69-df2c9f1e11c5">

- `My AMIs > Owned By Me` 에서 Custom Ami 를 선택할 수 있다.

<img width="500" alt="스크린샷 2024-11-01 오전 12 47 58" src="https://github.com/user-attachments/assets/57f69faf-2ba0-44ad-b516-f78bdfbfbc33">

- Instance type: t4g.small 로 할 경우 안될 수도 있다. medium 혹은 그 이상을 선택하자.

<img width="500" alt="스크린샷 2024-11-01 오전 12 50 31" src="https://github.com/user-attachments/assets/92d641bb-b6cd-4b27-95fa-0ab6a1992a69">

- Subnet 은 Auto Scaling Group 을 만들 때도 선택할 수 있어서 시작 템플릿에는 포함하지 않는다.
- 보안 그룹은 goopang-private-ec2-sg 로 선택한다.

<img width="500" alt="스크린샷 2024-11-01 오전 12 52 29" src="https://github.com/user-attachments/assets/f8e6192c-2f7d-427f-b019-dfc403b084f7">

- EC2 가 생성되면 자동으로 서버가 실행될 수 있도록 User Data 에 스크립트를 작성한다.

Create launch template 으로 시작 템플릿을 생성한다.

---

### 🔋 Create Auto Scaling group

<img width="500" alt="스크린샷 2024-11-01 오전 12 54 27" src="https://github.com/user-attachments/assets/a91bf805-69ae-4ea0-9a96-0515fff31425">

- 이름을 입력하고, 생성했던 launch template 을 선택한다.

<img width="500" alt="스크린샷 2024-11-01 오전 12 56 30" src="https://github.com/user-attachments/assets/6ae7d0fe-0fe9-4755-af17-9b52d757384d">

- VPC를 선택하고 두 개의 가용 영역 Private Subnet을 지정합니다.
  - 여기서 선택하는 서브넷은 Auto Scaling으로 생성되는 인스턴스들을 배치할 서브넷을 지정합니다.

<img width="500" alt="스크린샷 2024-11-01 오전 1 01 39" src="https://github.com/user-attachments/assets/ba21eb31-7d49-4319-85bf-f2bcd410ef9f">

다음은 Auto Scaling Group과 통합할 Load Balancer를 선택해야 한다.

- Attached to an Existing Load Balancer를 체크해서 이전에 만들어두었던 Load Balancer를 선택해준다. 
- 그리고, 기존에 생성했던 타겟 그룹을 선택해주겠습니다.
  - 여기서 선택한 타겟 그룹은 Auto Scaling Group으로 생성되는 인스턴스들을 자동으로 타겟 그룹에 등록하게 됩니다.

<img width="500" alt="스크린샷 2024-11-01 오전 1 01 55" src="https://github.com/user-attachments/assets/55182083-e278-44a6-9093-bbb754a33e1c">

- `Turn on Elastic Load Balancing health checks`: 이 설정은 Load Balancer의 Health Check 결과에 따라 Auto Scaling Group이 인스턴스를 종료시키거나 유지시키도록 할 수 있습니다.
- `Health check grace period`: 입력한 시간만큼 오토 스케일링 그룹이 인스턴스의 상태가 언헬시라도 종료시키지 않고 유예하도록 하는 설정입니다. 이 시간이 지난 후 헬스체크 상태를 판단하게 됩니다.

<img width="500" alt="스크린샷 2024-11-01 오전 1 07 24" src="https://github.com/user-attachments/assets/61230324-9b88-4119-981c-65829c3b2a5b">

- 그룹 사이즈의 Desired Capacity는 Auto Scaling 그룹에서 스케일링이 발생하지 않았을 때 유지되는 인스턴스의 개수를 지정할 수 있습니다.
- 아래의 스케일링에는 조정될 수 있는 인스턴스의 최소 최대 개수를 설정할 수 있습니다.
- Desired Capacity를 1, Mean Desired Capacity도 1, Max Desired Capacity는 3으로 설정해주겠습니다.
- Automatic Scaling은 조정 정책을 설정할 수 있습니다.

<img width="500" alt="스크린샷 2024-11-01 오전 1 07 32" src="https://github.com/user-attachments/assets/d451eda3-fded-4349-adfd-056c1391b8ae">

`Instance maintenance policy`: 인스턴스 교체가 발생할 때 가용성을 어떻게 관리할지에 대한 옵션입

- 예를 들어, `Launch before terminating` 옵션은 가용성을 우선적으로 유지보수하는 옵션으로 인스턴스 재시작시 기존 인스턴스를 종료시키기 전에 새로운 인스턴스가 준비될 때까지 대기 후 종료시킵니다.
  - 이 경우 모든 인스턴스를 재시작할 때 동시에 Desired Capacity 의 2배까지 인스턴스가 실행될 수 있습니다.
  - 이럴 경우 가용성에는 좋겠지만 비용이 많이 나가게 됩니다.
- 반대로 `Terminate and launch` 옵션은 비용을 절감할 수 있는 옵션으로 모든 인스턴스를 재시작할 때 기존 인스턴스를 종료시키면서 동시에 새로운 인스턴스를 시작시킵니다.
  - 여기서는 `Terminate and launch` 옵션을 선택한다.

각 옵션마다 장단점이 있어 운영 환경에서는 잘 살펴보고 선택해야 합니다.

<img width="500" alt="스크린샷 2024-11-01 오전 1 08 40" src="https://github.com/user-attachments/assets/1372b7ec-ac9d-4912-ac5b-658a88a3586c">

- Notifications는 스케일링이 될 때 AWS SNS라는 서비스를 통해서 알림을 받을 수 있습니다.

<img width="500" alt="스크린샷 2024-11-01 오전 1 12 01" src="https://github.com/user-attachments/assets/db731c2c-a93c-48d1-a64b-435046f1b3d0">

- Key 는 Name, Value 는 생성될 인스턴스에 보여질 이름을 입력해주시면 됩니다.
- 이렇게 입력해주는 이유는 오른쪽 태그 New Instances 설정을 통해 이 태그가 새로 생성되는 인스턴스에도 적용됩니다.
- 그래서 이 Auto Scaling Group으로 생성된 인스턴스의 이름이 지정되어 생성됩니다. 여러 인스턴스가 생성될 때 구분하기 좋습니다.

`Create Auto Scaling Group` 으로 `Auto Scaling Group`을 생성합니다.

---

<img width="1000" alt="스크린샷 2024-11-01 오후 2 16 11" src="https://github.com/user-attachments/assets/0167bccc-f32e-47ac-991d-802bed5517af">

<img width="1000" alt="스크린샷 2024-11-01 오후 2 17 00" src="https://github.com/user-attachments/assets/cde42e55-1da0-40fc-ac33-68ea139627fc">

<img width="500" alt="스크린샷 2024-11-01 오후 2 18 26" src="https://github.com/user-attachments/assets/2c5ba8f8-0cd7-45ec-945f-8a1c415fbe97">

<img width="500" alt="스크린샷 2024-11-01 오후 2 19 04" src="https://github.com/user-attachments/assets/e002f2d2-9c5f-4f05-b526-e400e3a05803">

- `alb` 의 DNS 로 `/health_check` 요청을 보내면 load balancing 에 의해 ipAddress 가 바뀌는 것을 볼 수 있다.

--- 

### 🔋 용량을 줄이는 수동 Scale-in 테스트

Target Group 에서 인스턴스를 등록 해제시킬 때 300초간 대기 후에 해제하는 설정 때문에 현재 설정 상태에서는 용량을 줄여도 오랜 시간이 지나야 인스턴스가 종료된다.

<img width="1000" alt="스크린샷 2024-11-01 오후 2 26 34" src="https://github.com/user-attachments/assets/88a02ad2-2b85-49c2-8408-c814625129b6">

<img width="500" alt="스크린샷 2024-11-01 오후 2 27 20" src="https://github.com/user-attachments/assets/33b68467-19e0-4552-9862-506f163ae069">

- `Target Group > Attributes > Target deregistration management > Deregistation delay` 에서 설정할 수 있다.
- `Deregistation delay` 를 5초로 변경한다.

이후에 `Auto Scaling Group > Desired Capacity` 를 `2 -> 1` 로 수정하면 인스턴스 하나가 자동 종료된다. 

<br/>

<br/>

<br/>

## ⚡️ Auto Scaling Group 새로고침

> 시작 템플릿을 새로운 버전으로 만들고 인스턴슫들을 최신 버전으로 리프레시 하는 기능

<img width="1000" alt="스크린샷 2024-11-01 오후 2 34 18" src="https://github.com/user-attachments/assets/0be76307-7c16-4d81-b0ba-6b3ab5ca45ae">

<img width="500" alt="스크린샷 2024-11-01 오후 2 35 06" src="https://github.com/user-attachments/assets/1780c23c-19b5-47a9-9ed1-1dc901d7d019">

- version 을 구분할 버젼명을 입력한다.
- `Auto Scaling guidance` 를 체크해준다.
- 아래 내용으로 `User Data` 수정

```shell
#!/bin/bash

#Git 레포지토리 클론 및 브랜치로 이동 
git clone -b 2_monolithic_cloud_v2 https://github.com/burger-2023/aws-operation-prac.git

#폴더 이동 
cd aws-operation-prac

#Gradle을 이용한 Spring Boot 프로젝트 빌드 후 빌드된 Spring Boot 애플리케이션 실행 
./gradlew build 
sudo java -jar build/libs/aws-msa-monolithic-prac-0.1.jar
```

--- 

### 🔋 Auto Scaling Group - Start Instance Refresh

<img width="1000" alt="스크린샷 2024-11-01 오후 2 41 00" src="https://github.com/user-attachments/assets/8e3c0915-73cd-4271-90b5-a3647ad772a4">

인스턴스의 refresh 되는 과정이 표시되고, 아래쪽에는 refresh 한 이력을 볼 수 있다.

<img width="500" alt="스크린샷 2024-11-01 오후 2 47 51" src="https://github.com/user-attachments/assets/6ecfac83-30e3-44e2-a324-df69ac349ec0">

- 먼저 인스턴스 리플레이스먼트 메소드를 보겠습니다.
- 오버라이드를 누르면, Auto Scaling Group을 생성할 때 설정했던 인스턴스 메인터넌스 정책을 복사해서 사용할 수 있습니다.
- 현재 설정을 설명드리면, 리프레시가 진행되는 동안 현재 인스턴스 개수를 기준으로 최소 90%에서 최대 100%까지 유지되어야 한다는 설정입니다.
  - 예를 들어, 최소가 50, 최대가 100%로 되어 있고, 현재처럼 2개의 인스턴스를 업데이트 한다면 진행하는 과정에서 최소 1개의 인스턴스는 살아있어야 하고 최대 2개까지 생성될 수 있는 설정이 됩니다.
  - 이 설정으로 업데이트를 진행하면 1개의 인스턴스가 먼저 업데이트 됩니다.
  - 그러면 1개의 이전 버전의 인스턴스와 1개의 새로운 버전의 인스턴스가 동시에 실행되는 상태가 되고 다음으로 남아있는 이전 버전의 인스턴스가 삭제되고 새로운 인스턴스를 하나 더 실행시키면서 업데이트 과정이 마무리가 됩니다.
- Launch Before Terminating 옵션은 최소가 100%로 고정되어 있고 최대를 설정할 수 있습니다.
  - 이 옵션은 최대 200%를 입력했다고 가정해보겠습니다.
  - 그러면 현재 2개의 인스턴스가 실행되고 있고 업데이트를 진행하는 동안 최소 2개, 최대 4개까지 생성될 수 있습니다.
  - 이 설정대로 업데이트가 진행되면 새로운 인스턴스를 바로 2개 실행시키고 인스턴스가 모두 실행되면 이전 버전의 인스턴스를 모두 삭제하게 되면서 리프레시가 마무리가 됩니다.
- 마지막 Custom Behavior 옵션은 최소와 최대를 사용자가 직접 입력할 수 있습니다. 
- 우리는 Terminate and Launch 옵션을 선택해서 최소를 50%로 설정해주겠습니다.

- Instance Warming Up은 인스턴스가 소프트웨어가 실행되거나 준비되는 시간을 감안하여 설정하는 시간으로 이 기간에 해당되는 인스턴스가 있으면 조정 정책에 의해 스케일링이 동작하지 않게 됩니다.

<img width="500" alt="스크린샷 2024-11-01 오후 3 02 58" src="https://github.com/user-attachments/assets/c3acfc14-c5b9-4f35-a681-b44422e5efad">

- Check Point는 지정한 개수만큼 인스턴스가 리프레시되면 지정 시간 동안 리프레시를 대기하게 됩니다.
  - 활성화하지 않으면 대기 없이 계속 진행됩니다.
  - 50%와 100%를 입력하게 되면 50% 업데이트가 되었을 때 대기하고 대기가 끝나면 다시 100%가 완료될 때까지 진행하는 설정이 됩니다.
- Skip matching 은 업데이트하려는 새로운 버전과 같은 버전의 인스턴스가 있을 때 해당 인스턴스를 리프레시 대상에서 제외할지 포함할지를 선택할 수 있습니다. 체크해서 제외하도록 설정하겠습니다.
- Standby Instances는 대기 상태의 인스턴스의 리프레시를 어떻게 동작할지 설정할 수 있고, 
- Scale-in protected instances 는 스케일인에 대해 보호되는 인스턴스의 리프레시를 어떻게 동작할지 설정할 수 있습니다.
- 클라우드 워치 알람은 지정한 인계 값에 알람을 설정할 수 있고, 추가적인 설정으로 자동으로 이전 버전으로 롤백하도록 설정할 수 있습니다.


<img width="500" alt="스크린샷 2024-11-01 오후 3 05 58" src="https://github.com/user-attachments/assets/ee7799be-3018-422c-82ca-c2f7ec27f87e">

- Desired Configuration에는 어떤 업데이트를 할지 지정해야 합니다. 
  - 업데이트 런치 템플릿을 선택하고, 업데이트하려는 시작 템플릿의 버전2를 지정해주겠습니다.
- `Choose a set of instance types and purchase options to override the instance type in the launch template` 은 업데이트가 진행되는 동안 생성되는 인스턴스의 인스턴스 유형을 지정할 수 있습니다.
- 롤백 세팅은 위에서 봤던 클라우드와치 알람 설정과 같이 사용했을 때 경보 상태가 되면 자동으로 롤백하도록 설정할 수 있습니다.
- 이 설정도 여기선 사용하지 않는다.

<img width="1000" alt="스크린샷 2024-11-01 오후 3 08 33" src="https://github.com/user-attachments/assets/c1b413e8-8ea4-4905-8b50-1fe4ee8306f6">

- 새로 생성된 인스턴스의 `Details > Tags` 을 보면 version 이 2 인 것으로 구분할 수 있다.

<img width="1000" alt="스크린샷 2024-11-01 오후 3 11 14" src="https://github.com/user-attachments/assets/135e96d9-5f20-496e-8571-e7938299eaf6">

- `Auto Scaling Group > Instance refresh` 에서 인스턴스들을 refresh 상황을 확인할 수 있다.

<img width="1000" alt="스크린샷 2024-11-01 오후 3 59 35" src="https://github.com/user-attachments/assets/62c48d96-490a-4057-9e93-9906bb527854">

- 하나의 인스턴스가 뜨는데 시간이 오래 걸려서 load balancer 의 health_check 설정 중 Unhealthy threshold 를 5 로, interval 을 60초로 변경 후에
- Instance refresh 에 겨우 성공한 모습이다.

<img width="500" alt="스크린샷 2024-11-01 오후 4 03 28" src="https://github.com/user-attachments/assets/d45fed49-044a-40de-98a6-19e1734c932d">

- alb DNS 를 이용해 `/health_check` 요청을 보내보면 branch 명이 v2 로 바뀐 것을 확인할 수 있다.

<br/>

<br/>

<br/>

## ⚡️ Auto Scaling Group 조정 정책 설정

### 🔋 Scale-out-policy

<img width="1000" alt="스크린샷 2024-11-01 오후 4 05 59" src="https://github.com/user-attachments/assets/2d58a5c5-22ad-4461-97ae-c7ab3547ea56">

`Automatic scaling > Create dynamic scaling policy`

- Auto Scaling Group은 스케일링 되는 시점을 이 CloudWatch 알람이라는 서비스를 통해 알 수 있습니다.
  - 클라우드워치 알람은 특정 지표의 특정 인계값을 정해 인계점을 초과하거나 미달일 때 알람을 발생시킬 수 있는 서비스입니다.
  - `Create a CloudWatch alarm` 을 클릭하면 클라우드워치 알람을 생성할 수 있습니다.
- `Take the action`: 알람이 발생했을 때 인스턴스를 늘리거나 줄이는 설정을 할 수 있습니다.
- `And then wait`: 인스턴스 스케일링이 되고 다음 스케일링을 시작할 때까지 대기하는 시간을 설정할 수 있습니다.

<img width="500" alt="스크린샷 2024-11-01 오후 4 29 50" src="https://github.com/user-attachments/assets/ff67a021-8649-402e-ab2c-3db7e7f3c124">

### 🔋 Scale-in-policy

<img width="500" alt="스크린샷 2024-11-01 오후 4 32 10" src="https://github.com/user-attachments/assets/27c2f708-a84f-44b0-8a55-cc993276de83">

<img width="500" alt="스크린샷 2024-11-01 오후 4 35 40" src="https://github.com/user-attachments/assets/b4b5e9b4-fb61-4be6-ac69-8b6e61faa19a">

- Scale-in-policy 관련 cloudwatch alarm 설정이다.

---

### 🔋 Create a CloudWatch alarm

먼저 기준으로 삼을 지표를 선택해야 한다.

<img width="1000" alt="스크린샷 2024-11-01 오후 4 11 02" src="https://github.com/user-attachments/assets/d0f3387a-859c-4867-b4b6-3e568117ad31">

<img width="1000" alt="스크린샷 2024-11-01 오후 4 13 12" src="https://github.com/user-attachments/assets/213e893f-e87e-46a6-b32f-86e89f922270">

`All > EC2 > By Auto Scaling Group`

- `Perl Instance Matrix`는 개별 인스턴스에 대한 지표를 선택할 수 있고, 
- `By Auto Scaling Group`을 선택하면 Auto Scaling Group에 포함되어 있는 인스턴스들의 평균값에 대한 지표를 선택할 수 있습니다.
  - 여기서는 By Auto Scaling Group 을 선택하겠습니다.
- 이중에서 사용해볼 지표는 `CPU Utilization`입니다.
  - 이 값은 그룹에 포함된 인스턴스들의 평균 CPU 사용률을 나타냅니다.

<img width="500" alt="스크린샷 2024-11-01 오후 4 23 05" src="https://github.com/user-attachments/assets/c01c66c8-2ffa-4353-b824-a68c98467309">

- 여기에 보이는 항목들이 현재 환경에서 알람으로 사용할 수 있는 지표라고 보시면 됩니다.
- `Statistic` 은 그래프의 통계 방법을 설정할 수 있고, 
- `Period`는 값을 측정할 기간의 단위를 설정할 수 있습니다.

<img width="500" alt="스크린샷 2024-11-01 오후 4 22 18" src="https://github.com/user-attachments/assets/3073ceaf-f737-4f1d-9922-5ec07ade2335">

`Conditions`

- Threshold Type은 `Static`과 `Anomaly Detection`이 있습니다.
  - `Static`은 실제 값을 토대로 해당 값과 비교하는 방법
  - `Anomaly Detection`은 머신러닝 알고리즘을 해당 지표의 과거 패턴을 학습해서 모델을 만든 후, 모델이 예상되는 패턴을 벗어나면 이상징후로 식별해서 알람을 발생시킵니다.

`Additional configuration`

- `Datapoints to alarm` 은 그래프에서 한 지표를 하나의 데이터 포인트로 보고 오른쪽에 입력된 수만큼 최근 데이터 포인트에 대해 왼쪽에 입력된 수만큼 조건에 해당되면 알람을 발생시킨다는 설정입니다.
  - 테스트를 빠르게 해보기 위해서 `1 out of 1` 로 설정한다.
- `Missing data treatment` 는 네트워크 문제 같은 이유로 데이터가 수집되지 않았을 경우에 해당 구간을 어떻게 처리할지 지정할 수 있습니다.
  - 기본 설정을 그대로 두면, 수집되지 않은 구간을 따로 집계하지는 않는다.

> **전체 설정을 정리해보면, Auto Scaling Group의 평균 CPU 사용률을 1분 단위로 지표로 확인해서 30% 이상인 지표와 최근 1개의 데이터 포인트에 대해 1개라도 있을 경우 알람을 발생시키는 설정이 됩니다.**

<img width="500" alt="스크린샷 2024-11-01 오후 4 25 08" src="https://github.com/user-attachments/assets/e93eddde-b5e6-44fd-b743-91779870da26">

- 여기서는 Notification 설정을 생략한다.

<img width="500" alt="스크린샷 2024-11-01 오후 4 26 23" src="https://github.com/user-attachments/assets/5cdeedb8-ccca-4e32-8d55-8330cfd28363">

`description` 까지 작성 완료하면 `Create a CloudWatch alarm` 으로 알람을 생성한다.

---

### 🔋 Auto Scaling Group 적용 테스트

`ASG` 의 `Desired Capacity: 1, Min: 1, Max: 4` 로 수정해준다.

---

**Cloud Watch**

<img width="1000" alt="스크린샷 2024-11-01 오후 4 42 46" src="https://github.com/user-attachments/assets/5297dcfe-7ae7-4efe-96de-bc0056216b33">

- Auto Scaling Group의 CPU 사용률이 현재 10% 이하라서 스케일 인의 조건에 부합합니다.
- 그래서 알람 상태가 되어 있지만 Auto Scaling Group의 설정에서 최소 용량을 1개로 지정해 놨기 때문에 스케일 인은 작동하지 않습니다.

**Scale-out-policy**

<img width="1000" alt="스크린샷 2024-11-01 오후 4 45 09" src="https://github.com/user-attachments/assets/6602c513-20e0-4888-a97a-7b26e5d0cd70">

- 테스트를 위해 생성된 인스턴스에 부하를 줘서 CPU 사용률을 임의로 높여보겠습니다.
- private-asg-instance 에 직접 접속해서 스트레스 도구를 설치해준다. 


```shell
# 스트레스 도구 설치 명령어
sudo yum install -y stress

# 스트레스 도구 실행 명령어
stress --cpu 1
```

<img width="1000" alt="스크린샷 2024-11-01 오후 4 57 13" src="https://github.com/user-attachments/assets/e526c891-07ec-47c9-8597-689c92043c03">

- CPU 사용률이 30% 이상이 되면서 알람이 발생하게 되고, 이 알람이 Auto Scaling Group에 설정된 스케일 아웃 정책에 따라 인스턴스가 추가되게 됩니다.

<img width="1000" alt="스크린샷 2024-11-01 오후 4 58 19" src="https://github.com/user-attachments/assets/bd10a141-3a80-4d7d-a948-60a4316af24d">

- 이 탭에서는 Auto Scaling Group 활동의 히스토리를 확인할 수 있습니다.
- 가장 위에 있는 내용을 확인해 보면 Scale Out 알람이 Scale Out Polish를 트리거해서 Desired Capacity를 1에서 2로 변경했다는 내용을 확인할 수 있습니다.

**Scale-in-policy**

<img width="1000" alt="스크린샷 2024-11-01 오후 5 02 38" src="https://github.com/user-attachments/assets/9fb03a5b-2edb-4895-8262-4e62f6217624">

<img width="1000" alt="스크린샷 2024-11-01 오후 5 05 42" src="https://github.com/user-attachments/assets/b04c3c89-c37e-4f74-ad10-f2343ddb541d">

- 인스턴스에 접속해서 부하를 주던 것을 멈추면, CPU 사용률이 다시 떨어지는 것을 볼 수 있다.
- 인스턴스의 수들도 늘었다가 줄어드는 것을 `Activity History` 에서 확인할 수 있다.

<br/>

<br/>

<br/>

## ⚡️ 정리

> `Auto Scaling Group`: Auto Scaling을 위해 AWS에서 제공하는 서비스

- Application Load Balancer와 쉬운 통합 제공
- 여러가지 조정 정책으로 오토 스케일링 조건 설정
- ALB와 ASG을 통해 고가용성 서비스 제공

**Auto Scaling Group 도입 과정**

<img width="500" alt="스크린샷 2024-11-01 오후 5 19 47" src="https://github.com/user-attachments/assets/b66be544-f4b1-4158-8a40-574c3e22f51d">

- Git 과 Java 가 설치된 사용자 정의 AMI를 만들었고, 생성될 인스턴스의 설정들을 시작 템플릿으로 미리 정의
- Auto Scaling Group으로 상황에 맞게 일관된 인스턴스를 자동으로 확장될 수 있도록 만들었습니다. 
  - 시작 템플릿을 수정하거나 버전이 업데이트되면 반영하기 위해 오토 스케일링 그룹의 새로고침 기능을 사용해봤습니다.
- Auto Scaling Group 에는 여러가지 조정 정책들이 있었고, 단순 조정 정책(Simple policy)을 사용해서 CPU 사용률을 기준으로 스케일링 테스트를 진행해봤습니다. 
- 확장이 되면 자동으로 애플리케이션 로드 밸런서에 등록이 돼서 로드 밸런싱도 자동으로 됐습니다.

**개선이 필요한 부분**

1. **현재 서버에서 데이터가 인스턴스의 개별 데이터베이스에 저장되고 있어 일관성이 없습니다.** 
  - EX) 인스턴스가 2개 이상 실행되어 로드밸런싱 되고 있을 때 회원가입을 하면 회원정보는 하나의 인스턴스 내부에 저장되게 됩니다.
  - 그렇게 되면 회원정보를 조회할 때 저장된 인스턴스로 라우팅될 때만 회원정보가 조회가 되고 다른 인스턴스로 라우팅되면 정보를 조회할 수 없게 됩니다.
2. **현재 서버에서 H2 데이터베이스를 사용하고 있습니다.**
  - `H2 데이터베이스`는 테스트할 때 많이 사용되는 `인메모리 데이터베이스`로 데이터를 메모리에 저장합니다. 
  - 그래서 인스턴스가 종료되면 데이터가 모두 삭제되게 됩니다. 
  - 실제 상용 환경에서는 사용할 수 없는 상태입니다.

**AWS의 관계형 데이터베이스인 `RDS` 를 이용하여 문제를 해결해보자.**
