all: pizzaria.exe

pizzaria.exe: main.o pizza.o ingrediente.o
	gcc -Wall -g -o pizzaria.exe main.o pizza.o ingrediente.o

%.o: %.c pizza.h ingrediente.h
	gcc -Wall -g -c $< -o $@

clean:
	del main.o pizza.o ingrediente.o pizzaria.exe

run: pizzaria.exe
	./pizzaria.exe


