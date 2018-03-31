var express = require('express');
var router = express.Router();
var Post = require('../models/PostModel');
var User = require('../models/UserModel');
var passport = require('passport');

/* POST new bài viết. */
router.post('/', passport.authenticate('jwt', {session: false}), function (req, res, next) {
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
    Post.find({}).populate('comments').limit(100).sort({name: 1}).exec((err, posts) => {
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

router.use('/:postId', (req, res, next) => {
    Post.findById(req.params.postId).populate('creator').populate('comments').exec((err, post) => {
        if (err)
            res.status(500).send(err);
        else if (post) {
            req.post = post;
            next();
        }
        else {
            res.json({
                success: false,
                data: {},
                message: "post not found"
            });
        }
    });
});

router.get('/:postId', function (req, res, next) {
    res.json({
        success: true,
        data: req.post,
        message: "success"
    });
});

router.put('/:postId', passport.authenticate('jwt', {session: false}), function (req, res, next) {
    if (req.body._id)
        delete req.body._id;
    if(req.body.reaction)
        delete req.body.reaction;
    if(req.body.comments)
        delete req.body.comments;
    if(req.body.creator)
        delete req.body.creator;
    if(req.body.dislike_amount)
        delete req.body.dislike_amount;
    if(req.body.like_amount)
        delete req.body.like_amount;
    if(req.body.category)
        delete req.body.category;
    //user is not creator
    if(req.user.id.localeCompare(req.post.creator._id) === 0){
        for (var p in req.body) {
            req.post[p] = req.body[p];
        }
        req.post.modify_date = Date.now();

        req.post.save((err) => {
            if (err)
                res.status(500).send(err);
            else
                res.json({
                    success: true,
                    message: "update post success",
                    data: req.post
                });
        });
    } else {
        res.json({
            success: false,
            message: "You don't have permission"
        })
    }
});

router.delete('/:postId', passport.authenticate('jwt', {session: false}), function (req, res, next) {
    if(req.user.id.localeCompare(req.post.creator._id) === 0){
        req.post.remove((err) => {
            if(err)
                res.status(500).send(err);
            else
                res.json({
                    success: true,
                    message: "delete post success"
                });
        });
    } else {
        res.json({
            success: false,
            message: "You don't have permission"
        })
    }
});

module.exports = router;
