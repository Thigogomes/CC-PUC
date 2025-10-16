main:-
    write('Digite um numero: '),nl,
    read(X), nl,
    fatorial(X, R),
    format('O Fatorial de ~w e igual a ~w~n', [X, R]).
    %halt.

fatorial(0, 1). %fato (usado para o caso base)

fatorial(N, R):-
    N > 0,
    N1 is N - 1,
    fatorial(N1, R1),
    R is N * R1.

%:-main. %chamada de diretiva.
