#18) 
#Escreva um programa que leia um número armazenado na primeira posição livre da memória. Após a 
#leitura desse número, um registrador qualquer será um flag, isto é, se esse número lido estiver na faixa de 
#50 a 100 ou 150 a 200 esse registrador deverá conter um “1”, caso contrário esse registrador deverá conter “0”. 
#Exemplo: 
#leia número; 
#se ( 50 <= número <=100 ou 150 <= número <= 200) 
#	registrador flag = 1; 
#caso contrério 
#	registrador flag = 0; 

# x= $s0; FLAG= $s1;

#inicio

.data
	.word 151

.text
.globl main
main:
	ori $t0, $zero, 0x1001 # t0 = 0x00001001
	sll $t0, $t0, 16 # t0 = 0x10010000

	lw $s0, 0 ($t0) # x = mem[0]
	ori $s1, $zero, 0 # FLAG = 0

	ori $t1, $zero, 50 # t1 = 50
	slt $t2 , $s0, $t1 # t2 = (t1 < s0) ? 1 : 0
	bne $t2, $zero, ou # if(t1 != 0)

	ori $t1, $zero, 100 # t1 = 100
	slt $t2 , $t1, $s0 # t2 = (t1 < s0) ? 1 : 0
	bne $t2, $zero, ou # if(t1 != 0)
	j inc_flag

	ou:
		ori $t1, $zero, 150 # t1 = 150
		slt $t2 , $s0, $t1 # t2 = (t1 < s0) ? 1 : 0
		bne $t2, $zero, store # if(t1 != 0)

		ori $t1, $zero, 200 # t1 = 200
		slt $t2 , $t1, $s0 # t2 = (t1 < s0) ? 1 : 0
		bne $t2, $zero, store # if(t1 != 0)
	
	inc_flag:
		addi $s1, $s1, 1 # FLAG = 1

	store:
		sw $s1, 4 ($t0) # mem[1] = FLAG

#fim
