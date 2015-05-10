import monitor.AbstractMonitor;
import monitor.Condition;

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
        System.out.println("Espero la recogida del ingrediente.");
        while (ingrediente != -1)
            vacio.await();
        leave();
    }
}
