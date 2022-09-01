drop database if exists Biblioteca;

create database Biblioteca;

use Biblioteca;


CREATE TABLE Obra(
    codigo bigint not NULL auto_increment,
    isbn bigint not NULL,
    titulo varchar(256) not NULL,
    categoria bigint,
    palavraChave varchar(256),
    dataPublicacao date not NULL,
    numEdicao int not NULL,
    editora_id bigint not NULL,
    numPaginas bigint not NULL,
    CONSTRAINT obra_pk PRIMARY KEY (codigo)
);

CREATE TABLE Autor(
    codigo bigint not NULL auto_increment,
    nome varchar(256) not NULL,
    iniciais varchar(30),
    CONSTRAINT autor_pk PRIMARY KEY(codigo)
);


CREATE TABLE RelObraAutor(
    id bigint not NULL auto_increment,
    codigo_autor bigint not NULL,
    codigo_obra bigint not NULL,
    CONSTRAINT obraAutor_pk PRIMARY KEY(id)
);

CREATE TABLE Editora(
    id bigint not NULL auto_increment,
    nome varchar(100) not NULL,
    CONSTRAINT editora_pk PRIMARY KEY(id)
);

CREATE TABLE CategoriaObra(
    codigo int not NULL auto_increment,
    descricao varchar(256),
    maximoDiasEmprestado bigint not null,
    taxaMulta decimal(10,6) not NULL,
    CONSTRAINT codigo_pk PRIMARY KEY (codigo)
);

CREATE TABLE Copia(
    id bigint not NULL auto_increment,
    state varchar(50) not NULL,
    obra_id bigint not NULL,
    CONSTRAINT copia_pk PRIMARY KEY(id)
);

CREATE TABLE Emprestimo(
    id bigint not NULL auto_increment,
    dataEmprestimo date not NULL,
    dataPrevistaDevolucao date not NULL,
    funcionarioResponsavel bigint not NULL,
    leitor bigint not NULL,
    codigoCopia bigint not NULL,
    atrasado boolean not NULL,
    CONSTRAINT emprestimo_pk PRIMARY KEY(id)
);

CREATE TABLE Devolucao(
    id bigint not NULL auto_increment,
    dataDevolucao date not NULL,
    multaTotal decimal(10,6) not NULL,
    codigo_emprestimo bigint not NULL,
    CONSTRAINT devolucao_pk PRIMARY KEY(id)
);

CREATE TABLE Reserva(
    id bigint not NULL auto_increment,
    dataReserva date not NULL,
    dataPrevistaRetirada date not NULL,
    dataPrevistaDevolucao date not NULL,
    funcionarioResponsavel bigint not NULL,
    leitor bigint not NULL,
    copiaReservada bigint not NULL,
    CONSTRAINT emprestimo_pk PRIMARY KEY(id)
);

CREATE TABLE Usuario(
    id bigint not NULL auto_increment,
    nome varchar(50) not NULL,
    telefone varchar(20) not NULL,
    dataNascimento date not null,
    endereco_id bigint,
    role varchar(20) not NULL,
    CONSTRAINT usuario_pk PRIMARY KEY(id)
);

CREATE TABLE Endereco(
    id bigint not NULL auto_increment,
    logradouro varchar(50) not NULL,
    numero int not NULL,
    cep int not NULL,
    cidade varchar(20),
    estado varchar(20),
    CONSTRAINT endereco_pk PRIMARY KEY(id)
);

CREATE TABLE Leitor(
    id bigint not NULL auto_increment,
    idUsuario bigint not null,
    email varchar(50) not NULL,
    categoria_id bigint,
    grupoAcademico boolean not NULL,
    CONSTRAINT leitor_fk FOREIGN KEY (idUsuario) REFERENCES Usario(id),
    CONSTRAINT leitor_pk PRIMARY KEY(id)
);

CREATE TABLE CategoriaLeitor(
    id bigint not NULL auto_increment,
    maximoDiasEmprestimo int not NULL,
    descricao varchar(50),
    CONSTRAINT categoriaLeitor_pk PRIMARY KEY(id)
);
