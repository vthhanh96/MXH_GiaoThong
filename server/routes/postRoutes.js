var express = require('express');
var router = express.Router();
var Post = require('../models/PostModel');
/* POST new bài viết. */
router.post('/', (req, res, next) => {
    const newPost = new Post(req.body);

    newPost.save((err) => {
        if (err) {
            res.json({
                result: 'failed',
                data: {},
                message: `error is : ${err}`
            });
        }
        else {
            res.status(201).send(newPost);
        }
    });
});

router.get('/', function (req, res, next) {
    Post.find({}).limit(100).sort({name: 1}).exec((err, posts) => {
        if (err) {
            res.json({
                result: "failed",
                data: [],
                message: `Error is : ${err}`
            });
        } else {
            res.json({
                result: "ok",
                data: posts,
                message: "Lấy danh sách bài viết thành công"
            });
        }
    });
});

module.exports = router;
