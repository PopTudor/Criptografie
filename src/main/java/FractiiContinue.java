import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Toate calculele se fac modulo n, suma si produsul ind reduse
 * modulo n la cel mai mic rest pozitiv (sau absolut la Pasul 5.).
 * Input: un numar compus n.
 * Output: un factor netrivial al lui n.
 * <br><br>
 * Created by Tudor on 21-Jan-17.
 */
public class FractiiContinue {
    private BigInteger b_1;
    private BigInteger b0, a0;
    private BigDecimal x0;
    private BigInteger n;

    private int i = 5;
    private int[] ai = new int[i];
    private int[] bi = new int[i];
    private int[] bi_2_mod_n = new int[i];
    private int[] xi = new int[i];

    public FractiiContinue(BigInteger n) {
        this.n = n;
        pasUnu(n);
        pasDoi();
        pasTrei();
    }

    /**
     * Fie b_1 = 1, b0 = a0 = floor(sqrt(n)) si x0 = sqrt(n) - a0.
     *
     * @param n
     */
    private void pasUnu(BigInteger n) {
        b_1 = BigInteger.ONE;
        double sqrt_n = Math.sqrt(n.doubleValue());
        b0 = a0 = BigInteger.valueOf((long) Math.floor(sqrt_n));
        x0 = BigDecimal.valueOf(sqrt_n).subtract(BigDecimal.valueOf(a0.doubleValue())).setScale(3, BigDecimal.ROUND_DOWN);

        System.out.println("a0 = b0 = " + a0);
        System.out.println("x0 = " + x0);
    }

    /**
     * Calculam b0^2 mod n(adica b0^2 - n)
     */
    private void pasDoi() {
        b0 = b0.modPow(BigInteger.valueOf(2), n);
        System.out.println("b0^2 = " + b0);
    }

    /**
     * Fie ai = 1/(xi-1). Avem xi = 1/(xi-1)-ai
     */
    private void pasTrei() {
        for (int j = 0; j < i; j++) {
            double sqrt_n = (int) Math.sqrt(n.doubleValue());
            ai[j] = bi[j] = (int) sqrt_n;
            xi[j] = (int) (sqrt_n - ai[j]);
            System.out.println("ai");
        }
    }
}
