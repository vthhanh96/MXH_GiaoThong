var express = require('express');
var router = express.Router();
var User = require('../models/UserModel');
var jwt = require('jsonwebtoken');
var config = require('../config/main');
var passport = require('passport');

router.post('/register', function(req, res) {
    if(!req.body.email || !req.body.password) {
        res.json({ success: false, message: 'Please enter email and password.' });
    } else {
        var newUser = new User({
            email: req.body.email,
            password: req.body.password,
            fullName: req.body.fullName,
            avatar_url:req.body.avatar_url
        });

        // Attempt to save the user
        newUser.save(function(err) {
            if (err) {
                return res.json({ success: false, message: 'That email address already exists.'});
            }
            res.json({ success: true, message: 'Successfully created new user.' });
        });
    }
});

router.post('/login', function(req, res) {
    User.findOne({
        email: req.body.email
    }, function(err, user) {
        if (err) throw err;

        if (!user) {
            res.send({ success: false, message: 'Authentication failed. User not found.' });
        } else {
            // Check if password matches
            user.comparePassword(req.body.password, function(err, isMatch) {
                if (isMatch && !err) {
                    // Create token if the password matched and no error was thrown
                    var token = jwt.sign(user.toJSON(), config.secret, {
                        expiresIn: 10080 // in seconds
                    });
                    res.json({ success: true, token: 'JWT ' + token });
                } else {
                    res.send({ success: false, message: 'Authentication failed. Passwords did not match.' });
                }
            });
        }
    });
});

router.get('/me', passport.authenticate('jwt', { session: false }), function(req, res, next) {
    res.json({
        success: true,
        message: "success",
        data: req.user
    })
});

router.use('/:userId', (req, res, next) => {
    User.findById(req.params.userId, (err, user) => {
        if (err)
            res.status(500).send(err);
        else if (user) {
            req.user = user;
            next();
        }
        else {
            res.json({
                success: false,
                data: {},
                message: "user not found"
            });
        }
    });
});

router.get('/:userId',passport.authenticate('jwt', { session: false }), function (req, res, next) {
    res.json({
        success: true,
        data: req.user,
        message: "success"
    });
});


module.exports = router;