package generic.test;

public class Test1 {
	public static String intToRoman(Integer num) {
		StringBuilder result = new StringBuilder();

		while (num >= 1000) {
			result.append("M");
			num -= 1000;
		}
		if (num >= 900) {
			result.append("CM");
			num -= 900;
		}
		if (num >= 500) {
			result.append("D");
			num -= 500;
		}
		if (num >= 400) {
			result.append("CD");
			num -= 400;
		}
		while (num >= 100) {
			result.append("C");
			num -= 100;
		}
		if (num >= 90) {
			result.append("XC");
			num -= 90;
		}
		if (num >= 50) {
			result.append("L");
			num -= 50;
		}
		if (num >= 40) {
			result.append("XL");
			num -= 40;
		}
		while (num >= 10) {
			result.append("X");
			num -= 10;
		}
		if (num >= 9) {
			result.append("IX");
			num -= 9;
		}
		if (num >= 5) {
			result.append("V");
			num -= 5;
		}
		if (num >= 4) {
			result.append("IV");
			num -= 4;
		}
		while (num >= 1) {
			result.append("I");
			num -= 1;
		}

		return result.toString();
	}

	public static void main(String[] args) {
//		System.out.println(intToRoman(49));  // Output: XLIX
//		System.out.println(intToRoman(23));  // Output: XXIII
//		System.out.println(intToRoman(1994));  // Output: MCMXCIV


		System.out.println(intToRoman(75));
		System.out.println(intToRoman(80));
		System.out.println(intToRoman(99));
		System.out.println(intToRoman(100));
		System.out.println(intToRoman(50));

	}
}
