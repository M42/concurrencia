class Fumador implements Runnable {
    int miIngrediente;
    public Thread thr;

    public Fumador(int p_miIngrediente) {
        miIngrediente = p_miIngrediente;
        thr = new Thread(this, "fumador "+miIngrediente);
    }

    public void run() {
        while (true) {
            estanco.obtenerIngrediente(miIngrediente);
            aux.dormir_max(2000);
        }
    }
}
