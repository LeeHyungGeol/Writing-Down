package generic.ex5;

import generic.animal.Animal;
import generic.animal.Cat;
import generic.animal.Dog;

public class WildcardMain1 {
	public static void main(String[] args) {
		Box<Object> objBox = new Box<>();
		Box<Dog> dogBox = new Box<>();
		Box<Cat> catBox = new Box<>();
		dogBox.set(new Dog("멍멍이", 100));

		WildcardEx.printGenericV1(dogBox);
		WildcardEx.printWildcardV1(dogBox);

		WildcardEx.printGenericV2(dogBox);
		WildcardEx.printWildcardV2(dogBox);

		Dog dog = WildcardEx.printAndReturnGeneric(dogBox);

		catBox.set(new Cat("냐옹이", 100));
		Cat cat = WildcardEx.printAndReturnGeneric(catBox);

		Dog animal = (Dog) WildcardEx.printAndReturnWildcard(dogBox); // 와일드카드는 캐스팅을 해야만 하위 타입을 사용할 수 있다.
	}
}
