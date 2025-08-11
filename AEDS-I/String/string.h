#include<stdio.h>
#include<stdlib.h>

int tamanho(char *);
int compara(char *, char *);
char *cocatena(char *str, char *str2);
void *copia(char *str, char *str2);

int main(){
    char str[20], str2[20];
    fgets(str,sizeof(str),stdin);
    if (str[tamanho(str) - 1] == '\n') {
        str[tamanho(str) - 1] = '\0';
    }
    fgets(str2,sizeof(str2),stdin);
    if (str2[tamanho(str2) - 1] == '\n') {
        str2[tamanho(str2) - 1] = '\0';
    }
    int n = tamanho(str);
    int m = tamanho(str2);
    printf("NUMERO DE LETRAS DA PRIMEIRA: ");
    printf("%d \n", n);
    printf("NUMERO DE LETRAS DA SEGUNDA: ");
    printf("%d \n", m);
    int x = compara(str,str2);
    printf("STRCMP: %d \n", x);
    printf("CONCATENACAO: ");
    char *k = cocatena(str,str2);
    printf("%s\n", k);
    printf("COPIA: ");
    copia(str, str2);
    printf("NOVO STR2: ");
    printf("%s\n", str2);
}

int tamanho(char *str){
    int cont=0;
    if(*str == '\0'){
        return cont;
    }else{
        return (cont+1) + tamanho(str+1);
    }
}

int compara(char *str, char *str2){
    int i = 0;
    while(*(str+i) != '\0' && *(str2+i) != '\0'){
        if(*(str+i) != *(str2+i)){
            if(*(str+i) < *(str2+i)){
                return -1;
            }else{
                return 1;
            }
        }
        i++;
    }
     if (*(str + i) == '\0' && *(str2 + i) == '\0') {
        return 0;
    } else if (*(str + i) == '\0') {
        return -1;
    } else {
        return 1;
    }
}

char *cocatena(char *str, char *str2){
    int i = 0, j=0, k=0, y=0;
    while(*(str+k) != '\0'){
        k++;
    }
    while(*(str+y) != '\0'){
        y++;
    }
    char *str3 = (char *)malloc((y+k+1)*sizeof(char));
    while(*(str+i) != '\0'){
        *(str3+i) = *(str+i);
        i++;
    }
    while(*(str2+j) != '\0'){
        *(str3+i) = *(str2+j);
        i++;
        j++;
    }
    *(str+i) = '\0';
    return str3;
}

void *copia(char *str, char *str2){
    int i=0;
    while(*(str+i) != '\0'){
        *(str2+i) = *(str+i);
        i++;
    }
    *(str2+i) = '\0';
    printf("%s\n", str2);
}
