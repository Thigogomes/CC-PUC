#programa 9 
#Considere a memória inicial da seguinte forma: 
#.text 
#.data 
#x1: .word 15 
#x2: .word 25 
#x3: .word 13 
#x4: .word 17 
#soma: .word -1 
#Escrever um programa que leia todos os números, calcule e substitua o valor da variável soma por este valor.

#inicio
.data 
x1: .word 15 
x2: .word 25 
x3: .word 13 
x4: .word 17 
soma: .word -1 

.text
.globl main
main:
	ori $t0, $zero, 0x1001 # t0 = 0x00001001
	sll $t0, $t0, 16 # t0 = 0x10010000

	lw $t1, 0 ($t0) # t1 = mamoria[0]
	lw $t2, 4 ($t0) # t2 = memoria[1]
	lw $t3, 8 ($t0) # t3 = memoria[2]
	lw $t4, 12 ($t0) # t4 = memoria[3]

	add $t5, $t1, $t2 # t5 = t1 + t2
	add $t6, $t3, $t4 # t6 = t3 + t4
	add $t7, $t5, $t6 # t7 = t5 + t6

	sw $t7, 16 ($t0) # memoria[4] = $t7
#fim