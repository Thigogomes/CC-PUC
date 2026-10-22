#programa 6 
#x = o maior inteiro possível; 
#y = 300000; 
#z =  x - 4y 

# x= $s0; y= $s1; z= $s2;

#inicio
.text
.globl main
main:
	ori $s0, $zero, 0x7FFFFFFF # x = 0x7FFFFFFF
	ori $s1, $zero, 0x493E0 # y = 0x493E0
	sll $t0, $s1, 2 # y  = 0x20D40
	sub $s2, $s0, $t0 # z = x - 4y
#fim