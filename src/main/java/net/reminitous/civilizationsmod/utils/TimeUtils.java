package net.reminitous.civilizationsmod.utils;

public class TimeUtils {
    public static boolean hasOneWeekPassed(long lastActiveMillis) {
        long weekMillis = 7L * 24 * 60 * 60 * 1000;
        return System.currentTimeMillis() - lastActiveMillis >= weekMillis;
    }
}
