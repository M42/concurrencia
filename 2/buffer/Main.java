public class Main {
    public static void main(String [] args) {
        Buffer buffer = new Buffer(5);
        Consumidor consumidor = new Consumidor(buffer, 10, 0);
        Productor productor   = new Productor (buffer, 10, 0);

        Thread thr_consum = new Thread(consumidor);
        Thread thr_produc = new Thread(productor);

        System.out.println("Empiezan a correr ambos procesos");
        thr_consum.start();
        thr_produc.start();
    }
}
