package enumeration.ex3;public class EnumRefMain {    public static void main(String[] args) {        System.out.println("class BASIC = " + Grade.BASIC.getClass());        System.out.println("class GOLD = " + Grade.GOLD.getClass());        System.out.println("class DIAMOND = " + Grade.DIAMOND.getClass());        // enum 은 toString() 메서드가 오버라이드 되어있기 때문에, 지정한 이름으로 문자열을 반환한다.//        ref BASIC = BASIC        System.out.println("ref BASIC = " + Grade.BASIC);        System.out.println("ref BASIC = " + refValue(Grade.BASIC));        System.out.println("ref GOLD = " + refValue(Grade.GOLD));        System.out.println("ref DIAMOND = " + refValue(Grade.DIAMOND));    }    private static String refValue(Object grade) {        return Integer.toHexString(System.identityHashCode(grade));    }}