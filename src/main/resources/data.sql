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
	),
    (
		(SELECT id FROM usuario WHERE email = 'administrador@email.com'),
		(SELECT id FROM permissao WHERE nome = 'USER')
	);