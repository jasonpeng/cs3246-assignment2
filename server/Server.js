
var port       = 3246;
var express    = require("express");
var http       = require('http');
var path       = require('path');
var fs         = require('fs');
var app        = express();
var httpServer = http.createServer(app);
httpServer.listen(port, "0.0.0.0");

app.use(express.bodyParser({
	uploadDir: './uploads'
}));
app.use(express.multipart());
app.use(express.static(__dirname));

    
app.get("/", function(req, res) {
  res.sendfile(__dirname+"/index.html");
});

app.post("/rf", function(req, res){
	console.log(req.body.relevant);
});

app.post("/upload", function(req, res){
	console.log("Uploading");
	var tempPath = req.files.image.path;
	// var targetPath = path.resolve('./uploads/queryImage.jpg');
	var imageName = req.files.image.name;
	console.log(tempPath  + " " + imageName);
	res.send({
		images:[1,3,7,9]
	});
});

 /* serves all the static files */
 app.get(/^(.+)$/, function(req, res){ 
     console.log('static file request : ' + req.params);
     res.sendfile( __dirname + req.params[0]); 
 });

// app.listen(port, function() {
//    console.log("Listening on " + port);
//  });



