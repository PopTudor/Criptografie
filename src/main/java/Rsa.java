/**
 * Created by Tudor on 21-Jan-17.
 */
public class Rsa {
    private long P, Q, E, N, PHI,  D;


    private static final String NPQ_STRING = "n=p*q=";
    private static final String PHI_STRING = "Φ=(p-1)*(q-1)=";


    public long encrypt(long message) {
        int i;
        long mesajCriptat = 1;
        for (i = 0; i < E; i++)
            mesajCriptat = mesajCriptat * message % N;
        mesajCriptat = mesajCriptat % N;
        return mesajCriptat;
    }

    int check() {
        int i;
        for (i = 3; E % i == 0 && PHI % i == 0; )
            return 1;
        return 0;
    }

    long decrypt(long encrypted) {
        int i;
        long mesajCurat = 1;
        for (i = 0; i < D; i++)
            mesajCurat = mesajCurat * encrypted % N;
        mesajCurat = mesajCurat % N;
        return mesajCurat;
    }

    void genPrivateKey() {
        long s;
        D = 1;
        do {
            s = (D * E) % PHI;
            D++;
        } while (s != 1);
        D = D - 1;
    }
}
