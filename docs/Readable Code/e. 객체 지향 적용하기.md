# 객체 지향 적용하기

# 💡 상속과 조합

> `상속(Inheritance)`: 부모와 자식 간의 어떤 부모의 기능을 자식이 그대로 가져가면서 추가적으로 뭔가를 더하는 그런 설계 구조

> `조합(Composition)`: 하나의 객체가 자신이 필요한 객체를 가져다가 필드에 선언하거나 해서 사용하는 구조

- 상속보다 조합을 사용하자
- 상속은 시멘트처럼 굳어지는 구조다. 수정이 어렵다.
  - 부모와 자식의 결합도가 높아진다.
  - 부모의 모든 행위와 기능을 자식들이 전부 다 알고 있어야 한다.
  - 부모에 변경이 일어나면 자식들 모두에 영향을 미친다.
- **조합과 인터페이스를 활용하는 것이 훨씬 유연한 구조!!!**
  - 상속을 통한 코드의 중복 제거가 주는 이점보다, **어떤 조합이나 인터페이스를 사용하여 유지보수하기 쉬운 중복이 생기더라도 유연한 구조 설계가 주는 이점이 더 크다.**
  - 예전에는 하드웨어 성능이 떨어졌기 때문에 상속을 통한 코드의 중복제거가 중요했지만, 지금은 아니다!!!
- **그렇다고 상속을 무조건 사용하지 말자는건 아니다. 상속을 사용해야만 효율이 나고 이 구조는 앞으로도 정말 변하지 않을 것 같을 때만 상속을 사용하자.**

**AS-IS**

```java
public abstract class Cell {
	// 하위 클래스에서도 사용할 수 있기 때문에 protected 로 변경
	protected static final String FLAG_SIGN = "⚑";
	protected static final String UNCHECKED_SIGN = "□";

	// 하위 클래스에서도 사용할 수 있도록 protected 로 변경
	protected boolean isFlagged;
	protected boolean isOpened;

	// 이것도 특정 셀에서만 유효하다.
	public abstract boolean isLandMine();

	public abstract boolean hasLandMineCount();

	public abstract String getSign();
}
```

```java
public class EmptyCell extends Cell {

	private static final String EMPTY_SIGN = "■";

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

- EmptyCell 클래스는 부모 클래스인 Cell 클래스의 isFlagged, isOpened 필드를 그대로 `상속`을 받아서 사용하고 있다.
  - **결합도가 엄청 높다!!!** 
  - 부모 클래스에 조금의 변경사항이 생기면 모든 자식들에 수정사항이 생긴다.
  - **캡슐화가 깨졌다고 봐도 된다!**

**TO-BE**

**CellState 클래스라는 별도의 책임이 분리가 됐다. CellState 와의 소통은 CellState 의 메서드를 통해서만 이루어지기 때문에, 각 Cell(Cell, EmptyCell, ...) 에서는 CellState 의 2개의 field인 isFlagged, isOpened 를 어떻게 관리하는지는 상관이 없어졌다.**
- 이렇게 분리가 되는 순간, 상속 구조 보다 훨씬 유연하고 이점이 많아졌다. 

```java
public interface Cell {

	// 하위 클래스에서도 사용할 수 있기 때문에 protected 로 변경
	String FLAG_SIGN = "⚑";
	String UNCHECKED_SIGN = "□";

	boolean isLandMine();
	boolean hasLandMineCount();
	String getSign();

	// isOpened, isFlagged 는 Cell 의 공통 기능이므로 그대로 둔다.
	void flag();

	void open();

	boolean isChecked();

	boolean isOpened();
}
```

```java
public class CellState {
	private boolean isFlagged;
	private boolean isOpened;

	private CellState(boolean isFlagged, boolean isOpened) {
		this.isFlagged = isFlagged;
		this.isOpened = isOpened;
	}

	public static CellState initialize() {
		return new CellState(false, false);
	}

	// isOpened, isFlagged 는 Cell 의 공통 기능이므로 그대로 둔다.
	public void flag() {
		this.isFlagged = true;
	}

	public void open() {
		this.isOpened = true;
	}
}

```

```java
public class EmptyCell implements Cell {

  private static final String EMPTY_SIGN = "■";

  private final CellState cellState = CellState.initialize();

  @Override
  public String getSign() {
    if (cellState.isOpened()) {
      return EMPTY_SIGN;
    }
    if (cellState.isFlagged()) {
      return FLAG_SIGN;
    }
    return UNCHECKED_SIGN;
  }

  @Override
  public void flag() {
    cellState.flag();
  }

  @Override
  public void open() {
    cellState.open();
  }

  @Override
  public boolean isChecked() {
    return cellState.isChecked();
  }

  @Override
  public boolean isOpened() {
    return cellState.isOpened();
  }
}
```

---

# 💡Value Object

> `Value Object`: **도메인**의 어떤 개념을 추상화하여 표현한 `값 객체`

- ***값*으로 취급하기 위해서 불변성, 동등성, 유효성 검증 등을 보장해야 한다.**
  - 불변성: final 필드, setter 금지
  - 동등성: 서로 다른 인스턴스여도(동일성이 달라도), 내부의 값이 같으면 같은 값 객체로 취급한다.
    - `equals()`, `hashCode()` 메서드 오버라이딩 필요
  - 유효성 검증: 객체가 생성되는 시점에 값에 대한 유효성을 보장하기
    - **값 객체가 가지고 있는 필드가 최소한의 보장 조건이 필요한 도메인 개념을 가지고 있다면**, 이를 객체 생성 시점에 검증해야 한다.
  
EX) 만원짜리 지폐에는 `일련번호`가 있고 그 일련번호로 지폐를 구별할 수 있지만, **우리는 일련번호가 달라도 다 같은 만원짜리 지폐라고 여긴다.**
- 여기서 일련번호는 `메모리 주소`라고 생각할 수 있다.

`값 객체`는 **우리 도메인에서 `단위 객체`가 될 수 있다.**
- Int, Long, Stream 같인 기본 타입들처럼 Money 라는 것을 **우리 도메인에서 기본 타입처럼 쓸거야** 라는 의미로 볼 수 있다.
- 그게 Value Object 의 의미다.

EX) Money 라는 Value Object 를 만들어보자.

```java
public class Money { 
	private final int value;
	
	public Money(int value) {
		if (value < 0) {
			throw new IllegalArgumentException("금액은 0원 이상이어야 합니다.");
        }
		this.value = value;
	}
	
	@Override
    public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Money money = (Money) o;
		return value == money.value;
	}
	
	@Override
    public int hashCode() {
		return Objects.hash(value);
	}
}
```

```java
Money money1 = new Money(1_000L);
Money money2 = new Money(1_000L);

assertThat(money1 == money2).isFalse();
assertThat(money1.equals(money2)).isTrue();
```

## ⚡️ VO vs Entity

`Entity`는 ***식별자가 존재한다.*** 식별자가 아닌 필드의 값이 달라도, **식별자가 같으면 동등한 객체로 취급한다.**
- equals() & hashCode() 도 식별자 필드만 가지고 재정의할 수 있다.
- 식별자가 같은데 식별자가 아닌 필드의 값이 서로 다른 두 인스턴스가 있다면, 같은 Entity가 시간이 지남에 따라 변화한 것으로 이해할 수도 있다.

`VO`는 ***식별자 없이, 내부의 모든 값이 다 같아야 동등한 객체로 취급한다.***
- 개념적으로, 전체 필드가 다같이 식별자 역할을 한다고 생각해도 된다.

**EX) Entity, VO 비교**

```java
@Entity
class UserAccount {
	private String userId; // 식별자
    private String password;
    private String email;
    private String phoneNumber;
    private Address 집주소;
}
```

```java
@VO
class Address {
	private String city;
    private String street;
    private String zipcode;
}
```

`rowIndex`, `columnIndex`가 각각 있을 때는 아무 의미가 없다. 항상 같이 있어야 어떠한 의미를 가지고 로직을 처리하게 된다.    \
그렇다면, `rowIndex`와 `columnIndex` 를 **함께 묶은** `값 객체(VO)`를 만들어보자!!!

```java
public class CellPosition {
	private final int rowIndex;
	private final int colIndex;

	private CellPosition(int rowIndex, int colIndex) {
		if (rowIndex < 0 || colIndex < 0) {
			throw new IllegalArgumentException("올바르지 않은 좌표입니다.");
		}
		this.rowIndex = rowIndex;
		this.colIndex = colIndex;
	}

	public static CellPosition of(int rowIndex, int colIndex) {
		return new CellPosition(rowIndex, colIndex);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		CellPosition that = (CellPosition) object;
		return rowIndex == that.rowIndex && colIndex == that.colIndex;
	}

	@Override
	public int hashCode() {
		return Objects.hash(rowIndex, colIndex);
	}

	public boolean isRowIndexMoreThanOrEqual(int rowIndex) {
		return this.rowIndex >= rowIndex;
	}

	public boolean isColIndexMoreThanOrEqual(int colIndex) {
		return this.colIndex >= colIndex;
	}

	public int getRowIndex() {
		return this.rowIndex;
	}

	public int getColIndex() {
		return this.colIndex;
	}
	
  ...
}
```

```java
public class RelativePosition {
	public static final List<RelativePosition> SURROUNDED_POSITIONS = List.of(
		RelativePosition.of(-1, -1),
		RelativePosition.of(-1, 0),
		RelativePosition.of(-1, -1),
		RelativePosition.of(0, -1),
		RelativePosition.of(0, 1),
		RelativePosition.of(1, -1),
		RelativePosition.of(1, 0),
		RelativePosition.of(1, 1)
	);

	private final int deltaRow;
	private final int deltaCol;

	private RelativePosition(int deltaRow, int deltaCol) {
		this.deltaRow = deltaRow;
		this.deltaCol = deltaCol;
	}

	public static RelativePosition of(int deltaRow, int deltaCol) {
		return new RelativePosition(deltaRow, deltaCol);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		RelativePosition that = (RelativePosition) object;
		return deltaRow == that.deltaRow && deltaCol == that.deltaCol;
	}

	@Override
	public int hashCode() {
		return Objects.hash(deltaRow, deltaCol);
	}

	public int getDeltaRow() {
		return this.deltaRow;
	}

	public int getDeltaCol() {
		return this.deltaCol;
	}
}
```

```java
public class CellSnapshot {
	private final CellSnapshotStatus status;
	private final int nearbyLandMineCount;

	private CellSnapshot(CellSnapshotStatus status, int nearbyLandMineCount) {
		this.status = status;
		this.nearbyLandMineCount = nearbyLandMineCount;
	}

	public static CellSnapshot of(CellSnapshotStatus status, int nearbyLandMineCount) {
		return new CellSnapshot(status, nearbyLandMineCount);
	}

	public static CellSnapshot ofEmpty() {
		return new CellSnapshot(CellSnapshotStatus.EMPTY, 0);
	}

	public static CellSnapshot ofFlag() {
		return new CellSnapshot(CellSnapshotStatus.FLAG, 0);
	}

	public static CellSnapshot ofLandMine() {
		return new CellSnapshot(CellSnapshotStatus.LAND_MINE, 0);
	}

	public static CellSnapshot ofNumber(int nearbyLandMineCount) {
		return new CellSnapshot(CellSnapshotStatus.NUMBER, nearbyLandMineCount);
	}

	public static CellSnapshot ofUnchecked() {
		return new CellSnapshot(CellSnapshotStatus.UNCHECKED, 5);
	}

	public CellSnapshotStatus getStatus() {
		return status;
	}

	public int getNearbyLandMineCount() {
		return nearbyLandMineCount;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		CellSnapshot that = (CellSnapshot) object;
		return getNearbyLandMineCount() == that.getNearbyLandMineCount() && getStatus() == that.getStatus();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getStatus(), getNearbyLandMineCount());
	}
}
```

---

# 💡 일급 컬렉션

> `일급 시민(First Class Citizen)`: 다른 요소에게 사용 가능한 모든 연산을 지원하는 요소
- 변수로 할당될 수 있다.
- 파라미터로 전달될 수 있다.
- 함수의 결과로 반환될 수 있다.

> `일급 함수`: 함수형 프로그래밍 언어에서, 함수는 일급 시민이다.
- 함수는 변수에 할당할 수 있고, 다른 함수에 전달할 수 있으며, 함수의 결과로 함수가 반환될 수 있다.

> 일급 컬렉션: 컬렉션을 포장하면서, 컬렉션만을 유일하게 필드로 가지는 객체
- 컬렉션을 다른 객체와 동등한 레벨로 다루기 위함
- 단 하나의 컬렉션 필드만을 가진다.

**컬렉션을 추상화하며 의미를 담을 수 있고, 가공 로직의 보금자리가 생긴다.**
- 가공 로직에 대한 테스트도 작성할 수 있다.
- 컬렉션이 그 자체로 혼자 돌아다니면 가공 로직, 여러 비즈니스 로직 의미를 담을 수 없다.
- ***컬렉션을 VO 처럼 한번 감싸서 가공 로직을 그 객체 안에 넣는 것이다!***
- 가공 로직을 위한 테스트 코드도 작성할 수 있다!

**만약 getter로 컬렉션을 반환할 일이 생긴다면, 외부 조작을 피하기 위해 꼭 새로운 컬렉션으로 만들어서 반환해주자.**

EX) CreditCards 라는 일급 컬렉션을 만들어보자.

```java
class CreditCards {
	private final List<CreditCard> cards;

    // 생성자

    public List<CreditCard> findValidCards() {
        return this.cards.stream()
            .filter(CreditCard::isValid)
            .toList();
    }
}
```

**TO-BE**

```java
public class Cells {
	private final List<Cell> cells;

	private Cells(List<Cell> cells) {
		this.cells = cells;
	}

	public static Cells of(List<Cell> cells) {
		return new Cells(cells);
	}

	public static Cells from(Cell[][] cells) {
		List<Cell> cellList = Arrays.stream(cells)
			.flatMap(Arrays::stream)
			.toList();
		return of(cellList);
	}

	public boolean isAllChecked() {
		return cells.stream()
			.allMatch(Cell::isChecked);
	}
}
```

```java
public class CellPositions {
	private final List<CellPosition> positions;

	private CellPositions(List<CellPosition> positions) {
		this.positions = positions;
	}

	public static CellPositions of(List<CellPosition> positions) {
		return new CellPositions(positions);
	}

	public static CellPositions from(Cell[][] board) {
		List<CellPosition> cellPositions = new ArrayList<>();

		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				CellPosition cellPosition = CellPosition.of(row, col);
				cellPositions.add(cellPosition);
			}
		}

		return of(cellPositions);
	}

	public List<CellPosition> extractRandomPositions(int count) {
		List<CellPosition> cellPositions = new ArrayList<>(positions);

		Collections.shuffle(cellPositions);
		return cellPositions.subList(0, count);
	}

	public List<CellPosition> subtract(List<CellPosition> positionListToSubtract) {
		List<CellPosition> cellPositions = new ArrayList<>(positions);
		CellPositions positionsToSubtract = CellPositions.of(positionListToSubtract);

		return cellPositions.stream()
			.filter(positionsToSubtract::doesNotContain)
			.toList();
	}

	private boolean doesNotContain(CellPosition position) {
		return !positions.contains(position);
	}

	public List<CellPosition> getPositions() {
		return new ArrayList<>(positions);
	}
}
```

---

# 💡 Enum 의 특성과 활용

> Enum: Enum 은 상수의 집합이며, 상수와 관련된 로직을 담을 수 있는 공간이다.

**상태와 행위를 한 곳에서 관리할 수 있는 추상화된 객체**

- **특정 도메인 개념에 대해 그 종류와 기능을 명시적으로 표현해줄 수 있다.**
- **만약 변경이 정말 잦은 개념은, Enum 보다 DB 로 관리하는 것이 나을 수 있다.**

**Enum 을 만들 때는 description 이라는 필드를 추가해서, Enum 의 설명을 추가하자!!!**

**TO-BE**

```java
public enum UserAction {
	OPEN("셀 열기"), FLAG("깃발 꽂기"), UNKNOWN("알 수 없음");

	private final String description;

	UserAction(String description) {
		this.description = description;
	}
}
```

```java
public enum CellSnapshotStatus {
	EMPTY("빈 셀"),
	FLAG("깃발"),
	LAND_MINE("지뢰"),
	NUMBER("숫자"),
	UNCHECKED("확인 전")
	;

	private final String description;

	CellSnapshotStatus(String description) {
		this.description = description;
	}
}
```

---

# 💡 다형성 활용하기

### 🔋 반복적인 if 문을 단순하게 만들어볼 수 없을까?

> 어떤 `조건`을 만족하면, 그 조건에 해당하는 `행위`를 수행한다.

<img width="500" src="https://github.com/user-attachments/assets/ebe86504-f0e6-4cb2-ab0a-1e2f52e3715e" alt="">

<img width="500" src="https://github.com/user-attachments/assets/ee833818-2440-4cc5-962d-604a426f0b6b" alt="">

**_OCP 를 적용해보자_**
- **변화하는 것** > ***구체***
- **스펙의 하위 구현체들이 조건과 행위를 바꿔가면서 이 추상화된 스펙을 만족시키고 있다.**
  - **조건 & 행위**
- **변하지 않는 것** > ***추상***
- **어떤 스펙, 상위 레벨의, 추상화 레빌이 높은 스펙이다.**
  1. 조건을 만족하는가?
  2. 행위를 수행한다.

<img width="700" src="https://github.com/user-attachments/assets/a4a5f5c2-5bea-49d8-a884-c7dd1e19ba76" alt="">

**AS-IS**

<img width="500" src="https://github.com/user-attachments/assets/fdfe869e-cb2b-41d4-81f1-17d05ad2257d" alt="">

```java
private static String decideCellSignFrom(CellSnapshot snapshot) {
		CellSnapshotStatus status = snapshot.getStatus();
		if (status == CellSnapshotStatus.EMPTY) {
			return EMPTY_SIGN;
		}
		if (status == CellSnapshotStatus.FLAG) {
			return FLAG_SIGN;
		}
		if (status == CellSnapshotStatus.LAND_MINE) {
			return LAND_MINE_SIGN;
		}
		if (status == CellSnapshotStatus.NUMBER) {
			return String.valueOf(snapshot.getNearbyLandMineCount());
		}
		if (status == CellSnapshotStatus.UNCHECKED) {
			return UNCHECKED_SIGN;
		}
		throw new IllegalArgumentException("확인할 수 없는 셀입니다.");
	}
```

**TO-BE**

<img width="500" src="https://github.com/user-attachments/assets/d2252470-7296-44fa-9aa4-042f5cffb679" alt="">

- `CellSignProvidable`: CellSign 을 제공할 수 있는 `스펙` 을 만들었다. -> **추상화**
  1. `조건`: `boolean supports(...)` 을 만족하는가?
  2. `행위`: `String provide(...)` 를 제공
- 각각의 EmptyCellSignProvider, FlagCellSignProvider, ... 를 따로 놓지 않고 CellSignFinder 를 통해 관리한다.

```java
public interface CellSignProvidable {
	boolean supports(CellSnapshot cellSnapshot);
	String provide(CellSnapshot cellSnapshot);
}
```

```java
public class EmptyCellSignProvider implements CellSignProvidable{

	private static final String EMPTY_SIGN = "■";

	@Override
	public boolean supports(CellSnapshot cellSnapshot) {
		return cellSnapshot.isSameStatus(CellSnapshotStatus.EMPTY);
	}

	@Override
	public String provide(CellSnapshot cellSnapshot) {
		return EMPTY_SIGN;
	}
}
```

```java
public class CellSignFinder {
	public static final List<CellSignProvidable> CELL_SIGN_PROVIDERS = List.of(
		new EmptyCellSignProvider(),
		new FlagCellSignProvider(),
		new LandMineCellSignProvider(),
		new NumberCellSignProvider(),
		new UncheckedCellSignProvider()
	);

	public String findCellSignFrom(CellSnapshot snapshot) {
		return CELL_SIGN_PROVIDERS.stream()
			.filter(provider -> provider.supports(snapshot))
			.findFirst()
			.map(provider -> provider.provide(snapshot))
			.orElseThrow(() -> new IllegalArgumentException("확인할 수 없는 셀입니다."));
	}
}
```

```java
String cellSign = cellSignFinder.findCellSignFrom(snapshot);
```

#### ⚛️ 하지만 이렇게 하면, CellSnapshotStatus 가 추가될 때마다 CellSignFinder 에도 추가해야 한다!!!

- 상태가 추가될 때 마다 구현체(xxxCellSignProvider)도 추가해야 하고, `CellSignFinder`에도 등록해야 한다.**

---

### 🔋 Enum 을 활용해보자!!!

> `CellSignProvider` 라는 `Enum` 으로 만들어보자!!!

1. 조건을 만족하는지 확인하고 행위를 수행하는 역할을 하는 메서드를 만든다.
2. 조건과 행위를 각각 Enum 으로 하나씩 다 갖고 있다.
- `CellSignProvider Enum` 자체가 `interface` 를 구현함으로써 **구현체도 갖고 있고, 추상화된 스펙도 같이 갖고 있는 형태이다.**

<img width="500" src="https://github.com/user-attachments/assets/aef86a87-a9ba-4f6a-889b-0ff2289d6946" alt="">

```java
public enum CellSignProvider implements CellSignProvidable{
	EMPTY(CellSnapshotStatus.EMPTY) {
		@Override
		public String provide(CellSnapshot cellSnapshot) {
			return EMPTY_SIGN;
		}
	},
	FLAG(CellSnapshotStatus.FLAG) {
		@Override
		public String provide(CellSnapshot cellSnapshot) {
			return FLAG_SIGN;
		}
	},
	LAND_MINE(CellSnapshotStatus.LAND_MINE) {
		@Override
		public String provide(CellSnapshot cellSnapshot) {
			return LAND_MINE_SIGN;
		}
	},
	NUMBER(CellSnapshotStatus.NUMBER) {
		@Override
		public String provide(CellSnapshot cellSnapshot) {
			return String.valueOf(cellSnapshot.getNearbyLandMineCount());
		}
	},
	UNKNOWN(CellSnapshotStatus.UNCHECKED) {
		@Override
		public String provide(CellSnapshot cellSnapshot) {
			return UNCHECKED_SIGN;
		}
	}
	;

	private static final String EMPTY_SIGN = "■";
	private static final String FLAG_SIGN = "⚑";
	private static final String LAND_MINE_SIGN = "☼";
	private static final String UNCHECKED_SIGN = "□";

	private final CellSnapshotStatus status;

	CellSignProvider(CellSnapshotStatus status) {
		this.status = status;
	}

	@Override
	public boolean supports(CellSnapshot cellSnapshot) {
		return cellSnapshot.isSameStatus(status);
	}

	public static String findCellSignFrom(CellSnapshot snapshot) {
		CellSignProvider cellSignProvider = findBy(snapshot);
		return cellSignProvider.provide(snapshot);
	}

	private static CellSignProvider findBy(CellSnapshot snapshot) {
		return Arrays.stream(values())
			.filter(provider -> provider.supports(snapshot))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("확인할 수 없는 셀입니다."));
	}
}
```

### 🔋 변하는 것과 변하지 않는 것을 분리하여 추상화하고, OCP 를 지키는 구조
