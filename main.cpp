#include <iostream>
#include <pthread.h>
#include <semaphore.h>

using namespace std;

const int num_items = 10;
const int tam_vect  = 5;
int buffer[tam_vect];
int pos_vect = 0;
sem_t puede_leer;
sem_t puede_escr;
sem_t mutex;


int producir_dato();
void consumir_dato(int dato);
void* productor(void*);
void* consumidor(void*);


int main() {
    pthread_t hebra_cons, hebra_prod;

    sem_init(&puede_leer, 0, 0);
    sem_init(&puede_escr, 0, tam_vect);
    sem_init(&mutex, 0, 1);

    pthread_create(&hebra_cons, NULL, consumidor, NULL);
    pthread_create(&hebra_prod, NULL, productor, NULL);
    pthread_exit(NULL);
}


int producir_dato() {
    static int contador = 1;
    return contador++;
}

void consumir_dato(int dato) {
    cout << "dato recibido: " << dato << endl;
}

void* productor(void*) {
    for (unsigned i=0; i<num_items; i++) {
        int dato = producir_dato();
        
        // Escribir dato al vector
        sem_wait(&puede_escr);
        sem_wait(&mutex);

        buffer[pos_vect] = dato;
        cerr << "Escrito: " << dato << "  En pos: " << pos_vect << endl;
        pos_vect++;

        sem_post(&mutex);
        sem_post(&puede_leer);
    }
    return NULL;
}

void* consumidor(void*) {
    for (unsigned i=0; i<num_items; i++) {
        int dato;

        // Leer dato desde vector
        sem_wait(&puede_leer);
        sem_wait(&mutex);

        pos_vect--;
        dato = buffer[pos_vect];
        cerr << "Leido: " << dato << "  En pos: " << pos_vect << endl;

        sem_post(&mutex);
        sem_post(&puede_escr);

        consumir_dato(dato);

    }
}
