var express = require('express');
var router = express.Router();
var Post = require('../models/PostModel');
var User = require('../models/UserModel');
var passport = require('passport');

/* POST new bài viết. */
router.post('/', passport.authenticate('jwt', { session: false }), function(req, res, next) {
    const newPost = new Post(req.body);
    newPost.creator = req.user;

    newPost.save((err) => {
        if (err) {
            res.json({
                success: false,
                data: {},
                message: `error is : ${err}`
            });
        }
        else {
            res.json({
                success: true,
                data: newPost,
                message: "success upload new post"
            })
        }
    });
});

router.get('/', function (req, res, next) {
    Post.find({}).limit(100).sort({name: 1}).exec((err, posts) => {
        if (err) {
            res.json({
                success: false,
                data: [],
                message: `Error is : ${err}`
            });
        } else {
            res.json({
                success: true,
                data: posts,
                message: "success"
            });
        }
    });
});

router.get('/:postId', function (req, res, next) {
    Post.findById(req.params.postId, (err, post) => {
        if(err) {
            res.json({
                success: false,
                data: [],
                message: `Error is: ${err}`
            });
        } else if(post) {
            res.json({
                success: true,
                data: post,
                message: "success"
            });
        } else {
            res.json({
                success: false,
                data: {},
                message: "post not found"
            });
        }
    })
});

module.exports = router;
