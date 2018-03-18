var User = require('../models/UserModel');
var config = require('../helpers/config');
var jwt = require('jsonwebtoken');
var bcrypt = require('bcrypt');

// register
exports.register = function(req, res) {

    // get user
    var user = new User(req.body);
    user.hash_password = bcrypt.hashSync(req.body.password, 10);

    // save
    user.save(function (err, newUser) {
        if (err) {
            res.json({"code": false, "message": "Error to save"});
            return
        }
        newUser.hash_password = undefined
        res.json({ message: 'Save ok', data: newUser });
    });
};

// login
exports.login = function(req, res) {

    var username = req.body.username;
    var password = req.body.password;

    // find
    User.findOne({
        'username': username
    }, function(err, user) {
        if (!user) {
            res.json({ error : 'User is not exist'})
        } else if (user &&
            user.comparePassword(password)) {
            var payload = { username: user.username };
            var jwtToken = jwt.sign(payload, config.jwtSecret, { expiresIn: 1 * 30 });
            console.log('jwtToken: ' + jwtToken);
            var jsonResponse = {'access_token': jwtToken, 'refresh_token': "xxxxx-xxx-xx-x"}
            res.json(jsonResponse)
        } else {
            res.json({ error : 'Login Error'})
        }
    })
};

// get current user
exports.users = function(req, res) {
    // find
    User.find({
    }, function(err, users) {
        if (err)
            throw err;
        if (users) {
            var jsonResponse = {'users': users}
            res.json(jsonResponse)
        } else {
            res.send(JSON.stringify({
                error : 'Login Error'
            }))
        }
    })
};