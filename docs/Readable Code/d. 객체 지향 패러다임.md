# 객체 지향 패러다임

객체 자체도 어떤 목적을 가지고 추상화가 된 것이 객체이고, 사실상 데이터와 코드의 조합이다.

이 객체가 여러 개가 생기다보니, 객체간의 `협력`, 각 객체가 담당하는 `책임`이 생긴다.

`캡추상다`: `캡슐화`, `추상화`, `상속`, `다형성` -> 다 추상화를 기반으로 하는 개념들
- `캡슐화`: 객체가 가지고 있는 데이터를 숨기고 그 데이터를 가공하는 로직도 숨기고 외부에 일부만 드러내는 것
  - 추상화와 비슷한 개념
  - 추상화의 하위 개념이라고도 볼 수 있다.
- `상속`: 꼭 필요한 곳에서 사용해야 한다. 정말 제한적으로 필요한 곳에서만 사용해야 한다.
- `다형성`: 추상화의 일종. 상위 추상화 레벨에서 요구되는 특징들만 뽑아내서 인터페이스화, 스펙화해서 실제 구현체들이 여러가지 형태로 바뀌어서 사용될 수 있는 것

`관심사의 분리(Separation Of Concern)`: 특정한 관심사(책임)에 따라서 우리가 객체를 만들어 낼 수 있다. 관심사에 따라 우리가 기능과 어떤 책임을 나누고 그 나눈 것들의 조합으로 프로그램이 돌아가는 것이다.
- 유지보수가 원활해진다.
- 추상화 과정의 일면
- `높은 응집도`, `낮은 결합도`: A 가 크게 변경되어도 B 는 최소한의 변경 혹은 변경이 아예 없는 형태로 유지가 될 수 있다.

<br/>

<br/>

<br/>

# 💡 객체 설계하기

<img width="500" src="https://github.com/user-attachments/assets/9ca84698-978e-40d6-a7a9-6bcf241fc0fc">

- 비공개 필드(데이터)
- 비공개 로직(기능 구현부)
- 공개 메서드 선언부 -> `추상화`: 공개 메서드 선언부만 외부에 노출함으로써 이 객체가 어떤 기능을 제공하는지 알게 되는 것이다.
  - **객체의 책임**을 `공개 메서드`를 통해 알 수 있다.

<img width="500" src="https://github.com/user-attachments/assets/2b1656dc-11b2-4986-af18-ba7164912cce">

- 이런 객체들이 모여서 **서로 상호간에 협력**을 하는 것이다. -> 객체간 협력 발생

<br/>

## ⚡️ 객체가 제공하는 것
- **절차 지향에서 잘 보이지 않았던 개념을 가시화** -> 그룹이 생겼기 때문에
- **관심사가 한 군데로 모이기 때문에, 유지보수성이 올라간다.**
  - EX) 객체 내부에서 객체가 가진 `데이터의 유효성 검증 책임`을 가질 수 있다. -> 생성자에서 유효성 검증을 할 수 있다.
- **여러 객체를 사용하는 입장에서는, 구체적인 구현에 신경 쓰지 않고 보다 높은 추상화 레벨에서 도메인 로직을 다룰 수 있다.**

<br/>

## ⚡️ 새로웅 객체를 만들 때 주의할 점

### 🔋 **1개의 관심사로 명확하게 책임이 정의되었는지 확인하기**

- 메서드를 추상화할 떄와 비슷하다.
- 객체를 만듦으로써 외부 세계와 어떤 소통을 하는지 생각하자.
- 어떤 책임을 갖고, 어떤 관심사를 통해서 이 객체가 생성되었는지를 계속 확인해야 한다.
- 리팩토링 혹은 요구사항이 변경되면서 이것은 계속 변경될 수 있다.
- 어떤 객체의 책임이나 역할이 끊임없이 변경될 수 있기 때문에 이껏도 고려해서 유도리 있게 해야한다.

---

### 🔋 생성자, 정적 팩토리 메서드에서 유효성 검증이 가능하다.

- 도메인에 특화된 검증 로직이 들어갈 수 있다.
  - **객체 안으로 유효성 검증 로직을 갖고 옴으로써 응집도가 높아진 것으로 볼 수 있다.**

**EX)**

```java
class Money {
	private long value;
	
	public Money(long value) {
        if (value < 0) {
            throw new IllegalArgumentException("음수는 불가능합니다.");
        }
        this.value = value;
    }
}
```

---

### 🔋 setter 사용 자제

**`setter` 라는 것은 외부 세계에서 값을 받아서 바로 내부에 있는 데이터를 변경하는 조금은 폭력적인 메서드이다.**

- 데이터는 불변이 최고다. **변하는 데이터라도 `객체가 핸들링`할 수 있어야 한다.**
- 객체 내부에서 외부 세계의 개입 없이 자체적인 변경/가공으로 처리할 수 있는지 확인
- **만약 외부에서 가지고 있는 데이터로 데이터 변경 요청을 해야 하는 경우, `setXxx` 라는 단순한 이름 보다는 `changeXxx`, `updateXxx` 와 같이 `의도를 드러내는 네이밍`을 고려하자.**

---

### 🔋 getter 도 처음에는 사용 자제. 반드시 필요한 경우에 추가하기

- 외부에서 객체 내 데이터가 필요하다고 getter 를 남발하는 것은 무례한 행동이다.

```java
Person person = new Person();

// (1)
if (person.getWallet().getIdentification().findAge() >= 19) {
        pass();	
}

// (2)
if (person.isAgeGreaterThanOrEqualTo(19)) {
        pass();
}
```

- (1) 은 폭력적이고, (2) 는 정중하다.
- 충분히 뭔가 메서드를 통해서 물어보거나 속에 있는 데이터를 밖으로 강제로 꺼내지 않고도 해결할 수 있는 상황인데, getter 라는 것이 남발되면 이렇게 **객체가 캡슐화로서 가지는 특징이 전혀 의미가 없어진다.**
- getter 를 남발하면 공개된 데이터나 마찬가지가 된다.
- `getter 를 쓰고 싶은데 쓰지 않고 대신에 객체에 물어볼 수 있지 않을까?` 하는 생각이 들어야 한다.

> ***객체에 메시지를 보내라!***

객체를 만들 때 getter, setter 모두 만들지 말고, 반드시 필요한 경우에만 추가하자.

반드시 필요한 순간도 온다. 왜냐하면 데이터를 뽑아서 써야 될 때가 분명히 있기 때문이다. 그때 getter 를 추가하자.

---

<img width="500" src="https://github.com/user-attachments/assets/dd35deda-6ab7-4db4-b19f-c835bf8e234d">

<img width="500" src="https://github.com/user-attachments/assets/8ba74ebd-b855-4e32-be40-ee8b57962be2">

**AS-IS**

```java
private static boolean isAllCellOpened() {
  return Arrays.stream(BOARD)
          .flatMap(Arrays::stream)
          .noneMatch(CLOSED_CELL_SIGN::equals);
}
```

**TO-BE**

```java
// 객체의 캡슐화된 데이터를 외부에서 알고 있다고 생각하지 말자.
// 외부에서는 데이터를 모르니까 짐작해서 물어보는 것이 최선이다.
private static boolean isAllCellChecked() {
	return Arrays.stream(BOARD)
            .flatMap(Arrays::stream)
            .allMatch(Cell::isChecked);
}
```

---

#### ⚛️ 객체 간의 역할과 책임을 한번 더 고민해 봅니다.

> 예시에서 person.get지갑().get신분증().findAge() >= 19 를 person.isAgeGreaterThanOrEqualTo(19) 메서드로 만들면 person 내부에서 다시 지갑.get신분증().findAge() 형태가 되는데, 이 경우 지갑.isAge..(19) 같은 메서드를 계속 만드는지 끊어내는 기준이 있는지 궁금합니다.

- 저는 그 경우에도 마찬가지로, 그 내부에서 객체 간의 역할과 책임을 한번 더 고민해 봅니다.

- 지갑.get신분증().findAge() 가 자연스러운지, 아니면 지갑에게 다시 질문하여 지갑.isAge..(19) 가 더 자연스러운지 Person 객체와 지갑 객체 사이에서 한번 더 고민해 보는거죠.
(이 예시에서는 지갑에게 다시 메시지를 보내 물어보는 후자가 더 좋지 않을까 싶네요 ㅎㅎ)
한번 더 들어가면 지갑과 신분증 사이에서 또 같은 고민을 해볼 수 있겠네요.

- 이렇게 계속 타고타고 들어가다가, 어느 순간 메시지를 보내지 않고 직접 꺼내도 되겠는데? 하는 순간이 오기도 합니다.

- 정리하면, 레이어마다 각 객체의 책임을 고려하여 결정하시면 됩니다.

---

### 🔋 필드의 수는 적을수록 좋다.

- 불필요한 데이터가 많을수록 복잡도가 높아지고 대응할 변화가 많아진다.
- 필드 A 를 가지고 계산할 수 있는 A 필드가 있다면, 메서드 기능으로 제공
- 단, 미리 가공하는 것이 성능상 이점이 있다면, 필드로 가지고 있는 것이 좋을 수도 있다.

```java
class Bill { 
	
	private final List<Menu> menus;
	// menus 를 통해 계산할 수 있는
	private final long totalPrice;
	
	// 필드 대신 제공할 수 있는 기능
	public long calculateTotalPrice() {
		return menus.stream()
                .mapToLong(Menu::getPrice)
                .sum();
    }
}
```

- totalPrice 는 menus 를 통해 계산할 수 있는 값이므로 굳이 필드로 가지고 있을 필요가 없다.
- menu 가 추가 혹은 삭제가 되는 경우 필드인 totalPrice 에 있는 값도 같이 수정이 필요해진다.
- **이렇게 미리 계산하지 않고 바로 계산해줄 수 있는 것들은 필드를 최소화하고 메서드 기능으로 제공하는 것이 좋다.**
- 하지만, 만약에 menus 를 통해서 calculate 하는 로직이 엄청 복잡한 경우에, **totalPrice 를 미리 계산하는 것이 성능상 이점이 있다면 필드로 가지고 있는 것이 좋다.**

---

### 🔋 정적 팩토리 메서드 of() 컨벤션

- 파라미터가 1개일 때 from(), 2개 이상일 때 of()를 사용하자는 뉘앙스는 제가 생각하기에 영어적 표현의 자연스러움 때문이라고 생각합니다.
그렇다고 반대로 파라미터가 0개, 1개일 때 of()를 쓰면 안 된다, 라는 의견에는 동의하지 않아요.

- 파라미터의 수에 따라 이름을 달리 하는 것보다, 이름을 통해 의도를 드러내는 데에 초점을 맞추는 것이 가장 중요하다고 생각합니다.
저는 그래서 보통 인스턴스 생성의 의미를 나타내는 정적 팩토리 메서드는 of(), create() 등을 많이 사용하고 있어요.

---

### 🔋 kotlin 에서의 Getter, Setter 사용법

> 접근 제어자를 private 으로 만들어주고 해당 프로퍼티의 값을 업데이트해주는 내부 메서드를 만드는 식으로 해야되나 이런 고민도 해봤구요. 아니면 팀원들과 잘 약속해서 써야하나? 이런 고민도 해봤습니다.

- 맞습니다! 자바와 다르게 코틀린은 언어 레벨부터 지향하는 바가 다르기 때문에, 말씀하신대로 접근 제어자를 통해 private set을 지정해서 사용합니다.
값의 변경이 필요하다면 의도가 드러나는 네이밍과 함께 변경 메서드를 만들어 값을 업데이트하시면 됩니다.

- 추가적으로, DTO의 경우는 val 프로퍼티를 가진 data class를 사용해서 불변을 보장하고 있어요.

> getter 도 처음에는 자제하는게 좋다고 하셨는데, 이러면 val 도 사용을 자제해야되는 것일까요..? var, val 둘 다 되게 편하고 좋은 거 같은데 이런 걸 아예 사용 안 하는 것은 코틀린을 쓰는 의미가 크게 반감되는 거 같기도 해서, 뭔가 다른 더 좋은 방법이 있을 것 같기도 합니다.

- 자바에서 getter를 자제하라는 의미는 무분별한 getter를 지양하고, 객체에 메시지를 보내 객체 지향적인 설계를 하라, 는 의미인데요.

- 코틀린에서는 아무래도 프로퍼티의 개념(필드 + 접근자)이 도입되다 보니, 이러한 지침을 그대로 적용하기는 무리가 있습니다.
- val을 사용하면서, 객체 지향적으로 객체를 설계하고 다른 객체 프로퍼티에 무분별하게 접근하고 있지 않은지 의식적으로 체크하는 것이 필요합니다.
- private get을 사용할 수도 있겠지만, 자바에서는 getter를 내가 의도를 가지고 만들어서 외부에 열어주는 방식이고, 코틀린에서는 (val 프로퍼티를 선언한 경우) 이미 존재하는 getter를 억지로 닫는 느낌이기 때문에, 필요한 경우가 아니라면 굳이 그렇게 번거롭게 가져갈 필요는 없어보여요. :)

<br/>

<br/>

<br/>

# 💡 SOLID

> 객체 지향 설계를 더 이해하기 쉽고 유연하고 유지보수하기 쉬운 형태로 만드는데 도움을 주는 원칙들이다.

<br/>

## ⚡️ SRP(Single Responsibility Principle)

> 하나의 클래스가 하나의 원칙만 갖도록 해라.

- 하나의 클래스는 단 한가지의 **변경 이유**만을 가져야 한다.
  - `'변경 이유' = 책임`
- 객체가 가진 공개 메서드, 필드, **상수** 등은 해당 객체의 단일 책임에 의해서만 변경되는가?
  - 상수도 마찬가지로 어떤 클래스가 이 상수를 갖고 있는 것이 어울리는가, 이 상수가 누구 책임이야 하는 고민이 든다. 
  - ***객체가 가진 모든 요소들에 대해서 어떤 변경사항이 생겼을 때 해당 객체의 단일 책임으로만 변경이 되는건지 보면 된다.***
- 관심사의 분리
  - `SRP` 는 좀 더 객체 레벨, 클래스 레벨에서 강조하는 원칙이다.
  - `관심사의 분리`는 모듈 레벨, 시스템 레벨에서 많이 나오는 내용이다.
- 높은 응집도, 낮은 결합도

> **`책임`을 볼 줄 아는 눈**: 경험으로 쌓인다.

---

```java
// 게임이 진행되는 핵심 로직들과 사용자 입출력에 대한 로직 책임을 분리한다.
private final ConsoleInputHandler consoleInputHandler = new ConsoleInputHandler();
private final ConsoleOutputHandler consoleOutputHandler = new ConsoleOutputHandler();
```

```java
// BOARD 도 하는 일이 너무 많고 중요하기 때문에 Minesweeper 클래스 내부에 상수로 두기에는 너무 책임이 과도하다.
// 이렇게 GameBoard 클래스를 두면 Minesweeper 입장에서는 Cell[][] 이중배열에 대해서는 모른다.
// 객체로 추상화가 되었고, 데이터 구조에 대한 것은 캐슐화가 되었기 때문이다.
private static final GameBoard gameBoard = new GameBoard(BOARD_ROW_SIZE, BOARD_COL_SIZE);

public class GameBoard {

	private static final int LAND_MINE_COUNT = 10;

	private final Cell[][] board;

	public GameBoard(int rowSize, int colSize) {
		board = new Cell[rowSize][colSize];
	}
}
```

```java
// print 할 때 AppException 에서 어떤걸 꺼내서 쓸지는 내부에서 알아서 결정할 것이고,
// 예외 상황(exception 에 대한 메시지)에 대한 메시지를 출력하겠다는 이 메서드명을 봤을 때
// 파라미터는 exception 자체를 넣어주는 것이 더 자연스럽지 않을까 한다.
consoleOutputHandler.printExceptionMessage(e);
```

```java
// SRP: cellInput 이라는 사용자의 입력을 받아서 rowIndex, colIndex 로 변환하는 역할을 하는 또 하나의 클래스로 볼 수 있지 않을까?
private final BoardIndexConverter boardIndexConverter = new BoardIndexConverter();
```

<br/>

<br/>

## ⚡️ OCP(Open-Closed Principle)

확정에는 열려 있고, 수정에는 닫혀 있어야 한다.

> **기존 코드의 변경 없이, 시스템의 기능을 확장할 수 있어야 한다.**

- 새로운 기능(요구사항)이 추가되었을 때 기존 코드가 과도하게 변경되지 않아야 한다.
- 추상화, 다형성, Interface 등을 활용해서 OCP 를 지킬 수 있다.

<img width="500" src="https://github.com/user-attachments/assets/27c40fb8-70db-47f8-ad89-38e46c5685e6">

---

```java
// 추상화를 정말 바로 보여주는 구조이다. 인터페이스가 갖고 있는 스펙들 즉, 선언된 메서드 선언부들이 이 객체가 어떠한 역할을 갖는지 설명을 해준다.
// 이 GameLevel 인터페이스를 MineSweeper 안에 넣어줄 것이다.
// Minesweeper 객체는 GameLevel 을 받을 것이지만, 인터페이스여서 런타임 시점에 어떤 GameLevel 구현체가 들어오는지는 모른다. 하지만 GameLevel 인터페이스의 스펙은 알고 있다.
// Minesweeper 는 GameLevel 의 스펙을 통해 구현하면 된다.
public interface GameLevel {
	int getRowSize();
	int getColSize();
	int getLandMineCount();
}
```

```java
public class GameApplication {

    // 이 클래스는 딱 프로그램 실행에 진입점만 가지게 된다.
    // 이름도 MinesweeperGame 에서 GameApplication 으로 변경한다. -> 이렇게 변경하면 지뢰찾기게임(Minesweeper 뿐만이 아닌 다른 게임도 실행할 수 있게 된다.)
    // 게임 실행에 대한 책임과 지뢰찾기 도메인 자체, 지뢰찾기 게임을 담당하는 역할을 분리했다.
    public static void main(String[] args) {
        GameLevel gameLevel = new Middle();
        Minesweeper minesweeper = new Minesweeper(gameLevel);
        minesweeper.run();
    }
}
```

```java
public class Minesweeper {

	// BOARD 도 하는 일이 너무 많고 중요하기 때문에 Minesweeper 클래스 내부에 상수로 두기에는 너무 책임이 과도하다.
	// 이렇게 GameBoard 클래스를 두면 Minesweeper 입장에서는 Cell[][] 이중배열에 대해서는 모른다.
	// 객체로 추상화가 되었고, 데이터 구조에 대한 것은 캐슐화가 되었기 때문이다.
	private final GameBoard gameBoard;
	// SRP: cellInput 이라는 사용자의 입력을 받아서 rowIndex, colIndex 로 변환하는 역할을 하는 또 하나의 클래스로 볼 수 있지 않을까?
	private final BoardIndexConverter boardIndexConverter = new BoardIndexConverter();

	// 게임이 진행되는 핵심 로직들과 사용자 입출력에 대한 로직 책임을 분리한다.
	private final ConsoleInputHandler consoleInputHandler = new ConsoleInputHandler();
	private final ConsoleOutputHandler consoleOutputHandler = new ConsoleOutputHandler();
	private int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

	public Minesweeper(GameLevel gameLevel) {
		gameBoard = new GameBoard(gameLevel);
	}
}
```

**AS-IS**

```java
public class GameBoard {

	private static final int LAND_MINE_COUNT = 10;

	private final Cell[][] board;

	public GameBoard(int rowSize, int colSize) {
		board = new Cell[rowSize][colSize];
	}
}
```

**TO-BE**

```java

public class GameBoard {

	private final Cell[][] board;
	private final int landMineCount;

	public GameBoard(GameLevel gameLevel) {
		int rowSize = gameLevel.getRowSize();
		int colSize = gameLevel.getColSize();
		board = new Cell[rowSize][colSize];
		landMineCount = gameLevel.getLandMineCount();
	}
}
```

<br/>

<br/>

## ⚡️ LSP(Liskov Substitution Principle)

리스코프 치환 법칙

`치환`이라는 단어에 집중하자.

> 상속 구조에서, 부모 클래스의 인스턴스를 자식 클래스의 인스턴스로 치환할 수 있어야 한다.

<img width="500" src="https://github.com/user-attachments/assets/6328522e-7388-49f7-a7a7-8ef217102250"/>

- 자식 클래스는 부모 클래스의 책임을 준수하며, 부모 클래스의 행동을 변경하지 않아야 한다.
- LSP 를 위반하면, 상속 클래스를 사용할 때 오동작, 예상 밖의 예외가 발생하거나, 이를 방지하기 위한 불필요한 타입 체크가 동반될 수 있다.
- 부모 클래스 인스턴스가 있던 곳에 자식 클래스로 치환이 일어나더라도 문제가 없어야 한다. 
- 상속 클래스를 이용할 때 오동작, 예상하지 못한 동작이 발생하거나 혹은 예외가 발생할 수도 있기 때문에 이러한 오동작 및 예외를 감지하기 위해 특정 자식 클래스를 탐지하기 위해 불필요한 타입 체크와 같은 것들이 동반될 수 있다는 단점이 있다.
  - 따라서, 상속 구조를 사용한다면 LSP 구조를 잘 따라야 한다.

상속 구조에서 보통 자식이 할 수 있는 기능들이 더 많다.
- 달리 얘기하면, 부모가 일하는 자리에 자식이 가서 일할 수 있다.

---

<img width="500" src="https://github.com/user-attachments/assets/e3336170-2b7b-43e7-ba35-aca170a0571e">


```java
package cleancode.minesweeper.tobe.cell;

public abstract class Cell2 {

	// 하위 클래스에서도 사용할 수 있기 때문에 protected 로 변경
	protected static final String FLAG_SIGN = "⚑";
	protected static final String UNCHECKED_SIGN = "□";


	// 하위 클래스에서도 사용할 수 있도록 protected 로 변경
	protected boolean isFlagged;
	protected boolean isOpened;

	// Cell 이 가진 속성: 근처 지뢰 갯수, 지뢰 여부
	// Cell 의 상태: 깃발 유무, 열렸다/닫혔다, 사용자가 확인함

	// 정적 팩토리 메서드를 좋아하는 이유: 메서드에 이름을 줄 수 있다.
	// 정적 팩토리 메서드가 여러개가 된다면 그에 맞는 다른 이름들을 지어줄 수도 있고, 검증과 같은 로직을 추가할 수도 있다.
	// 생성자 하나인 객체라도 정적 팩토리 메서드를 만들어서 생성자를 대체해보자.

	// 지뢰와 관련된 기능
	// 그런데 구현하고 보니 LandMineCell 은 그자체로 landMine 이라는 의미를 갖고 있는데 turnOnLandMine() 으로 켜주는 것이 이상하다.
	// 그리고 해당 기능 때문에 다른 자식 클래스인 EmptyCell, NumberCell 에서는 UnsupportedOperationException 을 던지고 있다.
	// 따라서 해당 기능들을 지워야 한다.
//	public abstract void turnOnLandMine();

	// 이것도 특정 셀에서만 유효하다.
	// 이것도 위와 마찬가지로 NumberCell 에서는 그 자체로 count 를 필드로 갖고 있어야 하지 메서드로 조정할 것이 아니다.
	// 메서드로 조정하다보니 다른 자식 클래스에서 UnsupportedOperationException 을 던지고 있다.
	// 따라서 해당 기능들을 지워야 한다.
//	public abstract void updateNearbyLandMineCount(int count);


	// isOpened, isFlagged 는 Cell 의 공통 기능이므로 그대로 둔다.
	public void flag() {
		this.isFlagged = true;
	}

	public void open() {
		this.isOpened = true;
	}

	public boolean isChecked() {
		return isFlagged || isOpened;
	}

	// 이것도 특정 셀에서만 유효하다.
	public abstract boolean isLandMine();

	public boolean isOpened() {
		return isOpened;
	}

	public abstract boolean hasLandMineCount();

	public abstract String getSign();
}
```

---

**AS-IS**

```java
package cleancode.minesweeper.tobe.cell;

public class LandMineCell extends Cell2{

	private static final String LAND_MINE_SIGN = "☼";

	private boolean isLandMine;

	@Override
	public void turnOnLandMine() {
		this.isLandMine = true;
	}

	@Override
	public void updateNearbyLandMineCount(int count) {
		throw new UnsupportedOperationException("지원하지 않는 기능입니다.");
	}

	@Override
	public boolean isLandMine() {
		return true;
	}

	@Override
	public boolean hasLandMineCount() {
		return false;
	}

	@Override
	public String getSign() {
		if (isOpened) {
			return LAND_MINE_SIGN;
		}
		if (isFlagged) {
			return FLAG_SIGN;
		}

		return UNCHECKED_SIGN;
	}
}
```

**TO-BE**

```java
package cleancode.minesweeper.tobe.cell;

public class LandMineCell extends Cell2{

	private static final String LAND_MINE_SIGN = "☼";

	@Override
	public boolean isLandMine() {
		return true;
	}

	@Override
	public boolean hasLandMineCount() {
		return false;
	}

	@Override
	public String getSign() {
		if (isOpened) {
			return LAND_MINE_SIGN;
		}
		if (isFlagged) {
			return FLAG_SIGN;
		}

		return UNCHECKED_SIGN;
	}
}
```

---

**AS-IS**

```java
package cleancode.minesweeper.tobe.cell;

public class NumberCell extends Cell2{

	private int nearbyLandMineCount;

	public NumberCell(int nearbyLandMineCount) {
		this.nearbyLandMineCount = nearbyLandMineCount;
	}

	@Override
	public void turnOnLandMine() {
		throw new UnsupportedOperationException("지원하지 않는 기능입니다.");
	}

	@Override
	public void updateNearbyLandMineCount(int count) {
		this.nearbyLandMineCount = count;
	}

	@Override
	public boolean isLandMine() {
		return false;
	}

	@Override
	public boolean hasLandMineCount() {
		return true;
	}

	@Override
	public String getSign() {
		if (isOpened) {
			return String.valueOf(nearbyLandMineCount);
		}
		if (isFlagged) {
			return FLAG_SIGN;
		}

		return UNCHECKED_SIGN;
	}
}
```

**TO-BE**

```java
package cleancode.minesweeper.tobe.cell;

public class NumberCell extends Cell2{

	private final int nearbyLandMineCount;

	public NumberCell(int nearbyLandMineCount) {
		this.nearbyLandMineCount = nearbyLandMineCount;
	}

	@Override
	public boolean isLandMine() {
		return false;
	}

	@Override
	public boolean hasLandMineCount() {
		return true;
	}

	@Override
	public String getSign() {
		if (isOpened) {
			return String.valueOf(nearbyLandMineCount);
		}
		if (isFlagged) {
			return FLAG_SIGN;
		}

		return UNCHECKED_SIGN;
	}
}
```

---

**AS-IS**

```java
package cleancode.minesweeper.tobe.cell;

public class EmptyCell extends Cell2{

	private static final String EMPTY_SIGN = "■";

	@Override
	public void turnOnLandMine() {
		throw new UnsupportedOperationException("지원하지 않는 기능입니다.");
	}

	@Override
	public void updateNearbyLandMineCount(int count) {
		throw new UnsupportedOperationException("지원하지 않는 기능입니다.");
	}

	@Override
	public boolean isLandMine() {
		return false;
	}

	@Override
	public boolean hasLandMineCount() {
		return false;
	}

	@Override
	public String getSign() {
		if (isOpened) {
			return EMPTY_SIGN;
		}
		if (isFlagged) {
			return FLAG_SIGN;
		}
		return UNCHECKED_SIGN;
	}
}
```

**TO-BE**

```java
package cleancode.minesweeper.tobe.cell;

public class EmptyCell extends Cell2{

	private static final String EMPTY_SIGN = "■";

	@Override
	public boolean isLandMine() {
		return false;
	}

	@Override
	public boolean hasLandMineCount() {
		return false;
	}

	@Override
	public String getSign() {
		if (isOpened) {
			return EMPTY_SIGN;
		}
		if (isFlagged) {
			return FLAG_SIGN;
		}
		return UNCHECKED_SIGN;
	}
}
```

<br/>

<br/>

<br/>

## ⚡️ ISP(Interface Segregation Principle)

클라이언트는 자신이 사용하지 않는 인터페이스에 의존하면 안된다.
- 인터페이스를 잘게 쪼개라!

`ISP`를 위반하면 하나의 인터페이스에 여러 기능들이 몰려 있게 되고 그렇게 되면 불필요한 의존성으로 인해 결합도가 높아지고, 특정 기능의 변경이 여러 클래스에 영향을 미칠 수 있다. 
- 예를 들어, 메서드의 시그니처가 변경될 경우에 그 메서드 시그니처를 구현하는 모든 클래스에 영향이 간다.

> **인터페이스를 기능 단위로 잘게 쪼개서 사용하자!**

<img width="500" src="https://github.com/user-attachments/assets/f1ffa2a6-d50b-4d54-b23b-934cd47cb051">

<img width="500" src="https://github.com/user-attachments/assets/0d376538-de3b-4de8-b373-79500c1592d1">

---

**AS-IS**

```java
public interface Game {
	void initialize();
	void run();
}
```

```java
public class AnotherGame implements Game{
	@Override
	public void initialize() {
		// 해당 게임이 초기화가 필요 없는 경우가 있을 수 있다.
		// initialize 의 메서드 시그니처가 변경될 경우 initialize 메서드가 필요 없는 AnotherGame 도 그에 맞게 변경이 필요하게 되는 경우가 발생한다.
		// 이런 경우 ISP 에 위배된다.
	}

	@Override
	public void run() {
		// 게임 진행
	}
}
```


**TO-BE**

```java
public interface GameInitializable {
	void initialize();
}
```

```java
public interface GameRunnable {
	void run();
}
```

```java
public class AnotherGame implements GameRunnable {
	@Override
	public void run() {
		// 게임 진행
	}
}
```

```java
public class Minesweeper implements GameInitializable, GameRunnable {
	...
  
	@Override
	public void initialize() {
		gameBoard.initializeGame();
	}

	@Override
	public void run() {
      ...
	}
}
```

<br/>

<br/>

## ⚡️ DIP(Dependency Inversion Principle)

> **상위 수준의 모듈은 하위 수준의 모듈에 의존해서는 안된다. 둘 모두 추상화에 의존해야 한다.**
> - 추상화 레벨이 높은 모듈이 구체 쪽에 가까운 모듈에 직접적으로 의존해서는 안된다!

- `의존성의 순방향`: **고수준 모듈이 저수준 모듈을 참조하는 것**
  - **저수준 모듈은 구체 쪽에 가깝기 때문에 어떤 기능을 구현하는데에 있어서 자주 바뀔 수 있다는 문제점이 있다.**
- `의존성의 역방향`: **고수준, 저수준 모듈이 모두 추상화에 의존하는 것**
  - 이 자연스로운 의존성을 역전시켜서, 고수준 모듈이 인터페이스와 같은 추상화된 스펙만 참조하는 것이다.
  - 고수준 모듈은 런타임 시점의 구현체와는 상관없이 추상화된 스펙만 알고 그것만 만족하면 되는 것이다.
  - 저수준 모듈도 이 인터페이스를 구현한 구현체이고, 얼마든지 갈아끼워질 수 있다.
  - **서로가 직접적으로 의존하지 않고, 추상화를 중간에 두고 의존하게 하는 것이 의존성 역전이다.**
  - 저수준 모듈이 변경되어도, 고수준 모듈에는 영향이 가지 않는다.
    - `고수준`: 추상화 레벨이 높은 것,
    - `저수준`: 추상화 레벨이 낮은 것, 구체(구현체) 쪽에 가까운 것
    - `의존성`: 하나의 모듈이 다른 하나의 모듈을 `알고 있거나`, `사용하거나`, `직접적으로 생성하는` 모든 것들
    - `순방향`: 자연스럽다는 의미

<img width="500" src="https://github.com/user-attachments/assets/c028226f-d240-4289-a6aa-b510588baebc">

---

### 🔋 DIP(Dependency Inversion Principle), DI(Dependency Injection), IoC(Inversion of Control)

`DI`는 `의존성 주입`이다.
- 의존성 주입, 필요한 의존성을 내가 직접 주입하는 것이 아닌 외부에서 주입해주는 것이다.
- `제 3자`가 항상 두 객체간의 의존성을 주입시켜준다.
- 스프링에서는 스프링 컨텍스트(IoC 컨테이너)가 이 역할을 한다.
- 런타입 시점에 일어난다.

`IoC`는 `제어의 역전`이다.
- 스프링에서만 사용되는 개념은 아니다.
- 제어의 순방향은 프로그램은 개발자가 만드는 것이므로 개발자가 프로그램을 제어해야 순방향 즉, 자연스러운 일이다.
- 제어의 역전(역방향)은 개발자가 프로그램을 제어하는 것이 아닌 프로그램(프레임워크)이 개발자가 만든 코드를 제어하는 것이다.
- 프레임워크가 메인이 되고 개발자가 만든 코드를 프레임워크가 제어하는 것이다.
- IoC 컨테이너는 객체의 생성, 소멸, 의존성 주입, 생명 주기를 담당하고, 객체는 생성된 객체를 사용만 하는 것이다.

---

```java
public interface InputHandler {
	String getUserInput();
}
```

```java
public interface OutputHandler {

	// show, print 중에 show 는 추상적인 느낌이고, print 는 구체적인 느낌이 강하다.
	// print 는 console 에 print 한다는 느낌이 강하다.
	// 높은 추상화 레벨인 OutputHandler 입장에서는 print 보다는 show 가 더 낫겠다.
	void showGameStartComments();
	void showBoard(GameBoard board);
	
	void showPrintGameWinningComment();
	void showGameLosingComment();
	void showCommentForSelectingCell();
	void showCommentForUserAction();
	void showExceptionMessage(GameException e);
	void showSimpleMessage(String message);
	
//	void printGameWinningComment();
//	void printGameLosingComment();
//	void printCommentForSelectingCell();
//	void printCommentForUserAction();
//	void printExceptionMessage(GameException e);
//	void printSimpleMessage(String message);
}
```

```java
public class ConsoleOutputHandler implements OutputHandler {

	@Override
	public void showGameStartComments() {
		...
	}
	
	...
}
```

```java
public class ConsoleInputHandler implements InputHandler {
	public static final Scanner SCANNER = new Scanner(System.in);

	@Override
	public String getUserInput() {
		...
	}
}
```

```java
public class Minesweeper implements GameInitializable, GameRunnable {
	// 게임이 진행되는 핵심 로직들과 사용자 입출력에 대한 로직 책임을 분리한다.
	// DIP: InputHandler, OutputHandler 는 이제 Console 에 관한 것은 모른다. 인터페이스만 의존하고 있다.
	// 구현체가 변경되어도 Minesweeper 클래스는 영향을 받지 않는다.
	private final InputHandler inputHandler;
	private final OutputHandler outputHandler;

	public Minesweeper(GameLevel gameLevel, InputHandler inputHandler, OutputHandler outputHandler) {
		gameBoard = new GameBoard(gameLevel);
		this.inputHandler = inputHandler;
		this.outputHandler = outputHandler;
	}
```

```java
public class GameApplication {

    // 이 클래스는 딱 프로그램 실행에 진입점만 가지게 된다.
    // 이름도 MinesweeperGame 에서 GameApplication 으로 변경한다. -> 이렇게 변경하면 지뢰찾기게임(Minesweeper 뿐만이 아닌 다른 게임도 실행할 수 있게 된다.)
    // 게임 실행에 대한 책임과 지뢰찾기 도메인 자체, 지뢰찾기 게임을 담당하는 역할을 분리했다.
    public static void main(String[] args) {
        GameLevel gameLevel = new Middle();
        InputHandler inputHandler = new ConsoleInputHandler();
        ConsoleOutputHandler outputHandler = new ConsoleOutputHandler();

        Minesweeper minesweeper = new Minesweeper(gameLevel, inputHandler, outputHandler);
        minesweeper.initialize();
        minesweeper.run();
    }
}
```

<br/>

<br/>

<br/>

---

> Post(게시글)을 작성한다고 가정 하면 PostService에는 UserRepository를 사용하여 User객체를 얻어야 하는데, 이러한 경우 단일 책임 원칙 위반이라고 얘기할 수 있는 건가요?
댓글을 작성한다고 가정하면, CommentService에 UserRepository, PostRepository를 사용하여 User와 Post 객체를 얻어야 합니다.

이 경우 PostService와 CommentService가 담당하는 책임은 각각 무엇일까요?
PostService라 Post Entity만 사용해야 할 것 같은데, User를 사용하게 되어 단일 책임이 아니게 된걸까요?

그렇지 않습니다. PostService는 '게시글 작성'의 책임, CommentService는 '댓글 작성'의 책임을 가지는 것이고, 이 과정에서 UserRepository를 통해 User 받아 사용하게 되는 현상은 매우 자연스럽습니다.
다른 도메인이 침투한 것이 아니라, 도메인 객체 간 협력이 일어나는 장소가 Service라고 생각하시면 좋습니다.

물론, PostService에서 User의 정보를 직접적으로 변경한다거나 하는 필요 이상의 접근이 생길 경우는 책임이 위반되었다고 볼 수도 있습니다.
이런 경우는 User를 변경하는 일을 맡고 있는 다른 객체에 잘 위임하여 관련 행위를 수행하도록 해야 합니다.

---

> 코드 작성 시 SOLID 원칙을 처음부터 준수해가며 작성하시는지, 아니면 개발 후 리팩토링을 통해 점진적으로 변경해 나가시는 편인지 궁금합니다.

SOLID 원칙을 이해하고 적용하는 것은 중요하지만, 코드를 작성할 때 원칙을 우선 시 하는 것은 권장하지 않습니다.
코드를 작성할 때에는, 원칙에 앞서 "도메인 이해" 과정이 가장 중요한데요.

한 번에 모든 원칙을 다 지키는 완벽한 코드, 완벽한 설계를 하기란 사실 불가능에 가깝습니다.
처음 작성하는 코드는 도메인에 대한 이해가 부족할 가능성이 높기에, 얕은 지식을 기반으로 편향적 사고를 하기가 쉽습니다. (그리고 자연스러운 과정입니다.)
더 나은 설계가 존재한다는 사실을 깨닫는 시점은 코드를 작성하는 후반부, 작성이 완료된 이후, 혹은 시간이 한참 지나서 다른 기능이 추가되었을 때 등 내가 작성한 코드를 다른 시각으로 바라볼 때인데요.
이때도 '모든 원칙을 반드시 완벽하게 적용해야지!' 하는 접근보다, 이해한 도메인의 특성을 고려하여 객체의 역할과 책임을 재설정하고, 리팩토링하고, 그에 맞게 필요한 원칙이 있다면 적용하겠다 라는 접근이 더 낫습니다.
때로는 트레이드오프로 원칙을 위배하는 방식을 선택하는 경우도 있을 수 있습니다. (성능 등의 이유로)

정리하면, 원칙은 더 나은 설계를 찾기 위한 도구라고 보시면 되고요.
내가 해결하려고 하는 문제상황, 도메인에 집중하는 것이 가장 우선이라고 이해하시면 되겠습니다.
