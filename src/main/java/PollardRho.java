/**
 * Created by Vlad on 21/01/2017.
 */
public class PollardRho {
    private long n;

    public PollardRho(long number) {
        n = number;
    }

    public long factorize() {
        return computePollardFactor2(n);
        // return computePollardFactor(n);
    }

    private static long computePollardFactor2(long n) {
        long i = 1;
        long x = 2;
        long y = 2;
        long k = 2;
        while (true) {
            i++;
            x = polyValue(x) % n;
            long d = euclid(y-x, n);
            if (d != 1 && d != n){
                return d;
            }
            if (i == k) {
                y = x;
                k *= 2;
            }
        }
    }

    private static long computePollardFactor(long n) throws Exception
    {
        int polyDegree = 2;
        while (polyDegree <= 20) {
            long a = 2;
            long b = 2;
            long d = 1;
            int iterationCount = 0;
            while (d == 1) {
                a = polyValue(a) % n;
                b = polyValue(b) % n;
                b = polyValue(b) % n;
                d = euclid(b-a, n);
                iterationCount++;
            }

            System.out.println("Iteration count for degree " + polyDegree + " is: " + iterationCount);
            if (1 < d && d < n){
                return d;
            }

            polyDegree++;
        }

        throw new Exception("No factor could be found");
    }

    private static long polyValue(long x) {
        return x*x - 1;
    }

    private static long euclid (long a, long b)
    {
        while (b > 0) {
            long rest = a % b;
            a = b;
            b = rest;
        }

        return a;
    }
}
