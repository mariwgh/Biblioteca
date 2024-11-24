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
('J.K. Rowling'),
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
--idEmprestimo é identity (alguns apagados)
insert into SisBib.Emprestimo
(idLeitor, idExemplar, dataEmprestimo, devolucaoEfetiva, devolucaoPrevista)
values
(2, 13, '11-11-2024', null, '21-11-2024')

delete SisBib.Emprestimo where idEmprestimo = 10


select * from SisBib.Exemplar
--ser preenchida no programa
--idExemplar é identity (alguns apagados)
insert into SisBib.Exemplar 
(idBiblioteca, codLivro, numeroExemplar)
values 
(7, 'JP1000', 1);

<<<<<<< HEAD
=======
--OPERACOES DE EXEMPLAR
delete SisBib.Exemplar where idExemplar = 12
select * from SisBib.Exemplar where idExemplar = 12
update SisBib.Exemplar set numeroExemplar = 12 where numeroExemplar = 12

>>>>>>> 23f3a091a5cf1fb7cf23c725ade9c402babe5c1e

select * from SisBib.Leitor
--idLeitor é identity
insert into SisBib.Leitor
(nome, estaSuspenso)
values
('Maria Oliveira', 'N'),
('Carlos Silva', 'S')


select * from SisBib.Livro
--ser preenchida no programa
insert into SisBib.Livro
(codLivro, titulo, idAutor, idArea)
values
('JP100', 'Jurassic Park', 5, 2)
update SisBib.Livro set codLivro = 'JP1000' where codLivro = 'JP100'


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
			print 'Esse exemplar não pode ser emprestado pois ainda não foi devolvido.'
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
