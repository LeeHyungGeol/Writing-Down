package generic.test.ex3;

public class UnitUtil {
	public static <T extends BioUnit> T maxHp(T t1, T t2) {
		if (t1.getHp() > t2.getHp()) {
			return t1;
		}
		return t2;
	}
}
