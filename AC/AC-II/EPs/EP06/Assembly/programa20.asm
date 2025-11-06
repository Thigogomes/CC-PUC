#programa 20 
#y = 
#x^4 + x^3 -2x^2   se x for par 
#x^5 – x^3  + 1       se x for impar 
#Os valores de x devem ser lidos da primeira posição livre da memória e o valor de y deverá ser 
#escrito na segunda posição livre. 

# x= $s0; y= $s1;
.data
	x: .word 3

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

	beq $t1, $zero, ehPar #se t1 for par manda para o label ehPar

	ehImpar:
		mult $t2, $s0 # x^3
		mflo $t2 # t2 = x^3

		sub $t3, $zero, $t2 # t3 = -x^3

		mult $t2, $s0 # x^4
		mflo $t2 # t2 = x^4
		mult $t2, $s0 # x^5
		mflo $t2 # t2 = x^5

		add $t3, $t3, $t2 #

		add $t3, $t3, 1 # t3 += 1

		j fim
	
	ehPar:
		add $t3, $t2, $t2 # t3 = 2*t2
		sub $t3, $zero, $t3 # t3 = -2*t2

		mult $t2, $s0 # x^3
		mflo $t2 # t2 = x^3

		add $t3, $t2, $t3 # t3 += t2

		mult $t2, $s0 # x^4
		mflo $t2 # t2 = x^4

		add $t3, $t2, $t3 # t3 += t2

	fim:
		add $s1, $zero, $t3 # y = t3
		sw $s1, 4 ($t0) # memoria[1] = y
#fim
