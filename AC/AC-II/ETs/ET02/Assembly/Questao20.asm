#20) 
#Para os dois exercícios a seguir, considere que a máquina opera a 100MHz e os CPIs das instruções são: 
#Instruções da ALU -> 3; 
#Instruções de Desvio -> 4; 
#Instruções de MEM -> 5; - Considere que um vetor de 100 números inteiros está armazenado na memória e o endereço base está 
#em $S1. Escrever um programa que some todos os elementos do vetor e armazene esta soma na primeira 
#posição de memória após o vetor. Calcule o CPI médio, o tempo de execução do programa, implemente 
#alguma melhoria nesse seu programa e calcule o speedup (se o seu programa já está na menor versão 
#possível, insira dois nops dentro do loop e calcule o speedup do programa original sobre esse com os dois nops). 

#inicio

.data 
	array: .word 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1
	array1: .word 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1
	array2: .word 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1
	array3: .word 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1
	array4: .word 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1

.text
.globl main
main:
	ori $t0, $zero, 0x1001 # t0 = 0x00001001
	sll $t0, $t0, 16 # t0 = 0x10010000

	ori $t1, $zero, 100 # n = tamanho
	ori $s0, $zero, 0 # soma = 0
	ori $t2, $zero, 0 # temp = 0
	ori $t3, $zero, 0 # pos_memoria

	loop:
		sll $t4, $t3, 2 # t4 = pos*4
		add $t5, $t4, $t0 # t5 = $mem[pos]
		lw $t2, 0 ($t5) # t2 = mem[t5]
		add $s0, $s0, $t2 # soma += t2
		addi $t3, $t3, 1 # t3++
		#nop
		#nop
		sub $t1, $t1, 1 # t1--
		bne $t1, $zero, loop # if(t1 != 0)

	Store:
		sll $t4, $t3, 2 # t4 = pos*4
		add $t5, $t4, $t0 # t5 = mem[n+1]
		sw $s0, 0 ($t5) # mem[n+1] = soma
#fim

#Total: 809

#ALU: 608 - 75%
#JUMP: 0 - 0%
#BRANCH: 100 - 12%
#MEMORY: 101 - 12%
#OTHER: 0 - 0%

#CICLOS DA ALU: 608 * 3 = 1824
#CICLOS DE MEMORIA: 101 * 5 = 505
#CICLOS DE DESVIO: 100 * 4 = 400

#CICLOS TOTAIS: 1824+505+400 = 2729

#CPI MEDIO: 2729 / 809 = 3.37

#----- Com NOP ----- #

#Total: 1009

#ALU: 808 - 80%
#JUMP: 0 - 0%
#BRANCH: 100 - 10%
#MEMORY: 101 - 10%
#OTHER: 0 - 0%

#CICLOS DA ALU: 808 * 3 = 2424
#CICLOS DE MEMORIA: 101 * 5 = 505
#CICLOS DE DESVIO: 100 * 4 = 400

#CICLOS TOTAIS: 2424+505+400 = 3329

#CPI MEDIO: 3329 / 1009 = 3.29
