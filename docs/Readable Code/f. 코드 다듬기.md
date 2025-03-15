# 코드 다듬기

# 💡 주석의 양면성

## ⚡️ 주석은 죄악이다! vs 주석 좀 남기자!

> 주석이 많다는 것은 그만큼 비즈니스 요구사항을 코드에 잘 표현하지 못했다.

> 코드를 설명하는 주석을 쓰면, 코드가 아닌 주석 설명에 의존하게 된다.
- 주석에 의존하여 코드를 작성하면, 적절하지 않은 추상화 레벨을 갖게 되어 낮은 품질의 코드가 만들어진다.

## ⚡️ 그럼 주석은 언제 쓰나?

## ⚡️ 좋은 주석

> 후대에 전해야 할 `의사 결정의 히스토리` 왜 이런 정책을 갖게 되었고, 왜 이런 히스토리를 갖게 되었는지를 코드로 설명할 수 없을 떄
> 
> **주석**을 사용한다.

```java
/**
 * 이 객체가 가진 정책은,
 * A 정책과 B 정책중, AB 테스트와 기술 회의를 통해 결정된 사항을 기반으로 선택되어 작성된 것이다.
 * 관련 문서: https://docs.google.com/document/d/1
 */
public class ImportantPolicy { }
```

- 주석을 작성할 때, 자주 변하는 정보는 최대한 지양해서 작성한다.
- 주석도 코드처럼 같이 관리해줘야 한다. 지속적인 업데이트가 필요하다.
- 주석이 아예 없는 것 보다, 부정확한 정보를 갖고 있는 주석이 더 최악이다.

> 💡 **먼저, 우리가 가진 모든 표현 방법을 통해 코드를 통해 의도들을 잘 작성하고, 그럼에도 불구히고 코드에 녹여내지 못하는 정보가 있다면 주석을 사용하자!** 

아래의 주석은 꼭 필요한 주석일까?

**AS-IS**

해당 주석은 게임의 상태값을 int 로 표시하고 있다. 하지만, 이것을 Enum 으로 표현하면 주석을 사용하지 않고도 의도를 더 명확하게 표현할 수 있다.

```java
private int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배
```

**TO-BE**

```java
public enum GameStatus {
	IN_PROGRESS("진행 중"),
	WIN("승리"),
	LOSE("패배")
	;

	private final String description;

	GameStatus(String description) {
		this.description = description;
	}
}
```

#### GameStatus 를 만들면서 Minesweeper 의 역할과 GameBoard 의 역할을 좀 더 확실하게 분리시켰다.

- `GameBoard` 는 실제 게임 진행에 대한 것들을 모두 담당한다.
  - 실제적인 지뢰 찾기의 도메인 로직을 모두 관리한다.
- `MineSweeper` 는 외부 세계와의 접점, 컨트롤러의 역할을 하는 것이다.
  - 게임을 실행하고 여러가지 입/출력들을 중간에서 담당하여 전달해준다.

### MVC 구조처럼 돌아가는 것을 볼 수 있다.

---

# 💡 변수와 메서드의 나열 순서

## ⚡️ 변수와 메서드는 어떤 순서대로 나열하는 것이 가장 좋을까?

> **`변수`는 사용하는 순서대로 나열한다.**
- **인지적 경제성(뇌 메모리)**
- 변수가 사용하기 가까운 쪽에 배치해야 된다.

> 객체 간의 협력 관점에서 바라볼 때, 공개 메서드(public)를 먼저 위에 배치하고, 그 다음에 비공개 메서드(private)를 나열한다.
- 물론, 이것이 정답은 아니다.

> 공개 메서드 끼리도 기준을 가치고 배치하는 것이 좋다!
- 메서드가 수십개인 객체의 경우, 메서드를 기준을 가지고 그룹화하여 배치한다. 똑같은 기능을 가진 메서드의 중복 생성 방지 및 일관성 있는 로직을 유지할 수 있다.
- ***상태 변경 >> 판별 >= 조회 메서드***
  - 객체의 상태를 변경하는 작업은 매우 중요한 작업이기 때문에 상단에 배치할 수 있도록 한다.

> 비공개(private) 메서드는, 공개(public) 메서드에서 언급된 순서대로 배치한다.
- `공통으로 사용하는 메서드`라면, **(가장 하단과 같은) 적당한 곳에 배치한다.**

### 🔋 중요한 것은, 나열 순서로도 의도와 정보를 전달할 수 있다는 것!

**AS-IS**

```java
public class Minesweeper implements GameInitializable, GameRunnable {
	private final GameBoard gameBoard;
	private final InputHandler inputHandler;
	private final OutputHandler outputHandler;

	public Minesweeper(GameConfig gameConfig) {
		gameBoard = new GameBoard(gameConfig.getGameLevel());
		this.inputHandler = gameConfig.getInputHandler();
		this.outputHandler = gameConfig.getOutputHandler();
	}

	@Override
	public void initialize() {
		gameBoard.initializeGame();
	}

	@Override
	public void run() {
		outputHandler.showGameStartComments();

		while (gameBoard.inInProgress()) {
			try {
				outputHandler.showBoard(gameBoard);

				CellPosition cellPosition = getCellInputFromUser();
				UserAction userActionInput = getUserActionInputFromUser();
				actOnCell(cellPosition, userActionInput);
			} catch (GameException e) {
				outputHandler.showExceptionMessage(e);
			} catch (Exception e) {
				outputHandler.showSimpleMessage("프로그램에 문제가 생겼습니다.");
			}
		}
		outputHandler.showBoard(gameBoard);

		if (gameBoard.isWinStatus()) {
			outputHandler.showGameWinningComment();
		}
		if (gameBoard.isLoseStatus()) {
			outputHandler.showGameLosingComment();
		}
	}

	private void actOnCell(CellPosition cellPosition, UserAction userActionInput) {
		if (doesUserChooseToPlantFlag(userActionInput)) {
			gameBoard.flagAt(cellPosition);
			return;
		}
		if (doesUserChooseToOpenCell(userActionInput)) {
			gameBoard.openAt(cellPosition);
			return;
		}
		throw new GameException("잘못된 번호를 선택하셨습니다.");
	}

	private boolean doesUserChooseToOpenCell(UserAction userAction) {
		return userAction == UserAction.OPEN;
	}

	private boolean doesUserChooseToPlantFlag(UserAction userAction) {
		return userAction == UserAction.FLAG;
	}

	private UserAction getUserActionInputFromUser() {
		outputHandler.showCommentForUserAction();
		return inputHandler.getUserActionFromUser();
	}

	private CellPosition getCellInputFromUser() {
		outputHandler.showCommentForSelectingCell();
		CellPosition cellPosition = inputHandler.getCellPositionFromUser();
		if (gameBoard.isInvalidCellPosition(cellPosition)) {
			throw new GameException("잘못된 좌표를 선택하셨습니다.");
		}

		return cellPosition;
	}
}
```

**TO-BE**

```java
public class Minesweeper implements GameInitializable, GameRunnable {
	private final GameBoard gameBoard;
	private final InputHandler inputHandler;
	private final OutputHandler outputHandler;

	public Minesweeper(GameConfig gameConfig) {
		gameBoard = new GameBoard(gameConfig.getGameLevel());
		this.inputHandler = gameConfig.getInputHandler();
		this.outputHandler = gameConfig.getOutputHandler();
	}

	@Override
	public void initialize() {
		gameBoard.initializeGame();
	}

	@Override
	public void run() {
		outputHandler.showGameStartComments();

		while (gameBoard.inInProgress()) {
			try {
				outputHandler.showBoard(gameBoard);

				CellPosition cellPosition = getCellInputFromUser();
				UserAction userActionInput = getUserActionInputFromUser();
				actOnCell(cellPosition, userActionInput);
			} catch (GameException e) {
				outputHandler.showExceptionMessage(e);
			} catch (Exception e) {
				outputHandler.showSimpleMessage("프로그램에 문제가 생겼습니다.");
			}
		}
		outputHandler.showBoard(gameBoard);

		if (gameBoard.isWinStatus()) {
			outputHandler.showGameWinningComment();
		}
		if (gameBoard.isLoseStatus()) {
			outputHandler.showGameLosingComment();
		}
	}

	private CellPosition getCellInputFromUser() {
		outputHandler.showCommentForSelectingCell();
		CellPosition cellPosition = inputHandler.getCellPositionFromUser();
		if (gameBoard.isInvalidCellPosition(cellPosition)) {
			throw new GameException("잘못된 좌표를 선택하셨습니다.");
		}

		return cellPosition;
	}

	private UserAction getUserActionInputFromUser() {
		outputHandler.showCommentForUserAction();
		return inputHandler.getUserActionFromUser();
	}

	private void actOnCell(CellPosition cellPosition, UserAction userActionInput) {
		if (doesUserChooseToPlantFlag(userActionInput)) {
			gameBoard.flagAt(cellPosition);
			return;
		}
		if (doesUserChooseToOpenCell(userActionInput)) {
			gameBoard.openAt(cellPosition);
			return;
		}
		throw new GameException("잘못된 번호를 선택하셨습니다.");
	}

	private boolean doesUserChooseToPlantFlag(UserAction userAction) {
		return userAction == UserAction.FLAG;
	}

	private boolean doesUserChooseToOpenCell(UserAction userAction) {
		return userAction == UserAction.OPEN;
	}
}
```

**AS-IS**

```java
public class GameBoard {

	private final Cell[][] board;
	private final int landMineCount;
	private GameStatus gameStatus;

	public GameBoard(GameLevel gameLevel) {
		int rowSize = gameLevel.getRowSize();
		int colSize = gameLevel.getColSize();
		board = new Cell[rowSize][colSize];

		landMineCount = gameLevel.getLandMineCount();
		initializeGameStatus();
	}

	public void initializeGame() {
		initializeGameStatus();
		CellPositions cellPositions = CellPositions.from(board);

		initializeEmptyCells(cellPositions);

		List<CellPosition> landMinePositions = cellPositions.extractRandomPositions(landMineCount);
		initializeLandMineCells(landMinePositions);

		List<CellPosition> numberPositionCandidates = cellPositions.subtract(landMinePositions);
		initializeNumberCells(numberPositionCandidates);
	}

	public void flagAt(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		cell.flag();

		checkIfGameIsOver();
	}

	private void checkIfGameIsOver() {
		if (isAllCellChecked()) {
			changeGameStatusToWin();
		}
	}

	private void changeGameStatusToWin() {
		gameStatus = GameStatus.WIN;
	}

	public void openAt(CellPosition cellPosition) {
		if (isLandMineCellAt(cellPosition)) {
			openOneCellAt(cellPosition);
			changeGameStatusToLose();
			return;
		}

		openSurroundedCells(cellPosition);
		checkIfGameIsOver();
	}

	public void openOneCellAt(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		cell.open();
	}

	public void openSurroundedCells(CellPosition cellPosition) {
		if (isOpenedCell(cellPosition)) {
			return;
		}
		if (isLandMineCellAt(cellPosition)) {
			return;
		}

		openOneCellAt(cellPosition);

		if (doesCellHaveLandMineCount(cellPosition)) {
			return;
		}

		List<CellPosition> surroundedPositions = calculateSurroundedPositions(cellPosition, getRowSize(), getColSize());
		surroundedPositions.forEach(this::openSurroundedCells);
	}

	private boolean doesCellHaveLandMineCount(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		return cell.hasLandMineCount();
	}

	private boolean isOpenedCell(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		return cell.isOpened();
	}

	public boolean isLandMineCellAt(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		return cell.isLandMine();
	}

	public boolean isAllCellChecked() {
		Cells cells = Cells.from(board);
		return cells.isAllChecked();
	}

	public boolean isInvalidCellPosition(CellPosition cellPosition) {
		int rowSize = getRowSize();
		int colSize = getColSize();

		return cellPosition.isRowIndexMoreThanOrEqual(rowSize)
			|| cellPosition.isColIndexMoreThanOrEqual(colSize);
	}

	public boolean inInProgress() {
		return gameStatus == GameStatus.IN_PROGRESS;
	}

	private void initializeGameStatus() {
		gameStatus = GameStatus.IN_PROGRESS;
	}

	private void changeGameStatusToLose() {
		gameStatus = GameStatus.LOSE;
	}

	private void initializeEmptyCells(CellPositions cellPositions) {
		List<CellPosition> allPositions = cellPositions.getPositions();
		for (CellPosition position : allPositions) {
			updateCellAt(position, new EmptyCell());
		}
	}

	private void initializeLandMineCells(List<CellPosition> landMinePositions) {
		for (CellPosition position : landMinePositions) {
			updateCellAt(position, new LandMineCell());
		}
	}

	private void initializeNumberCells(List<CellPosition> numberPositionCandidates) {
		for (CellPosition candidatePosition : numberPositionCandidates) {
			int count = countNearbyLandMines(candidatePosition);
			if (count != 0) {
				updateCellAt(candidatePosition, new NumberCell(count));
			}
		}
	}

	private void updateCellAt(CellPosition position, Cell cell) {
		board[position.getRowIndex()][position.getColIndex()] = cell;
	}

	public CellSnapshot getSnapshot(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		return cell.getSnapshot();
	}

	private Cell findCell(CellPosition cellPosition) {
		return board[cellPosition.getRowIndex()][cellPosition.getColIndex()];
	}

	public int getRowSize() {
		return board.length;
	}

	public int getColSize() {
		return board[0].length;
	}

	private int countNearbyLandMines(CellPosition cellPosition) {
		int rowSize = getRowSize();
		int colSize = getColSize();

		long count = calculateSurroundedPositions(cellPosition, rowSize, colSize).stream()
			.filter(this::isLandMineCellAt)
			.count();

		return (int) count;
	}

	private List<CellPosition> calculateSurroundedPositions(CellPosition cellPosition, int rowSize, int colSize) {
		return RelativePosition.SURROUNDED_POSITIONS.stream()
			.filter(cellPosition::canCalculatePositionBy)
			.map(cellPosition::calculatePositionBy)
			.filter(position -> position.isRowIndexLessThan(rowSize))
			.filter(position -> position.isColIndexLessThan(colSize))
			.toList();
	}

	public boolean isWinStatus() {
		return gameStatus == GameStatus.WIN;
	}

	public boolean isLoseStatus() {
		return gameStatus == GameStatus.LOSE;
	}
}
```

**TO-BE**

```java
public class GameBoard {

	private final Cell[][] board;
	private final int landMineCount;
	private GameStatus gameStatus;

	public GameBoard(GameLevel gameLevel) {
		int rowSize = gameLevel.getRowSize();
		int colSize = gameLevel.getColSize();
		board = new Cell[rowSize][colSize];

		landMineCount = gameLevel.getLandMineCount();
		initializeGameStatus();
	}

	// public
	// 상태 변경
	public void initializeGame() {
		initializeGameStatus();
		CellPositions cellPositions = CellPositions.from(board);

		initializeEmptyCells(cellPositions);

		List<CellPosition> landMinePositions = cellPositions.extractRandomPositions(landMineCount);
		initializeLandMineCells(landMinePositions);

		List<CellPosition> numberPositionCandidates = cellPositions.subtract(landMinePositions);
		initializeNumberCells(numberPositionCandidates);
	}

	public void openAt(CellPosition cellPosition) {
		if (isLandMineCellAt(cellPosition)) {
			openOneCellAt(cellPosition);
			changeGameStatusToLose();
			return;
		}

		openSurroundedCells(cellPosition);
		checkIfGameIsOver();
	}

	public void flagAt(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		cell.flag();

		checkIfGameIsOver();
	}

	// 판별
	public boolean isInvalidCellPosition(CellPosition cellPosition) {
		int rowSize = getRowSize();
		int colSize = getColSize();

		return cellPosition.isRowIndexMoreThanOrEqual(rowSize)
			|| cellPosition.isColIndexMoreThanOrEqual(colSize);
	}

	public boolean inInProgress() {
		return gameStatus == GameStatus.IN_PROGRESS;
	}


	// 조회
	public CellSnapshot getSnapshot(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		return cell.getSnapshot();
	}

	public int getRowSize() {
		return board.length;
	}

	public int getColSize() {
		return board[0].length;
	}

	public boolean isWinStatus() {
		return gameStatus == GameStatus.WIN;
	}

	public boolean isLoseStatus() {
		return gameStatus == GameStatus.LOSE;
	}


	// private

	private void initializeGameStatus() {
		gameStatus = GameStatus.IN_PROGRESS;
	}

	private void initializeEmptyCells(CellPositions cellPositions) {
		List<CellPosition> allPositions = cellPositions.getPositions();
		for (CellPosition position : allPositions) {
			updateCellAt(position, new EmptyCell());
		}
	}

	private void initializeLandMineCells(List<CellPosition> landMinePositions) {
		for (CellPosition position : landMinePositions) {
			updateCellAt(position, new LandMineCell());
		}
	}

	private void initializeNumberCells(List<CellPosition> numberPositionCandidates) {
		for (CellPosition candidatePosition : numberPositionCandidates) {
			int count = countNearbyLandMines(candidatePosition);
			if (count != 0) {
				updateCellAt(candidatePosition, new NumberCell(count));
			}
		}
	}

	private int countNearbyLandMines(CellPosition cellPosition) {
		int rowSize = getRowSize();
		int colSize = getColSize();

		long count = calculateSurroundedPositions(cellPosition, rowSize, colSize).stream()
			.filter(this::isLandMineCellAt)
			.count();

		return (int) count;
	}

	private List<CellPosition> calculateSurroundedPositions(CellPosition cellPosition, int rowSize, int colSize) {
		return RelativePosition.SURROUNDED_POSITIONS.stream()
			.filter(cellPosition::canCalculatePositionBy)
			.map(cellPosition::calculatePositionBy)
			.filter(position -> position.isRowIndexLessThan(rowSize))
			.filter(position -> position.isColIndexLessThan(colSize))
			.toList();
	}

	private void updateCellAt(CellPosition position, Cell cell) {
		board[position.getRowIndex()][position.getColIndex()] = cell;
	}

	private void openSurroundedCells(CellPosition cellPosition) {
		if (isOpenedCell(cellPosition)) {
			return;
		}
		if (isLandMineCellAt(cellPosition)) {
			return;
		}

		openOneCellAt(cellPosition);

		if (doesCellHaveLandMineCount(cellPosition)) {
			return;
		}

		List<CellPosition> surroundedPositions = calculateSurroundedPositions(cellPosition, getRowSize(), getColSize());
		surroundedPositions.forEach(this::openSurroundedCells);
	}

	private void openOneCellAt(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		cell.open();
	}

	private boolean isOpenedCell(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		return cell.isOpened();
	}

	private boolean isLandMineCellAt(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		return cell.isLandMine();
	}

	private boolean doesCellHaveLandMineCount(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		return cell.hasLandMineCount();
	}

	private void checkIfGameIsOver() {
		if (isAllCellChecked()) {
			changeGameStatusToWin();
		}
	}

	private boolean isAllCellChecked() {
		Cells cells = Cells.from(board);
		return cells.isAllChecked();
	}

	private void changeGameStatusToWin() {
		gameStatus = GameStatus.WIN;
	}

	private void changeGameStatusToLose() {
		gameStatus = GameStatus.LOSE;
	}

	private Cell findCell(CellPosition cellPosition) {
		return board[cellPosition.getRowIndex()][cellPosition.getColIndex()];
	}
}
```

---



