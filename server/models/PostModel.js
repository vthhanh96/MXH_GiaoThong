var mongoose = require('mongoose');
var Schema = mongoose.Schema;
var Category = require('./CategoryModel');

var PostSchema = new Schema({
    post_user_id: {
        type: Number,
        required: true
    },
    post_content : {
        type : String,
        default: ""
    },
    created_Date: {
        type : Date,
        default: Date.now()
    },
    modify_Date:{
        type: Date,
        default: Date.now()
    },
    lat:{
      type: Number,
        required: true
    },
    long:{
        type: Number,
        required: true
    },
});

// //setter chuyển chữ thành chữ hoa trước khi đẩy lên database:
// PostSchema.path('name').set((inputString) => {
//     return inputString[0].toUpperCase() + inputString.slice(1);
// });

module.exports  = mongoose.model('Post', PostSchema);