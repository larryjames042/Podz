package mirror.co.larry.podz.Util;

public class HelperUtil {
    public static String convertSecToHr(long seconds){
        long p1 = seconds % 60;
        long p2 = seconds / 60;
        long p3 = p2 % 60;
        p2 = p2 / 60;
        return p2 + ":" + p3 + ":" + p1;
    }
}