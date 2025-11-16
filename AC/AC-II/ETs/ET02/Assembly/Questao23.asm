#23) 
#Escreva um programa que solicite ao usuário que digite dois números, seu programa deverá conter uma 
#função que receba esses dois números e retorne o primeiro elevado ao segundo. Esse resultado deverá ser 
#mostrado na tela. O programa rodará indefinidamente até que o primeiro número digitado seja 0 (zero).  
#Obs.: Caso você não tenha visto a utilização de handlers e a leitura de valores pelo teclado, os dois 
#números deverão ser lidos da primeira e segunda posição livre da memória. O resultado será 
#escrito na terceira posição livre da memória e o programa irá executar apenas uma vez.

#inicio
.data
	msg1: .asciiz "Digite o primeiro numero: "
	msg2: .asciiz "Digite o segundo numero: "
	msg3: .asciiz "Resposta: "
	newline: .asciiz "\n"

.text
.globl main
main:
	loop:
		la $a0, msg1 # coloca o endereço da mensagem msg1 em $a0 (la = load address)
		li $v0, 4 # carrega o valor 4 no registrador $v0, $v0 é usado para informar ao sistema qual serviço da syscall será usado,
				# 4 significa: imprimir uma string no console.(li = load immediate)
		syscall # Executa a chamada de sistema (system call), imprime a mensagem por causa do código 4

		li $v0, 5 # Carrega o valor 5 no registrador $v0.
		syscall # Como $v0 contém 5 que le e armazena o valor digitado
		move $s0, $v0 # move o valor em v0 para s0

		beq $s0, $zero, fim

		la $a0, msg2
		li $v0, 4
		syscall

		li $v0, 5 
		syscall
		move $s1, $v0

		ori $s2, $zero, 1 # s2 = 1
	
		ori $t1, $zero, 0 # i = 0
		ori $t2, $zero, 0 # j = 0
		ori $t3, $zero, 0 #soma parcial do produto
		ori $t4, $s0, 0 # soma acumulada
		beq  $s1, $zero, result # caso de numero elevado a 0

	pot:
		addi $t1, $t1, 1 # i++
		ori $t2, $zero, 0 # j = 0
		ori $t3, $zero, 0 # soma parcial = 0
		bne $t1, $s1, multi # if(i != y)
		add $s2, $zero, $t4 # s2 = t4
		j result
	
	multi:
		add $t3, $t3, $t4 # soma += x
		addi $t2, $t2, 1 # j++
		bne $t2, $s0, multi # if(j != x)
		ori $t4, $t3, 0 # soma acumulada = soma parcial
		j pot
	
	result:	
		la $a0, msg3
		li $v0, 4
		syscall

		li $v0, 1 # 1 é o código para imprimir um numero inteiro
		move $a0, $s2
		syscall

		li $v0, 4
		la $a0, newline
		syscall

		j loop
	fim:

#fim
