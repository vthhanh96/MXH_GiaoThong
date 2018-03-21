var mongoose = require('mongoose');
var Schema = mongoose.Schema;
var Category = require('./CategoryModel');
var User = require('./UserModel');

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
}, {
    versionKey: false
});

module.exports  = mongoose.model('Post', PostSchema);