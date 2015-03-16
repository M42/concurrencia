#include <time.h>
#include <unistd.h>
#include <stdlib.h>
#include <semaphore.h>
#include <pthread.h>

void fumar();
void* consumir(void*);
void* repartir(void*);
sem_t mostrador;
sem_t papel;
sem_t cerll;
sem_t tabac;

int main() {
    // Random seed
    srand(time(NULL)); 

    // Hebras
    pthread_t fumador_sinpapel;
    pthread_t fumador_sincerll;
    pthread_t fumador_sintabac;
    pthread_t estanquero;

    // Semáforos
    sem_init(&mostrador, 0, 1);
    sem_init(&papel, 0, 0);
    sem_init(&cerll, 0, 0);
    sem_init(&tabac, 0, 0);

    // Inicializa hebras
}

void* repartir(void*) {
    while (true) {
        int ingrediente = rand() % 3;

        sem_wait(&mostrador);

        switch (ingrediente) {
        case 0: sem_post(&papel); break;
        case 1: sem_post(&cerll); break;
        case 2: sem_post(&tabac); break;
        }
    }
}

void* consumir(void* objeto) {
    sem_t& ingrediente;

    switch (int(objeto)) {
    case 0: ingrediente = papel; break;
    case 1: ingrediente = cerll; break;
    case 2: ingrediente = tabac; break;
    }

    while (true) {
        sem_wait(ingrediente);
        sem_post(&mostrador);
        fumar();
    }
}

void fumar() {
    // Fuma un número aleatorio de milisegundos
    const unsigned miliseg = 100U + (rand() % 1900U) ;
    usleep( 1000U*miliseg );
}
