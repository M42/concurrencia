import monitor.AbstractMonitor;
import monitor.Condition;


class Buffer extends AbstractMonitor { 
    private int numSlots = 0;
    private int cont = 0;
    private double[] buffer = null;
    private Condition no_vacio = makeCondition();
    private Condition no_lleno = makeCondition();

    public Buffer (int p_numSlots){ 
        numSlots = p_numSlots;
        buffer = new double[numSlots];
    }

    public void depositar(double valor) { 
        enter();

        while (cont == numSlots)
            no_lleno.await();
        buffer[cont] = valor; 
        cont++;
        no_vacio.signal();

        leave();
    }

    public double extraer() {
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


    public static void main(String [] args) {
        Buffer buffer = new Buffer(5);
        Productor[] productores = new Productor[4];
        Consumidor[] consumidores = new Consumidor[4];

        for (int i=0; i<4; i++) {
            productores[i] = new Productor(buffer, 10, i);
            consumidores[i] = new Consumidor(buffer, 10, i);
        }

        System.out.println("Empiezan a correr todos los procesos");
        for (Consumidor cons: consumidores) cons.thr.start();
        for (Productor prod: productores) prod.thr.start();
    }
}


class Productor implements Runnable {
    private Buffer bb ;
    int veces;
    int numP;
    Thread thr;
    
    public Productor(Buffer pbb, int pveces, int pnumP) { 
        bb = pbb;
        veces = pveces;
        numP = pnumP;
        thr = new Thread(this,"productor "+numP);
    }

    public void run() {
        try {
            double item = 100*numP;
            for(int i=0 ; i<veces ; i++) { 
                System.out.println(thr.getName()+", produciendo " + item);
                bb.depositar(item++);
            }
        }
        catch(Exception e) { 
            System.err.println("Excepcion en main: " + e);
        }
    }
}


class Consumidor implements Runnable {
    private Buffer bb;
    int veces;
    int numC;
    Thread thr;

    public Consumidor (Buffer pbb, int pveces, int pnumC) { 
        bb = pbb;
        veces = pveces;
        numC = pnumC;
        thr = new Thread(this,"consumidor "+numC);
    }
    
    public void run() { 
        try {
            for( int i=0 ; i<veces ; i++ ) { 
                double item = bb.extraer ();
                System.out.println(thr.getName()+", consumiendo "+item);
            }
        }
        catch(Exception e) { 
            System.err.println("Excepcion en main: " + e);
        }
    }
}

