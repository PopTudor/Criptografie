import org.apache.commons.lang.time.StopWatch;

import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

/**
 * Created by Tudor on 21-Jan-17.
 */
public class Main {


    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws IOException {
        // Generating the keys
        //testRsa();
//        testPollard();
        StopWatch sw = new StopWatch();
        sw.start();
        FractiiContinue fractiiContinue = new FractiiContinue(BigInteger.valueOf(10123),6);
        sw.stop();
        System.out.printf("\r\nContinued Fractions Factorization took %dms\r\n", sw.getTime());
    }

    private static void testPollard() throws IOException {
        Rsa rsa = new Rsa();
        System.out.printf("Using RSA with public key: (e, N) = (%d, %d)", rsa.getE(), rsa.getN());
        System.out.println();

        String teststring;
        System.out.println("Enter the plain text to encrypt:");
        DataInputStream in = new DataInputStream(System.in);
        teststring = in.readLine();
        System.out.println("Encrypting String: " + teststring);
        byte[] encrypted = rsa.encrypt(teststring.getBytes());

        PollardRho pr = new PollardRho(rsa.getN());
        StopWatch sw = new StopWatch();
        sw.start();
        long p = pr.factorize();
        sw.stop();
        System.out.printf("\r\nPollard Rho Factorization took %dms\r\n", sw.getTime());
        long q = rsa.getN() / p;
        long phi = (p-1)*(q-1);

        BigInteger d = BigInteger.valueOf(rsa.getE()).modInverse(BigInteger.valueOf(phi));
        System.out.printf("Pollard found p=%d; q=%d => phi=%d and finally d=%d\r\n\r\n", p, q, phi, d.longValue());

        rsa.assignPrivateKey(d);
        byte[] decrypted = rsa.decrypt(encrypted);
        System.out.println("Decrypted String: " + new String(decrypted) + "\r\n");
    }

    private static void testRsa() throws IOException {
        Random random = new Random();
        random.ints(0, 100);
        DataInputStream in = new DataInputStream(System.in);
        BigInteger p = BigInteger.valueOf(1873);
        BigInteger q = BigInteger.valueOf(2029);
        BigInteger n = p.multiply(q);
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        BigInteger e = BigInteger.probablePrime(32 / 2, random);
        while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0) {
            e.add(BigInteger.ONE);
        }
        BigInteger d = e.modInverse(phi);


        Rsa rsa = new Rsa(e, d, n);
        String teststring;
        System.out.println("Enter the plain text:");
        teststring = in.readLine();
        System.out.println("Encrypting String: " + teststring);
        //System.out.println("String in Bytes: " + bytesToString(teststring.getBytes()));
        // encrypt
        byte[] encrypted = rsa.encrypt(teststring.getBytes());
        //System.out.println("Encrypted in string: " + bytesToString(encrypted));
        // decrypt
        byte[] decrypted = rsa.decrypt(encrypted);
        //System.out.println("Decrypting Bytes: " + bytesToString(decrypted));
        System.out.println("Decrypted String: " + new String(decrypted));
    }

    private static String bytesToString(byte[] encrypted) {
        String test = "";
        for (byte b : encrypted) {
            test += Byte.toString(b);
        }
        return test;
    }
}
