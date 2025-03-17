# Spring & JPA 기반 테스트

# 💡 레이어드 아키텍쳐(Layered Architecture)와 테스트

## ⚡️ 레이어드 아키텍쳐(Layered Architecture)란?

<img alt="레이어드 아키텍쳐(Layered Architecture)" width="500" src="https://github.com/user-attachments/assets/877512f9-4705-4c15-97d1-4914a75518ce">

-  Persistence Layer 하위에 Infrastructure Layer 를 추가해서 4-tier layer 로 구성할 수 있다.

## 🧐️ 왜 레이어를 구분하는가?

> `관심사의 분리`!!! > 더 책임을 잘 나누고 유지보수하기 용이하게 만들어보자!

### 🤔 그런데 테스트하기 복잡해보인다?

> Spring & JPA 라는 기술 자체 보다는 우리가 무엇을 어떻게 테스트할 것인지에 집중하자!

**이제는 클래스나 메서드에 대한 단위 테스트가 아닌, A 모듈, B 모듈을 통합하여 통합 테스트를 진행해보자!**

## ⚡️ 통합 테스트(Integration Test)

>  **여러 모듈이 협력하는 기능을 통합적으로 검증하는 테스트**
- 일반적으로 작은 범위의 단위 테스트 만으로는 기능 전체의 신뢰성을 보장할 수 없다.
- 풍부한 단위 테스트 & 큰 기능 단위를 검증하는 통합 테스트
