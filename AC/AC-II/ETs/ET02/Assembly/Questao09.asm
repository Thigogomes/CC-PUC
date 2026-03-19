#9) 
#A [ j ] = h + A [ i ] ; 

# i= $s0; j= $s1; h= $s2;

#inicio 

.data
	.word 1
	.word 2
	.word 3

.text
.globl main
main:
	ori $t0, $zero, 0x1001 # t0 = 0x00001001
	sll $t0, $t0, 16 # t0 = 0x10010000
  
	ori $s0, $zero, 2 # i = 2
	ori $s1, $zero, 3 # j = 3
	ori $s2, $zero, 6 # h = 6

	sll $t3, $s0, 2 # i = i*4
	sll $t4, $s1, 2 # j = j*4

	add $t1, $t0, $t3 # t1 = t0 = i
	add $t2, $t0, $t4 # t2 = t0 + j

	lw $t5, 0 ($t1) # t5 = A[i] 

	add $t6, $s2, $t5 # t6 = h + A [i]

	sw $t6, 0 ($t2) # A [j] = h + A [i]

#fim