# 💡 MSA를 AWS 클라우드로 이전 2 - RDS, S3, CloudFront


## ⚡️ MSA와 RDS

<img width="500" alt="스크린샷 2024-11-21 오전 1 09 29" src="https://github.com/user-attachments/assets/e25d1d6d-6b62-4813-b2b6-ff7a53c9c48b">

- 모노리식 아키텍쳐와의 차이점은 각 서비스에서 `RDS`의 인증 정보를 통해 개별 데이터베이스에 접근
- 장애와 확장성에 좋은 구조
- 여기서는 비용이 많이 발생할 수 있으므로 서비스당 개별 RDS 가 아닌 하나의 RDS 에 여러개의 데이터베이스를 사용한다.

## ⚡️ MSA와 S3, Cloudfront

<img width="500" alt="스크린샷 2024-11-21 오전 1 09 43" src="https://github.com/user-attachments/assets/8e255c6b-cfc6-421c-968e-4521925d316b">

- 모든 서비스가 하나의 S3 버킷과 하나의 `Cloudfront` 배포를 사용
- `S3`와 `Cloudfront`는 자동으로 확장

<br/>

<br/>

<br/>

## ⚡️ RDS 생성

이전에 생성했던 `bastion-host-instance` 에 졉속(connect)해서 `postgreSQL 클라이언트`를 설치한다.

### 🔋 postgreSQL 클라이언트 설치 명령어

```shell
sudo dnf install postgresql15
```

### 🔋 postgreSQL 접속 명령어

```shell
psql --host=[endpoint] --port=5432 --dbname=[db이름] --username=[username]
```


### 🔋 데이터 베이스 생성 명령어

```shell
create database [db이름];
```

- ex) `create database product_database;`

<br/>

<br/>

<br/>

## ⚡️ 생성한 RDS 서버에 애플리케이션 서버의 각각의 서비스를 연결

RDS 의 보안그룹에 EC2 인스턴스가 사용할 보안그룹을 먼저 허용해준다.

**public EC2 보안그룹에 대해 Postgres 기본 port 5432 를 허용해준다.**

<img width="1000" alt="스크린샷 2024-11-26 오후 4 38 59" src="https://github.com/user-attachments/assets/18c1d025-ee3d-4925-b8af-60d68b8bec2b">

---

<img width="500" alt="스크린샷 2024-11-26 오후 4 42 35" src="https://github.com/user-attachments/assets/49fea883-fcd0-4523-8b63-ce62c3684ed3">

- 아직 ALB 를 생성하지 않았기 때문에 Public-Subnet 에 인스턴스를 배치한다.
- SSH, HTTP port 가 열려 있는 보안그룹을 선택해준다.


<img width="500" alt="스크린샷 2024-11-26 오후 4 44 11" src="https://github.com/user-attachments/assets/078ebe7d-adf4-4f47-8706-28bd4b1facbc">

- **ECR 에서 image 를 pull 받아야 하기 때문에 `Instance Profile` 도 설정해준다.**

### 🔋 user-service User-Data

```shell
#!/bin/bash

# ecr 로그인
[ecr 로그인 명령어]

# 컨테이너 실행
docker run -p 80:8002 --name user-service-container \
-e "spring.datasource.url=jdbc:postgresql://[rds-endpoint]:5432/[user-database-name]" \
-e "spring.datasource.username=[username]" \
-e "spring.datasource.password=[password]" \
-e "spring.datasource.driverClassName=org.postgresql.Driver" \
-t [ecr-user-repository-image-uri]
```

- 먼저 `ECR` 에 로그인한다.
- `docker container` 실행 명령어
  - port 를 `host` 의 `80 port` 와 `container` 의 `8002 port` 를 **포워딩한다.**
    - **외부에서 80 port 로 접근해서 container(여기서는 user-service) 의 8002 port 로 접속하기 위해서이다.**
- 여기서 image pull 받는 명령어가 없는 이유는 docker run 명령어를 실행할 때 실행할 image 가 없으면 먼저 pull 을 받아오는 작업을 진행하기 때문이다.
- **그래서, 이 명령어에서는 pull 을 받아오는 작업도 같이 수행하게 된다.**


### 🔋 product-service User-Data

```shell
#!/bin/bash

# ecr 로그인
[ecr 로그인 명령어]

# 컨테이너 실행
docker run -p 80:8001 --name product-service-container \
-e "spring.datasource.url=jdbc:postgresql://[rds-endpoint]:5432/[product-database-name]" \
-e "spring.datasource.username=[username]" \
-e "spring.datasource.password=[password]" \
-e "spring.datasource.driverClassName=org.postgresql.Driver" \
-t [ecr-product-repository-image-uri]
```


### 🔋 cart-service User-Data

```shell
#!/bin/bash

# ecr 로그인
[ecr 로그인 명령어]

# 컨테이너 실행
docker run -p 80:8000 --name cart-service-container \
-e "msa.product-service.url=[product-service-instance-private-ip]" \
-e "msa.user-service.url=[user-service-instance-private-ip]" \
-e "spring.datasource.url=jdbc:postgresql://[rds-endpoint]:5432/[cart-database-name]" \
-e "spring.datasource.username=[username]" \
-e "spring.datasource.password=[password]" \
-e "spring.datasource.driverClassName=org.postgresql.Driver" \
-t [ecr-cart-repository-image-uri]
```

- 다른 서비스의 호스트 이름을 입력해줘야 합니다.
- 이때 Public IP를 입력하거나 Private IP를 입력할 수 있는데 일반적으로 같은 VPC 내에 존재하는 인스턴스 간의 통신은 Private IP로 하는 것이 비용이나 성능적으로 효율적입니다.
- 이유는 퍼블릭 IP로 요청을 하게 되면 AWS 네트워크를 벗어나서 다시 들어오게 되고 프라이빗 IP로 요청을 보내면 AWS 네트워크 내에서 통신이 이루어집니다.
- 추가적으로 트래픽이 웹으로 나가지 않기 때문에 보완적으로 더 효율적이게 됩니다.

--- 

### 🔋 postman 으로 테스트

<img width="1000" alt="스크린샷 2024-11-26 오후 5 22 01" src="https://github.com/user-attachments/assets/554fc0ea-8752-487c-b8fd-5589a5714197">

- 먼저, `EC2` 에 연결된 `Security-Group(sg)` 에 `80 port` 를 열어준다.
- 그 다음에 EC2 의 public ip:80 으로 접속해서 테스트를 진행한다.

---

<br/>

<br/>

<br/>

## ⚡️ S3, CloudFront 통합 (실습)

### 🔋 S3+Cloudfront 통합 user-service EC2 instance User-Data

```shell
#!/bin/bash

# ecr 로그인
aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin [ecr-url]

# 컨테이너 실행
docker run -p 80:8002 --name user-service-container \
-e "spring.datasource.url=jdbc:postgresql://[rds-endpoint]/[user-database-name]" \
-e "spring.datasource.username=[username]" \
-e "spring.datasource.password=[password]" \
-e "spring.datasource.driverClassName=org.postgresql.Driver" \
-e "cloud.aws.s3.bucket=[s3-bucket-name]" \
-e "cloud.aws.region.static=ap-northeast-2" \
-e "cloud.aws.cloud-front.domain-name=[cloud-front-dns]" \
-e "cloud.aws.cloud-front.distribution-id=[distributionId]" \
-t [ecr-user-repository-image-uri]
```

<br/>

<br/>

<br/>

## ⚡️ 정리

<img width="600" alt="스크린샷 2024-11-26 오후 6 15 16" src="https://github.com/user-attachments/assets/e2cb0b9d-4450-4262-b716-eb2ee33e39bb">

- RDS는 각 서비스마다 개별 데이터베이스를 사용해서 연결했습니다.
- 이때 장바구니 데이터베이스에는 제품 정보가 없기 때문에 제품 서비스로 서비스 간의 통신이 이루어졌다.
- S3는 RDS와 다르게 하나의 버킷에 여러 서비스들이 접근해서 사용했었습니다.
- S3는 개발자가 따로 확장을 관리할 필요가 없어 MSA와 통합에 아주 적합했습니다.
- CloudFront도 특별한 보안 정책이나 요구 조건이 없으면 하나의 배포를 사용해도 문제가 없었습니다. 
- 하나의 배포만 사용하는 것은 관리할 포인트를 줄여져 운영에는 효율적일 수 있었습니다.

### 🔋 개선이 필요한 부분

- 확장성과 가용성
- ALB와 오토스케일링 그룹
- 현재 구조에서는 트래픽 증가에 따라 확장이 되지 않을 겁니다.

