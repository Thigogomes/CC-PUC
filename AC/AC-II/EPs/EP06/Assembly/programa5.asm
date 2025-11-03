#programa 5  
#x = 100000; 
#y = 200000; 
#z = x + y; 

# x= $s0; y= $s1; z= $s2;

#inicio
.text
.globl main
main:
	ori $s0, $zero, 0x186A # x = 0x186A
	sll $s0, $s0, 4 # x = 0x186A0

	ori $s1, $zero, 0x30D4 # y = 0x20D4
	sll $s1, $s1, 4 # y  = 0x20D40
	
	add $s2, $s0, $s1 # z = x + y
#fim