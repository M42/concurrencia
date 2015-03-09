#include <iostream>
#include <pthread.h>

using namespace std;

const int num_items = 100;
const int tam_vect  = 10;

int producir_dato();
void consumir_dato();
void* productor(void*);


int main() {
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
        // Leer dato desde vector
    }
    return NULL;
}

void* consumidor(void*) {
    for (unsigned i=0; i<num_items; i++) {
        int dato;
        // Leer dato desde vector
        consumir_dato(dato);
    }
}
