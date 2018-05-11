var express = require('express');
var router = express.Router();
var Post = require('../models/PostModel');
var User = require('../models/UserModel');
var Category = require('../models/CategoryModel');
var passport = require('passport');

/* POST new bài viết. */
router.post('/', passport.authenticate('jwt', {
    session: false,
    failureRedirect: '/unauthorized'
}), function (req, res, next) {
    if (req.body.category) {
        Category.findById(req.body.category).exec((err, category) => {
            if (err) {
                res.json({
                    success: false,
                    data: {},
                    message: `error is : ${err}`
                });
            }
            else {
                delete req.body.category;
                const newPost = new Post(req.body);
                newPost.creator = req.user;
                if (category) {
                    newPost.category = category;
                }

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
            }
        });
    }


});
router.post('/listUserPost', passport.authenticate('jwt', {
    session: false,
    failureRedirect: '/unauthorized'
}), function (req, res, next) {
    Post.find({creator: req.body.creator}).populate("creator").exec((err, post) => {
        if (err) {
            res.json({
                success: false,
                data: [],
                message: `Error is : ${err}`
            });
        } else {
            res.json({
                success: true,
                data: post,
                message: "success"
            });
        }
    });

});


router.get('/', function (req, res, next) {
    Post.find({}).populate('creator').populate("category").populate("reaction").limit(100).sort({name: 1}).exec((err, posts) => {
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

router.post('/filter', function (req, res, next) {
    Post.find({'category': {$in: req.body.category}}).populate('creator').populate("category").populate("reaction").limit(100).sort({name: 1}).exec((err, posts) => {
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
    Post.findById(req.params.postId).populate('creator').populate('category')
        .populate({
            path: 'comments',
            model: 'Comment',
            populate: {
                path: 'creator',
                model: 'User'
            }
        })
        .exec((err, post) => {
        if (err)
            res.json({
                success: false,
                message: `Error: ${err}`
            });
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

router.put('/:postId', passport.authenticate('jwt', {
    session: false,
    failureRedirect: '/unauthorized'
}), function (req, res, next) {
    if (req.body._id)
        delete req.body._id;
    if (req.body.reaction)
        delete req.body.reaction;
    if (req.body.comments)
        delete req.body.comments;
    if (req.body.creator)
        delete req.body.creator;
    if (req.body.dislike_amount)
        delete req.body.dislike_amount;
    if (req.body.like_amount)
        delete req.body.like_amount;
    //user is not creator
    if (req.user.id.localeCompare(req.post.creator._id) === 0) {
        for (var p in req.body) {
            req.post[p] = req.body[p];
        }
        req.post.modify_date = Date.now();

        req.post.save((err) => {
            if (err)
                res.json({
                    success: false,
                    message: `Error: ${err}`
                });
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

router.delete('/:postId', passport.authenticate('jwt', {
    session: false,
    failureRedirect: '/unauthorized'
}), function (req, res, next) {
    if (req.user.id.localeCompare(req.post.creator._id) === 0) {
        req.post.remove((err) => {
            if (err)
                res.json({
                    success: false,
                    message: `Error: ${err}`
                });
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
