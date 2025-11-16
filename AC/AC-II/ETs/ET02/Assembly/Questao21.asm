#21) - Repita os cálculos anteriores para o seguinte programa em MIPS: 
#	addi $S3, $S2, 396 
#LOOP: 
#	lw $S1, 0($S2) 
#	addi $S1, $S1, 1 
#	sw $S1, 0 ($S2) 
#	addi $s2, $s2, 4 
#	sub $S4, $S3, $S2 
#	bne $S4, $zero, LOOP 

#inicio

.data
array:  .word 0, 1, 2, 3, 4, 5, 6, 7, 8, 9
        .word 10, 11, 12, 13, 14, 15, 16, 17, 18, 19
        .word 20, 21, 22, 23, 24, 25, 26, 27, 28, 29
        .word 30, 31, 32, 33, 34, 35, 36, 37, 38, 39
        .word 40, 41, 42, 43, 44, 45, 46, 47, 48, 49
        .word 50, 51, 52, 53, 54, 55, 56, 57, 58, 59
        .word 60, 61, 62, 63, 64, 65, 66, 67, 68, 69
        .word 70, 71, 72, 73, 74, 75, 76, 77, 78, 79
        .word 80, 81, 82, 83, 84, 85, 86, 87, 88, 89
        .word 90, 91, 92, 93, 94, 95, 96, 97, 98

.text
.globl main
main:
	ori $s2, $zero, 0x1001 # t0 = 0x00001001
	sll $s2, $s2, 16 # t0 = 0x10010000
	addi $s3, $s2, 396
	LOOP: 
		lw $s1, 0($s2) 
		addi $s1, $s1, 1 
		sw $s1, 0 ($s2) 
		addi $s2, $s2, 4 
		sub $s4, $s3, $s2 
		bne $s4, $zero, LOOP 
#fim

#Total: 597

#ALU: 300 - 50%
#JUMP: 0 - 0%
#BRANCH: 99 - 16%
#MEMORY: 198 - 33%
#OTHER: 0 - 0%

#CICLOS DA ALU: 300 * 3 = 900
#CICLOS DE MEMORIA: 99 * 5 = 495
#CICLOS DE DESVIO: 198 * 4 = 792

#CICLOS TOTAIS: 900 + 495 + 792 = 2187

#CPI MEDIO: 2187 / 597 = 3.66
