import monitor.AbstractMonitor;
import monitor.Condition;

class Estanco extends AbstractMonitor {
    int ingrediente = 0;
    private Condition papel = makeCondition();
    private Condition tabaco = makeCondition();
    private Condition cerilla = makeCondition();
    private Condition vacio = makeCondition();

    // invocado por cada fumador, indicando su ingrediente o numero
    public void obtenerIngrediente(int miIngrediente) {
        enter();
        
        while (ingrediente != miIngrediente) {
            switch (miIngrediente) {
            case 1: papel.await(); break;
            case 2: tabaco.await(); break;
            case 3: cerillla.await(); break;
            }
        }

        ingrediente = 0;
        vacio.signal();

        leave();
    }

    // invocado por el estanquero, indicando el ingrediente que pone
    public void ponerIngrediente(int ingrediente) {
        enter();
        this.ingrediente = ingrediente;

        switch (ingrediente) {
        case 1: papel.signal(); break;
        case 2: tabaco.signal(); break;
        case 3: cerilla.signal(); break;
        }
        
        leave();
    }

    // invocado por el estanquero
    public void esperarRecogidaIngrediente() {
        enter();
        vacio.await();
        leave();
    }
}
