var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var PostSchema = new Schema({
    creator: {type: mongoose.Schema.Types.ObjectId,  ref: 'User'},
    category: {type: mongoose.Schema.Types.ObjectId,  ref: 'Category'},
    level: {
        type: Number
    },
    isActive: {
        type: Boolean,
        default: true
    },
    content : {
        type : String,
        default: ""
    },
    imageUrl: {
        type: String
    },
    created_date: {
        type : Date,
        default: Date.now()
    },
    modify_date:{
        type: Date,
        default: Date.now()
    },
    latitude:{
      type: Number,
        required: true
    },
    longitude:{
        type: Number,
        required: true
    },
    place: {
      type: String,
      required: true
    },
    comments: [{type: mongoose.Schema.Types.ObjectId, ref: 'Comment'}],
    reaction: [{type: mongoose.Schema.Types.ObjectId, ref: 'Reaction'}],
    like_amount: {
        type: Number,
        default: 0,
    },
    dislike_amount: {
        type: Number,
        default: 0,
    }
}, {
    usePushEach: true,
    versionKey: false
});

module.exports  = mongoose.model('Post', PostSchema);