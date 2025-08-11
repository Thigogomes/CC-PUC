#include "pizza.h"
#include<stdio.h>
#include <ctype.h>
#include<string.h>

void venderPizza(char c[], char d[]){
    float valorFinal = 0;
    Pizzas p;
    Ingredientes ing;
    for (int i = 0; i < 50; i++) {
        memset(p.ingrediente[i].nome, 0, sizeof(p.ingrediente[i].nome));
    }
    char ingrediente[100];
    FILE *cardapio = fopen(c,"r");
    FILE *ingredientesDisponiveis = fopen(d,"r");
    printf("\n==============================================================\n");
    printf("selecione uma pizza do cardapio(Use o ID): ");
    printf("\n==============================================================\n\n");
    int id_venda, id_adicional;
    scanf("%d", &id_venda);
    getchar();
    while(fscanf(cardapio, "%d\n%[^\n]\n%c\n%f\n", &p.id, p.nome, &p.tamanho, &p.precoBase) == 4){
        if(id_venda == p.id){
            valorFinal = valorFinal + p.precoBase;
        }else{
            int y = 1;
            while(fscanf(cardapio, "%100[^\n]\n",ingrediente) == 1 && y == 1){
                if(isdigit(ingrediente[0])){
                    fseek(cardapio, -strlen(ingrediente) - 3, SEEK_CUR);
                    y = 0;
                }
            }
        }
     }
    do{
        printf("\n--------------------------------------------------------------\n");
        printf("Digite o ID dos ingredientes adicionais(Digite 0 para parar): ");
        printf("\n--------------------------------------------------------------\n\n");
        scanf("%d", &id_adicional);
        getchar();
        //rewind(ingredientesDisponiveis);
        while(fscanf(ingredientesDisponiveis, "%d\n%[^\n]\n%f\n", &ing.id, ing.nome, &ing.preco) == 3){
            if(ing.id == id_adicional){
                valorFinal = valorFinal + ing.preco;
            }
        }
    }while(id_adicional != 0);

    fclose(cardapio);
    fclose(ingredientesDisponiveis);
    printf("\n--------------------------------------------------------------\n");
    printf("Preco final da pizza: %.2f", valorFinal);
    printf("\n--------------------------------------------------------------\n\n");
}


void cadastrarPizzas(char c[]){
    Pizzas p;
    for (int i = 0; i < 50; i++) {
        memset(p.ingrediente[i].nome, 0, sizeof(p.ingrediente[i].nome));
    }
    int i = 0;
    FILE *cardapio = fopen(c, "a");
    printf("\n==============================================================\n");
    printf("Cadastro de Pizzas");
    printf("\n==============================================================\n\n");
    if(cardapio){
        printf("Digite o id da pizza: ");
        scanf("%d", &p.id);
        getchar();
        printf("Digite o nome da pizza: ");
        scanf("%100[^\n]", p.nome);
        getchar();
        printf("Digite o tamanho da pizza(P, M ou G): ");
        scanf(" %c", &p.tamanho);
        getchar();
        if(p.tamanho != 'P'&& p.tamanho != 'M' && p.tamanho != 'G'){
            while(p.tamanho != 'P'&& p.tamanho != 'M' && p.tamanho != 'G'){
                printf("Digite o tamanho da pizza: ");
                scanf(" %c", &p.tamanho);
                getchar();
            }
        }
        printf("Digite o preco base da pizza: ");
        scanf("%f", &p.precoBase);
        getchar();
        i = 0;
        printf("Digite o %d ingrediente da pizza(Digite ENTER para finalizar): ", i+1);//talvez colocar um sistema para ver se o ingrediente exite
        scanf("%100[^\n]", p.ingrediente[i].nome);
        getchar();
        while(strlen(p.ingrediente[i].nome) > 0){
            i++;
            printf("Digite o %d ingrediente da pizza: ", i+1);
            scanf("%100[^\n]", p.ingrediente[i].nome);
            getchar();
        }
        fprintf(cardapio, "%d\n", p.id);
        fprintf(cardapio, "%s\n", p.nome);
        fprintf(cardapio, "%c\n", p.tamanho);
        fprintf(cardapio, "%.2f\n", p.precoBase);
        for(int j = 0; j < i; j++){
            fprintf(cardapio, "%s\n", p.ingrediente[j].nome);
        }

    fclose(cardapio);
    printf("\nPizza cadastrada com sucesso!\n");
    }else{
        printf("Erro ao cadastrar!");
    }
}

void visualizarPizzas(char c[]){
    Pizzas p;
    char ingrediente[100];
    FILE *cardapio = fopen(c, "r");
    if(cardapio){
        printf("\n==============================================================\n");
        printf("Cardapio de Pizzas");
        printf("\n==============================================================\n\n");
        printf("\n--------------------------------------------------------------\n\n");
        while(fscanf(cardapio, "%d\n%[^\n]\n%c\n%f\n", &p.id, p.nome, &p.tamanho, &p.precoBase) == 4){
            printf("ID: %d\n", p.id);
            printf("Nome: %s\n", p.nome);
            printf("Tamanho: %c\n", p.tamanho);
            printf("Ingredientes:\n");
            printf("Preco base: %.2f\n", p.precoBase);
            int y = 1;
            while(fscanf(cardapio, "%100[^\n]\n", ingrediente) == 1 && y == 1){
                if(isdigit(ingrediente[0])){
                    fseek(cardapio, -strlen(ingrediente) - 3, SEEK_CUR);
                    y = 0;
                }else{
                    printf("-%s\n", ingrediente);
                }
            }
            printf("\n--------------------------------------------------------------\n\n");;
        }
        fclose(cardapio);
    }else{
        printf("Erro ao abrir o arquivo!");
    }
}

void excluirPizza(char c[]){
    Pizzas p;
    for (int i = 0; i < 50; i++) {
        memset(p.ingrediente[i].nome, 0, sizeof(p.ingrediente[i].nome));
    }
    int id_excluir, i = 0;
    char *t = ("temp3.txt");
    char ingrediente[100];
    FILE *cardapio = fopen(c, "r");
    FILE *temporaria = fopen(t, "w");
    if(cardapio){
        printf("\n==============================================================\n");
        printf("Digite o ID do ingrediente que deseja excluir: ");
        printf("\n==============================================================\n\n");
        scanf("%d", &id_excluir);
        while(fscanf(cardapio, "%d\n%[^\n]\n%c\n%f\n", &p.id, p.nome, &p.tamanho, &p.precoBase) == 4){
            if(p.id != id_excluir){
                fprintf(temporaria, "%d\n", p.id);
                fprintf(temporaria, "%s\n", p.nome);
                fprintf(temporaria, "%c\n", p.tamanho);
                fprintf(temporaria, "%.2f", p.precoBase);
                int y = 1;
                while(fscanf(cardapio, "%100[^\n]\n", ingrediente) == 1 && y == 1){
                    if(isdigit(ingrediente[0])){
                        fseek(cardapio, -strlen(ingrediente) - 3, SEEK_CUR);
                        y = 0;
                    }else{
                        fprintf(temporaria, "%s\n", ingrediente);
                    }
                }
            }else{
                i++;
                int y = 1;
                while (fscanf(cardapio, "%100[^\n]\n", ingrediente) == 1 && y == 1) {
                    if (isdigit(ingrediente[0])) {
                        fseek(cardapio, -strlen(ingrediente) - 3, SEEK_CUR);
                        y = 0;
                    }
                }
            }
        }
    }else{
        printf("Erro ao excluir!");
    }
    fclose(cardapio);
    fclose(temporaria);
    if(i == 1){
        remove(c);
        rename(t, c);
        printf("Pizza excluida com sucesso!\n");
    }else{
        printf("Pizza nao encontrada!");
    }
}

void editarPizza(char c[]){
    Pizzas p;
    for (int i = 0; i < 50; i++) {
        memset(p.ingrediente[i].nome, 0, sizeof(p.ingrediente[i].nome));
    }
    char *t = ("temp4.txt");
    char ingrediente[100];
    int id_editar, i = 0, k = 0, y = 1;
    FILE *cardapio = fopen(c, "r");
    FILE *temporaria = fopen(t, "w");
    printf("\n==============================================================\n");
    printf("Digite o ID do pizza que deseja editar: ");
    printf("\n==============================================================\n\n");;
    scanf("%d", &id_editar);
    if(cardapio){
        while(fscanf(cardapio, "%d\n%[^\n]\n%c\n%f\n", &p.id, p.nome, &p.tamanho, &p.precoBase) == 4){
            if(id_editar != p.id){
                fprintf(temporaria, "%d\n", p.id);
                fprintf(temporaria, "%s\n", p.nome);
                fprintf(temporaria, "%c\n", p.tamanho);
                fprintf(temporaria, "%.2f\n", p.precoBase);
                int y = 1;
                while(fscanf(cardapio, "%100[^\n]\n",ingrediente) == 1 && y == 1){
                    if(isdigit(ingrediente[0])){
                        fseek(cardapio, -strlen(ingrediente) - 3, SEEK_CUR);
                        y = 0;
                    }else{
                        fprintf(temporaria, "%s\n", ingrediente);
                    }
                }
            }else{
                i++;
                printf("\n-----------------Informacoes Atuais da Pizza:-----------------\n\n");
                printf("ID: %d\n", p.id);
                printf("Nome: %s\n", p.nome);
                printf("Tamanho: %c\n", p.tamanho);
                printf("Preco base: %.2f\n", p.precoBase);
                printf("Ingredientes: \n");
                while (fscanf(cardapio, "%100[^\n]\n", ingrediente) == 1) {
                    if (isdigit(ingrediente[0])) {
                        fseek(cardapio, -strlen(ingrediente) - 3, SEEK_CUR && y == 1);
                        y = 0;
                    } else {
                        printf("- %s\n", ingrediente);
                    }
                }
                printf("\n--------------------------------------------------------------\n\n");;
                printf("Digite o novo ID da pizza: ");
                scanf("%d", &p.id);
                getchar();
                printf("Digite o novo nome da pizza: ");
                scanf("%100[^\n]", p.nome);
                getchar();
                printf("Digite o novo tamanho da pizza (P, M ou G): ");
                scanf(" %c", &p.tamanho);
                getchar();
                while (p.tamanho != 'P' && p.tamanho != 'M' && p.tamanho != 'G') {
                    printf("Tamanho invalido. Digite P, M ou G: ");
                    scanf(" %c", &p.tamanho);
                    getchar();
                }
                printf("Digite o novo preco base: ");
                scanf("%f", &p.precoBase);
                getchar();
                k = 0;
                printf("Digite os ingredientes da pizza (Digite apenas ENTER para finalizar):\n");
                do {
                    printf("Ingrediente %d: ", k + 1);
                    scanf("%100[^\n]", p.ingrediente[k].nome);
                    getchar();
                    k++;
                }while(strlen(p.ingrediente[k-1].nome) > 0);
                fprintf(temporaria, "%d\n", p.id);
                fprintf(temporaria, "%s\n", p.nome);
                fprintf(temporaria, "%c\n", p.tamanho);
                fprintf(temporaria, "%.2f\n", p.precoBase);
                for (int j = 0; j < k - 1; j++) {
                    fprintf(temporaria, "%s\n", p.ingrediente[j].nome);
                }
            }
        }
    }else{
        printf("Erro ao abrir o arquivo!");
    }
    fclose(cardapio);
    fclose(temporaria);
    if (i == 1) {
        remove(c);
        rename(t, c);
        printf("Pizza editada com sucesso!\n");
    } else {
        printf("Pizza não encontrada!\n");
    }
}
