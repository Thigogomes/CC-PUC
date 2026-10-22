#8) 
#h = k + A [ i ] ; 

# i= $s0; k= $s1; h= $s2;

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
  
	ori $s0, $zero, 2 # i = 2
	ori $s1, $zero, 5 # k = 5

	sll $t3, $s0, 2 # i = i*4

	add $t1, $t0, $t3 # t1 = i + t0

	lw $t2, 0 ($t1) # t1 = A[i] 

	add $s2, $t2, $s1 # h = k + A [i]

#fim