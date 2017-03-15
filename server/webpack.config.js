const path = require('path');

module.exports = {
  entry: "./app/index.js",
  output: {
    filename: "app.js",
    path: path.resolve(__dirname, "app/dist")
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
