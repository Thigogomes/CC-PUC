#include "ingrediente.h"
#include<stdio.h>
#include<string.h>
#include<ctype.h>

void cadastrarIngredientes(char d[]){
    Ingredientes c;
    FILE *ingredientesDisponiveis = fopen(d,"a");
    printf("\n==============================================================\n");
    printf("Cadastro de Ingredientes");
    printf("\n==============================================================\n\n");
    if(ingredientesDisponiveis){
        printf("Digite o ID do ingrediente: ");
        scanf("%d", &c.id);
        getchar();
        printf("Digite o nome do ingrediente: ");
        scanf("%100[^\n]", c.nome);
        printf("DIgite o preco do ingrediente: ");
        scanf("%f", &c.preco);
        fprintf(ingredientesDisponiveis, "%d\n", c.id);
        fprintf(ingredientesDisponiveis, "%s\n", c.nome);
        fprintf(ingredientesDisponiveis, "%.2f\n", c.preco);
        fclose(ingredientesDisponiveis);
    }else{
        printf("Erro ao cadastrar!");
    }
}

void visualizarIngredientes(char d[]){
    Ingredientes c;
    int i = 0;
    FILE *ingredientesDisponiveis = fopen(d,"r");
    printf("\n==============================================================\n");
    printf("Ingredientes Disponiveis");
    printf("\n==============================================================\n\n");
    printf("\n--------------------------------------------------------------\n\n");
    if(ingredientesDisponiveis){
        while(fscanf(ingredientesDisponiveis, "%d\n%[^\n]\n%f\n", &c.id, c.nome, &c.preco) == 3){
            printf("ID: %d\n", c.id);
            printf("Nome: %s\n", c.nome);
            printf("Preco: %.2f\n", c.preco);
            printf("\n--------------------------------------------------------------\n\n");
            i++;
        }
        fclose(ingredientesDisponiveis);
        if(i == 0){
            printf("Nunhum ingrediente cadastrado!\n");
        }
    }else{
        printf("Erro ao acessar o arquivo!");
    }
}

void excluirIngrediente(char d[]){
    Ingredientes c;
    int id_excluir, i = 0;
    char *t = ("temp.txt");
    FILE *ingredientesDisponiveis = fopen(d,"r");
    FILE *temporario = fopen(t, "w");
    printf("\n==============================================================\n");
    printf("Digite o ID do ingrediente que deseja excluir: ");
    printf("\n==============================================================\n\n");
    scanf("%d", &id_excluir);
    if(ingredientesDisponiveis || temporario){
        while(fscanf(ingredientesDisponiveis, "%d\n%100[^\n]\n%f\n", &c.id, c.nome, &c.preco) == 3){
            if(c.id != id_excluir){
                fprintf(temporario, "%d\n", c.id);
                fprintf(temporario, "%s\n", c.nome);
                fprintf(temporario, "%.2f\n", c.preco);
            }else{
                i++;
            }
        }
    }else{
        printf("Erro ao abrir arquivo!");
    }
    fclose(ingredientesDisponiveis);
    fclose(temporario);
    if(i == 1){
        remove(d);
        rename(t, d);
        printf("Ingrediente excluido com sucesso!\n");
    }else{
        printf("Ingrediente nao encontrado");
    }
}

void editarIngrediente(char d[]){
    Ingredientes c;
    int id_editar, i = 0;
    char *t = ("temp1.txt");
    FILE *ingredientesDisponiveis = fopen(d,"r");
    FILE *temporario = fopen(t, "w");
    printf("\n==============================================================\n");
    printf("Digite o ID do ingrediente que deseja editar: ");
    printf("\n==============================================================\n\n");
    scanf("%d", &id_editar);
    if(ingredientesDisponiveis || temporario){
        while(fscanf(ingredientesDisponiveis, "%d\n%100[^\n]\n%f\n", &c.id, c.nome, &c.preco) == 3){
            if(c.id != id_editar){
                fprintf(temporario, "%d\n", c.id);
                fprintf(temporario, "%s\n", c.nome);
                fprintf(temporario, "%.2f\n", c.preco);
            }else{
                i++;
                printf("\n---------------Informacoes Atuais do Ingrediete:--------------\n\n");
                printf("%d\n", c.id);
                printf("%s\n", c.nome);
                printf( "%.2f\n", c.preco);
                printf("\n--------------------------------------------------------------\n\n");
                printf("Digite o novo ID: ");
                scanf("%d", &c.id);
                getchar();
                printf("Digite o novo nome: ");
                scanf("%100[^\n]", c.nome);
                getchar();
                printf("Digite o novo preco: ");
                scanf("%f", &c.preco);
                fprintf(temporario, "%d\n", c.id);
                fprintf(temporario, "%s\n", c.nome);
                fprintf(temporario, "%.2f\n", c.preco);
            }
        }
    }else{
        printf("Erro ao abrir o arquivo!");
    }
    fclose(ingredientesDisponiveis);
    fclose(temporario);
    if(i == 1){
        remove(d);
        rename(t, d);
    }else{
        printf("Ingrediente nao encontrado!");
    }
}

