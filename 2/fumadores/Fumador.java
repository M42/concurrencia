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
