import monitor.AbstractMonitor;
import monitor.Condition;
import java.util.Random;



class Estanco extends AbstractMonitor {
    int ingrediente = -1;
    private Condition papel = makeCondition();
    private Condition tabaco = makeCondition();
    private Condition cerilla = makeCondition();
    private Condition vacio = makeCondition();

    // invocado por cada fumador, indicando su ingrediente o numero
    public void obtenerIngrediente(int miIngrediente) {
        enter();
        
        while (ingrediente != miIngrediente) {
            switch (miIngrediente) {
            case 0: papel.await(); break;
            case 1: tabaco.await(); break;
            case 2: cerilla.await(); break;
            }
        }
        
        System.out.println("Obtengo el ingrediente: " + ingrediente);
        ingrediente = -1;
        vacio.signal();

        leave();
    }

    // invocado por el estanquero, indicando el ingrediente que pone
    public void ponerIngrediente(int ingrediente) {
        enter();
        this.ingrediente = ingrediente;
        System.out.println("Coloco un ingrediente: " + ingrediente);

        switch (ingrediente) {
        case 0: papel.signal(); break;
        case 1: tabaco.signal(); break;
        case 2: cerilla.signal(); break;
        }
        
        leave();
    }

    // invocado por el estanquero
    public void esperarRecogidaIngrediente() {
        enter();
        System.out.println("0Espero la recogida.");
        while (ingrediente != -1)
            vacio.await();
        leave();
    }


    public static void main(String [] args) {
        Estanco estanco = new Estanco();
        Estanquero estanquero = new Estanquero(estanco);
        Fumador fumador_papel = new Fumador(0, estanco);
        Fumador fumador_tabac = new Fumador(1, estanco);
        Fumador fumador_ceril = new Fumador(2, estanco);

        Thread thr_estanq = new Thread(estanquero);
        Thread thr_fpapel = new Thread(fumador_papel);
        Thread thr_ftabac = new Thread(fumador_tabac);
        Thread thr_fceril = new Thread(fumador_ceril);

        System.out.println("Empiezan a correr ambos procesos");
        estanquero.thr.start();
        fumador_papel.thr.start();
        fumador_tabac.thr.start();
        fumador_ceril.thr.start();
    }
}


class Estanquero implements Runnable {
    public Thread thr;
    Estanco estanco;

    public Estanquero(Estanco estanco) {
        this.estanco = estanco;
        thr = new Thread(this, "estanquero");
    }
    
    public void run() {
        int ingrediente;
        while (true) {
            ingrediente = (int) (3.0*Math.random());
            estanco.ponerIngrediente(ingrediente);
            estanco.esperarRecogidaIngrediente();
        }
    }
}


class Fumador implements Runnable {
    int miIngrediente;
    Estanco estanco;
    public Thread thr;

    public Fumador(int p_miIngrediente, Estanco estanco) {
        this.estanco = estanco;
        miIngrediente = p_miIngrediente;
        thr = new Thread(this, "fumador "+miIngrediente);
    }

    public void run() {
        while (true) {
            estanco.obtenerIngrediente(miIngrediente);
            Aux.dormir_max(2000);
        }
    }
}


class Aux {
    static Random genAlea = new Random();

    static void dormir_max(int milisecsMax) {
        try {
            Thread.sleep(genAlea.nextInt(milisecsMax));
        }
        catch(InterruptedException e) {
            System.err.println("sleep interumpido en ’aux.dormir_max()’");
        }
    }
}
