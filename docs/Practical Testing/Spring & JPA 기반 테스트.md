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

---

# 💡 Spring & JPA 훑어보기 & 기본 엔티티 설계

## ⚡️ Library vs Framework

<img alt="레이어드 아키텍쳐(Layered Architecture)" width="500" src="https://github.com/user-attachments/assets/2b8ff83f-e451-4706-a975-18b8cea92705"/>

- **Library**: `내 코드가 주체가 된다.(능동적)` 필요하면 외부의 것들을 끌어와서 사용하게 되는데 그것이 라이브러리
- **Framework**: 용어에서 볼 수 있듯이 `이미 프레임(Frame)이 있다.` 이미 동작할 수 있는 환경이 갖춰져 있다. **`내 코드가 수동적`으로 프레임 안에 들어가서 역할을 하게 된다.**

## ⚡️ Spring IoC, DI, AOP

- **IoC(Inversion of Control)**: 제어의 역전, `객체의 생성부터 생명주기 관리까지 모든 객체에 대한 제어권을 컨테이너(프레임워크)`가 가지고 있다.
- **DI(Dependency Injection)**: 의존성 주입, `객체 간의 의존성을 외부에서 주입`하는 것을 말한다.
- **AOP(Aspect Oriented Programming)**: 관점 지향 프로그래밍, `핵심 비즈니스 로직과 공통 기능을 분리`하여 관리하는 것을 말한다.
  - 비즈니스 흐름과 관계 없는 부분을 관점이라는 용어로 부르고, 얘를 다른 모듈로 분리해 내는 것. `Spring` 에서는 `Proxy 기반`으로 AOP 를 구현한다.

## ⚡️ JPA(Java Persistence API), ORM

<img alt="JPA. ORM" width="500" src="https://github.com/user-attachments/assets/c374682b-4782-433b-a991-fc5f5e668140"/>

> `ORM(Object-Relational Mapping)`
- 객체 지향 패러다임과 관계형 데이터베이스의 패러다임 간의 `불일치`를 해결하기 위한 기술
  - 이전에는 개발자가 객체의 데이터를 한땀한땀 매핑하여 DB에 저장 및 조회(CRUD)
  - ORM을 사용함으로써 개발자는 단순 작업을 줄이고, 비즈니스 로직에 집중할 수 있다. 

> `JPA(Java Persistence API)`
- Java 진영의 `ORM 기술의 표준 인터페이스`
- `인터페이스`이고, 여러 구현체가 있지만 보통 Hibernate 를 많이 사용한다.
- 반복적인 CRUD SQL 을 생성 및 실행해주고, 여러 부가 기능들을 제공한다.
- 편리하지만 쿼리를 직접 작성하지 않기 때문에, 어떤 식으로 쿼리가 만들어지고 실행되는지 명확하게 이해하고 있어야 한다.

## ⚡️ 기본 엔티티 설계

<img alt="다대다 관계" width="500" src="https://github.com/user-attachments/assets/57c115c7-6b43-4adf-97d8-b1d6d46c8aea"/>

<img alt="기본 엔티티 설계" width="500" src="https://github.com/user-attachments/assets/bb7b5499-932d-480a-8503-24b1bfc6a1c3"/>

---


