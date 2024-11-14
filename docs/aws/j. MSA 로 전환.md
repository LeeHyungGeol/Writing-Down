# 💡 MSA 로 전환


## ⚡️ Micro Service Architecture

<img width="700" alt="스크린샷 2024-11-14 오후 8 18 06" src="https://github.com/user-attachments/assets/fc5e4053-ab0c-45d7-9198-714122efeb28">

- 하나의 큰 애플리케이션을 여러 **서비스 단위로 나눈** 아키텍처
- 유저 서비스, 장바구니 서비스, 제품 서비스
- 필요에 따라 서비스끼리 통신

## ⚡️ MSA의 장점: 확장성

<img width="700" alt="스크린샷 2024-11-14 오후 8 18 24" src="https://github.com/user-attachments/assets/03ed0f7c-056d-428f-9a61-9c55e556fb21">

- 모노리식 아키텍처는 전체 서비스를 확장
- MSA는 필요한 서비스만 **독립적으로 확장**
- 자원을 효율적으로 사용, 비용 관리에 유리

## ⚡️ MSA의 장점: 장애 격리

<img width="700" alt="스크린샷 2024-11-14 오후 8 18 54" src="https://github.com/user-attachments/assets/0284ffb4-4ce5-4847-b3fc-8f43e59de24c">

- 모노리식 아키텍처는 한 서비스가 장애가 생기면 전체 시스템에 영향
- MSA는 한 서비스의 장애가 발생해도 **다른 서비스에 영향을 주지 않음**
- 각각의 서비스가 격리

## ⚡️ MSA의 장점: 빠른 배포와 유지보수

<img width="700" alt="스크린샷 2024-11-14 오후 8 19 22" src="https://github.com/user-attachments/assets/33a3b1a1-78ed-4cdc-bf6e-6be7b8141e86">

- 모노리식 아키텍처는 전체 프로젝트를 다시 배포해야해서 많은 시간이 소요
- MSA는 특정 서비스만 배포하면 되기때문에 **배포가 빠름**

## ⚡️ MSA의 장점: 기술적 유연성

<img width="700" alt="스크린샷 2024-11-14 오후 8 19 41" src="https://github.com/user-attachments/assets/7b2009f8-fda7-4236-998c-1a9f49442b98">

- 모노리식 아키텍처는 하나의 프레임워크와 하나의 데이터베이스
  - 한번 선택한 기술 스택에 얽매이게 되고 시스템 전체를 변경하지 않는 한 기술을 바꾸기가 어려울 때도 있다.
- MSA는 각 서비스마다 **다른 프레임워크와 데이터베이스를 선택**할 수 있음

## ⚡️ MSA의 장점: 팀 간 독립적인 작업환경

<img width="700" alt="스크린샷 2024-11-14 오후 8 19 54" src="https://github.com/user-attachments/assets/1d1e2451-1bfc-4ad2-8d9f-e759bc1cd50d">

- 모노리식 아키텍처는 협업에 어려움이 생길 수 있음
- MSA는 각 서비스를 **팀별로 독립적**으로 관리
  - 각 팀이 독립적인 릴리즈 주기를 가질 수 있다.

## ⚡️ MSA 프로젝트 클론 및 로컬 테스트

```shell
# User Service
git clone -b 1_msa_local_user https://github.com/burger-2023/aws-msa-user-operation-prac.git

# Product Service
git clone -b 1_msa_local_product https://github.com/burger-2023/aws-msa-product-operation-prac.git

# Cart Service
git clone -b 1_msa_local_cart https://github.com/burger-2023/aws-msa-cart-operation-prac.git

#스프링 부트 애플리케이션 실행 명령어 > 폴더로 이동한 후

./gradlew bootRun
```
