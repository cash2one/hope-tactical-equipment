var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function (req, res, next) {
    res.render('index', {title: 'Express', body: '<h1>HelloWorld</h1>'});
});
router.get("/admin", function (req, res, next) {
    res.render('admin', {title: 'MotherFucker!' , content:'<h1>Guest</h1>'})
})

module.exports = router;
