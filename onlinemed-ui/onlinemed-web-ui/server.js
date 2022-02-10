//Install express server
const express = require('express');
const path = require('path');
const httpProxy = require('http-proxy');
const apiProxy = httpProxy.createProxyServer();

const app = express();
const backend = 'https://onlinemed.herokuapp.com/';

// Serve only the static files form the dist directory
app.use(express.static(__dirname + '/dist/onlinemed-web-ui'));

app.get('/*', function (req, res) {

  res.sendFile(path.join(__dirname + '/dist/onlinemed-web-ui/index.html'));
});

app.all("/api/*", function (req, res) {
  apiProxy.web(req, res, {target: backend});
});

// Start the app by listening on the default Heroku port
app.listen(process.env.PORT || 8080);
