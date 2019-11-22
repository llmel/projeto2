verificarAutenticacao();

function verificarAutenticacao() {
	if (sessionStorage.usuario != undefined) {
  		window.location.href = "./index.html";
	} 
}

function entrar() {
var formulario = new URLSearchParams(new FormData(form_login));
console.log("Chamou o fetch " + formulario);
fetch('/usuarios/logar', {
  method: "POST",
  body: formulario
}).then(function (response) {
  if (response.ok) {

    response.json().then(function (resposta) {

      sessionStorage.usuario = resposta.login;
      verificarAutenticacao();

    });
  } else {
    console.log('Erro de login!');
  }
});

return false;
}






/*
router.post('/entrar', function (req, res, next) {

  banco.conectar().then(() => {
    console.log(`Chegou p/ login: ${JSON.stringify(req.body)}`);
    var login = req.body.login; // depois de .body, use o nome (name) do campo em seu formulário de login
    var senha = req.body.senha; // depois de .body, use o nome (name) do campo em seu formulário de login
    if (login == undefined || senha == undefined) {
      throw new Error(`Dados de login não chegaram completos: ${login} / ${senha}`);
    }
    return banco.sql.query(`select * from usuario where usuario='${login}' and senha='${senha}'`);
  }).then(consulta => {

    console.log(`Usuários encontrados: ${JSON.stringify(consulta.recordset)}`);

    if (consulta.recordset.length==1) {
      res.send(consulta.recordset[0]);
    } else {
      res.sendStatus(404);
    }

  }).catch(err => {

    var erro = `Erro no login: ${err}`;
    console.error(erro);
    res.status(500).send(erro);

  }).finally(() => {
    banco.sql.close();
  });

});
*/