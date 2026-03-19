#3) 
#x = 3; 
#y = x * 1025 ; 

# x= $s0; y= $s1;

ori $s0, $zero, 3 # x = 3
sll $s1, $s0, 10 # y = x*1024
add $s1, $s1, $s0 # y = x*1025