package tqdream.myutil;

import java.util.Random;

import tqdream.utils.StringUtils;

/**
 * Random Utils
 * <ul>
 * Shuffling algorithm
 * <li>{@link #shuffle(Object[])} Shuffling algorithm, Randomly permutes the specified array using a default source of
 * randomness</li>
 * <li>{@link #shuffle(Object[], int)} Shuffling algorithm, Randomly permutes the specified array</li>
 * <li>{@link #shuffle(int[])} Shuffling algorithm, Randomly permutes the specified int array using a default source of
 * randomness</li>
 * <li>{@link #shuffle(int[], int)} Shuffling algorithm, Randomly permutes the specified int array</li>
 * </ul>
 * <ul>
 * get random int
 * <li>{@link #getRandom(int)} get random int between 0 and max</li>
 * <li>{@link #getRandom(int, int)} get random int between min and max</li>
 * </ul>
 * <ul>
 * get random numbers or letters
 * <li>{@link #getRandomCapitalLetters(int)} get a fixed-length random string, its a mixture of uppercase letters</li>
 * <li>{@link #getRandomLetters(int)} get a fixed-length random string, its a mixture of uppercase and lowercase letters
 * </li>
 * <li>{@link #getRandomLowerCaseLetters(int)} get a fixed-length random string, its a mixture of lowercase letters</li>
 * <li>{@link #getRandomNumbers(int)} get a fixed-length random string, its a mixture of numbers</li>
 * <li>{@link #getRandomNumbersAndLetters(int)} get a fixed-length random string, its a mixture of uppercase, lowercase
 * letters and numbers</li>
 * <li>{@link #getRandom(String, int)} get a fixed-length random string, its a mixture of chars in source</li>
 * <li>{@link #getRandom(char[], int)} get a fixed-length random string, its a mixture of chars in sourceChar</li>
 * </ul>
 *
 * @author jingle1267@163.com
 */
public class RandomUtils {

    public static final String NUMBERS_AND_LETTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String NUMBERS             = "0123456789";
    public static final String LETTERS             = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String CAPITAL_LETTERS     = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWER_CASE_LETTERS  = "abcdefghijklmnopqrstuvwxyz";

    private RandomUtils() {
        throw new AssertionError();
    }

    /**
     * get a fixed-length random string, its a mixture of uppercase, lowercase letters and numbers
     * 
     * @param length  length
     * @return  RandomUtils
     */
    public static String getRandomNumbersAndLetters(int length) {
        return getRandom(NUMBERS_AND_LETTERS, length);
    }

    /**
     * get a fixed-length random string, its a mixture of numbers
     * 
     * @param length  length
     * @return  RandomUtils
     */
    public static String getRandomNumbers(int length) {
        return getRandom(NUMBERS, length);
    }

    /**
     * get a fixed-length random string, its a mixture of uppercase and lowercase letters
     * 
     * @param length  length
     * @return  RandomUtils
     */
    public static String getRandomLetters(int length) {
        return getRandom(LETTERS, length);
    }

    /**
     * get a fixed-length random string, its a mixture of uppercase letters
     * 
     * @param length  length
     * @return   CapitalLetters
     */
    public static String getRandomCapitalLetters(int length) {
        return getRandom(CAPITAL_LETTERS, length);
    }

    /**
     * get a fixed-length random string, its a mixture of lowercase letters
     * 
     * @param length  length
     * @return  get a fixed-length random string, its a mixture of lowercase letters
     */
    public static String getRandomLowerCaseLetters(int length) {
        return getRandom(LOWER_CASE_LETTERS, length);
    }

    /**
     * get a fixed-length random string, its a mixture of chars in source
     * 
     * @param source  source
     * @param length  length
     * @return  get a fixed-length random string, its a mixture of chars in source
     */
    public static String getRandom(String source, int length) {
        return StringUtils.isEmpty(source) ? null : getRandom(source.toCharArray(), length);
    }

    /**
     * get a fixed-length random string, its a mixture of chars in sourceChar
     * 
     * @param sourceChar    sourceChar
     * @param length  length
     * @return   get a fixed-length random string, its a mixture of chars in sourceChar
     */
    public static String getRandom(char[] sourceChar, int length) {
        if (sourceChar == null || sourceChar.length == 0 || length < 0) {
            return null;
        }

        StringBuilder str = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            str.append(sourceChar[random.nextInt(sourceChar.length)]);
        }
        return str.toString();
    }


    /**
     *
     * @param max  接受的数值
     * @return  返回一个随机的数值
     */
    public static int getRandom(int max) {

        return getRandom(0, max);
    }


    /**
     *
     * @param min  最小
     * @param max  最大
     * @return  返回一个范围的数值
     */
    public static int getRandom(int min, int max) {

        if (min > max) {
            return 0;
        }
        if (min == max) {
            return min;
        }
        return min + new Random().nextInt(max - min);
    }

    /**
     * Shuffling algorithm, Randomly permutes the specified array using a default source of randomness
     * 
     * @param objArray  数组
     * @return 从新的数组
     */
    public static boolean shuffle(Object[] objArray) {
        if (objArray == null) {
            return false;
        }

        return shuffle(objArray, getRandom(objArray.length));
    }

    /**
     * Shuffling algorithm, Randomly permutes the specified array
     * 
     * @param objArray  数组
     * @param shuffleCount  洗的个数
     * @return  是否成功
     */
    public static boolean shuffle(Object[] objArray, int shuffleCount) {
        int length;
        if (objArray == null || shuffleCount < 0 || (length = objArray.length) < shuffleCount) {
            return false;
        }

        for (int i = 1; i <= shuffleCount; i++) {
            int random = getRandom(length - i);
            Object temp = objArray[length - i];
            objArray[length - i] = objArray[random];
            objArray[random] = temp;
        }
        return true;
    }

    /**
     * Shuffling algorithm, Randomly permutes the specified int array using a default source of randomness
     * 
     * @param intArray  数组
     * @return  洗牌之后
     */
    public static int[] shuffle(int[] intArray) {
        if (intArray == null) {
            return null;
        }

        return shuffle(intArray, getRandom(intArray.length));
    }

    /**
     * Shuffling algorithm, Randomly permutes the specified int array
     * 
     * @param intArray   数组
     * @param shuffleCount  范围
     * @return  新的数组
     */
    public static int[] shuffle(int[] intArray, int shuffleCount) {
        int length;
        if (intArray == null || shuffleCount < 0 || (length = intArray.length) < shuffleCount) {
            return null;
        }

        int[] out = new int[shuffleCount];
        for (int i = 1; i <= shuffleCount; i++) {
            int random = getRandom(length - i);
            out[i - 1] = intArray[random];
            int temp = intArray[length - i];
            intArray[length - i] = intArray[random];
            intArray[random] = temp;
        }
        return out;
    }
}
