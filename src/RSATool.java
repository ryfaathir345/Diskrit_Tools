import java.util.Scanner;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.UnitValue;
import java.io.IOException;
import java.io.*;

public class RSATool {
    public static List<BigInteger> asciiBlocks = new ArrayList<>();
    public static List<BigInteger> encryptedBlocks = new ArrayList<>();
    public static List<BigInteger> decryptedBlocks = new ArrayList<>();
    public static List<BigInteger> encryptedAlpha = new ArrayList<>();
    public static List<BigInteger> encryptedBeta = new ArrayList<>();
    public static List<BigInteger> decryptedMessages = new ArrayList<>();
    public static StringBuilder hasilDekripsiTeks = new StringBuilder();
    public static final BigInteger P = new BigInteger("2579");  // Modulus
    public static final BigInteger PRIVATE_KEY = new BigInteger("1813");  // Kunci Privat

    public static BigInteger modularExponentiationSilent(BigInteger base, BigInteger exponent, BigInteger modulus) {
        BigInteger result = BigInteger.ONE;
        base = base.mod(modulus);

        while (exponent.compareTo(BigInteger.ZERO) > 0) {
            if (exponent.mod(BigInteger.TWO).equals(BigInteger.ONE)) {
                result = result.multiply(base).mod(modulus);
            }
            exponent = exponent.shiftRight(1);
            base = base.multiply(base).mod(modulus);
        }

        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) { // Menu utama
            System.out.println("\n===== MENU PROGRAM =====");
            System.out.println("1. Convert Text To ASCII");
            System.out.println("2. Enkripsi RSA");
            System.out.println("3. Dekripsi RSA");
            System.out.println("4. Enkripsi El Gamal");
            System.out.println("5. Dekripsi El Gamal");
            System.out.println("6. List Hasil Enkripsi RSA");
            System.out.println("7. List Hasil Dekripsi RSA");
            System.out.println("8. List Hasil ASCII RSA");
            System.out.println("9. List Hasil Enkripsi El Gamal");
            System.out.println("10. List Hasil Dekripsi El Gamal");
            System.out.println("11. List Hasil ChipherText El Gamal");
            System.out.println("12. Convert PDF");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu (0-12): ");

            int pilihan = scanner.nextInt();
            scanner.nextLine(); // Membersihkan newline dari input

            switch (pilihan) {
                //convert ASCII
                case 1:
                    ascii.convertTextToASCII(scanner);
                    break;
                case 2:
                    //Enkripsi RSA
                    if (asciiBlocks.isEmpty()) {
                        System.out.println("❌ Anda harus melakukan konversi ASCII terlebih dahulu! (Pilih opsi 1)");
                    } else {
                        enkripsi.encryptBlocks();
                    }
                    break;
                case 3:
                    //Dekripsi RSA
                    if (encryptedBlocks.isEmpty()) {
                        System.out.println("❌ Anda harus melakukan Enkripsi terlebih dahulu! (Pilih opsi 2)");
                    } else {
                        dekripsi.decryptBlocks();
                    }
                    break;
                case 4:  // Menjalankan Enkripsi ElGamal
                    if (RSATool.asciiBlocks.isEmpty()) {
                        System.out.println("❌ Tidak ada data ASCII! Lakukan konversi ASCII terlebih dahulu (Pilih opsi 1)");
                    } else {
                        System.out.println("\n🔒 Menjalankan proses enkripsi ElGamal...");
                        enkripsiEl.encryptBlocks();  // Memanggil metode enkripsi ElGamal
                        System.out.println("✅ Proses enkripsi selesai!");
                    }
                    break;
                case 5:  // Menjalankan proses dekripsi
                    if (RSATool.encryptedAlpha.isEmpty() || RSATool.encryptedBeta.isEmpty()) {
                        System.out.println("❌ Tidak ada data untuk didekripsi! Lakukan enkripsi terlebih dahulu (Pilih opsi 5).");
                    } else {
                        System.out.println("\n🔓 Menjalankan proses dekripsi...");
                        dekripsiEl.decryptMessages(RSATool.encryptedAlpha, RSATool.encryptedBeta);  // Panggil metode dengan parameter
                        System.out.println("✅ Proses dekripsi selesai!");
                    }
                    break;
                case 6:
                    //List Hasil Enkripsi RSA
                    if (encryptedBlocks.isEmpty()) {
                        System.out.println("❌ Tidak ada hasil enkripsi! (Pilih opsi 2 untuk mengenkripsi)");
                    } else {
                        System.out.println("\n🔒 HASIL ENKRIPSI:");
                        for (int i = 0; i < encryptedBlocks.size(); i++) {
                            System.out.println("C" + (i + 1) + " = " + encryptedBlocks.get(i));
                        }
                    }
                    break;
                case 7:
                    //List Hasil Dekripsi RSA
                    if (encryptedBlocks.isEmpty()) {
                        System.out.println("❌ Tidak ada hasil enkripsi! (Pilih opsi 2 untuk mengenkripsi)");
                    } else {
                        System.out.println("\n🔒 HASIL ENKRIPSI:");
                        for (int i = 0; i < decryptedBlocks.size(); i++) {
                            System.out.println("C" + (i + 1) + " = " + decryptedBlocks.get(i));
                        }
                    }
                    break;
                case 8:
                    if (asciiBlocks.isEmpty()) {
                        System.out.println("❌ Tidak ada hasil konversi ASCII! (Pilih opsi 1 untuk melakukan konversi)");
                    } else {
                        System.out.println("\n🔡 HASIL KONVERSI ASCII:");
                        for (int i = 0; i < asciiBlocks.size(); i++) {
                            System.out.println("C" + (i + 1) + " = " + asciiBlocks.get(i));
                        }
                    }
                    break;
                case 9:
                    //List Hasil Enkripsi El Gamal
                    if (RSATool.encryptedAlpha.isEmpty()) {
                        System.out.println("❌ Tidak ada hasil enkripsi! (Pilih opsi 2 untuk mengenkripsi)");
                    } else {
                        System.out.println("\n🔒 HASIL ENKRIPSI EL GAMAL:");
                        for (int i = 0; i < RSATool.encryptedAlpha.size(); i++) {
                            System.out.println("Blok " + (i + 1) + ":");
                            System.out.println("   α = " + RSATool.encryptedAlpha.get(i));
                            System.out.println("   β = " + RSATool.encryptedBeta.get(i));
                            System.out.println("---------------------------");
                        }
                    }
                    break;
                case 10:
                    // Dekripsi El Gamal tanpa menampilkan langkah-langkah
                    if (encryptedAlpha.isEmpty() || encryptedBeta.isEmpty()) {
                        System.out.println("❌ Tidak ada hasil enkripsi! (Pilih opsi 2 untuk mengenkripsi)");
                    } else {
                        System.out.println("\n🔓 HASIL DEKRIPSI EL GAMAL:");

                        for (int i = 0; i < encryptedAlpha.size(); i++) {
                            BigInteger gamma = encryptedAlpha.get(i);
                            BigInteger delta = encryptedBeta.get(i);

                            // Gunakan fungsi modularExponentiationSilent() dari class ini sendiri
                            BigInteger inverseGamma = modularExponentiationSilent(gamma, PRIVATE_KEY, P);
                            BigInteger M = inverseGamma.multiply(delta).mod(P);

                            System.out.println("\n🔹 Hasil Dekripsi Blok " + (i + 1) + ":");
                            System.out.println("   y = " + inverseGamma);
                            System.out.println("   M = " + M + " (karakter: " + (char) M.intValue() + ")");
                        }
                    }
                    break;
                case 11:  // Misalnya case 10 untuk menampilkan CipherText
                    chipherText.displayCipherText();  // Memanggil method displayCipherText dari class ChipherText
                    break;
                case 12:
                    try {
                        System.out.print("Masukkan Nama: ");
                        String nama = scanner.nextLine();
                        System.out.print("Masukkan NIM: ");
                        String nim = scanner.nextLine();
                        System.out.print("Masukkan Kelas: ");
                        String kelas = scanner.nextLine();
                        System.out.print("Masukkan nama file (tanpa ekstensi .pdf): ");
                        String fileName = scanner.nextLine();

                        // Tangkap semua output
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        PrintStream printStream = new PrintStream(outputStream);
                        PrintStream originalOut = System.out;
                        System.setOut(printStream);

                        // Jalankan semua opsi yang menghasilkan output
                        enkripsiEl.encryptBlocks();
                        chipherText.displayCipherText();
                        dekripsiEl.decryptMessages(RSATool.encryptedAlpha, RSATool.encryptedBeta);

                        // Kembalikan output ke konsol
                        System.setOut(originalOut);

                        // Simpan hasil ke PDF dalam folder "OutputPDF"
                        String outputText = outputStream.toString();
                        String folderPath = "OutputPDF";
                        new File(folderPath).mkdir(); // Buat folder jika belum ada
                        String filePath = folderPath + "/" + fileName + ".pdf";

                        // Buat PDF dengan informasi tambahan
                        PDFGenerator.buatPDF(nama, nim, kelas, outputText, filePath);

                        System.out.println("✅ Output berhasil disimpan dalam PDF: " + filePath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 0:
                    System.out.println("Terima Kasih Sudah Menggunakan Program ini😊😊...");
                    System.out.println("Keluar dari program...");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("❌ Pilihan tidak valid!");
            }

        }
    }
}

class ascii {
    public static void convertTextToASCII(Scanner scanner) {
        System.out.print("Masukkan teks: ");
        String input = scanner.nextLine();

        // Cegah input kosong
        if (input.isEmpty()) {
            System.out.println("⚠️ Teks tidak boleh kosong!");
            return;
        }

        System.out.println("\n🔘 Pilih opsi pemrosesan teks:");
        System.out.println("1. Ubah semua huruf menjadi kapital (UPPERCASE)");
        System.out.println("2. Gunakan teks apa adanya (original)");

        System.out.print("Pilihan Anda (1/2): ");
        String pilihan = scanner.nextLine();

        switch (pilihan) {
            case "1":
                input = input.toUpperCase();
                System.out.println("✅ Teks telah diubah menjadi kapital.");
                break;
            case "2":
                System.out.println("✅ Teks akan digunakan apa adanya.");
                break;
            default:
                System.out.println("⚠️ Pilihan tidak valid! Menggunakan teks apa adanya sebagai default.");
        }

        System.out.println("\n=====================================");
        System.out.println("|  i  | Karakter  |  ASCII  |");
        System.out.println("=====================================");

        RSATool.asciiBlocks.clear(); // Reset daftar ASCII Blocks

        // Menampilkan tabel ASCII & Menyimpan ke asciiBlocks
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            int ascii = (int) c;
            String charDisplay = (c == ' ') ? "<spasi>" : String.valueOf(c);

            System.out.printf("| %3d | %-9s |   %3d   |\n", i + 1, charDisplay, ascii);

            // Simpan nilai ASCII sebagai BigInteger ke asciiBlocks
            RSATool.asciiBlocks.add(BigInteger.valueOf(ascii));
        }

        System.out.println("=====================================\n");
    }
}

class enkripsi {
    private static final BigInteger n = new BigInteger("3337"); // Sesuai dokumen
    private static final BigInteger e = new BigInteger("79");  // Sesuai dokumen

    public static void encryptBlocks() {
        if (RSATool.asciiBlocks.isEmpty()) {
            System.out.println("❌ Tidak ada data untuk dienkripsi! Konversi ASCII terlebih dahulu.");
            return;
        }

        RSATool.encryptedBlocks.clear(); // Reset hasil enkripsi sebelumnya
        System.out.println("\n🔒 PROSES ENKRIPSI:");

        for (int i = 0; i < RSATool.asciiBlocks.size(); i++) {
            BigInteger block = RSATool.asciiBlocks.get(i);
            BigInteger encryptedBlock = modularExponentiation(block, e, n);
            RSATool.encryptedBlocks.add(encryptedBlock); // Simpan hasil enkripsi

            // Menampilkan hasil enkripsi
            System.out.println("\n✅ Ditemukan nilai C" + (i + 1) + " = " + encryptedBlock);
            System.out.println("🔹 Hasil Enkripsi M = " + block + "^" + e + " mod " + n + " adalah " + encryptedBlock);
            System.out.println(" ");
        }

        System.out.println("\n✅ Semua blok telah dienkripsi dan disimpan untuk dekripsi!");
    }

    public static BigInteger modularExponentiation(BigInteger base, BigInteger exponent, BigInteger mod) {
        BigInteger result = BigInteger.ONE;
        BigInteger power = base.mod(mod);
        System.out.println(base + "^1 mod " + mod + "  = " + power);

        BigInteger[] intermediateResults = new BigInteger[80];
        intermediateResults[1] = power;

        for (int i = 2; i <= 64; i *= 2) {
            BigInteger beforeMod = intermediateResults[i / 2].multiply(intermediateResults[i / 2]);
            power = beforeMod.mod(mod);
            intermediateResults[i] = power;
            System.out.println(base + "^" + i + " mod " + mod + " = [" + base + "^" + (i / 2) + " mod " + mod + "] . [" + base + "^" + (i / 2) + " mod " + mod + "] mod " + mod);
            System.out.println("                = " + beforeMod + " mod " + mod);
            System.out.println("                = " + power);
        }

        BigInteger beforeMod72 = intermediateResults[8].multiply(intermediateResults[64]);
        intermediateResults[72] = beforeMod72.mod(mod);
        System.out.println(base + "^72 mod " + mod + " = [" + base + "^8 mod " + mod + "] . [" + base + "^64 mod " + mod + "] mod " + mod);
        System.out.println("               = " + beforeMod72 + " mod " + mod);
        System.out.println("               = " + intermediateResults[72]);

        BigInteger beforeMod76 = intermediateResults[4].multiply(intermediateResults[72]);
        intermediateResults[76] = beforeMod76.mod(mod);
        System.out.println(base + "^76 mod " + mod + " = [" + base + "^4 mod " + mod + "] . [" + base + "^72 mod " + mod + "] mod " + mod);
        System.out.println("               = " + beforeMod76 + " mod " + mod);
        System.out.println("               = " + intermediateResults[76]);

        BigInteger beforeMod78 = intermediateResults[2].multiply(intermediateResults[76]);
        intermediateResults[78] = beforeMod78.mod(mod);
        System.out.println(base + "^78 mod " + mod + " = [" + base + "^2 mod " + mod + "] . [" + base + "^76 mod " + mod + "] mod " + mod);
        System.out.println("               = " + beforeMod78 + " mod " + mod);
        System.out.println("               = " + intermediateResults[78]);

        System.out.println(base + "^79 mod " + mod + " = [" + base + "^1 mod " + mod + "] . [" + base + "^78 mod " + mod + "] mod " + mod);

        BigInteger beforeMod79 = intermediateResults[1].multiply(intermediateResults[78]);
        System.out.println("               = " + beforeMod79 + " mod " + mod);

        BigInteger finalResult = beforeMod79.mod(mod);
        System.out.println("               = " + finalResult);

        intermediateResults[79] = finalResult;

        return intermediateResults[79];
    }
}

class dekripsi {
    private static final BigInteger n = new BigInteger("3337"); // Modulus
    private static final BigInteger d = new BigInteger("1019"); // Kunci privat

    public static void decryptBlocks() {
        if (RSATool.encryptedBlocks.isEmpty()) {
            System.out.println("❌ Tidak ada data untuk didekripsi! Lakukan enkripsi terlebih dahulu.");
            return;
        }

        RSATool.decryptedBlocks.clear(); // Reset hasil sebelumnya
        System.out.println("\n🔓 PROSES DEKRIPSI:");

        for (int i = 0; i < RSATool.encryptedBlocks.size(); i++) {
            BigInteger encryptedBlock = RSATool.encryptedBlocks.get(i);
            BigInteger decryptedBlock = modularExponentiation(encryptedBlock, d, n);

            // **Simpan hasil ke dalam decryptedBlocks**
            RSATool.decryptedBlocks.add(decryptedBlock);

            // **Menampilkan hasil dekripsi dengan cara perhitungan**
            System.out.println("\n✅ Ditemukan nilai M" + (i + 1) + " = " + decryptedBlock);
            System.out.println("🔹 Hasil Dekripsi C" + (i + 1) + " = " + encryptedBlock + "^" + d + " mod " + n + " adalah " + decryptedBlock);
            System.out.println(" ");
        }

        System.out.println("\n✅ Semua blok telah berhasil didekripsi!");
    }

    public static BigInteger modularExponentiation(BigInteger base, BigInteger exponent, BigInteger mod) {
        int[] exponents = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 768, 896, 960, 992, 1008, 1016, 1018, 1019};
        BigInteger[] values = new BigInteger[exponents.length];

        values[0] = base.mod(mod);
        System.out.println(base + "^" + exponents[0] + " mod " + mod + " = " + values[0]);

        for (int i = 1; i < exponents.length; i++) {
            if (exponents[i] == exponents[i - 1] * 2) {
                System.out.println(base + "^" + exponents[i] + " mod " + mod + " = [" + base + "^" + exponents[i - 1] + " mod " + mod + "] . [" + base + "^" + exponents[i - 1] + " mod " + mod + "] mod " + mod);
                System.out.println("               = [" + values[i - 1] + "] . [" + values[i - 1] + "] mod " + mod);
                values[i] = (values[i - 1].multiply(values[i - 1])).mod(mod);
            } else {
                int factorIndex = -1;
                for (int j = i - 1; j >= 0; j--) {
                    if (exponents[j] == exponents[i] - exponents[i - 1]) {
                        factorIndex = j;
                        break;
                    }
                }
                if (factorIndex != -1) {
                    System.out.println(base + "^" + exponents[i] + " mod " + mod + " = [" + base + "^" + exponents[i - 1] + " mod " + mod + "] . [" + base + "^" + exponents[factorIndex] + " mod " + mod + "] mod " + mod);
                    System.out.println("               = [" + values[i - 1] + "] . [" + values[factorIndex] + "] mod " + mod);
                    values[i] = (values[i - 1].multiply(values[factorIndex])).mod(mod);
                } else {
                    values[i] = (values[i - 1].multiply(values[i - 1])).mod(mod);
                }
            }
            System.out.println("               = " + (values[i - 1].multiply(values[i - 1])) + " mod " + mod);
            System.out.println("               = " + values[i]);
        }

        return values[values.length - 1];
    }
}

class enkripsiEl {
    public static final BigInteger p = new BigInteger("2579"); // Modulus
    public static final BigInteger g = new BigInteger("2"); // Basis α
    public static final BigInteger y = new BigInteger("949"); // Basis β

    public static void encryptBlocks() {
        if (RSATool.asciiBlocks.isEmpty()) {
            System.out.println("❌ Tidak ada data ASCII! Lakukan konversi ASCII terlebih dahulu (Pilih opsi 1)");
            return;
        }
        RSATool.encryptedAlpha.clear();
        RSATool.encryptedBeta.clear();
        Random random = new Random();
        System.out.println("\n🔒 PROSES ENKRIPSI EL GAMAL:");

        for (int i = 0; i < RSATool.asciiBlocks.size(); i++) {
            BigInteger asciiValue = RSATool.asciiBlocks.get(i);
            int k = random.nextInt(50) + 1;
            System.out.println("\n✅ Blok " + (i + 1) + " - ASCII: " + asciiValue + " (k = " + k + ")");

            BigInteger alpha = modularExponentiation(g, k, p);
            RSATool.encryptedAlpha.add(alpha);

            BigInteger betaBase = modularExponentiation(y, k, p);
            BigInteger beforeModBeta = betaBase.multiply(asciiValue);
            BigInteger beta = beforeModBeta.mod(p);
            RSATool.encryptedBeta.add(beta);

            System.out.println(y + "^" + k + " mod " + p + " = " + betaBase);
            System.out.println(y + "^" + k + " × " + asciiValue + " mod " + p);
            System.out.println("= " + beforeModBeta + " mod " + p);
            System.out.println("= " + beta);
            System.out.println("🔹 Hasil Enkripsi Blok " + (i + 1) + ": ASCII (M) = " + asciiValue + ", α = " + alpha + ", β = " + beta);
        }
    }

    public static BigInteger modularExponentiation(BigInteger base, int exponent, BigInteger mod) {
        BigInteger result = BigInteger.ONE;
        BigInteger power = base.mod(mod);
        System.out.println(base + "^1 mod " + mod + " = " + power);

        BigInteger[] intermediateResults = new BigInteger[exponent + 1];
        intermediateResults[1] = power;
        boolean isPowerOfTwo = (exponent & (exponent - 1)) == 0;

        for (int i = 2; i <= exponent; i *= 2) {
            BigInteger beforeMod = intermediateResults[i / 2].multiply(intermediateResults[i / 2]);
            power = beforeMod.mod(mod);
            intermediateResults[i] = power;
            System.out.println(base + "^" + i + " mod " + mod + " = [" + base + "^" + (i / 2) + " mod " + mod + "] × [" + base + "^" + (i / 2) + " mod " + mod + "] mod " + mod);
            System.out.println("= (" + intermediateResults[i / 2] + " × " + intermediateResults[i / 2] + ") mod " + mod);
            System.out.println("= " + power);
        }
        if (isPowerOfTwo) return intermediateResults[exponent];

        BigInteger beforeFinalMod = BigInteger.ONE;
        StringBuilder exponentExpression = new StringBuilder();
        StringBuilder numericExpression = new StringBuilder();
        StringBuilder decomposition = new StringBuilder("🔹 Menghitung hasil akhir " + base + "^" + exponent + " mod " + mod + ":");
        decomposition.append("\n" + base + "^" + exponent + " mod " + mod + " = ");

        int remainingExp = exponent;
        boolean first = true;
        while (remainingExp > 0) {
            int highestPower = 1;
            while (highestPower * 2 <= remainingExp) highestPower *= 2;
            remainingExp -= highestPower;
            if (!first) {
                exponentExpression.append(" × ");
                numericExpression.append(" × ");
            }
            first = false;
            exponentExpression.append("[" + base + "^" + highestPower + " mod " + mod + "]");
            numericExpression.append(intermediateResults[highestPower]);
            beforeFinalMod = beforeFinalMod.multiply(intermediateResults[highestPower]);
        }
        decomposition.append(exponentExpression).append(" mod ").append(mod);
        decomposition.append("\n= (").append(numericExpression).append(") mod ").append(mod);
        decomposition.append("\n= ").append(beforeFinalMod).append(" mod ").append(mod);

        BigInteger finalResult = beforeFinalMod.mod(mod);
        decomposition.append("\n= ").append(finalResult);
        System.out.println(decomposition);
        return finalResult;
    }
}

class chipherText {
    public static void displayCipherText() {
        if (RSATool.encryptedAlpha.isEmpty() || RSATool.encryptedBeta.isEmpty()) {
            System.out.println("❌ Tidak ada hasil enkripsi! Silakan lakukan enkripsi terlebih dahulu.");
            return;
        }

        System.out.println("\n🔐 ChipherText:");
        List<BigInteger> alphaList = RSATool.encryptedAlpha;
        List<BigInteger> betaList = RSATool.encryptedBeta;

        // Format output sesuai permintaan
        int count = 0;
        for (int i = 0; i < alphaList.size(); i++) {
            System.out.print("(" + alphaList.get(i) + ", " + betaList.get(i) + ") ");
            count++;

            // Tambahkan baris baru setiap 5 pasangan
            if (count % 8 == 0) {
                System.out.println();
            }
        }
    }
}

class dekripsiEl {
    private static final BigInteger P = new BigInteger("2579"); // Modulus
    private static final BigInteger PRIVATE_KEY = new BigInteger("1813"); // Kunci privat
    public static List<BigInteger> decryptedMessages = new ArrayList<>(); // Menyimpan hasil M
    public static StringBuilder hasilDekripsiTeks = new StringBuilder(); // Hasil karakter
    public static List<BigInteger> yList = new ArrayList<>();

    public static void decryptMessages(List<BigInteger> encryptedAlpha, List<BigInteger> encryptedBeta) {
        System.out.println("\n🔓 PROSES DEKRIPSI EL GAMAL");
        decryptedMessages.clear();  // Reset sebelum digunakan kembali
        yList.clear(); // Reset y
        hasilDekripsiTeks.setLength(0);

        for (int i = 0; i < encryptedAlpha.size(); i++) {
            BigInteger gamma = encryptedAlpha.get(i);
            BigInteger delta = encryptedBeta.get(i);
            System.out.println("\n✅ Blok " + (i + 1) + " - Dekripsi dengan γ = " + gamma + ", δ = " + delta);

            BigInteger inverseGamma = modularExponentiation(gamma, PRIVATE_KEY, P);
            BigInteger beforeMod = inverseGamma.multiply(delta);
            BigInteger M = beforeMod.mod(P);

            decryptedMessages.add(M); // Simpan hasil M
            yList.add(inverseGamma); // Simpan y
            hasilDekripsiTeks.append((char) M.intValue()); // Gabungkan sebagai karakter

            System.out.println("\n✅ Perhitungan: M = (" + inverseGamma + " × " + delta + ") mod " + P);
            System.out.println("  = " + beforeMod + " mod " + P);
            System.out.println("  = " + M);
            System.out.println("\n🔹 Hasil Dekripsi Blok " + (i + 1) + ": y = " + inverseGamma + ", M = " + M + " (Karakter: '" + (char) M.intValue() + "')");
        }
    }

    public static BigInteger modularExponentiation(BigInteger base, BigInteger exponent, BigInteger mod) {
        System.out.println(base + "^1 mod " + mod + " = " + base.mod(mod));
        BigInteger[] powerTable = new BigInteger[11];
        powerTable[0] = base.mod(mod);
        for (int i = 1; i < powerTable.length; i++) {
            BigInteger previousPower = powerTable[i - 1];
            BigInteger beforeMod = previousPower.multiply(previousPower);
            powerTable[i] = beforeMod.mod(mod);
            System.out.println(base + "^" + (1 << i) + " mod " + mod + " = [" + base + "^" + (1 << (i - 1)) + " mod " + mod + "] × [" + base + "^" + (1 << (i - 1)) + " mod " + mod + "] mod " + mod);
            System.out.println("                = (" + previousPower + " × " + previousPower + ") mod " + mod);
            System.out.println("                = " + beforeMod + " mod " + mod);
            System.out.println("                = " + powerTable[i]);
        }

        BigInteger beforeFinalMod = BigInteger.ONE;
        StringBuilder exponentExpression = new StringBuilder();
        StringBuilder numericExpression = new StringBuilder();
        StringBuilder decomposition = new StringBuilder("\n🔹 Menghitung hasil akhir " + base + "^" + exponent + " mod " + mod + ":");
        decomposition.append("\n" + base + "^" + exponent + " mod " + mod + " = ");
        int exp = exponent.intValue();
        boolean first = true;
        for (int i = powerTable.length - 1; i >= 0; i--) {
            int power = 1 << i;
            if (exp >= power) {
                exp -= power;
                if (!first) {
                    exponentExpression.append(" × ");
                    numericExpression.append(" × ");
                }
                first = false;
                exponentExpression.append("[" + base + "^" + power + " mod " + mod + "]");
                numericExpression.append(powerTable[i]);
                beforeFinalMod = beforeFinalMod.multiply(powerTable[i]);
            }
        }
        decomposition.append(exponentExpression).append(" mod ").append(mod);
        decomposition.append("\n                = (" + numericExpression + ") mod " + mod);
        decomposition.append("\n                = " + beforeFinalMod + " mod " + mod);
        decomposition.append("\n                = " + beforeFinalMod.mod(mod));
        System.out.println(decomposition);
        return beforeFinalMod.mod(mod);
    }
}

class PDFGenerator {
    public static void buatPDF(String nama, String nim, String kelas, String text, String filePath) {
        try {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Font yang digunakan
            PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont regularFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            // Judul laporan
            document.add(new Paragraph("LAPORAN HASIL ENKRIPSI & DEKRIPSI")
                    .setFont(boldFont)
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("\nNama: " + nama).setFont(boldFont).setFontSize(12));
            document.add(new Paragraph("NIM: " + nim).setFont(boldFont).setFontSize(12));
            document.add(new Paragraph("Kelas: " + kelas).setFont(boldFont).setFontSize(12));

            // 🔽 Tambahan: Menampilkan tabel ASCII
            // Garis pemisah
            document.add(new LineSeparator(new SolidLine()).setMarginTop(10).setMarginBottom(10));

            // **Menampilkan Hasil Konversi ASCII dalam Format 3 Kolom**
            document.add(new Paragraph("🔡 HASIL KONVERSI ASCII:")
                    .setFont(boldFont).setFontSize(14));

            if (RSATool.asciiBlocks == null || RSATool.asciiBlocks.isEmpty()) {
                document.add(new Paragraph("❌ Tidak ada hasil konversi ASCII! (Pilih opsi 1 untuk melakukan konversi)")
                        .setFont(regularFont).setFontSize(12));
            } else {
                Table table = new Table(new float[]{1, 1, 1});
                table.setWidth(UnitValue.createPercentValue(100));

                for (int i = 0; i < RSATool.asciiBlocks.size(); i++) {
                    table.addCell(new Cell().add(new Paragraph("C" + (i + 1) + " = " + RSATool.asciiBlocks.get(i)))
                            .setFont(regularFont)
                            .setFontSize(12)
                            .setBorder(Border.NO_BORDER));
                }

                document.add(table);
            }

            // Garis pemisah awal
            document.add(new LineSeparator(new SolidLine()).setMarginTop(10).setMarginBottom(10));

            // Memproses teks per baris
            String[] lines = text.split("\n");
            for (String line : lines) {
                if (line.contains("PROSES ENKRIPSI EL GAMAL") || line.contains("PROSES DEKRIPSI EL GAMAL") || line.contains("ChipherText")) {
                    document.add(new LineSeparator(new SolidLine()).setMarginTop(5).setMarginBottom(5));
                    document.add(new Paragraph("\n" + line)
                            .setFont(boldFont)
                            .setFontSize(14));
                } else if (line.startsWith("Blok ")) {
                    document.add(new Paragraph("\n" + line)
                            .setFont(boldFont)
                            .setFontSize(12));
                } else if (line.matches(".*mod.*")) { // Jika baris mengandung "mod", berarti perhitungan modular
                    document.add(new Paragraph("    " + line) // Tambahkan indentasi untuk pemisahan perhitungan modular
                            .setFont(regularFont)
                            .setFontSize(12)
                            .setTextAlignment(TextAlignment.LEFT));
                } else if (line.startsWith("Hasil Enkripsi Blok") || line.startsWith("Hasil Dekripsi Blok")) {
                    document.add(new LineSeparator(new SolidLine()).setMarginTop(5).setMarginBottom(5));
                    document.add(new Paragraph(line)
                            .setFont(boldFont)
                            .setFontSize(12));
                } else if (line.startsWith("M =")) { // Menyorot hasil akhir M
                    document.add(new Paragraph(line)
                            .setFont(boldFont)
                            .setFontSize(16)  // Ukuran lebih besar
                            .setTextAlignment(TextAlignment.CENTER)); // Posisikan di tengah
                } else if (line.startsWith("α =") || line.startsWith("β =")) { // Menyorot hasil enkripsi
                    document.add(new Paragraph(line)
                            .setFont(boldFont)
                            .setFontSize(12));
                } else {
                    document.add(new Paragraph(line).setFont(regularFont).setFontSize(12));
                }
            }
            // 🔓 Menampilkan Hasil Dekripsi ElGamal
            document.add(new LineSeparator(new SolidLine()).setMarginTop(10).setMarginBottom(10));
            document.add(new Paragraph("🔓 HASIL DEKRIPSI EL GAMAL:")
                    .setFont(boldFont).setFontSize(14));

            if (dekripsiEl.decryptedMessages == null || dekripsiEl.decryptedMessages.isEmpty()) {
                document.add(new Paragraph("❌ Tidak ada hasil dekripsi! (Pilih opsi 10 untuk mendekripsi)")
                        .setFont(regularFont).setFontSize(12));
            } else {
                for (int i = 0; i < dekripsiEl.decryptedMessages.size(); i++) {
                    BigInteger y = dekripsiEl.yList.get(i); // 🔥 Ambil y dari daftar
                    BigInteger M = dekripsiEl.decryptedMessages.get(i);
                    char karakter = (char) M.intValue();

                    document.add(new Paragraph("\n🔹 Hasil Dekripsi Blok " + (i + 1) + ":")
                            .setFont(boldFont).setFontSize(12));
                    document.add(new Paragraph("   y = " + y)
                            .setFont(regularFont).setFontSize(12));
                    document.add(new Paragraph("   M = " + M + " (karakter: '" + karakter + "')")
                            .setFont(regularFont).setFontSize(12));
                }

                document.add(new Paragraph("\n✅ Hasil Gabungan: " + dekripsiEl.hasilDekripsiTeks.toString())
                        .setFont(boldFont).setFontSize(13)
                        .setTextAlignment(TextAlignment.CENTER));
            }

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}