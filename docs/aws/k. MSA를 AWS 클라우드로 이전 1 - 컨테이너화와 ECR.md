# 💡 MSA를 AWS 클라우드로 이전 1 - 컨테이너화와 ECR

# 💡컨테이너

> **애플리케이션과 필요한 의존성, 라이브러리를 패키징 하는 기술**

- 패키징은 EC2 를 이미지로 만들 때와 비슷하다고 생각하면 된다.
- 애플리케이션을 일관성 있게 배포 및 실행
- 시스템을 경량화시켜 빠른배포

## ⚡️ 컨테이너의 특징: 환경의 일관성

<img width="700" alt="스크린샷 2024-11-14 오후 8 59 19" src="https://github.com/user-attachments/assets/84c7b433-2058-4329-8376-bda07fa28be4">

- 모두 다른 환경
    - 서버의 OS, 라이브러리, 버전 등
    - EX) `내 컴퓨터에서는 잘 돌아갔는데...` 와 같은 문제를 컨테이너를 활용하면 줄일 수 있다.
- 동일한 환경이었다면, 동일한 문제의 원인
- 컨테이너 기술을 통한 환경의 일관성

## ⚡️ 컨테이너의 필요성: 시스템 경량화

<img width="700" alt="스크린샷 2024-11-14 오후 8 59 36" src="https://github.com/user-attachments/assets/280a0c94-4ca3-481b-ac67-2c9530f707f9">

- **VM보다 많이 가벼움**
    - `VM`은 하이퍼바이저와 각각의 애플리케이션에 게스트 OS 생성
    - **`컨테이너`는 호스트 OS와 Binary, Library를 공유해서 사용하기 때문에 시스템을 상대적으로 경량화할 수 있다.**
- **컨테이너는 이미지 단위로 생성된다.**
- 이미지는 버젼별로 태깅돼서 버전 관리가 가능 및 다른 버젼으로 롤백 가능
- CI/CD 통합 및 자동화
- 격리된 환경으로 인한 보안 강화

## ⚡️ 컨테이너와 MSA

- 컨테이너는 독립적인 배포와 확장이 가능해야 한다.
    - 컨테이너를 사용하면 각 서비스의 종속성과 환경을 격리해서 독립적인 배포를 쉽게 할 수 있다.
    - **AMI와 비슷한 역할을 한다고 생각하실 수도 있지만 훨씬 경량화되어 있고 EC2 외에 다양한 환경에서 사용할 수 있는 차이점이 있습니다.**
- **컨테이너는 각 마이크로 서비스에 필요한 정확한 양의 메모리나 CPU를 할당할 수 있어 리소스를 더 효율적으로 관리할 수도 있습니다.**
- OS레벨의 장애 격리

# 💡 Docker

> **컨테이너 기술을 사용해 패키징하고 실행하는데 사용되는 오픈소스 플랫폼**

- `Docker Engine` - 컨테이너를 관리하는 핵심 기능을 담당
    - **컨테이너가 실행되는 환경을 제공하고 API 를 통해 개발자와 상호작용할 수 있다.**
- `Dockerfile` - `Docker Image`를 생성하기 위한 스크립트 파일
    - **애플리케이션 실행에 필요한 환경과 명령어를 정의**
- `Docker Image` - `Dockerfile`의 빌드 결과물
    - **애플리케이션과 빌드 결과물로 애플리케이션과 의존성이 패키징된 형태**
- `Docker Container` - `Docker Image` 를 기반으로 실행되는 `인스턴스`
    - Docker Engine 에 의해서 관리된다.

---

- `Docker Engine`: 로컬 환경에서는 사용할 수 있는 도커의 플랫폼은 가장 기본적인 플랫폼
- 여러 컨테이너를 효율적으로 관리할 수 있는 도커 컴포즈
- 이 둘을 모두 UI를 통해 작업할 수 있는 도커 데스크탑 등이 있습니다.

## ⚡️ Docker Hub

`로컬에서 도커 이미지를 만들고 클라우드에서 사용하려면 어떻게 해야 할까?`

> **`컨테이너 이미지`를 저장하고 공유할 수 있는 이미지 저장소**

- **도커 허브에 이미지를 푸시하고, EC2에서 풀받아 사용할 수 있음**
- 다양한 오픈소스 이미지가 퍼블릭으로 저장되어 있음

# 💡AWS ECR: Elastic Container Registry

<img width="300" alt="스크린샷 2024-11-14 오후 8 59 51" src="https://github.com/user-attachments/assets/841ceaa3-fc3c-4cad-88dd-04687a4400a2">

> **AWS에서 제공하는 Docker Image 를 저장하는 컨테이너 이미지 저장소**

- ECR을 통한 다른 서비스와 쉬운 통합
- ECR을 사용하면 EC2에서 이미지를 풀받거나 ECS에서 이미지를 사용하기 굉장히 쉽다.
- 보안 기능 제공

<br/>

<br/>

<br/>

## ⚡️ 서버를 도커 이미지로 만들고 실행

- `aws-msa-user-operation-prac`

```shell
FROM amazoncorretto:17.0.7-al2023-headless

VOLUME /tmp

COPY build/libs/aws-msa-user-service-1.0.jar user-service.jar

ENTRYPOINT ["java","-jar","user-service.jar"]%
```

- `FROM` 명령어를 통해 아마존에서 제공하는 JDK인 Amazon Coreto 17.0.7 기반의 이미지를 사용하여 빌드하고
- `VOLUME` 명령어를 통해 TMP 디렉토리를 `컨테이너 볼륨`으로 지정합니다.
- 이를 통해 컨테이너가 삭제되어도 해당 디렉토리에 저장된 데이터는 영구적으로 보존됩니다.
    - 그러나 이 볼륨은 익명 볼륨으로 생성되며, 컨테이너를 재시작할 때 기존 볼륨에 자동으로 접근하지 않습니다.
    - 즉, 이 명령어만으로는 컨테이너 간의 데이터가 공유가 되지는 않고, **필요에 따라 데이터 공유가 필요하다면 컨테이너를 실행하는 명령어에서 볼륨 마운트를 명시적으로 지정해줘야 합니다.**
- `COPY` 명령어를 통해 빌드된 jar 파일을 user-service.jar 로 실행될 컨테이너에 복사하고,
- `ENTRYPOINT`를 통해 복사된 jar 파일을 실행하도록 명령어를 지정합니다.

1. `COPY` 명령어에 있는 jar 파일을 만들어주기 위해 Spring 애플리케이션을 빌드한다.

```shell
# 스프링 애플리케이션 빌드 명령어
./gradlew clean build
```

2. Docker Image build 명령어를 입력한다.

```shell
# 도커 이미지 빌드 명령어
# 이 명령어는 현재 폴더에 도커 파일이 있으면 빌드를 진행하고 이미지 이름은 유저 서비스로 하겠다는 의미
docker build -t user-service .

docker build -t product-service .

docker build -t cart-service .
```

3. 도커 이미지 조회 명령어

```shell
docker images 
```

4. `docker 컨테이너들`이 사용할 `네트워크`를 구성해줘야 한다.

```shell
docker network create goopang-network
```

5. PostgreSQL 컨테이너를 실행한다.

하지만 현재 로컬에는 Postgres 이미지가 없다. 이럴 땐 Docker Hub에서 받아올 수 있다.

```shell
# docker hub 에서 postgres 이미지 Pull 명령어
docker pull postgres

# postgres 컨테이너 실행 명령어
docker run --name postgres -p 5432:5432 -d \
-e POSTGRES_USER=postgres \
-e POSTGRES_PASSWORD=postgres \
-e POSTGRES_DB=goopangdb \
--network goopang-network \
postgres
```

- `p` 는 컨테이너 port 와 호스트 port 를 바인딩해준다.
- `network` 는 컨테이너를 실행할 네트워크를 지정한다.
- 마지막의 `postgres` 는 실행할 docker image 를 입력한다.
- `4cebc89c345a3ca77be9f84cf2e79c6a5a59fdca027fded621d0e0eb84663eed` 와 같이 실행하고 생성되는 무작위의 문자열은 `컨테이너의 ID` 에 해당한다.

```shell
docker ps
```

- `docker ps` 명령어를 통해 **현재 실행중인 컨테이너**를 확인할 수 있다.

6. 서비스를 컨테이너화 한다.

```shell
# user service 실행 명령어

docker run -p 8002:8002 --name user-service-container \
-e "spring.datasource.url=jdbc:postgresql://postgres:5432/goopangdb" \
-e "spring.datasource.username=postgres" \
-e "spring.datasource.password=postgres" \
-e "spring.datasource.driverClassName=org.postgresql.Driver" \
--network goopang-network \
-t user-service

# product service 실행 명령어

docker run -p 8001:8001 --name product-service-container \
-e "spring.datasource.url=jdbc:postgresql://postgres:5432/goopangdb" \
-e "spring.datasource.username=postgres" \
-e "spring.datasource.password=postgres" \
-e "spring.datasource.driverClassName=org.postgresql.Driver" \
--network goopang-network \
-t product-service

# cart service 실행 명령어

docker run -p 8000:8000 --name cart-service-container \
-e "spring.datasource.url=jdbc:postgresql://postgres:5432/goopangdb" \
-e "spring.datasource.username=postgres" \
-e "spring.datasource.password=postgres" \
-e "spring.datasource.driverClassName=org.postgresql.Driver" \
-e "msa.product-service.url=product-service-container:8001" \
-e "msa.user-service.url=user-service-container:8002" \
--network goopang-network \
-t cart-service
```

- Docker 의 장점 중 하나를 확인할 수 있는데
- 첫 번째 환경 변수를 보면 `URL`에 `postgres` 라고 입력되어 있다.
- 여기서 `postgres`는 조금 전 생성했던 `Postgres 컨테이너의 이름`을 입력한 것입니다.

**이렇게 같은 네트워크 내에서 컨테이너의 이름을 환경 변수로 사용하면 해당 컨테이너를 가리킬 수 있게 됩니다.**

- cart-service 는 다른 서비스들과 통신하는 부분이 있다.
- 이 부분을 기존에는 Localhost 주소의 다른 포트로 요청을 보냈지만
- 이제는 `Docker network`에 같이 있기 때문에 Postgres 연결과 마찬가지로 컨테이너의 이름을 입력해주면 됩니다.
- 명령어의 환경 변수 부분에 제품 서비스와 유저 서비스 URL을 각각 실행된 컨테이너 이름으로 지정되어 있습니다.

<br/>

<br/>

<br/>

## ⚡️ ECR - 컨테이너와 AWS 통합

`docker image` 를 `AWS` 와 통합하기 위해 `ECR` 을 사용한다.

### 🔋 Create Repository

<img width="500" alt="스크린샷 2024-11-15 오후 4 06 17" src="https://github.com/user-attachments/assets/01ecf66d-27b0-46ef-ab49-cf4109c1fb91">

`Image tag mutability`

- docker image 를 push 할 때, **이미 저장되어 있는 `tag`를 덮었을지에 대한 설정**
    - 만약 활성화하고 기존에 있던 태그와 똑같은 tag의 image를 push하면 오류가 발생하게 됩니다.
    - 여기서는 비활성화해서 중복된 tag를 push할 수 있도록 하겠습니다.
    - `중복된 tag`를 push하면 **기존에 있던 image는 tag가 없어집니다.**

`Image scanning settings - deprecated`

- push 를 할 때 `image 보안 scan` 을 활성화할지에 대한 설정
    - 이 단계에서의 설정은 지원이 중단되었다.

`Encryption settings`

- `KMS` 서비스를 통한 암호화를 사용할지에 대한 설정
    - ECR에는 기본 암호화가 있기 때문에 여기사는 설정하지 않는다.

---

### 🔋 IAM User 생성 - Create User

**repository 생성이 완료되었다면, private repository 에 `push 권한이 있는 IAM User`를 만든다.**

> `IAM`: **어떤 리소스에 누가 접근할 수 있는지, access 할 수 있는 사용자가 어떤 작업을 수행할 수 있는지에 대한 제어**

- IAM 사용자는 개별 계정 단위로 권한을 부여할 수 있다.

<img width="500" alt="스크린샷 2024-11-15 오후 4 23 13" src="https://github.com/user-attachments/assets/d52b66b5-af37-460d-a5e8-27064a82e24c">

`Provide user access to the AWS Management Console - optional`

- 현재 생성하려는 사용자가 AWS 콘솔에 ID 와 PW 를 입력해서 접속하여 사용할 User 인지를 설정할 수 있다.
    - 여기서는 체크하지 않는다.

<img width="500" alt="스크린샷 2024-11-15 오후 4 22 33" src="https://github.com/user-attachments/assets/b01057bc-bae4-4707-8ff2-9f62dfb15b03">

`Permissions 옵션`은 `Attach Policies Directory`를 선택

- `Attach Policies Directory`: AWS에서 만들어 놓았거나 개발자가 직접 만든 권한 정책을 생성할 유저에게 직접 연결하는 방법
- `EC2InstanceProfileForImageBuilderECRContainerBu\
- ilds` 를 선택

---

### 🔋 Create Access Key

<img width="500" alt="스크린샷 2024-11-15 오후 4 26 00" src="https://github.com/user-attachments/assets/ae4161ab-6411-4db5-bed2-11cb8d85e463">

`Security Credentials > Access Keys`

- 이 키를 통해 로컬에서 사용자의 CLI 로그인을 하고 사용자의 권한을 통해 이미지를 푸시할 수 있다.

아래의 노란 경고는

- CloudShell 을 사용하거나 다른 방법으로 인증 활성화를 권장하는 경고이고 아래 체크는 권장 사항을 이해하고 생성하겠다는 의미
- 다른 방법을 권장하는 이유는 액세스 키를 직접 생성하고 관리하는 것은 노출의 위험이 있기 때문이다.
- 여기서는 그냥 체크하고 진행한다.

key 를 생성하면 `Access Key` 와 `Secret Access Key` 를 알려주는데, `Secret Access Key` 는 여기서 한번만 알려주기 때문에 안전한 곳에 저장해두어야 한다.

AKIARJV6GE6NEURBZ5JA

DpKEiJI3k2xl6croZ+do1PvG5HzlCLVa4yhuLnNO

---

### 🔋 AWS CLI 를 통해 image 를 push

**AWS CLI 로그인 명령어**

```shell
aws configure
```

- `aws configure` 로 조금 전 생성했던 IAM User 로 로그인한다.

### 🔋View Push Commands

<img width="1000" alt="스크린샷 2024-11-20 오전 12 18 10" src="https://github.com/user-attachments/assets/b407de31-d21d-4f91-9745-dd84d1499ed3">

- 이 화면엔 push 된 Image 들이 보인다.

1. `aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin 089519433626.dkr.ecr.ap-northeast-2.amazonaws.com`

- 로컬에서 로컬에서 도커 명령어로 AWS ECR에 로그인하는 명령어

2. `docker build -t goopang-user-service .`

- 도커 빌드
- 그 전에 해당 폴더로 이동해줘야 한다.
- 여기서는 `ARM 아키텍쳐`를 사용하고 있었다. 상대적으로 성능이 좋고 비용이 저렴하기 때문이다.
- `docker` 는 기본적으로 `build` 하면 **ARM 아키텍쳐에서는 컨테이너가 실행되지 않는다.**
- 그래서 **ARM 전용으로 빌드하는 명령어를 추가해준다.** `--platform linux/arm64` 를 추가해줘야 한다.

3. `docker tag goopang-user-service:latest 089519433626.dkr.ecr.ap-northeast-2.amazonaws.com/goopang-user-service:latest`

- 빌드한 이미지에 태그를 작성하는 명령어

4. `docker push 089519433626.dkr.ecr.ap-northeast-2.amazonaws.com/goopang-user-service:latest`

- ECR 리포지토리로 이미지를 푸시하는 명령어

**AWS ECR 빌드 명령어 - arm 아키텍쳐용**

```shell
docker build --platform linux/arm64 -t [이미지이름] .
```

<br/>

<br/>

<br/>

## ⚡️ ECR 리포지토리에 push 한 Image 를 EC2 에 배포

EC2 에 부여했던 Profile Role 에 ECR 에 접근할 수 있는 권한을 부여해야 한다.

`IAM > Roles`

이전에는 기존에 AWS 에서 만들어놨던 것을 사용했는데, 이번에는 직접 만들어본다.

`Add Permissions > Create inline policy`

**ECR access policy Json**

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "ecr:GetDownloadUrlForLayer",
        "ecr:BatchGetImage",
        "ecr:BatchCheckLayerAvailability",
        "ecr:GetAuthorizationToken"
      ],
      "Resource": "*"
    }
  ]
}
```

- `Action`에 나열된 `ECR`에 대한 작업을 적용하는 모든 리소스에 허용하는 정책

**이제 S3, CloudFront, ECR 에 대한 권한이 생겼다.**

### 🔋 docker 가 설치된 AMI 생성

EC2 인스턴스를 생성한다. - `msa-image-instance`

<img width="500" alt="스크린샷 2024-11-20 오전 2 21 44" src="https://github.com/user-attachments/assets/20918d2d-b9ac-4895-9360-e08411481500">

인스턴스가 생성되면 `connect` 로 접속하여, `docker` 를 설치해준다.

**Amazon Linux 2023 Docker 설치 명령어**

```shell
sudo dnf install docker # docker 설치

sudo systemctl start docker # docker 시작

sudo usermod -aG docker $USER # ec2-user에 docker 명령어 권한 부여를 위해 docker 그룹에 추가

newgrp docker # docker 그룹 변경 사항 적용
```

`Actions > Image and templates > Create image`

<img width="500" alt="스크린샷 2024-11-20 오전 2 46 14" src="https://github.com/user-attachments/assets/a9b508f4-b5d0-4dff-be87-7c950d903f9c">

---

**3개의 서비스(user, cart, product)를 실행할 인스턴스를 새로 생성한다.**

<img width="500" alt="스크린샷 2024-11-20 오전 3 18 13" src="https://github.com/user-attachments/assets/d1a36635-ef2d-4bf8-a608-0a94175d11c5">

<img width="500" alt="스크린샷 2024-11-20 오전 2 51 55" src="https://github.com/user-attachments/assets/246b7a74-464e-4cf2-90ba-d288cffb5194">


security group 을 새로 생성한다.
- subnet 은 아직 ALB 가 없기 때문에 public 으로 해준다.

<img width="500" alt="스크린샷 2024-11-20 오전 2 52 36" src="https://github.com/user-attachments/assets/79bb90a7-1a6f-4c4d-a523-6ad8e29daa90">

- IAM Profile 인스턴스 프로파일을 선택해준다.

이제 생성한 ec2 에 접속(connect) 한다.

---

> **`ECR` 에 로그인 하고, `docker image` 를 `Pull` 받는다. 그리고, `컨테이너를 실행`하기만 하면 된다.**

**ecr 로그인**

`aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin [ecr-url]`
- 로컬에서 할 때와 다르게 AWS CLI 로그인이 없이 되는 이유는 EC2 프로파일로 선택한 IAM 롤이 EC2에 로그인되어 있기 때문입니다.

**EC2의 identity 조회 명령어**

`aws sts get-caller-identity`
- **`EC2 의 Role` 을 확인**

**image pull**

`docker pull [ecr-repository-uri]`

**container 네트워크 생성**

`docker network create ecommerce-network`
- **컨테이너 실행 전에 `docker network` 를 생성해준다.**

**user container run**

`docker run --rm -p 8002:8002 --network ecommerce-network --name user-service-container -t [ecr-repository-uri]`

**background user container run**

`docker run --rm -d -p 8002:8002 --network ecommerce-network --name user-service-container -t [ecr-user-repository-uri]`

**background product container run**

`docker run --rm -d -p 8001:8001 --network ecommerce-network --name product-service-container -t [ecr-product-repository-uri]`

**background cart container run**

```shell
docker run --rm -d -p 8000:8000 --network ecommerce-network \
-e "msa.product-service.url=product-service-container:8001" \
-e "msa.user-service.url=user-service-container:8002" \
--name cart-service-container \
-t [ecr-cart-repository-uri]
```

<br/>

<br/>

<br/>

## ⚡️ Lifecycle Rule 을 이용하여 ECR Image 관리 및 비용 절감

<img width="500" alt="스크린샷 2024-11-20 오전 3 33 46" src="https://github.com/user-attachments/assets/a2c494ee-6504-4f5d-8917-914be59d8015">

- Untagged 된 이미지들은 자동으로 삭제하게 만든다.

<br/>

<br/>

<br/>

## ⚡️ 정리

### 🔋 Container와 ECR

<img width="500" alt="스크린샷 2024-11-20 오전 3 39 02" src="https://github.com/user-attachments/assets/0803f16b-adbe-4c4d-a254-15857d657c92">

- 애플리케이션과 필요한 의존성을 패키징 하는 기술
- 컨테이너 기술을 활용한 도커를 통해 컨테이너를 구동
- AWS 클라우드 환경에서 실행하기 위해 ECR 서비스와 통합
- 하나의 인스턴스에 모든 서비스가 실행되고 있다.


### 🔋 AWS로 배포과정

<img width="1000" alt="스크린샷 2024-11-20 오전 3 39 29" src="https://github.com/user-attachments/assets/1fe1ef57-6a60-4dd6-9804-a190ee60c628">

- 로컬에서 개발
- 개발한 프로그램을 Dockerfile로 빌드
- 빌드된 DockerImage를 ECR로 푸시
- 푸시하기 전에 ECR이 레포지토리를 생성해줬고, 로컬에서 푸시하기 위해서는 CLI를 통해 권한이 있는 유저에 로그인이 되어있어야 했습니다.
- 푸시된 DockerImage를 EC2에서 Pull받고 실행. 이때도 마찬가지로 EC2에도 권한이 필요했었습니다.

### 🔋 개선이 필요한 부분
- 현재 구조는 EC2만 배포되어 있어 모노래식 아키텍처 때와 마찬가지로 데이터베이스나 파일 저장소가 통합되어 있지 않습니다.
- 그리고 ALB나 오토 스케일링 그룹도 없어 수평적인 확장이 되지 않는 상태라 가용성도 좋지 못합니다. 
- RDS, S3, CloudFront 통합
