#ifndef PIZZA_H_INCLUDED
#define PIZZA_H_INCLUDED

#include "ingrediente.h"

typedef struct Pizzas{
    int id;
    char nome[100];
    char tamanho;
    float precoBase;
    struct ingredientes ingrediente[50];
}Pizzas;

void cadastrarPizzas(char c[]);
void visualizarPizzas(char c[]);
void excluirPizza(char c[]);
void editarPizza(char c[]);
void venderPizza(char c[], char d[]);

#endif // PIZZA_H_INCLUDED
