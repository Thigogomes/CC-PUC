#programa 17 
#Para a expressão a seguir, escreva um programa que calcule o valor de k: 
#k =  x * y   (Você deverá realizar a multiplicação através de somas!) 
#O valor de x deve ser lido da primeira posição livre da memória e o valor de y deverá lido da 
#segunda posição livre. O valor de k, após calculado, deverá ainda ser escrito na terceira posição 
#livre da memória. 

# x= $s0; y= $s1; k= $s2

.data
	x: .word 2
	y: .word 3

#inicio
.text
.globl main
main:
	ori $t0, $zero, 0x1001 # t0 = 0x00001001
	sll $t0, $t0, 16 # t0 = 0x10010000

	lw $s0, 0 ($t0) # x = memoria[0]
	lw $s1, 4 ($t0) # y = memoria[1]

	ori $t1, $zero, 0 # i = 0
	ori $t2, $zero, 0 # soma = 0

	loop:
		add $t2, $t2, $s0 # soma += x
		addi $t1, $t1, 1 # i++
		bne $t1, $s1, loop # if(i != y)

	add $s2, $zero, $t2 # k = soma
	sw $s2, 8 ($t0) # memoria[2] = k
#fim