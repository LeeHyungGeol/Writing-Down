# 💡 논리, 사고의 흐름(코드)

***`인간의 뇌`는 `범주화`를 통해 정보를 처리하고 정리하는 시스템을 가지고 있다.***

- EX) 벌레라고 한다면, 바퀴벌레, 메뚜기, 사슴벌레, 장수풍뎅이 등등 많은 종류가 있지만 우리는 벌레라고 하면 집 안에 있으면 안되는 것이라고 기억할 뿐이다. 그것이 범주화이다.

***`인간의 뇌`는 `한번에 한가지 일` 밖에 하지 못한다. 멀티태스킹은 단지 저글링일 뿐이다.***

- 멀티태스킹은 사실상 뇌의 입장에서 본다면 매 순간순간마다 뇌를 그 해당 작업에 맞게 재설정하는 작업을 하므로 그만큼에 시간 비용이 드므로 이는 전체적인 수행 능력을 떨어트리고 속도를 저하시킨다.

***`인지적 경제성`: `최소한의 인지`만 우리가 가져가서 `최대의 효율`을 내보자.***

> **결론적으로, 우리가 작성한 코드를 다른 사람이 읽을 때, 읽는 사람의 `뇌 메모리를 효과적으로 사용할 수 있도록` 논리 구조를 짜거나 가독성을 높여야 하는 것이 핵심이다.**

<br/>

<br/>

<br/>

# 💡 Early Return

> **`Early Return`은 바로 return 해버릴 수 있으면 빠르게 return 해버리자는 뜻이다.**

- **`Early Return` 은 `else 사용을 지양하자`는 것이다.**
  - 메서드가 명확하고 표현하고자 하는게 짧다면 else 사용을 해도 된다.
- switch case 문도 비슷하게 남발하지 말자. 

--- 

**AS-IS**

```java
int selectedColumnIndex = getSelectedColIndex(cellInput);
int selectedRowIndex = getSelectedRowIndex(cellInput);
if (doesUserChooseToPlantFlag(userActionInput)) {
	BOARD[selectedRowIndex][selectedColumnIndex] = FLAG_SIGN;
	checkIfGameIsOver();
} else if (doesUserChooseToOpenCell(userActionInput)) {
	if (isLandMineCell(selectedRowIndex, selectedColumnIndex)) {
		BOARD[selectedRowIndex][selectedColumnIndex] = LAND_MINE_SIGN;
		changeGameStatusToLose();
		continue;
	} else {
		open(selectedRowIndex, selectedColumnIndex);
	}
	checkIfGameIsOver();
} else {
	System.out.println("잘못된 번호를 선택하셨습니다.");
}
```

**TO-BE**

```java
actOnCell(cellInput, userActionInput);

private static void actOnCell(String cellInput, String userActionInput) {
	int selectedColumnIndex = getSelectedColIndex(cellInput);
	int selectedRowIndex = getSelectedRowIndex(cellInput);
	if (doesUserChooseToPlantFlag(userActionInput)) {
		BOARD[selectedRowIndex][selectedColumnIndex] = FLAG_SIGN;
		checkIfGameIsOver();
		return;
	}
	if (doesUserChooseToOpenCell(userActionInput)) {
		if (isLandMineCell(selectedRowIndex, selectedColumnIndex)) {
			BOARD[selectedRowIndex][selectedColumnIndex] = LAND_MINE_SIGN;
			changeGameStatusToLose();
			return;
		}
		open(selectedRowIndex, selectedColumnIndex);
		checkIfGameIsOver();
		return;
	}
	System.out.println("잘못된 번호를 선택하셨습니다.");
}
```

- `if, else if, else 문`을 남발하면, 뇌가 조건문들의 조건을 계속 기억하면서 다음 코드를 읽어야 한다.
- 이는 뇌의 메모리를 더 많이 사용하게 되므로, `else if, else 문`을 없애고, **if 문이 끝날 때**는 `return` 을 사용하여 `Early Return`을 하였다.

<br/>

<br/>

<br/>

# 💡 사고의 depth 줄이기

## ⚡️ 중첩 분기문, 중첩 반복문

**AS-IS**

```java
for (int i = 0; i < 20; ++i) {
	for (int j = 20; j < 30; ++j) {
		if (i >= 10 && j < 25) {
			doSomething();
		}
	}
}
```

**TO-BE**

```java
for (int i = 0; i < 20; ++i) {
	doSomethingWithI(i);
}

private void doSomethingWithI(int i) {
    for (int j = 20; j < 30; ++j) {
	    doSomethingWithIJ(i, j);
    }
}

private void doSomethingWithIJ(int i, int j) {
	if (i >= 10 && j < 25) {
		doSomething();
	}
}
```

- 메서드 추출만으로 중첩 분기문, 중첩 반복을 최대한 depth 를 줄여서 사고를 쪼갤 수 있다.

> **무조건 1 depth 로 만들어라가 아니다!**

- 보이는 depth 를 무조건 줄이는 것이 아니라, **`추상화`를 통한 `사고 과정의 depth 를 줄이는 것`이 중요하다.**
- 2중 중첩 구조로 표현하는 것이 사고하는 데에 더 효율적이라면, 메서드로 분리하는 것보다 그냥 두는 것이 더 나을 수 있다. 오히려, 메서드로 분리해서 더 혼란을 줄 수 있다.

---

<br/>

## ⚡️ 사용할 변수는 가깝게 선언하기

> **사용할 변수는 가깝게 선언하기**

**AS-IS**

```java
int x = 10;
// 코드 100줄

int y = x + 10;
```

**TO-BE**

```java
// 코드 100줄

int x = 10;
int y = x + 10;
```

---

<br/>

> **메서드를 리팩토링할 때 여러군데에 쓰이는 메서드라면 변경했을 때, 컴파일 에러가 발생할 수 있으므로 `메서드를 똑같은 것 하나를 더 복제해놓고` 리팩토링을 하자.**

**AS-IS**

```java
private static boolean isAllCellOpened() {
	boolean isAllOpened = true;
	for (int row = 0; row < BOARD_ROW_SIZE; row++) {
		for (int col = 0; col < BOARD_COL_SIZE; col++) {
			if (BOARD[row][col].equals(CLOSED_CELL_SIGN)) {
				isAllOpened = false;
			}
		}
	}
	return isAllOpened;
}
```

**TO-BE**

```java
private static boolean isAllCellOpened() {
        return Arrays.stream(BOARD)  // Stream<String[]>
                .flatMap(Arrays::stream) // Stream<String> // 여기서 그냥 map 을 한다면 Stream<Stream<String>> 이 된다.
                .noneMatch(cell -> cell.equals(CLOSED_CELL_SIGN));
    }
```

- 3중 depth 에 대한 복잡도를 해소했다.
- Stream 이 중첩 for 문, if 문 보다 항상 가독성을 높이는 것이 아니다.

---

**AS-IS**

```java
 public static void main(String[] args) {
	showGameStartComments();
	Scanner scanner = new Scanner(System.in);
	initializeGame();
	while (true) {
		showBoard();
		if (doesUserWinTheGame()) {
			System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
			break;
		}
		if (doesUserLoseTheGame()) {
			System.out.println("지뢰를 밟았습니다. GAME OVER!");
			break;
		}
		String cellInput = getCellInputFromUser(scanner);
		String userActionInput = getUserActionInputFromUser(scanner);
		actOnCell(cellInput, userActionInput);
	}
}
```

**TO-BE**

```java
public static final Scanner SCANNER = new Scanner(System.in);

public static void main(String[] args) {
	showGameStartComments();
	initializeGame();
	while (true) {
		showBoard();
		if (doesUserWinTheGame()) {
			System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
			break;
		}
		if (doesUserLoseTheGame()) {
			System.out.println("지뢰를 밟았습니다. GAME OVER!");
			break;
		}
		String cellInput = getCellInputFromUser();
		String userActionInput = getUserActionInputFromUser();
		actOnCell(cellInput, userActionInput);
	}
}
```

- **Scanner 사용이 선언부와 멀어서 가독성이 떨어진다.**
- Scanner 를 사용하는 쪽으로 옮겼더니 가독성은 높아졌지만, while 문 안에 선언되어서 반복문을 돌 때 마다 Scanner 객체를 생성하게 된다.
- **Scanner 를 `상수`로 선언하여, 한 번만 생성하고 계속 사용하도록 하였다.**
- 상수로 선언했기 때문에, 메서드 파라미터에서 Scanner 를 넘겨주지 않아도 된다.

<br/>

<br/>

<br/>

# 💡 공백 라인을 대하는 자세

> 공백 라인도 의미를 가진다.     
> -> **복잡한 로직의 의미 단위를 나누어 보여줌으로써 읽는 사람에게 추가적인 정보를 전달한다.**

---

**AS-IS**

```java
public static void main(String[] args) {
	showGameStartComments();
	initializeGame();
	while (true) {
		showBoard();
		if (doesUserWinTheGame()) {
			System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
			break;
		}
		if (doesUserLoseTheGame()) {
			System.out.println("지뢰를 밟았습니다. GAME OVER!");
			break;
		}
		String cellInput = getCellInputFromUser();
		String userActionInput = getUserActionInputFromUser();
		actOnCell(cellInput, userActionInput);
	}
}
```

**TO-BE**

```java
public static void main(String[] args) {
	showGameStartComments();
	initializeGame();
	
	while (true) {
		showBoard();
		
		if (doesUserWinTheGame()) {
			System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
			break;
		}
		if (doesUserLoseTheGame()) {
			System.out.println("지뢰를 밟았습니다. GAME OVER!");
			break;
		}
		
		String cellInput = getCellInputFromUser();
		String userActionInput = getUserActionInputFromUser();
		actOnCell(cellInput, userActionInput);
	}
}
```

<br/>

<br/>

<br/>

# 💡 부정어를 대하는 자세

> **부정어구를 쓰지 않아도 되는 상황인지 체크**     
> **부정의 의미를 담은 다른 단어가 존재하는지 고민하기`(Not Left -> Right)` or 부정어구로 메서드명 구성`(isNot, doesNot)`**

-> **`부정 연산자(!)`의 가독성이 떨어진다!**

**AS-IS**

```java
if (!isLeftDirection()) {
	doSomething();
}
```

**TO-BE**

```java
if (isNotLeftDirection()) {
    doSomething();
}

if (isRightDirection()) {
	doSomething();
}
```

---

**AS-IS**

```java
for (int row = 0; row < BOARD_ROW_SIZE; row++) {
	for (int col = 0; col < BOARD_COL_SIZE; col++) {
		int count = 0;
		if (!isLandMineCell(row, col)) {
			if (row - 1 >= 0 && col - 1 >= 0 && isLandMineCell(row - 1, col - 1)) {
				count++;
			}
			if (row - 1 >= 0 && isLandMineCell(row - 1, col)) {
				count++;
			}
			if (row - 1 >= 0 && col + 1 < BOARD_COL_SIZE && isLandMineCell(row - 1, col + 1)) {
				count++;
			}
			if (col - 1 >= 0 && isLandMineCell(row, col - 1)) {
				count++;
			}
			if (col + 1 < BOARD_COL_SIZE && isLandMineCell(row, col + 1)) {
				count++;
			}
			if (row + 1 < BOARD_ROW_SIZE && col - 1 >= 0 && isLandMineCell(row + 1, col - 1)) {
				count++;
			}
			if (row + 1 < BOARD_ROW_SIZE && isLandMineCell(row + 1, col)) {
				count++;
			}
			if (row + 1 < BOARD_ROW_SIZE && col + 1 < BOARD_COL_SIZE && isLandMineCell(row + 1, col + 1)) {
				count++;
			}
			NEARBY_LAND_MINE_COUNTS[row][col] = count;
			continue;
		}
		NEARBY_LAND_MINE_COUNTS[row][col] = 0;
	}
}
```

**TO-BE**

```java
for (int row = 0; row < BOARD_ROW_SIZE; row++) {
	for (int col = 0; col < BOARD_COL_SIZE; col++) {
		if (isLandMineCell(row, col)) {
			NEARBY_LAND_MINE_COUNTS[row][col] = 0;
			continue;
		}
		int count = countNearByLandMines(row, col);
		NEARBY_LAND_MINE_COUNTS[row][col] = count;
	}
}

private static int countNearByLandMines(int row, int col) {
	int count = 0;
	if (row - 1 >= 0 && col - 1 >= 0 && isLandMineCell(row - 1, col - 1)) {
		count++;
	}
	if (row - 1 >= 0 && isLandMineCell(row - 1, col)) {
		count++;
	}
	if (row - 1 >= 0 && col + 1 < BOARD_COL_SIZE && isLandMineCell(row - 1, col + 1)) {
		count++;
	}
	if (col - 1 >= 0 && isLandMineCell(row, col - 1)) {
		count++;
	}
	if (col + 1 < BOARD_COL_SIZE && isLandMineCell(row, col + 1)) {
		count++;
	}
	if (row + 1 < BOARD_ROW_SIZE && col - 1 >= 0 && isLandMineCell(row + 1, col - 1)) {
		count++;
	}
	if (row + 1 < BOARD_ROW_SIZE && isLandMineCell(row + 1, col)) {
		count++;
	}
	if (row + 1 < BOARD_ROW_SIZE && col + 1 < BOARD_COL_SIZE && isLandMineCell(row + 1, col + 1)) {
		count++;
	}
	return count;
}
```

<br/>

<br/>

<br/>

# 💡 해피 케이스와 예외 처리

> **사람은 해피 케이스에 몰두하는 경향이 있지만, 개발자라면 당연하게도 `예외 처리를 꼼꼼하게 하는 것`이 개발자의 역량이라고 볼 수 있다.**

## ⚡️ **예외가 발생할 가능성 낮추기**

## ⚡️ **어떤 값의 검증이 필요한 부분은 주로 외부 세계와의 접점**
- `사용자 입력`, `객체 생성자`, `외부 서버의 요청` 등

## ⚡️ **의도한 예외와 예상하지 못한 예외를 구분하기**

- 사용자에게 보여줄 예외와, 개발자가 보고 처리해야 할 예외 구분

`의도한 예외`는 개발자가 인지하고 Exception을 던지는 경우     
`의도하지 않은 예외`는 개발자가 인지하지 못하고 Exception이 발생한다는 뜻

- `의도한 예외`를 발생 시켜 흐름을 제어하거나 어떤 의도를 전달할 수도 있고,
- 반대로 `의도하지 않은 예외`가 발생하는 것을 방지하기 위해 예외가 발생할 수 있는 구간을 잘 방어하여 `의도한 예외`로 전환하는 과정이 중요

## ⚡️ NULL을 대하는 자세

- **항상 `NullPointException` 을 방지하는 방향으로 경각심 가지기**
- **메서드 설계 시 `return null 을 자제한다.`**
  - 당연히 메서드를 사용하는 입장에서는 무언가 값을 받았기 때문에 그 값을 사용할 것이다. 그런데 그 값이 null 이라면, NPE 가 발생하는 상황이 만들어질 수 있다.
  - 만약 어렵다면, `Optional` 사용을 고민해 본다.

## ⚡️ **Optional 에 관하여**

- Optional 은 비싼 객체다. **꼭 필요한 상황에서 `반환 타입`에 사용한다.**
- **Optional 을 `파라미터`로 받지 않도록 한다.** 분기 케이스가 3개나 된다. (Optional 이 가진 데이터가 null 인지 아닌지 + Optional 그 자체가 null)
- Optional 을 반환한다면 최대한 빠르게 해소한다.

### 🔋 **Optional 을 해소하는 방법**

- 분기문을 만드는 `isPresent()-get()` **웬만하면 사용하지 말자!**
- **대신 풍부한 API 사용**
  - EX) `orElseGet()`, `orElseThrow()`, `ifPresent()`, `ifPresentOrElse()` 등

`orElse()`, `orElseGet()`, `orElseThrow()` 의 차이 숙지

**성능상 문제가 발생할 수 있다!**

- `orElse()`: 무조건 실행, orElse 다음에 오는 값이 확정된 값일 때 사용
- `orElseGet()`: Optional 이 null 일 때만 실행, 값을 제공하는 동작(Supplier) 정의
- `orElseThrow()`: Optional 이 null 일 때만 실행, 예외를 던지는 동작(Supplier) 정의
  - 값이 있으면 쓰고, 없으면 예외를 던지겠다. throw 안에 예외를 던지는 람다식을 넣으면 된다.

**EX)**

```java
public T orElse(T other) {
    return value != null ? value : other;
}

public T orElseGet(Supplier<? extends T> other) {
    return value != null ? value : other.get();
}
```

```java
Integer result = somethingOptional
        .orElse(performanceHeavy());

Integer result2 = somethingOptional
        .orElseGet(() -> performanceHeavy());

Integer result3 = somethingOptional
        .orElse(0);
```

- **`orElse` 의 `performanceHeavy()` 메서드는 호출할 필요가 없는 경우에도 항상 실행된다.**
  - `orElse` 는 `orElse(0)` 처럼 **정확히 명시된 값**일 때만 `orElse()` 를 사용하자. 
- **`orElseGet` 의 `performanceHeavy()` 메서드는 `Optional` 이 `null` 일 때만 실행된다.**

---

**AS-IS**

```java
public static void main(String[] args) {
	showGameStartComments();
	initializeGame();
	while (true) {
		showBoard();
		if (doesUserWinTheGame()) {
			System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
			break;
		}
		if (doesUserLoseTheGame()) {
			System.out.println("지뢰를 밟았습니다. GAME OVER!");
			break;
		}
		String cellInput = getCellInputFromUser();
		String userActionInput = getUserActionInputFromUser();
		actOnCell(cellInput, userActionInput);
	}
}

private static int convertRowFrom(char cellInputRow) {
  return Character.getNumericValue(cellInputRow) - 1;
}

private static int convertColFrom(char cellInputCol) {
  switch (cellInputCol) {
    case 'a':
      return  0;
    case 'b':
      return 1;
    case 'c':
      return 2;
    case 'd':
      return 3;
    case 'e':
      return 4;
    case 'f':
      return 5;
    case 'g':
      return 6;
    case 'h':
      return 7;
    case 'i':
      return 8;
    case 'j':
      return 9;
    default:
      return -1;
  }
}
```

**TO-BE**

```java
public static void main(String[] args) {
	showGameStartComments();
	initializeGame();
	while (true) {
		try {
			showBoard();

			if (doesUserWinTheGame()) {
				System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
				break;
			}
			if (doesUserLoseTheGame()) {
				System.out.println("지뢰를 밟았습니다. GAME OVER!");
				break;
			}
			String cellInput = getCellInputFromUser();
			String userActionInput = getUserActionInputFromUser();
			actOnCell(cellInput, userActionInput);
		} catch (AppException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println("프로그램에 문제가 생겼습니다.");
			e.printStackTrace();
		}
	}
}

private static int convertRowFrom(char cellInputRow) {
  int rowIndex = Character.getNumericValue(cellInputRow) - 1;
  if (rowIndex >= BOARD_ROW_SIZE) {
    throw new AppException("잘못된 입력입니다.");
  }

  return rowIndex;
}

private static int convertColFrom(char cellInputCol) {
  switch (cellInputCol) {
    case 'a':
      return  0;
    case 'b':
      return 1;
    case 'c':
      return 2;
    case 'd':
      return 3;
    case 'e':
      return 4;
    case 'f':
      return 5;
    case 'g':
      return 6;
    case 'h':
      return 7;
    case 'i':
      return 8;
    case 'j':
      return 9;
    default:
      throw new AppException("잘못된 입력입니다.");
  }
}
```

<br/>

---

**AS-IS**

```java
private static boolean isAllCellOpened() {
	return Arrays.stream(BOARD)
            .flatMap(Arrays::stream)
            .noneMatch(cell -> cell.equals(CLOSED_CELL_SIGN));
}
```

**TO-BE**

```java
private static boolean isAllCellOpened() {
	return Arrays.stream(BOARD)
            .flatMap(Arrays::stream)
            .noneMatch(cell -> CLOSED_CELL_SIGN.equals(cell));
}
```
