#11) 
#j = 0; 
#i = 10; 
#do  
#  { 
#j = j + 1; 
#  }  
#while ( j != i );
 
# i= $s0; j= $s1;

#inicio 

.text
.globl main
main:
	ori $s1, $zero, 0 # j = 0
	ori $s0, $zero, 10 # i = 10
	
	while:
		addi $s1, $s1, 1 # j = j + 1
	bne $s1, $s0, while # if(j != i)
	
#fim
