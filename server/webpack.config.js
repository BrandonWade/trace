const path = require('path');

module.exports = {
  entry: "./ui/index.js",
  output: {
    filename: "app.js",
    path: path.resolve(__dirname, "ui/app")
  },
  module: {
    loaders: [
      {
        test: /\.js$/,
        exclude: /(node_modules)/,
        loader: 'babel-loader',
        query: {
          presets: ['env']
        }
      }
    ]
  }
};
