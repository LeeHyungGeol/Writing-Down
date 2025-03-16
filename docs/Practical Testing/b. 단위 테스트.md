# 단위 테스트

# 💡 초간단 카페 키오스크 서비스 

## ⚡️ 요구사항
- 주문 목록에 음료 추가 / 삭제 기능
- 주문 목록 전체 지우기
- 주문 목록 총 금액 계산하기
- 주문 생성하기

---

# 수동 테스트 vs 자동화된 테스트

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
