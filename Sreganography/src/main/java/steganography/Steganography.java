package steganography;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Steganography {
    /**
     * Стеганография
     * Реализовать приложение, которое извлекает данные из входного файла, учитывая следующий алгоритм шифрования.
     * Для скрытной передачи данных используется такое свойство символа, как регистр. Если буква в нижнем регистре –
     * это соответствует «0», если буква в заглавном регистре – это соответствует «1».
     * <p>
     * ???Очередной символ секретного сообщения составляется из 8-ми битов, которые формируют код этого символа. В шифровании
     * используются только буквы русского или английского алфавита. Знаки препинания и цифры не учитываются.
     * <p>
     * Составить двоичное представление числа в виде строки по 8 бит
     * конвертировать в целое число
     * Ascii перевести в UTF-16
     * <p>
     * Приложение на входе получает имя текстового файла, анализирует текст по регистрам букв и извлекает из него
     * секретное сообщение, после чего выводит сообщение на экран.
     * <p>
     * Указание:
     * Для проверки символа, является ли он буквой, используйте функцию 	Character.isLetter
     * Для перевода символов в верхний и нижний регистр используйте функции
     * Character.toUpper/Character.toLower
     * Для корректной работы строки, содержащие русские буквы, должны быть объявлены как char. Учесть, что в Java
     * символы кодируются 2 байтами в типе данных char, а в задаче один символ кодируется только 1 байтом.
     */
    public static String deshifrator(String s) {
        String g = "";
        ArrayList<Byte> list = new ArrayList<Byte>();
        System.out.print("ASCII: ");
        for (int i = 0; i < s.length(); i++) {
            if (checkFromC(s.charAt(i))) {
                if (Character.isUpperCase(s.charAt(i)))
                    g += 1;
                if (Character.isLowerCase((s.charAt(i))))
                    g += 0;
            }
            if (g.length() == 8) {
                int asciiCode = Integer.parseInt(g, 2);
               // System.out.print(asciiCode + " ");
                list.add((byte) asciiCode);
                g = "";
            }
        }
        byte[] mass = new byte[list.size()];
        String m = "";
        for (int j = 0; j < mass.length; j++) {
            mass[j] = list.get(j);
        }
        try {
            m = new String(mass, "windows-1251");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return m;
    }

    public static String shifrator(String textSource, String text) throws UnsupportedEncodingException {
        textSource = textSource.toLowerCase();
        String shifr = "";
        byte[] asciiByte = text.getBytes("windows-1251");
        int[] asciiForBinary = new int[asciiByte.length];
        for (int i = 0; i < asciiByte.length; i++) {
            asciiForBinary[i] = asciiByte[i];
            if (asciiForBinary[i] < 0)
                asciiForBinary[i] += 256;
        }
        String[] binary = new String[asciiForBinary.length];
        for (int i = 0; i < asciiForBinary.length; i++) {
            binary[i] = makeZero(Integer.toBinaryString(asciiForBinary[i]));
        }
        if (!checkCount(textSource, binary))
            return null;
        int textLength = 0;
        for (int i = 0; i < binary.length; i++) {
            for (int j = 0; j < binary[i].length(); j++) {
                while (!checkFromC(textSource.charAt(textLength))) {
                    shifr += textSource.charAt(textLength);
                    textLength++;
                }
                if (binary[i].charAt(j) == '0') {
                    shifr += textSource.charAt(textLength);
                }
                if (binary[i].charAt(j) == '1') {
                    shifr += Character.toUpperCase(textSource.charAt(textLength));
                }
                textLength++;
            }
        }
        return shifr + textSource.substring(textLength);
    }

    private static boolean checkFromC(char symbolForShifr) {
        return ((Character.isLetter(symbolForShifr) && symbolForShifr != 'ё' && symbolForShifr
                != 'Ё' && symbolForShifr != ' ' && symbolForShifr != 'Ч' && symbolForShifr
                != 'ч' && symbolForShifr != '!' && symbolForShifr != '?' && symbolForShifr != ','
                && symbolForShifr != '.'));
    }

    private static boolean checkCount(String textForShifr, String[] binary) {
        int count = 0;
        for (int i = 0; i < textForShifr.length(); i++) {
            if (checkFromC(textForShifr.charAt(i)))
                count++;
        }
        return binary.length * 8 <= count;
    }

    private static String makeZero(String binary) {
        if (binary.length() < 8) {
            String zero = "";
            for (int j = binary.length(); j < 8; j++) {
                zero += "0";
            }
            binary = zero + binary;
        }
        return binary;
    }
}
