var mongoose = require('mongoose');
var Schema = mongoose.Schema;
var User = require('./UserModel');

var CommentSchema = new Schema({
    creator: {type: mongoose.Schema.Types.ObjectId,  ref: 'User'},
    content : {
        type : String,
        default: ""
    },
    created_date: {
        type : Date,
        default: Date.now()
    },
    modify_date:{
        type: Date,
        default: Date.now()
    }
}, {
    usePushEach: true,
    versionKey: false
});

module.exports = mongoose.model('Comment', CommentSchema);