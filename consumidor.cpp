#include <iostream>
#include <pthread.h>
#include <semaphore.h>

using namespace std;

const int num_items = 20;
const int tam_vect  = 8;
int buffer[tam_vect];
int primera_libre = 0;
sem_t puede_leer; // Posiciones ocupadas en el vector
sem_t puede_escr; // Posiciones restantes en el vector
sem_t mutex;      // Exclusión mutua


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

    cout << "fin" << endl;
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

        buffer[primera_libre] = dato;
        cerr << "Escrito: " << dato << "  En pos: " << primera_libre << endl;
        primera_libre++;

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

        primera_libre--;
        dato = buffer[primera_libre];
        cerr << "Leido: " << dato << "  En pos: " << primera_libre << endl;

        sem_post(&mutex);
        sem_post(&puede_escr);

        consumir_dato(dato);
    }
}
