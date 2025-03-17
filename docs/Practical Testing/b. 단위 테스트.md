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

# 💡 테스트 케이스 세분화 하기

> 한 종류의 음료 여러 잔을 한 번에 담는 기능을 추가한다면?

## ⚡️ 질문하기: 암묵적이거나 아직 드러나지 않은 요구사항이 있는가?

> **기획자나 디자이너 등 타직군의 사람들에게 이 요구사항이 실제 내가 개발할 때의 요구사항과 일치하는지 혹은 아직 드러나지 않은 요구사항이 있거나 아직 도출이 안된 요구사항이 있는지 질문해보아야 한다!**

- **성공 케이스**
- **예외 케이스**

> **경계값 테스트**: `범위(이상, 이하, 초과, 미만), 구간, 날짜 등`

---

# 💡 테스트하기 어려운 영역을 구분하고 분리하기

> **`요구사항 추가`: 가게 운영 시간(10:00 ~ 22:00)외에는 주문을 생성할 수 없다.**

## ⚡️ 테스트가 개발 환경이 아닌 실제 외부 현재 상황에 따라 실패할 수 있는 상황이 발생할 수 있다!
- EX) 현재 시간이 13시이면 테스트가 성공하고, 23시이면 테스트가 실패한다. 
- **테스트가 실패하는 이유는 개발자가 만든 코드가 잘못된 것이 아니라, 외부 환경이 변화되어서 발생한 것이다.**
  - 현재의 일시를 가지고 제약 조건을 걸어서 테스트가 어려운 코드가 들어온 것이다.
  - 전체가 테스트가 불가능한 상태에 빠진다.

중요한 것은 현재 시각이 아니라 어떤 시각이 주어졌을 때 이 시각을 가지고 어떤 조건을 판단하는 것이다.

우리가 테스트 코드 상에서 원하는 값을 넣어줄 수 있도록 설계하는 것이 중요하다.

> **테스트 어려운 영역을 구분해서 외부로 분리할수록 테스트 가능한 코드는 많아진다!**

## ⚡️ 테스트 하기 어려운 영역이란?

> **관측할 때 마다 다른 값에 의존하는 코드**
- `현재 날짜/시간`, `랜덤 값`, `전역 변수/함수`, `사용자 입력` 등

> **외부 세계에 영향을 주는 코드**
- `표준 출력(log 등)`, `메시지 발송(이메일 전송 등)`, `데이터베이스에 기록하기(db, file system 에 기록 등)` 등

## ⚡️ 그렇다면 테스트하기 쉽게 하려면? > `순수 함수(pure function)`

`함수형 프로그래밍`에서 외부 세계와 단절된 함수를 `순수 함수`라고 한다.

- **같은 입력에는 항상 같은 결과**
- **외부 세상과 단절된 형태**

이런 조건들을 만족하는 것이 `테스트하기 쉬운 코드`이다.
