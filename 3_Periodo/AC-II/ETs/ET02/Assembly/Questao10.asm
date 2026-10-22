#10) 
#h = A [ i ] ; 
#	A[ i ] = A [ i + 1] ; 
#	A [ i + 1] = h ; 

# h= $s0; i= $s1;

#inicio 

.data
	.word 1
	.word 2
	.word 3
	.word 4

.text
.globl main
main:
	ori $t0, $zero, 0x1001 # t0 = 0x00001001
	sll $t0, $t0, 16 # t0 = 0x10010000

	ori $s1, $zero, 2 # i = 2
	sll $t1, $s1, 2 # t1 = i*4
	add $t2, $t1, $t0 # t2 = &A[i]

	lw $s0, 0 ($t2) # h = A[i]
	lw $t3, 4 ($t2) # t3 = A[i+1]

	sw $t3, 0 ($t2) # A[i] = A[i+1] 
	sw $s0, 4 ($t2) # A[i+1] = h 
#fim
