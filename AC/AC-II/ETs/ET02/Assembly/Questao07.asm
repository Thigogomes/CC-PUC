#7) 
#A[ 12 ] = h + A [ 8 ];

# h= $s0; 

#inicio 

.data
	.word 1
	.word 2
	.word 3
	.word 4
	.word 5
	.word 6
	.word 7
	.word 8

.text
.globl main
main:
	ori $t0, $zero, 0x1001 # t0 = 0x00001001
	sll $t0, $t0, 16 # t0 = 0x10010000

	lw $t1, 28 ($t0) # t1 = A[8]   
	ori $s0, $zero, 10 # h = 10
	add $t2, $t1, $s0 # h + A [ 8 ]

	sw $t2, 44 ($t0) # A[ 12 ] = h + A [ 8 ]

#fim