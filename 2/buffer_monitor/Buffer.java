import monitor.AbstractMonitor;
import monitor.Condition;

public class Buffer extends AbstractMonitor { 
    private int numSlots = 0;
    private int cont = 0;
    private double[] buffer = null;
    private Condition no_vacio = makeCondition();
    private Condition no_lleno = makeCondition();

    public Buffer (int p_numSlots){ 
        numSlots = p_numSlots;
        buffer = new double[numSlots];
    }

    public void depositar( double valor ) throws InterruptedException { 
        enter();

        while (cont == numSlots)
            no_lleno.await();
        buffer[cont] = valor; 
        cont++;
        no_vacio.signal();

        leave();
    }

    public synchronized double extraer() throws InterruptedException {
        enter();
        double valor;

        while (cont == 0)
            no_vacio.await();
        cont--; 
        valor = buffer[cont];
        no_lleno.signal();

        leave();
        return valor;
    }
}
