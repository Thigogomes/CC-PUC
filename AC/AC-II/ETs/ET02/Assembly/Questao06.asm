#6) 
#x = -1; 
#y = x / 32 ;

# x= $s0; y= $s1;

subi $s0, $zero, 1 # x = -1
sra $s1, $s0, 5 # y = x/32