#22)
#Escreva uma função que receba como argumentos 2 números inteiros de 32 bits. Essa função deverá 
#também retornar um inteiro. 
#O primeiro número recebido como parâmetro representa um endereço de memória e o segundo uma 
#quantidade de elementos. Sua função deverá criar um vetor que tem início nesse endereço de memória 
#(primeiro argumento) e a quantidade de elementos desse vetor dadas pelo segundo argumento. 
#Cada elemento do vetor é um elemento da série: 
#y[i] = 2i – 1 ; se i for par; 
#y[i] = i ; se i for impar. 
#O valor retornado será a soma de todos os elementos de y[]. 

#inicio

.text
.globl main
main:
	ori $t0, $zero, 0x1001 # t0 = 0x00001001
	sll $t0, $t0, 16 # t0 = 0x10010000

	ori $s0, $zero 8 # n

	add $a0, $zero, $t0 # a0 = t0
	add $a1, $zero, $s0 # a1 = s0

	jal criar_vetor
	j fim

	criar_vetor:
		ori $t2, $zero, 0 # i = 0
		add $v0, $zero, 0 # soma = 0

	loop:
		slt $t7, $t2, $a1 # t7 = (t2 < a1) ? 1 : 0
		beq $t7, $zero, end #
		andi $t3, $t2, 0x1 # 
		beq $t3, $zero, ehPar #

	ehImpar:
		add $t4, $t2, $zero # t4 = i
		j store

	ehPar:
		add $t4, $t2, $t2 # t4 = 2*i
		sub $t4, $t4, 1 # t4 = 2*i -1

	store:
		sll $t5, $t2, 2 # t5 = i*4
		add $t6, $a0, $t5 # endereço de y[i]
		sw $t4, 0 ($t6) # mem[i] = t4
		add $t2, $t2, 1 # i++
		add $v0, $v0, $t4 # soma += t4
		j loop

	end:
		jr $ra

	fim:
#fim