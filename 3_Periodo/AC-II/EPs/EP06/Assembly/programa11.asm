#programa 11 
#Considere o seguinte programa: y = x – z + 300000 
#Faça um programa que calcule o valor de y conhecendo os valores de x e z. Os valores de x e z 
#estão armazenados na memória e, na posição imediatamente a seguir, o valor de y deverá ser 
#escrito, ou seja: 
#.data 
#x: .word 100000 
#z: .word 200000 
#y: .word 0   # esse valor deverá ser sobrescrito após a execução do programa.

# x= $s0; z= $s1; y= $s2;
#inicio
.data 
x: .word 100000 
z: .word 200000 
y: .word 0   # esse valor deverá ser sobrescrito após a execução do programa.

.text
.globl main
main:
	ori $t0, $zero, 0x1001 # t0 = 0x00001001
	sll $t0, $t0, 16 # t0 = 0x10010000

	lw $s0, 0 ($t0) # s0 = mamoria[0]
	lw $s1, 4 ($t0) # s1 = memoria[1]

	sub $t1, $s0, $s1 # t1 = x - z

	ori $t2, $zero, 0x493E # t2 = 0x493E
	sll $t2, $t2, 4 # t2 = 0x493E0 = 300000 

	add $s2, $t1, $t2 # z = t1 - t2

	sw $s2, 8 ($t0) # memoria[2] = $s2
#fim