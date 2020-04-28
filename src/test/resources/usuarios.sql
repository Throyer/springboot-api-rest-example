-- INSERINDO USUARIOS
INSERT INTO usuario
    (nome, email, senha, active)
VALUES
    ('usuario um',      'usuario_um@email.com',     '$2a$10$L9CP7W3JIKhVz5J/WiMP/.LEqvAMC915HxxON98MFtyJzXbIEnajS', 1),
    ('usuario dois',    'usuario_dois@email.com',   '$2a$10$L9CP7W3JIKhVz5J/WiMP/.LEqvAMC915HxxON98MFtyJzXbIEnajS', 1),
    ('usuario tres',    'usuario_tres@email.com',   '$2a$10$L9CP7W3JIKhVz5J/WiMP/.LEqvAMC915HxxON98MFtyJzXbIEnajS', 1),
    ('usuario quatro',  'usuario_quatro@email.com', '$2a$10$L9CP7W3JIKhVz5J/WiMP/.LEqvAMC915HxxON98MFtyJzXbIEnajS', 1),
    ('usuario cinco',   'usuario_cinco@email.com',  '$2a$10$L9CP7W3JIKhVz5J/WiMP/.LEqvAMC915HxxON98MFtyJzXbIEnajS', 1);

-- ATRIBUINDO PERMISSOES NOS USUARIOS
INSERT INTO usuario_permissao
    (usuario_id, permissao_id)
VALUES
    (
	 	  (SELECT id FROM usuario     WHERE email = 'usuario_um@email.com'),
		  (SELECT id FROM permissao   WHERE nome = 'ADMINISTRADOR')
    ),
    (
		  (SELECT id FROM usuario     WHERE email = 'usuario_dois@email.com'),
		  (SELECT id FROM permissao   WHERE nome = 'USER')
    ),
    (
	 	  (SELECT id FROM usuario     WHERE email = 'usuario_tres@email.com'),
		  (SELECT id FROM permissao   WHERE nome = 'USER')
    ),
    (
	 	  (SELECT id FROM usuario     WHERE email = 'usuario_quatro@email.com'),
	    (SELECT id FROM permissao   WHERE nome = 'ADMINISTRADOR')
    ),
    (
	    (SELECT id FROM usuario     WHERE email = 'usuario_cinco@email.com'),
	    (SELECT id FROM permissao   WHERE nome = 'USER')
    );