#programa 18 
#Para a expressão a seguir, escreva um programa que calcule o valor de k:   
#k = x^y 
#Obs: Você poderá utilizar o exercício anterior. 
#O valor de x deve ser lido da primeira posição livre da memória e o valor de y deverá lido da 
#segunda posição livre. O valor de k, após calculado, deverá ainda ser escrito na terceira posição livre da memória. 
#Dê um valor para x e y (dê valores pequenos !!) e use o MARS para verificar a quantidade de 
#instruções conforme o tipo (ULA, Desvios, Mem ou Outras) 

# x= $s0; y= $s1; k= $s2

.data
	x: .word 3
	y: .word 3

#inicio
.text
.globl main
main:
	ori $t0, $zero, 0x1001 # t0 = 0x00001001
	sll $t0, $t0, 16 # t0 = 0x10010000

	lw $s0, 0 ($t0) # x = memoria[0]
	lw $s1, 4 ($t0) # y = memoria[1]

	ori $s2, $zero, 1 # k = 1

	ori $t1, $zero, 0 # i = 0
	ori $t2, $zero, 0 # j = 0
	ori $t3, $zero, 0 #soma parcial do produto
	ori $t4, $s0, 0 # soma acumulada

	beq  $s1, $zero, store

	pot:
		addi $t1, $t1, 1 # i++
		ori $t2, $zero, 0 # j = 0
		ori $t3, $zero, 0 # soma parcial = 0
		bne $t1, $s1, multi # if(i != y)
		j fim
	
	multi:
		add $t3, $t3, $t4 # soma += x
		addi $t2, $t2, 1 # j++
		bne $t2, $s0, multi # if(j != x)
		ori $t4, $t3, 0 # soma acumulada = soma parcial
		j pot
	
	fim:
	add $s2, $zero, $t4 # k = soma

	store:
		sw $s2, 8 ($t0) # memoria[2] = k
#fim
