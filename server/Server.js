
var port       = 3246;
var express    = require("express");
var http       = require('http');
var path       = require('path');
var fs         = require('fs');
var sys        = require('sys')
var exec       = require('child_process').exec;
var app        = express();
var httpServer = http.createServer(app);
httpServer.listen(port, "0.0.0.0");

var response;

app.use(express.bodyParser({
	uploadDir: './upload'
}));
app.use(express.multipart());
app.use(express.static(__dirname));

    
app.get("/", function(req, res) {
  res.sendfile(__dirname+"/index.html");
});

app.post("/rf", function(req, res){
	console.log(req.body.relevant);
});

function onJavaReturn(error, stdout, stderr) {
	console.log(stdout);
	console.log(error);
	console.log(stderr);
	response.send({
		images: stdout
	});
}

app.post("/searchwithimage", function(req, res){
	response = res;
	console.log("Uploading");
	console.log(req.body);
	if (!(req.files.image == null)){
		var tempPath          = req.files.image.path;
		// var targetPath     = path.resolve('./uploads/queryImage.jpg');
		var imageName         = req.files.image.name;
		console.log(tempPath  + " " + imageName);
		// res.send({
		// 	images:[1,3,7,9]
		// });
		var cmd               = req.body;
		cmd["commandType"]    = "searchWithImage";
		cmd["queryImage"]     = tempPath;
		cmd["numberOfResult"] = 20;
		console.log(JSON.stringify(cmd));
		// resizeImage 
		exec("java -jar -Xmx4g ImageQuery.jar "+ "'"+JSON.stringify(cmd)+"'" , onJavaReturn);	
	}
	else {
		res.send("400", {message: "No image uploaded!"});
	}
	
});

app.post("/searchwithcolor", function(req, res){
	response = res;
	console.log(req.body);
	if(req.body.color != null){
			// resizeImage 
	// exec("java -jar -Xmx4g ImageQuery.jar "+tempPath+" 20", onJavaReturn);
	res.send("200", req.body);
	}
	else {
		res.send("400", {message: "No image uploaded!"});
	}
	
});

 /* serves all the static files */
 app.get(/^(.+)$/, function(req, res){ 
     console.log('static file request : ' + req.params);
     res.sendfile( __dirname + req.params[0]); 
 });

// app.listen(port, function() {
//    console.log("Listening on " + port);
//  });



