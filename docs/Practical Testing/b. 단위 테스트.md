# 단위 테스트

# 💡 초간단 카페 키오스크 서비스 

## ⚡️ 요구사항
- 주문 목록에 음료 추가 / 삭제 기능
- 주문 목록 전체 지우기
- 주문 목록 총 금액 계산하기
- 주문 생성하기

---

# 💡 수동 테스트 vs 자동화된 테스트

## ⚡️ 수동 테스트의 문제점

```java
class CafeKioskTest {
	@Test
	void add() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		cafeKiosk.add(new Americano());

		System.out.println(">>> 담긴 음료 수 : " + cafeKiosk.getBeverages().size());
		System.out.println(">>> 담긴 음료 : " + cafeKiosk.getBeverages().get(0).getName());
	}

}
```

- **콘솔**에 출력된 결과를 확인하게 된다면 **최종 확인 주체는 나 즉, '사람'**이 되어 버린다. 
- **다른 사람**이 이 테스트 코드를 봤을 때 **무엇을 검증해야 하는지 어떤 게 맞고, 어떤 게 틀린 상황인지를 알 수가 없다.** 
- 콘솔에 출력하는 테스트는 **무조건 성공만 하는 테스트 코드**다. **무엇을 검증하지 않는다.**

---

# 💡 JUnit5로 테스트하기

## ⚡️ 단웨 테스트

> **단위 테스트**는 `작은 코드(클래스 or 메서드)` 단위를 **독립적(외부 상황(외부 api 등)에 의존하지 않는)**으로 검증하는 테스트

- 다른 테스트에 비해 검증 속도가 매우 빠르고, 안정적이다.

### 🔋 JUnit5 
- 단위 테스트를 위한 **테스트 프레임워크**
- YUnit - Kent Beck
  - SUnit(Smalltalk), JUnit(Java), NUnit(.NET), PHPUnit(PHP), ...

### 🔋 AssertJ
- 테스트 코드 작성을 원활하게 돕는 테스트 라이브러리
- 풍부한 API, 메서드 체이닝 지원

```java
class CafeKioskTest {
	@Test
	void add_manual_test() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		cafeKiosk.add(new Americano());

		System.out.println(">>> 담긴 음료 수 : " + cafeKiosk.getBeverages().size());
		System.out.println(">>> 담긴 음료 : " + cafeKiosk.getBeverages().get(0).getName());
	}

	@Test
	void add() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		cafeKiosk.add(new Americano());

		assertThat(cafeKiosk.getBeverages()).hasSize(1);
		assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
	}


	@Test
	void remove() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();

		cafeKiosk.add(americano);
		assertThat(cafeKiosk.getBeverages()).hasSize(1);

		cafeKiosk.remove(americano);

		assertThat(cafeKiosk.getBeverages()).isEmpty();
	}

	@Test
	void clear() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();
		Latte latte = new Latte();

		cafeKiosk.add(americano);
		cafeKiosk.add(latte);
		assertThat(cafeKiosk.getBeverages()).hasSize(2);

		cafeKiosk.clear();
		assertThat(cafeKiosk.getBeverages()).isEmpty();
	}
}
```

- 위의 코드를 보면 사람이 확인하는 것은 아무것도 없이 모두 `자동화된 테스트들`이다.

---



