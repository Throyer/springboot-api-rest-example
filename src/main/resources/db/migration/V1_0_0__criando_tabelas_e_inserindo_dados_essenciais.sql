CREATE TABLE IF NOT EXISTS usuario ( 
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(20) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,
    active BIT DEFAULT 1,
    created_at DATETIME DEFAULT sysdate(),
    updated_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE IF NOT EXISTS permissao ( 
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(20) NOT NULL UNIQUE,
    descricao VARCHAR(255),
    active BIT DEFAULT 1,
    created_at DATETIME DEFAULT sysdate(),
    updated_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE IF NOT EXISTS usuario_permissao ( 
    usuario_id BIGINT NOT NULL,
    permissao_id BIGINT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuario (id),
    FOREIGN KEY (permissao_id) REFERENCES permissao (id) 
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

-- PERMISSOES
INSERT INTO permissao
    (nome, active)
VALUES
    ('ADMINISTRADOR',   1),
    ('USER',            1);

-- USUARIO ADMINISTRADOR
INSERT INTO usuario
    (nome, email, senha, active)
VALUES
    ('ADMINISTRADOR', 'administrador@email.com', '$2a$10$L9CP7W3JIKhVz5J/WiMP/.LEqvAMC915HxxON98MFtyJzXbIEnajS', 1);

-- ATRIBUINDO PERMISSOES NO ADMINISTRADOR
INSERT INTO usuario_permissao
    (usuario_id, permissao_id)
VALUES
    (
        (SELECT id FROM usuario WHERE email = 'administrador@email.com'),
        (SELECT id FROM permissao WHERE nome = 'ADMINISTRADOR')
    ),(
        (SELECT id FROM usuario WHERE email = 'administrador@email.com'),
        (SELECT id FROM permissao WHERE nome = 'USER')
    );