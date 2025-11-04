#programa 15: 
#Escrever  um  programa  que  crie  um  vetor  de  100  elementos  na memória onde vetor[i] = 2*i + 1.
#Após a  última  posição  do  vetor criado,  escrever  a  soma  de  todos  os  valores armazenados do vetor. 
#Use o MARS para verificar a quantidade de instruções conforme o tipo (ULA, Desvios, Mem ou Outras)

# i= $s0;

.text
.globl main
main:
	ori $t0, $zero, 0x1001 # t0 = 0x00001001
	sll $t0, $t0, 16 # t0 = 0x10010000

	ori $s0, $zero, 0 # i = 0
	ori $s1, $zero, 0 # soma = 0
	ori $s2, $zero, 0 # result = 0
	ori $t1, $zero, 100 # n = 100
	
	loop:
		sll $t2, $s0, 1 # 2*i
		addi $s2, $t2, 1 # result = t2 + 1
		sw $s2, 0($t0) # memeoria[i] = result

		add $s1, $s1, $s2 # soma += result

		addi $t0, $t0, 4 # pos += 4 -> próximo endereço
		
		addi $s0, $s0, 1 # i++
		bne $s0, $t1, loop # if(i != 100)

	sw $s1, 0($t0) # memeoria[i] = soma -> grava soma após o último elemento
#fim