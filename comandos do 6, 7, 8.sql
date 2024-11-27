select * from SisBib.Area

insert into SisBib.Area
(idArea, nome)
values
(1, 'Ficção Fantástica'),
(2, 'Ciência')


select * from SisBib.Autor
--idAutor é identity (alguns apagados)
insert into SisBib.Autor
(nome)
values
('Casey McQuiston'),
('Michael Crichton')


select * from SisBib.Biblioteca
--idBiblioteca é identity (alguns apagados)
insert into SisBib.Biblioteca
(nome)
values
('Biblioteca1'),
('Biblioteca2'),
('Biblioteca Central')


select* from SisBib.Emprestimo
--ser preenchida no programa
--idEmprestimo é identity

insert into SisBib.Emprestimo
(idLeitor, idExemplar, dataEmprestimo, devolucaoEfetiva, devolucaoPrevista)
values
(2, 13, '11-11-2024', null, '21-11-2024')

delete SisBib.Emprestimo where idEmprestimo = 10
--para achar numero exemplar de um id
select * from SisBib.Exemplar where idExemplar = 13 and idBiblioteca = 2
--comando de devolucao
update SisBib.Emprestimo set devolucaoEfetiva = '30-11-2024' where idLeitor = 2 and idExemplar = (select idExemplar from SisBib.Exemplar where codLivro = 'JURPK1' and numeroExemplar = 6 and idBiblioteca = 2)


select * from SisBib.Exemplar
--ser preenchida no programa
--idExemplar é identity (alguns apagados)

insert into SisBib.Exemplar 
(idBiblioteca, codLivro, numeroExemplar)
values 
(7, 'JP1000', 1);

--OPERACOES DE EXEMPLAR
delete SisBib.Exemplar where idExemplar = '23'
update SisBib.Exemplar set numeroExemplar = 12 where numeroExemplar = 12 and idExemplar = 1 and idBiblioteca = 2
select * from SisBib.Exemplar where idBiblioteca = 2 and idExemplar = 20 and codLivro = 'QVCALK'


select * from SisBib.Leitor
--idLeitor é identity

insert into SisBib.Leitor
(nome, estaSuspenso)
values
('Maria Oliveira', 'N'),
('Carlos Silva', 'S')

update SisBib.Leitor set estaSuspenso = 'N' where idLeitor = 2


select * from SisBib.Livro
--ser preenchida no programa

insert into SisBib.Livro
(codLivro, titulo, idAutor, idArea)
values
('JP1000', 'Jurassic Park', 5, 2)



--6.	Criar view para listar os livros em atraso e o valor de multa a ser cobrada de cada leitor. Cada dia de atraso corresponde a R$5,00.

--rotinas que retornam valores ou tabelas
create function SisBib.valorAtraso (@devEfetiva Date, @devPrevista Date)
returns money
as
begin
	declare @valorMulta money

	-- a data q leitor devolveu foi maior que a data q era p devolver, ocorreu um atraso
	if (@devPrevista < @devEfetiva)
		begin
			set @valorMulta = cast((cast(@devEfetiva as datetime) - cast(@devPrevista as datetime)) as int) * 5.00
		end

	else
		set @valorMulta = 0

	return @valorMulta
end

create view SisBib.atrasos 
as
select 
	ex.idExemplar,
	l.titulo, 
	SisBib.valorAtraso(em.devolucaoEfetiva, em.devolucaoPrevista) as 'Valor Multa'
from SisBib.Emprestimo as em
inner join
SisBib.Exemplar as ex on ex.idExemplar = em.idExemplar
inner join
SisBib.Livro as l on l.codLivro = ex.codLivro
where em.devolucaoPrevista < em.devolucaoEfetiva


select * from SisBib.atrasos


--7.	Criar trigger para impedir que um exemplar já emprestado e ainda não devolvido seja emprestado novamente.

create trigger SisBib.exemplarDisponivel on SisBib.Emprestimo
instead of insert
as
begin
	declare @idExemplarRequerido int
	select @idExemplarRequerido = idExemplar from inserted

	-- if foi emprestado (esta na tabela emprestimo) e nao foi devolvido (data efetiva null)
	if exists (select idExemplar from SisBib.Emprestimo where idExemplar = @idExemplarRequerido and devolucaoEfetiva is null)
		begin
			throw 50001, 'Esse exemplar não pode ser emprestado pois ainda não foi devolvido.', 1
		end

	else
		begin
			declare @idLeitor int
			select @idLeitor = idLeitor from inserted

			declare @dataEmprestimo date
			select @dataEmprestimo = dataEmprestimo from inserted

			declare @devolucaoPrevista date
			select @devolucaoPrevista = devolucaoPrevista from inserted

			insert into SisBib.Emprestimo
			(idLeitor, idExemplar, dataEmprestimo, devolucaoEfetiva, devolucaoPrevista)
			values
			(@idLeitor, @idExemplarRequerido, @dataEmprestimo, null, @devolucaoPrevista)
		end
end


insert into SisBib.Emprestimo
(idLeitor, idExemplar, dataEmprestimo, devolucaoEfetiva, devolucaoPrevista)
values
(1, 13, '11-11-2024', null, '21-11-2024')

select * from SisBib.Emprestimo


--8.	Criar stored procedure para atualizar a situação do leitor para suspenso.

select * from SisBib.Leitor
--Ou seja, o atraso na devolução de um livro torna o leitor suspenso.
--modelo pronto p update leitor, mas precisa que algm execute passando como parametro o id do leitor

create proc SisBib.suspenderLeitor
	@idLeitor int
as
begin
	update SisBib.Leitor set estaSuspenso = 'S' where idLeitor = @idLeitor
end

exec SisBib.suspenderLeitor 1

select idLeitor from SisBib.Emprestimo where devolucaoPrevista < devolucaoEfetiva and idLeitor = 2 and idExemplar = (select idExemplar from SisBib.Exemplar where codLivro = 'JURPK1' and numeroExemplar = 6 and idBiblioteca = 2)

select * from SisBib.Livro
select * from SisBib.Emprestimo
update SisBib.Livro set codLivro = 'JURPRK' where codLivro = 'JURPK1'
select * from SisBib.Exemplar where idArea in (select idArea from SisBib.Livro)




-- trigger ADICIONAIS para: 

--um leitor pode ter consigo, simultaneamente, até 5 livros emprestados, no máximo

create trigger SisBib.limitarLivrosLeitor on SisBib.Emprestimo
instead of insert
as
begin
	declare @idLeitor int
    select @idLeitor = idLeitor from inserted

    if (select count(*) from SisBib.Emprestimo where idLeitor = @idLeitor and devolucaoEfetiva is null) >= 5
		begin
			throw 50002, 'O leitor já atingiu o limite de 5 livros emprestados simultaneamente.', 1
		end
    else
		begin
			insert into SisBib.Emprestimo 
			(idLeitor, idExemplar, dataEmprestimo, devolucaoEfetiva, devolucaoPrevista)
			values
			(select idLeitor from inserted, select idExemplar from inserted, select dataEmprestimo from inserted, select devolucaoEfetiva from inserted, select devolucaoPrevista from inserted)
		end
end


--numExemplar n pode se repetir em livro da biblioteca:
--(Um livro pode ter vários exemplares, numerados de 1 a n, que representam, cada um, um livro físico do acervo. 
--ada exemplar pertence fisicamente a uma das bibliotecas da cidade, de maneira que, 
--mesmo que o código do livro e do exemplar possam se repetir pelas diversas bibliotecas, 
--é necessário saber em qual biblioteca o exemplar físico está localizado.)

create trigger SisBib.repeticaoNumeroExemplar on SisBib.Exemplar
instead of insert
as
begin
    declare @idBiblioteca int
	declare @codLivro varchar(6)
	declare @numeroExemplar int

    select @idBiblioteca = idBiblioteca from inserted
	select @codLivro = codLivro from inserted
	select @numeroExemplar = numeroExemplar from inserted

    if exists (select idExemplar from SisBib.Exemplar where idBiblioteca = @idBiblioteca and codLivro = @codLivro and numeroExemplar = @numeroExemplar)
		begin
			throw 50003, 'Já existe um exemplar com este número para este livro na mesma biblioteca.', 1
		end
    else
		begin
			insert into SisBib.Exemplar 
			(idBiblioteca, codLivro, numeroExemplar)
			values
			(@idBiblioteca, @codLivro, @numeroExemplar)
		end
end

select * from SisBib.Exemplar
insert into SisBib.Exemplar 
(idBiblioteca, codLivro, numeroExemplar)
values (2, 'EBJSHW', 3)


--se esta suspenso nao pode emprestar

create trigger SisBib.impedeEmprestimoSuspenso on SisBib.Emprestimo
instead of insert
as
begin
    if exists (select 1 from SisBib.Leitor where idLeitor = (select idLeitor from inserted) and estaSuspenso = 's')
		begin
			throw 50000, 'não é possível realizar o empréstimo. o leitor está suspenso.', 1
		end
    else
		begin
			insert into SisBib.Emprestimo 
			(idLeitor, idExemplar, dataEmprestimo, devolucaoEfetiva, devolucaoPrevista)
			select idLeitor, idExemplar, dataEmprestimo, devolucaoEfetiva, devolucaoPrevista from inserted
		end
end
