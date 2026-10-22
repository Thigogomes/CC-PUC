#programa 10 
#Considere o seguinte programa: y = 127x – 65z + 1 
#Faça um programa que calcule o valor de y conhecendo os valores de x e z. Os valores de x e z 
#estão armazenados na memória e, na posição imediatamente a seguir, o valor de y deverá ser 
#escrito, ou seja: 
#.data 
#x: .word 5 
#z: .word 7 
#y: .word 0   # esse valor deverá ser sobrescrito após a execução do programa.

# x= $s0; z= $s1; y= $s2;

#inicio
.data 
x: .word 5 
z: .word 7 
y: .word 0 

.text
.globl main
main:
	ori $t0, $zero, 0x1001 # t0 = 0x00001001
	sll $t0, $t0, 16 # t0 = 0x10010000

	lw $s0, 0 ($t0) # ts0 = mamoria[0]
	lw $s1, 4 ($t0) # s1 = memoria[1]

	sll $t1, $s0, 7 # t1 = 128x
	sub $t1, $t1, $s0 # t1 = 127x

	sll $t2, $s1, 6 # t2 = 64z
	add $t2, $t2, $s1 # t2 = 65z

	sub $t3, $t1, $t2 # t3 = t1 - t2
	addi $s2, $t3, 1 # s2 = t3 + 1

	sw $s2, 8 ($t0) # memoria[2] = $s2
#fim