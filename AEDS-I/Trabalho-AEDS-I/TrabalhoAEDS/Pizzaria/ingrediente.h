#ifndef INGREDIENTE_H_INCLUDED
#define INGREDIENTE_H_INCLUDED

typedef struct ingredientes{
    int id;
    char nome[100];
    float preco;
}Ingredientes;

void cadastrarIngredientes(char d[]);
void visualizarIngredientes(char d[]);
void editarIngrediente(char d[]);
void excluirIngrediente(char d[]);

#endif // INGREDIENTE_H_INCLUDED
