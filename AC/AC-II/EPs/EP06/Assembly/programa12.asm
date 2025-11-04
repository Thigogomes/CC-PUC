#programa 12 
#Considere a seguinte situação: 
#int ***x; 
#onde x contem um ponteiro para um ponteiro para um ponteiro para um inteiro. 
#Nessa situação, considere que a posição inicial de memória contenha o inteiro em questão.  
#Coloque todos os outros valores em registradores, use os endereços de memória que quiser dentro 
#do espaço de endereçamento do Mips. 
#Resumo do problema: 
#k = MEM [ MEM [MEM [ x ] ] ].   
#Crie um programa que implemente a estrutura de dados acima, leia o valor de K, o multiplique por 
#2 e o reescreva no local correto conhecendo-se apenas o valor de x.

# k= $s0;
#inicio
.data
x: .word p2   
p2: .word p1   
p1: .word value     
value: .word 2

.text
.globl main
main:
	ori $t0, $zero, 0x1001 # t0 = 0x00001001
	sll $t0, $t0, 16 # t0 = 0x10010000

	lw $t1, 0 ($t0) # t1 = p2
	lw $t2, 0 ($t1) # t2 = p1
	lw $t3, 0 ($t2) # t3 = value
	lw $s0, 0 ($t3) # k = 2

	add $s0, $s0, $s0 # k = 2*k

	sw $s0, 0 ($t3) # memoria[$t3] = k
#fim