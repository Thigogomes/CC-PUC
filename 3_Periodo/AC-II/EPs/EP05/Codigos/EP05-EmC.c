#include <stdio.h>
#include <stdlib.h>
#include <time.h>

float mips(float base, float t) {
    t = (t-base)/1000.0;
    float resp = 10/t;
    printf("%f\n", resp);
    return resp;
}

float cpi(float base, float t, float f){
    t = (t-base);
    float resp = (t*f)/10;
    printf("%f\n", resp);
    return resp;
}

int main() {
    clock_t start, end;
    double time, media = 0.0;
    register int c;
    int k;
    char i = 1, j = 3;

    for (k = 1; k <= 10; k++) {
        start = clock();
        for (c = 1; c <= 10000000; c++) {
            i = i + 3;
        }
        end = clock();

        time = (((double)(end - start) * 1000.0) / CLOCKS_PER_SEC);
        printf("Tempo gasto (%d): %f ms\n", k, time);
        media += time;
    }
    printf("Tempo gasto medio: %f ms\n", media / 10.0);

    return 0;
}