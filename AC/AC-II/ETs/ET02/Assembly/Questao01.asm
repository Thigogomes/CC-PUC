#1) 
#a = 10; 
#b = -1; 
#a = a + 1; 
#c = a + b; 

# a= $s0; b= $s2; c= $s2;

ori $s0, $zero, 10 # a = 10
ori $s1, $zero, -1 # b = -1
addi $s0, $s0, 1 # a = a + 1
add $s2, $s0, $s1 # c = a + b