class Estanquero implements Runnable {
    public Thread thr;
    
    public void run() {
        int ingrediente;
        while (true) {
            ingrediente = (int) (Math.random () * 3.0);
            estanco.ponerIngrediente(ingrediente);
            estanco.esperarRecogidaIngrediente();
        }
    }
}
