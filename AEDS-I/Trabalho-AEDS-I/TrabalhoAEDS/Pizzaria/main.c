#include "pizza.h"
#include "ingrediente.h"
#include<stdio.h>
#include <ctype.h>
#include<string.h>

int main(){
    char *cardapio = ("cardapio.txt");
    char *ingredientesDisponiveis = ("ingredientes.txt");
    int opcao, x = 1;
    while(x == 1){
        printf("\n==============================================================\n");
        printf("Menu principal");
        printf("\n==============================================================\n\n");
        printf("1 - Cadastrar Ingrediente.\n");
        printf("2 - Visualizar Ingredientes.\n");
        printf("3 - Editar Ingrediente.\n");
        printf("4 - Excluir Ingrediente.\n");
        printf("5 - Cadastrar Pizza.\n");
        printf("6 - Visualizar Pizzas.\n");
        printf("7 - Editar Pizza.\n");
        printf("8 - Excluir Pizza.\n");
        printf("9 - Vender Pizza.\n");
        printf("0 - Sair.\n\n");
        printf("Escolha uma opcao: ");
        scanf("%d", &opcao);

        if(opcao == 1){
            cadastrarIngredientes(ingredientesDisponiveis);
        }else if(opcao == 2){
            visualizarIngredientes(ingredientesDisponiveis);
        }else if(opcao == 3){
            editarIngrediente(ingredientesDisponiveis);
        }else if(opcao == 4){
            excluirIngrediente(ingredientesDisponiveis);
        }else if(opcao == 5){
            cadastrarPizzas(cardapio);
        }else if(opcao == 6){
            visualizarPizzas(cardapio);
        }else if(opcao == 7){
            editarPizza(cardapio);
        }else if(opcao == 8){
            excluirPizza(cardapio);
        }else if(opcao == 9){
            venderPizza(cardapio, ingredientesDisponiveis);
        }else if(opcao == 0){
            x = 2;
            return 0;
        }else{
            printf("\nOpcao inexistente!\n");
        }
    }
}
