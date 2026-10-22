#programa 16 
#Escreva um programa que avalie a expressão: (x*y)/z.  
#Use x = 1600000 (=0x186A00), y = 80000 (=0x13880), e z = 400000 (=0x61A80).
#Inicializar os registradores com os valores acima. 

# x= $s0; y= $s1; z= $s2

#inicio
.data
	x: .word 0x186A00
	y: .word 0x13880
	z: .word 0x61A80

.text
.globl main
main:
	ori $t0, $zero, 0x1001 # t0 = 0x00001001
	sll $t0, $t0, 16 # t0 = 0x10010000

	lw $s0, 0 ($t0) # x = 0x186A00
	srl $t1, $s0, 4 # t1 = 0x186A0

	lw $s1, 4 ($t0) # y = 0x13880
	srl $t2, $s1, 4 # t2 = 0x1388

	lw $s2, 8 ($t0) # z = 0x61A80
	srl $t3, $s2, 4 # t3 =  = 0x61A8

	mult $t1, $t2 # x * y
	mflo $t4 # t4 = x * y

	div  $t4, $t3 # t4 / t3
	mflo $t5 # t5 = t4 / t3

	sll $t5, $t5, 4 # vai mover os 4bits para a esquerda que eu tirei
	sw $t5, 12 ($t0) # mempria[3] = t5
	
#fim
