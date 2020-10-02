package program;

import steganography.Steganography;
import steganography.exceptions.InsufficientTextSizeException;

import java.io.IOException;

public class Program {
    public static void main(String[] args) throws IOException, InsufficientTextSizeException {
        Steganography.cryptFromFileToFile("message14.txt", "Шифр.txt", "Зашифрованный_текст.txt", "EUC-JP");
        Steganography.decodeFromFileToFile("Зашифрованный_текст.txt", "deshifr123.txt", "EUC-JP");
    }
}
