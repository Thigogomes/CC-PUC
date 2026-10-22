#programa 4  
#x = 3; 
#y = 4 ; 
#z = ( 15*x + 67*y)*4 

# x= $s0; y= $s1; z= $s2;

#inicio
.text
.globl main
main:
	ori $s0, $zero, 3 # x = 3
	ori $s1, $zero, 4 # y = 4

	sll $t0, $s0, 4 # t0 = 16x
	sub $t0, $t0, $s0 # t0 = 15x

	sll $t1, $s1, 6 # t1 = 64x
	add $t1, $t1, $s1 # t1 = 65x
	add $t1, $t1, $s1 # t1 = 66x
	add $t1, $t1, $s1 # t1 = 67x

	add $t2, $t0, $t1 # t2 = t0 + t1
	sll $t2, $t2, 2 # t2 = 4*t2
	add $s2, $zero, $t2 # z = t2
#fim