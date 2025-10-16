:- dynamic aluno/2.

aluno('Yasmin', 'LP').
aluno('Yasmin', 'AEDS3').
aluno('Bernado', 'AEDS3').
aluno('Marcella', 'LP').
aluno('Felipe', 'C2').
aluno('Vitor', 'LP').
aluno('Felipe', 'LP').
aluno('Heitor', 'C2').
aluno('Heitor', 'AEDS3').
aluno('Heitor','LP').
aluno('Lincoln', 'AEDS2').
professor('Marco', 'LP').
professor('Theldo', 'AEDS1').
professor('Theldo', 'AC1').
professor('Max', 'AEDS2').
diretor('Max', 'ICEI').

sao_alunos_do_professor(A, P) :- professor(P, M), aluno(A, M).

frequenta('Yasmin', 'Puc').
frequenta('Felipe', 'Puc').
funcionario('Marco', 'Puc').
funcionario('Theldo', 'Puc').
funcionario('Theldo', 'UFMG').

alunos_associados(A, F) :- frequenta(A, F).
funcionario_associado(F, U) :- funcionario(F, U).
professor_associado(P, F) :- funcionario_associado(P, F); professor(P, _).
mostrar_alunos:- aluno(Aluno, _), write(Aluno), nl, fail.
