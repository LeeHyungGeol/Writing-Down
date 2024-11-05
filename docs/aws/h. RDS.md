# 💡 RDS - 데이터베이스 관리를 AWS에서

## ⚡️ RDS - Relational Database Service

> AWS에서 제공하는 관계형 데이터베이스 서비스

- PostgreSQL, MySQL, MariaDB, Oracle, Microsoft SQL, AWS Aurora
- 운영적인 부분을 AWS가 관리

## ⚡️ 왜 DB 를 AWS 가 관리하는 것이 장점인가?

- **RDS 는 관리형이라 EC2 처럼 ssh 같은 방법으로 서버에 접속할 수 없다.**
  - 단점이라고 볼 수도 있지만 관리자를 포함해서 누구도 ssh 접속을 할 수 없기 때문에 보안적으로 좋다고 할 수도 있다.
- **중단 없는 스토리지 Auto-scaling**
  - Auto-scaling 은 사용 가능한 스토리지가 10% 미만의 상태가 5분 이상 지속될 때 자동으로 진행된다.
  - On-premise 로 운영한다면, 이 부분은 아주 힘든 작업이 될 수도 있다.
- **편리한 자동, 수동 백업 및 마이그레이션**
- `Multi AZ 배포`, `읽기 전용 복제본`, `RDS Proxy`


## ⚡️ Multi AZ 배포

<img width="500" alt="스크린샷 2024-11-01 오후 5 53 20" src="https://github.com/user-attachments/assets/e1d60b36-d5c1-4b3a-b91e-cb4c974c0635">

> **여러 가용영역에 복제본을 배포하는 기능**

- 고가용성에 유리
- 메인 DB 에 장애가 발생해도 복제복이 메인 DB 로 승격되어 장애 조치를 자동으로 해준다.
- 장애조치 시 조금의 지연시간 발생

## ⚡️ 읽기 전용 복제본

<img width="500" alt="스크린샷 2024-11-01 오후 5 53 42" src="https://github.com/user-attachments/assets/f2483b3f-666f-4094-8695-05155edddcee">

> **읽기 트래픽 분산을 위한 복제본**

- `메인 DB`는 쓰기 트래픽, `읽기 전용 복제본`은 읽기 트래픽
- 메인과 복제본은 각각의 스토리지를 사용한다. 메인에서 읽기 전용 복제본에 비동기로 데이터를 복사해 일관된 데이터 유지
- 복제본 각각의 엔드포인트를 사용

## ⚡️ RDS Proxy

<img width="100" alt="스크린샷 2024-11-01 오후 5 55 12" src="https://github.com/user-attachments/assets/4df5ec2f-985a-4906-aebe-b25acb152a1c">

> **RDS 보다 앞에서 트래픽을 받아서 RDS 로 전달**

- 전달하는 과정에서 DB Connection 을 재사용해서 연결 오버헤드를 줄여준다.
- 지속적인 DB 연결 시도를 모니터링한다. 
  - 연결에 지연되거나 실패가 발생하는 곳으로 트래픽을 보내지 않는 장애 조치 기능도 있다.
  -  이런 기능 덕분에 장애 발생 시 스탠바이 RDS로 빠르게 트래픽을 옮겨줄 수 있다.
- 실제로 Multi-AZ의 장애조치 시간을 최대 66% 이상 단축이 가능하다.

<br/>

<br/>

<br/>

## ⚡️ RDS 인스턴스 생성

### 🔋 RDS 보안 그룹(sg) 생성

<img width="500" alt="스크린샷 2024-11-03 오후 11 04 16" src="https://github.com/user-attachments/assets/05fbf0c1-60d9-4378-9464-120f4d5a42fd">

- 여기서는 DB 를 `PostgreSQL` 을 사용할 것이다.
- `PostgreSQL` 의 `기본 포트`인 `5432 port` 를 `Private-ec2`, `bastion-host` 에서 접속할 수 있도록 설정해줘야 한다.

### 🔋 RDS 를 위한 DB subnet group 생성

> **`Subnet Group`은 RDS 인스턴스가 배치될 수 있는 서브넷을 그룹 단위로 지정해주는 기능**

<img width="500" alt="스크린샷 2024-11-03 오후 11 09 31" src="https://github.com/user-attachments/assets/1a767d28-1bc0-4dc9-a360-81aab1c8f474">

- RDS를 배치할 가용영역을 선택하고 서브넷을 선택해줘야 합니다.
- 서브넷은 보안이나 세밀한 관리를 위해 EC2와 별개의 RDS 전용 서브넷을 생성해서 사용할 수도 있지만,
- 관리 편의를 위해 EC2와 같은 서브넷을 사용하기로 한다.

### 🔋 Create database(RDS)

- `Templates`: `Free tier` 로 선택한다. 큰 차이는 없는데 프리티어를 선택하면 프리티어 범위에서도 선택할 수 있다.
- `Multi-AZ DB Cluster`: 3개의 가용영역(AZ) 사용, 메인 DB 인스턴스 1개 & 복제본 DB 2개 인스턴스
- `Multi-AZ DB instance`: 2개의 가용영역(AZ) 사용, 메인 DB 인스턴스 1개 & 메인 인스턴스의 장애 조치를 위한 스탠바이 인스턴스(복제본 DB 1개 인스턴스)

---

<img width="500" alt="스크린샷 2024-11-04 오전 12 13 29" src="https://github.com/user-attachments/assets/071ad0cb-4ba3-479f-b733-d0ad9656412a">

**암호를 설정하는 방법**에
- `AWS Secrets Manager`라는 서비스를 통해 암호를 관리하는 기능이 있습니다.
  - 이 서비스를 사용하면 주기적으로 비밀번호를 자동으로 교체하거나 접근을 제한하는 등 보안을 강화할 수 있습니다.
  - 여기서는 따로 사용하지 않는다.
- `Auto generate password`: AWS 에서 무작위 암호를 생성해주는 설정
  - 암호를 생성하면 한번만 암호를 가르쳐준다.
  - 이 설정을 사용하게 된다면 한번 가르쳐주는 암호를 잘 보관해야 한다.

---

**Instance configuration, Storage**

<img width="500" alt="스크린샷 2024-11-04 오전 12 16 43" src="https://github.com/user-attachments/assets/2d448187-1100-4437-b10e-c49d0c32f1e1">

- `Storage autoscaling`: Auto-scaling 을 활성화시키고 최대 스토리지 용량을 지정할 수 있다.
  - 활성화가 되면 조건에 만족할 경우 최대 입력한 값 만큼 스토리지 용량이 자동으로 늘어나게 된다.
  - 여기서는 활성화 해도 auto-scaling 되었는지 확인하기 어려워서 사용하지 않는다.

---

**Connectivity**

<img width="500" alt="스크린샷 2024-11-04 오전 12 22 46" src="https://github.com/user-attachments/assets/116481b9-46fc-4616-b03f-a94b8a472aae">

- `Compute resource`은 단일 EC2 인스턴스에 연결할 때 주로 사용하고, 여기서는 Auto-scaling group 에서 생성되는 다수의 인스턴스에 연결될 것이기 때문에
  - 여기서는 따로 설정하지 않는다.
- 여기서 rds 를 private-subnet 에서 사용할 것이기 때문에 `Public access` 는 막아둔다.
- `Availability Zone(AZ)` 는 따로 설정하지 않으면 AWS 에서 적절하게 배치하게 되니 설정하지 않는다.
- Certificate Authority 는 DB 연결 암호화를 위한 인증서를 설정할 수 있다.
  - 여기서는 기본 값으로 둔다.

---

**Database authentication**

<img width="500" alt="스크린샷 2024-11-04 오전 12 25 57" src="https://github.com/user-attachments/assets/444ef1d2-04c4-4f6a-9147-5408f9ab1ae8">

- `Database authentication`: DB 에 연결할 때 인증하는 방식을 설정할 수 있다.
- `Monitoring`: 여기서는 사용하지 않을 것이기 때문에 꺼둔다.

---

**Additional configuration**

<img width="500" alt="스크린샷 2024-11-04 오전 12 35 23" src="https://github.com/user-attachments/assets/1854b0e1-11e1-4975-82ec-ec1a8cc48a9f">

- `Initial database name`: 기본으로 생성할 DB 이름
- `Automated backup`: 여기서는 비용이 발생할 수 있기 때문에 꺼둔다.
- `Encryption`: DB 에 저장되는 데이터에 대한 암호화를 설정할 수 있다.
  - 비용이 추가로 들거나 하지는 않으니 웬만해서는 설정해준다. 
- `Log exports` 설정 X
- `Maintenance`: DB 버전 업그레이드를 자동으로 하도록 설정
  - 이 업그레이드는 `Maintenance window` 로 설정한 시간에 수행된다.
  - DB 에 대해 수동으로 한 수정도 이 시간에 같이 할 수 있다.
  - 이렇게 함으로써 업데이트나 수정 중에 DB 가 일시적으로 중단될 수 있는 시간을 사용자에게 용량을 최소한으로 미칠 수 있는 시간대에 설정할 수 있다.
  - EX) 서비스의 사용량이 새벽 2시부터 4시까지 가장 적다면 이 기간에 업데이트를 진행하여 사용자에게 최소한의 용량을 줄 수 있습니다.
- `Deletion protection`: 우발적으로나 실수로 삭제되는 것에 대한 잠금 설정을 할 수 있습니다.

<br/>

<br/>

<br/>

## ⚡️ RDS 인스턴스 연결

### 🔋 서버에 RDS 연결 설정을 할 새로운 시작 템플릿(launch template) 생성

<img width="500" alt="스크린샷 2024-11-04 오전 12 59 43" src="https://github.com/user-attachments/assets/5c169d1f-701a-4acf-854c-04d8ad5ed3e8">

**User Data 수정**

- 아래 명령어의 대괄호( [, ] ) 는 지워주셔야합니다!

```shell
#!/bin/bash

# Git 레포지토리 클론 및 브랜치로 이동

git clone -b 3_monolithic_rds https://github.com/burger-2023/aws-operation-prac.git

# 폴더 이동

cd aws-operation-prac

# Gradle을 이용한 Spring Boot 프로젝트 빌드 후 빌드된 Spring Boot 애플리케이션 실행

./gradlew build 

sudo java -jar build/libs/aws-msa-monolithic-prac-0.1.jar \
--spring.datasource.url=jdbc:postgresql://[rds 엔드포인트:db 포트번호/rds DB Name] \
--spring.datasource.username=[rds Master username] \
--spring.datasource.password=[rds Master password]
```

---

<img width="500" alt="스크린샷 2024-11-04 오전 1 29 30" src="https://github.com/user-attachments/assets/8a61d5de-0f82-4b1b-a1e7-dcbae3efbcb8">

- launch template 을 생성했으면, `Auto scaling group` 의 버젼도 새로 생성한 launch template 으로 변경해줍니다.

<img width="500" alt="스크린샷 2024-11-04 오전 2 14 45" src="https://github.com/user-attachments/assets/d30bbe87-8637-43be-9490-4b369662f635">

- postman 으로 user 를 몇개 더 등록 후에, EC2 인스턴스를 삭제하고, auto scaling group 에 의해 새로운 인스턴스가 생성되도록 한다.

<img width="500" alt="스크린샷 2024-11-04 오전 2 24 34" src="https://github.com/user-attachments/assets/ca851917-324e-4645-922c-c6c576ba3953">

- 새로 생성된 인스턴스로 `/health_check` 를 해보면 ipAddress 가 이전과 다른 것을 확인할 수 있다.

<img width="500" alt="스크린샷 2024-11-04 오전 2 26 26" src="https://github.com/user-attachments/assets/0f9860e4-1aa1-4791-8d02-7b6358cef58c">

- 다시 postman 으로 이전에 종료된 EC2 인스턴스 에서 생성한 user 를 조회해보면 정상적으로 조회되는 것을 확인할 수 있다.

<br/>

<br/>

<br/>

## ⚡️ RDS 읽기 복제본 - 읽기 트래픽 분산

<img width="500" alt="스크린샷 2024-11-04 오전 2 30 00" src="https://github.com/user-attachments/assets/25937ebd-00e5-4c00-ab9f-fc35cd47d61d">

`Create read replica`
- 이 버튼을 통해 읽기 전용 복제본을 생성할 수 있는데 비활성화가 되어 있는 이유는 자동 백업이 비활성화되어 있어서입니다.
- 읽기 전용 복제본은 자동 백업으로 생성된 스냅샷을 통해 생성되기 때문에 자동 백업이 활성화되어 있고 스냅샷이 생성되어 있어야 합니다.
- 활성화해주기 위해 `Modify` 한다.

---

<img width="500" alt="스크린샷 2024-11-04 오전 2 35 59" src="https://github.com/user-attachments/assets/a1b254fd-2d1f-4304-8b76-e2db3a775f3a">

- `Backup retention period`: 자동으로 생성된 스냅샷에 대해 얼마의 기간동안 보관할지에 대한 설정을 할 수 있다.
  - `0` 으로 선택하면 백업을 하지 않겠다는 의미이다. 최저비용으로 하기 위해 1 을 선택한다.

---

<img width="500" alt="스크린샷 2024-11-04 오전 2 38 45" src="https://github.com/user-attachments/assets/670d68fe-22c7-4f12-9dff-f6de0800e98a">

- `When to apply modifications`
- 위는 `maintenance window` 기간에 업데이트를 진행하도록 설정할 수 있고
- 아래는 즉시 수정하도록 할 수 있다.
  - 만약 데이터베이스가 중단되거나 성능에 영향을 미칠 수 있는 수정일 경우에는 maintenance window 기간에 수정을 하도록 활용할 수 있고 지금은 즉시 수정되도록 한다.

---

<img width="500" alt="스크린샷 2024-11-04 오전 2 46 12" src="https://github.com/user-attachments/assets/466830c4-9d4f-4405-b9f8-ff030fea55be">

`Automated Backups`
- 백업 설정에 대한 항목이 추가된 것을 확인할 수 있다.

---

<img width="500" alt="스크린샷 2024-11-04 오전 2 47 17" src="https://github.com/user-attachments/assets/2c13d6ed-1594-49e3-9ef6-42e53c360722">

`Snapshots > System`
- 백업 설정으로 스냅샷이 생성된 것을 확인할 수 있다.
- 이 스냅샷을 통해 읽기 전용 복제본을 생성할 수 있다.

---

다시 `Databases > Actions > Create read replica`


<img width="500" alt="스크린샷 2024-11-04 오전 2 56 22" src="https://github.com/user-attachments/assets/2c22330f-9098-457c-8be1-d1eedd7566b1">

<img width="500" alt="스크린샷 2024-11-04 오전 2 56 46" src="https://github.com/user-attachments/assets/8dfe040b-6173-4de1-86a6-23b18118f93e">

<img width="500" alt="스크린샷 2024-11-04 오전 2 57 22" src="https://github.com/user-attachments/assets/7618adb0-1674-4603-869b-7f720d4ba8e7">

`goopang-rds-db-read-replica`
- Instance configuration, AWS Region, Storage 설정은 그대로 둔다.
- `Storage autoscaling` 설정은 꺼준다.
- `Availability` 는 비용을 절감하기 위해 `Single DB instance` 로 한다.
- `Connectivity`(서브넷이나 퍼블릭 액세스, VPC 등 네트워크 설정)은 그대로 둔다.
- `Database authentication`(인증방식)도 그대로 둔다.
- `Performance Insights` 는 따로 사용하지 않을 것이기 때문에 비활성화 해준다.
- `Deletion protection` 은 활성화 해준다.

<img width="500" alt="스크린샷 2024-11-04 오전 3 06 50" src="https://github.com/user-attachments/assets/2e96a48a-dac9-481f-b046-efbba0f991a3">

---

이제 시작 템플릿 새로운 버전을 통해 쿠팡 서버가 읽기 전용 복제본에 읽기 트래픽을 보내도록 설정한다.

<img width="500" alt="스크린샷 2024-11-04 오전 3 08 44" src="https://github.com/user-attachments/assets/f03bd555-175f-4caa-b05a-665614bf5628">

User Data 를 아래와 같이 수정해준다.

```shell
#!/bin/bash

#Git 레포지토리 클론 및 브랜치로 이동
git clone -b 4_monolithic_rds_read_replica https://github.com/burger-2023/aws-operation-prac.git

#폴더 이동
cd aws-operation-prac

#Gradle을 이용한 Spring Boot 프로젝트 빌드 후 빌드된 Spring Boot 애플리케이션 실행
./gradlew build

# 읽기 전용으로 사용할 데이터베이스 엔드포인트:포트/DB이름, 유저이름, 비밀번호
# 쓰기 전용으로 사용할 데이터베이스 엔드포인트:포트/DB이름, 유저이름, 비밀번호
java -jar build/libs/aws-msa-monolithic-prac-0.1.jar \
--spring.datasource.write.jdbc-url=jdbc:postgresql://[엔드포인트:포트/데이터베이스] \
--spring.datasource.write.username=[유저이름] \
--spring.datasource.write.password=[비밀번호] \
--spring.datasource.read.jdbc-url=jdbc:postgresql://[읽기 전용 엔드포인트:포트/데이터베이스]  \
--spring.datasource.read.username=[유저이름] \
--spring.datasource.read.password=[비밀번호]
```

<img width="500" alt="스크린샷 2024-11-04 오전 3 22 00" src="https://github.com/user-attachments/assets/a32fd3e0-e6b2-4dc2-ae32-a9ac3e3112a3">

- `health_check` 응답값의 `branch` 값이 변경된 것을 확인할 수 있다.

---

- 읽기 전용 복제본에 읽기 요청이 분산이 되는지 확인해보겠습니다.
- 확인 방법은 애플리케이션의 요청에 따른 연결된 RDS의 엔드포인트를 로그 파일에 출력하도록 이번 버전에 설정해놨습니다.
- 그래서 읽기 요청을 한번, 쓰기 요청을 한번 보내고 해당 로그 파일에 어떻게 출력되는지 확인해보겠습니다.
- `postman` 으로 `유저 조회`, `유저 등록` 요청을 한번씩 보내놓는다.
- private-ec2 에 connect 한다.
- 아래의 두 명령어로 로그 파일을 확인할 수 있다.
  - cat 명령어로 로그 파일의 내용을 확인해보면 리드 리플리카 엔드포인트가 먼저 출력되어 있고 다음은 메인 RDS 엔드포인트가 보입니다.

```shell
ls -al /aws-operation-prac/

cat /aws-operation-prac/application.log
```

<br/>

<br/>

<br/>

## ⚡️ RDS Multi AZ - 고가용성 확보

`rds 메인 인스턴스를 선택 > Actions > Convert to Multi-AZ deployment`

<img width="500" alt="스크린샷 2024-11-04 오전 3 49 19" src="https://github.com/user-attachments/assets/d8517b6e-4018-4804-8cae-ea20e931b0a6">

<img width="500" alt="스크린샷 2024-11-04 오후 5 08 10" src="https://github.com/user-attachments/assets/6c6c0ff5-2949-49e5-8637-dd585bd44d21">

- 위의 경고는 Multi AZ 복제본을 생성할 때 데이터를 동기식으로 복제하게 됩니다. 
- 그러면 메인 데이터베이스의 성능에 영향을 줄 수 있기 때문에, 1기 복제본을 생성해서 해당 1기 복제본을 Multi AZ로 변환하고, 
- 변환이 되면 1기 복제본을 메인으로 승격시켜 사용해라는 경고입니다.
- 실습에서는 바로 변환해준다. 실제 운영할 때는 해당 옵션을 사용하는 것이 좋다.

---

<img width="1000" alt="스크린샷 2024-11-04 오후 5 28 54" src="https://github.com/user-attachments/assets/25f2350c-db40-47e5-99ae-c3c3b053a682">

`Multi-AZ` 속성에 `Yes` 표시를 확인한다.
- 이렇게 되면, `Subnet Group`으로 지정했던 가용 영역 중 한 곳에 **스탠바이 복제본**이 생성됐고 
- 메인 데이터베이스에 문제가 생기면 **스탠바이 복제본이 메인 데이터베이스로 승격이 된다.**

---

<img width="500" alt="스크린샷 2024-11-04 오후 5 27 29" src="https://github.com/user-attachments/assets/f09f7b76-827d-4a28-9350-703db8627fac">

<img width="1000" alt="스크린샷 2024-11-04 오후 5 33 09" src="https://github.com/user-attachments/assets/fb0479bd-f6c7-46d9-9d02-525ed0a0762f">

`Reboot` 를 하면서 `Failover` 를 하도록 선택할 수 있다.

- failover 과 완료된 후에, 메인 rds 인스턴스의 가용영역(AZ) 이 `2a -> 2b` 로 스탠바이 복제본과 교체된 것을 확인할 수 있다.
- 의도적인 재부팅을 포함해 어떤 장애조치 상황이 와도 `Multi-AZ` 기능은 자동으로 발생합니다. 
- AWS 내에서도 최대한 빠르게 전환이 되도록 노력하고 있지만 약간의 다운타임은 부가피하게 발생한다고 합니다.
- 그래도 Multi AZ를 사용함으로써 대기시간을 최소화하여 자동으로 높은 가용성을 확보하는데 큰 도움이 된다.

<br/>

<br/>

<br/>

## ⚡️ 정리

<img width="500" alt="스크린샷 2024-11-04 오후 6 55 17" src="https://github.com/user-attachments/assets/366f8de0-bb81-4c52-8ab4-cdb2367a55a3">

> AWS 관리형 관계형 데이터베이스

- `읽기 전용 복제본`: 메인 RDS로부터 읽기 트래픽을 분산해 가용성을 형성시킬 수 있다.
- `멀티 AZ 배포`: 메인 RDS와 다른 가용 영역에 대기 복제본을 배포해서 메인 RDS에 장애가 발생할 때를 대비할 수 있는 구조로 만들 수 있었습니다.
- `RDS Proxy`: 작은 규모의 서비스에서는 큰 효과를 못 볼 수도 있지만 서비스가 커지고 데이터베이스의 연결이 많아지면 큰 효과를 볼 수 있는 서비스입니다.
  - 서비스로부터 연결을 관리해서 불필요한 커넥션을 빠르게 종료하고 필요에 따라 재사용도 처리해줍니다. 
  - 또 장애가 발생하면 빠르게 해당 인스턴스에 대한 커넥션을 처리해서 기존 Multi-AZ 배포의 장애 조치보다 최대 66%까지 개선할 수 있었습니다.
  - 그 외에도 Public Access가 불가능하기 때문에 보안을 강화하기에도 좋았습니다.
- `스냅샷 복원`: 스냅샷을 자동과 수동으로 생성할 수 있었는데, 수동은 기간에 상관없이 보관할 수 있고, 자동은 1일에서 35일까지 보관할 수 있었습니다.
  - 원하는 리전의 가용영역에 RDS를 복원할 수 있었고, 데이터 암호화가 되지 않았던 인스턴스의 스냅샷도 복원시에는 암호화를 활성화할 수도 있었습니다.

**개선이 필요한 부분**

- 파일 저장 -> **거의 무한히 확장되는 저장소 `S3`**
- 파일 데이터의 일관성 -> **더 빠르고 더 안전하게 배포를 위한 `CloudFront`**
