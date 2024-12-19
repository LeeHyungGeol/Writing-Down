# 💡 VPC Endpoint - NAT Gateway 없이 프라이빗 서브넷 사용

## ⚡️ VPC Endpoint

> **인터넷 게이트웨이나 NAT 게이트웨이 없이 AWS 서비스에 접근**

- 트래픽은 인터넷을 거치지 않고, `AWS의 내부 네트워크`를 사용
- 보안을 강화하고, 데이터 전송 비용을 절약
- 종류는 크게 3가지가 있다. `Gateway Endpoint`, `Interface Endpoint`, `Gateway Loadbalancer Endpoint`
- 이중에서 `Gateway Endpoint`, `Interface Endpoint` 를 사용해본다.

## ⚡️ Interface Endpoint

<img width="700" alt="스크린샷 2024-11-27 오후 6 18 39" src="https://github.com/user-attachments/assets/f93bcf5d-111e-4ed7-bd62-471e0e9a8df9">

> **가용영역의 서브넷에 배치되어 Private IP를 할당받는 리소스**

- `AWS의 PrivateLink` 기술을 활용해 VPC 내의 리소스와 AWS 서비스간의 프라이빗한 연결 제공
  - **`PrivateLink`는 프라이빗한 연결을 가능하게 해주는 AWS의 기술**
- Internet Gateway나 NAT Gateway없이 ECR 이나 S3 같은 AWS의 리소스에 접근을 가능하게 해준다.
- 이때 트래픽은 외부 인터넷으로 나가지 않고 AWS 내부 네트워크만 사용하게 된다.
- 시간당 0.013$, 데이터 전송 1GB당 0.01$
  - NAT Gateway 보다 4배 이상 저렴하다.


## ⚡️ NAT Gateway vs Interface Endpoint

<img width="700" alt="스크린샷 2024-11-27 오후 6 18 57" src="https://github.com/user-attachments/assets/391bed36-f42e-422a-926a-f3515e1de181">

- AWS에 VPC가 존재하고, VPC에는 퍼블릭, 프라이빗 서브넷을 배치
- 이때 프라이빗 서브넷의 EC2가 ECR로 요청을 하려는 상황


## ⚡️ NAT Gateway vs Interface Endpoint

<img width="1000" alt="스크린샷 2024-11-27 오후 6 19 13" src="https://github.com/user-attachments/assets/46952a72-5d3c-4fb8-8640-349d3a2510b5">

- NAT Gateway - 시간당 0.059$, 월 처리된 데이터 GB당 0.059$
  - 월 100GB 사용 시 - 49$, 월 100TB 사용 시 6,084$
- Interface Endpoint - 시간당 0.013$, 처리된 데이터 GB당 0.01$이하
  - 월 100GB 사용 시 - 10.36$, 월 100TB 사용 시 1,009.36$


## ⚡️ Gateway Endpoint

<img width="700" alt="스크린샷 2024-11-27 오후 6 19 29" src="https://github.com/user-attachments/assets/3052854e-0060-4545-ae2a-f053699fcc26">

> **인터넷 게이트웨이나 NAT 게이트웨이 없이 S3나 DynamoDB에 연결**

- Gateway Endpoint는 VPC 내부에 생성되어, S3나 DynamoDB를 대상으로 지정
- `프라이빗 서브넷 라우팅 테이블`에 `Gateway Endpoint`를 타겟으로 설정한다.
  - 이렇게 설정하면 S3 로 가는 트래픽은 AWS 내부 네트워크를 통해 NAT Gateway 없이 접근할 수 있게 된다.
- **추가 비용이 없음**

<br/>

<br/>

<br/>

## ⚡️ VPC Endpoint 로 ECR, S3 를 NAT Gateway 없이 Private Subnet 에서 접근

NAT Gateway 없이 혹은 NAT Instance 를 실행하지 않고 Private Subnet 에 배치되어 있는 EC2 Instance 에서 ECR, S3 에 접근을 시도하면 에러가 발생한다.

### 🔋 Create Endpoint

> **`VPC ECR Endpoint` 생성**

<img width="700" alt="스크린샷 2024-11-27 오후 6 59 39" src="https://github.com/user-attachments/assets/cc9d69d2-315e-4bdd-afb1-bec5398076c9">

- `ecr.api`: ECR 에 ECR 로그인 같은 API 요청을 하기 위해 필요하다.
- `ecr.dkr`: docker image 를 pull 받거나 push 할 때 필요하다.
- Enable DNS name: ECR 의 endpoint 를 사용할지에 대한 여부

<img width="700" alt="스크린샷 2024-11-27 오후 6 59 49" src="https://github.com/user-attachments/assets/25c1e2b5-6555-431f-a075-fd35c6238ae7">

- Subnet 은 Private Subnet 을 선택해준다.
- 가용성이나 성능을 위해서는 모든 가용영역에 배치하는게 좋겠지만 여기서는 하나의 가용영역에만 배치한다.
  - 비용도 그만큼 많이 들기 때문이다.

<img width="700" alt="스크린샷 2024-11-27 오후 7 06 10" src="https://github.com/user-attachments/assets/16581c83-e5e3-4a8d-a231-75060e501d9b">

- 모든 VPC Endpoint 에서 사용할 공통 보안그룹을 새로 생성한다.
- 인바운드 룰은 모든 트래픽(`All traffic`)을 EC2 Private-ec2 보안그룹(`private-ec2-sg`)에 열어준다.
- 그리고, 모든 트래픽(`All traffic`)을 서버가 사용하고 있는 보안그룹(`msa-sg`)에도 열어준다.

`Policy` 는 우선 `full access` 를 선택해준다.

이렇게만 하면 Create endpoint 를 할 때 에러가 발생한다.
- 이유는 지정한 VPC에 DNS Name을 사용할 수 있는 설정이 비활성화 되어있기 때문이다.

VPC 를 선택 후 `Actions > Edit VPC settings`

<img width="700" alt="스크린샷 2024-11-27 오후 7 12 05" src="https://github.com/user-attachments/assets/1fdc6243-dd3e-4b9d-b625-87e51bb03e1c">

- `Enable DNS resolution`: VPC 내의 도메인 이름을 IP 주소로 변환해주는 기능
- `Enable DNS hostnames`: VPC 내의 인스턴스에 DNS host 이름을 할당할 수 있게 해주는 기능
- `Enable DNS hostnames`를 체크해준다.

**그런 다음에 ecr.api, ecr.dkr endpoint 를 각각 생성해준다.**

---

이 상태에서 private-ec2 에서 docker 에 로그인 한후 image 를 pull 받으려고 하면 에러가 발생한다. / 
이유는 ECR 이 image layer 를 S3 에 저장하는데 지금은 S3 에 접근할 수 없기 때문에 에러가 발생한다.

> **그래서, `S3` 에 대한 `VPC Endpoint` 도 생성해줘야 한다.**

<img width="700" alt="스크린샷 2024-11-27 오후 7 31 36" src="https://github.com/user-attachments/assets/07e0a298-37a6-4095-8b24-f86162d591d0">

- 비용이 들지 않는 `Gateway endpoint` 를 선택한다.

<img width="700" alt="스크린샷 2024-11-27 오후 8 29 50" src="https://github.com/user-attachments/assets/5b608a77-3214-462d-a01f-8166cae30ac9">

- `Gateway Endpoint`에 라우팅 설정할 `Routing table`을 지정해야 합니다.
- 여기서는 `private-routing-table` 를 선택해준다.

---

`EC2 > Auto scaling groups > user-service-asg > Actions > edit` 

<img width="700" alt="스크린샷 2024-11-27 오후 8 44 09" src="https://github.com/user-attachments/assets/0ade5091-7ba9-4f1a-a3b0-c1e51e6427ad">

> 이전에는 Auto-scaling-group 의 launch template 을 public-ec2 로 설정했었다. /
이제는 private-ec2 버젼으로 launch template 을 새로 생성해준다.

<img width="700" alt="스크린샷 2024-11-27 오후 8 45 28" src="https://github.com/user-attachments/assets/1c75278c-09cd-4b52-a5f9-c221e48af446">

- private-subnet 으로 이름을 설정한다.
- 나머지 설정은 그대로 두고,
- `Network settings > Advanced network configuration > Auto-assign Public IP` 는 `Disable` 로 설정해준다. 
- `Create template version`

<img width="700" alt="스크린샷 2024-11-27 오후 8 47 55" src="https://github.com/user-attachments/assets/b48cf2d0-99f6-4b70-afac-3066b7e1315a">

- `Edit service-asg` 에서
- 새로 생성한 template 을 다시 설정해주고
- Network 탭에서는 public-subnet 2개를 지우고, private-subnet 2개를 선택해준다.

<br/>

<br/>

<br/>

# 💡CloudFront 캐시 무효화 자동화 솔루션

## ⚡️ 현재 CloudFront 캐시 무효화가 불가능한 이유

<img width="700" alt="스크린샷 2024-11-27 오후 9 25 38" src="https://github.com/user-attachments/assets/1f8b6507-8533-48d5-b8e5-86f303b10724">

- 현재 프라이빗 서브넷에 EC2가 배치되어있고, 이미지가 업로드될 때 CloudFront에 캐시 무효화 요청을 보낸다고 하자.
- 이전까지는 NAT Gateway를 통해 인터넷을 거쳐 CloudFront로 요청이 도달
- 현재는 NAT Gateway가 없어 요청이 인터넷으로 갈 수 없어 요청이 CloudFront까지 갈 수 없음
- CloudFront에 대한 VPC Endpoint가 지원되지 않음
- VPC Endpoint 외에 다른 방법을 찾아야 한다.

## ⚡️ CloudFront 캐시 무효화 솔루션

> **이 문제를 해결하기 위해 `AWS Lambda` 와 `S3 버킷 이벤트`를 사용한댜.**

<img width="1000" alt="스크린샷 2024-11-27 오후 9 24 40" src="https://github.com/user-attachments/assets/773f4a3a-b87b-40b9-bc17-12ecc2257fc8">

- `Lambda`: 서버리스 컴퓨팅 서비스, 서버 없이 코드를 배포하고 실행할 수 있는 서비스
- `S3 버킷 이벤트`: 버킷에 특정 이벤트가 발생할 때 자동으로 Lambda 같은 서비스를 트리거
  - EX) **객체가 추가되거나 삭제될 때 이런 이벤트를 Lambda 같은 다른 서비스에 트리거할 수 있다.**
- **이런 방법으로 `image resize 솔루션`도 응용해볼 수 있다.**

<br/>

<br/>

<br/>
 
## ⚡️ CloudFront 캐시 무효화를 위한 Lambda 함수 생성

### 🔋 Create function

<img width="700" alt="스크린샷 2024-11-27 오후 9 36 56" src="https://github.com/user-attachments/assets/551798b9-6a00-4c9b-b6a3-49fcd7b22fd4">

- `Node.js 20.x` 런타임을 선택해준다.
- Lambda 를 생성해준다.

---

<img width="700" alt="스크린샷 2024-11-27 오후 9 48 45" src="https://github.com/user-attachments/assets/dd0c2159-f86a-4bab-bec5-3a691b733e7d">

- 이제 여기에 cloudFront 캐시 무효화 코드를 작성해주면 된다.
- 하지만, 그전에
- Lambda 함수에 
- CloudFront 에 캐시 무효화를 할 수 있는 권한과 
- 무효화를 시킬 객체인지 구분하기 위해 버전 정보를 조회하는 권한을 부여해줘야 한다.

`Configuration > Permissions > Execution role > Role name`

- **이미 등록되어 있는 권한: `AWSLambdaBasicExecutionRole`: CloudWatch 서비스로 log 를 보내는 권한**

`Add Permissions > Create inline policy`


**Lambda 함수 CloudFront 캐시 무효화, S3 객체 버전 조회 권한 Json**

```shell
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": "cloudfront:CreateInvalidation",
            "Resource": "*"
        }, { 
            "Effect": "Allow", 
            "Action": "s3:ListBucketVersions", 
            "Resource": "arn:aws:s3:::bucket-name" 
        }
    ]
}
```

- `bucket-name` 은 현재 사용하고 있는 S3 버킷 이름으로 변경해준다.

`Create Policy` 를 통해 권한을 부여해준다.

--- 

**cloudfront 캐시 무효화 코드** 를 작성해준다.

```js
// AWS SDK 클라이언트와 명령어를 ESM 문법으로 임포트
import { S3Client, ListObjectVersionsCommand } from "@aws-sdk/client-s3";
import { CloudFrontClient, CreateInvalidationCommand } from "@aws-sdk/client-cloudfront";

// S3 및 CloudFront 클라이언트 초기화
const s3 = new S3Client();
const cloudfront = new CloudFrontClient();

// Lambda 핸들러 함수
export const handler = async (event) => {
    // 이벤트에서 버킷 이름과 객체 키 추출
    const bucket = event.Records[0].s3.bucket.name;
    const key = decodeURIComponent(event.Records[0].s3.object.key.replace(/\+/g, ' '));

    // 실제 CloudFront 배포 ID로 교체하거나 환경 변수 사용
    const distributionId = process.env.CLOUDFRONT_DISTRIBUTION_ID || 'YOUR_CLOUDFRONT_DISTRIBUTION_ID';

    try {
        // 객체 버전을 나열하기 위한 파라미터 준비
        const listParams = {
            Bucket: bucket,
            Prefix: key,
        };

        // ListObjectVersions 명령어 생성 및 실행
        const listCommand = new ListObjectVersionsCommand(listParams);
        const versionList = await s3.send(listCommand);

        // 최신 버전을 제외한 이전 버전 필터링
        const previousVersions = versionList.Versions.filter(version => !version.IsLatest);

        if (previousVersions.length > 0) {
            console.log('이전 버전이 발견되어 캐시 무효화를 시작합니다.');

            // 무효화 배치 파라미터 준비
            const invalidationBatch = {
                CallerReference: `${Date.now()}`, // 무효화 고유성을 보장하기 위한 고유 값
                Paths: {
                    Quantity: 1,
                    Items: [`/${key}`], // 무효화할 경로 지정
                },
            };

            const invalidationParams = {
                DistributionId: distributionId,
                InvalidationBatch: invalidationBatch,
            };

            // CreateInvalidation 명령어 생성 및 실행
            const invalidationCommand = new CreateInvalidationCommand(invalidationParams);
            const invalidationResult = await cloudfront.send(invalidationCommand);

            console.log('캐시 무효화에 성공했습니다:', invalidationResult);
        } else {
            console.log('이전 버전이 없어 캐시 무효화가 필요하지 않습니다.');
        }
    } catch (error) {
        console.error('S3 이벤트 처리 중 오류 발생:', error);
        throw new Error('Lambda 함수가 S3 이벤트를 처리하는 데 실패했습니다.');
    }
};
```

- `YOUR_CLOUDFRONT_DISTRIBUTION_ID`는 실제 CloudFront 배포 ID로 교체해준다.
- `deploy`를 눌러준다.

---

### 🔋 S3 Bucket Event 설정

`S3 > bucket-name > Properties > Event notifications > Create event notification`

<img width="700" alt="스크린샷 2024-11-27 오후 10 09 12" src="https://github.com/user-attachments/assets/7c251f21-792d-4e57-8af2-dffb7ef3b268">

<img width="700" alt="스크린샷 2024-11-27 오후 10 09 27" src="https://github.com/user-attachments/assets/ea5389d7-e5ba-4b91-80dd-a6797c0f69fc">

<img width="700" alt="스크린샷 2024-11-27 오후 10 10 33" src="https://github.com/user-attachments/assets/2256762b-8a87-44ab-9a0b-3e2bf4302534">

- `prefix`: 입력한 문자열로 시작하는 키를 가진 객체만 이벤트에 적용되도록 제한하는 설정
- `suffix`: 입력한 문자열로 끝나는 키를 가진 객체만 이벤트에 적용되도록 제한하는 설정
- `Event Type`: 어떤 이벤트가 발생했을 때 trigger 할지 설정
  - 캐시 무효화는 생성, 삭제, 복원일 때만 되도록 선택
- 마지막으로 대상(`destination`)을 지정해줘야 한다.
- 이벤트는 람다 외에도 SNS나 SQS 서비스를 대상으로 지정해줄 수도 있습니다.
- 우리는 생성했던 람다 함수를 선택해주고 세이브해주겠습니다.

---

### 🔋 Auto-scaling-group 에서 user-service-asg 의 launch template 수정

`EC2 > Auto-scaling groups > user-service-asg > Actions > edit`

- User-Data 부분에 cloudfront-distribution-id 부분을 지워준다.
- 나머지 설정은 그대로 둔다.
