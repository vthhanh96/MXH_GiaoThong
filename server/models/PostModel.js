var mongoose = require('mongoose');
var Schema = mongoose.Schema;
var autoIncrement = require('mongoose-auto-increment-fix');

var PostSchema = new Schema({
    creator: {type: Number,  ref: 'User'},
    category: {type: Number,  ref: 'Category'},
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
    comments: [{type: Number, ref: 'Comment'}],
    reaction: [{type: Number, ref: 'Reaction'}],
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

PostSchema.plugin(autoIncrement.plugin, 'Post');

module.exports  = mongoose.model('Post', PostSchema);