#programa 21 
# y = { x^3 + 1    se x >  0
#     { x^4 - 1    se x <= 0
#Os valores de  x devem ser lidos da primeira posição livre da memória e o valor de y deverá ser 
#escrito na segunda posição livre. 

# x= $s0; y= $s1;
.data
	x: .word -2

#inicio
.text
.globl main
main:
	ori $t0, $zero, 0x1001 # t0 = 0x00001001
	sll $t0, $t0, 16 # t0 = 0x10010000

	lw $s0, 0 ($t0) # x = memoria[0]

	andi $t1, $s0, 1 # ver se o ultimo bit é 0 ou 1

	add $t2, $zero, $s0 # t2 = x
	mult $t2, $t2 # x^2
	mflo $t2 # t2 = x^2
	mult $t2, $s0 # x^3
	mflo $t2 # t2 = x^3
	
	slt $t1, $zero, $s0 # t1 = (0 < s0) ? 1 : 0
	beq $t1, 1, maior # if(t1 == 1) -> manda para o label 'maior'

	menorOuIgual:
		subi $t3, $zero, 1 # t3 = -1

		mult $t2, $s0 # x^4
		mflo $t2 # t2 = x^4
	
		add $t3, $t3, $t2 # t3 += t2
		j fim

	maior:
		addi $t3, $t2, 1 # t3 = x^3 +1
		
	fim:
		add $s1, $zero, $t3 # y = t3
		sw $s1, 4 ($t0) # memoria[1] = y
#fim
