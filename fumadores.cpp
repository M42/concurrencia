#include <time.h>
#include <unistd.h>
#include <stdlib.h>
#include <semaphore.h>
#include <pthread.h>
#include <string>
#include <iostream>
using namespace std;

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
    pthread_create(&estanquero, NULL, repartir, NULL);
    pthread_create(&fumador_sinpapel, NULL, consumir, (void*) 0);
    pthread_create(&fumador_sincerll, NULL, consumir, (void*) 1);
    pthread_create(&fumador_sintabac, NULL, consumir, (void*) 2);

    pthread_exit(NULL);
}

void* repartir(void*) {
    while (true) {
        int ingrediente = rand() % 3;
        string nombre_ing;

        switch (ingrediente) {
        case 0: nombre_ing = "papel"; break;
        case 1: nombre_ing = "cerillas"; break;
        case 2: nombre_ing = "tabaco"; break;
        }

        sem_wait(&mostrador);
        cerr << "Estanquero: reparto " << nombre_ing << endl;

        switch (ingrediente) {
        case 0: sem_post(&papel); break;
        case 1: sem_post(&cerll); break;
        case 2: sem_post(&tabac); break;
        }
    }
}

void* consumir(void* objeto) {
    sem_t* ingrediente;
    string nombre;

    switch (int(objeto)) {
    case 0: ingrediente = &papel; nombre = "Sinpapel";    break;
    case 1: ingrediente = &cerll; nombre = "Sincerillas"; break;
    case 2: ingrediente = &tabac; nombre = "Sintabaco";   break;
    }

    while (true) {
        sem_wait(ingrediente);
        sem_post(&mostrador);

        cerr << nombre << ": empiezo a fumar" << endl;
        fumar();
    }
}

void fumar() {
    // Fuma un número aleatorio de milisegundos
    const unsigned miliseg = 100U + (rand() % 1900U);
    usleep( 1000U*miliseg );
}
