import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

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

    private int i = 6;
    private int[] ai = new int[i];
    private int[] bi = new int[i];
    private int[] bi_2_mod_n = new int[i];
    private double[] xi = new double[i];
    private List<Integer> B = new ArrayList<>(5);
    private List<List<Integer>> V = new ArrayList<>();

    public FractiiContinue(BigInteger n) {
        this.n = n;
        pasUnu();
        pasDoi();
        pasTrei();
        pasPatru();
        pasCinci();
        pasSase();
        pasSapte();
        pasOpt();
    }

    /**
     * Fie b_1 = 1, b0 = a0 = floor(sqrt(n)) si x0 = sqrt(n) - a0.
     */
    private void pasUnu() {
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
//        b0 = b0.modPow(BigInteger.valueOf(2), n);
        System.out.println("b0^2 = " + b0);
        ai[0] = a0.intValue();
        bi[0] = b0.intValue();
        xi[0] = x0.doubleValue();
    }

    /**
     * Fie ai = 1/(xi-1). Avem xi = 1/(xi-1)-ai
     */
    private void pasTrei() {
        System.out.println("----------------------------------------------------------------");
        System.out.print("i\t");
        for (int j = 0; j < i; j++) System.out.printf("%9d ", j);
        System.out.println();
        for (int j = 1; j < i; j++) {
            double sqrt = 1.0 / xi[j - 1];
            ai[j] = (int) sqrt;
            xi[j] = sqrt - ai[j];
        }
        System.out.printf("ai\t");
        for (int j = 0; j < i; j++) System.out.printf("%9d ", ai[j]);
        System.out.println();
        System.out.print("xi\t");
        for (int j = 0; j < i; j++) System.out.printf("  %8.3f", xi[j]);

        System.out.println();
    }

    /**
     * Calculeaza bi = a[i]*b[i-1]+b[i-2]
     */
    private void pasPatru() {
        for (int j = 1; j < i; j++) {
            try {
                bi[j] = ai[j] * bi[j - 1] + bi[j - 2];
            } catch (ArrayIndexOutOfBoundsException e) {
                bi[j] = ai[j] * bi[j - 1] + 1;
            }
            bi[j] %= n.intValue();
        }
        System.out.print("bi\t");
        for (int j = 0; j < i; j++) System.out.printf("%10d", bi[j]);
        System.out.println();
    }

    /**
     * Calculeaza bi^2 mod n
     */
    private void pasCinci() {
        for (int j = 0; j < i; j++) {
            bi_2_mod_n[j] = BigInteger.valueOf(bi[j]).modPow(BigInteger.valueOf(2), n).intValue();
            if (bi_2_mod_n[j] > 1000) {
                bi_2_mod_n[j] = n.intValue() % bi_2_mod_n[j];
                bi_2_mod_n[j] = bi_2_mod_n[j] * -1;
            }
        }
        System.out.printf("bi2 mod n");
        for (int j = 0; j < i; j++) System.out.printf("%6d   ", bi_2_mod_n[j]);
        System.out.println("\n----------------------------------------------------------------");
    }

    /**
     * Alegem dintre nr de la pct 5, acele nr care se factorizeaza in valoare absoluta
     * in numere prime mici.
     * Se formeaza baza de factori B alcatuita din -1 si nr prime ce apar in mai mult
     * decat un element bi^2 mod n sau care apar cu o putere para intr-un singur element
     */
    List<List<Integer>> candidati = new ArrayList<>();

    private void pasSase() {
        for (int aBi_2_mod_n : bi_2_mod_n) {
            List<Integer> factoriPrimi = primeFactors(aBi_2_mod_n);
            System.out.printf("%d = %s\n", aBi_2_mod_n, factoriPrimi.toString());

            if (isValid(factoriPrimi))
                candidati.add(factoriPrimi);
        }
        System.out.println("Number candidates");
        for (List<Integer> list : candidati) {
            System.out.println(list.toString());
        }
    }


    /**
     * We check for small number in a list of prime numbers
     * Check if the list has a number greater than 13. If so return false else true
     *
     * @param factoriPrimi
     * @return
     */
    private boolean isValid(List<Integer> factoriPrimi) {
        for (Integer integer : factoriPrimi) {
            if (integer > 13)
                return false;
        }
        return true;
    }

    /**
     * Pastreaza numerele candidate ce apar o singura data in numerele candidate
     * sau numerele care apar cu o putere para intr-un singur element
     */
    private void pasSapte() {
        B.add(-1);
        Map<Integer, Integer> numarFrecvente = candidati.stream()
                .flatMap(Collection::stream)
                .collect(groupingBy(Function.identity(), summingInt(value -> 1)));

        Collection<Integer> keySet = new ArrayList<>();
        numarFrecvente.forEach((number, frequency) -> {
            // adauga doar numerele care apar in cel putin doua elemente din bi^2 mod n sau
            // care apar cu o putere para intr-un singur element
            if (frequency > 1 && number > 0 || Math.sqrt(number) == 0)
                keySet.add(number);
        });

        B.addAll(keySet);
        System.out.printf("B = %s", B.toString());
    }

    private void pasOpt() {
        System.out.println();
        System.out.println("Pas 8: calcul vectori asociati lui B");
        for (int j = 0; j < candidati.size(); j++) {
            List<Integer> integers = new ArrayList<>(i);
            List<Integer> candidatiJ = candidati.get(j);

            for (int k = 0; k < i; k++) {
                integers.add(0);
                if (candidatiJ.contains(B.get(k)))
                    integers.set(k, 1);
            }
            V.add(integers);
        }
        for (int j = 0; j < V.size(); j++) {
            System.out.printf("v%d = %s\n", j, V.get(j));
        }
    }

    public static List<Integer> primeFactors(int numbers) {
        int absNumbers = Math.abs(numbers);
        List<Integer> factors = new ArrayList<>();
        if (numbers < 0)
            factors.add(-1);
        for (int i = 2; i <= absNumbers / i; i++) {
            while (absNumbers % i == 0) {
                factors.add(i);
                absNumbers /= i;
            }
        }
        if (absNumbers > 1) {
            factors.add(absNumbers);
        }
        return factors;
    }
}
