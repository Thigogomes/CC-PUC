#12) 
#Escreva um programa  que leia um valor A da memória, identifique se  o  número  é  negativo  ou  não  e  
#encontre  o  seu  módulo.  O  valor deverá ser reescrito sobre A.
 
# a= $s0;

#inicio

.data
	.word -2 

.text
.globl main
main:
	ori $t0, $zero, 0x1001 # t0 = 0x00001001
	sll $t0, $t0, 16 # t0 = 0x10010000

	lw $s0, 0 ($t0) # A = mem[0]

	sra $t1, $s0, 31 # pegar o bit de sinal

	beq $t1, $zero, fim 

	sub $t2, $zero, $s0 # t2 = 0 - a
	sw $t2, 0 ($t0) # mem[0] = t2

	fim:
	
#fim
