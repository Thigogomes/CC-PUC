#programa 14: 
#Escreva um programa  que leia um valor A da memória, identifique se  o  número  é  par  ou  não.  
#Um valor deverá ser escrito na segunda posição livre da memória (0 para par e 1 para ímpar). 

# a= $s0;
#inicio
.data
A: .word 3

.text
.globl main
main:
	ori $t0, $zero, 0x1001 # t0 = 0x00001001
	sll $t0, $t0, 16 # t0 = 0x10010000

	lw $s0, 0 ($t0) # a = memoria[0]
	
	andi $t1, $s0, 1 # para ver se o ultimo bit e 0 ou 1
	beq $t1, 0, par
	ori $t1, $zero, 0
	j fim

	par:
		ori $t1, $zero, 1
		
	fim:
		sw $t1, 4($t0)
#fim