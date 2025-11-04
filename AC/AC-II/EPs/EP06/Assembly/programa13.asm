#programa 13: 
#Escreva um programa  que leia um valor A da memória, identifique se  o  número  é  negativo  ou  
#não  e  encontre  o  seu  módulo.  O  valor deverá ser reescrito sobre A. 

# a= $s0;
#inicio
.data
A: .word -2

.text
.globl main
main:
	ori $t0, $zero, 0x1001 # t0 = 0x00001001
	sll $t0, $t0, 16 # t0 = 0x10010000

	lw $s0, 0 ($t0) # a = memoria[0]
	
	sra $t1, $s0, 31 # Desloca o bit de sinal para todos os bits para verificar se o número é negativo
	beq $t1, $zero, ehPositivo
	sub $s0, $zero, $s0

	ehPositivo:
		sw $s0, 0($t0)
#fim