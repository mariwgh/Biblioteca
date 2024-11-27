/* Logico_Biblioteca: */

create schema SisBib

CREATE TABLE SisBib.Biblioteca (
    idBiblioteca int PRIMARY KEY Identity,
    nome varchar(50)
);

CREATE TABLE SisBib.Livro (
    codLivro varchar(6) PRIMARY KEY,
    titulo varchar(100),
    idAutor int,
    idArea int

	--CONSTRAINT FK_Livro_2 FOREIGN KEY (idAutor) REFERENCES SisBib.Autor (idAutor) ON DELETE CASCADE,
    --CONSTRAINT FK_Livro_3 FOREIGN KEY (idArea) REFERENCES SisBib.Area (idArea) ON DELETE CASCADE
);

CREATE TABLE SisBib.Leitor (
    idLeitor int PRIMARY KEY Identity,
    nome varchar(50),
    estaSuspenso char(1)
);

CREATE TABLE SisBib.Emprestimo (
    idEmprestimo int PRIMARY KEY Identity,
    idLeitor int,
	idExemplar int,
    dataEmprestimo Date,
	--data devolvida
    devolucaoEfetiva Date,
	--data esperada
    devolucaoPrevista Date

	--CONSTRAINT FK_Emprestimo_1 FOREIGN KEY (idLeitor) REFERENCES SisBib.Leitor (idLeitor) ON DELETE SET NULL,
    --CONSTRAINT FK_Emprestimo_3 FOREIGN KEY (idExemplar) REFERENCES SisBib.Exemplar (idExemplar)
);

CREATE TABLE SisBib.Autor (
    idAutor int PRIMARY KEY Identity,
    nome varchar(50)
);

CREATE TABLE SisBib.Area (
    idArea int PRIMARY KEY,
    nome varchar(50)
);

CREATE TABLE SisBib.Exemplar (
    idExemplar int PRIMARY KEY Identity,
    idBiblioteca int,
    codLivro varchar(6),
    numeroExemplar int

	--CONSTRAINT FK_Exemplar_1 FOREIGN KEY (idBiblioteca) REFERENCES SisBib.Biblioteca (idBiblioteca) ON DELETE CASCADE,
    --CONSTRAINT FK_Exemplar_2 FOREIGN KEY (codLivro) REFERENCES SisBib.Livro (codLivro) ON DELETE SET NULL
);
 
--add foreign keys e conectando 
ALTER TABLE SisBib.Livro ADD CONSTRAINT FK_Livro_2
    FOREIGN KEY (idAutor)
    REFERENCES SisBib.Autor (idAutor)
    ON DELETE CASCADE;
 
ALTER TABLE SisBib.Livro ADD CONSTRAINT FK_Livro_3
    FOREIGN KEY (idArea)
    REFERENCES SisBib.Area (idArea)
    ON DELETE CASCADE;
 
ALTER TABLE SisBib.Emprestimo ADD CONSTRAINT FK_Emprestimo_1
    FOREIGN KEY (idLeitor)
    REFERENCES SisBib.Leitor (idLeitor)
    ON DELETE SET NULL;
 
ALTER TABLE SisBib.Emprestimo ADD CONSTRAINT FK_Emprestimo_3
    FOREIGN KEY (idExemplar)
    REFERENCES SisBib.Exemplar (idExemplar);
 
ALTER TABLE SisBib.Exemplar ADD CONSTRAINT FK_Exemplar_1
    FOREIGN KEY (idBiblioteca)
    REFERENCES SisBib.Biblioteca (idBiblioteca)
    ON DELETE CASCADE;
 
ALTER TABLE SisBib.Exemplar ADD CONSTRAINT FK_Exemplar_2
    FOREIGN KEY (codLivro)
    REFERENCES SisBib.Livro (codLivro)
    ON DELETE SET NULL;


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


select * from SisBib.Emprestimo
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
update SisBib.Emprestimo set devolucaoEfetiva = NULL where idLeitor = 2 and idExemplar = (select idExemplar from SisBib.Exemplar where codLivro = 'JURPK1' and numeroExemplar = 6 and idBiblioteca = 2)
--leitor que atrasou, comando devolucao para chamar sp
select idLeitor from SisBib.Emprestimo where devolucaoPrevista < devolucaoEfetiva and idLeitor = 2 and idExemplar = (select idExemplar from SisBib.Exemplar where codLivro = 'JURPK1' and numeroExemplar = 6 and idBiblioteca = 2)



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


drop view SisBib.atrasos

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

drop trigger SisBib.exemplarDisponivel


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
--NAO RODEI

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

SELECT * FROM SisBib.Exemplar
insert into SisBib.Exemplar 
(idBiblioteca, codLivro, numeroExemplar)
values (2, 'EBJSHW', 3)


--se for mudar o codLivro, mudar em exemplar tb 
--NAO ADIANTA

create trigger SisBib.atualizarCodLivro on SisBib.Livro
for update
as
begin
    if exists (select codLivro from deleted) and exists (select codLivro from inserted)
		begin
			declare @codVelho varchar(6)
			declare @codNovo varchar(6)

			select @codVelho = codLivro from deleted
			select @codNovo = codLivro from inserted
		
			update SisBib.Exemplar set codLivro = @codNovo where codLivro = @codVelho
		end
end

DROP TRIGGER SisBib.atualizarCodLivro

SELECT * FROM SisBib.Livro
update SisBib.Livro set codLivro = 'JURPRK' where codLivro = 'JURPK1'