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
