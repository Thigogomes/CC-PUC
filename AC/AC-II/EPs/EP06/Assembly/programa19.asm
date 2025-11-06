#programa 19 
#Escrever um programa que leia dois números da memória, a primeira e segunda posições 
#respectivamente (os coloque em $s0 e $s1) e determine a quantidade de bits significantes de cada 
#um. Coloque as respostas em $t0 e $t1, a partir desse resultado faça a multiplicação. Caso o número 
#de bits significantes de ambos seja menor do que 32 a resposta deverá estar apenas em $s2, caso 
#contrário a resposta estará em $s2 e $s3 (LO e HI respectivamente). 
#Para os exercícios a seguir, considere as variáveis com números abaixo de 16 bits, salvo se 
#mencionado ao contrário. 

.data
	x: .word 2
	y: .word 3

#inicio
.text
.globl main
main:
	ori $t2, $zero, 0x1001 # t0 = 0x00001001
	sll $t2, $t2, 16 # t0 = 0x10010000

	lw $s0, 0 ($t2) # s0 = memoria[0]
	lw $s1, 4 ($t2) # s1 = memeoria[1]

	addi $t0, $zero, 0 # t0 = 0
	addi $t1, $zero, 0 # t1 = 0

	or $t3, $s0, $zero # t3 = s0
	or $t4, $s1, $zero # t4 = s1

	cont1:
		beq $t3, $zero, cont2 #
		addi $t5, $t3, 1 #
		srl $t3, $t3, 1 #
		beq $t5, $zero, cont1 #
		addi $t0, $t0, 1 #
		j cont1

	cont2:
		beq $t4, $zero, check #
		addi $t5, $t4, 1 #
		srl $t4, $t4, 1 #
		beq $t6, $zero, cont2 #
		addi $t1, $t1, 1 #
		j cont2

	check:
		slti $t5, $t0, 32 #
		slti $t6, $t1, 32 #
		add $t7, $t5, $t6 #
		beq $t7, $zero, multi2

	multi1:
		mult $s0, $s1 #
		mflo $s2 #
		mfhi $s3 #

		slti $t5, $t1, 32 
		j store1
	
	multi2:
		mult $s0, $s1 #
		mflo $s2 #

		slti $t5, $t1, 32 #
		j store2

	store1:
		sw $s2, 8 ($t2) # memoria[2] = s2
		sw $s3, 12 ($t2) # memoria[3] = s3
		j fim

	store2:
		sw $s2, 8 ($t2) # memoria[2] = s2

	fim:
#fim
