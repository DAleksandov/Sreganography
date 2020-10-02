package steganography;

import steganography.exceptions.InsufficientTextSizeException;

import java.io.*;
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
    public static String decoder(String textDecode, String encoding) throws IOException {
        String g = "";
        ArrayList<Byte> list = new ArrayList<Byte>();
        for (int i = 0; i < textDecode.length(); i++) {
            if (checkFromC(textDecode.charAt(i))) {
                if (Character.isUpperCase(textDecode.charAt(i)))
                    g += 1;
                if (Character.isLowerCase((textDecode.charAt(i))))
                    g += 0;
            }
            if (g.length() == 8) {
                int asciiCode = Integer.parseInt(g, 2);
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
            m = new String(mass, encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return m;
    }

    private static String loadData(String fileName) throws IOException {
        String line = "";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            while (bufferedReader.ready()) {
                line += bufferedReader.readLine();
            }
            return line;
        } catch (IOException e) {
            throw e;
        }
    }

    public static void decodeFromFileToFile(String fileNameSourceText, String encryptedTextFileName, String encoding) throws IOException {
        saveData(encryptedTextFileName, decoder(loadData(fileNameSourceText), encoding));
    }


    /**
     * 2. Реализовать программу, которая принимает на вход
     * имя файла, где располагается сообщение для шифрования,
     * имя файла, где располагается текст, в котором будет происходить шифрование,
     * имя файла, куда будет записан зашифрованное сообщение,
     * а также кодировку, в которой производится шифрование.
     * <p>
     * Необходимо произвести шифрование сообщения по правилам, описанным в 1 пункте. При невозможности произвести шифрование в переданном тексте
     * сгенерировать исключение InsufficientTextSizeException.
     * <p>
     * При реализации 1 и 2 пунктов проекта задачу необходимо разбить на статические методы, открытыми сделать лишь те методы, которые должны будут вызываться пользователем.
     * Все остальные служебные вспомогательные методы необходимо закрыть.
     * ПРЕДУСМОТРЕТЬ ВОЗМОЖНОСТЬ ВВОДА НЕ ТОЛЬКО ИМЕН ФАЙЛОВ ДЛЯ ВВОДА ИНФОРМАЦИИ, А ТАК ЖЕ ВВОДА И СТРОК В МЕТОДЫ.
     * ЭТОГО МОЖНО ДОСТИЧЬ ЛИШЬ В ТОМ СЛУЧАЕ, КОГДА МЕТОД СЧИТЫВАНИЯ С ФАЙЛА БУДЕТ ОТДЕЛЕН ОТ МЕТОДА ШИФРОВАНИЯ.
     *
     * @param sourceText
     * @param CryptText
     * @return
     * @throws UnsupportedEncodingException
     */

    public static String encoder(String sourceText, String CryptText, String encoding) throws IOException, InsufficientTextSizeException {
        sourceText = sourceText.toLowerCase();
        String shifr = "";
        byte[] asciiByte = CryptText.getBytes(encoding);
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
        if (!checkCount(sourceText, binary))
            throw new InsufficientTextSizeException("Недостаточная длина текста для шифрования!");
        int textLength = 0;
        for (int i = 0; i < binary.length; i++) {
            for (int j = 0; j < binary[i].length(); j++) {
                while (!checkFromC(sourceText.charAt(textLength))) {
                    shifr += sourceText.charAt(textLength);
                    textLength++;
                }
                if (binary[i].charAt(j) == '0') {
                    shifr += sourceText.charAt(textLength);
                }
                if (binary[i].charAt(j) == '1') {
                    shifr += Character.toUpperCase(sourceText.charAt(textLength));
                }
                textLength++;
            }
        }
        return shifr + sourceText.substring(textLength);
    }

    public static void cryptFromFileToFile(String fileNameSourceText, String fileNameShifrText, String encryptedTextFileName, String encoding) throws IOException, InsufficientTextSizeException {
        saveData(encryptedTextFileName, encoder(loadData(fileNameSourceText), loadData(fileNameShifrText), encoding));
    }

    private static boolean checkFromC(char symbolForCrypt) {
        return ((Character.isLetter(symbolForCrypt) && symbolForCrypt != 'ё' && symbolForCrypt
                != 'Ё' && symbolForCrypt != ' ' && symbolForCrypt != 'Ч' && symbolForCrypt
                != 'ч' && symbolForCrypt != '!' && symbolForCrypt != '?' && symbolForCrypt != ','
                && symbolForCrypt != '.'));
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

    private static void saveData(String fileName, String textShifr) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(textShifr);
        }
    }
}
