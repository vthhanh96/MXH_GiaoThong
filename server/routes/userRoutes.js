var express = require('express');
var router = express.Router();
var User = require('../models/UserModel');
var jwt = require('jsonwebtoken');
var config = require('../config/main');
var passport = require('passport');
const nodemailer = require('nodemailer');
var voucher_codes = require('voucher-code-generator');

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

router.post('/forgotPassword',function (req,res,next) {

    let transporter = nodemailer.createTransport({
        service:'Gmail',
        auth:{
            user:'netficuit@gmail.com',
            pass:'@14520288Mh'
        }
    });

    var codeGenerates = voucher_codes.generate({
        length: 5,
        count: 1,
        charset: voucher_codes.charset("alphanumeric"),
        prefix: "UIT"
    });

    var data = {
        from: 'NetFicUIT@gmail.com',
        to: req.body.email,
        subject: 'Đặt lại mật khẩu của bạn',
        text: `Mật khẩu mới của bạn là : ${codeGenerates}`
    };

    transporter.sendMail(data,(err,info)=>{
        if(err){
            res.send({
                success: false,
                message: "Gửi thất bại"+ err
            });
        } else {
            res.send({
                success: true,
                message: codeGenerates[0],
            });
        }
    });
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

router.post('/resetPass',function (req,res,next) {

});

module.exports = router;