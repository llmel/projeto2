// não mexa nestas 3 linhas!
var express = require('express');
var router = express.Router();
var banco = require('../app-banco');
// não mexa nessas 3 linhas!

router.post('/logar', (requisicao,resposta) => { // FFCC00
  console.log("Chegou no end-point")
  console.log(requisicao.body)
banco.conectar().then(() => {
    return banco.sql.query(`
        select * from teste where login = '${requisicao.body.login}' and senha = ${requisicao.body.senha}
        `);
  }).then(consulta => {
    console.log(consulta);
    if (consulta.recordset.length == 1) {
        resposta.send(consulta.recordset[0]);
    }
  }).finally(() => {
    banco.sql.close();
  });
  
})



/*
router.get("/teste",function (req,res) {
  console.log("Chegou no end-point")
  res.send();
})
*/


// não mexa nesta linha!
module.exports = router;
