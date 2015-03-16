#include <time.h>
#include <unistd.h>
#include <stdlib.h>


void fumar();


int main() {
    // Random seed
    srand(time(NULL)); 

    
}


void fumar() {
    // Fuma un número aleatorio de milisegundos
    const unsigned miliseg = 100U + (rand() % 1900U) ;
    usleep( 1000U*miliseg );
}
