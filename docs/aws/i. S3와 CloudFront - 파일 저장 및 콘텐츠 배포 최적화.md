# 💡 S3와 CloudFront - 파일 저장 및 콘텐츠 배포 최적화

# 💡 S3(Simple Storage Service)

> 데이터를 저장할 수 있는 저장소, 사용할 때 용량을 미리 정할 필요 없이 저장하는 대로 공간을 사용할 수 있습니다.

- 주로 **파일 저장, 백업용**으로 사용
- 간단한 설정으로 정적 웹사이트 배포도 가능
- 저렴한 가격
- AWS 에서는 공식적으로 강력한 (99.99999999%) 의 내구성, 거의 손실이 없다는 의미
- 버킷과 객체

## ⚡️ S3 - 버킷

<img width="500" alt="스크린샷 2024-11-06 오전 12 35 14" src="https://github.com/user-attachments/assets/40d0516f-4776-4740-997a-7dba4418deed">

> 파일을 저장할 수 있는 공간

- VPC 외부에 위치한다.
- Bucket은 AWS의 모든 유저의 Bucket을 통틀어 **유일한 이름**을 가져야합니다.
- `Region` 에 포함됨

## ⚡️ S3 - 객체

> Bucket에 저장되는 파일 데이터를 의미

- 객체는 파일과 메타데이터를 포함
  - 파일은 이미지나 영상 데이터를 의미 
  - 메타 데이터는 키와 값 쌍으로 이루어진 파일의 컨텐츠 유형이나 생성 날짜 같은 데이터가 포함될 수 있습니다.
- 객체는 버킷 내에 고유한 키값을 가짐 
  - ex) images/cat.jpg
  - 이 이름의 슬래쉬(/)는 폴더 구조를 뜻하는게 아니라 이름에 포함되는 문자 중 하나이다.
- 폴더 구조를 가지고 있지 않음
  - 대신 위의 예시처럼 슬래쉬(/)를 이용하여 폴더 구조와 유사하게 사용 가능하다.

## ⚡️ S3 - 보안: 액세스 제어 목록(ACL) 비활성화

<img width="500" alt="스크린샷 2024-11-06 오전 12 36 56" src="https://github.com/user-attachments/assets/16a69f46-87da-4b0f-86eb-e30896a6e70e">

- ACL은 버킷과 객체에 대한 접근을 관리
  - 버킷과 객체는 각각 `액세스 제어 목록(ACL)`이 연결되어 있는데, 이를 통해 다른 AWS 계정이나 그룹에 접근 권한을 부여해줄 수 있습니다.
- S3에 객체를 읽거나 리스트 요청이 오면 `액세스 제어 목록(ACL)`을 확인해서 권한에 따른 응답을 한다.
- AWS에서는 `액세스 제어 목록(ACL)`으로 권한 관리를 하지 않고 비활성화하는 것을 권장합니다.

`액세스 제어 목록(ACL)`을 비활성화하면 버킷을 생성한 계정에만 권한이 있게 되는데 **이런 독점관리를 권장하고 있습니다.**

**버킷과 객체는 액세스 제어 목록을 각각 설정 가능**

## ⚡️ S3 - 보안: 올바른 버킷 정책 

<img width="500" alt="스크린샷 2024-11-06 오전 12 37 32" src="https://github.com/user-attachments/assets/8c9429a9-e35e-4d6e-b3de-26244553a9b5">

> 버킷 정책: 버킷의 객체에 대한 권한을 JSON으로 작성하는 방법

- 여기서 가장 피해야 할 작성 방법은 많이 사용하는 방법 중의 하나인데, `Principal`의 **와일드 카드**를 사용하지 않는 것입니다.
- 버킷 정책의 `Principal`에 **와일드 카드**를 입력하면 `Action`에 적힌 작업이 누구에게나 허용된다는 의미


## ⚡️ S3 - 보안: Public Access 차단 사용

<img width="500" alt="스크린샷 2024-11-06 오전 12 38 15" src="https://github.com/user-attachments/assets/6736933d-6181-4546-9816-9fda4acd7732">

> **`Public Access 차단`을 사용하면 이전에 봤던 액세스 제어 목록이나 버킷 정책을 설정할 때 직접적으로나 실수로 Public으로 설정되는 것을 방지 가능**

- 체크박스로 각 항목의 Public Access를 차단할 수 있습니다.
- 위의 경고는 Public Access 차단을 사용한 상태에서 버킷 정책의 Wildcard를 사용하려고 할 때 나타난다.
- **특별한 사례를 제외하고는 사용을 권장**



## ⚡️ S3 - 보안: 애플리케이션 및 AWS 서비스에 IAM 역할 사용

<img width="100" alt="스크린샷 2024-11-06 오전 12 39 28" src="https://github.com/user-attachments/assets/18ccf747-0786-43fa-b2f3-ee2e309fae78">

> **`IAM 역할`을 통해 `권한`을 부여**

- EC2나 애플리케이션에서 S3에 접근 시 인증정보(AccessKey 와 같은)를 EC2 나 애플리케이션에 직접 입력하는 것은 굉장히 위험한 일이다.
  - 이럴 때 IAM 역할을 통해 권한을 부여한다.
  - 직접적인 인증 정보를 입력할 필요가 없어 노출에 대해 안심하고 사용할 수 있다.
  - 위의 이미지의 모자가 IAM 역할을 의미하는 로고입니다.
- `IAM 역할`은 버킷 정책과 유사한 **JSON 정책**을 사용해 **일시적 권한 부여**


## ⚡️ S3 - 보안: 그 외의 보안 권장 사항

- 유휴 데이터에 대한 암호화
- 전송 중인 데이터 암호화(TLS)
- 실수로 삭제하는 것을 방지하기 위한 객체 잠금
- 보존과 복원을 위한 Bucket 버전 관리 활성화
- 다른 리전에 백업을 두는 교차리전 복제
- VPC 내부의 트래픽을 관리하기 위한 VPC 엔드포인트
- 그 외에도 보안을 위한 관리형 AWS 서비스들
  - Amazon Detective
  - IAM Access Analyzer
  - Amazon Macie
  - AWS Security Hub
  - Amazon GuardDuty

<br/>

<br/>

<br/>

## ⚡️ S3 - 무한 확장 저장소

### 🔋 Create Bucket

<img width="500" alt="스크린샷 2024-11-06 오후 9 33 56" src="https://github.com/user-attachments/assets/45613b19-a1c5-4d2c-919a-867035f29d59">

- `Region` 은 자동으로 설정되어 있다.
- `Bucket name` 은 중복되지 않도록 설정해야 한다.(ex: `mybucket-2024-11-06`)
- `Copy settings from existing bucket` 은 다른 bucket 의 설정을 복사해오는 것이다.

---

<img width="500" alt="스크린샷 2024-11-06 오후 9 35 18" src="https://github.com/user-attachments/assets/bbcc110f-6fe0-42dd-b176-fffa81b6450f">

- `ACLs disabled (recommended)`은 버킷 객체에 대한 액세스 권한을 버킷 정책을 사용해서 관리하는데 중점을 두겠다는 의미
  - 이렇게 하면 권한 관리를 중앙 집중화하여 일관되고 관리하기 쉬운 보안 구조가 됩니다.

---

<img width="500" alt="스크린샷 2024-11-06 오후 9 36 16" src="https://github.com/user-attachments/assets/39db40f7-d220-49f4-bbf6-e9502005b1b8">

`Public Access 차단`에 대한 설정입니다.
- 이 설정은 활성화 한다.

버킷의 `버전 관리` 설정입니다.
- 여기서는 비활성화 한다.


<img width="500" alt="스크린샷 2024-11-06 오후 9 39 59" src="https://github.com/user-attachments/assets/4f341664-3168-4b65-9ce9-dda638789cea">


`암호화 설정`이다.
- `SSE-S3`는 서버에서 암호화를 하고 `S3`가 키를 관리하는 방식입니다.
- `SSE-KMS`는 마찬가지로 서버에서 암호화를 하고 `KMS`라는 AWS 서비스에서 키를 관리합니다.
- `DSSE-KMS`는 가장 최신에 생긴 기능으로 이중 계층으로 암호화하는 방법입니다.
- 참고로 2023년 1월 5일부터 필수적으로 S3에 암호화를 설정하도록 정책이 바뀌었습니다.

여기서는 무료로 사용할 수 있고 관리가 가장 쉬운 SSE S3를 사용한다.

---

<img width="500" alt="스크린샷 2024-11-06 오후 9 40 07" src="https://github.com/user-attachments/assets/8c2ca3b6-da09-4a69-87ec-fcccc1ce9045">

- 추가 설정에는 `Object Lock`, 객체 잠금 설정이 있는데 여기서는 사용하지 않겠습니다.

### 🔋 일반 유저가 Bucket 에 접근 가능하게 하도록 설정하기

<img width="500" alt="스크린샷 2024-11-06 오후 9 50 23" src="https://github.com/user-attachments/assets/1b4f9e1d-c4a3-40d8-9ce8-a36fb07c09b6">

- Bucket 에 들어가서 Drag & Drop 으로 파일을 간단하게 업로드할 수 있다.

<img width="500" alt="스크린샷 2024-11-06 오후 9 52 54" src="https://github.com/user-attachments/assets/93363117-2a45-4478-b9ec-b427b73960a8">

- 파일을 업로드 후 상세화면에 들어가면 오른쪽 위에 `open` 버튼을 확인할 수 있다.
- 클릭하면 파일이 열린다.

**여기서 의문이 생길 수 있다!**
- 분명히 Public Access에 대한 차단을 설정했는데 URL을 통해 객체에 접근이 가능하다.
- Bucket 소유 계정이라 된다고 생각하실 수도 있는데 이렇게 시크릿 모드에서도 조회가 가능하다.

**이유는 URL 뒤쪽에 보면 난수로 된 토큰 값이 입력되어 있다.**
- 이렇게 `open`으로 조회한 URL은 프리사인드 URL로 사전에 서명이 된 URL입니다. 
- **권한이 없어도 누구나 접근할 수 있는 `임시 URL`입니다.**

`Object overview > Object URL`
- **`Object URL` 을 클릭하면 접근이 거부되는 것을 확인할 수 있다.**
- open 과 다른 점은 URL 에 토큰 값이 없다.

---

> 권한이 없는 일반 유저도 Bucket 에 접근할 수 있도록 정책을 수정

`Bucket > Permissions > Bucket Policy > Policy Generator`

<img width="500" alt="스크린샷 2024-11-06 오후 10 04 09" src="https://github.com/user-attachments/assets/685916ba-888b-4a1f-9b78-7f331f8654fd">

- `Policy Type`: S3 Bucket Policy
- `Principal`: 와일드카드(`*`)

<img width="150" alt="스크린샷 2024-11-06 오후 10 07 00" src="https://github.com/user-attachments/assets/9aab066b-b401-4f09-a915-0363fae0ff78">

- 정책 구문 중 `Resource`의 버킷명 마지막에 슬래시 별표를 입력해주겠습니다.
  - `arn:aws:s3:::mybucket-2024-11-06/*`
  - 이유는 그냥 버킷 이름만 입력하게 되면 액션에 대한 대상은 버킷이 됩니다.
  - 하지만 GetObjectAction은 대상이 객체여야 하고, 이때 슬래시 별표을 입력해주면 해당 버킷의 모든 객체가 대상이 됩니다.

---

`Public Access 차단` 설정이 활성화되어 있는데, `Principal`에 `와일드 카드(*)`가 입력되어 있기 때문입니다.
- **해결방법 1: 와일드 카드를 사용하지 않는다.**
  - 하지만, 여기서는 와일드 카드를 사용하지 않으면 일반 유저가 객체에 접근할 수 없다.
- **해결방법 2: Public Access 제어를 비활성화 한다.**

`Bucket > Permissions > Block public access (bucket settings) > Edit` 에 들어가서 설정 해제를 해주면 된다.

**이제 모든 유저가 파일에 접근이 가능한 상태가 되었습니다.** 
- 이 설정은 객체 접근 테스트를 위한 임시 설정이라 이후에는 보안을 강화하기 위해 수정할 필요가 있습니다. 
- 운영 환경에서는 이렇게 사용하지 않는다.

<br/>

<br/>

<br/>


## ⚡️ S3에 접근할 수 있는 권한이 있는 IAM 역할을 만들어 EC2에 권한을 부여

최종적으로 구팡 서버를 통해 S3의 유저 이미지를 업로드해보는 것 까지 해본다.

---

`IAM > Roles`
- 생성한 적이 없는데도 Roles 가 생성되어 있다. 
- 이유는 이전에 생성했던 `RDS Proxy`나 `Auto-scaling` 같은 서비스들은 생성할 때 필요한 권한을 AWS에서 자체적으로 생성핳 수 있기 때문이다.

### 🔋 Create Role

<img width="500" alt="스크린샷 2024-11-06 오후 10 35 03" src="https://github.com/user-attachments/assets/34e221c0-cb38-48dd-859e-aafdd8094634">

`Select trusted entity`

첫번째로, 역할의 두 가지 구성 중 하나인 `신뢰 정책(Trusted Entity)`을 설정할 수 있습니다.
- `신뢰 정책`은 권한을 부여해줄 리소스나 사용자를 지정하는 구성입니다.
  - Trusted Entity Type은 부여할 대상의 타입을 선택할 수 있습니다. 
  - AWS 서비스를 선택해주고 아래에는 사용 사례를 EC2로 선택해주겠습니다.

**이렇게 설정하면, EC2에 권한을 부여하도록 정의된다.**

---

<img width="500" alt="스크린샷 2024-11-06 오후 10 41 25" src="https://github.com/user-attachments/assets/772d5001-cb1a-40d6-bf87-fff61a5f377e">

`Add Permissions`

두번째엔, `권한 정책(Permissions policy)`을 선택할 수 있다.
- 이 정책들은 AWS 나 개발자가 미리 만들어놓은 정책들이다.
- 역할을 생성하는 과정에서는 AWS 나 개발자가 미리 만들어놓은 정책만 선택할 수 있지만, 이후에는 커스텀해서 정책을 부여해줄 수도 있다.
  - 여기서는 `AmazonS3FullAccess`를 선택한다.
  - `AmazonS3FullAccess`: s3 에 대한 모든 권한을 대상에게 부여해줄 수 있다.
  - 운영환경에서는 필요한 권한만 커스텀해서 만들어서 부여하는 것을 권장하고, AWS 에서도 최소한의 필요한 권한만 부여하는 최소 권한 원칙을 권장한다.

`Role name: goopangEc2Profile`

> **위의 2가지 구성을 합쳐서 EC2 가 S3 에 접근할 수 있는 권한을 부여할 수 있는 역할이 된다.**

---

### 🔋 modify launch template

<img width="500" alt="스크린샷 2024-11-06 오후 10 49 24" src="https://github.com/user-attachments/assets/2de90fb5-bbc9-43b0-bdea-2e7cc65b8198">

`Advanced details > IAM instance profile`

**이 항목들엔 IAM 역할을 설정할 때 신뢰 정책(Trusted policy)에 EC2 가 포함되어 있는 역할들만 표시된다.**

---

`User Data 수정`

```shell
#!/bin/bash

#Git 레포지토리 클론 및 브랜치로 이동 
git clone -b 5_1_monolithic_s3 https://github.com/burger-2023/aws-operation-prac.git

#폴더 이동 
cd aws-operation-prac

# Gradle을 이용한 Spring Boot 프로젝트 빌드 후 빌드된 Spring Boot 애플리케이션 실행 
./gradlew build 
java -jar build/libs/aws-msa-monolithic-prac-0.1.jar \
--spring.datasource.url=jdbc:postgresql://[RDS엔드포인트]:[DB연결포트]/[DB이름] \
--spring.datasource.username=[RDS계정이름] \
--spring.datasource.password=[비밀번호] \
--cloud.aws.s3.bucket=[버킷명] \
--cloud.aws.region.static=[버킷이생성된리전]
```

<img width="500" alt="스크린샷 2024-11-07 오전 12 19 09" src="https://github.com/user-attachments/assets/c26ff670-fd74-4d49-a025-f447b4ba70f8">

<img width="500" alt="스크린샷 2024-11-06 오후 11 19 14" src="https://github.com/user-attachments/assets/1a1a38f5-66f6-45c1-9c81-1efda3c9ba21">

**등록했던 이미지를 다시 삭제했더니 폴더들도 사라진 것을 확인할 수 있다.**

- S3는 폴더 구조가 아닌 펜 이상 폴더 구조처럼 사용하고 있는 것입니다.
- 슬래시는 객체의 키의 일부분이기 때문에 빈 폴더로는 존재할 수가 없습니다.
- 그래서 폴더 타입의 내부의 객체가 존재하지 않으면 해당 폴더도 삭제되기 때문에 유저 폴더까지 모두 삭제되었습니다.

<br/>

<br/>

<br/>

## ⚡️ S3 버젼 관리

> **객체의 `버전`을 관리하는 기능**

- `Bucket 단위`로 활성화
- 활성화 된 상태에서 객체를 업로드하면 같은 키를 기준으로 버젼이 할당된다.
- 키 단위로 처음 업로드된 객체가 버전1, 같은 키로 업로드된 객체가 버전2, 버젼3, 버젼4... 가 된다.

### 🔋 S3 버전 관리 장점

- `데이터 보호` - 실수로 삭제하거나 덮어쓸 때 **이전 버전을 보존하거나 복원 가능**
- `버전 관리` - 각 버젼은 버전별로 고유한 버전 ID를 가지고, 기존 개별 객체처럼 삭제나 조회 등 관리 가능
- `Delete Marker` - 객체를 삭제해도 해당 객체는 삭제되지 않고, **삭제되었다는 마커만 추가**
  - 객체가 삭제되었다는 표시만 하고 객체의 버젼들은 유지가 된다.
  - 이때 실제로 삭제다 되지 않아서 비용적인 부분에서 비효율적일 수 있지만, 버전이 보존되는 장점
  - `S3 LifeCycle` 기능을 통해 위의 단점을 보완 가능

### 🔋 S3 버전 관리 - Delete Marker

<img width="500" alt="스크린샷 2024-11-07 오전 12 23 01" src="https://github.com/user-attachments/assets/2e56970c-2056-4108-8914-96e6f676bbf2">

<img width="500" alt="스크린샷 2024-11-07 오전 12 23 28" src="https://github.com/user-attachments/assets/d0626ea9-9bbf-4c99-9a04-c80fd563eac7">

- 버전 관리가 활성화된 경우 **단순 삭제 시 Delete Marker 생성**, 객체의 기존 버전들은 유지, 최신 버젼은 Delete Marker 가 된다.

**객체를 진짜로 삭제하려면?**
- 단순 Delete요청이 아닌 **Delete 요청에 삭제하려는 `버전 ID`을 지정해야 한다.**
- Delete Marker를 제외한 모든 버전이 삭제 되면, 해당 객체는 만료된 객체 삭제 마커(expired object delete marker)가 된다.
- 이렇게만 보면 삭제에 대한 관리가 굉장히 번거로울 수 있다.
- 이 부분에 대한 것도 `S3 LifeCycle` 기능을 통해 해결 가능하다.

### 🔋 S3 Bucket 버전 관리 활성화

<img width="500" alt="스크린샷 2024-11-07 오전 2 48 00" src="https://github.com/user-attachments/assets/158cfc69-85b5-456d-a562-035e860a7761">

<img width="500" alt="스크린샷 2024-11-07 오전 2 52 27" src="https://github.com/user-attachments/assets/b54c2ea5-e2a9-4d5d-9ad0-145fc73c07dc">

`Buckets > Properties > Bucket Versioning`
- `Enable` 만 체크해주면 끝이다.

<img width="500" alt="스크린샷 2024-11-07 오전 2 54 07" src="https://github.com/user-attachments/assets/21e7bce6-beb0-43e7-a910-bb350f2e9f90">

`Show versions`를 눌러보면 `Version ID` 가 `null` 로 나온다. 

- 이유는 버전 관리를 활성화하지 않은 상태에서 생성된 객체들은 버전 관리가 활성화되면, 기존 객체들의 버전을 NULL로 설정하고 따로 ID를 부여하지 않기 때문이다.

<img width="500" alt="스크린샷 2024-11-07 오전 2 57 58" src="https://github.com/user-attachments/assets/16f62562-5bd5-48b2-8efb-5b5d2d257353">

- 같은 Key 값을 가진 객체를 업로드하면 Version ID 가 생성된 같은 Key 값의 객체를 확인할 수 있다.
- 이 객체는 조회하게 되면 최근에 올린 객체가 조회된다. 물론, 이전 객체를 조회하는 방법도 있다.
  - `Object URL` 의 마지막에 `versionId` 로 이전 버젼을 조회할 수 있다.

<img width="500" alt="스크린샷 2024-11-07 오전 3 02 02" src="https://github.com/user-attachments/assets/6b3fc420-7e9d-4c6f-b0bb-37032b27b16f">

- 객체를 선택해서 삭제하면 삭제가 된 것 처럼 객체가 안보이지만, `Show version`를 활성화시키면 삭제한 객체가 보인다.
  - 가장 최신 버젼의 `Delete marker` 가 생성된 것을 볼 수 있다.
- 버젼이 활성화된 상태에서 이렇게 일반 삭제를 하면 Delete Marker 가 최신 버젼으로 생성된다. 
- 특정 버젼을 완전히 삭제하려면, 
  - 콘솔: Show versions 를 활성화하고, 삭제하려는 버젼을 선택해서 Delete 버튼을 누르면 된다.
  - 람다나 애플리케이션: 삭제하려는 객체의 versionId 를 같이 넣어주면 된다.
- Delete marker 객체도 삭제할 수 있다.
- Delete marker 도 일반 객체와 버져닝에 대해 똑같이 관리된다.
  - Delete marker 객체를 삭제하면, Delete marker 바로 이전 버젼의 객체를 조회할 수 있도록 복구된다.

<img width="500" alt="스크린샷 2024-11-07 오전 3 11 19" src="https://github.com/user-attachments/assets/ed08a3d7-fdeb-4ec3-8c6b-6a4c4b4c387d">

위의 이미지 객체는 Delete 마커를 제외한 모든 버전이 사라졌고 정말 의미없는 상태가 된다.
- `Expired Object Delete Marker`: Delete 마커를 제외한 모든 버전이 사라졌고 정말 의미없는 상태
- **이 상태의 객체 사이즈는 0바이트로 비용에 영향을 주지 않겠지만 콘솔에서 직접 조작하는 경우에 `운영상의 불편함`을 줄 수 있다.**
- 이런 경우에는 `S3 LifeCycle`을 사용해서 문제를 해결할 수 있다.

<br/>

<br/>

<br/>

## ⚡️ S3 Storage 클래스와 LifeCycle

### 🔋 S3의 스토리지 클래스

> **객체는 무조건 하나의 스토리지 클래스에 포함**

- 서비스의 여러 요구사항에 맞게 나누어진 객체의 계층
  - 자주 접근하는 데이터, 장기 보관용, 저렴한 비용 등 요구사항을 적절히 고려해서 스토리지 클래스를 선택할 수 있다.
- 실질적으로 사용되는 클래스는 7가지(1가지가 더 있지만 AWS 자체적으로 사용을 권장하지 않는다.)

### 🔋 Standard 클래스

<img width="750" alt="스크린샷 2024-11-07 오전 3 22 07" src="https://github.com/user-attachments/assets/c4575702-35c7-4638-8b91-ada404c6e02b">

> **자주 액세스하는 객체를 위한 스토리지 클래스**

- 성능에 민감하거나 자주 액세스 되는 데이터
- 사용 사례: 웹사이트 컨텐츠, 모바일 게임, 앱 데이터와 같이 빈번한 접근이 필요한 곳에 사용된다.
- 1TB 비용: 1,024GB * $0.025 = $25.6

---

### 🔋 Intelligent-Tiering 클래스

> **별다른 작업 없이 가장 비용 효율적인 액세스 계층으로 자동으로 데이터를 이동시키는 스토리지 클래스**

- **자체적으로 액세스 패턴을 모니터링해서 일정 기간 액세스 되지 않는 객체를 더 저렴한 계층으로 이동시킨다.**
- 이 클래스를 사용하게 되면 약간의 모니터링과 자동화 요금이 있습니다.
- **주로 액세스 패턴을 알 수 없거나 변화하는 데이터를 위한 클래스**
- 기본적으로 액세스 패턴에 따라` 3가지 액세스 계층`을 이동시키고
  - `Frequent Access, Infrequent Access, Archive Instance Access`
- `설정을 하면 2가지 계층`을 더 사용할 수 있다. 
  - 아카이브 액세스 계층 - `Archive Access, Deep Archive Access`
  - 아카이브 계층에 있는 객체는 몇 분에서 몇 시간 이상 검색 시간이 걸리므로 잘 접근하지 않고 오랜 기간 저렴하게 저장하려는 경우에 요구에 맞게 적절히 추가하는 것이 중요합니다.
  - 만약 스토리지 클래스 관리 부분에 큰 운영 시간을 투자하고 싶지 않다면 적절한 선택이 될 것 같습니다.


### 🔋 Intelligent-Tiering 클래스 저장 비용

<img width="750" alt="스크린샷 2024-11-07 오전 3 22 17" src="https://github.com/user-attachments/assets/5763a5de-e5f3-4740-830c-b69baffde362">

- Frequent Access: Standard 클래스와 동일한 비용
- Infrequent Access: 1TB당 $14.1, 45%절감
- Archive Instant Access: 1TB당 $5.1, 80%절감
- Archive Access: 1TB당 $4.6, 82%절감
- Deep Archive Access: 1TB당 $2.1, 92%절감
- 객체 1,000개당 $0.0025의 모니터링 및 자동화 비용
  - 주의할 점은 인텔리전트 티어링 클래스는 모니터링과 자동화 비용으로 객체 1000개당 0.0025달러 추가 비용이 있습니다.

---


### 🔋 Standard-IA, One Zone-IA 클래스

> **오래 보관되고 자주 액세스되지 않는 데이터를 위한 클래스**

- 이름의 IA는 Infrequent Access (빈번하지 않은 액세스)
- `Standard-IA`: 여러 개의 가용영역에 중복되게 저장해서 한 가용영역에 문제가 생겼을 때 복원력이 있다는 의미이다.
- `One Zone-IA`: 한 가용영역에만 저장되어서 더 저렴하지만, 가용영역에 문제가 생기면 복원력이 없다. 
- 사용 사례: 백업


### 🔋 Standard-IA, One Zone-IA 클래스 저장 비용

<img width="750" alt="스크린샷 2024-11-07 오전 3 22 26" src="https://github.com/user-attachments/assets/e2552fdc-ae54-46f6-bcca-a06921c3b909">

- Standard-IA: 1TB당 $14.1, 45%절감
- One Zone-IA: 1TB당 $11.3, 56%절감

---

### 🔋 Glacier 클래스

> **저렴한 비용으로 데이터 아카이브를 위한 클래스**

- **굉장히 저비용이다.**
- **데이터를 장기간 보관하는 아카이브를 위한 클래스**
- Glacier Instant Retrieval - 이 클래스는 거의 액세스 되지 않고 밀리초 단위로 검색되어야 하는 데이터를 아카이브하는데 사용합니다. 스탠다드 IA와 같은 성능으로 액세스 비용은 높지만 저장 비용은 더 저렴합니다.
- Glacier Flexible Retrieval - 분 단위로 데이터를 검색해야 하는 아카이브에 사용됩니다. 저장되는 데이터는 최소 90일 저장되어야 하고 약 1분에서 5분 이내에 액세스할 수 있습니다.
- Glacier Deep Archive - 딥 아카이브 클래스는 거의 액세스가 되지 않고 보관만 할 때 사용합니다. 최소 저장기간은 180일이고 기본 검색시간은 12시간입니다.
- Flexible Retrieval, Deep Archive 클래스는 보관이 되면 보관을 한 후에 접근이 가능합니다.(접근 시 복원 필요)

### 🔋 Glacier 클래스 저장 비용

<img width="750" alt="스크린샷 2024-11-07 오전 3 22 36" src="https://github.com/user-attachments/assets/39a5788b-35c5-47c5-8c23-29cad3fa06d6">

- Glacier Instant Retrieval: 1TB당 $5.1, 80%절감
- Glacier Flexible Retrieval: 1TB당 $4.6, 82%절감
- Glacier Deep Archive: 1TB당 $2.05, 92%절감

---

### 🔋 Express One Zone 클래스

- 2023년 12월 1일 출시
- Standard 클래스에 비해 데이터 접근 속도 10배
- 비용 50% 절감

**이렇게 할 수 있는 이유는 기존의 S3 는 VPC 외부에 배치되었지만, Express One Zone 클래스는 VPC 내부 가용영역에 버킷을 배치한다.**
- **기존 버킷과는 다르게 `내부적으로 폴더 구조`를 가진** `Directory Bucket`을 사용
- EC2 같은 자원에 더 가까이 배치하여 성능을 최적화
- 사용 사례: 머신 러닝, 실시간 데이터 분석 작업 등에 사용
- 쿠팡 같은 e-commerce 서비스는 적합한 사례가 아니다.


### 🔋 S3 스토리지 클래스 비교

<img width="1000" alt="스크린샷 2024-11-07 오전 3 22 49" src="https://github.com/user-attachments/assets/26e19a97-b237-426d-b601-2f552eb50f08">

- 이유는 내구성이 다른 클래스들에 비해 매우 낮습니다.
- 내구성 99.99%는 1만 개의 객체를 1년간 저장하면 1개의 객체가 손실될 수 있다는 치명적일 수도 있는 내구성입니다.
- 이 내구성은 다른 클래스와 약 1만 배가 차이나는 수치입니다.


### 🔋 스토리지 Lifecycle

아마 스토리지 클래스의 종류가 굉장히 많아서 직접 클래스를 이동시키는 건 거의 불가능한 일이다. 

> **그래서 S3 에는 `LifeCycle` 을 통해 `클래스를 관리`할 수 있다.**

- 객체의 수명 주기(Lifecycle)를 통해 관리
1. `전환 작업`: **객체가 클래스를 전환하는 시기를 정의해서 관리**
   - EX) 예를 들면 생성 후 30일이 지나면 Standard IA로 전환하고 90일이 지나면 Glacier Instant Retrieval 클래스로 전환되도록 할 수 있습니다.
2. `만료 작업`: **객체가 만료되는 시기를 정해 자동으로 삭제**
   - 이 기능을 통해 이전 강의에서 알아봤던 Expired Object Delete Marker 문제를 해결할 수 있다.
- 가장 비싼 클래스와 가장 저렴한 클래스는 약 10배 이상의 비용 차이가 난다.
- S3의 비용 절감을 위해 꼭 필요한 기능

<br/>

## ⚡️ 객체의 클래스 변경 및 Lifecycle 설정으로 클래스 변경을 자동화

### 🔋 Storage class 수동 변경

<img width="500" alt="스크린샷 2024-11-07 오후 3 50 05" src="https://github.com/user-attachments/assets/9fa1eec5-7f5b-4ff5-b15e-c65e48040adf">

기본적으로 아무 설정 없이 객체를 업로드하면 `스탠다드 클래스`가 설정됩니다.

<img width="500" alt="스크린샷 2024-11-07 오후 3 53 20" src="https://github.com/user-attachments/assets/acaed8de-24ad-442e-96c9-90d1cc5ea93a">

AWS 에서 지원하는 모든 클래스들을 볼 수 있다.
- `Designed for`: 해당 티어를 사용할 때 AWS 에서 추천하는 접근 빈도가 나와있다.

<img width="500" alt="스크린샷 2024-11-07 오후 3 54 53" src="https://github.com/user-attachments/assets/96e72cae-5f5c-4a79-b9ac-d0e347895642">

- Glacier Deep Archive 클래스는 장기 데이터 보관을 위해 설계되어서 즉각적인 접근을 제공하지 않는다. 
- 그래서 조회를 하려면 복원 작업이 선행되어야 한다.

<img width="500" alt="스크린샷 2024-11-07 오후 3 59 04" src="https://github.com/user-attachments/assets/9cb69db0-aae0-4ef5-96be-2e37e49c4a46">

- 복원으로 생성될 객체 복제본을 사용할 수 있는 기간을 설정할 수 있다. 
  - 이 기간이 지나면 복제된 객체는 삭제가 된다. 
- `Retrieval tier`는 복원 속도를 설정할 수 있습니다.
  - 이 클래스(`Glacier Deep Archive 클래스`)는 48시간, 12시간 중에 선택할 수 있습니다. 
  - 빠른 복원일수록 비용이 더 많이 부과됩니다. 
- 복원이 된 복제본은 `Standard 클래스`로 복원이 됩니다.

> 하지만 이렇게 수동으로 클래스를 이동시켜주는건 굉장히 비효율적이다.

<br/>

### 🔋 Create Lifecycle rule

`Buckets > Management > Lifecycle rules`

<img width="500" alt="스크린샷 2024-11-07 오후 4 15 17" src="https://github.com/user-attachments/assets/f2d94f60-316e-433a-bbe9-89f590cb625a">

<img width="500" alt="스크린샷 2024-11-07 오후 4 33 39" src="https://github.com/user-attachments/assets/3cd39b03-5e85-4a2e-8eba-f35cba625455">

**`Rule`의 적용 범위를 지정**
1. Bucket 의 모든 객체를 지정할 수도 있고 
2. 필터를 통해 조건에 해당하는 객체로 제한할 수 있다.
   - 필터를 선택하면 `prefix`로 이름에 포함된 글자를 지정하거나 객체의 태그 또는 객체의 용량으로 지정할 수도 있다.

전체 객체에 적용을 선택하면 경고문이 뜬다. /
전체 객체에 적용된다는 내용을 이해했냐고 물어보는 내용이다. 체크해주고 넘어가면 된다.

---

<img width="500" alt="스크린샷 2024-11-07 오후 4 34 21" src="https://github.com/user-attachments/assets/549738cb-7241-4eaf-beda-7bf0208a9bd7">

<img width="500" alt="스크린샷 2024-11-07 오후 4 34 46" src="https://github.com/user-attachments/assets/622d213a-1e81-4e15-a923-33d112719178">

`Transition current versions of objects between storage classes`
- 현재 버전에 대해 스토리지 클래스를 이동시키는 규칙을 설정할 수 있다.
- 왼쪽에는 이동할 스토리지 클래스를 선택하고 오른쪽은 조건인 객체가 생성되고 초과된 일수를 입력합니다.

`Transition noncurrent versions of objects between storage classes`
- 버킷의 버전 관리 옵션을 활성화했을 때 최신 버전이 아닌 이전 버전들에 대해 클래스 이동 규칙을 지정할 수 있습니다.
- `Number of newer versions to retain - Optional`: 규칙에 적용되지 않고 보존시킬 버전의 개수를 지정할 수 있습니다.
  - 이 옵션은 버젼 관리가 비활성화되면 아무 효과가 없어진다. 

<img width="500" alt="스크린샷 2024-11-07 오후 4 35 16" src="https://github.com/user-attachments/assets/292857ec-f681-43f0-8e1f-4544acaf00c2">

`Expire current versions of objects`
- 다음 세번째 옵션은 버전관리가 활성화되어 있을 땐 딜리트 마커를 추가하고 활성화되지 않았을 땐 객체를 삭제하는 옵션입니다.
- 삭제 기준은 생성되고 입력한 일수가 지나면 삭제가 됩니다.
- 굉장히 조심히 사용해야 할 옵션입니다.

`Permanently delete noncurrent versions of objects`
- 최신 버전이 아닌 버전에 대한 삭제를 관리합니다.
- `Days after objects become noncurrent`: 이전 버전이 된 기간을 기준으로 정할 수 있고, 
- `Number of newer versions to retain - Optional`: 보존할 버전의 수를 지정할 수도 있습니다.
- 버전 관리가 활성화되어 있지 않으면 아무런 효과가 없는 옵션입니다.

<img width="500" alt="스크린샷 2024-11-07 오후 4 38 46" src="https://github.com/user-attachments/assets/787b3b23-d2a4-4897-bc20-6fce8c4af27d">\

`Delete expired object delete markers or incomplete multipart uploads`
- 현재 버전이 Delete Marker이고 이전 버전이 모두 삭제된 객체를 삭제해줍니다.
- 버전 관리를 활성화했다면 활용하기 좋은 기능입니다.
- `Delete incomplete multipart uploads`: S3의 Multipart로 파일을 업로드할 수 있는데 이때 업로드가 완료되지 않은 객체를 일정 기간이 지나면 자동으로 삭제할 수 있습니다.
- 이 규칙은 `Expire current versions of objects` (3번 규칙)과 함께 사용하면 충돌이 일어날 수 있기 때문에 함께 사용할 수 없다.

3번째 옵션을 체크 해제하고 규칙을 생성한다.

> 하지만 이 기능은 AWS에서 공식적인 삭제 주기가 정해져 있지 않고 해외 사례에서 찾아본 결과 100TB의 객체를 삭제 후 많은 Expired Object Delete 마커가 생성되었지만 삭제까지 일주일 이상 걸렸다고 합니다. \
> AWS 문의 답변에서도 즉시 삭제를 원하는 경우 동기식으로 삭제하는 다른 프로세스를 이용해라고 하고 있습니다.

<br/>

<br/>

<br/>

# 💡 CloudFront

## ⚡️ CloudFront

<img width="500" alt="스크린샷 2024-11-07 오후 5 00 34" src="https://github.com/user-attachments/assets/1bee19d0-43c7-4619-be23-331cee014daf">

>  AWS의 **콘텐츠 전송 네트워크 서비스** (CDN)

- 정적 및 동적 웹 콘텐츠를 **사용자에게 빠르게** 전달
- 전 세계의 여러 위치한 **엣지 로케이션에 데이터 센터를 두고, 요청되는 콘텐츠를 캐시에 놓는다.**(임시 저장)
- 사용자가 요청하면, **지리적으로 가장 가까운 엣지 로케이션에서** 콘텐츠 제공 - `캐시 히트`
- 엣지 로케이션에 해당 콘텐츠가 없으면, 원본 서버에 콘텐츠를 가져와 사용자에게 전달 - `캐시 미스`
- 이때 엣지 로케이션은 원본 서버에서 가져온 콘텐츠를 캐시
  - **여기서 `원본 서버`는 `S3` 가 될 수 있다.**

## ⚡️ AWS 백본 네트워크

<img width="500" alt="스크린샷 2024-11-07 오후 5 00 59" src="https://github.com/user-attachments/assets/821eaa09-73b0-48f9-acea-80e5ca3ee1e2">

> 데이터 센터, 가용영역, 리전, 엣지 로케이션을 연결하는 **초고속 네트워크**

- **AWS의 리소스들 사이에서 데이터를 효율적으로 이동하게 한다.**
- 사용자가 어떤 위치에서 요청을 해도 **최적의 경로를 찾아** 빠르게 데이터 전송
- AWS 서비스의 전체적인 **성능과 안정성**에 매우 중요한 역할

이런 고성능 네트워크 백본에 연결된 엣지 로케이션은 2023년 11월 기준으로 전 세계에서 450개 이상 배치되어 있고 한국에도 6개의 엣지 로케이션이 배치되어 있습니다.

## ⚡️ Cloudfront 구성방법

<img width="500" alt="스크린샷 2024-11-07 오후 5 01 19" src="https://github.com/user-attachments/assets/26efc643-4d31-431c-912f-a91bb4dd0584">

1. `오리진 서버 구성` - 콘텐츠를 제공할 **`오리진 서버`를 구성** (여기서 오리진 서버는 EC2 or S3 Bucket)
2. `파일 업로드` - 제공할 **콘텐츠 업로드**
   - S3 버킷을 사용하면 클라우드 프론트만 접근이 가능하도록 제한할 수 있어 보안적으로도 통합이 우수하다.
3. `Cloudfront 배포 생성` - 콘텐츠를 가져올 오리진 서버를 지정할 배포 생성
   - `배포 구성`: Cloudfront 에서 가져온 오리진을 지정하거나 캐시 정책, 보안 같은 설정들을 하는 것
4. `도메인 이름 할당` - 배포를 생성하면 고유의 도메인 이름이 할당
   - CloudFront의 캐시 콘텐츠를 사용하려면 이 도메인 이름을 통해서 접근할 수 있다. 
   - 도메인은 커스텀 도메인도 사용할 수 있다.
5. `배포 구성 전송` - 세 번째 단계에서 생성한 배포를 전 세계의 Edge Location으로 자동으로 전송하게 됩니다.

**이런 단계를 거치게 되면 사용자에게 CloudFront를 통해 캐시된 콘텐츠를 제공할 수 있게 된다.**

## ⚡️ Cloudfront 오리진 액세스 제어

<img width="500" alt="스크린샷 2024-11-07 오후 5 01 35" src="https://github.com/user-attachments/assets/4dc9e048-7deb-47cc-89df-d3af39635f2b">

CloudFront는 S3를 Origin으로 사용하면 Origin Access 제어라는 서비스를 사용할 수 있다.

> `오리진 액세스 제어(OAC)` 는 외부에서 S3 로 접근할 수 없고, 오직 **`CloudFront`만 `S3 오리진`으로 접근을 허용**

- S3 오리진을 보호
- `OAI`의 단점을 보완하고 개선한 것이 `OAC`
- 지금은 완전히 OAC 사용을 권장
- S3 오리진의 `버킷 정책`을 수정해서 간단하게 사용 가능


## ⚡️ Cloudfront 비용

<img width="500" alt="스크린샷 2024-11-07 오후 5 01 49" src="https://github.com/user-attachments/assets/e483e7e5-9f7a-4870-9561-8d234dfee611">

- 사용량 기반 요금제
- 선불 요금이나 콘텐츠 보유량에 대한 약정이 필요하지 않음
- Cloudfront가 요청에 응답할 때 비용 발생
- 추가 기능 사용 시 요금 발생할 수 있음

<br/>

<br/>

<br/>

## ⚡️Cloudfront 도입

### 🔋 Create a Cloudfront Distribution

<img width="500" alt="스크린샷 2024-11-12 오전 1 53 12" src="https://github.com/user-attachments/assets/60761969-eb45-4f69-93e9-453d70a2f8db">

- `Origin`: 원본 데이터를 가져올 곳을 지정
- `Origin domain`: `S3 Bucket` 을 지정한다.
- `Origin Path`: `Cloudfront` 가 `Origin` 으로 요청을 보낼 때 기본으로 포함될 경로
  - 여기서는 입력하지 않는다.
- `Origin access`: OAC 사용을 설정

<img width="500" alt="스크린샷 2024-11-12 오전 2 01 44" src="https://github.com/user-attachments/assets/7e60dfa6-a09e-4c7a-98ab-3496e227cfd5">

`Default Cache Behavior`: 객체 압축 자동화나 요청 프로토컬, 요청 메서드, 액세스 제한, 캐시 정책 등 여러 설정이 가능하다. 여기서는 HTTP를 HTTPS로 리다이렉트하는 설정만 한다.
- `Redirect HTTP to HTTPS`: `CloudFront`를 통해 객체에 접근할 때, HTTP 요청을 보내게 되면 HTTPS로 리다이렉트가 됩니다.

<img width="500" alt="스크린샷 2024-11-12 오전 2 04 42" src="https://github.com/user-attachments/assets/4457ea86-d766-4009-8ac9-6519233f62df">

`Function Association`은 원본 파일이나 캐시된 컨텐츠를 전송하기 전후의 코드를 통해 조작할 수 있습니다.
- EX) 헤더를 추가하거나 다른 URL로 리디렉트하거나 토큰을 검증하는 작업을 할 수 있다.
- 지금은 설정하지 않는다.

<img width="500" alt="스크린샷 2024-11-12 오전 2 05 02" src="https://github.com/user-attachments/assets/ddc083b8-49e6-495f-8a0e-bc5af845a9db">

`WAF`는 AWS 방화벽 서비스입니다.
- 사용량에 따라 비용만 추가되고 다른 작업은 없어 아주 간단하게 사용할 수 있다.
- 지금은 사용하지 않지만 운영 환경에서는 활성화하는 것을 권장한다.


`Distribution domain name` + `/S3 Bucket 객체의 Key 값` 
- 이렇게 한번 호출이 되면 엣지 로케이션에 캐시가 되어서, 다른 사용자가 요청을 하면 더 빠르게 응답을 받을 수 있을 겁니다.

### 🔋 S3 Bucket 강화를 위한 OAC 설정 - Create control setting

<img width="500" alt="스크린샷 2024-11-12 오전 2 26 26" src="https://github.com/user-attachments/assets/ed1314d4-fd87-4036-a4e2-03e2a8d7ff2a">

- 클라우드 프론트가 원본 서버인 S3에 요청을 보낼 때 인증 정보 포함 여부에 대한 설정을 할 수 있습니다.
- `Signing behavior`: authorization 헤더의 값을 보내거나 보내지 않거나 또는 보내고 덮어쓰지 않게 할 수 있는데 보내는 동작 설정(`Sign requests`)을 권장하고 있고 그 외의 설정은 특수한 경우에만 사용합니다.

<img width="500" alt="스크린샷 2024-11-12 오전 2 29 38" src="https://github.com/user-attachments/assets/a90f7040-11f9-4e41-9955-fc5ef3780057">

`Distributions > Edit origin`
- 이전에 설정했던 `Public` 을 `Origin access control settings` 로 변경한다.
- 금방 생성한 `OAC` 로 설정한다.
- `Bucket 설정`도 이에 맞게 바꿔줘야 하는데, `Copy policy` 로 복사해가면 된다.


`Edit Bucket Policy` 를 통해 복사한 Bucket 정책으로 수정해준다. 
- 정책을 간단하게 살펴보면 생성했던 CloudFront Distribution 의 S3 GetObject 동작을 Bucket 의 모든 객체에 대해 허용하는 내용입니다.

이렇게 설정하면 Bucket 의 Object URL 로는 객체로 접근을 못하게 된다.  /
`Distribution domain name` 을 통해서만 객체에 접근할 수 있는 것을 확인할 수 있다.

> 이렇게 OAC를 통해 CloudFront로만 S3 객체에 접근이 가능하고 S3 객체의 직접적인 접근을 막아서 보안을 한층 강화할 수 있게 되었습니다.

<img width="500" alt="스크린샷 2024-11-12 오전 2 36 03" src="https://github.com/user-attachments/assets/164db094-3fd0-467d-b77b-c70ee56e8f45">

- `Edit Block public access (bucket settings)`: Bucket 의 Public access 차단도 활성화해준다.

이렇게 까지 해주면 Bucket 의 보안이 권장사항에 맞게 설정되는 것이다.

<br/>

<br/>

<br/>

## ⚡️ 서버에 CloudFront 적용

<img width="500" alt="스크린샷 2024-11-12 오전 2 59 22" src="https://github.com/user-attachments/assets/6f8659c0-3822-4690-b112-66dd7eb84c69">

**먼저 EC2 에 Cloudfront 접근 권한을 추가한다.**

<img width="500" alt="스크린샷 2024-11-12 오전 3 01 20" src="https://github.com/user-attachments/assets/7711724b-cd03-4a86-b7f3-fa89e0640c9c">

`IAM > roles > EC2 역할 선택 > Permissions > Attach policies > CloudFrontFullAccess 추가`
- 운영 환경에서는 필요한 권한만 최소한으로 직접 작성해서 사용하시는 게 좋다.

---

**CloudFront 정보가 입력된 버전의 시작 템플릿을 생성해준다.**
- `Advanced Details > IAM instance profile` 이 이전에 수정한 역할이 선택되어 있는지 확인한다.
- User Data 부분 수정

```shell
#!/bin/bash

#Git 레포지토리 클론 및 브랜치로 이동
git clone -b 5_2_monolithic_s3_with_cloudfront https://github.com/burger-2023/aws-operation-prac.git

#폴더 이동
cd aws-operation-prac

#Gradle을 이용한 Spring Boot 프로젝트 빌드 후 빌드된 Spring Boot 애플리케이션 실행
./gradlew build
java -jar build/libs/aws-msa-monolithic-prac-0.1.jar \
--spring.datasource.url=jdbc:postgresql://[RDS엔드포인트]:[DB연결포트]/[DB이름] \
--spring.datasource.username=[DB유저이름] \
--spring.datasource.password=[DB비밀번호] \
--cloud.aws.s3.bucket=[S3버킷명] \
--cloud.aws.region.static=ap-northeast-2 \
--cloud.aws.cloud-front.domain-name=[CloudFront엔드포인트] \
--cloud.aws.cloud-front.distribution-id=[배포ID]
```

<br/>

<br/>

<br/>

## ⚡️ 정리

### 🔋 S3와 Cloudfront

- S3는 사용자가 원하는 만큼의 데이터를 저장할 수 있는 저장소
- 저렴한 가격에 비해 강력한 내구성(99.999999999%)
- 버킷과 객체로 구성
- Cloudfront는 콘텐츠 전송 네트워크 서비스 (CDN)
- 전 세계의 여러 위치한 엣지 로케이션에 콘텐츠를 캐시(임시 저장)
- 사용자가 요청하면, 가장 가까운 엣지 로케이션에서 빠르게 콘텐츠 제공

### 🔋 현재까지 구조 정리

**1. VPC 구성**

<img width="500" alt="스크린샷 2024-11-12 오전 3 30 40" src="https://github.com/user-attachments/assets/6ee895e6-31dd-4211-8403-8dd95d79802b">

- VPC생성
  - CIDR 블록으로 사용할 IP 주소의 범위를 설정해서 VPC를 생성하고 
  - 퍼블릭 서브넷과 프라이빗 서브넷을 두 개의 가용 영역에 걸쳐 구축
- 인터넷 게이트웨이를 VPC에 배치
- 퍼블릭 서브넷의 라우팅 테이블은 인터넷 게이트웨이를 가리켜 외부 인터넷에서 트래픽을 들어올 수도 있고 나갈 수도 있게 설정
- 넷 게이트웨이를 퍼블릭 서브넷에 배치했고 프라이빗 서브넷의 라우팅 테이블이 넷 게이트웨이를 가리켜 트래픽이 외부로 나갈 수 있게 되었지만 외부에서는 접근할 수 없도록 보안을 강화

**2. EC2와 Bastion Host**

<img width="500" alt="스크린샷 2024-11-12 오전 3 30 49" src="https://github.com/user-attachments/assets/7c4a31ad-7481-4713-bca1-a05c4f31e56c">

- 퍼블릭 서브넷에 배치된 EC2에 쿠팡 서버를 배포해서 누구든지 서비스를 제공받을 수 있게 구성
- 하지만 퍼블릭 서브넷은 보안적으로 문제가 있었고, Bastion Host를 도입하면서 쿠팡 서버를 프라이빗 서브넷으로 이전
- 베스처노스트는 쿠팡 서버보다 앞에서 트래픽을 받아 쿠팡 서버의 보안을 향상
- 구팡 서버로 직접적인 접근 불가능
- <p style="color: red;">서비스에 필요한 트래픽도 접근 불가능</p>

**3. ALB**

<img width="500" alt="스크린샷 2024-11-12 오전 3 31 00" src="https://github.com/user-attachments/assets/ba615364-56a3-4078-9511-3452824b2022">

- 퍼블릭 서브넷에 ALB 배치
- 외부에서 들어오는 HTTP 트래픽을 프라이빗 서브넷의 EC2로 전달
- 애플리케이션 로드 밸런서는 수평으로 확장되는 여러 개의 EC2의 트래픽을 로드 밸런싱해주는게 주 기능으로 확장성에 꼭 필요한 역할을 하는 서비스
- AWS 관리형 서비스
- <p style="color: red;">수동으로 수평확장은 사실상 불가능</p>


**4. ASG**

<img width="500" alt="스크린샷 2024-11-12 오전 3 31 10" src="https://github.com/user-attachments/assets/8c0b5f35-27e1-44fd-bf9e-cbd084b60dc6">

- AMI를 사용해서 우리가 원하는 초기 환경을 미리 구성한 상태의 EC2를 배포할 수 있게
- 생성한 AMI와 EC2 인스턴스의 세부 설정들을 정의한 시작 템플릿을 생성
- 시작 템플릿은 오토 스케일링 그룹이 EC2 인스턴스를 생성할 때 참고할 구성
- 시작 템플릿을 기반으로 오토 스케일링 그룹을 생성하고 이전에 생성했던 애플리케이션 로드 밸런서의 대상 그룹을 선택해 쉽게 통합
- 마지막으로 조정 정책을 설정해서 트래픽이나 CPU, 메모리 사용률에 따라 확장될 수 있도록 단계 조정 정책이나 단순 조정 정책들을 사용
- 이렇게 해서 가용성과 확장성을 모두 확보
- <p style="color: red;">일관성이 없는 데이터</p>


**5. RDS**

<img width="500" alt="스크린샷 2024-11-12 오전 3 31 21" src="https://github.com/user-attachments/assets/6cdb356c-b15f-427a-887e-f8e696831f6a">

- AWS에서 제공하는 관계형 데이터베이스
- AWS에서 운영부분을 관리
- 중단 없는 스토리지 확장
- 손쉬운 백업, 마이그레이션
- 멀티 AZ 배포, 읽기전용 복제본, RDS Proxy
- 아직 **파일 저장** 부분이 문제
  - 중앙 관리가 되지 않아 각각 인스턴스에 저장되고 있고 일관성이 없다.
- <p style="color: red;">일관성이 없는 데이터</p>

**6. S3, Cloudfront**

<img width="500" alt="스크린샷 2024-11-12 오전 3 31 30" src="https://github.com/user-attachments/assets/e44f5b08-ad24-441a-a218-8fa0f836cce8">

- S3 - 확장되는 파일 저장소 서비스
- 버킷과 객체로 구성
- 보안설정으로 안전하게 관리
- Cloudfront - 콘텐트 전송 네트워크 서비스
- 오리진의 콘텐츠를 전세계 엣지 로케이션에 캐시

### 🔋 개선이 필요한 부분

<img width="500" alt="스크린샷 2024-11-12 오전 3 31 42" src="https://github.com/user-attachments/assets/e2060f94-912b-4483-aebd-0e07953ef4c6">

- 구팡 서비스는 현재 모노리식 아키텍쳐로 구성
- 트래픽이 계속 늘어날 것을 대비해야 한다.
- 서비스가 성장함에 따라 문제가 발생할 수 있음

> **마이크로 서비스 아키텍쳐로 모노리식 아키텍쳐의 단점을 보완**
