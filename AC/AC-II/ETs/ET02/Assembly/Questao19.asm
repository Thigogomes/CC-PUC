#19) 
#Escreva um programa que calcule a mediana de 3 números armazenados na memória. Após encontrar 
#essa mediana, escrevê-la na posição seguinte aos 3 números. 
#Exemplo: 
#.data 
#A: .word 23 
#B: .word 98 
#C: .word 17 
#Os três números acima serão armazenados na memória quando o programa for iniciado. 
#A mediana é maior ou igual a um dos inteiros e menor ou igual ao outro. 
#No caso acima, a mediana é o número 23 
#Um outro caso possível: 
#.data 
#A: .word 9 
#B: .word 98 
#C: .word 9 
#Nesse caso a mediana é o "9". 
#Considere que os números nas posições A, B e C podem ser trocados de rodada para rodada do seu programa. 

#inicio

.data 
	A: .word 23 
	B: .word 98 
	C: .word 17 

.text
.globl main
main:
	ori $t0, $zero, 0x1001 # t0 = 0x00001001
	sll $t0, $t0, 16 # t0 = 0x10010000

	lw $s0, 0 ($t0) # a = mem[0]
	lw $s1, 4 ($t0) # b = mem[1]
	lw $s2, 8 ($t0) # c = mem[2]

	ori $t1, $zero, 0 # aux = 0
	
	swap_AB:
		slt $t1, $s1, $s0 # t1 = (s1 < s0) ? 1 : 0
		beq $t1, $zero, swap_AC # if(t1 < 0)
		ori $t2, $s1, 0 # aux = s1
		ori $s1, $s0, 0 # s1 = s0
		ori $s0, $t2, 0 # s0 = aux

	swap_AC:
		slt $t1, $s2, $s0 # t1 = (s2 < s0) ? 1 : 0
		beq $t1, $zero, swap_BC # if(t1 < 0)
		ori $t2, $s2, 0 # aux = s2
		ori $s2, $s0, 0 # s2 = s0
		ori $s0, $t2, 0 # s0 = aux

	swap_BC:
		slt $t1, $s2, $s1 # t1 = (s2 < s1) ? 1 : 0
		beq $t1, $zero, store # if(t1 < 0)
		ori $t2, $s2, 0 # aux = s2
		ori $s2, $s1, 0 # s2 = s1
		ori $s1, $t2, 0 # s1 = aux

	store:
		sw $s1, 12 ($t0) # mem[3] = s1
#fim
