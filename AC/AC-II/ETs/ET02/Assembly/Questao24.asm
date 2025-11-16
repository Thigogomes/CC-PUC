#24) 
#Você deverá criar duas funções nesse exercício. Uma função que receba como argumentos 2 números 
#inteiros de 32 bits. Essa função deverá também retornar um inteiro.  O primeiro número recebido como 
#parâmetro representa um endereço de memória e o segundo uma quantidade de elementos. A quantidade 
#de elementos máxima é 30, se o numero recebido for superior a 30 sua função deverá usar 30. 
#Os dois números acima deverão estar nas duas primeiras posições livres da memória (portanto devem ser 
#lidos da memória para serem passados à função). 
#Sua função deverá criar um vetor que tem início no endereço de memória recebido como primeiro 
#argumento e com a quantidade de elementos recebida como o segundo argumento.  
#Uma segunda função que receba um número (este número terá no máximo 16 bits)  e retorne o seu quadrado. 
#Cada elemento do vetor y será dado como:  
#y[i] = 2i2 +2i+1 se i for par;  
#y[i] = i2 se i for impar.  
#O valor retornado será a soma de todos os elementos de y[].  

#inicio

.data
	address: .word 0x10010008
	number: .word 21

.text
.globl main
main:
	ori $t0, $zero, 0x1001 # t0 = 0x00001001
	sll $t0, $t0, 16 # t0 = 0x10010000

	lw $s0, 0 ($t0) # s0 = mem[0]
	lw $s1, 4 ($t0) # s1 = mem[1]

	add $a0, $zero, $s0 # ao = s0
	add $a1, $zero, $s1 # a1 = s1

	jal .criar_vetor
	j fim

	.criar_vetor:
		ori $t2, $zero, 0 # i = 0
		add $v0, $zero, 0 # v0 = 0
		ori $t5, $zero, 0 # soma = 0
		slti $t3, $a1, 30 # t3 = (a1 < 30) ? 1 : 0
		bne $t3, $zero, check # if( t3 != 0)
		ori $a1, $zero, 30 # a1 = 30
	check:
		beq $t2, $a1, end # if(i == a1)
		andi $t3, $t2, 0x1 
		beq $t3, $zero, ehPar # if(t3 == 0)
	ehImpar:
		add $a3, $zero, $t2 # a3 = t2
		jal .square
		move $t4, $v0 
		j store
	ehPar:
		mult $t2, $t2 # t2*t2
		mflo $t4 # v0 = t2*t2
		add $t4, $t4, $t2 # v0 = 2i2 +i
		add $t4, $t4, $t2 #v0 = 2i2 +2i
		addi $t4, $t4, 1 # 2i2 +2i + 1
	store:
		add $t5, $t5, $t4 # soma += t4
		sw $t4, 0 ($a0) # mem[] = v0
		addi $a0, $a0, 4 # adrress += 4 -> avança uma posição
		addi $t2, $t2, 1 # i++
		j check
	end:
		move $v0, $t5 # v0 = t5
		lw   $ra, 0($sp) # restaura o endereço de retorno; $sp  → aponta para o topo da pilha
    		addi $sp, $sp, 4 # fecha espaço na pilha
		jr $ra

	.square:
		mult $a3, $a3 # a3*a3
		mflo $v0 # v0 = a3*a3
		jr $ra
	fim:
#fim
