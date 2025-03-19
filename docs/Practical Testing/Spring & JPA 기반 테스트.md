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

# 💡 Persistence Layer 테스트

## 📝 요구사항 추가

- 키오스크 주문을 위한 상품 후보 리스트 조회하기
- 상품의 판매 상태: 판매중, 판매보류, 판매중지
  - 판매중, 판매보류인 상태의 상품을 화면에 보여준다.
- id, 상품 번호, 상품 타입, 판매 상태, 상품 이름, 가격 

<img alt="Persistence Layer Test" width="500" src="https://github.com/user-attachments/assets/0953a208-5da3-460d-9fdf-4f9ea1555979"> 

## ⚡️ Persistence Layer 의 역할

> **Data Access 역할**
> 
> **비즈니스 가공 로직이 포함되어서는 안된다. Data 에 대한 CRUD 에만 집중한 레이어**

## ⚡️ Persistence Layer 테스트를 하는 이유

```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingStatuses);
}
```

### 🤔 이렇게 코드가 간단하고 명확한데 테스트가 필요할까?

- **`Repository`** 를 구현하는 기술, 방법(MyBatis, QueryDSL 등)에 따라 테스트 방법이 달라질 수 있기 때문에 그런 것들을 보장하고자 한다.
1. **이것도 내가 작성한 코드이기 때문에 제대로 쿼리가 날라가는지 보장하기 위해서 테스트 코드 작성이 필요하다.**
2. **이 코드가 미래에는 어떠한 형태로 변형될지 모르기 때문에 그거에 대한 보장도 필요하다.**

Persistence Layer 테스트(Repository 테스트)는 사실상 단위 테스트에 가깝다.
- **DB 에 access 하는 로직만 갖고 있기 때문에 이 기능 단위로 보자면 `단위 테스트`에 가깝다.**


```java
assertThat(products).hasSize(2)
        .extracting("productNumber", "name", "sellingStatus")
        .containsExactlyInAnyOrder(
		tuple("001", "아메리카노", SELLING),
                tuple("002", "카페라뗴", HOLD)
        );
```

- **`List`를 검증할 때 주로 `hasSize`, `extracting`, `containsXxx` 메서드를 사용하자!**

```java
@ActiveProfiles("test")
```

- **테스트 코드를 작성할 때는 `@ActiveProfiles` 를 사용하여 `test profile` 을 사용하도록 설정하자!**

```java
//@SpringBootTest
@DataJpaTest
```

- **`@DataJpaTest`**: JPA 관련된 빈만 등록하여 테스트를 진행한다. `@SpringBootTest` 보다 빠르게 테스트를 진행할 수 있다.

---

# 💡 Business Layer 테스트

## 📝 요구사항 추가
- 상품 번호 리스트를 받아 주문 생성하기
- 주문은 주문 상태, 주문 등록 시간을 가진다.
- 주문의 총 금액을 계산할 수 있어야 한다.

## ⚡️ Business Layer 의 역할

**비즈니스 로직을 구현하는 역할**

>  **Persistence Layer 와의 상호작용(Data를 읽고 쓰는 행위)을 통해 비즈니스 로직을 전개시킨다.**

### ✅ ***트랜잭션*** 을 보장해야 한다!!!

<img alt="Business Layer Test" width="500" src="https://github.com/user-attachments/assets/66fcee1f-77f2-46f7-937f-8b092d6e90a7"/>

- Service Test 는 Persistence Layer 를 배제하지 않고 Business Layer + Persistence Layer 를 통합적으로 테스트한다.
