var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var PostSchema = new Schema({
    creator: {type: mongoose.Schema.Types.ObjectId,  ref: 'User'},
    category: [{type: mongoose.Schema.Types.ObjectId,  ref: 'Category'}],
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
    },
    latitude:{
      type: Number,
        required: true
    },
    longitude:{
        type: Number,
        required: true
    },
    comments: [{type: mongoose.Schema.Types.ObjectId,  ref: 'Comment'}]
}, {
    usePushEach: true,
    versionKey: false
});

module.exports  = mongoose.model('Post', PostSchema);