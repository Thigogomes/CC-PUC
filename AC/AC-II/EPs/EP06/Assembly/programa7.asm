#programa 7 
#Considere a seguinte instrução iniciando um programa: 
#ori $8, $0, 0x01 
#Usando apenas instruções reg-reg lógicas e/ou instruções de deslocamento (sll, srl e 
#sra), continuar o programa de forma que ao final, tenhamos o seguinte conteúdo no 
#registrador $8: 
#$8 = 0xFFFFFFFF 

#inicio
.text
.globl main
main:
	ori $8, $zero, 0x01 # $8 = 0x00000001
	ori $8, $8, 0xFFFF # $8 = 0x0000FFFF
	sll $8, $8, 16 # $8 = 0xFFFF0000
	ori $8, $8, 0xFFFF # 0xFFFFFFFF
#fim