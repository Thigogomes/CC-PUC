#17) 
#Escreva um programa que compute a série de Fibonacci, a série é definida como: 
#1, 1, 2, 3, 5, 8, 13, 21, 34, 55, ... 
#Cada termo da soma é a soma dos dois termos predecessores. 
#Exemplo: o termo 13 é a soma de 5 e 8. 
#Escreva o programa que compute os primeiros 100 termos da série. Se não for possível computar estes 
#100 termos, identifique a maior quantidade possível suportada pelo emulador. 
#Cada termo deverá estar em uma posição da memória. O primeiro termo na primeira posição livre da memória. 

#inicio

.text
.globl main
main:
	ori $t0, $zero, 0x1001 # t0 = 0x00001001
	sll $t0, $t0, 16 # t0 = 0x10010000

	ori $s0, $zero, 12 # n
	ori $s1, $zero, 0 # pos_memoria

	ori $t1, $zero, 1 # anterior = 1
	ori $t2, $zero, 1 # proxima = 1
	ori $t3, $zero, 0 # auxiliar

	fibonacci:
		beq $s0, $zero, fim # if(s0 == 0)
			sll $t4, $s1, 2 #  t4 = s1*4
			add $t5, $t4, $t0 # t5 = &mem[t4]
			sw $t1, 0 ($t5) # mem[t4] = anterior

			add $t3, $t1, $zero # aux = anterior
			add $t1, $t2, $zero # anterior = proxima
			add $t2, $t2, $t3 #  proxima = aux + proxima

			sub $s0, $s0, 1 # s0--
			add $s1, $s1, 1 # s1++
			j fibonacci

	fim:

#fim
