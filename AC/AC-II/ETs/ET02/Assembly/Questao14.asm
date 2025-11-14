#14) 
#Considere que a partir da primeira posição livre da memória temos um vetor com 100 elementos. 
#Escrever um programa que ordene esse vetor de acordo com o algoritmo da bolha. Faça o teste colocando 
#um vetor totalmente desordenado e verifique se o algoritmo funciona. 

#inicio

.data
#array: .word 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1
array: .word 14, 13, 12, 11, 10, 9, 8, 20, 19, 18, 17, 16, 15, 7, 6, 5, 4, 3, 2, 1

.text
.globl main
main:
	ori $t0, $zero, 0x1001 # t0 = 0x00001001
	sll $t0, $t0, 16 # t0 = 0x10010000

	ori $s0, $zero, 20 # tamanho
	ori $s1, $s0, 0 # i = tamanho
	ori $s2, $zero, 0 # j = 0

	ori $s3, $zero, 0 # aux = 0

	bolha1:
		subi $s1, $s1, 1 # i--
		ori $s2, $zero, 0 # j = 0
		beq $s1, $zero, fim # if(s1 == 0)
	
		bolha2:
			slt $t1, $s2, $s1 # t1 = (s2 < s1) ? 1 : 0
			beq $t1, $zero, bolha1 # if(t1 == 0)
				
				sll $t2, $s2, 2 # t2 = j*4
				add $t2, $t2, $t0 # t2 = &mem[j]
				lw $t3, 0 ($t2) # t3 = mem[j]
				
				addi $t4, $s2, 1 # t4 = j+1
				sll $t5, $t4, 2 # t5 = (j+1)*4
				add $t5, $t5, $t0 # t5 = &mem[j+1]
				lw $t6, 0 ($t5) # t5 = mem[j+1]
	
				slt $t7, $t3, $t6 # t7 = (t3 < t6) ? 1 : 0
				bne $t7, $zero, inc_j # if(t7 != 0)
					add $s3, $zero, $t6 # aux = V[j+1]
					add $t6, $zero, $t3 # V[j+1] = V[j]
					add $t3, $zero, $s3 # V[j] = aux
					sw $t3, 0 ($t2) # mem[j] = t3
					sw $t6, 0 ($t5) # mem[j+1] = t6
					j bolha2
	inc_j:
		addi $s2, $s2, 1 # j++
		j bolha2
	fim:
#fim
