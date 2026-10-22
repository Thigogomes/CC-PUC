#13) 
#Escreva um  programa que leia da memória um valor de Temperatura TEMP. Se TEMP>=30 e TEMP 
#<=50 uma variável FLAG, também na memória, deverá receber o valor 1, caso contrário, FLAG deverá 
#ser zero.
 
# TEMP= $s0; FLAG= $S1;

#inicio

.data
	.word 50

.text
.globl main
main:
	ori $t0, $zero, 0x1001 # t0 = 0x00001001
	sll $t0, $t0, 16 # t0 = 0x10010000

	lw $s0, 0 ($t0) # A = mem[0]
	ori $s1, $zero, 0 # FLAG = 0

	ori $t1, $zero, 30 # t1 = 30
	slt $t2, $s0, $t1 # t2 = (s0 < 30) ? 1 : 0
	bne $t2, $zero, fim # if(t2 != 0)

	ori $t3, $zero, 50 # t3 = 50
	slt $t4, $t3, $s0 # t4 = (50 < s0) ? 1 : 0
	bne $t4, $zero, fim # if(t4 == 0)
	
	addi $s1, $s1, 1 # FLAG = 1
	
	fim:
		sw $s1, 4 ($t0) # mem[1] = FLAG
#fim
