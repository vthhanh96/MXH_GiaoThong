var mongoose = require('mongoose');
var Schema = mongoose.Schema;
var User = require('./UserModel');

//status_reaction:
//like: 1
//dislike: 2

var ReactionSchema = new Schema({
    creator: {type: mongoose.Schema.Types.ObjectId,  ref: 'User'},
    status_reaction : {
        type : Number,
        require: true
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

module.exports = mongoose.model('Reaction', ReactionSchema);