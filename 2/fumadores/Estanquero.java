class Estanquero implements Runnable {
    public Thread thr;

    public Estanquero() {
        thr = new Thread(this, "estanquero");
    }
    
    public void run() {
        int ingrediente;
        while (true) {
            ingrediente = (int) (Math.random() * 3.0);
            estanco.ponerIngrediente(ingrediente);
            estanco.esperarRecogidaIngrediente();
        }
    }
}
